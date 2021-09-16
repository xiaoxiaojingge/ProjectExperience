package com.itjing.qqcommon;

import java.io.Serializable;

/**
 * @author: lijing
 * @Date: 2021年09月16日 10:44
 * @Description: 客户端和服务端通信时的消息实体
 */
public class Message implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 发送方
     */
    private String sender;

    /**
     * 接受方
     */
    private String receiver;

    /**
     * 消息内容
     */
    private String content;

    /**
     * 发送时间
     */
    private String sendTime;

    /**
     * 消息类型
     */
    private String messageType;

    /**
     * 进行扩展 和文件相关的成员
     */
    private byte[] fileBytes;

    private int fileLen = 0;

    /**
     * 将文件传输到哪里
     */
    private String dest;

    /**
     * 源文件路径
     */
    private String src;

    public byte[] getFileBytes() {
        return fileBytes;
    }

    public void setFileBytes(byte[] fileBytes) {
        this.fileBytes = fileBytes;
    }

    public int getFileLen() {
        return fileLen;
    }

    public void setFileLen(int fileLen) {
        this.fileLen = fileLen;
    }

    public String getDest() {
        return dest;
    }

    public void setDest(String dest) {
        this.dest = dest;
    }

    public String getSrc() {
        return src;
    }

    public void setSrc(String src) {
        this.src = src;
    }

    public String getMessageType() {
        return messageType;
    }

    public void setMessageType(String messageType) {
        this.messageType = messageType;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getReceiver() {
        return receiver;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getSendTime() {
        return sendTime;
    }

    public void setSendTime(String sendTime) {
        this.sendTime = sendTime;
    }
}
