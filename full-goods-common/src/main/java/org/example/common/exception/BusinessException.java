package org.example.common.exception;

import lombok.Getter;

/**
 * 业务异常
 */
@Getter
public class BusinessException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    /**
     * 错误码
     */
    private final Integer code;

    /**
     * 错误消息
     */
    private final String message;

    public BusinessException(String message) {
        this(message, 500);
    }

    public BusinessException(String message, Integer code) {
        super(message);
        this.code = code;
        this.message = message;
    }
}