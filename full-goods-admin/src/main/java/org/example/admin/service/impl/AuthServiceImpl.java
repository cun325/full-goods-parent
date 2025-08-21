package org.example.admin.service.impl;

import org.example.admin.service.AuthService;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

/**
 * 认证服务实现类
 */
@Service
public class AuthServiceImpl implements AuthService {

    // 存储验证码的内存缓存（实际项目中应使用Redis）
    private final Map<String, String> verificationCodes = new ConcurrentHashMap<>();
    
    // 存储验证码发送时间
    private final Map<String, Long> codeTimestamps = new ConcurrentHashMap<>();
    
    // 存储有效的token（实际项目中应使用Redis）
    private final Map<String, String> validTokens = new ConcurrentHashMap<>();
    
    // 验证码有效期（5分钟）
    private static final long CODE_EXPIRE_TIME = 5 * 60 * 1000;
    
    // Token有效期（24小时）
    private static final long TOKEN_EXPIRE_TIME = 24 * 60 * 60 * 1000;

    @Override
    public boolean sendVerificationCode(String phone) {
        try {
            // 验证手机号格式
            if (!isValidPhone(phone)) {
                return false;
            }
            
            // 检查发送频率（60秒内只能发送一次）
            Long lastSendTime = codeTimestamps.get(phone);
            if (lastSendTime != null && System.currentTimeMillis() - lastSendTime < 60000) {
                return false;
            }
            
            // 生成6位随机验证码
            String code = generateVerificationCode();
            
            // 存储验证码和时间戳
            verificationCodes.put(phone, code);
            codeTimestamps.put(phone, System.currentTimeMillis());
            
            // 模拟发送短信（实际项目中调用短信服务）
            System.out.println("发送验证码到手机号: " + phone + ", 验证码: " + code);
            
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public Map<String, Object> loginWithPhoneCode(String phone, String code) {
        try {
            // 验证验证码
            if (!verifyCode(phone, code)) {
                return null;
            }
            
            // 生成token
            String token = generateToken(phone);
            
            // 存储token
            validTokens.put(token, phone);
            
            // 清除已使用的验证码
            verificationCodes.remove(phone);
            codeTimestamps.remove(phone);
            
            // 构造返回结果
            Map<String, Object> result = new HashMap<>();
            result.put("token", token);
            result.put("phone", phone);
            result.put("loginTime", new Date());
            result.put("expiresIn", TOKEN_EXPIRE_TIME / 1000); // 秒
            
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public Map<String, Object> verifyToken(String token) {
        try {
            String phone = validTokens.get(token);
            if (phone == null) {
                return null;
            }
            
            // 构造用户信息
            Map<String, Object> userInfo = new HashMap<>();
            userInfo.put("phone", phone);
            userInfo.put("isAdmin", true);
            userInfo.put("permissions", Arrays.asList("product:read", "product:write", "order:read", "order:write", "user:read", "user:write"));
            
            return userInfo;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public boolean logout(String token) {
        try {
            validTokens.remove(token);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public Map<String, Object> getCurrentUserInfo(String token) {
        return verifyToken(token);
    }

    @Override
    public boolean verifyCode(String phone, String code) {
        try {
            String storedCode = verificationCodes.get(phone);
            Long timestamp = codeTimestamps.get(phone);
            
            if (storedCode == null || timestamp == null) {
                return false;
            }
            
            // 检查验证码是否过期
            if (System.currentTimeMillis() - timestamp > CODE_EXPIRE_TIME) {
                verificationCodes.remove(phone);
                codeTimestamps.remove(phone);
                return false;
            }
            
            // 验证验证码
            return storedCode.equals(code);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public String generateToken(String phone) {
        // 简单的token生成（实际项目中应使用JWT）
        String timestamp = String.valueOf(System.currentTimeMillis());
        String random = UUID.randomUUID().toString().replace("-", "");
        return "admin_" + phone + "_" + timestamp + "_" + random;
    }

    @Override
    public String parseToken(String token) {
        try {
            if (token.startsWith("admin_")) {
                String[] parts = token.split("_");
                if (parts.length >= 2) {
                    return parts[1]; // 返回手机号
                }
            }
            return null;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    
    /**
     * 验证手机号格式
     */
    private boolean isValidPhone(String phone) {
        if (phone == null || phone.length() != 11) {
            return false;
        }
        return phone.matches("^1[3-9]\\d{9}$");
    }
    
    /**
     * 生成6位验证码
     */
    private String generateVerificationCode() {
        Random random = new Random();
        return String.format("%06d", random.nextInt(1000000));
    }
    
    /**
     * 定期清理过期的验证码和token（实际项目中应使用定时任务）
     */
    public void cleanExpiredData() {
        long currentTime = System.currentTimeMillis();
        
        // 清理过期验证码
        codeTimestamps.entrySet().removeIf(entry -> {
            if (currentTime - entry.getValue() > CODE_EXPIRE_TIME) {
                verificationCodes.remove(entry.getKey());
                return true;
            }
            return false;
        });
    }
}