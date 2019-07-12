package com.example.project3;

import java.io.Serializable;
import java.util.Date;

public class Comment implements Serializable  {
    private String username;
    private int genre;
    private String videoId;
    private String comment;
    private Date createdAt;

    public String getUsername(){
        return username;
    }
    public int getGenre() { return genre; }
    public String getVideoId(){ return videoId; }
    public String getComment(){
        return comment;
    }
    public Date getCreatedAt(){
        return createdAt;
    }
    public void setUsername(String author){
        this.username = author;
    }
    public void setGenre(int genre) { this.genre = genre; }
    public void setVideoId(String videoId){ this.videoId = videoId; }
    public void setComment(String content){
        this.comment = content;
    }
    public void setCreatedAt(Date createdAt){
        this.createdAt = createdAt;
    }
}