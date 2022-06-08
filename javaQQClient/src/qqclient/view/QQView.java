package qqclient.view;

import qqclient.service.FileClientService;
import qqclient.service.MessageClientService;
import qqclient.service.UserClientService;

import java.util.Scanner;

/**
 * @author 程柯嘉
 * @version 1.0
 * 客户端的菜单页面
 */
@SuppressWarnings({"all"})
public class QQView {

    public static void main(String[] args) {
        mainMenu();
        System.out.println("客户端退出系统");
    }

    //显示初始主菜单
    private static void mainMenu() {
        String key = null;
        boolean loop = true;
        UserClientService userClientService = new UserClientService();
        Scanner s = new Scanner(System.in);

        while (loop) {
            System.out.println("==========欢迎登录网络通信系统==========");
            System.out.println("\t\t\t 1 登录系统");
            System.out.println("\t\t\t 9 退出系统");
            System.out.print("请输入你的选择：");
            key = s.next();

            switch (key) {
                case "1":
                    System.out.print("请输入账号：");
                    String userId = s.next();
                    System.out.print("请输入密码：");
                    String passwd = s.next();

                    //此时需要到服务端验证是否存在用户
                    if (userClientService.checkUser(userId, passwd)) {
                        secondMenu(userId, s, userClientService);
                    } else {
                        System.out.println("登录失败");
                    }
                    break;
                case "9":
                    System.out.println("退出成功");
                    loop = false;
                    break;
            }
        }
    }

    //显示二级菜单
    private static void secondMenu(String userId, Scanner s, UserClientService userClientService) {
        boolean loop = true;
        String key = null;
        System.out.println("==========欢迎(用户" + userId + ")登录成功==========");

        while (loop) {
            System.out.println("==========网络通信系统二级菜单(用户" + userId + ")==========");
            System.out.println("\t\t\t 1 显示在线用户列表");
            System.out.println("\t\t\t 2 群发消息");
            System.out.println("\t\t\t 3 私聊消息");
            System.out.println("\t\t\t 4 发送文件");
            System.out.println("\t\t\t 9 退出登录");
            System.out.print("请输入你的选择：");
            key = s.next();

            switch (key) {
                case "1":
                    userClientService.onlineFriendList();
                    try {
                        Thread.currentThread().sleep(500);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    break;
                case "2":
                    System.out.println("请输入想对大家说的话");
                    String content = s.next();
                    MessageClientService.sendMessageToAll(content, userId);
                    try {
                        Thread.currentThread().sleep(500);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    break;
                case "3":
                    System.out.print("请输入你想要私聊的用户（在线）：");
                    String reciever = s.next();
                    System.out.print("请输入想说的话：");
                    content = s.next();
                    MessageClientService.sendMessageToOne(content, userId, reciever);
                    try {
                        Thread.currentThread().sleep(500);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    break;
                case "4":
                    System.out.print("请输入你想要发送文件的用户：");
                    String recieverId = s.next();
                    System.out.print("请输入你想要发送的文件完整路径：");
                    String filePath = s.next();
                    System.out.print("请输入你想要发送文件到对方的路径：");
                    String recievePath = s.next();
                    FileClientService.sendFileToOne(userId, recieverId, filePath, recievePath);
                    try {
                        Thread.currentThread().sleep(500);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    break;
                case "9":
                    userClientService.logout();
                    loop = false;
                    break;
            }

        }
    }


}
