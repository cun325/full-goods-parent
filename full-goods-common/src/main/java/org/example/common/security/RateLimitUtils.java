package org.example.common.security;

import org.springframework.util.StringUtils;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import java.util.Map;

/**
 * API限流工具类
 * 提供基于令牌桶和滑动窗口的限流功能
 * 
 * @author system
 * @since 1.0.0
 */
public class RateLimitUtils {
    
    /**
     * 令牌桶存储
     */
    private static final Map<String, TokenBucket> TOKEN_BUCKETS = new ConcurrentHashMap<>();
    
    /**
     * 滑动窗口存储
     */
    private static final Map<String, SlidingWindow> SLIDING_WINDOWS = new ConcurrentHashMap<>();
    
    /**
     * 默认令牌桶容量
     */
    private static final int DEFAULT_BUCKET_CAPACITY = 100;
    
    /**
     * 默认令牌生成速率（每秒）
     */
    private static final int DEFAULT_REFILL_RATE = 10;
    
    /**
     * 默认滑动窗口大小（秒）
     */
    private static final int DEFAULT_WINDOW_SIZE = 60;
    
    /**
     * 默认滑动窗口限制
     */
    private static final int DEFAULT_WINDOW_LIMIT = 1000;
    
    /**
     * 清理过期数据的间隔（毫秒）
     */
    private static final long CLEANUP_INTERVAL = 5 * 60 * 1000L; // 5分钟
    
    /**
     * 上次清理时间
     */
    private static volatile long lastCleanupTime = System.currentTimeMillis();
    
    /**
     * 令牌桶算法限流
     * 
     * @param key 限流键
     * @param capacity 桶容量
     * @param refillRate 令牌生成速率（每秒）
     * @param tokens 需要的令牌数
     * @return 是否允许通过
     */
    public static boolean tryAcquire(String key, int capacity, int refillRate, int tokens) {
        if (!StringUtils.hasText(key) || capacity <= 0 || refillRate <= 0 || tokens <= 0) {
            return false;
        }
        
        cleanupIfNeeded();
        
        TokenBucket bucket = TOKEN_BUCKETS.computeIfAbsent(key, k -> new TokenBucket(capacity, refillRate));
        return bucket.tryAcquire(tokens);
    }
    
    /**
     * 令牌桶算法限流（使用默认参数）
     * 
     * @param key 限流键
     * @return 是否允许通过
     */
    public static boolean tryAcquire(String key) {
        return tryAcquire(key, DEFAULT_BUCKET_CAPACITY, DEFAULT_REFILL_RATE, 1);
    }
    
    /**
     * 令牌桶算法限流（指定令牌数）
     * 
     * @param key 限流键
     * @param tokens 需要的令牌数
     * @return 是否允许通过
     */
    public static boolean tryAcquire(String key, int tokens) {
        return tryAcquire(key, DEFAULT_BUCKET_CAPACITY, DEFAULT_REFILL_RATE, tokens);
    }
    
    /**
     * 滑动窗口算法限流
     * 
     * @param key 限流键
     * @param windowSize 窗口大小（秒）
     * @param limit 窗口内限制次数
     * @return 是否允许通过
     */
    public static boolean checkSlidingWindow(String key, int windowSize, int limit) {
        if (!StringUtils.hasText(key) || windowSize <= 0 || limit <= 0) {
            return false;
        }
        
        cleanupIfNeeded();
        
        SlidingWindow window = SLIDING_WINDOWS.computeIfAbsent(key, k -> new SlidingWindow(windowSize, limit));
        return window.tryAcquire();
    }
    
    /**
     * 滑动窗口算法限流（使用默认参数）
     * 
     * @param key 限流键
     * @return 是否允许通过
     */
    public static boolean checkSlidingWindow(String key) {
        return checkSlidingWindow(key, DEFAULT_WINDOW_SIZE, DEFAULT_WINDOW_LIMIT);
    }
    
