package org.example.kaifangyuanzi.exam.Q11.service;

import org.example.kaifangyuanzi.exam.Q11.entity.Event;
import org.example.kaifangyuanzi.exam.Q11.common.PageResult;

public interface EventService {
    PageResult<Event> listEvents(int page, int size, String keyword, String status);
    Event getEvent(Long id);
    Event createEvent(Event event);
    Event updateEvent(Long id, Event event);
    void deleteEvent(Long id);

}
