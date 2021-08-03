package com.itjing.community.service;

import com.itjing.community.entity.LoginTicket;
import com.itjing.community.entity.User;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;
import java.util.Map;

/**
 * @author: lijing
 * @Date: 2021年07月30日 14:06
 * @Description:
 */
public interface UserService {

    /**
     * 查找用户
     *
     * @param id
     * @return
     */
    User findUserById(int id);

    /**
     * 注册用户
     *
     * @param user
     * @return
     */
    Map<String, Object> register(User user);

    /**
     * 激活账号
     *
     * @param userId
     * @param code
     * @return
     */
    int activation(int userId, String code);

    /**
     * 登录
     *
     * @param username
     * @param password
     * @param expiredSeconds
     * @return
     */
    Map<String, Object> login(String username, String password, int expiredSeconds);

    /**
     * 退出登录
     *
     * @param ticket
     */
    void logout(String ticket);

    /**
     * 查询登录凭证
     *
     * @param ticket
     * @return
     */
    LoginTicket findLoginTicket(String ticket);

    /**
     * 修改头像地址
     *
     * @param userId
     * @param headerUrl
     * @return
     */
    int updateHeader(int userId, String headerUrl);

    /**
     * 修改密码
     *
     * @param user
     * @param oldPassword
     * @param newPassword
     * @return
     */
    Map<String, Object> changePassword(User user, String oldPassword, String newPassword);

    /**
     * 根据用户名查询用户
     *
     * @param username
     * @return
     */
    User findUserByName(String username);

    /**
     * 获取用户权限
     * @param userId
     * @return
     */
    Collection<? extends GrantedAuthority> getAuthorities(int userId);
}
