package org.example.kaifangyuanzi.exam.Q10.service;

public interface UserService {
    String register(String username,String password);
    String loginByPassword(String username,String password);
    String loginByToken(String token);
}
