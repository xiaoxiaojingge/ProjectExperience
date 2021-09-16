package com.itjing.qqclient.service;

import com.itjing.qqcommon.Message;
import com.itjing.qqcommon.MessageType;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.Date;

/**
 * @author: lijing
 * @Date: 2021年09月16日 13:53
 * @Description: 该类/对象，提供和消息相关的服务方法
 */
public class MessageClientService {
    /**
     * @param content  内容
     * @param senderId 发送者
     */
    public void sendMessageToAll(String content, String senderId) {
        //构建message
        Message message = new Message();
        //群发消息这种类型
        message.setMessageType(MessageType.MESSAGE_TO_ALL_MES);
        message.setSender(senderId);
        message.setContent(content);
        //发送时间设置到message对象
        message.setSendTime(new Date().toString());
        System.out.println(senderId + " 对大家说 " + content);
        //发送给服务端

        try {
            ObjectOutputStream oos =
                    new ObjectOutputStream(ManageClientConnectServerThread.getClientConnectServerThread(senderId).getSocket().getOutputStream());
            oos.writeObject(message);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * @param content    内容
     * @param senderId   发送用户id
     * @param receiverId 接收用户id
     */
    public void sendMessageToOne(String content, String senderId, String receiverId) {
        //构建message
        Message message = new Message();
        //普通的聊天消息这种类型
        message.setMessageType(MessageType.MESSAGE_COMM_MES);
        message.setSender(senderId);
        message.setReceiver(receiverId);
        message.setContent(content);
        //发送时间设置到message对象
        message.setSendTime(new Date().toString());
        System.out.println(senderId + " 对 " + receiverId + " 说 " + content);
        //发送给服务端

        try {
            ObjectOutputStream oos =
                    new ObjectOutputStream(ManageClientConnectServerThread.getClientConnectServerThread(senderId).getSocket().getOutputStream());
            oos.writeObject(message);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
