package org.example.kaifangyuanzi.exam.Q10.service;

public interface UserService {

    /**
     * 用户注册
     * @param username
     * @param password
     * @return
     */
    String register(String username,String password);

    /**
     * 用户名密码登录
     * @param username
     * @param password
     * @return
     */
    String loginByPassword(String username,String password);

    /**
     * 根据token登录
     * @param token
     * @return
     */
    String loginByToken(String token);
}
