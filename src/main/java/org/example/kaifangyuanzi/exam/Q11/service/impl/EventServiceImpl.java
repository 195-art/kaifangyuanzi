package org.example.kaifangyuanzi.exam.Q11.service.impl;

import org.example.kaifangyuanzi.exam.Q10.entity.SysUser;
import org.example.kaifangyuanzi.exam.Q10.mapper.UserMapper;
import org.example.kaifangyuanzi.exam.Q11.common.PageResult;
import org.example.kaifangyuanzi.exam.Q11.entity.Event;
import org.example.kaifangyuanzi.exam.Q11.exception.*;
import org.example.kaifangyuanzi.exam.Q11.mapper.EventMapper;
import org.example.kaifangyuanzi.exam.Q11.mapper.RegistrationMapper;
import org.example.kaifangyuanzi.exam.Q11.service.EventService;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.annotation.Isolation;

@Service
public class EventServiceImpl implements EventService {
    private final EventMapper eventMapper;
    private final UserMapper userMapper;
    private final RegistrationMapper registrationMapper;

    public EventServiceImpl(EventMapper eventMapper, UserMapper userMapper, RegistrationMapper registrationMapper) {
        this.eventMapper = eventMapper;
        this.userMapper = userMapper;
        this.registrationMapper = registrationMapper;
    }

    @Override
    @Cacheable(cacheNames = "eventList", keyGenerator = "eventListKeyGenerator")
    public PageResult<Event> listEvents(int page, int size, String keyword, String status) {
        if (page < 1 || size < 1 || size > 100) throw new BusinessException("页码必须大于0，每页数量必须在1到100之间");
        keyword = keyword == null || keyword.isBlank() ? null : keyword.trim();
        status = status == null || status.isBlank() ? null : status.trim();
        if (status != null && !status.equals("即将开始") && !status.equals("已结束")) {
            throw new BusinessException("活动状态无效");
        }
        PageResult<Event> result = new PageResult<>();
        result.setExpiresAt(eventMapper.nextEventTime());
        result.setRecords(eventMapper.selectPage(keyword, status, (page - 1L) * size, size));
        result.setTotal(eventMapper.count(keyword, status));
        result.setPage(page);
        result.setSize(size);
        return result;
    }

    @Override
    public Event getEvent(Long id) {
        Event event = eventMapper.selectById(id);
        if (event == null) throw new EventNotFoundException();
        return event;
    }

    @Override
    @Transactional(isolation = Isolation.READ_COMMITTED)
    @CacheEvict(cacheNames = "eventList", allEntries = true)
    public Event createEvent(Event event) {
        validate(event);
        event.setId(null);
        event.setCreatorId(getCurrentUserId());
        eventMapper.insert(event);
        return eventMapper.selectById(event.getId());
    }

    @Override
    @Transactional(isolation = Isolation.READ_COMMITTED)
    @CacheEvict(cacheNames = "eventList", allEntries = true)
    public Event updateEvent(Long id, Event event) {
        validate(event);
        Long userId = getCurrentUserId();
        Event existing = eventMapper.selectByIdForUpdate(id);
        if (existing == null) throw new EventNotFoundException();
        if (!existing.getCreatorId().equals(userId)) throw new UnauthorizedAccessException();
        if (event.getCapacity() < registrationMapper.countByEvent(id)) {
            throw new BusinessException("名额不能少于已报名人数");
        }
        event.setId(id);
        eventMapper.update(event);
        return eventMapper.selectById(id);
    }

    @Override
    @Transactional(isolation = Isolation.READ_COMMITTED)
    @CacheEvict(cacheNames = "eventList", allEntries = true)
    public void deleteEvent(Long id) {
        Long userId = getCurrentUserId();
        Event existing = eventMapper.selectByIdForUpdate(id);
        if (existing == null) throw new EventNotFoundException();
        if (!existing.getCreatorId().equals(userId)) throw new UnauthorizedAccessException();
        registrationMapper.deleteByEvent(id);
        eventMapper.deleteById(id);
    }

    private void validate(Event event) {
        if (event == null || event.getTitle() == null || event.getTitle().isBlank()) throw new BusinessException("活动标题不能为空");
        if (event.getTitle().length() > 100) throw new BusinessException("活动标题不能超过100字");
        if (event.getLocation() != null && event.getLocation().length() > 200) throw new BusinessException("举办地点不能超过200字");
        if (event.getEventTime() == null) throw new BusinessException("活动时间不能为空");
        if (event.getCapacity() == null || event.getCapacity() <= 0) throw new BusinessException("名额必须大于0");
    }

    private Long getCurrentUserId() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !auth.isAuthenticated() || "anonymousUser".equals(auth.getName())) throw new UnauthorizedAccessException("请先登录");
        SysUser user = userMapper.selectByUsername(auth.getName());
        if (user == null) throw new UnauthorizedAccessException("登录用户不存在");
        return user.getId();
    }
}
