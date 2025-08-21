package org.example.api.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.example.api.dto.LoginDTO;
import org.example.api.dto.RegisterDTO;
import org.example.api.dto.UpdateUserDTO;
import org.example.api.mapper.UserMapper;
import org.example.api.service.UserService;
import org.example.common.entity.User;
import org.example.common.exception.BusinessException;
import org.example.common.util.SecurityUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.example.common.service.AsyncService;

import java.util.Date;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * 用户服务实现类
 */
@Slf4j
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;
    
    @Autowired
    private StringRedisTemplate redisTemplate;
    
    @Autowired
    private AsyncService asyncService;
    
    // 验证码Redis前缀
    private static final String VERIFY_CODE_PREFIX = "verify_code:";
    // 验证码有效期（分钟）
    private static final long VERIFY_CODE_EXPIRE = 5;
    
    // token有效期（小时）
    private static final long TOKEN_EXPIRE = 24;
    
    // token Redis前缀
    private static final String TOKEN_PREFIX = "user_token:";

    @Override
    public User login(LoginDTO loginDTO) {
        if (loginDTO == null || StringUtils.isBlank(loginDTO.getMobile()) || StringUtils.isBlank(loginDTO.getPassword())) {
            throw new BusinessException("手机号或密码不能为空");
        }

        User user = userMapper.selectByMobile(loginDTO.getMobile());
        if (user == null) {
            throw new BusinessException("用户不存在");
        }

        // 加密后比较密码
        String encryptedPassword = SecurityUtil.encryptPassword(loginDTO.getPassword());
        if (!user.getPassword().equals(encryptedPassword)) {
            throw new BusinessException("密码错误");
        }

        if (user.getStatus() != 1) {
            throw new BusinessException("账号已被禁用");
        }

        // 更新登录时间和IP
        Date now = new Date();
        userMapper.updateLoginInfo(user.getId(), now, "127.0.0.1"); // 实际项目中应该获取真实IP
        
        // 更新内存中的对象
        user.setLastLoginTime(now);
        user.setLastLoginIp("127.0.0.1");
        
        // 生成并保存token
        String token = SecurityUtil.generateToken();
        String tokenKey = TOKEN_PREFIX + user.getId();
        redisTemplate.opsForValue().set(tokenKey, token, TOKEN_EXPIRE, TimeUnit.HOURS);
        log.info("用户登录成功，用户ID: {}, 生成token: {}", user.getId(), token);

        return user;
    }

    @Override
    @CacheEvict(value = "userCache", key = "'mobile:' + #registerDTO.mobile")
    public User register(RegisterDTO registerDTO) {
        if (registerDTO == null || StringUtils.isBlank(registerDTO.getMobile()) || 
            StringUtils.isBlank(registerDTO.getPassword()) || StringUtils.isBlank(registerDTO.getConfirmPassword())) {
            throw new BusinessException("注册信息不完整");
        }

        if (!registerDTO.getPassword().equals(registerDTO.getConfirmPassword())) {
            throw new BusinessException("两次密码输入不一致");
        }

        // 检查手机号是否已注册
        User existUser = userMapper.selectByMobile(registerDTO.getMobile());
        if (existUser != null) {
            throw new BusinessException("该手机号已注册");
        }

        // 验证码校验
        if (StringUtils.isNotBlank(registerDTO.getVerifyCode())) {
            boolean verified = verifyCode(registerDTO.getMobile(), registerDTO.getVerifyCode());
            if (!verified) {
                throw new BusinessException("验证码错误");
            }
        }

        // 创建新用户
        User user = new User();
        user.setMobile(registerDTO.getMobile());
        user.setPassword(SecurityUtil.encryptPassword(registerDTO.getPassword())); // 加密存储密码
        user.setUsername(registerDTO.getMobile()); // 默认使用手机号作为用户名
        user.setNickname("用户" + registerDTO.getMobile().substring(7)); // 默认昵称
        user.setStatus(1);
        Date now = new Date();
        user.setCreateTime(now);
        user.setUpdateTime(now);

        // 保存用户到数据库
        userMapper.insert(user);

        return user;
    }

    @Override
    @Cacheable(value = "userCache", key = "'mobile:' + #mobile", unless = "#result == null")
    public User getByMobile(String mobile) {
        if (StringUtils.isBlank(mobile)) {
            return null;
        }
        return userMapper.selectByMobile(mobile);
    }

    @Override
    @Cacheable(value = "verifyCodeCache", key = "'send:' + #mobile", unless = "!#result")
    public boolean sendVerifyCode(String mobile) {
        if (StringUtils.isBlank(mobile)) {
            throw new BusinessException("手机号不能为空");
        }

        // 生成6位随机验证码
        String verifyCode = SecurityUtil.generateVerifyCode();
        
        // 保存验证码到Redis，设置过期时间
        String key = VERIFY_CODE_PREFIX + mobile;
        redisTemplate.opsForValue().set(key, verifyCode, VERIFY_CODE_EXPIRE, TimeUnit.MINUTES);
        
        try {
            // 异步发送验证码
            asyncService.sendVerifyCodeAsync(mobile, verifyCode);
            
            // 记录用户操作日志
            asyncService.logUserActionAsync(null, "SEND_VERIFY_CODE", "手机号: " + mobile);
            
            return true;
        } catch (Exception e) {
            log.error("发送验证码失败: {}", e.getMessage());
            return false;
        }
    }

    @Override
    public boolean verifyCode(String mobile, String code) {
        if (StringUtils.isBlank(mobile) || StringUtils.isBlank(code)) {
            return false;
        }
        
        String key = VERIFY_CODE_PREFIX + mobile;
        String savedCode = redisTemplate.opsForValue().get(key);
        
        if (code.equals(savedCode)) {
            // 验证成功后删除验证码
            redisTemplate.delete(key);
            return true;
        }
        
        return false;
    }

    @Override
    public boolean resetPassword(String mobile, String newPassword, String verifyCode) {
        if (StringUtils.isBlank(mobile) || StringUtils.isBlank(newPassword) || StringUtils.isBlank(verifyCode)) {
            return false;
        }
        
        // 验证码校验
        if (!verifyCode(mobile, verifyCode)) {
            return false;
        }
        
        User user = userMapper.selectByMobile(mobile);
        if (user == null) {
            return false;
        }
        
        // 更新密码
        String encryptedPassword = SecurityUtil.encryptPassword(newPassword);
        int rows = userMapper.updatePassword(user.getId(), encryptedPassword);
        
        return rows > 0;
    }
    
    @Override
    @CacheEvict(value = "userCache", allEntries = true)
    public User updateUserInfo(UpdateUserDTO updateUserDTO) {
        if (updateUserDTO == null) {
            throw new BusinessException("更新信息不能为空");
        }
        
        if (StringUtils.isBlank(updateUserDTO.getToken())) {
            throw new BusinessException("token不能为空");
        }
        
        // 根据token获取用户信息
        User user = getByToken(updateUserDTO.getToken());
        if (user == null) {
            throw new BusinessException("用户不存在或token已过期");
        }
        
        // 更新用户信息
        if (StringUtils.isNotBlank(updateUserDTO.getUsername())) {
            user.setUsername(updateUserDTO.getUsername());
        }
        
        if (StringUtils.isNotBlank(updateUserDTO.getNickname())) {
            user.setNickname(updateUserDTO.getNickname());
        }
        
        if (StringUtils.isNotBlank(updateUserDTO.getAvatar())) {
            user.setAvatar(updateUserDTO.getAvatar());
        }
        
        if (StringUtils.isNotBlank(updateUserDTO.getMobile())) {
            // 检查手机号是否已被其他用户使用
            User existUser = userMapper.selectByMobile(updateUserDTO.getMobile());
            if (existUser != null && !existUser.getId().equals(user.getId())) {
                throw new BusinessException("该手机号已被其他用户使用");
            }
            user.setMobile(updateUserDTO.getMobile());
        }
        
        if (StringUtils.isNotBlank(updateUserDTO.getGender())) {
            user.setGender(updateUserDTO.getGender());
        }
        
        if (StringUtils.isNotBlank(updateUserDTO.getBirthday())) {
            user.setBirthday(updateUserDTO.getBirthday());
        }
        
        if (StringUtils.isNotBlank(updateUserDTO.getEmail())) {
            user.setEmail(updateUserDTO.getEmail());
        }
        
        if (StringUtils.isNotBlank(updateUserDTO.getAddress())) {
            user.setAddress(updateUserDTO.getAddress());
        }
        
        if (StringUtils.isNotBlank(updateUserDTO.getBio())) {
            user.setBio(updateUserDTO.getBio());
        }
        
        user.setUpdateTime(new Date());
        
        // 更新数据库
        userMapper.update(user);
        
        return user;
    }
    
    @Override
    public boolean changePassword(String mobile, String oldPassword, String newPassword) {
        if (StringUtils.isBlank(mobile) || StringUtils.isBlank(oldPassword) || StringUtils.isBlank(newPassword)) {
            return false;
        }
        
        User user = userMapper.selectByMobile(mobile);
        if (user == null) {
            return false;
        }
        
        // 验证旧密码
        String encryptedOldPassword = SecurityUtil.encryptPassword(oldPassword);
        if (!user.getPassword().equals(encryptedOldPassword)) {
            return false;
        }
        
        // 更新密码
        String encryptedNewPassword = SecurityUtil.encryptPassword(newPassword);
        int rows = userMapper.updatePassword(user.getId(), encryptedNewPassword);
        
        return rows > 0;
    }
    
    @Override
    public String getByTokenKey(String tokenKey) {
        if (StringUtils.isBlank(tokenKey)) {
            return null;
        }
        
        try {
            return redisTemplate.opsForValue().get(tokenKey);
        } catch (Exception e) {
            log.error("根据tokenKey获取token值异常", e);
            return null;
        }
    }
    
    @Override
    @Cacheable(value = "userCache", key = "'token:' + #token", unless = "#result == null")
    public User getByToken(String token) {
        if (StringUtils.isBlank(token)) {
            return null;
        }
        
        // 根据token从Redis中查找用户信息
        try {
            // 获取所有token keys
            Set<String> keys = redisTemplate.keys(TOKEN_PREFIX + "*");
            if (keys == null || keys.isEmpty()) {
                log.warn("Redis中没有找到任何token");
                return null;
            }
            
            // 查找匹配的token
            for (String key : keys) {
                String savedToken = redisTemplate.opsForValue().get(key);
                if (token.equals(savedToken)) {
                    // 从key中提取用户ID
                    String userIdStr = key.substring(TOKEN_PREFIX.length());
                    try {
                        Long userId = Long.parseLong(userIdStr);
                        return userMapper.selectById(userId);
                    } catch (NumberFormatException e) {
                        log.error("无法解析用户ID: {}", userIdStr, e);
                        break;
                    }
                }
            }
        } catch (Exception e) {
            log.error("根据token获取用户信息异常", e);
        }
        
        return null;
    }
    
    @Override
    @CacheEvict(value = "userCache", key = "'token:' + #token")
    public boolean logout(String token) {
        if (StringUtils.isBlank(token)) {
            return false;
        }
        
        // 从Redis中删除token
        try {
            // 查找匹配的token并删除
            Set<String> keys = redisTemplate.keys(TOKEN_PREFIX + "*");
            if (keys != null && !keys.isEmpty()) {
                for (String key : keys) {
                    String savedToken = redisTemplate.opsForValue().get(key);
                    if (token.equals(savedToken)) {
                        redisTemplate.delete(key);
                        log.info("用户登出成功，token已删除: {}", token);
                        return true;
                    }
                }
            }
            log.warn("未找到匹配的token: {}", token);
            return false;
        } catch (Exception e) {
            log.error("用户登出异常", e);
            return false;
        }
    }
}



