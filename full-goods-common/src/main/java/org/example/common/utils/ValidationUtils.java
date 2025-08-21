package org.example.common.utils;

import org.example.common.exception.BusinessException;
import org.springframework.util.StringUtils;

import java.util.Collection;
import java.util.regex.Pattern;

/**
 * 参数校验工具类
 * 提供常用的参数校验方法
 */
public class ValidationUtils {

    private static final Pattern EMAIL_PATTERN = Pattern.compile(
            "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$");
    
    private static final Pattern PHONE_PATTERN = Pattern.compile(
            "^1[3-9]\\d{9}$");

    /**
     * 校验对象不为空
     */
    public static void notNull(Object obj, String message) {
        if (obj == null) {
            throw new BusinessException(message);
        }
    }

    /**
     * 校验字符串不为空
     */
    public static void notEmpty(String str, String message) {
        if (!StringUtils.hasText(str)) {
            throw new BusinessException(message);
        }
    }

    /**
     * 校验集合不为空
     */
    public static void notEmpty(Collection<?> collection, String message) {
        if (collection == null || collection.isEmpty()) {
            throw new BusinessException(message);
        }
    }

    /**
     * 校验数组不为空
     */
    public static void notEmpty(Object[] array, String message) {
        if (array == null || array.length == 0) {
            throw new BusinessException(message);
        }
    }

    /**
     * 校验条件为真
     */
    public static void isTrue(boolean condition, String message) {
        if (!condition) {
            throw new BusinessException(message);
        }
    }

    /**
     * 校验条件为假
     */
    public static void isFalse(boolean condition, String message) {
        if (condition) {
            throw new BusinessException(message);
        }
    }

    /**
     * 校验数字为正数
     */
    public static void isPositive(Number number, String message) {
        notNull(number, message);
        if (number.doubleValue() <= 0) {
            throw new BusinessException(message);
        }
    }

    /**
     * 校验数字不为负数
     */
    public static void isNotNegative(Number number, String message) {
        notNull(number, message);
        if (number.doubleValue() < 0) {
            throw new BusinessException(message);
        }
    }

    /**
     * 校验字符串长度
     */
    public static void lengthBetween(String str, int min, int max, String message) {
        notEmpty(str, message);
        int length = str.length();
        if (length < min || length > max) {
            throw new BusinessException(message);
        }
    }

    /**
     * 校验邮箱格式
     */
    public static void isValidEmail(String email, String message) {
        notEmpty(email, message);
        if (!EMAIL_PATTERN.matcher(email).matches()) {
            throw new BusinessException(message);
        }
    }

    /**
     * 校验手机号格式
     */
    public static void isValidPhone(String phone, String message) {
        notEmpty(phone, message);
        if (!PHONE_PATTERN.matcher(phone).matches()) {
            throw new BusinessException(message);
        }
    }

    /**
     * 校验ID有效性
     */
    public static void isValidId(Long id, String message) {
        notNull(id, message);
        if (id <= 0) {
            throw new BusinessException(message);
        }
    }

    /**
     * 校验分页参数
     */
    public static void validatePageParams(Integer page, Integer size) {
        isPositive(page, "页码必须大于0");
        isPositive(size, "每页大小必须大于0");
        isTrue(size <= 100, "每页大小不能超过100");
    }

    /**
     * 校验枚举值
     */
    public static <T extends Enum<T>> void isValidEnum(Class<T> enumClass, String value, String message) {
        notEmpty(value, message);
        try {
            Enum.valueOf(enumClass, value.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new BusinessException(message);
        }
    }
}