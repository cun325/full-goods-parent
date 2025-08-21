package org.example.admin.service;

import java.util.Map;

/**
 * 认证服务接口
 * 处理登录、验证码等认证相关业务逻辑
 */
public interface AuthService {

    /**
     * 发送手机验证码
     * @param phone 手机号
     * @return 发送是否成功
     */
    boolean sendVerificationCode(String phone);

    /**
     * 手机号验证码登录
     * @param phone 手机号
     * @param code 验证码
     * @return 登录结果，包含token和用户信息
     */
    Map<String, Object> loginWithPhoneCode(String phone, String code);

    /**
     * 验证token有效性
     * @param token JWT token
     * @return 用户信息，如果token无效返回null
     */
    Map<String, Object> verifyToken(String token);

    /**
     * 退出登录
     * @param token JWT token
     * @return 退出是否成功
     */
    boolean logout(String token);

    /**
     * 获取当前登录用户信息
     * @param token JWT token
     * @return 用户信息
     */
    Map<String, Object> getCurrentUserInfo(String token);

    /**
     * 验证验证码是否正确
     * @param phone 手机号
     * @param code 验证码
     * @return 验证是否通过
     */
    boolean verifyCode(String phone, String code);

    /**
     * 生成JWT token
     * @param phone 手机号
     * @return JWT token
     */
    String generateToken(String phone);

    /**
     * 解析JWT token获取手机号
     * @param token JWT token
     * @return 手机号
     */
    String parseToken(String token);
}