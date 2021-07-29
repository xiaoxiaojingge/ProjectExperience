package com.itjing.webchat.mapper;

import com.itjing.webchat.pojo.ChatMsg;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChatMsgMapper {
    int deleteByPrimaryKey(String id);

    int insert(ChatMsg record);

    int insertSelective(ChatMsg record);

    ChatMsg selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(ChatMsg record);

    int updateByPrimaryKey(ChatMsg record);

    List<ChatMsg> getUnReadMsgListByAcceptUid(String acceptUserId);
}