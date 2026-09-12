package org.example.kaifangyuanzi.exam.Q11.controller;


import org.example.kaifangyuanzi.exam.Q11.common.ApiResponse;
import org.example.kaifangyuanzi.exam.Q11.entity.Event;
import org.example.kaifangyuanzi.exam.Q11.service.RegistrationService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/Q11/registration")
public class RegistrationController {
    private final RegistrationService registrationService;

    public RegistrationController(RegistrationService registrationService) {
        this.registrationService = registrationService;
    }

    @PostMapping("/{eventId}")
    public ApiResponse<Void> register(@PathVariable Long eventId) {
        registrationService.register(eventId);
        return ApiResponse.ok(null);
    }

    @DeleteMapping("/{eventId}")
    public ApiResponse<Void> cancel(@PathVariable Long eventId) {
        registrationService.cancel(eventId);
        return ApiResponse.ok(null);
    }

    @GetMapping("/mine")
    public ApiResponse<List<Event>> mine() {
        return ApiResponse.ok(registrationService.myRegistrations());
    }



}
