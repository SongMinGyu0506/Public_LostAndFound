package com.example.lostandfind.data;

import androidx.annotation.NonNull;

import com.google.firebase.firestore.Exclude;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

public class Post implements Serializable {
    @Exclude private String id;

    private String title; //Post 제목
    private String contents; //Post 본문
    private String location;
    private String lostDate;
    private String category; //카테고리
    private String postDate; //Post 작성 날짜
    private String name;
    private String image; //이미지
    private String writerUID;
    private String writerEmail;

//    private String pattern = "yyyy-MM-dd HH:mm";
//    private SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);


    public Post() {}
    //Constructor
    public Post(String imageName, String title, String category, String location, String lostDate, String postDate, String content, String writerEmail, String name, String writerUID) {
//        simpleDateFormat.setTimeZone(TimeZone.getTimeZone("Asia/Seoul"));
//        this.postDate = simpleDateFormat.format(new Date()).toString();

        this.image = imageName;
        this.title = title;
        this.category = category;
        this.location = location;
        this.lostDate = lostDate;
        this.postDate = postDate;
        this.contents = content;

        this.writerEmail = writerEmail;
        this.name = name;
        this.writerUID = writerUID;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getPostDate() {
        return postDate;
    }

    public void setPostDate(String postDate) {
        this.postDate = postDate;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getContents() {
        return contents;
    }

    public void setContents(String contents) {
        this.contents = contents;
    }

    public String getLostDate() {
        return lostDate;
    }

    public void setLostDate(String lostDate) {
        this.lostDate = lostDate;
    }

    public String getWriterEmail() {
        return writerEmail;
    }

    public void setWriterEmail(String writerEmail) {
        this.writerEmail = writerEmail;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getWriterUID() {
        return writerUID;
    }

    public void setWriterUID(String writerUID) {
        this.writerUID = writerUID;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    @NonNull
    @Override
    public String toString() {
        return this.getImage() + this.getTitle();
    }
}
