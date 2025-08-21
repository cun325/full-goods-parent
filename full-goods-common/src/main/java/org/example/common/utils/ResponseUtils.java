package org.example.common.utils;

import org.example.common.response.Result;
import org.springframework.http.ResponseEntity;

import java.util.Map;
import java.util.HashMap;

/**
 * 响应工具类
 * 提供统一的响应格式处理
 */
public class ResponseUtils {

    /**
     * 成功响应
     */
    public static ResponseEntity<Map<String, Object>> success(Object data) {
        return success("操作成功", data);
    }

    /**
     * 成功响应带消息
     */
    public static ResponseEntity<Map<String, Object>> success(String message, Object data) {
        Map<String, Object> response = new HashMap<>();
        response.put("code", 200);
        response.put("message", message);
        response.put("data", data);
        return ResponseEntity.ok(response);
    }

    /**
     * 失败响应
     */
    public static ResponseEntity<Map<String, Object>> error(String message) {
        return error(500, message);
    }

    /**
     * 失败响应带状态码
     */
    public static ResponseEntity<Map<String, Object>> error(int code, String message) {
        Map<String, Object> response = new HashMap<>();
        response.put("code", code);
        response.put("message", message);
        response.put("data", null);
        return ResponseEntity.ok(response);
    }

    /**
     * 参数错误响应
     */
    public static ResponseEntity<Map<String, Object>> badRequest(String message) {
        return error(400, message);
    }

    /**
     * 未授权响应
     */
    public static ResponseEntity<Map<String, Object>> unauthorized(String message) {
        return error(401, message);
    }

    /**
     * 禁止访问响应
     */
    public static ResponseEntity<Map<String, Object>> forbidden(String message) {
        return error(403, message);
    }

    /**
     * 资源未找到响应
     */
    public static ResponseEntity<Map<String, Object>> notFound(String message) {
        return error(404, message);
    }

    /**
     * 将Result转换为ResponseEntity
     */
    public static ResponseEntity<Map<String, Object>> fromResult(Result<?> result) {
        Map<String, Object> response = new HashMap<>();
        response.put("code", result.getCode());
        response.put("message", result.getMessage());
        response.put("data", result.getData());
        return ResponseEntity.ok(response);
    }
}