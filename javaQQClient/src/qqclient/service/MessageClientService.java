package qqclient.service;

import qqcommon.Message;
import qqcommon.MessageType;

import java.io.*;
import java.util.Date;

/**
 * @author 程柯嘉
 * @version 1.0
 * 该对象提供和消息相关的方法
 */
@SuppressWarnings({"all"})
public class MessageClientService {
    //实现私聊
    public static void sendMessageToOne(String content, String sender, String reciever) {
        Message message = new Message();
        message.setSender(sender);
        message.setMessageType(MessageType.MESSAGE_COMM_MES);
        message.setReciever(reciever);
        message.setContent(content);
        message.setTime(new Date().toString());//发送时间
        System.out.println(sender + "对" + reciever + "说" + content);

        try {
            ObjectOutputStream oos = new ObjectOutputStream(ManageClientConnectServerThread.getClientConnectServerThread(sender).getSocket().getOutputStream());
            oos.writeObject(message);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //实现群发
    public static void sendMessageToAll(String content, String sender) {
        Message message = new Message();
        message.setSender(sender);
        message.setMessageType(MessageType.MESSAGE_TO_ALL_MES);
        message.setContent(content);
        message.setTime(new Date().toString());//发送时间
        System.out.println(sender + "对 大家 说" + content);

        try {
            ObjectOutputStream oos = new ObjectOutputStream(ManageClientConnectServerThread.getClientConnectServerThread(sender).getSocket().getOutputStream());
            oos.writeObject(message);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
