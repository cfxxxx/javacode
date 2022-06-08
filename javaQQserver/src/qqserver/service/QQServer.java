package qqserver.service;

import qqcommon.Message;
import qqcommon.MessageType;
import qqcommon.User;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author 程柯嘉
 * @version 1.0
 * 服务端监听9999端口，等待客户端的连接并保持通讯
 */
@SuppressWarnings({"all"})
public class QQServer {
    //创建一个集合，存放多个用户，如果这些用户登录，就认为是合法
    private static HashMap<String, User> validUsers = new HashMap<>();

    //存放离线消息

    private static ConcurrentHashMap<String, ArrayList<Message>> hm = new ConcurrentHashMap<>();


    public static ConcurrentHashMap<String, ArrayList<Message>> getHm() {
        return hm;
    }

    static {//在静态代码块初始化validUsers
        validUsers.put("100", new User("100", "123456"));
        validUsers.put("200", new User("200", "123456"));
        validUsers.put("300", new User("300", "123456"));
        validUsers.put("400", new User("400", "123456"));
        validUsers.put("程柯嘉", new User("程柯嘉", "123456"));
        validUsers.put("张佳乐", new User("张佳乐", "123456"));
        validUsers.put("小佳佳", new User("小佳佳", "123456"));

    }

    private ServerSocket serverSocket = null;

    public QQServer() {
        System.out.println("服务端在9999端口监听");
        try {
            new Thread(new SendNewsToAll()).start();
            serverSocket = new ServerSocket(9999);


            //监听是一直在监听的，每当和某个客户端建立连接，就会继续监听
            while (true) {
                //如果没有客户端连接，则会阻塞到这里
                Socket socket = serverSocket.accept();

                ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
                ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
                User user = (User) ois.readObject();
                Message message = new Message();


                if (checkUser(user.getUserId(), user.getPasswd())) {
                    message.setMessageType(MessageType.MESSAGE_LOGIN_SUCCEED);
                    oos.writeObject(message);
                    ServerConnectClientThread scct = new ServerConnectClientThread(socket, user.getUserId());
                    scct.start();
                    //把线程对象放到集合中方便管理
                    ManageServerConnectClientThread.addServerConnectClientThread(user.getUserId(), scct);

                    //当hm中有给目标发送的离线消息时
                    if (hm.containsKey(user.getUserId())) {
                        Iterator<Message> iterator = hm.get(user.getUserId()).iterator();
                        while (iterator.hasNext()) {
                            Message m = iterator.next();
                            ObjectOutputStream objectOutputStream = new ObjectOutputStream(scct.getSocket().getOutputStream());
                            objectOutputStream.writeObject(m);
                        }
                    }
                } else {
                    System.out.println("用户Id = " + user.getUserId() + " pwd = " + user.getPasswd() + " 验证失败");
                    message.setMessageType(MessageType.MESSAGE_LOGIN_FAILED);
                    oos.writeObject(message);
                    //关闭socket
                    socket.close();
                }
            }
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        } finally {
            //如果服务端退出while循环，说明服务器不再监听
            try {
                serverSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private boolean checkUser(String userId, String pwd) {
        User user = validUsers.get(userId);
        if (user == null) {//说明userId不在用户列表中
            return false;
        }
        if (!user.getPasswd().equals(pwd)) {//说明userId和pwd不匹配
            return false;
        }
        return true;
    }
}
