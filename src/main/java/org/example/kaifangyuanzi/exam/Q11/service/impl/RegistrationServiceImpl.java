package org.example.kaifangyuanzi.exam.Q11.service.impl;


import jakarta.transaction.Transactional;
import org.example.kaifangyuanzi.exam.Q10.entity.SysUser;
import org.example.kaifangyuanzi.exam.Q10.mapper.UserMapper;
import org.example.kaifangyuanzi.exam.Q11.entity.Event;
import org.example.kaifangyuanzi.exam.Q11.entity.Registration;
import org.example.kaifangyuanzi.exam.Q11.exception.BusinessException;
import org.example.kaifangyuanzi.exam.Q11.exception.EventNotFoundException;
import org.example.kaifangyuanzi.exam.Q11.exception.UnauthorizedAccessException;
import org.example.kaifangyuanzi.exam.Q11.mapper.EventMapper;
import org.example.kaifangyuanzi.exam.Q11.mapper.RegistrationMapper;
import org.example.kaifangyuanzi.exam.Q11.service.RegistrationService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
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
    @Transactional
    public void register(Long eventId) {
        Event event = eventMapper.selectById(eventId);
        if (event == null) {
            throw new EventNotFoundException();
        }
        Long userId = getCurrentUserId();

        if(registrationMapper.countByEventAndUser(eventId, userId) > 0) {
            throw new BusinessException("您已报名过该活动，请勿重复报名");
        }

        int registered = registrationMapper.countByEvent(eventId);
        if(registered >= event.getCapacity()){
            throw new BusinessException("活动名额已满，报名失败");
        }

        Registration registration = new Registration();
        registration.setEventId(eventId);
        registration.setUserId(userId);
        registrationMapper.insert(registration);

    }

    @Override
    public void cancel(Long eventId) {
        Event event = eventMapper.selectById(eventId);
        if (event == null) {
            throw new EventNotFoundException();
        }
        Long userId = getCurrentUserId();

        if(registrationMapper.countByEventAndUser(eventId, userId) == 0) {
            throw new BusinessException("您还没有报名该活动");
        }

        registrationMapper.deleteByEventAndUser(eventId, userId);
    }

    @Override
    public List<Event> myRegistrations() {
        Long userId = getCurrentUserId();
        List<Long> eventIds = registrationMapper.findEventIdsByUser(userId);

        List<Event> events = new ArrayList<>();
        for (Long eventId : eventIds) {
            events.add(eventMapper.selectById(eventId));
        }
        return events;

    }


    private Long getCurrentUserId() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || auth.getName() == null || "anonymousUser".equals(auth.getName())) {
            throw new UnauthorizedAccessException("请先登录");
        }
        String username = auth.getName();
        SysUser user = userMapper.selectByUsername(username);
        if (user == null) {
            throw new UnauthorizedAccessException("登录用户不存在");
        }
        return user.getId();
    }
}
