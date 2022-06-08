package qqserver.service;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

/**
 * @author 程柯嘉
 * @version 1.0
 * 该类用于管理和客户端通信的线程
 */
@SuppressWarnings({"all"})
public class ManageServerConnectClientThread {
    private static HashMap<String, ServerConnectClientThread> hm = new HashMap<>();

    public static HashMap<String, ServerConnectClientThread> getHm() {
        return hm;
    }

    public static void addServerConnectClientThread(String userId, ServerConnectClientThread serverConnectClientThread) {
        hm.put(userId, serverConnectClientThread);
    }

    public static ServerConnectClientThread getServerConnectClientThread(String userId) {
        return hm.get(userId);
    }

    public static String getOnlineUsers() {
        String onlineUserList = "";
        Iterator<String> iterator = hm.keySet().iterator();
        while (iterator.hasNext()) {
            onlineUserList += iterator.next().toString() + " ";
        }
        return onlineUserList;
    }

    public static void remove(String userId) {
        hm.remove(userId);
    }
}
