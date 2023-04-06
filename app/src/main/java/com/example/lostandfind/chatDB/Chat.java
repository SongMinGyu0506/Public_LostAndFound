package com.example.lostandfind.chatDB;

import com.google.firebase.firestore.ServerTimestamp;

import java.util.Date;

public class Chat {
    private String senderUID;
    private String senderName;
    private String text;
    private String sendDate;

    public Chat() {}

    public Chat(String senderUID, String senderName, String text, String sendDate){
        this.senderUID = senderUID;
        this.senderName = senderName;
        this.text = text;
        this.sendDate = sendDate;
    }

    public String getSenderUID() {
        return senderUID;
    }
    public void setSenderUID(String senderUID) {
        this.senderUID = senderUID;
    }
    public String getSenderName() {
        return senderName;
    }
    public void setSenderName(String senderName) {
        this.senderName = senderName;
    }
    public String getText() {
        return text;
    }
    public void setText(String text) {
        this.text = text;
    }
    public String getSendDate() {
        return sendDate;
    }
    public void setSendDate(String sendDate) {
        this.sendDate = sendDate;
    }
}
