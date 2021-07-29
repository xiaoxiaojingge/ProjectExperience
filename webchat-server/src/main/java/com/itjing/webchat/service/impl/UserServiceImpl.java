package com.itjing.webchat.service.impl;

import com.itjing.webchat.enums.MsgActionEnum;
import com.itjing.webchat.enums.MsgSignFlagEnum;
import com.itjing.webchat.enums.SearchFriendsStatusEnum;
import com.itjing.webchat.idworker.Sid;
import com.itjing.webchat.mapper.*;
import com.itjing.webchat.netty.DataContent;
import com.itjing.webchat.netty.UserChanelRel;
import com.itjing.webchat.pojo.ChatMsg;
import com.itjing.webchat.pojo.FriendsRequest;
import com.itjing.webchat.pojo.MyFriends;
import com.itjing.webchat.pojo.User;
import com.itjing.webchat.service.UserService;
import com.itjing.webchat.utils.FastDFSClient;
import com.itjing.webchat.utils.FileUtils;
import com.itjing.webchat.utils.JsonUtils;
import com.itjing.webchat.utils.QRCodeUtils;
import com.itjing.webchat.vo.FriendsRequestVO;
import com.itjing.webchat.vo.MyFriendsVO;
import io.netty.channel.Channel;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Date;
import java.util.List;

/**
 * @author: lijing
 * @Date: 2021年07月27日 16:33
 * @Description:
 */
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    UserMapper userMapper;

    @Autowired
    MyFriendsMapper myFriendsMapper;

    @Autowired
    FriendsRequestMapper friendsRequestMapper;

    @Autowired
    UserMapperCustom userMapperCustom;

    @Autowired
    ChatMsgMapper chatMsgMapper;

    @Autowired
    Sid sid;

    @Autowired
    QRCodeUtils qrCodeUtils;

    @Autowired
    FastDFSClient fastDFSClient;

    @Override
    public User getUserById(String id) {
        return userMapper.selectByPrimaryKey(id);
    }

    @Override
    public User queryUserNameIsExit(String username) {
        User user = userMapper.queryUserNameIsExit(username);
        return user;
    }

    @Override
    public User insert(User user) {
        String userId = sid.nextShort();
        //为每个注册用户生成一个唯一的二维码
        String qrCodePath = "/usr/local/qrcode/" + userId + "qrcode.png";
        //创建二维码对象信息
        qrCodeUtils.createQRCode(qrCodePath, "bird_qrcode:" + user.getUsername());
        MultipartFile qrcodeFile = FileUtils.fileToMultipart(qrCodePath);
        String qrCodeURL = "";
        try {
            qrCodeURL = fastDFSClient.uploadQRCode(qrcodeFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
        user.setId(userId);
        user.setQrcode(qrCodeURL);
        userMapper.insert(user);
        return user;
    }

    @Override
    public User updateUserInfo(User user) {
        userMapper.updateByPrimaryKeySelective(user);
        User result = userMapper.selectByPrimaryKey(user.getId());
        return result;
    }

    @Override
    public Integer preconditionSearchFriends(String myUserId, String friendUserName) {
        User user = queryUserNameIsExit(friendUserName);
        //1.搜索的用户如果不存在，则返回【无此用户】
        if (user == null) {
            return SearchFriendsStatusEnum.USER_NOT_EXIST.status;
        }
        //2.搜索的账号如果是你自己，则返回【不能添加自己】
        if (myUserId.equals(user.getId())) {
            return SearchFriendsStatusEnum.NOT_YOURSELF.status;
        }
        //3.搜索的朋友已经是你好友，返回【该用户已经是你的好友】
        MyFriends myfriend = new MyFriends();
        myfriend.setMyUserId(myUserId);
        myfriend.setMyFriendUserId(user.getId());
        MyFriends myF = myFriendsMapper.selectOneByExample(myfriend);
        if (myF != null) {
            return SearchFriendsStatusEnum.ALREADY_FRIENDS.status;
        }
        return SearchFriendsStatusEnum.SUCCESS.status;
    }

    @Override
    public void sendFriendRequest(String myUserId, String friendUserName) {
        User user = queryUserNameIsExit(friendUserName);
        MyFriends myfriend = new MyFriends();
        myfriend.setMyUserId(myUserId);
        myfriend.setMyFriendUserId(user.getId());
        MyFriends myF = myFriendsMapper.selectOneByExample(myfriend);
        if (myF == null) {
            FriendsRequest friendsRequest = new FriendsRequest();
            String requestId = sid.nextShort();
            friendsRequest.setId(requestId);
            friendsRequest.setSendUserId(myUserId);
            friendsRequest.setAcceptUserId(user.getId());
            friendsRequest.setRequestDateTime(new Date());
            friendsRequestMapper.insert(friendsRequest);
        }
    }

    @Override
    public List<FriendsRequestVO> queryFriendRequestList(String acceptUserId) {
        return userMapperCustom.queryFriendRequestList(acceptUserId);
    }

    @Override
    public void deleteFriendRequest(FriendsRequest friendsRequest) {
        friendsRequestMapper.deleteByFriendRequest(friendsRequest);
    }

    @Override
    public void passFriendRequest(String sendUserId, String acceptUserId) {
        //进行双向好友数据保存
        saveFriends(sendUserId, acceptUserId);
        saveFriends(acceptUserId, sendUserId);

        //删除好友请求表中的数据
        FriendsRequest friendsRequest = new FriendsRequest();
        friendsRequest.setSendUserId(sendUserId);
        friendsRequest.setAcceptUserId(acceptUserId);
        deleteFriendRequest(friendsRequest);

        Channel sendChannel = UserChanelRel.get(sendUserId);
        if (sendChannel != null) {
            //使用websocket 主动推送消息到请求发起者，更新他的通讯录列表为最新
            DataContent dataContent = new DataContent();
            dataContent.setAction(MsgActionEnum.PULL_FRIEND.type);

            //消息的推送
            sendChannel.writeAndFlush(new TextWebSocketFrame(JsonUtils.objectToJson(dataContent)));
        }
    }

    //通过好友请求并保存数据到my_friends 表中
    private void saveFriends(String sendUserId, String acceptUserId) {
        MyFriends myFriends = new MyFriends();
        String recordId = sid.nextShort();

        myFriends.setId(recordId);
        myFriends.setMyUserId(sendUserId);
        myFriends.setMyFriendUserId(acceptUserId);

        myFriendsMapper.insert(myFriends);
    }

    @Override
    public List<MyFriendsVO> queryMyFriends(String userId) {
        return userMapperCustom.queryMyFriends(userId);
    }



    @Override
    public String saveMsg(com.itjing.webchat.netty.ChatMsg chatMsg) {
        ChatMsg msgDB = new ChatMsg();
        String msgId = sid.nextShort();
        msgDB.setId(msgId);
        msgDB.setAcceptUserId(chatMsg.getReceiverId());
        msgDB.setSendUserId(chatMsg.getSenderId());
        msgDB.setCreateTime(new Date());
        msgDB.setSignFlag(MsgSignFlagEnum.unsign.type);
        msgDB.setMsg(chatMsg.getMsg());

        chatMsgMapper.insert(msgDB);

        return msgId;
    }

    @Override
    public void updateMsgSigned(List<String> msgIdList) {
        userMapperCustom.batchUpdateMsgSigned(msgIdList);
    }

    @Override
    public List<ChatMsg> getUnReadMsgList(String acceptUserId) {
        List<ChatMsg> result = chatMsgMapper.getUnReadMsgListByAcceptUid(acceptUserId);
        return result;
    }

}
