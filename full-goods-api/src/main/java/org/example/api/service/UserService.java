package org.example.api.service;

import org.example.api.dto.LoginDTO;
import org.example.api.dto.RegisterDTO;
import org.example.api.dto.UpdateUserDTO;
import org.example.common.entity.User;

/**
 * 用户服务接口
 */
public interface UserService {

    /**
     * 用户登录
     *
     * @param loginDTO 登录信息
     * @return 用户信息
     */
    User login(LoginDTO loginDTO);

    /**
     * 用户注册
     *
     * @param registerDTO 注册信息
     * @return 用户信息
     */
    User register(RegisterDTO registerDTO);

    /**
     * 根据手机号查询用户
     *
     * @param mobile 手机号
     * @return 用户信息
     */
    User getByMobile(String mobile);

    /**
     * 发送验证码
     *
     * @param mobile 手机号
     * @return 是否发送成功
     */
    boolean sendVerifyCode(String mobile);

    /**
     * 验证验证码
     *
     * @param mobile 手机号
     * @param code   验证码
     * @return 是否验证成功
     */
    boolean verifyCode(String mobile, String code);

    /**
     * 重置密码
     *
     * @param mobile      手机号
     * @param newPassword 新密码
     * @param verifyCode  验证码
     * @return 是否重置成功
     */
    boolean resetPassword(String mobile, String newPassword, String verifyCode);
    
    /**
     * 更新用户信息
     *
     * @param updateUserDTO 用户信息
     * @return 更新后的用户信息
     */
    User updateUserInfo(UpdateUserDTO updateUserDTO);
    
    /**
     * 修改密码
     *
     * @param mobile      手机号
     * @param oldPassword 旧密码
     * @param newPassword 新密码
     * @return 是否修改成功
     */
    boolean changePassword(String mobile, String oldPassword, String newPassword);
    
    /**
     * 根据token获取用户
     *
     * @param token token
     * @return 用户信息
     */
    User getByToken(String token);
    
    /**
     * 根据token key获取token值
     *
     * @param tokenKey token key
     * @return token值
     */
    String getByTokenKey(String tokenKey);
    
    /**
     * 用户登出
     *
     * @param token token
     * @return 是否登出成功
     */
    boolean logout(String token);
}