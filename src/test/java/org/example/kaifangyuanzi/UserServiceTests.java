package org.example.kaifangyuanzi;

import org.example.kaifangyuanzi.exam.Q10.entity.SysUser;
import org.example.kaifangyuanzi.exam.Q10.mapper.UserMapper;
import org.example.kaifangyuanzi.exam.Q10.service.impl.UserServiceImpl;
import org.example.kaifangyuanzi.exam.Q10.util.JwtUtil;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserServiceTests {
    private final UserMapper users = mock(UserMapper.class);
    private final JwtUtil jwt = mock(JwtUtil.class);
    private final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
    private final UserServiceImpl service = new UserServiceImpl(users, jwt, encoder);

    @Test
    void registrationStoresHashAndHandlesDuplicate() {
        when(users.insertUser(any())).thenReturn(1);
        assertEquals("注册成功", service.register("Alice", "password"));
        ArgumentCaptor<SysUser> user = ArgumentCaptor.forClass(SysUser.class);
        verify(users).insertUser(user.capture());
        assertNotEquals("password", user.getValue().getPassword());
        assertTrue(encoder.matches("password", user.getValue().getPassword()));
        when(users.insertUser(any())).thenThrow(new DuplicateKeyException("duplicate"));
        assertEquals("用户名已存在", service.register("Bob", "password"));
    }

    @Test
    void oldPasswordIsUpgradedAfterCorrectLogin() {
        SysUser user = new SysUser();
        user.setId(1L);
        user.setUsername("Alice");
        user.setPassword("password");
        when(users.selectByUsername("Alice")).thenReturn(user);
        when(jwt.generateToken("Alice")).thenReturn("token");
        assertEquals("用户名或密码错误", service.loginByPassword("Alice", "wrong"));
        verify(users, never()).updatePassword(any(), any(), any());
        assertEquals("token", service.loginByPassword("Alice", "password"));
        ArgumentCaptor<String> hash = ArgumentCaptor.forClass(String.class);
        verify(users).updatePassword(eq(1L), eq("password"), hash.capture());
        assertTrue(encoder.matches("password", hash.getValue()));
    }
}
