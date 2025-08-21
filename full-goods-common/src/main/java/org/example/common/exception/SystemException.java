package org.example.common.exception;

/**
 * 系统异常类
 * 用于处理系统级别的异常，如配置错误、资源不可用等
 * 
 * @author system
 * @since 1.0.0
 */
public class SystemException extends RuntimeException {
    
    private static final long serialVersionUID = 1L;
    
    /**
     * 错误码
     */
    private Integer code;
    
    /**
     * 错误信息
     */
    private String message;
    
    /**
     * 构造函数
     */
    public SystemException() {
        super();
    }
    
    /**
     * 构造函数
     * 
     * @param message 错误信息
     */
    public SystemException(String message) {
        super(message);
        this.message = message;
        this.code = 500;
    }
    
    /**
     * 构造函数
     * 
     * @param code 错误码
     * @param message 错误信息
     */
    public SystemException(Integer code, String message) {
        super(message);
        this.code = code;
        this.message = message;
    }
    
    /**
     * 构造函数
     * 
     * @param message 错误信息
     * @param cause 原因
     */
    public SystemException(String message, Throwable cause) {
        super(message, cause);
        this.message = message;
        this.code = 500;
    }
    
    /**
     * 构造函数
     * 
     * @param code 错误码
     * @param message 错误信息
     * @param cause 原因
     */
    public SystemException(Integer code, String message, Throwable cause) {
        super(message, cause);
        this.code = code;
        this.message = message;
    }
    
    /**
     * 获取错误码
     * 
     * @return 错误码
     */
    public Integer getCode() {
        return code;
    }
    
    /**
     * 设置错误码
     * 
     * @param code 错误码
     */
    public void setCode(Integer code) {
        this.code = code;
    }
    
    /**
     * 获取错误信息
     * 
     * @return 错误信息
     */
    @Override
    public String getMessage() {
        return message;
    }
    
    /**
     * 设置错误信息
     * 
     * @param message 错误信息
     */
    public void setMessage(String message) {
        this.message = message;
    }
    
    /**
     * 创建系统异常
     * 
     * @param message 错误信息
     * @return SystemException
     */
    public static SystemException of(String message) {
        return new SystemException(message);
    }
    
    /**
     * 创建系统异常
     * 
     * @param code 错误码
     * @param message 错误信息
     * @return SystemException
     */
    public static SystemException of(Integer code, String message) {
        return new SystemException(code, message);
    }
    
    /**
     * 创建系统异常
     * 
     * @param message 错误信息
     * @param cause 原因
     * @return SystemException
     */
    public static SystemException of(String message, Throwable cause) {
        return new SystemException(message, cause);
    }
    
    /**
     * 创建系统异常
     * 
     * @param code 错误码
     * @param message 错误信息
     * @param cause 原因
     * @return SystemException
     */
    public static SystemException of(Integer code, String message, Throwable cause) {
        return new SystemException(code, message, cause);
    }
}