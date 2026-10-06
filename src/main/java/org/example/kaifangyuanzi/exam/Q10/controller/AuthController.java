package org.example.kaifangyuanzi.exam.Q10.controller;

import org.example.kaifangyuanzi.exam.Q10.entity.SysUser;
import org.example.kaifangyuanzi.exam.Q10.mapper.UserMapper;
import org.example.kaifangyuanzi.exam.Q10.service.UserService;
import org.example.kaifangyuanzi.exam.Q10.util.JwtUtil;
import org.example.kaifangyuanzi.exam.Q11.common.ApiResponse;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import java.util.Map;
import io.jsonwebtoken.JwtException;

@RestController
@RequestMapping("/Q10/auth")
public class AuthController {
    private final UserService userService;
    private final UserMapper userMapper;
    private final JwtUtil jwtUtil;

    public AuthController(UserService userService, UserMapper userMapper, JwtUtil jwtUtil) {
        this.userService = userService;
        this.userMapper = userMapper;
        this.jwtUtil = jwtUtil;
    }

    @PostMapping("/register")
    public ResponseEntity<ApiResponse<String>> register(@RequestParam String username, @RequestParam String password) {
        String result = userService.register(username, password);
        return result.equals("注册成功") ? success(result) : ResponseEntity.badRequest().body(ApiResponse.error(400, result));
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<String>> login(
            @RequestParam(required = false) String username, @RequestParam(required = false) String password,
            @RequestHeader(value = "token", required = false) String token) {
        if (username != null || password != null) {
            String result = userService.loginByPassword(username, password);
            if (!result.startsWith("eyJ")) return ResponseEntity.status(401).body(ApiResponse.error(401, result));
            ApiResponse<String> body = ApiResponse.ok("登录成功");
            body.setMessage("登录成功");
            return ResponseEntity.ok().header("token", result).body(body);
        }
        String result = userService.loginByToken(token);
        return result.equals("登录成功") ? success(result) : ResponseEntity.status(401).body(ApiResponse.error(401, result));
    }

    @GetMapping("/me")
    public ResponseEntity<ApiResponse<Map<String, Object>>> me(@RequestHeader(value = "token", required = false) String token) {
        if (token == null || token.isBlank()) {
            return ResponseEntity.status(401).body(ApiResponse.error(401, "请重新登录"));
        }
        String username;
        try {
            username = jwtUtil.getUsername(token);
        } catch (JwtException | IllegalArgumentException e) {
            return ResponseEntity.status(401).body(ApiResponse.error(401, "请重新登录"));
        }
        if (username == null || username.isBlank()) return ResponseEntity.status(401).body(ApiResponse.error(401, "请重新登录"));
        SysUser user = userMapper.selectByUsername(username);
        if (user == null) return ResponseEntity.status(401).body(ApiResponse.error(401, "登录用户不存在"));
        return ResponseEntity.ok(ApiResponse.ok(Map.of("id", user.getId(), "username", user.getUsername())));
    }

    private ResponseEntity<ApiResponse<String>> success(String message) {
        ApiResponse<String> body = ApiResponse.ok(message);
        body.setMessage(message);
        return ResponseEntity.ok(body);
    }
}
