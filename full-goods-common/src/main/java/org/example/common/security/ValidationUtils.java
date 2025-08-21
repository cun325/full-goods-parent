package org.example.common.security;

import org.springframework.util.StringUtils;

import java.util.regex.Pattern;
import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * 输入验证工具类
 * 提供各种输入验证和安全检查功能
 * 
 * @author system
 * @since 1.0.0
 */
public class ValidationUtils {
    
    /**
     * SQL注入关键词
     */
    private static final String[] SQL_INJECTION_KEYWORDS = {
        "select", "insert", "update", "delete", "drop", "create", "alter", "exec", "execute",
        "union", "and", "or", "where", "order", "group", "having", "count", "chr", "mid",
        "master", "truncate", "char", "declare", "script", "iframe", "object", "embed",
        "onload", "onerror", "onclick", "onmouseover", "onfocus", "onblur", "onchange",
        "onsubmit", "onreset", "onselect", "onunload", "javascript:", "vbscript:",
        "expression", "applet", "meta", "xml", "blink", "link", "style", "script",
        "embed", "object", "iframe", "frame", "frameset", "ilayer", "layer", "bgsound",
        "title", "base"
    };
    
    /**
     * XSS攻击关键词
     */
    private static final String[] XSS_KEYWORDS = {
        "<script", "</script>", "<iframe", "</iframe>", "<object", "</object>",
        "<embed", "</embed>", "<applet", "</applet>", "<meta", "<link",
        "<style", "</style>", "<title", "</title>", "<base", "<frame",
        "<frameset", "</frameset>", "<ilayer", "</ilayer>", "<layer", "</layer>",
        "<bgsound", "javascript:", "vbscript:", "onload=", "onerror=",
        "onclick=", "onmouseover=", "onfocus=", "onblur=", "onchange=",
        "onsubmit=", "onreset=", "onselect=", "onunload=", "expression(",
        "url(", "@import", "behavior:"
    };
    
    /**
     * 路径遍历攻击关键词
     */
    private static final String[] PATH_TRAVERSAL_KEYWORDS = {
        "../", "..\\\\", "%2e%2e%2f", "%2e%2e\\\\", "..%2f", "..%5c",
        "%252e%252e%252f", "%252e%252e%255c", "....//", "....\\\\\\\\",
        "/etc/passwd", "/etc/shadow", "\\\\windows\\\\system32",
        "c:\\\\windows\\\\system32", "/proc/", "/sys/"
    };
    
    /**
     * 常用正则表达式
     */
    private static final Pattern EMAIL_PATTERN = Pattern.compile(
        "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$"
    );
    
    private static final Pattern PHONE_PATTERN = Pattern.compile(
        "^1[3-9]\\d{9}$"
    );
    
    private static final Pattern ID_CARD_PATTERN = Pattern.compile(
        "^[1-9]\\d{5}(18|19|20)\\d{2}((0[1-9])|(1[0-2]))(([0-2][1-9])|10|20|30|31)\\d{3}[0-9Xx]$"
    );
    
    private static final Pattern USERNAME_PATTERN = Pattern.compile(
        "^[a-zA-Z0-9_]{4,20}$"
    );
    
    private static final Pattern PASSWORD_PATTERN = Pattern.compile(
        "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,20}$"
    );
    
    private static final Pattern IP_PATTERN = Pattern.compile(
        "^((25[0-5]|2[0-4]\\d|[01]?\\d\\d?)\\.){3}(25[0-5]|2[0-4]\\d|[01]?\\d\\d?)$"
    );
    
    private static final Pattern URL_PATTERN = Pattern.compile(
        "^(https?|ftp)://[^\\s/$.?#].[^\\s]*$"
    );
    
    private static final Pattern CHINESE_PATTERN = Pattern.compile(
        "[\\u4e00-\\u9fa5]"
    );
    
    /**
     * 检查字符串是否包含SQL注入关键词
     * 
     * @param input 输入字符串
     * @return 是否包含SQL注入关键词
     */
    public static boolean containsSqlInjection(String input) {
        if (!StringUtils.hasText(input)) {
            return false;
        }
        
        String lowerInput = input.toLowerCase();
        for (String keyword : SQL_INJECTION_KEYWORDS) {
            if (lowerInput.contains(keyword)) {
                return true;
            }
        }
        return false;
    }
    
    /**
     * 检查字符串是否包含XSS攻击关键词
     * 
     * @param input 输入字符串
     * @return 是否包含XSS攻击关键词
     */
    public static boolean containsXss(String input) {
        if (!StringUtils.hasText(input)) {
            return false;
        }
        
        String lowerInput = input.toLowerCase();
        for (String keyword : XSS_KEYWORDS) {
            if (lowerInput.contains(keyword)) {
                return true;
            }
        }
        return false;
    }
    
