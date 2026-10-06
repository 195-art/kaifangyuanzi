package org.example.kaifangyuanzi;

import org.example.kaifangyuanzi.exam.Q11.common.PageResult;
import org.example.kaifangyuanzi.exam.Q11.config.RedisCacheConfig;
import org.example.kaifangyuanzi.exam.Q11.entity.Event;
import org.junit.jupiter.api.Test;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.cache.RedisCache;
import org.springframework.cache.transaction.TransactionAwareCacheDecorator;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.serializer.RedisSerializer;
import tools.jackson.databind.ObjectMapper;
import java.time.*;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CacheTests {
    @Test
    void cacheKeysDistinguishNullAndLiteralKeywords() {
        var generator = new RedisCacheConfig().eventListKeyGenerator(new ObjectMapper());
        Object all = generator.generate(this, null, 1, 10, null, null);
        assertNotEquals(all, generator.generate(this, null, 1, 10, "all", null));
        assertNotEquals(all, generator.generate(this, null, 1, 10, "null", null));
        assertNotEquals(generator.generate(this, null, 1, 10, "a,b", "c"), generator.generate(this, null, 1, 10, "a", "b,c"));
    }

    @Test
    void cacheExpiresAtNextEventBoundary() {
        RedisCacheManager manager = (RedisCacheManager) new RedisCacheConfig().cacheManager(mock(RedisConnectionFactory.class));
        manager.afterPropertiesSet();
        var cache = (TransactionAwareCacheDecorator) manager.getCache("eventList");
        var function = ((RedisCache) cache.getTargetCache()).getCacheConfiguration().getTtlFunction();
        PageResult<Event> page = new PageResult<>();
        page.setExpiresAt(LocalDateTime.now().plusSeconds(30));
        Duration ttl = function.getTimeToLive("key", page);
        assertTrue(ttl.toMillis() > 0 && ttl.toMillis() <= 30000);
        page.setExpiresAt(LocalDateTime.now().minusSeconds(1));
        assertEquals(Duration.ofMillis(1), function.getTimeToLive("key", page));
    }

    @Test
    void cachedEventsKeepTypesAndCalculateCurrentStatus() {
        Event event = new Event();
        event.setEventTime(LocalDateTime.now().minusDays(1));
        event.setStatus("即将开始");
        PageResult<Event> page = new PageResult<>();
        page.setRecords(List.of(event));
        RedisSerializer<Object> serializer = RedisSerializer.json();
        PageResult<?> decoded = (PageResult<?>) serializer.deserialize(serializer.serialize(page));
        assertNotNull(decoded);
        assertInstanceOf(Event.class, decoded.getRecords().get(0));
        assertEquals("已结束", ((Event) decoded.getRecords().get(0)).getStatus());
    }
}
