package qqserver.service;

import qqcommon.Message;
import qqcommon.MessageType;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

/**
 * @author 程柯嘉
 * @version 1.0
 * 该类对应的一个线程和某个客户端保持通信
 */
@SuppressWarnings({"all"})
public class ServerConnectClientThread extends Thread{
    private Socket socket;
    private String userId;//连接到服务端的用户Id

    public ServerConnectClientThread(Socket socket, String userId) {
        this.socket = socket;
        this.userId = userId;
    }

    public Socket getSocket() {
        return socket;
    }

    @Override
    public void run() {
        boolean loop = true;
        while (loop) {
            try {
                System.out.println("服务端和客户端" + userId + "保持通信，读取数据...");
                ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
                Message message = (Message) ois.readObject();

                switch (message.getMessageType()) {
                    case MessageType.MESSAGE_GET_ONLINE_FRIEND:
                        System.out.println(message.getSender() + "要求返回在线用户列表");
                        String onlineUserList = ManageServerConnectClientThread.getOnlineUsers();

                        Message message2 = new Message();
                        message2.setMessageType(MessageType.MESSAGE_RET_ONLINE_FRIEND);
                        message2.setContent(onlineUserList);

                        ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
                        oos.writeObject(message2);
                        break;
                    case MessageType.MESSAGE_CLIENT_EXIT:
                        System.out.println(userId + "退出");
                        ManageServerConnectClientThread.remove(userId);
                        socket.close();//关闭连接
                        loop = false;
                        break;
                    case MessageType.MESSAGE_COMM_MES:

                        if (ManageServerConnectClientThread.getServerConnectClientThread(message.getReciever()) != null) {
                            ObjectOutputStream ooStream = new ObjectOutputStream(ManageServerConnectClientThread.getServerConnectClientThread(message.getReciever()).getSocket().getOutputStream());
                            ooStream.writeObject(message);
                        } else {
                            ArrayList<Message> arrayList = new ArrayList<>();
                            arrayList.add(message);
                            QQServer.getHm().put(message.getReciever(), arrayList);
                        }
                        break;
                    case MessageType.MESSAGE_TO_ALL_MES:
                        HashMap<String, ServerConnectClientThread> hm = ManageServerConnectClientThread.getHm();
                        Iterator<String> iterator = hm.keySet().iterator();
                        while (iterator.hasNext()) {
                            String onlineUserId = iterator.next();
                            if (!onlineUserId.equals(message.getSender())) {//排除发送者
                                ObjectOutputStream objectOutputStream = new ObjectOutputStream(hm.get(onlineUserId).getSocket().getOutputStream());
                                objectOutputStream.writeObject(message);
                            }
                        }
                        break;
                    case MessageType.MESSAGE_FILE_MES:
                        ObjectOutputStream objectOutputStream = new ObjectOutputStream(ManageServerConnectClientThread.getServerConnectClientThread(message.getReciever()).getSocket().getOutputStream());
                        objectOutputStream.writeObject(message);
                        break;
                }
            } catch (IOException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
    }
}
