package qqclient.service;

import qqcommon.Message;
import qqcommon.MessageType;

import java.io.*;
import java.net.Socket;

/**
 * @author 程柯嘉
 * @version 1.0
 */
@SuppressWarnings({"all"})
public class ClientConnectServerThread extends Thread {
    private Socket socket;

    public ClientConnectServerThread(Socket socket) {
        this.socket = socket;
    }

    public Socket getSocket() {
        return socket;
    }

    @Override
    public void run() {
        //因为Thread需要在后台和服务器通信，因此做成while循环
        while (true) {
            try {
                ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
                //如果服务器没有发送Message对象，线程会阻塞在这里
                Message message = (Message) ois.readObject();

                switch (message.getMessageType()) {
                    case MessageType.MESSAGE_RET_ONLINE_FRIEND:
                        String[] onlineUsers = message.getContent().split(" ");
                        System.out.println("当前在线用户列表如下：");
                        for (int i = 0; i < onlineUsers.length; i++) {
                            System.out.println("用户：" + onlineUsers[i]);
                        }
                        break;
                    case MessageType.MESSAGE_COMM_MES:
                        //把从服务器端转发的消息显示到控制台
                        System.out.println("\n" + message.getSender() + " 对 " + message.getReciever() + " 说 " + message.getContent());
                        break;
                    case MessageType.MESSAGE_TO_ALL_MES:
                        System.out.println("\n" + message.getSender() + " 对 大家 说" + message.getContent());
                        break;
                    case MessageType.MESSAGE_FILE_MES:
                        System.out.println("\n" + message.getReciever() + " 收到了来自 " + message.getSender() + " 的文件 " + message.getFilePath() + " 存放到 " + message.getAimPath());
                        byte[] bytes = message.getBytes();
                        BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(message.getAimPath()));
                        bos.write(bytes);
                        System.out.println("保存文件成功~~");
                }
            } catch (IOException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
    }
}
