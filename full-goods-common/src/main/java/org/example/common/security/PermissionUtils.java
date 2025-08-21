package org.example.common.security;

import org.springframework.util.StringUtils;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 权限管理工具类
 * 提供权限验证、角色管理等功能
 * 
 * @author system
 * @since 1.0.0
 */
public class PermissionUtils {
    
    /**
     * 权限分隔符
     */
    private static final String PERMISSION_SEPARATOR = ",";
    
    /**
     * 角色分隔符
     */
    private static final String ROLE_SEPARATOR = ",";
    
    /**
     * 权限层级分隔符
     */
    private static final String HIERARCHY_SEPARATOR = ":";
    
    /**
     * 通配符
     */
    private static final String WILDCARD = "*";
    
    /**
     * 超级管理员角色
     */
    private static final String SUPER_ADMIN_ROLE = "SUPER_ADMIN";
    
    /**
     * 管理员角色
     */
    private static final String ADMIN_ROLE = "ADMIN";
    
    /**
     * 用户角色
     */
    private static final String USER_ROLE = "USER";
    
    /**
     * 访客角色
     */
    private static final String GUEST_ROLE = "GUEST";
    
    /**
     * 角色权重映射
     */
    private static final Map<String, Integer> ROLE_WEIGHTS = new HashMap<>();
    
    static {
        ROLE_WEIGHTS.put(SUPER_ADMIN_ROLE, 1000);
        ROLE_WEIGHTS.put(ADMIN_ROLE, 800);
        ROLE_WEIGHTS.put(USER_ROLE, 500);
        ROLE_WEIGHTS.put(GUEST_ROLE, 100);
    }
    
    /**
     * 检查用户是否具有指定权限
     * 
     * @param userPermissions 用户权限列表
     * @param requiredPermission 需要的权限
     * @return 是否具有权限
     */
    public static boolean hasPermission(String userPermissions, String requiredPermission) {
        if (!StringUtils.hasText(userPermissions) || !StringUtils.hasText(requiredPermission)) {
            return false;
        }
        
        Set<String> permissions = parsePermissions(userPermissions);
        return checkPermission(permissions, requiredPermission);
    }
    
    /**
     * 检查用户是否具有任意一个权限
     * 
     * @param userPermissions 用户权限列表
     * @param requiredPermissions 需要的权限列表
     * @return 是否具有任意一个权限
     */
    public static boolean hasAnyPermission(String userPermissions, String... requiredPermissions) {
        if (!StringUtils.hasText(userPermissions) || requiredPermissions == null || requiredPermissions.length == 0) {
            return false;
        }
        
        Set<String> permissions = parsePermissions(userPermissions);
        for (String requiredPermission : requiredPermissions) {
            if (checkPermission(permissions, requiredPermission)) {
                return true;
            }
        }
        return false;
    }
    
    /**
     * 检查用户是否具有所有权限
     * 
     * @param userPermissions 用户权限列表
     * @param requiredPermissions 需要的权限列表
     * @return 是否具有所有权限
     */
    public static boolean hasAllPermissions(String userPermissions, String... requiredPermissions) {
        if (!StringUtils.hasText(userPermissions) || requiredPermissions == null || requiredPermissions.length == 0) {
            return false;
        }
        
        Set<String> permissions = parsePermissions(userPermissions);
        for (String requiredPermission : requiredPermissions) {
            if (!checkPermission(permissions, requiredPermission)) {
                return false;
            }
        }
        return true;
    }
    
    /**
     * 检查用户是否具有指定角色
     * 
     * @param userRoles 用户角色列表
     * @param requiredRole 需要的角色
     * @return 是否具有角色
     */
    public static boolean hasRole(String userRoles, String requiredRole) {
        if (!StringUtils.hasText(userRoles) || !StringUtils.hasText(requiredRole)) {
            return false;
        }
        
        Set<String> roles = parseRoles(userRoles);
        return roles.contains(requiredRole);
    }
    
    /**
     * 检查用户是否具有任意一个角色
     * 
     * @param userRoles 用户角色列表
     * @param requiredRoles 需要的角色列表
     * @return 是否具有任意一个角色
     */
    public static boolean hasAnyRole(String userRoles, String... requiredRoles) {
        if (!StringUtils.hasText(userRoles) || requiredRoles == null || requiredRoles.length == 0) {
            return false;
        }
        
        Set<String> roles = parseRoles(userRoles);
        for (String requiredRole : requiredRoles) {
            if (roles.contains(requiredRole)) {
                return true;
            }
        }
        return false;
    }
    
