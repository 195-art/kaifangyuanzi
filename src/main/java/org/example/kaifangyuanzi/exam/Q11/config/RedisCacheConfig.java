package org.example.kaifangyuanzi.exam.Q11.config;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.jsontype.impl.LaissezFaireSubTypeValidator;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.cache.CacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;

import java.time.Duration;

// Redis 缓存配置：自定义 CacheManager
@Configuration("q11RedisCacheConfig")
public class RedisCacheConfig {

    @Bean
    public CacheManager cacheManager(RedisConnectionFactory factory) {
        // 自定义 ObjectMapper：让 Redis 缓存支持 LocalDateTime（Event.eventTime 就是这类型）
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());                    // 支持 LocalDateTime
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS); // 时间存成字符串，不存成数组
        // 保留类型信息（@class 字段），从 Redis 读出来才能还原成 PageResult/Event 对象
        objectMapper.activateDefaultTyping(
                LaissezFaireSubTypeValidator.instance,
                ObjectMapper.DefaultTyping.NON_FINAL,
                JsonTypeInfo.As.PROPERTY);

        RedisCacheConfiguration config = RedisCacheConfiguration.defaultCacheConfig()
                // 缓存 30 分钟自动过期，防止旧数据一直不更新
                .entryTtl(Duration.ofMinutes(30))
                // 用 JSON 格式存进 Redis（默认的 JDK 序列化要求类实现 Serializable，容易踩坑）
                .serializeValuesWith(RedisSerializationContext.SerializationPair
                        .fromSerializer(new GenericJackson2JsonRedisSerializer(objectMapper)));

        return RedisCacheManager.builder(factory)
                .cacheDefaults(config)
                .build();
    }
}
