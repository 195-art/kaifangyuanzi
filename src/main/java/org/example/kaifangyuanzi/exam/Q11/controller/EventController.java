package org.example.kaifangyuanzi.exam.Q11.controller;


import org.example.kaifangyuanzi.exam.Q11.common.ApiResponse;
import org.example.kaifangyuanzi.exam.Q11.entity.Event;
import org.example.kaifangyuanzi.exam.Q11.common.PageResult;

import org.example.kaifangyuanzi.exam.Q11.service.EventService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/Q11/event")
public class EventController {
    private final EventService eventService;

    public EventController(EventService eventService) {
        this.eventService = eventService;
    }

    @GetMapping("/list")
    public ApiResponse<PageResult<Event>> list(@RequestParam(defaultValue = "1") int page, @RequestParam(defaultValue = "10") int size,
                                               @RequestParam(required = false) String keyword, @RequestParam(required = false) String status){
        return ApiResponse.ok(eventService.listEvents(page, size, keyword, status));
    }

    @GetMapping("/{id}")
    public ApiResponse<Event> detail(@PathVariable Long id){
        return ApiResponse.ok(eventService.getEvent(id));
    }

    @PostMapping
    public ApiResponse<Event> create(@RequestBody Event event){
        return ApiResponse.ok(eventService.createEvent(event));
    }

    @PutMapping("/{id}")
    public ApiResponse<Event> update(@PathVariable Long id,@RequestBody Event event){
        return ApiResponse.ok(eventService.updateEvent(id, event));
    }

    @DeleteMapping("/{id}")
    public ApiResponse<Event> delete(@PathVariable Long id){
        eventService.deleteEvent(id);
        return ApiResponse.ok(null);
    }
}
