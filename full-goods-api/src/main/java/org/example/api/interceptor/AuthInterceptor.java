package org.example.api.interceptor;

import lombok.extern.slf4j.Slf4j;
import org.example.api.context.UserContext;
import org.example.api.service.UserService;
import org.example.common.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 认证拦截器
 * 从请求头中获取token并解析用户信息
 */
@Slf4j
@Component
public class AuthInterceptor implements HandlerInterceptor {

    @Autowired
    private UserService userService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // 从请求头中获取token
        String token = getTokenFromRequest(request);
        
        if (StringUtils.hasText(token)) {
            try {
                // 根据token获取用户信息
                User user = userService.getByToken(token);
                if (user != null) {
                    // 将用户ID存储到ThreadLocal中
                    UserContext.setCurrentUserId(user.getId());
                    log.debug("用户认证成功，用户ID: {}", user.getId());
                } else {
                    log.warn("Token无效或已过期: {}", token);
                }
            } catch (Exception e) {
                log.error("解析token异常: {}", token, e);
            }
        }
        
        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        // 清理ThreadLocal，避免内存泄漏
        UserContext.clear();
    }

    /**
     * 从请求中获取token
     * 支持从Authorization头部和token参数中获取
     */
    private String getTokenFromRequest(HttpServletRequest request) {
        // 1. 从Authorization头部获取
        String authHeader = request.getHeader("Authorization");
        if (StringUtils.hasText(authHeader) && authHeader.startsWith("Bearer ")) {
            return authHeader.substring(7);
        }
        
        // 2. 从token参数获取
        String tokenParam = request.getParameter("token");
        if (StringUtils.hasText(tokenParam)) {
            return tokenParam;
        }
        
        // 3. 从自定义头部获取
        String tokenHeader = request.getHeader("X-Auth-Token");
        if (StringUtils.hasText(tokenHeader)) {
            return tokenHeader;
        }
        
        return null;
    }
}