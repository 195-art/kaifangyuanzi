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

/**
 * 报名服务实现类
 */
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

    /**
     * 报名活动
     * @param eventId
     */
    @Override
    @Transactional(isolation = Isolation.READ_COMMITTED)
    public void register(Long eventId) {
        Long userId = getCurrentUserId();
        //查询活动信息并加锁
        Event event = eventMapper.selectByIdForUpdate(eventId);
        if (event == null) throw new EventNotFoundException();
        //校验活动是否已结束
        if (!event.getEventTime().isAfter(LocalDateTime.now())) throw new BusinessException("活动已结束");
        //校验是否重复报名
        if (registrationMapper.countByEventAndUser(eventId, userId) > 0) throw new BusinessException("您已报名过该活动，请勿重复报名");
        //校验活动名额是否已满
        if (registrationMapper.countByEvent(eventId) >= event.getCapacity()) throw new BusinessException("活动名额已满，报名失败");
        Registration registration = new Registration();
        registration.setEventId(eventId);
        registration.setUserId(userId);
        try {
            //新增报名记录
            registrationMapper.insert(registration);
        } catch (DuplicateKeyException e) {
            throw new BusinessException("您已报名过该活动，请勿重复报名");
        }
    }

    /**
     * 取消报名
     * @param eventId
     */
    @Override
    @Transactional(isolation = Isolation.READ_COMMITTED)
    public void cancel(Long eventId) {
        Long userId = getCurrentUserId();
        //查询活动信息并加锁
        if (eventMapper.selectByIdForUpdate(eventId) == null) throw new EventNotFoundException();
        //删除报名记录
        if (registrationMapper.deleteByEventAndUser(eventId, userId) == 0) throw new BusinessException("您还没有报名该活动");
    }

    /**
     * 查询我的报名活动列表
     * @return
     */
    @Override
    public List<Event> myRegistrations() {
        return registrationMapper.findEventsByUser(getCurrentUserId());
    }

    /**
     * 获取当前登录用户id
     * @return
     */
    private Long getCurrentUserId() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        //校验是否已登录
        if (auth == null || !auth.isAuthenticated() || "anonymousUser".equals(auth.getName())) throw new UnauthorizedAccessException("请先登录");
        //根据用户名查询用户
        SysUser user = userMapper.selectByUsername(auth.getName());
        if (user == null) throw new UnauthorizedAccessException("登录用户不存在");
        return user.getId();
    }
}
