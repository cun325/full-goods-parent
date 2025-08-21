package org.example.admin.controller;

import org.example.admin.service.AuthService;
import org.example.common.response.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * 认证控制器
 * 处理登录、验证码等认证相关功能
 */
@RestController
@RequestMapping("/auth")
@CrossOrigin(origins = "*")
public class AuthController {

    @Autowired
    private AuthService authService;

    /**
     * 发送手机验证码
     * @param request 请求体，包含手机号
     * @return 发送结果
     */
    @PostMapping("/send-code")
    public Result<String> sendVerificationCode(@RequestBody Map<String, String> request) {
        try {
            String phone = request.get("phone");
            if (phone == null || phone.trim().isEmpty()) {
                return Result.failed("手机号不能为空");
            }
            
            boolean success = authService.sendVerificationCode(phone);
            if (success) {
                return Result.success("验证码发送成功");
            } else {
                return Result.failed("验证码发送失败");
            }
        } catch (Exception e) {
            return Result.failed("发送验证码时发生错误: " + e.getMessage());
        }
    }

    /**
     * 手机号验证码登录
     * @param loginRequest 登录请求，包含手机号和验证码
     * @return 登录结果，包含token
     */
    @PostMapping("/login")
    public Result<Map<String, Object>> login(@RequestBody Map<String, String> loginRequest) {
        try {
            String phone = loginRequest.get("phone");
            String code = loginRequest.get("code");
            
            if (phone == null || phone.trim().isEmpty()) {
                return Result.failed("手机号不能为空");
            }
            if (code == null || code.trim().isEmpty()) {
                return Result.failed("验证码不能为空");
            }
            
            Map<String, Object> loginResult = authService.loginWithPhoneCode(phone, code);
            if (loginResult != null) {
                return Result.success(loginResult);
            } else {
                return Result.failed("验证码错误或已过期");
            }
        } catch (Exception e) {
            return Result.failed("登录时发生错误: " + e.getMessage());
        }
    }

    /**
     * 验证token有效性
     * @param token JWT token
     * @return 验证结果
     */
    @PostMapping("/verify-token")
    public Result<Map<String, Object>> verifyToken(@RequestParam String token) {
        try {
            Map<String, Object> userInfo = authService.verifyToken(token);
            if (userInfo != null) {
                return Result.success(userInfo);
            } else {
                return Result.failed("Token无效或已过期");
            }
        } catch (Exception e) {
            return Result.failed("验证Token时发生错误: " + e.getMessage());
        }
    }

    /**
     * 退出登录
     * @param token JWT token
     * @return 退出结果
     */
    @PostMapping("/logout")
    public Result<String> logout(@RequestParam String token) {
        try {
            boolean success = authService.logout(token);
            if (success) {
                return Result.success("退出登录成功");
            } else {
                return Result.failed("退出登录失败");
            }
        } catch (Exception e) {
            return Result.failed("退出登录时发生错误: " + e.getMessage());
        }
    }

    /**
     * 获取当前登录用户信息
     * @param token JWT token
     * @return 用户信息
     */
    @GetMapping("/user-info")
    public Result<Map<String, Object>> getUserInfo(@RequestParam String token) {
        try {
            Map<String, Object> userInfo = authService.getCurrentUserInfo(token);
            if (userInfo != null) {
                return Result.success(userInfo);
            } else {
                return Result.failed("获取用户信息失败");
            }
        } catch (Exception e) {
            return Result.failed("获取用户信息时发生错误: " + e.getMessage());
        }
    }
}