package org.example.admin.controller;

import org.example.admin.service.AdminUserService;
import org.example.common.entity.User;
import org.example.common.response.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 用户管理控制器
 */
@RestController("adminUserController")
@RequestMapping("/admin/user")
@CrossOrigin(origins = "*")
public class UserController {

    @Autowired
    private AdminUserService adminUserService;

    /**
     * 获取用户列表
     */
    @GetMapping("/list")
    public Result<Map<String, Object>> getUserList(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String search,
            @RequestParam(required = false) String status) {
        try {
            Map<String, Object> result = adminUserService.getUserList(page, size, search, status);
            return Result.success(result);
        } catch (Exception e) {
            return Result.failed("获取用户列表失败: " + e.getMessage());
        }
    }

    /**
     * 获取用户详情
     */
    @GetMapping("/{id}")
    public Result<User> getUser(@PathVariable Long id) {
        try {
            User user = adminUserService.getUserById(id);
            if (user != null) {
                return Result.success(user);
            } else {
                return Result.failed("用户不存在");
            }
        } catch (Exception e) {
            return Result.failed("获取用户详情失败: " + e.getMessage());
        }
    }

    /**
     * 新增用户
     */
    @PostMapping("/add")
    public Result<String> addUser(@RequestBody User user) {
        try {
            adminUserService.addUser(user);
            return Result.success("用户添加成功");
        } catch (Exception e) {
            return Result.failed("添加用户失败: " + e.getMessage());
        }
    }

    /**
     * 更新用户
     */
    @PutMapping("/update")
    public Result<String> updateUser(@RequestBody User user) {
        try {
            adminUserService.updateUser(user);
            return Result.success("用户更新成功");
        } catch (Exception e) {
            return Result.failed("更新用户失败: " + e.getMessage());
        }
    }

    /**
     * 删除用户
     */
    @DeleteMapping("/delete/{id}")
    public Result<String> deleteUser(@PathVariable Long id) {
        try {
            adminUserService.deleteUser(id);
            return Result.success("用户删除成功");
        } catch (Exception e) {
            return Result.failed("删除用户失败: " + e.getMessage());
        }
    }

    /**
     * 批量删除用户
     */
    @DeleteMapping("/batch-delete")
    public Result<String> batchDeleteUsers(@RequestBody List<Long> ids) {
        try {
            adminUserService.batchDeleteUsers(ids);
            return Result.success("批量删除成功");
        } catch (Exception e) {
            return Result.failed("批量删除失败: " + e.getMessage());
        }
    }

    /**
     * 切换用户状态（启用/禁用）
     */
    @PutMapping("/toggle-status/{id}")
    public Result<String> toggleUserStatus(@PathVariable Long id) {
        try {
            adminUserService.toggleUserStatus(id);
            return Result.success("状态切换成功");
        } catch (Exception e) {
            return Result.failed("状态切换失败: " + e.getMessage());
        }
    }

    /**
     * 批量切换用户状态
     */
    @PutMapping("/batch-toggle-status")
    public Result<String> batchToggleStatus(@RequestBody List<Long> ids) {
        try {
            adminUserService.batchToggleStatus(ids);
            return Result.success("批量状态切换成功");
        } catch (Exception e) {
            return Result.failed("批量状态切换失败: " + e.getMessage());
        }
    }

    /**
     * 获取用户统计数据
     */
    @GetMapping("/statistics")
    public Result<Map<String, Object>> getUserStatistics() {
        try {
            Map<String, Object> statistics = adminUserService.getUserStatistics();
            return Result.success(statistics);
        } catch (Exception e) {
            return Result.failed("获取统计数据失败: " + e.getMessage());
        }
    }
}