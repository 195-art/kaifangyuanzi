package org.example.kaifangyuanzi.exam.Q11.config;

import org.springframework.cache.CacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.RedisSerializer;

import java.time.Duration;

// Redis 缓存配置：自定义 CacheManager
@Configuration("q11RedisCacheConfig")
public class RedisCacheConfig {

    @Bean
    public CacheManager cacheManager(RedisConnectionFactory factory) {
        // RedisSerializer.json()：Spring Data Redis 4 官方推荐的 JSON 序列化器（基于 Jackson 3）
        // 默认就带类型信息（@class 字段），读出来能还原成 PageResult/Event 对象；
        // Jackson 3 原生支持 LocalDateTime（Event.eventTime 就是这类型），无需再手动配置 ObjectMapper
        RedisCacheConfiguration config = RedisCacheConfiguration.defaultCacheConfig()
                // 缓存 30 分钟自动过期，防止旧数据一直不更新
                .entryTtl(Duration.ofMinutes(30))
                // 用 JSON 格式存进 Redis（默认的 JDK 序列化要求类实现 Serializable，容易踩坑）
                .serializeValuesWith(RedisSerializationContext.SerializationPair
                        .fromSerializer(RedisSerializer.json()));

        return RedisCacheManager.builder(factory)
                .cacheDefaults(config)
                .build();
    }
}