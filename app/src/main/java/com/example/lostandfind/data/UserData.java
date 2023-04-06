package com.example.lostandfind.data;

import java.time.LocalDateTime;

public class UserData {
    private String uid; //UID
    private String email; //email
    private String name;
    private String localTime;

    public UserData() {}

    public UserData(String UID, String email, String name) {
        this.uid = UID;
        this.email = email;
        this.name = name;
        localTime = LocalDateTime.now().toString();
    }

    public UserData(String UID, String email, String name, String localTime) {
        this.uid = UID;
        this.email = email;
        this.name = name;
        this.localTime = localTime;
    }

    public String getUID() {
        return uid;
    }

    public void setUID(String UID) {
        this.uid = UID;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLocalTime() {
        return localTime;
    }

    public void setLocalTime(String localTime) {
        this.localTime = localTime;
    }
}
