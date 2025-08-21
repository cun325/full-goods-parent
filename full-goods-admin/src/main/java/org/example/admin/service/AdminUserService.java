package org.example.admin.service;

import org.example.common.entity.User;

import java.util.List;
import java.util.Map;

/**
 * 用户管理服务接口
 */
public interface AdminUserService {

    /**
     * 获取用户列表
     */
    Map<String, Object> getUserList(int page, int size, String search, String status);

    /**
     * 根据ID获取用户
     */
    User getUserById(Long id);

    /**
     * 添加用户
     */
    void addUser(User user);

    /**
     * 更新用户
     */
    void updateUser(User user);

    /**
     * 删除用户
     */
    void deleteUser(Long id);

    /**
     * 批量删除用户
     */
    void batchDeleteUsers(List<Long> ids);

    /**
     * 切换用户状态
     */
    void toggleUserStatus(Long id);

    /**
     * 批量切换用户状态
     */
    void batchToggleStatus(List<Long> ids);

    /**
     * 获取用户统计数据
     */
    Map<String, Object> getUserStatistics();
}