    /**
     * 获取令牌桶剩余令牌数
     * 
     * @param key 限流键
     * @return 剩余令牌数
     */
    public static int getAvailableTokens(String key) {
        if (!StringUtils.hasText(key)) {
            return 0;
        }
        
        TokenBucket bucket = TOKEN_BUCKETS.get(key);
        return bucket != null ? bucket.getAvailableTokens() : 0;
    }
    
    /**
     * 获取滑动窗口当前计数
     * 
     * @param key 限流键
     * @return 当前计数
     */
    public static int getCurrentCount(String key) {
        if (!StringUtils.hasText(key)) {
            return 0;
        }
        
        SlidingWindow window = SLIDING_WINDOWS.get(key);
        return window != null ? window.getCurrentCount() : 0;
    }
    
    /**
     * 重置限流器
     * 
     * @param key 限流键
     */
    public static void reset(String key) {
        if (StringUtils.hasText(key)) {
            TOKEN_BUCKETS.remove(key);
            SLIDING_WINDOWS.remove(key);
        }
    }
    
    /**
     * 清理所有限流器
     */
    public static void resetAll() {
        TOKEN_BUCKETS.clear();
        SLIDING_WINDOWS.clear();
    }
    
    /**
     * 获取限流器统计信息
     * 
     * @param key 限流键
     * @return 统计信息
     */
    public static RateLimitInfo getRateLimitInfo(String key) {
        if (!StringUtils.hasText(key)) {
            return null;
        }
        
        TokenBucket bucket = TOKEN_BUCKETS.get(key);
        SlidingWindow window = SLIDING_WINDOWS.get(key);
        
        return new RateLimitInfo(
            key,
            bucket != null ? bucket.getAvailableTokens() : 0,
            bucket != null ? bucket.getCapacity() : 0,
            bucket != null ? bucket.getRefillRate() : 0,
            window != null ? window.getCurrentCount() : 0,
            window != null ? window.getLimit() : 0,
            window != null ? window.getWindowSize() : 0
        );
    }
    
    /**
     * 如果需要则清理过期数据
     */
    private static void cleanupIfNeeded() {
        long now = System.currentTimeMillis();
        if (now - lastCleanupTime > CLEANUP_INTERVAL) {
            synchronized (RateLimitUtils.class) {
                if (now - lastCleanupTime > CLEANUP_INTERVAL) {
                    cleanup();
                    lastCleanupTime = now;
                }
            }
        }
    }
    
    /**
     * 清理过期数据
     */
    private static void cleanup() {
        long now = System.currentTimeMillis();
        
        // 清理长时间未使用的令牌桶
        TOKEN_BUCKETS.entrySet().removeIf(entry -> {
            TokenBucket bucket = entry.getValue();
            return now - bucket.getLastAccessTime() > CLEANUP_INTERVAL;
        });
        
        // 清理长时间未使用的滑动窗口
        SLIDING_WINDOWS.entrySet().removeIf(entry -> {
            SlidingWindow window = entry.getValue();
            return now - window.getLastAccessTime() > CLEANUP_INTERVAL;
        });
    }
    
    /**
     * 令牌桶实现
     */
    private static class TokenBucket {
        private final int capacity;
        private final int refillRate;
        private final AtomicInteger tokens;
        private final AtomicLong lastRefillTime;
        private volatile long lastAccessTime;
        
        public TokenBucket(int capacity, int refillRate) {
            this.capacity = capacity;
            this.refillRate = refillRate;
            this.tokens = new AtomicInteger(capacity);
            this.lastRefillTime = new AtomicLong(System.currentTimeMillis());
            this.lastAccessTime = System.currentTimeMillis();
        }
        
        public boolean tryAcquire(int tokensToAcquire) {
            refill();
            this.lastAccessTime = System.currentTimeMillis();
            
            while (true) {
                int currentTokens = tokens.get();
                if (currentTokens < tokensToAcquire) {
                    return false;
                }
                
                if (tokens.compareAndSet(currentTokens, currentTokens - tokensToAcquire)) {
                    return true;
                }
            }
        }
        
