package org.example.api.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.extern.slf4j.Slf4j;
import org.example.api.dto.ChangePasswordDTO;
import org.example.api.dto.LoginDTO;
import org.example.api.dto.RegisterDTO;
import org.example.api.dto.ResetPasswordDTO;
import org.example.api.dto.TokenDTO;
import org.example.api.dto.UpdateUserDTO;
import org.example.api.dto.VerifyCodeDTO;
import org.example.api.service.UserService;
import org.example.api.vo.UserVO;
import org.example.common.entity.User;
import org.example.common.response.Result;
import org.example.common.util.SecurityUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.Map;
import java.util.List;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.example.api.mapper.UserMapper;
import org.springframework.util.StringUtils;

/**
 * 用户控制器
 */
@Slf4j
@Api(tags = "用户接口")
@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;
    
    @Autowired
    private UserMapper userMapper;

    @ApiOperation(value = "用户登录", notes = "用户通过手机号和密码登录系统")
    @ApiResponses({
        @ApiResponse(code = 200, message = "登录成功"),
        @ApiResponse(code = 400, message = "参数错误"),
        @ApiResponse(code = 401, message = "用户名或密码错误")
    })
    @PostMapping("/login")
    public Result<Map<String, Object>> login(
            @ApiParam(value = "登录信息", required = true) @Valid @RequestBody LoginDTO loginDTO) {
        User user = userService.login(loginDTO);
        
        // 转换为VO对象
        UserVO userVO = new UserVO();
        BeanUtils.copyProperties(user, userVO);
        
        // 获取该用户的token
        String token = getTokenFromRedis(user.getId());
        
        // 返回用户信息和token
        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("token", token);
        resultMap.put("userInfo", userVO);
        
        return Result.success(resultMap);
    }
    
    /**
     * 从Redis中获取用户的token
     * @param userId 用户ID
     * @return token
     */
    private String getTokenFromRedis(Long userId) {
        String tokenKey = "user_token:" + userId;
        return userService.getByTokenKey(tokenKey);
    }

    @ApiOperation(value = "用户注册", notes = "新用户注册账号")
    @ApiResponses({
        @ApiResponse(code = 200, message = "注册成功"),
        @ApiResponse(code = 400, message = "参数错误或手机号已注册")
    })
    @PostMapping("/register")
    public Result<UserVO> register(
            @ApiParam(value = "注册信息", required = true) @Valid @RequestBody RegisterDTO registerDTO) {
        User user = userService.register(registerDTO);
        
        // 转换为VO对象
        UserVO userVO = new UserVO();
        BeanUtils.copyProperties(user, userVO);
        
        return Result.success(userVO);
    }

    @ApiOperation("发送验证码")
    @PostMapping("/sendVerifyCode")
    public Result<String> sendVerifyCode(@Valid @RequestBody VerifyCodeDTO verifyCodeDTO) {
        boolean success = userService.sendVerifyCode(verifyCodeDTO.getMobile());
        if (success) {
            return Result.success("验证码发送成功");
        } else {
            return Result.failed("验证码发送失败");
        }
    }

    @ApiOperation("重置密码")
    @PostMapping("/resetPassword")
    public Result<String> resetPassword(@Valid @RequestBody ResetPasswordDTO resetPasswordDTO) {
        // 验证两次密码是否一致
        if (!resetPasswordDTO.getNewPassword().equals(resetPasswordDTO.getConfirmPassword())) {
            return Result.failed("两次密码输入不一致");
        }
        
        boolean success = userService.resetPassword(
                resetPasswordDTO.getMobile(), 
                resetPasswordDTO.getNewPassword(), 
                resetPasswordDTO.getVerifyCode());
        
        if (success) {
            return Result.success("密码重置成功");
        } else {
            return Result.failed("密码重置失败");
        }
    }

    @ApiOperation("检查手机号是否已注册")
    @PostMapping("/checkMobile")
    public Result<Boolean> checkMobile(@Valid @RequestBody VerifyCodeDTO verifyCodeDTO) {
        User user = userService.getByMobile(verifyCodeDTO.getMobile());
        return Result.success(user != null);
    }
    
    @ApiOperation("获取用户信息")
    @PostMapping("/info")
    public Result<UserVO> getUserInfo(@Valid @RequestBody TokenDTO tokenDTO) {
        // 根据token获取用户信息
        User user = userService.getByToken(tokenDTO.getToken());
        if (user == null) {
            return Result.failed("用户不存在或token已过期");
        }
        
        // 转换为VO对象
        UserVO userVO = new UserVO();
        BeanUtils.copyProperties(user, userVO);
        
        
        return Result.success(userVO);
    }
    
    @ApiOperation("用户登出")
    @PostMapping("/logout")
    public Result<String> logout(@Valid @RequestBody TokenDTO tokenDTO) {
        // 用户登出
        boolean success = userService.logout(tokenDTO.getToken());
        if (success) {
            return Result.success("登出成功");
        } else {
            return Result.failed("token无效或已过期");
        }
    }
    
    @ApiOperation("更新用户信息")
    @PostMapping("/update")
    public Result<UserVO> updateUserInfo(@Valid @RequestBody UpdateUserDTO updateUserDTO) {
        User user = userService.updateUserInfo(updateUserDTO);
        
        // 转换为VO对象
        UserVO userVO = new UserVO();
        BeanUtils.copyProperties(user, userVO);
        
        return Result.success(userVO);
    }
    
    @ApiOperation("修改密码")
    @PostMapping("/changePassword")
    public Result<String> changePassword(@Valid @RequestBody ChangePasswordDTO changePasswordDTO) {
        // 验证两次密码是否一致
        if (!changePasswordDTO.getNewPassword().equals(changePasswordDTO.getConfirmPassword())) {
            return Result.failed("两次密码输入不一致");
        }
        
        // 实际项目中应该根据token获取用户信息
        // 这里简化处理，模拟获取一个固定用户
        // TODO: 根据token获取用户手机号
        boolean success = userService.changePassword(
                "13800138000", 
                changePasswordDTO.getOldPassword(), 
                changePasswordDTO.getNewPassword());
        
        if (success) {
            return Result.success("密码修改成功");
        } else {
            return Result.failed("密码修改失败，请检查旧密码是否正确");
        }
    }
    
    // ==================== Admin接口 ====================
    
    @ApiOperation("管理员获取用户列表（分页）")
    @GetMapping("/list")
    public Result<Map<String, Object>> getUserListForAdmin(
            @ApiParam("页码") @RequestParam(defaultValue = "1") Integer page,
            @ApiParam("每页大小") @RequestParam(defaultValue = "10") Integer size,
            @ApiParam("搜索关键词") @RequestParam(required = false) String search,
            @ApiParam("用户状态") @RequestParam(required = false) String status) {
        try {
            // 设置分页参数
            PageHelper.startPage(page, size);
            
            List<User> users;
            if (StringUtils.hasText(search) || StringUtils.hasText(status)) {
                // 根据条件查询
                users = userMapper.selectByConditionsWithPage(search, status);
            } else {
                // 查询所有
                users = userMapper.selectAllWithPage();
            }
            
            // 获取分页信息
            PageInfo<User> pageInfo = new PageInfo<>(users);
            
            // 构建返回结果
            Map<String, Object> result = new HashMap<>();
            result.put("list", pageInfo.getList());
            result.put("total", pageInfo.getTotal());
            result.put("page", page);
            result.put("size", size);
            
            return Result.success(result);
        } catch (Exception e) {
            log.error("获取用户列表失败", e);
            return Result.failed("获取用户列表失败: " + e.getMessage());
        }
    }
    
    @ApiOperation("管理员获取用户详情")
    @GetMapping("/{id}")
    public Result<User> getUserByIdForAdmin(@PathVariable Long id) {
        try {
            User user = userMapper.selectByPrimaryKey(id);
            if (user != null) {
                return Result.success(user);
            } else {
                return Result.failed("用户不存在");
            }
        } catch (Exception e) {
            log.error("获取用户详情失败", e);
            return Result.failed("获取用户详情失败: " + e.getMessage());
        }
    }
    
    @ApiOperation("管理员更新用户状态")
    @PutMapping("/status/{id}")
    public Result<String> toggleUserStatus(@PathVariable Long id) {
        try {
            User user = userMapper.selectByPrimaryKey(id);
            if (user == null) {
                return Result.failed("用户不存在");
            }
            
            // 切换状态：1-正常，0-禁用
            Integer newStatus = user.getStatus() == 1 ? 0 : 1;
            user.setStatus(newStatus);
            userMapper.updateByPrimaryKey(user);
            
            return Result.success("用户状态更新成功");
        } catch (Exception e) {
            log.error("更新用户状态失败", e);
            return Result.failed("更新用户状态失败: " + e.getMessage());
        }
    }
    
    @ApiOperation("管理员删除用户")
    @DeleteMapping("/delete/{id}")
    public Result<String> deleteUser(@PathVariable Long id) {
        try {
            int result = userMapper.deleteByPrimaryKey(id);
            if (result > 0) {
                return Result.success("用户删除成功");
            } else {
                return Result.failed("用户删除失败，用户不存在");
            }
        } catch (Exception e) {
            log.error("删除用户失败", e);
            return Result.failed("删除用户失败: " + e.getMessage());
        }
    }
    
    @ApiOperation("管理员添加用户")
    @PostMapping("/add")
    public Result<String> addUserForAdmin(@RequestBody User user) {
        try {
            // 检查手机号是否已存在
            User existUser = userMapper.selectByMobile(user.getMobile());
            if (existUser != null) {
                return Result.failed("该手机号已被注册");
            }
            
            // 设置默认值
            if (user.getStatus() == null) {
                user.setStatus(1); // 默认正常状态
            }
            if (user.getPassword() == null || user.getPassword().isEmpty()) {
                user.setPassword(SecurityUtil.encryptPassword("123456")); // 默认密码
            } else {
                user.setPassword(SecurityUtil.encryptPassword(user.getPassword()));
            }
            user.setCreateTime(new java.util.Date());
            user.setUpdateTime(new java.util.Date());
            
            int result = userMapper.insert(user);
            if (result > 0) {
                return Result.success("用户添加成功");
            } else {
                return Result.failed("用户添加失败");
            }
        } catch (Exception e) {
            log.error("添加用户失败", e);
            return Result.failed("添加用户失败: " + e.getMessage());
        }
    }
    
    @ApiOperation("管理员更新用户")
    @PutMapping("/update")
    public Result<String> updateUserForAdmin(@RequestBody User user) {
        try {
            if (user.getId() == null) {
                return Result.failed("用户ID不能为空");
            }
            
            // 检查用户是否存在
            User existUser = userMapper.selectByPrimaryKey(user.getId());
            if (existUser == null) {
                return Result.failed("用户不存在");
            }
            
            // 如果修改了手机号，检查是否与其他用户冲突
            if (user.getMobile() != null && !user.getMobile().equals(existUser.getMobile())) {
                User mobileUser = userMapper.selectByMobile(user.getMobile());
                if (mobileUser != null && !mobileUser.getId().equals(user.getId())) {
                    return Result.failed("该手机号已被其他用户使用");
                }
            }
            
            // 更新时间
            user.setUpdateTime(new java.util.Date());
            
            // 如果密码不为空，则加密
            if (user.getPassword() != null && !user.getPassword().isEmpty()) {
                user.setPassword(SecurityUtil.encryptPassword(user.getPassword()));
            } else {
                // 保持原密码
                user.setPassword(existUser.getPassword());
            }
            
            int result = userMapper.updateByPrimaryKey(user);
            if (result > 0) {
                return Result.success("用户更新成功");
            } else {
                return Result.failed("用户更新失败");
            }
        } catch (Exception e) {
            log.error("更新用户失败", e);
            return Result.failed("更新用户失败: " + e.getMessage());
        }
    }

    @ApiOperation("管理员获取用户统计数据")
    @GetMapping("/statistics")
    public Result<Map<String, Object>> getUserStatistics() {
        try {
            Map<String, Object> statistics = new HashMap<>();
            
            // 总用户数
            Long totalUsers = userMapper.countTotal();
            statistics.put("totalUsers", totalUsers);
            
            // 今日注册用户数
            Long todayRegistered = userMapper.countTodayRegistered();
            statistics.put("todayRegistered", todayRegistered);
            
            // 活跃用户数（状态为1的用户）
            Long activeUsers = userMapper.countByStatus(1);
            statistics.put("activeUsers", activeUsers);
            
            // 禁用用户数（状态为0的用户）
            Long bannedUsers = userMapper.countByStatus(0);
            statistics.put("bannedUsers", bannedUsers);
            
            // 月增长率（简化计算）
            Double monthlyGrowth = userMapper.calculateMonthlyGrowth();
            statistics.put("monthlyGrowth", monthlyGrowth != null ? monthlyGrowth : 0.0);
            
            return Result.success(statistics);
        } catch (Exception e) {
            log.error("获取用户统计数据失败", e);
            return Result.failed("获取用户统计数据失败: " + e.getMessage());
        }
    }
}