    /**
     * 检查字符串是否包含路径遍历攻击关键词
     * 
     * @param input 输入字符串
     * @return 是否包含路径遍历攻击关键词
     */
    public static boolean containsPathTraversal(String input) {
        if (!StringUtils.hasText(input)) {
            return false;
        }
        
        String lowerInput = input.toLowerCase();
        for (String keyword : PATH_TRAVERSAL_KEYWORDS) {
            if (lowerInput.contains(keyword)) {
                return true;
            }
        }
        return false;
    }
    
    /**
     * 综合安全检查
     * 
     * @param input 输入字符串
     * @return 是否安全
     */
    public static boolean isSafeInput(String input) {
        return !containsSqlInjection(input) && 
               !containsXss(input) && 
               !containsPathTraversal(input);
    }
    
    /**
     * 获取不安全输入的原因
     * 
     * @param input 输入字符串
     * @return 不安全的原因列表
     */
    public static List<String> getUnsafeReasons(String input) {
        List<String> reasons = new ArrayList<>();
        
        if (containsSqlInjection(input)) {
            reasons.add("包含SQL注入关键词");
        }
        if (containsXss(input)) {
            reasons.add("包含XSS攻击关键词");
        }
        if (containsPathTraversal(input)) {
            reasons.add("包含路径遍历攻击关键词");
        }
        
        return reasons;
    }
    
    /**
     * 验证邮箱格式
     * 
     * @param email 邮箱地址
     * @return 是否为有效邮箱格式
     */
    public static boolean isValidEmail(String email) {
        return StringUtils.hasText(email) && EMAIL_PATTERN.matcher(email).matches();
    }
    
    /**
     * 验证手机号格式
     * 
     * @param phone 手机号
     * @return 是否为有效手机号格式
     */
    public static boolean isValidPhone(String phone) {
        return StringUtils.hasText(phone) && PHONE_PATTERN.matcher(phone).matches();
    }
    
    /**
     * 验证身份证号格式
     * 
     * @param idCard 身份证号
     * @return 是否为有效身份证号格式
     */
    public static boolean isValidIdCard(String idCard) {
        return StringUtils.hasText(idCard) && ID_CARD_PATTERN.matcher(idCard).matches();
    }
    
    /**
     * 验证用户名格式
     * 
     * @param username 用户名
     * @return 是否为有效用户名格式
     */
    public static boolean isValidUsername(String username) {
        return StringUtils.hasText(username) && USERNAME_PATTERN.matcher(username).matches();
    }
    
    /**
     * 验证密码强度
     * 
     * @param password 密码
     * @return 是否为强密码
     */
    public static boolean isStrongPassword(String password) {
        return StringUtils.hasText(password) && PASSWORD_PATTERN.matcher(password).matches();
    }
    
    /**
     * 验证IP地址格式
     * 
     * @param ip IP地址
     * @return 是否为有效IP地址格式
     */
    public static boolean isValidIp(String ip) {
        return StringUtils.hasText(ip) && IP_PATTERN.matcher(ip).matches();
    }
    
    /**
     * 验证URL格式
     * 
     * @param url URL地址
     * @return 是否为有效URL格式
     */
    public static boolean isValidUrl(String url) {
        return StringUtils.hasText(url) && URL_PATTERN.matcher(url).matches();
    }
    
    /**
     * 检查是否包含中文字符
     * 
     * @param input 输入字符串
     * @return 是否包含中文字符
     */
    public static boolean containsChinese(String input) {
        return StringUtils.hasText(input) && CHINESE_PATTERN.matcher(input).find();
    }
    
    /**
     * 验证字符串长度
     * 
     * @param input 输入字符串
     * @param minLength 最小长度
     * @param maxLength 最大长度
     * @return 是否在指定长度范围内
     */
    public static boolean isValidLength(String input, int minLength, int maxLength) {
        if (!StringUtils.hasText(input)) {
            return minLength <= 0;
        }
        int length = input.length();
        return length >= minLength && length <= maxLength;
    }
    
    /**
     * 验证数字范围
     * 
     * @param value 数值
     * @param min 最小值
     * @param max 最大值
     * @return 是否在指定范围内
     */
    public static boolean isInRange(Number value, Number min, Number max) {
        if (value == null) {
            return false;
        }
        double val = value.doubleValue();
        double minVal = min != null ? min.doubleValue() : Double.MIN_VALUE;
        double maxVal = max != null ? max.doubleValue() : Double.MAX_VALUE;
        return val >= minVal && val <= maxVal;
    }
    
