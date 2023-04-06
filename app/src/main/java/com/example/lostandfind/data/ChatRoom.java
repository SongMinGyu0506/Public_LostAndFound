package com.example.lostandfind.data;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.TimeZone;

//1:1 채팅방을 위한 클래스 개인별 채팅 목록도 해당 클래스를 이용할 예정
public class ChatRoom implements Serializable {
    private String userEmail1; //나 또는 상대방
    private String userEmail2;
    private String userName1; //나 또는 상대방
    private String userName2;
    private ArrayList<ChatItem> chatItems;
    private String chatRoomTime;

    private String TEMPLATE = "yyyy-MM-dd HH:mm";
    private SimpleDateFormat simpleDateFormat = new SimpleDateFormat(TEMPLATE);

    public ChatRoom() {}
    public ChatRoom(String userName1, String userName2,
                    ArrayList<ChatItem> chatItems,
                    String userEmail1, String userEmail2) {

        this.userName1 = userName1;
        this.userName2 = userName2;

        this.chatItems = chatItems;

        this.userEmail1 = userEmail1;
        this.userEmail2 = userEmail2;

        simpleDateFormat.setTimeZone(TimeZone.getTimeZone("Asia/Seoul"));
        this.chatRoomTime = simpleDateFormat.format(new Date()).toString();
    }

    public String getUserName1() {
        return userName1;
    }

    public void setUserName1(String userName1) {
        this.userName1 = userName1;
    }

    public String getUserName2() {
        return userName2;
    }

    public void setUserName2(String userName2) {
        this.userName2 = userName2;
    }

    public ArrayList<ChatItem> getChatItems() {
        return chatItems;
    }

    public void setChatItems(ArrayList<ChatItem> chatItems) {
        this.chatItems = chatItems;
    }

    public String getChatRoomTime() {
        return chatRoomTime;
    }

    public void setChatRoomTime(String chatRoomTime) {
        this.chatRoomTime = chatRoomTime;
    }

    public String getUserEmail1() {
        return userEmail1;
    }

    public void setUserEmail1(String userEmail1) {
        this.userEmail1 = userEmail1;
    }

    public String getUserEmail2() {
        return userEmail2;
    }

    public void setUserEmail2(String userEmail2) {
        this.userEmail2 = userEmail2;
    }

    public void addChatItem(ChatItem chatItem) {
        chatItems.add(chatItem);
    }
}
