package com.itjing.webchat.mapper;

import com.itjing.webchat.pojo.User;
import org.springframework.stereotype.Repository;

@Repository
public interface UserMapper {
    int deleteByPrimaryKey(String id);

    int insert(User record);

    int insertSelective(User record);

    User selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(User record);

    int updateByPrimaryKey(User record);

    /**
     * 根据用户名查找指定用户对象
     * @param username
     * @return
     */
    User queryUserNameIsExit(String username);

}