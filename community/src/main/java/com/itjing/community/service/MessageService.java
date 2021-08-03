package com.itjing.community.service;

import com.itjing.community.entity.Message;

import java.util.List;

/**
 * @author: lijing
 * @Date: 2021年08月01日 11:34
 * @Description:
 */
public interface MessageService {

    /**
     * 查询当前用户的会话列表,针对每个会话只返回一条最新的私信.
     * @param userId
     * @param offset
     * @param limit
     * @return
     */
    List<Message> findConversations(int userId, int offset, int limit);

    /**
     * 查询当前用户的会话数量.
     * @param userId
     * @return
     */
    int findConversationCount(int userId);


    /**
     * 查询某个会话所包含的私信列表.
     * @param conversationId
     * @param offset
     * @param limit
     * @return
     */
    List<Message> findLetters(String conversationId, int offset, int limit);

    /**
     * 查询某个会话所包含的私信数量.
     * @param conversationId
     * @return
     */
    int findLetterCount(String conversationId);

    /**
     * 查询未读私信的数量
     * @param userId
     * @param conversationId
     * @return
     */
    int findLetterUnreadCount(int userId, String conversationId);

    /**
     * 新增消息
     * @param message
     * @return
     */
    int addMessage(Message message);

    /**
     * 修改消息的状态
     * @param ids
     * @return
     */
    int readMessage(List<Integer> ids);

    /**
     * 查询某个主题下最新的通知
     * @param userId
     * @param topic
     * @return
     */
    Message findLatestNotice(int userId, String topic);

    /**
     * 查询某个主题所包含的通知数量
     * @param userId
     * @param topic
     * @return
     */
    int findNoticeCount(int userId, String topic);

    /**
     * 查询未读的通知的数量
     * @param userId
     * @param topic
     * @return
     */
    int findNoticeUnreadCount(int userId, String topic);

    /**
     * 查询某个主题所包含的通知列表
     * @param userId
     * @param topic
     * @param offset
     * @param limit
     * @return
     */
    List<Message> findNotices(int userId, String topic, int offset, int limit);

    /**
     * 移除通知消息
     * @param ids
     * @return
     */
    int removeMessage(List<Integer> ids);
}
