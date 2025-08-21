package org.example.common.security;

import org.springframework.util.DigestUtils;
import org.springframework.util.StringUtils;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.SecureRandom;
import java.util.Base64;
import java.util.regex.Pattern;

/**
 * 安全工具类
 * 提供加密、解密、密码验证、输入验证等安全相关功能
 * 
 * @author system
 * @since 1.0.0
 */
public class SecurityUtils {
    
    /**
     * AES加密算法
     */
    private static final String AES_ALGORITHM = "AES";
    
    /**
     * AES加密模式
     */
    private static final String AES_TRANSFORMATION = "AES/ECB/PKCS5Padding";
    
    /**
     * 默认密钥（生产环境应从配置文件读取）
     */
    private static final String DEFAULT_SECRET_KEY = "MySecretKey12345";
    
    /**
     * 密码强度正则表达式（至少8位，包含大小写字母、数字和特殊字符）
     */
    private static final Pattern STRONG_PASSWORD_PATTERN = Pattern.compile(
        "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$"
    );
    
    /**
     * 邮箱格式正则表达式
     */
    private static final Pattern EMAIL_PATTERN = Pattern.compile(
        "^[A-Za-z0-9+_.-]+@([A-Za-z0-9.-]+\\.[A-Za-z]{2,})$"
    );
    
    /**
     * 手机号格式正则表达式
     */
    private static final Pattern PHONE_PATTERN = Pattern.compile(
        "^1[3-9]\\d{9}$"
    );
    
    /**
     * SQL注入关键词
     */
    private static final String[] SQL_INJECTION_KEYWORDS = {
        "'", "\"", ";", "--", "/*", "*/", "xp_", "sp_", "exec", "execute",
        "select", "insert", "update", "delete", "drop", "create", "alter",
        "union", "and", "or", "where", "order", "by", "group", "having"
    };
    
    /**
     * XSS攻击关键词
     */
    private static final String[] XSS_KEYWORDS = {
        "<script", "</script>", "javascript:", "vbscript:", "onload=", "onerror=",
        "onclick=", "onmouseover=", "onfocus=", "onblur=", "onchange=", "onsubmit="
    };
    
    /**
     * MD5加密
     * 
     * @param input 输入字符串
     * @return MD5加密后的字符串
     */
    public static String md5(String input) {
        if (!StringUtils.hasText(input)) {
            return null;
        }
        return DigestUtils.md5DigestAsHex(input.getBytes(StandardCharsets.UTF_8));
    }
    
