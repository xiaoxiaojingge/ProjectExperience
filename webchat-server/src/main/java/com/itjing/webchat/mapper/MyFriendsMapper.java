package com.itjing.webchat.mapper;

import com.itjing.webchat.pojo.MyFriends;
import org.springframework.stereotype.Repository;

@Repository
public interface MyFriendsMapper {
    int deleteByPrimaryKey(String id);

    int insert(MyFriends record);

    int insertSelective(MyFriends record);

    MyFriends selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(MyFriends record);

    int updateByPrimaryKey(MyFriends record);

    MyFriends selectOneByExample(MyFriends mfe);
}