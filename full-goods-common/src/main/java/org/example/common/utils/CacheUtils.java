package org.example.common.utils;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;
import java.util.Set;
import java.util.List;
import java.util.Map;

/**
 * 缓存工具类
 * 提供Redis缓存的常用操作
 */
@Slf4j
@Component
public class CacheUtils {

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    // 默认过期时间（秒）
    private static final long DEFAULT_EXPIRE = 3600;

    /**
     * 设置缓存
     */
    public void set(String key, Object value) {
        set(key, value, DEFAULT_EXPIRE);
    }

    /**
     * 设置缓存带过期时间
     */
    public void set(String key, Object value, long expire) {
        try {
            redisTemplate.opsForValue().set(key, value, expire, TimeUnit.SECONDS);
            log.debug("设置缓存成功: key={}, expire={}s", key, expire);
        } catch (Exception e) {
            log.error("设置缓存失败: key={}", key, e);
        }
    }

    /**
     * 获取缓存
     */
    @SuppressWarnings("unchecked")
    public <T> T get(String key, Class<T> clazz) {
        try {
            Object value = redisTemplate.opsForValue().get(key);
            if (value != null && clazz.isInstance(value)) {
                log.debug("获取缓存成功: key={}", key);
                return (T) value;
            }
        } catch (Exception e) {
            log.error("获取缓存失败: key={}", key, e);
        }
        return null;
    }

    /**
     * 获取缓存（泛型方法）
     */
    public Object get(String key) {
        try {
            return redisTemplate.opsForValue().get(key);
        } catch (Exception e) {
            log.error("获取缓存失败: key={}", key, e);
            return null;
        }
    }

    /**
     * 删除缓存
     */
    public boolean delete(String key) {
        try {
            Boolean result = redisTemplate.delete(key);
            log.debug("删除缓存: key={}, result={}", key, result);
            return Boolean.TRUE.equals(result);
        } catch (Exception e) {
            log.error("删除缓存失败: key={}", key, e);
            return false;
        }
    }

    /**
     * 批量删除缓存
     */
    public long delete(Set<String> keys) {
        try {
            Long result = redisTemplate.delete(keys);
            log.debug("批量删除缓存: keys={}, result={}", keys, result);
            return result != null ? result : 0;
        } catch (Exception e) {
            log.error("批量删除缓存失败: keys={}", keys, e);
            return 0;
        }
    }

    /**
     * 检查缓存是否存在
     */
    public boolean exists(String key) {
        try {
            Boolean result = redisTemplate.hasKey(key);
            return Boolean.TRUE.equals(result);
        } catch (Exception e) {
            log.error("检查缓存存在性失败: key={}", key, e);
            return false;
        }
    }

    /**
     * 设置过期时间
     */
    public boolean expire(String key, long expire) {
        try {
            Boolean result = redisTemplate.expire(key, expire, TimeUnit.SECONDS);
            log.debug("设置过期时间: key={}, expire={}s, result={}", key, expire, result);
            return Boolean.TRUE.equals(result);
        } catch (Exception e) {
            log.error("设置过期时间失败: key={}", key, e);
            return false;
        }
    }

    /**
     * 获取过期时间
     */
    public long getExpire(String key) {
        try {
            Long expire = redisTemplate.getExpire(key, TimeUnit.SECONDS);
            return expire != null ? expire : -1;
        } catch (Exception e) {
            log.error("获取过期时间失败: key={}", key, e);
            return -1;
        }
    }

    /**
     * 递增
     */
    public long increment(String key) {
        return increment(key, 1);
    }

    /**
     * 递增指定值
     */
    public long increment(String key, long delta) {
        try {
            Long result = redisTemplate.opsForValue().increment(key, delta);
            return result != null ? result : 0;
        } catch (Exception e) {
            log.error("递增失败: key={}, delta={}", key, delta, e);
            return 0;
        }
    }

    /**
     * 递减
     */
    public long decrement(String key) {
        return decrement(key, 1);
    }

    /**
     * 递减指定值
     */
    public long decrement(String key, long delta) {
        try {
            Long result = redisTemplate.opsForValue().decrement(key, delta);
            return result != null ? result : 0;
        } catch (Exception e) {
            log.error("递减失败: key={}, delta={}", key, delta, e);
            return 0;
        }
    }

    /**
     * 获取匹配的键
     */
    public Set<String> keys(String pattern) {
        try {
            return redisTemplate.keys(pattern);
        } catch (Exception e) {
            log.error("获取匹配键失败: pattern={}", pattern, e);
            return null;
        }
    }

    /**
     * Hash操作 - 设置
     */
    public void hSet(String key, String hashKey, Object value) {
        try {
            redisTemplate.opsForHash().put(key, hashKey, value);
            log.debug("Hash设置成功: key={}, hashKey={}", key, hashKey);
        } catch (Exception e) {
            log.error("Hash设置失败: key={}, hashKey={}", key, hashKey, e);
        }
    }

    /**
     * Hash操作 - 获取
     */
    public Object hGet(String key, String hashKey) {
        try {
            return redisTemplate.opsForHash().get(key, hashKey);
        } catch (Exception e) {
            log.error("Hash获取失败: key={}, hashKey={}", key, hashKey, e);
            return null;
        }
    }

    /**
     * Hash操作 - 删除
     */
    public boolean hDelete(String key, String... hashKeys) {
        try {
            Long result = redisTemplate.opsForHash().delete(key, (Object[]) hashKeys);
            return result != null && result > 0;
        } catch (Exception e) {
            log.error("Hash删除失败: key={}, hashKeys={}", key, hashKeys, e);
            return false;
        }
    }

    /**
     * 生成缓存键
     */
    public static String buildKey(String prefix, Object... parts) {
        StringBuilder sb = new StringBuilder(prefix);
        for (Object part : parts) {
            sb.append(":").append(part);
        }
        return sb.toString();
    }
}