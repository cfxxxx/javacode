package qqclient.service;

import qqcommon.Message;
import qqcommon.MessageType;
import qqcommon.User;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;

/**
 * @author 程柯嘉
 * @version 1.0
 * 该类完成用户登录验证和用户注册功能
 */
@SuppressWarnings({"all"})
public class UserClientService {

    private boolean result = false;
    private User user = new User();
    private Socket socket;

    //根据userId和pwd到服务器验证该用户是否合法
    public boolean checkUser(String userId, String pwd) {
        user.setUserId(userId);
        user.setPasswd(pwd);

        try {
            socket = new Socket(InetAddress.getLocalHost(), 9999);

            ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
            oos.writeObject(user);//发送User对象交给服务端验证是否合法

            //读取从服务器返回的Message对象
            ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
            Message ms = (Message) ois.readObject();

            if (ms.getMessageType().equals(MessageType.MESSAGE_LOGIN_SUCCEED)) {//如果登录成功
                //创建一个和服务器通信的用户线程
                ClientConnectServerThread clientConnectServerThread = new ClientConnectServerThread(socket);
                clientConnectServerThread.start();
                //将用户线程加入到管理用户线程的集合中
                ManageClientConnectServerThread.addClientConnectServerThread(userId, clientConnectServerThread);
                result = true;
            } else {//如果登录失败，就不能启动和服务器通信的线程，关闭socket
                socket.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return result;
    }

    //向服务器端请求在线用户列表
    public void onlineFriendList() {
        Message message = new Message();
        message.setMessageType(MessageType.MESSAGE_GET_ONLINE_FRIEND);
        message.setSender(user.getUserId());
        //向服务端发送message包
        //应得到当前线程的Socket对应的ObjectOutputStream对象
        try {
            ObjectOutputStream oos = new ObjectOutputStream(ManageClientConnectServerThread.getClientConnectServerThread(user.getUserId()).getSocket().getOutputStream());
            oos.writeObject(message);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //客户端退出
    public void logout() {
        Message message = new Message();
        message.setMessageType(MessageType.MESSAGE_CLIENT_EXIT);
        try {
            ObjectOutputStream oos = new ObjectOutputStream(ManageClientConnectServerThread.getClientConnectServerThread(user.getUserId()).getSocket().getOutputStream());
            oos.writeObject(message);
            System.out.println(user.getUserId() + "退出系统");
            System.exit(0);//退出系统,结束进程
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
