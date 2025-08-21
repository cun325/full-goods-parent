package org.example.api.context;

/**
 * 用户上下文
 * 使用ThreadLocal存储当前请求的用户信息
 */
public class UserContext {
    
    private static final ThreadLocal<Long> USER_ID_HOLDER = new ThreadLocal<>();
    
    /**
     * 设置当前用户ID
     * @param userId 用户ID
     */
    public static void setCurrentUserId(Long userId) {
        USER_ID_HOLDER.set(userId);
    }
    
    /**
     * 获取当前用户ID
     * @return 用户ID，如果未设置则返回null
     */
    public static Long getCurrentUserId() {
        return USER_ID_HOLDER.get();
    }
    
    /**
     * 清理当前线程的用户信息
     */
    public static void clear() {
        USER_ID_HOLDER.remove();
    }
    
    /**
     * 检查当前是否有用户登录
     * @return 是否已登录
     */
    public static boolean isLoggedIn() {
        return USER_ID_HOLDER.get() != null;
    }
}