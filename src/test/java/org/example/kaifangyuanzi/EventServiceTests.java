package org.example.kaifangyuanzi;

import org.example.kaifangyuanzi.exam.Q10.entity.SysUser;
import org.example.kaifangyuanzi.exam.Q10.mapper.UserMapper;
import org.example.kaifangyuanzi.exam.Q11.entity.Event;
import org.example.kaifangyuanzi.exam.Q11.exception.*;
import org.example.kaifangyuanzi.exam.Q11.mapper.*;
import org.example.kaifangyuanzi.exam.Q11.service.impl.*;
import org.junit.jupiter.api.*;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import java.time.LocalDateTime;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class EventServiceTests {
    EventMapper events;
    RegistrationMapper registrations;
    UserMapper users;
    EventServiceImpl service;
    Event event;

    @BeforeEach
    void setUp() {
        events = mock(EventMapper.class);
        registrations = mock(RegistrationMapper.class);
        users = mock(UserMapper.class);
        service = new EventServiceImpl(events, users, registrations);
        SysUser user = new SysUser();
        user.setId(1L);
        user.setUsername("Alice");
        when(users.selectByUsername("Alice")).thenReturn(user);
        SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken("Alice", null, List.of()));
        event = new Event();
        event.setId(10L);
        event.setTitle("读书会");
        event.setCreatorId(1L);
        event.setCapacity(5);
        event.setEventTime(LocalDateTime.now().plusDays(1));
        when(events.selectByIdForUpdate(10L)).thenReturn(event);
        when(events.selectById(10L)).thenReturn(event);
    }

    @AfterEach
    void clearContext() { SecurityContextHolder.clearContext(); }

    @Test
    void cannotReduceCapacityBelowRegistrations() {
        when(registrations.countByEvent(10L)).thenReturn(6);
        assertThrows(BusinessException.class, () -> service.updateEvent(10L, event));
        verify(events, never()).update(any());
    }

    @Test
    void nonOwnerCannotModifyOrDelete() {
        event.setCreatorId(2L);
        assertThrows(UnauthorizedAccessException.class, () -> service.updateEvent(10L, event));
        assertThrows(UnauthorizedAccessException.class, () -> service.deleteEvent(10L));
        verify(events, never()).update(any());
        verify(registrations, never()).deleteByEvent(any());
    }

    @Test
    void rejectsInvalidEvent() {
        event.setCapacity(0);
        assertThrows(BusinessException.class, () -> service.createEvent(event));
        event.setCapacity(1);
        event.setTitle(" ");
        assertThrows(BusinessException.class, () -> service.createEvent(event));
        verify(events, never()).insert(any());
    }

    @Test
    void fullAndEndedEventsRejectRegistration() {
        RegistrationServiceImpl registrationService = new RegistrationServiceImpl(registrations, events, users);
        when(registrations.countByEvent(10L)).thenReturn(5);
        assertThrows(BusinessException.class, () -> registrationService.register(10L));
        event.setEventTime(LocalDateTime.now().minusDays(1));
        assertThrows(BusinessException.class, () -> registrationService.register(10L));
        verify(registrations, never()).insert(any());
    }

    @Test
    void deletionRemovesRegistrationsBeforeEvent() {
        service.deleteEvent(10L);
        var order = inOrder(registrations, events);
        order.verify(registrations).deleteByEvent(10L);
        order.verify(events).deleteById(10L);
    }

    @Test
    void paginationUsesLongOffsetAndValidatesStatus() {
        service.listEvents(Integer.MAX_VALUE, 100, "all", null);
        verify(events).selectPage("all", null, (Integer.MAX_VALUE - 1L) * 100, 100);
        assertThrows(BusinessException.class, () -> service.listEvents(1, 10, null, "invalid"));
    }
}
