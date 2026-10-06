package org.example.kaifangyuanzi.exam.Q11.service.impl;

import org.example.kaifangyuanzi.exam.Q10.entity.SysUser;
import org.example.kaifangyuanzi.exam.Q10.mapper.UserMapper;
import org.example.kaifangyuanzi.exam.Q11.entity.Event;
import org.example.kaifangyuanzi.exam.Q11.entity.Registration;
import org.example.kaifangyuanzi.exam.Q11.exception.*;
import org.example.kaifangyuanzi.exam.Q11.mapper.*;
import org.example.kaifangyuanzi.exam.Q11.service.RegistrationService;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.annotation.Isolation;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class RegistrationServiceImpl implements RegistrationService {
    private final RegistrationMapper registrationMapper;
    private final EventMapper eventMapper;
    private final UserMapper userMapper;

    public RegistrationServiceImpl(RegistrationMapper registrationMapper, EventMapper eventMapper, UserMapper userMapper) {
        this.registrationMapper = registrationMapper;
        this.eventMapper = eventMapper;
        this.userMapper = userMapper;
    }

    @Override
    @Transactional(isolation = Isolation.READ_COMMITTED)
    public void register(Long eventId) {
        Long userId = getCurrentUserId();
        Event event = eventMapper.selectByIdForUpdate(eventId);
        if (event == null) throw new EventNotFoundException();
        if (!event.getEventTime().isAfter(LocalDateTime.now())) throw new BusinessException("活动已结束");
        if (registrationMapper.countByEventAndUser(eventId, userId) > 0) throw new BusinessException("您已报名过该活动，请勿重复报名");
        if (registrationMapper.countByEvent(eventId) >= event.getCapacity()) throw new BusinessException("活动名额已满，报名失败");
        Registration registration = new Registration();
        registration.setEventId(eventId);
        registration.setUserId(userId);
        try {
            registrationMapper.insert(registration);
        } catch (DuplicateKeyException e) {
            throw new BusinessException("您已报名过该活动，请勿重复报名");
        }
    }

    @Override
    @Transactional(isolation = Isolation.READ_COMMITTED)
    public void cancel(Long eventId) {
        Long userId = getCurrentUserId();
        if (eventMapper.selectByIdForUpdate(eventId) == null) throw new EventNotFoundException();
        if (registrationMapper.deleteByEventAndUser(eventId, userId) == 0) throw new BusinessException("您还没有报名该活动");
    }

    @Override
    public List<Event> myRegistrations() {
        return registrationMapper.findEventsByUser(getCurrentUserId());
    }

    private Long getCurrentUserId() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !auth.isAuthenticated() || "anonymousUser".equals(auth.getName())) throw new UnauthorizedAccessException("请先登录");
        SysUser user = userMapper.selectByUsername(auth.getName());
        if (user == null) throw new UnauthorizedAccessException("登录用户不存在");
        return user.getId();
    }
}