    /**
     * 检查用户是否具有所有角色
     * 
     * @param userRoles 用户角色列表
     * @param requiredRoles 需要的角色列表
     * @return 是否具有所有角色
     */
    public static boolean hasAllRoles(String userRoles, String... requiredRoles) {
        if (!StringUtils.hasText(userRoles) || requiredRoles == null || requiredRoles.length == 0) {
            return false;
        }
        
        Set<String> roles = parseRoles(userRoles);
        for (String requiredRole : requiredRoles) {
            if (!roles.contains(requiredRole)) {
                return false;
            }
        }
        return true;
    }
    
    /**
     * 检查用户是否为超级管理员
     * 
     * @param userRoles 用户角色列表
     * @return 是否为超级管理员
     */
    public static boolean isSuperAdmin(String userRoles) {
        return hasRole(userRoles, SUPER_ADMIN_ROLE);
    }
    
    /**
     * 检查用户是否为管理员（包括超级管理员）
     * 
     * @param userRoles 用户角色列表
     * @return 是否为管理员
     */
    public static boolean isAdmin(String userRoles) {
        return hasAnyRole(userRoles, SUPER_ADMIN_ROLE, ADMIN_ROLE);
    }
    
    /**
     * 获取用户最高角色权重
     * 
     * @param userRoles 用户角色列表
     * @return 最高角色权重
     */
    public static int getHighestRoleWeight(String userRoles) {
        if (!StringUtils.hasText(userRoles)) {
            return 0;
        }
        
        Set<String> roles = parseRoles(userRoles);
        return roles.stream()
                   .mapToInt(role -> ROLE_WEIGHTS.getOrDefault(role, 0))
                   .max()
                   .orElse(0);
    }
    
    /**
     * 比较两个用户的角色权重
     * 
     * @param userRoles1 用户1角色列表
     * @param userRoles2 用户2角色列表
     * @return 比较结果（正数表示用户1权重更高，负数表示用户2权重更高，0表示相等）
     */
    public static int compareRoleWeight(String userRoles1, String userRoles2) {
        int weight1 = getHighestRoleWeight(userRoles1);
        int weight2 = getHighestRoleWeight(userRoles2);
        return Integer.compare(weight1, weight2);
    }
    
    /**
     * 检查用户1是否比用户2权限更高
     * 
     * @param userRoles1 用户1角色列表
     * @param userRoles2 用户2角色列表
     * @return 用户1是否比用户2权限更高
     */
    public static boolean hasHigherPermission(String userRoles1, String userRoles2) {
        return compareRoleWeight(userRoles1, userRoles2) > 0;
    }
    
    /**
     * 合并权限列表
     * 
     * @param permissionLists 权限列表数组
     * @return 合并后的权限字符串
     */
    public static String mergePermissions(String... permissionLists) {
        if (permissionLists == null || permissionLists.length == 0) {
            return "";
        }
        
        Set<String> allPermissions = new HashSet<>();
        for (String permissions : permissionLists) {
            if (StringUtils.hasText(permissions)) {
                allPermissions.addAll(parsePermissions(permissions));
            }
        }
        
        return String.join(PERMISSION_SEPARATOR, allPermissions);
    }
    
    /**
     * 合并角色列表
     * 
     * @param roleLists 角色列表数组
     * @return 合并后的角色字符串
     */
    public static String mergeRoles(String... roleLists) {
        if (roleLists == null || roleLists.length == 0) {
            return "";
        }
        
        Set<String> allRoles = new HashSet<>();
        for (String roles : roleLists) {
            if (StringUtils.hasText(roles)) {
                allRoles.addAll(parseRoles(roles));
            }
        }
        
        return String.join(ROLE_SEPARATOR, allRoles);
    }
    
    /**
     * 移除权限
     * 
     * @param userPermissions 用户权限列表
     * @param permissionsToRemove 要移除的权限列表
     * @return 移除后的权限字符串
     */
    public static String removePermissions(String userPermissions, String... permissionsToRemove) {
        if (!StringUtils.hasText(userPermissions) || permissionsToRemove == null || permissionsToRemove.length == 0) {
            return userPermissions;
        }
        
        Set<String> permissions = parsePermissions(userPermissions);
        Set<String> toRemove = new HashSet<>(Arrays.asList(permissionsToRemove));
        
        permissions.removeAll(toRemove);
        
        return String.join(PERMISSION_SEPARATOR, permissions);
    }
    
    /**
     * 移除角色
     * 
     * @param userRoles 用户角色列表
     * @param rolesToRemove 要移除的角色列表
     * @return 移除后的角色字符串
     */
    public static String removeRoles(String userRoles, String... rolesToRemove) {
        if (!StringUtils.hasText(userRoles) || rolesToRemove == null || rolesToRemove.length == 0) {
            return userRoles;
        }
        
        Set<String> roles = parseRoles(userRoles);
        Set<String> toRemove = new HashSet<>(Arrays.asList(rolesToRemove));
        
        roles.removeAll(toRemove);
        
        return String.join(ROLE_SEPARATOR, roles);
    }
    
