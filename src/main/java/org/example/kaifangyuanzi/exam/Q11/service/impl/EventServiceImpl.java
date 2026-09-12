package org.example.kaifangyuanzi.exam.Q11.service.impl;

import org.example.kaifangyuanzi.exam.Q10.entity.SysUser;
import org.example.kaifangyuanzi.exam.Q10.mapper.UserMapper;
import org.example.kaifangyuanzi.exam.Q11.common.PageResult;
import org.example.kaifangyuanzi.exam.Q11.entity.Event;
import org.example.kaifangyuanzi.exam.Q11.exception.EventNotFoundException;
import org.example.kaifangyuanzi.exam.Q11.exception.UnauthorizedAccessException;
import org.example.kaifangyuanzi.exam.Q11.mapper.EventMapper;
import org.example.kaifangyuanzi.exam.Q11.service.EventService;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EventServiceImpl implements EventService {
    private final EventMapper eventMapper;
    private final UserMapper userMapper;

    public EventServiceImpl(EventMapper eventMapper, UserMapper userMapper) {
        this.eventMapper = eventMapper;
        this.userMapper = userMapper;
    }

    // @Cacheable：查之前先去 Redis 找，没有才查数据库；查完结果存进 Redis
    // key 把 page/size/keyword/status 都算进去，不同筛选条件是不同缓存，不会串
    @Override
    @Cacheable(cacheNames = "eventList", key = "#page + '-' + #size + '-' + (#keyword == null ? 'all' : #keyword) + '-' + (#status == null ? 'all' : #status)")
    public PageResult<Event> listEvents(int page, int size, String keyword, String status) {
        if( page < 1 ) page = 1;
        if( size < 1 ) size = 10;

        int offset = (page - 1) * size;
        PageResult<Event> result = new PageResult<>();
        result.setRecords(eventMapper.selectPage(keyword, status, offset, size));
        result.setTotal(eventMapper.count(keyword, status));
        result.setPage(page);
        result.setSize(size);
        return result;
    }

    @Override
    public Event getEvent(Long id) {
        Event event = eventMapper.selectById(id);
        if( event == null ){
            throw new EventNotFoundException();
        }
        return event;
    }

    @Override
    @CacheEvict(cacheNames = "eventList",allEntries = true)
    public Event createEvent(Event event) {
        event.setCreatorId(getCurrentUserId());
        eventMapper.insert(event);
        return event;
    }

    @Override
    @CacheEvict(cacheNames = "eventList",allEntries = true)
    public Event updateEvent(Long id, Event event) {
        Event dbEvent = eventMapper.selectById(id);
        if( dbEvent == null ){
            throw new EventNotFoundException();
        }
        if(!dbEvent.getCreatorId().equals(getCurrentUserId())){
            throw new UnauthorizedAccessException();
        }
        event.setId(id);
        eventMapper.update(event);
        return event;
    }

    @Override
    @CacheEvict(cacheNames = "eventList",allEntries = true)
    public void deleteEvent(Long id) {
        Event dbEvent = eventMapper.selectById(id);
        if( dbEvent == null ){
            throw new EventNotFoundException();
        }
        if(!dbEvent.getCreatorId().equals(getCurrentUserId())){
            throw new UnauthorizedAccessException();
        }
        eventMapper.deleteById(id);
    }

    private Long getCurrentUserId() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if(auth == null || auth.getName() == null || "anonymousUser".equals(auth.getName())){
            throw new UnauthorizedAccessException("请先登录");
        }
        String username = auth.getName();
        SysUser user = userMapper.selectByUsername(username);
        if(user == null){
            throw new UnauthorizedAccessException("登录用户不存在");
        }
        return user.getId();
    }
}
