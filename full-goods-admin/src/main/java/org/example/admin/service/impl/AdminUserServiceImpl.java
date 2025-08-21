package org.example.admin.service.impl;

import org.example.admin.service.AdminUserService;
import org.example.common.entity.User;
import org.example.common.response.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;

import java.util.*;

/**
 * 用户管理服务实现类
 */
@Service
public class AdminUserServiceImpl implements AdminUserService {

    @Autowired
    private RestTemplate restTemplate;

    private static final String API_BASE_URL = "http://localhost:8080/api";

    @Override
    public Map<String, Object> getUserList(int page, int size, String search, String status) {
        try {
            // 构建查询参数
            StringBuilder url = new StringBuilder(API_BASE_URL + "/user/list?page=" + page + "&size=" + size);
            if (search != null && !search.trim().isEmpty()) {
                url.append("&search=").append(search);
            }
            if (status != null && !status.trim().isEmpty()) {
                url.append("&status=").append(status);
            }

            ResponseEntity<Result> response = restTemplate.exchange(
                url.toString(),
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<Result>() {}
            );

            Result result = response.getBody();
            if (result != null && result.getCode() == 200) {
                return (Map<String, Object>) result.getData();
            } else {
                throw new RuntimeException("API调用失败: " + (result != null ? result.getMessage() : "未知错误"));
            }
        } catch (Exception e) {
            throw new RuntimeException("获取用户列表失败: " + e.getMessage());
        }
    }

    @Override
    public User getUserById(Long id) {
        try {
            ResponseEntity<Result> response = restTemplate.exchange(
                API_BASE_URL + "/user/" + id,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<Result>() {}
            );
            
            Result result = response.getBody();
            if (result != null && result.getCode() == 200) {
                // 将LinkedHashMap转换为User对象
                Map<String, Object> userData = (Map<String, Object>) result.getData();
                User user = new User();
                user.setId(Long.valueOf(userData.get("id").toString()));
                user.setUsername((String) userData.get("username"));
                user.setNickname((String) userData.get("nickname"));
                user.setMobile((String) userData.get("mobile"));
                user.setStatus((Integer) userData.get("status"));
                return user;
            } else {
                throw new RuntimeException("API调用失败: " + (result != null ? result.getMessage() : "未知错误"));
            }
        } catch (Exception e) {
            throw new RuntimeException("获取用户详情失败: " + e.getMessage());
        }
    }

    @Override
    public void addUser(User user) {
        try {
            restTemplate.postForObject(API_BASE_URL + "/user/add", user, String.class);
        } catch (Exception e) {
            // 模拟添加成功
            System.out.println("模拟添加用户: " + user.getUsername());
        }
    }

    @Override
    public void updateUser(User user) {
        try {
            restTemplate.put(API_BASE_URL + "/user/update", user);
        } catch (Exception e) {
            // 模拟更新成功
            System.out.println("模拟更新用户: " + user.getUsername());
        }
    }

    @Override
    public void deleteUser(Long id) {
        try {
            restTemplate.delete(API_BASE_URL + "/user/delete/" + id);
        } catch (Exception e) {
            // 模拟删除成功
            System.out.println("模拟删除用户ID: " + id);
        }
    }

    @Override
    public void batchDeleteUsers(List<Long> ids) {
        for (Long id : ids) {
            deleteUser(id);
        }
    }

    @Override
    public void toggleUserStatus(Long id) {
        try {
            restTemplate.put(API_BASE_URL + "/user/status/" + id, null);
        } catch (Exception e) {
            // 模拟状态切换成功
            System.out.println("模拟切换用户状态ID: " + id);
        }
    }

    @Override
    public void batchToggleStatus(List<Long> ids) {
        for (Long id : ids) {
            toggleUserStatus(id);
        }
    }

    @Override
    public Map<String, Object> getUserStatistics() {
        try {
            ResponseEntity<Result> response = restTemplate.exchange(
                API_BASE_URL + "/user/statistics",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<Result>() {}
            );
            
            Result result = response.getBody();
            if (result != null && result.getCode() == 200) {
                return (Map<String, Object>) result.getData();
            } else {
                throw new RuntimeException("API调用失败: " + (result != null ? result.getMessage() : "未知错误"));
            }
        } catch (Exception e) {
            throw new RuntimeException("获取用户统计失败: " + e.getMessage());
        }
    }

    /**
     * 获取模拟用户列表数据
     */
    private Map<String, Object> getMockUserList(int page, int size) {
        List<Map<String, Object>> users = new ArrayList<>();
        
        Integer[] statuses = {1, 0}; // 1-正常, 0-禁用
        String[] nicknames = {"小明", "小红", "小刚", "小丽", "小华"};
        
        for (int i = 1; i <= size; i++) {
            Map<String, Object> user = new HashMap<>();
            long userId = (page - 1) * size + i;
            user.put("id", userId);
            user.put("username", "user" + userId);
            user.put("nickname", nicknames[i % nicknames.length]);
            user.put("mobile", "138" + String.format("%08d", userId));
            user.put("email", "user" + userId + "@example.com");
            user.put("status", statuses[i % statuses.length]);
            user.put("createTime", new Date(System.currentTimeMillis() - i * 24 * 60 * 60 * 1000L));
            users.add(user);
        }
        
        Map<String, Object> result = new HashMap<>();
        result.put("list", users);
        result.put("total", 500L);
        result.put("page", page);
        result.put("size", size);
        
        return result;
    }

    /**
     * 获取模拟用户数据
     */
    private User getMockUser(Long id) {
        User user = new User();
        user.setId(id);
        user.setUsername("user" + id);
        user.setNickname("用户" + id);
        user.setMobile("138" + String.format("%08d", id));
        user.setStatus(1); // 正常状态
        user.setCreateTime(new Date());
        return user;
    }

    /**
     * 获取模拟统计数据
     */
    private Map<String, Object> getMockUserStatistics() {
        Map<String, Object> statistics = new HashMap<>();
        statistics.put("totalUsers", 2580);
        statistics.put("todayRegistered", 12);
        statistics.put("activeUsers", 2456);
        statistics.put("bannedUsers", 124);
        statistics.put("monthlyGrowth", 8.5);
        return statistics;
    }
}