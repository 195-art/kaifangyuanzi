package org.example.kaifangyuanzi.exam.Q10.service.impl;

import org.example.kaifangyuanzi.exam.Q10.entity.SysUser;
import org.example.kaifangyuanzi.exam.Q10.mapper.UserMapper;
import org.example.kaifangyuanzi.exam.Q10.service.UserService;
import org.example.kaifangyuanzi.exam.Q10.util.JwtUtil;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import io.jsonwebtoken.JwtException;

/**
 * 用户服务实现类
 */
@Service
public class UserServiceImpl implements UserService {
    private final UserMapper userMapper;
    private final JwtUtil jwtUtil;
    private final PasswordEncoder passwordEncoder;

    public UserServiceImpl(UserMapper userMapper, JwtUtil jwtUtil, PasswordEncoder passwordEncoder) {
        this.userMapper = userMapper;
        this.jwtUtil = jwtUtil;
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * 用户注册
     * @param username
     * @param password
     * @return
     */
    @Override
    public String register(String username, String password) {
        //校验用户名和密码是否为空
        if (username == null || username.isBlank() || password == null || password.isBlank()) return "用户名或密码不能为空";
        username = username.trim();
        //校验用户名和密码长度
        if (username.length() > 50 || password.getBytes(StandardCharsets.UTF_8).length > 72) return "用户名或密码过长";
        //判断用户名是否已存在
        if (userMapper.selectByUsername(username) != null) return "用户名已存在";
        SysUser user = new SysUser();
        user.setUsername(username);
        //密码加密存储
        user.setPassword(passwordEncoder.encode(password));
        try {
            //新增用户
            return userMapper.insertUser(user) > 0 ? "注册成功" : "注册失败";
        } catch (DuplicateKeyException e) {
            return "用户名已存在";
        }
    }

    /**
     * 用户名密码登录
     * @param username
     * @param password
     * @return
     */
    @Override
    public String loginByPassword(String username, String password) {
        //校验用户名和密码是否为空
        if (username == null || username.isBlank() || password == null || password.isBlank()) return "用户名或密码不能为空";
        //校验用户名和密码长度
        if (username.trim().length() > 50 || password.getBytes(StandardCharsets.UTF_8).length > 72) return "用户名或密码过长";
        //根据用户名查询用户
        SysUser user = userMapper.selectByUsername(username.trim());
        if (user == null) return "用户名或密码错误";
        String stored = user.getPassword();
        //判断密码是否已加密
        boolean hashed = stored.startsWith("$2a$") || stored.startsWith("$2b$") || stored.startsWith("$2y$");
        //校验密码是否正确
        boolean matches = hashed ? passwordEncoder.matches(password, stored)
                : MessageDigest.isEqual(password.getBytes(StandardCharsets.UTF_8), stored.getBytes(StandardCharsets.UTF_8));
        if (!matches) return "用户名或密码错误";
        //旧密码未加密时，自动升级为加密存储
        if (!hashed) userMapper.updatePassword(user.getId(), stored, passwordEncoder.encode(password));
        //生成并返回token
        return jwtUtil.generateToken(user.getUsername());
    }

    /**
     * 根据token登录
     * @param token
     * @return
     */
    @Override
    public String loginByToken(String token) {
        //校验token是否为空
        if (token == null || token.isBlank()) return "token无效或已过期，请重新登录";
        try {
            String username = jwtUtil.getUsername(token);
            return username != null && userMapper.selectByUsername(username) != null ? "登录成功" : "登录用户不存在";
        } catch (JwtException | IllegalArgumentException e) {
            return "token无效或已过期，请重新登录";
        }
    }
}
