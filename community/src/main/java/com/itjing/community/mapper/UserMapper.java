package com.itjing.community.mapper;

import com.itjing.community.entity.User;
import org.springframework.stereotype.Repository;

/**
 * @author: lijing
 * @Date: 2021年07月30日 14:02
 * @Description:
 */
@Repository
public interface UserMapper {
    /**
     * 通过id查询用户
     * @param id
     * @return
     */
    User selectById(int id);

    /**
     * 通过名称查询用户
     * @param username
     * @return
     */
    User selectByName(String username);

    /**
     * 通过email查询用户
     * @param email
     * @return
     */
    User selectByEmail(String email);

    /**
     * 添加用户
     * @param user
     * @return
     */
    int insertUser(User user);

    /**
     * 修改状态
     * @param id
     * @param status
     * @return
     */
    int updateStatus(int id, int status);

    /**
     * 修改头像
     * @param id
     * @param headerUrl
     * @return
     */
    int updateHeader(int id, String headerUrl);

    /**
     * 修改密码
     * @param id
     * @param password
     * @return
     */
    int updatePassword(int id, String password);
}
