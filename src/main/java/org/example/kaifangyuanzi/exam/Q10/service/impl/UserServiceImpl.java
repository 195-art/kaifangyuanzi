package org.example.kaifangyuanzi.exam.Q10.service.impl;

import org.example.kaifangyuanzi.exam.Q10.entity.SysUser;
import org.example.kaifangyuanzi.exam.Q10.mapper.UserMapper;
import org.example.kaifangyuanzi.exam.Q10.service.UserService;
import org.example.kaifangyuanzi.exam.Q10.util.JwtUtil;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    private final UserMapper userMapper;
    private final JwtUtil jwtUtil;

    public UserServiceImpl(UserMapper userMapper, JwtUtil jwtUtil) {
        this.userMapper = userMapper;
        this.jwtUtil = jwtUtil;
    }

    @Override
    public String register(String username, String password) {
        if(username == null || password == null || username.isBlank() || password.isBlank()){
            return "用户名或密码不能为空";
        }
        if(userMapper.selectByUsername(username) != null){
            return "用户名已存在";
        }
        SysUser user = new SysUser();
        user.setUsername(username);
        user.setPassword(password);
        return userMapper.insertUser(user) > 0 ?"注册成功":"注册失败";

    }

    @Override
    public String loginByPassword(String username, String password) {
        if (username == null || username.isBlank() || password == null || password.isBlank()) {
            return "用户名或密码不能为空";
        }
        SysUser user = userMapper.selectByUsername(username);
        if(user == null){
            return "用户名不存在";
        }
        if(!user.getPassword().equals(password)){
            return "密码错误";
        }
        return jwtUtil.generateToken(username);
    }

    @Override
    public String loginByToken(String token) {
        if (token == null || token.isBlank()) {
            return "token不能为空";
        }
        return jwtUtil.validateToken(token)? "登录成功" : "token无效或已过期，请重新登录";
    }
}
