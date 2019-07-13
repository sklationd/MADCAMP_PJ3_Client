package com.example.project3.Comment;

import java.io.Serializable;
import java.util.Date;

public class Comment implements Serializable  {
    private String _id;
    private String username;
    private int genre;
    private String videoId;
    private String comment;
    private Date createdAt;

    public String get_id(){ return _id; }
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
    public void set_id(String _id){ this._id = _id; }
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