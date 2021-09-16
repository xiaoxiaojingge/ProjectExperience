package com.itjing.qqcommon;

import java.io.Serializable;

/**
 * @author: lijing
 * @Date: 2021年09月16日 10:26
 * @Description: 用户/客户实体
 */
public class User implements Serializable {

    private static final long serialVersionUID = 1L;


    /**
     * 用户id/用户名
     */
    private String userId;
    /**
     * 用户密码
     */
    private String password;

    public User() {
    }

    public User(String userId, String password) {
        this.userId = userId;
        this.password = password;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
