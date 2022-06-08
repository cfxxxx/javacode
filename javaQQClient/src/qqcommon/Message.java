package qqcommon;

import java.io.Serializable;

/**
 * @author 程柯嘉
 * @version 1.0
 * 表示客户端和服务器端通信时的消息对象
 */
@SuppressWarnings({"all"})
public class Message implements Serializable {
    private static final long serialVersionUID = 1L;
    private String sender;//发送方
    private String reciever;//接收方
    private String content;//消息内容
    private String time;//发送时间
    private String messageType;//消息类型

    //发送文件的扩展
    private byte[] bytes;
    private int len;
    private String filePath;
    private String aimPath;

    public byte[] getBytes() {
        return bytes;
    }

    public void setBytes(byte[] bytes) {
        this.bytes = bytes;
    }

    public int getLen() {
        return len;
    }

    public void setLen(int len) {
        this.len = len;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public String getAimPath() {
        return aimPath;
    }

    public void setAimPath(String aimPath) {
        this.aimPath = aimPath;
    }

    public String getMessageType() {
        return messageType;
    }

    public void setMessageType(String messageType) {
        this.messageType = messageType;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getReciever() {
        return reciever;
    }

    public void setReciever(String reciever) {
        this.reciever = reciever;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