    /**
     * 验证字符串是否只包含指定字符
     * 
     * @param input 输入字符串
     * @param allowedChars 允许的字符
     * @return 是否只包含指定字符
     */
    public static boolean containsOnlyAllowedChars(String input, String allowedChars) {
        if (!StringUtils.hasText(input) || !StringUtils.hasText(allowedChars)) {
            return false;
        }
        
        for (char c : input.toCharArray()) {
            if (allowedChars.indexOf(c) == -1) {
                return false;
            }
        }
        return true;
    }
    
    /**
     * 验证字符串是否不包含指定字符
     * 
     * @param input 输入字符串
     * @param forbiddenChars 禁止的字符
     * @return 是否不包含指定字符
     */
    public static boolean doesNotContainForbiddenChars(String input, String forbiddenChars) {
        if (!StringUtils.hasText(input) || !StringUtils.hasText(forbiddenChars)) {
            return true;
        }
        
        for (char c : forbiddenChars.toCharArray()) {
            if (input.indexOf(c) != -1) {
                return false;
            }
        }
        return true;
    }
    
    /**
     * 清理HTML标签
     * 
     * @param input 输入字符串
     * @return 清理后的字符串
     */
    public static String cleanHtml(String input) {
        if (!StringUtils.hasText(input)) {
            return input;
        }
        
        // 移除HTML标签
        String cleaned = input.replaceAll("<[^>]*>", "");
        
        // 解码HTML实体
        cleaned = cleaned.replace("&lt;", "<")
                        .replace("&gt;", ">")
                        .replace("&amp;", "&")
                        .replace("&quot;", "\"")
                        .replace("&#x27;", "'")
                        .replace("&#x2F;", "/")
                        .replace("&#x5C;", "\\");
        
        return cleaned.trim();
    }
    
    /**
     * 转义HTML特殊字符
     * 
     * @param input 输入字符串
     * @return 转义后的字符串
     */
    public static String escapeHtml(String input) {
        if (!StringUtils.hasText(input)) {
            return input;
        }
        
        return input.replace("&", "&amp;")
                   .replace("<", "&lt;")
                   .replace(">", "&gt;")
                   .replace("\"", "&quot;")
                   .replace("'", "&#x27;")
                   .replace("/", "&#x2F;")
                   .replace("\\", "&#x5C;");
    }
    
    /**
     * 转义SQL特殊字符
     * 
     * @param input 输入字符串
     * @return 转义后的字符串
     */
    public static String escapeSql(String input) {
        if (!StringUtils.hasText(input)) {
            return input;
        }
        
        return input.replace("'", "''")
                   .replace("\\", "\\\\")
                   .replace("\0", "\\0")
                   .replace("\n", "\\n")
                   .replace("\r", "\\r")
                   .replace("\t", "\\t")
                   .replace("\u001a", "\\Z");
    }
    
    /**
     * 验证文件名安全性
     * 
     * @param filename 文件名
     * @return 是否为安全的文件名
     */
    public static boolean isSafeFilename(String filename) {
        if (!StringUtils.hasText(filename)) {
            return false;
        }
        
        // 检查路径遍历
        if (containsPathTraversal(filename)) {
            return false;
        }
        
        // 检查危险字符
        String dangerousChars = "<>:\"|?*\0";
        if (!doesNotContainForbiddenChars(filename, dangerousChars)) {
            return false;
        }
        
        // 检查保留名称（Windows）
        String[] reservedNames = {"CON", "PRN", "AUX", "NUL", "COM1", "COM2", "COM3", "COM4", 
                                 "COM5", "COM6", "COM7", "COM8", "COM9", "LPT1", "LPT2", 
                                 "LPT3", "LPT4", "LPT5", "LPT6", "LPT7", "LPT8", "LPT9"};
        
        String upperFilename = filename.toUpperCase();
        for (String reserved : reservedNames) {
            if (upperFilename.equals(reserved) || upperFilename.startsWith(reserved + ".")) {
                return false;
            }
        }
        
        return true;
    }
    
    /**
     * 获取密码强度等级
     * 
     * @param password 密码
     * @return 强度等级（0-4）
     */
    public static int getPasswordStrength(String password) {
        if (!StringUtils.hasText(password)) {
            return 0;
        }
        
        int strength = 0;
        
        // 长度检查
        if (password.length() >= 8) {
            strength++;
        }
        
        // 包含小写字母
        if (password.matches(".*[a-z].*")) {
            strength++;
        }
        
        // 包含大写字母
        if (password.matches(".*[A-Z].*")) {
            strength++;
        }
        
        // 包含数字
        if (password.matches(".*\\d.*")) {
            strength++;
        }
        
        // 包含特殊字符
        if (password.matches(".*[@$!%*?&].*")) {
            strength++;
        }
        
        return Math.min(strength, 4);
    }
}