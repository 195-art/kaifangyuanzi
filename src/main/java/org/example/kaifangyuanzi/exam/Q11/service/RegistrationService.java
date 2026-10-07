package org.example.kaifangyuanzi.exam.Q11.service;

import org.example.kaifangyuanzi.exam.Q11.entity.Event;

import java.util.List;

public interface RegistrationService {

    /**
     * 报名活动
     * @param eventId
     */
    void register(Long eventId);

    /**
     * 取消报名
     * @param eventId
     */
    void cancel(Long eventId);

    /**
     * 查询我的报名活动列表
     * @return
     */
    List<Event> myRegistrations();
}
