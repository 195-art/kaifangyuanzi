package org.example.kaifangyuanzi.exam.Q11.controller;


import org.example.kaifangyuanzi.exam.Q11.common.ApiResponse;
import org.example.kaifangyuanzi.exam.Q11.entity.Event;
import org.example.kaifangyuanzi.exam.Q11.common.PageResult;

import org.example.kaifangyuanzi.exam.Q11.service.EventService;
import org.springframework.web.bind.annotation.*;

/**
 * 活动管理
 */
@RestController
@RequestMapping("/Q11/event")
public class EventController {
    private final EventService eventService;

    public EventController(EventService eventService) {
        this.eventService = eventService;
    }

    /**
     * 分页查询活动列表
     * @param page
     * @param size
     * @param keyword
     * @param status
     * @return
     */
    @GetMapping("/list")
    public ApiResponse<PageResult<Event>> list(@RequestParam(defaultValue = "1") int page, @RequestParam(defaultValue = "10") int size,
                                               @RequestParam(required = false) String keyword, @RequestParam(required = false) String status){
        return ApiResponse.ok(eventService.listEvents(page, size, keyword, status));
    }

    /**
     * 根据id查询活动详情
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    public ApiResponse<Event> detail(@PathVariable Long id){
        return ApiResponse.ok(eventService.getEvent(id));
    }

    /**
     * 新增活动
     * @param event
     * @return
     */
    @PostMapping
    public ApiResponse<Event> create(@RequestBody Event event){
        return ApiResponse.ok(eventService.createEvent(event));
    }

    /**
     * 修改活动
     * @param id
     * @param event
     * @return
     */
    @PutMapping("/{id}")
    public ApiResponse<Event> update(@PathVariable Long id,@RequestBody Event event){
        return ApiResponse.ok(eventService.updateEvent(id, event));
    }

    /**
     * 删除活动
     * @param id
     * @return
     */
    @DeleteMapping("/{id}")
    public ApiResponse<Event> delete(@PathVariable Long id){
        eventService.deleteEvent(id);
        return ApiResponse.ok(null);
    }
}
