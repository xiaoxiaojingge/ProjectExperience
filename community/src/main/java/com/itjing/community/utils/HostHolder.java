package com.itjing.community.utils;

import com.itjing.community.entity.User;
import org.springframework.stereotype.Component;

/**
 * @author: lijing
 * @Date: 2021年07月31日 13:15
 * @Description: 持有用户信息, 用于代替session对象，使用ThreadLocal
 */
@Component
public class HostHolder {
    private ThreadLocal<User> users = new ThreadLocal<>();

    public void setUser(User user) {
        users.set(user);
    }

    public User getUser() {
        return users.get();
    }

    public void clear() {
        users.remove();
    }
}