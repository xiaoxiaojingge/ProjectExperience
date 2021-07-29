package com.itjing.webchat.service;

import com.itjing.webchat.pojo.ChatMsg;
import com.itjing.webchat.pojo.FriendsRequest;
import com.itjing.webchat.pojo.User;
import com.itjing.webchat.vo.FriendsRequestVO;
import com.itjing.webchat.vo.MyFriendsVO;

import java.util.List;

/**
 * @author: lijing
 * @Date: 2021年07月27日 16:31
 * @Description:
 */
public interface UserService {

    /**
     * 根据用户id查找
     * @param id
     * @return
     */
    User getUserById(String id);

    /**
     * 根据用户名查找指定用户对象
     * @param username
     * @return
     */
    User queryUserNameIsExit(String username);

    /**
     * 保存,新增用户
     * @param user
     * @return
     */
    User insert(User user);

    /**
     * 修改用户
     * @param user
     * @return
     */
    User updateUserInfo(User user);

    /**
     * 搜索好友的前置条件接口
     * @param myUserId
     * @param friendUserName
     * @return
     */
    Integer preconditionSearchFriends(String myUserId, String friendUserName);

    /**
     * 发送好友请求
     * @param myUserId
     * @param friendUserName
     */
    void  sendFriendRequest(String myUserId,String friendUserName);

    /**
     * 好友请求列表查询
     * @param acceptUserId
     * @return
     */
    List<FriendsRequestVO> queryFriendRequestList(String acceptUserId);

    /**
     * 处理好友请求——忽略好友请求
     * @param friendsRequest
     */
    void deleteFriendRequest(FriendsRequest friendsRequest);

    /**
     * 处理好友请求——通过好友请求
     * @param sendUserId
     * @param acceptUserId
     */
    void passFriendRequest(String sendUserId,String acceptUserId);

    /**
     * 好友列表查询
     * @param userId
     * @return
     */
    List<MyFriendsVO> queryMyFriends(String userId);

    /**
     * 保存用户聊天消息
     * @param chatMsg
     * @return
     */
    String saveMsg(com.itjing.webchat.netty.ChatMsg chatMsg);

    void updateMsgSigned(List<String> msgIdList);

    /**
     * 获取未签收的消息列表
     * @param acceptUserId
     * @return
     */
    List<ChatMsg> getUnReadMsgList(String acceptUserId);
}
