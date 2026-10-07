package org.example.kaifangyuanzi.exam.Q11.service;

import org.example.kaifangyuanzi.exam.Q11.entity.Event;
import org.example.kaifangyuanzi.exam.Q11.common.PageResult;

public interface EventService {

    /**
     * 分页查询活动列表
     * @param page
     * @param size
     * @param keyword
     * @param status
     * @return
     */
    PageResult<Event> listEvents(int page, int size, String keyword, String status);

    /**
     * 根据id查询活动详情
     * @param id
     * @return
     */
    Event getEvent(Long id);

    /**
     * 新增活动
     * @param event
     * @return
     */
    Event createEvent(Event event);

    /**
     * 修改活动
     * @param id
     * @param event
     * @return
     */
    Event updateEvent(Long id, Event event);

    /**
     * 删除活动
     * @param id
     */
    void deleteEvent(Long id);

}
