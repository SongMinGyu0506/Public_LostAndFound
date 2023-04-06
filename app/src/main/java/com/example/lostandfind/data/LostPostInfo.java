package com.example.lostandfind.data;

import com.google.firebase.firestore.Exclude;

import java.io.Serializable;

public class LostPostInfo implements Serializable {
    @Exclude private String id;

    private String title;       //게시글 제목
    private String contents;    //게시글 내용
    private String location;    //잃어버린 장소
    private String lostDate;    //잃어버린 날짜
    private String category;    //분실한 물건의 종류
    private String postDate;    //게시글 작성 날짜
    private String name;        //작성자 이름
    private String image;       //이미지 이름
    private String writerUID;   //작성자 uid
    private String writerEmail; //작성자 Email

    //기본 생성자
    public LostPostInfo(){

    }

    public LostPostInfo(String title, String contents, String location,
                        String lostDate, String category, String postDate,
                        String name, String writerUID, String image, String writerEmail){
        this.title = title;
        this.contents = contents;
        this.location = location;
        this.lostDate = lostDate;
        this.category = category;
        this.postDate = postDate;
        this.name = name;
        this.image = image;
        this.writerUID = writerUID;
        this.writerEmail = writerEmail;
    }

    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }

    public String getImage() {
        return image;
    }
    public void setImage(String image) {
        this.image = image;
    }

    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }
    public String getContents() {
        return contents;
    }
    public void setContents(String contents) {
        this.contents = contents;
    }
    public String getWriterUID() {
        return writerUID;
    }
    public void setWriterUID(String writerUID) {
        this.writerUID = writerUID;
    }
    public String getLocation() {
        return location;
    }
    public void setLocation(String location) {
        this.location = location;
    }
    public String getLostDate() {
        return lostDate;
    }
    public void setLostDate(String lostDate) {
        this.lostDate = lostDate;
    }
    public String getCategory() {
        return category;
    }
    public void setCategory(String category) {
        this.category = category;
    }
    public String getPostDate() {
        return postDate;
    }
    public void setPostDate(String postDate) {
        this.postDate = postDate;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getWriterEmail() {
        return writerEmail;
    }

    public void setWriterEmail(String writerEmail) {
        this.writerEmail = writerEmail;
    }
}
