package qqclient.service;

import java.util.HashMap;

/**
 * @author 程柯嘉
 * @version 1.0
 * 该是类管理客户端连接到服务器端的线程的类
 */
@SuppressWarnings({"all"})
public class ManageClientConnectServerThread {
    //把多个线程放入一个HashMap集合，key是账号，value是线程
    private static HashMap<String, ClientConnectServerThread> hm = new HashMap<>();

    public static void addClientConnectServerThread(String userId, ClientConnectServerThread clientConnectServerThread) {
        hm.put(userId, clientConnectServerThread);
    }

    public static ClientConnectServerThread getClientConnectServerThread(String userId) {
        return  hm.get(userId);
    }
}
