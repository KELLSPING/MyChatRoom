package com.example.mychatroom;

public class MsgModel {
    private String msgId, senderId, msg;

    public MsgModel(String msgId, String senderId, String msg) {
        this.msgId = msgId;
        this.senderId = senderId;
        this.msg = msg;
    }

    public MsgModel() {
    }

    public String getMsgId() {
        return msgId;
    }

    public void setMsgId(String msgId) {
        this.msgId = msgId;
    }

    public String getSenderId() {
        return senderId;
    }

    public void setSenderId(String senderId) {
        this.senderId = senderId;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