        private void refill() {
            long now = System.currentTimeMillis();
            long lastRefill = lastRefillTime.get();
            
            if (now > lastRefill) {
                long timePassed = now - lastRefill;
                int tokensToAdd = (int) (timePassed * refillRate / 1000);
                
                if (tokensToAdd > 0 && lastRefillTime.compareAndSet(lastRefill, now)) {
                    while (true) {
                        int currentTokens = tokens.get();
                        int newTokens = Math.min(capacity, currentTokens + tokensToAdd);
                        
                        if (tokens.compareAndSet(currentTokens, newTokens)) {
                            break;
                        }
                    }
                }
            }
        }
        
        public int getAvailableTokens() {
            refill();
            return tokens.get();
        }
        
        public int getCapacity() {
            return capacity;
        }
        
        public int getRefillRate() {
            return refillRate;
        }
        
        public long getLastAccessTime() {
            return lastAccessTime;
        }
    }
    
    /**
     * 滑动窗口实现
     */
    private static class SlidingWindow {
        private final int windowSize; // 窗口大小（秒）
        private final int limit; // 限制次数
        private final AtomicInteger count;
        private final AtomicLong windowStart;
        private volatile long lastAccessTime;
        
        public SlidingWindow(int windowSize, int limit) {
            this.windowSize = windowSize;
            this.limit = limit;
            this.count = new AtomicInteger(0);
            this.windowStart = new AtomicLong(System.currentTimeMillis());
            this.lastAccessTime = System.currentTimeMillis();
        }
        
        public boolean tryAcquire() {
            long now = System.currentTimeMillis();
            this.lastAccessTime = now;
            
            // 检查是否需要重置窗口
            long windowStartTime = windowStart.get();
            if (now - windowStartTime >= windowSize * 1000L) {
                // 尝试重置窗口
                if (windowStart.compareAndSet(windowStartTime, now)) {
                    count.set(1);
                    return true;
                }
            }
            
            // 在当前窗口内增加计数
            int currentCount = count.incrementAndGet();
            return currentCount <= limit;
        }
        
        public int getCurrentCount() {
            long now = System.currentTimeMillis();
            long windowStartTime = windowStart.get();
            
            // 如果窗口已过期，返回0
            if (now - windowStartTime >= windowSize * 1000L) {
                return 0;
            }
            
            return count.get();
        }
        
        public int getLimit() {
            return limit;
        }
        
        public int getWindowSize() {
            return windowSize;
        }
        
        public long getLastAccessTime() {
            return lastAccessTime;
        }
    }
    
    /**
     * 限流信息
     */
    public static class RateLimitInfo {
        private final String key;
        private final int availableTokens;
        private final int bucketCapacity;
        private final int refillRate;
        private final int currentCount;
        private final int windowLimit;
        private final int windowSize;
        
        public RateLimitInfo(String key, int availableTokens, int bucketCapacity, int refillRate,
                           int currentCount, int windowLimit, int windowSize) {
            this.key = key;
            this.availableTokens = availableTokens;
            this.bucketCapacity = bucketCapacity;
            this.refillRate = refillRate;
            this.currentCount = currentCount;
            this.windowLimit = windowLimit;
            this.windowSize = windowSize;
        }
        
        public String getKey() {
            return key;
        }
        
        public int getAvailableTokens() {
            return availableTokens;
        }
        
        public int getBucketCapacity() {
            return bucketCapacity;
        }
        
        public int getRefillRate() {
            return refillRate;
        }
        
        public int getCurrentCount() {
            return currentCount;
        }
        
        public int getWindowLimit() {
            return windowLimit;
        }
        
        public int getWindowSize() {
            return windowSize;
        }
        
        @Override
        public String toString() {
            return "RateLimitInfo{" +
                   "key='" + key + '\'' +
                   ", availableTokens=" + availableTokens +
                   ", bucketCapacity=" + bucketCapacity +
                   ", refillRate=" + refillRate +
                   ", currentCount=" + currentCount +
                   ", windowLimit=" + windowLimit +
                   ", windowSize=" + windowSize +
                   '}';
        }
    }
}