package org.example.common.exception;

import lombok.extern.slf4j.Slf4j;
import org.example.common.response.Result;
import org.example.common.utils.LogUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.servlet.NoHandlerFoundException;

import javax.servlet.http.HttpServletRequest;
import java.sql.SQLException;
import java.util.stream.Collectors;

/**
 * 全局异常处理器
 * 统一处理应用中的各种异常，提供友好的错误响应
 * 
 * @author system
 * @since 1.0.0
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * 处理参数校验异常 - @Valid 注解校验失败
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Result<String> handleMethodArgumentNotValidException(MethodArgumentNotValidException e, HttpServletRequest request) {
        String requestUri = request.getRequestURI();
        String errorMessage = e.getBindingResult().getFieldErrors().stream()
                .map(fieldError -> fieldError.getField() + ": " + fieldError.getDefaultMessage())
                .collect(Collectors.joining(", "));
        
        LogUtils.businessWarn("参数校验失败 - URI: {}, 错误信息: {}", requestUri, errorMessage);
        return Result.validateFailed("参数校验失败: " + errorMessage);
    }
    
    /**
     * 处理参数绑定异常 - @ModelAttribute 校验失败
     */
    @ExceptionHandler(BindException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Result<String> handleBindException(BindException e, HttpServletRequest request) {
        String requestUri = request.getRequestURI();
        String errorMessage = e.getBindingResult().getFieldErrors().stream()
                .map(fieldError -> fieldError.getField() + ": " + fieldError.getDefaultMessage())
                .collect(Collectors.joining(", "));
        
        LogUtils.businessWarn("参数绑定失败 - URI: {}, 错误信息: {}", requestUri, errorMessage);
        return Result.validateFailed("参数绑定失败: " + errorMessage);
    }
    


    /**
     * 处理业务异常
     */
    @ExceptionHandler(BusinessException.class)
    @ResponseStatus(HttpStatus.OK)
    public Result<String> handleBusinessException(BusinessException e, HttpServletRequest request) {
        String requestUri = request.getRequestURI();
        LogUtils.businessWarn("业务异常 - URI: {}, 错误码: {}, 错误信息: {}", 
                             requestUri, e.getCode(), e.getMessage());
        return Result.failed(e.getMessage(), e.getCode());
    }
    
    /**
     * 处理系统异常
     */
    @ExceptionHandler(SystemException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public Result<String> handleSystemException(SystemException e, HttpServletRequest request) {
        String requestUri = request.getRequestURI();
        LogUtils.businessError("系统异常 - URI: {}, 错误码: {}, 错误信息: {}", 
                              requestUri, e.getCode(), e.getMessage());
        LogUtils.exception("GlobalExceptionHandler", "handleSystemException", e);
        return Result.failed(e.getMessage(), e.getCode());
    }

    /**
     * 处理缺少请求参数异常
     */
    @ExceptionHandler(MissingServletRequestParameterException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Result<String> handleMissingServletRequestParameterException(MissingServletRequestParameterException e, HttpServletRequest request) {
        String requestUri = request.getRequestURI();
        String errorMessage = String.format("缺少必需的请求参数: %s", e.getParameterName());
        
        LogUtils.businessWarn("缺少请求参数 - URI: {}, 参数名: {}", requestUri, e.getParameterName());
        return Result.failed(errorMessage);
    }
    
    /**
     * 处理参数类型不匹配异常
     */
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Result<String> handleMethodArgumentTypeMismatchException(MethodArgumentTypeMismatchException e, HttpServletRequest request) {
        String requestUri = request.getRequestURI();
        String errorMessage = String.format("参数类型不匹配: %s", e.getName());
        
        LogUtils.businessWarn("参数类型不匹配 - URI: {}, 参数名: {}, 期望类型: {}, 实际值: {}", 
                             requestUri, e.getName(), e.getRequiredType(), e.getValue());
        return Result.failed(errorMessage);
    }
    
    /**
     * 处理非法参数异常
     */
    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Result<String> handleIllegalArgumentException(IllegalArgumentException e, HttpServletRequest request) {
        String requestUri = request.getRequestURI();
        
        LogUtils.businessWarn("非法参数异常 - URI: {}, 错误信息: {}", requestUri, e.getMessage());
        return Result.failed("参数错误: " + e.getMessage());
    }

    /**
     * 处理HTTP请求方法不支持异常
     */
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    @ResponseStatus(HttpStatus.METHOD_NOT_ALLOWED)
    public Result<String> handleHttpRequestMethodNotSupportedException(HttpRequestMethodNotSupportedException e, HttpServletRequest request) {
        String requestUri = request.getRequestURI();
        String method = request.getMethod();
        
        LogUtils.businessWarn("HTTP方法不支持 - URI: {}, 方法: {}, 支持的方法: {}", 
                             requestUri, method, String.join(", ", e.getSupportedMethods()));
        return Result.failed("HTTP方法不支持: " + method);
    }
    
    /**
     * 处理HTTP媒体类型不支持异常
     */
    @ExceptionHandler(HttpMediaTypeNotSupportedException.class)
    @ResponseStatus(HttpStatus.UNSUPPORTED_MEDIA_TYPE)
    public Result<String> handleHttpMediaTypeNotSupportedException(HttpMediaTypeNotSupportedException e, HttpServletRequest request) {
        String requestUri = request.getRequestURI();
        
        LogUtils.businessWarn("媒体类型不支持 - URI: {}, 内容类型: {}", requestUri, e.getContentType());
        return Result.failed("媒体类型不支持: " + e.getContentType());
    }
    
    /**
     * 处理HTTP消息不可读异常
     */
    @ExceptionHandler(HttpMessageNotReadableException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Result<String> handleHttpMessageNotReadableException(HttpMessageNotReadableException e, HttpServletRequest request) {
        String requestUri = request.getRequestURI();
        
        LogUtils.businessWarn("HTTP消息不可读 - URI: {}, 错误信息: {}", requestUri, e.getMessage());
        return Result.failed("请求体格式错误");
    }
    
    /**
     * 处理文件上传大小超限异常
     */
    @ExceptionHandler(MaxUploadSizeExceededException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Result<String> handleMaxUploadSizeExceededException(MaxUploadSizeExceededException e, HttpServletRequest request) {
        String requestUri = request.getRequestURI();
        
        LogUtils.businessWarn("文件上传大小超限 - URI: {}, 最大大小: {}", requestUri, e.getMaxUploadSize());
        return Result.failed("文件上传大小超限");
    }
    
    /**
     * 处理404异常
     */
    @ExceptionHandler(NoHandlerFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Result<String> handleNoHandlerFoundException(NoHandlerFoundException e, HttpServletRequest request) {
        String requestUri = request.getRequestURI();
        String method = request.getMethod();
        
        LogUtils.businessWarn("接口不存在 - 方法: {}, URI: {}", method, requestUri);
        return Result.failed("接口不存在");
    }
    
    /**
     * 处理SQL异常
     */
    @ExceptionHandler(SQLException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public Result<String> handleSQLException(SQLException e, HttpServletRequest request) {
        String requestUri = request.getRequestURI();
        
        LogUtils.businessError("数据库异常 - URI: {}, SQL状态: {}, 错误码: {}", 
                              requestUri, e.getSQLState(), e.getErrorCode());
        LogUtils.exception("GlobalExceptionHandler", "handleSQLException", e);
        
        return Result.failed("数据库操作失败");
    }
    
    /**
     * 处理空指针异常
     */
    @ExceptionHandler(NullPointerException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public Result<String> handleNullPointerException(NullPointerException e, HttpServletRequest request) {
        String requestUri = request.getRequestURI();
        
        LogUtils.businessError("空指针异常 - URI: {}", requestUri);
        LogUtils.exception("GlobalExceptionHandler", "handleNullPointerException", e);
        
        return Result.failed("系统内部错误");
    }
    
    /**
     * 处理非法状态异常
     */
    @ExceptionHandler(IllegalStateException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public Result<String> handleIllegalStateException(IllegalStateException e, HttpServletRequest request) {
        String requestUri = request.getRequestURI();
        
        LogUtils.businessError("非法状态异常 - URI: {}, 错误信息: {}", requestUri, e.getMessage());
        LogUtils.exception("GlobalExceptionHandler", "handleIllegalStateException", e);
        
        return Result.failed("系统状态异常");
    }

    /**
     * 处理运行时异常
     */
    @ExceptionHandler(RuntimeException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public Result<String> handleRuntimeException(RuntimeException e, HttpServletRequest request) {
        String requestUri = request.getRequestURI();
        
        LogUtils.businessError("运行时异常 - URI: {}, 异常类型: {}, 错误信息: {}", 
                              requestUri, e.getClass().getSimpleName(), e.getMessage());
        LogUtils.exception("GlobalExceptionHandler", "handleRuntimeException", e);
        
        return Result.failed("系统运行异常");
    }

    /**
     * 处理其他所有异常
     */
    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public Result<String> handleException(Exception e, HttpServletRequest request) {
        String requestUri = request.getRequestURI();
        
        LogUtils.businessError("未知异常 - URI: {}, 异常类型: {}, 错误信息: {}", 
                              requestUri, e.getClass().getSimpleName(), e.getMessage());
        LogUtils.exception("GlobalExceptionHandler", "handleException", e);
        
        return Result.failed("系统内部错误");
    }
}