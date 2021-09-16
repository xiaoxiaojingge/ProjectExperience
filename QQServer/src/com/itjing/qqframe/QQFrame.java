package com.itjing.qqframe;

import com.itjing.qqserver.service.QQServer;

/**
 * @author: lijing
 * @Date: 2021年09月16日 12:22
 * @Description: 该类创建QQServer ,启动后台的服务
 */
public class QQFrame {
    public static void main(String[] args) {
        new QQServer();
    }
}