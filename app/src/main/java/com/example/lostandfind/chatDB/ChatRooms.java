package com.example.lostandfind.chatDB;


import com.google.firebase.firestore.Exclude;

import java.io.Serializable;

public class ChatRooms implements Serializable{
//    @Exclude private String id;

    String id;
    String receiverUID;
    String receiverName;
    String senderUID;
    String senderName;
    String lastChat;
    String lastChatTime;

    public ChatRooms(){ }

    public ChatRooms(String id, String receiverUID, String receiverName, String senderUID, String senderName){
        this.id = id;
        this.receiverUID = receiverUID;
        this.receiverName = receiverName;
        this.senderUID = senderUID;
        this.senderName = senderName;
    }

    public ChatRooms(String id, String receiverUID, String receiverName, String senderUID, String senderName,
                     String lastChat, String lastChatTime){
        this.id = id;
        this.receiverUID = receiverUID;
        this.receiverName = receiverName;
        this.senderUID = senderUID;
        this.senderName = senderName;
        this.lastChat = lastChat;
        this.lastChatTime = lastChatTime;
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

    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }

    public String getReceiverName() {
        return receiverName;
    }
    public void setReceiverName(String receiverName) {
        this.receiverName = receiverName;
    }
    public String getReceiverUID() {
        return receiverUID;
    }
    public void setReceiverUID(String receiverUID) {
        this.receiverUID = receiverUID;
    }

    public String getLastChat() {
        return lastChat;
    }
    public void setLastChat(String lastChat) {
        this.lastChat = lastChat;
    }
    public String getLastChatTime() {
        return lastChatTime;
    }
    public void setLastChatTime(String lastChatTime) {
        this.lastChatTime = lastChatTime;
    }
}
