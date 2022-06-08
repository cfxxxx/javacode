package qqserver.service;

import qqcommon.Message;
import qqcommon.MessageType;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.*;

/**
 * @author 程柯嘉
 * @version 1.0
 */
@SuppressWarnings({"all"})
public class SendNewsToAll implements Runnable{
    private Scanner scanner = new Scanner(System.in);

    @Override
    public void run() {
        while (true) {
            System.out.println("请输入服务器要推送的新闻/消息【输入exit表示退出推送服务】：");
            String news = scanner.next();
            if (news.equals("exit")) {
                break;
            }
            Message message = new Message();
            message.setSender("服务器");
            message.setMessageType(MessageType.MESSAGE_TO_ALL_MES);
            message.setContent(news);
            message.setTime(new Date().toString());

            System.out.println("服务器推送消息给所有人说：" + news);

            HashMap<String, ServerConnectClientThread> hm = ManageServerConnectClientThread.getHm();
            Set<String> userIds = hm.keySet();
            Iterator<String> iterator = userIds.iterator();

            while (iterator.hasNext()) {
                String onlineUser = iterator.next().toString();
                try {
                    ObjectOutputStream oos = new ObjectOutputStream(hm.get(onlineUser).getSocket().getOutputStream());
                    oos.writeObject(message);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
