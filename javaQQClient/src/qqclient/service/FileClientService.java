package qqclient.service;

import qqcommon.Message;
import qqcommon.MessageType;

import java.io.*;

/**
 * @author 程柯嘉
 * @version 1.0
 */
@SuppressWarnings({"all"})
public class FileClientService {
    public static void sendFileToOne(String sender, String reciever, String filePath, String recievePath) {
        Message message = new Message();
        message.setSender(sender);
        message.setReciever(reciever);
        message.setMessageType(MessageType.MESSAGE_FILE_MES);
        message.setAimPath(recievePath);
        message.setFilePath(filePath);

        BufferedInputStream bis = null;
        try {
            bis = new BufferedInputStream(new FileInputStream(filePath));
            byte[] bytes = new byte[(int) new File(filePath).length()];
            bis.read(bytes);
            message.setBytes(bytes);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (bis != null) {
                try {
                    bis.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        System.out.println(sender + " 向 " + reciever + " 发送文件 " + filePath + " 到对方的 " + recievePath);

        try {
            ObjectOutputStream oos = new ObjectOutputStream(ManageClientConnectServerThread.getClientConnectServerThread(sender).getSocket().getOutputStream());
            oos.writeObject(message);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