    /**
     * SHA-256加密
     * 
     * @param input 输入字符串
     * @return SHA-256加密后的字符串
     */
    public static String sha256(String input) {
        if (!StringUtils.hasText(input)) {
            return null;
        }
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(input.getBytes(StandardCharsets.UTF_8));
            StringBuilder hexString = new StringBuilder();
            for (byte b : hash) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) {
                    hexString.append('0');
                }
                hexString.append(hex);
            }
            return hexString.toString();
        } catch (Exception e) {
            throw new RuntimeException("SHA-256加密失败", e);
        }
    }
    
    /**
     * 生成盐值
     * 
     * @return 随机盐值
     */
    public static String generateSalt() {
        SecureRandom random = new SecureRandom();
        byte[] salt = new byte[16];
        random.nextBytes(salt);
        return Base64.getEncoder().encodeToString(salt);
    }
    
    /**
     * 密码加盐哈希
     * 
     * @param password 原始密码
     * @param salt 盐值
     * @return 加盐哈希后的密码
     */
    public static String hashPassword(String password, String salt) {
        if (!StringUtils.hasText(password) || !StringUtils.hasText(salt)) {
            throw new IllegalArgumentException("密码和盐值不能为空");
        }
        return sha256(password + salt);
    }
    
    /**
     * 验证密码
     * 
     * @param password 原始密码
     * @param salt 盐值
     * @param hashedPassword 已哈希的密码
     * @return 是否匹配
     */
    public static boolean verifyPassword(String password, String salt, String hashedPassword) {
        if (!StringUtils.hasText(password) || !StringUtils.hasText(salt) || !StringUtils.hasText(hashedPassword)) {
            return false;
        }
        String computedHash = hashPassword(password, salt);
        return computedHash.equals(hashedPassword);
    }
    
    /**
     * AES加密
     * 
     * @param plainText 明文
     * @param secretKey 密钥
     * @return 加密后的Base64字符串
     */
    public static String aesEncrypt(String plainText, String secretKey) {
        if (!StringUtils.hasText(plainText)) {
            return null;
        }
        try {
            SecretKeySpec keySpec = new SecretKeySpec(secretKey.getBytes(StandardCharsets.UTF_8), AES_ALGORITHM);
            Cipher cipher = Cipher.getInstance(AES_TRANSFORMATION);
            cipher.init(Cipher.ENCRYPT_MODE, keySpec);
            byte[] encrypted = cipher.doFinal(plainText.getBytes(StandardCharsets.UTF_8));
            return Base64.getEncoder().encodeToString(encrypted);
        } catch (Exception e) {
            throw new RuntimeException("AES加密失败", e);
        }
    }
    
    /**
     * AES解密
     * 
     * @param encryptedText 加密的Base64字符串
     * @param secretKey 密钥
     * @return 解密后的明文
     */
    public static String aesDecrypt(String encryptedText, String secretKey) {
        if (!StringUtils.hasText(encryptedText)) {
            return null;
        }
        try {
            SecretKeySpec keySpec = new SecretKeySpec(secretKey.getBytes(StandardCharsets.UTF_8), AES_ALGORITHM);
            Cipher cipher = Cipher.getInstance(AES_TRANSFORMATION);
            cipher.init(Cipher.DECRYPT_MODE, keySpec);
            byte[] decrypted = cipher.doFinal(Base64.getDecoder().decode(encryptedText));
            return new String(decrypted, StandardCharsets.UTF_8);
        } catch (Exception e) {
            throw new RuntimeException("AES解密失败", e);
        }
    }
    
    /**
     * 使用默认密钥AES加密
     * 
     * @param plainText 明文
     * @return 加密后的Base64字符串
     */
    public static String aesEncrypt(String plainText) {
        return aesEncrypt(plainText, DEFAULT_SECRET_KEY);
    }
    
    /**
     * 使用默认密钥AES解密
     * 
     * @param encryptedText 加密的Base64字符串
     * @return 解密后的明文
     */
    public static String aesDecrypt(String encryptedText) {
        return aesDecrypt(encryptedText, DEFAULT_SECRET_KEY);
    }
    
    /**
     * 验证密码强度
     * 
     * @param password 密码
     * @return 是否为强密码
     */
    public static boolean isStrongPassword(String password) {
        if (!StringUtils.hasText(password)) {
            return false;
        }
        return STRONG_PASSWORD_PATTERN.matcher(password).matches();
    }
    
    /**
     * 验证邮箱格式
     * 
     * @param email 邮箱
     * @return 是否为有效邮箱格式
     */
    public static boolean isValidEmail(String email) {
        if (!StringUtils.hasText(email)) {
            return false;
        }
        return EMAIL_PATTERN.matcher(email).matches();
    }
    
    /**
     * 验证手机号格式
     * 
     * @param phone 手机号
     * @return 是否为有效手机号格式
     */
    public static boolean isValidPhone(String phone) {
        if (!StringUtils.hasText(phone)) {
            return false;
        }
        return PHONE_PATTERN.matcher(phone).matches();
    }
    
    /**
     * 防SQL注入检查
     * 
     * @param input 输入字符串
     * @return 是否包含SQL注入风险
     */
    public static boolean containsSqlInjection(String input) {
        if (!StringUtils.hasText(input)) {
            return false;
        }
        String lowerInput = input.toLowerCase();
        for (String keyword : SQL_INJECTION_KEYWORDS) {
            if (lowerInput.contains(keyword.toLowerCase())) {
                return true;
            }
        }
        return false;
    }
    
    /**
     * 防XSS攻击检查
     * 
     * @param input 输入字符串
     * @return 是否包含XSS攻击风险
     */
    public static boolean containsXss(String input) {
        if (!StringUtils.hasText(input)) {
            return false;
        }
        String lowerInput = input.toLowerCase();
        for (String keyword : XSS_KEYWORDS) {
            if (lowerInput.contains(keyword.toLowerCase())) {
                return true;
            }
        }
        return false;
    }
    
    /**
     * 清理XSS攻击字符
     * 
     * @param input 输入字符串
     * @return 清理后的字符串
     */
    public static String cleanXss(String input) {
        if (!StringUtils.hasText(input)) {
            return input;
        }
        
        // 移除脚本标签
        input = input.replaceAll("(?i)<script[^>]*>.*?</script>", "");
        input = input.replaceAll("(?i)<script[^>]*>", "");
        input = input.replaceAll("(?i)</script>", "");
        
        // 移除事件处理器
        input = input.replaceAll("(?i)on\\w+\\s*=\\s*[\"'][^\"']*[\"']", "");
        input = input.replaceAll("(?i)on\\w+\\s*=\\s*[^\\s>]*", "");
        
        // 移除javascript和vbscript协议
        input = input.replaceAll("(?i)javascript:", "");
        input = input.replaceAll("(?i)vbscript:", "");
        
        // 转义HTML特殊字符
        input = input.replace("<", "&lt;");
        input = input.replace(">", "&gt;");
        input = input.replace("\"", "&quot;");
        input = input.replace("'", "&#x27;");
        input = input.replace("/", "&#x2F;");
        
        return input;
    }
    
    /**
     * 生成随机字符串
     * 
     * @param length 长度
     * @return 随机字符串
     */
    public static String generateRandomString(int length) {
        if (length <= 0) {
            throw new IllegalArgumentException("长度必须大于0");
        }
        
        String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        SecureRandom random = new SecureRandom();
        StringBuilder sb = new StringBuilder(length);
        
        for (int i = 0; i < length; i++) {
            sb.append(chars.charAt(random.nextInt(chars.length())));
        }
        
        return sb.toString();
    }
    
    /**
     * 生成随机数字字符串
     * 
     * @param length 长度
     * @return 随机数字字符串
     */
    public static String generateRandomNumber(int length) {
        if (length <= 0) {
            throw new IllegalArgumentException("长度必须大于0");
        }
        
        SecureRandom random = new SecureRandom();
        StringBuilder sb = new StringBuilder(length);
        
        for (int i = 0; i < length; i++) {
            sb.append(random.nextInt(10));
        }
        
        return sb.toString();
    }
    
    /**
     * 掩码敏感信息（手机号）
     * 
     * @param phone 手机号
     * @return 掩码后的手机号
     */
    public static String maskPhone(String phone) {
        if (!StringUtils.hasText(phone) || phone.length() < 7) {
            return phone;
        }
        return phone.substring(0, 3) + "****" + phone.substring(phone.length() - 4);
    }
    
    /**
     * 掩码敏感信息（邮箱）
     * 
     * @param email 邮箱
     * @return 掩码后的邮箱
     */
    public static String maskEmail(String email) {
        if (!StringUtils.hasText(email) || !email.contains("@")) {
            return email;
        }
        
        String[] parts = email.split("@");
        if (parts.length != 2) {
            return email;
        }
        
        String username = parts[0];
        String domain = parts[1];
        
        if (username.length() <= 2) {
            return email;
        }
        
        String maskedUsername = username.substring(0, 1) + "***" + username.substring(username.length() - 1);
        return maskedUsername + "@" + domain;
    }
    
    /**
     * 掩码敏感信息（身份证号）
     * 
     * @param idCard 身份证号
     * @return 掩码后的身份证号
     */
    public static String maskIdCard(String idCard) {
        if (!StringUtils.hasText(idCard) || idCard.length() < 8) {
            return idCard;
        }
        return idCard.substring(0, 4) + "**********" + idCard.substring(idCard.length() - 4);
    }
}