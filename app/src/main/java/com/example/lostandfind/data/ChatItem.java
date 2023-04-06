package com.example.lostandfind.data;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

//해당 클래스는 채팅 내용 각각의 데이터 저장을 위한 클래스임(채팅방X)
public class ChatItem {
    //채팅 메타데이터
    private String userName;
    private String userEmail;
    private String chatTime;

    private String TEMPLATE = "yyyy-MM-dd HH:mm";
    private SimpleDateFormat simpleDateFormat = new SimpleDateFormat(TEMPLATE);

    //채팅 내용
    private String context;

    public ChatItem(){}
    public ChatItem(String userName, String userEmail, String context, String chatTime) {
        simpleDateFormat.setTimeZone(TimeZone.getTimeZone("Asia/Seoul"));

        this.userName = userName;
        this.userEmail = userEmail;
        this.context = context;
        this.chatTime = simpleDateFormat.format(new Date()).toString();
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public String getChatTime() {
        return chatTime;
    }

    public void setChatTime(String chatTime) {
        this.chatTime = chatTime;
    }

    public String getContext() {
        return context;
    }

    public void setContext(String context) {
        this.context = context;
    }
}
