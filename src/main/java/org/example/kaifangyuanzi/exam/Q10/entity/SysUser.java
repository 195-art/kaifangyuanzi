package org.example.kaifangyuanzi.exam.Q10.entity;

import java.util.Date;

/**
 * 系统用户实体类
 */
public class SysUser {
    /** 用户id */
    private Long id;
    /** 用户名 */
    private String username;
    /** 密码 */
    private String password;
    /** 注册日期 */
    private Date registerDate;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Date getRegisterDate() {
        return registerDate;
    }

    public void setRegisterDate(Date registerDate) {
        this.registerDate = registerDate;
    }
}
