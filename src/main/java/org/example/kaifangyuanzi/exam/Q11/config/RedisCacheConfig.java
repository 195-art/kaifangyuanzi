package org.example.kaifangyuanzi.exam.Q11.config;

import org.example.kaifangyuanzi.exam.Q11.common.PageResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CachingConfigurer;
import org.springframework.cache.interceptor.CacheErrorHandler;
import org.springframework.cache.interceptor.KeyGenerator;
import tools.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.*;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.serializer.*;
import java.time.Duration;
import java.time.LocalDateTime;

@Configuration("q11RedisCacheConfig")
public class RedisCacheConfig implements CachingConfigurer {
    private static final Logger log = LoggerFactory.getLogger(RedisCacheConfig.class);

    @Bean
    public KeyGenerator eventListKeyGenerator(ObjectMapper mapper) {
        return (target, method, params) -> mapper.writeValueAsString(params);
    }

    @Bean
    public CacheManager cacheManager(RedisConnectionFactory factory) {
        RedisCacheConfiguration config = RedisCacheConfiguration.defaultCacheConfig()
                .entryTtl((key, value) -> {
                    Duration ttl = Duration.ofMinutes(1);
                    if (value instanceof PageResult<?> page && page.getExpiresAt() != null) {
                        Duration remaining = Duration.between(LocalDateTime.now(), page.getExpiresAt());
                        if (remaining.isNegative() || remaining.isZero()) return Duration.ofMillis(1);
                        if (remaining.compareTo(ttl) < 0) ttl = remaining;
                    }
                    return ttl;
                })
                .serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(RedisSerializer.json()));
        return RedisCacheManager.builder(factory).cacheDefaults(config).transactionAware().build();
    }

    @Bean
    @Override
    public CacheErrorHandler errorHandler() {
        return new CacheErrorHandler() {
            public void handleCacheGetError(RuntimeException e, Cache cache, Object key) { failed(cache); }
            public void handleCachePutError(RuntimeException e, Cache cache, Object key, Object value) { failed(cache); }
            public void handleCacheEvictError(RuntimeException e, Cache cache, Object key) { failed(cache); }
            public void handleCacheClearError(RuntimeException e, Cache cache) { failed(cache); }
            private void failed(Cache cache) { log.warn("缓存 {} 暂不可用", cache.getName()); }
        };
    }
}
