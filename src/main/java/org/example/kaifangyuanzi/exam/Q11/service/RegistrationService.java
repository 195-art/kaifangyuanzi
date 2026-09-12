package org.example.kaifangyuanzi.exam.Q11.service;

import org.example.kaifangyuanzi.exam.Q11.entity.Event;

import java.util.List;

public interface RegistrationService {
    void register(Long eventId);
    void cancel(Long eventId);
    List<Event> myRegistrations();
}
