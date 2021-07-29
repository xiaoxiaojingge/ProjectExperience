package com.itjing.webchat.mapper;

import com.itjing.webchat.pojo.FriendsRequest;
import org.springframework.stereotype.Repository;

@Repository
public interface FriendsRequestMapper {
    int deleteByPrimaryKey(String id);

    int insert(FriendsRequest record);

    int insertSelective(FriendsRequest record);

    FriendsRequest selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(FriendsRequest record);

    int updateByPrimaryKey(FriendsRequest record);

    /**
     * 根据好友请求对象进行删除操作
     * @param friendsRequest
     */
    void deleteByFriendRequest(FriendsRequest friendsRequest);
}