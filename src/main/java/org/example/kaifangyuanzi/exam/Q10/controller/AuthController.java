package org.example.kaifangyuanzi.exam.Q10.controller;

import jakarta.servlet.http.HttpServletResponse;
import org.example.kaifangyuanzi.exam.Q10.service.UserService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/Q10/auth")
public class AuthController {
    private final UserService userService;

    public AuthController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    public String register(@RequestParam String username, @RequestParam String password) {
        return userService.register(username, password);
    }

    @PostMapping("/login")
    public String login(
            @RequestParam(required = false) String username,
            @RequestParam(required = false) String password,
            @RequestHeader(value = "token",required = false) String token,
            HttpServletResponse response){

        if(token != null && !token.isBlank()){
            return userService.loginByToken(token);
        }

        String result = userService.loginByPassword(username,password);
        if(result.startsWith("eyJ")){
            response.setHeader("token",result);
            return "登录成功";
        }
        return result;

    }

}