    /**
     * 解析权限字符串
     * 
     * @param permissions 权限字符串
     * @return 权限集合
     */
    private static Set<String> parsePermissions(String permissions) {
        if (!StringUtils.hasText(permissions)) {
            return new HashSet<>();
        }
        
        return Arrays.stream(permissions.split(PERMISSION_SEPARATOR))
                     .map(String::trim)
                     .filter(StringUtils::hasText)
                     .collect(Collectors.toSet());
    }
    
    /**
     * 解析角色字符串
     * 
     * @param roles 角色字符串
     * @return 角色集合
     */
    private static Set<String> parseRoles(String roles) {
        if (!StringUtils.hasText(roles)) {
            return new HashSet<>();
        }
        
        return Arrays.stream(roles.split(ROLE_SEPARATOR))
                     .map(String::trim)
                     .filter(StringUtils::hasText)
                     .collect(Collectors.toSet());
    }
    
    /**
     * 检查权限
     * 
     * @param userPermissions 用户权限集合
     * @param requiredPermission 需要的权限
     * @return 是否具有权限
     */
    private static boolean checkPermission(Set<String> userPermissions, String requiredPermission) {
        if (userPermissions.isEmpty() || !StringUtils.hasText(requiredPermission)) {
            return false;
        }
        
        // 直接匹配
        if (userPermissions.contains(requiredPermission)) {
            return true;
        }
        
        // 通配符匹配
        if (userPermissions.contains(WILDCARD)) {
            return true;
        }
        
        // 层级权限匹配
        for (String permission : userPermissions) {
            if (matchesHierarchicalPermission(permission, requiredPermission)) {
                return true;
            }
        }
        
        return false;
    }
    
    /**
     * 匹配层级权限
     * 
     * @param userPermission 用户权限
     * @param requiredPermission 需要的权限
     * @return 是否匹配
     */
    private static boolean matchesHierarchicalPermission(String userPermission, String requiredPermission) {
        if (!StringUtils.hasText(userPermission) || !StringUtils.hasText(requiredPermission)) {
            return false;
        }
        
        // 如果用户权限以通配符结尾，检查是否为层级匹配
        if (userPermission.endsWith(HIERARCHY_SEPARATOR + WILDCARD)) {
            String prefix = userPermission.substring(0, userPermission.length() - 2);
            return requiredPermission.startsWith(prefix + HIERARCHY_SEPARATOR);
        }
        
        // 如果用户权限以通配符结尾（不带层级分隔符），检查前缀匹配
        if (userPermission.endsWith(WILDCARD)) {
            String prefix = userPermission.substring(0, userPermission.length() - 1);
            return requiredPermission.startsWith(prefix);
        }
        
        return false;
    }
    
    /**
     * 获取权限的层级深度
     * 
     * @param permission 权限
     * @return 层级深度
     */
    public static int getPermissionDepth(String permission) {
        if (!StringUtils.hasText(permission)) {
            return 0;
        }
        
        return (int) permission.chars().filter(ch -> ch == HIERARCHY_SEPARATOR.charAt(0)).count() + 1;
    }
    
    /**
     * 获取权限的父权限
     * 
     * @param permission 权限
     * @return 父权限
     */
    public static String getParentPermission(String permission) {
        if (!StringUtils.hasText(permission)) {
            return null;
        }
        
        int lastIndex = permission.lastIndexOf(HIERARCHY_SEPARATOR);
        if (lastIndex > 0) {
            return permission.substring(0, lastIndex);
        }
        
        return null;
    }
    
    /**
     * 检查权限是否为子权限
     * 
     * @param parentPermission 父权限
     * @param childPermission 子权限
     * @return 是否为子权限
     */
    public static boolean isChildPermission(String parentPermission, String childPermission) {
        if (!StringUtils.hasText(parentPermission) || !StringUtils.hasText(childPermission)) {
            return false;
        }
        
        return childPermission.startsWith(parentPermission + HIERARCHY_SEPARATOR);
    }
    
    /**
     * 获取所有子权限
     * 
     * @param permissions 权限列表
     * @param parentPermission 父权限
     * @return 子权限列表
     */
    public static List<String> getChildPermissions(String permissions, String parentPermission) {
        if (!StringUtils.hasText(permissions) || !StringUtils.hasText(parentPermission)) {
            return new ArrayList<>();
        }
        
        Set<String> permissionSet = parsePermissions(permissions);
        return permissionSet.stream()
                           .filter(permission -> isChildPermission(parentPermission, permission))
                           .collect(Collectors.toList());
    }
}