package com.example.project3.Video;

import java.util.Date;

public class Comment{
    private String author;
    private String content;
    private Date time;

    public String getAuthor(){
        return this.author;
    }
    public String getContent(){
        return this.content;
    }
    public Date getTime(){
        return this.time;
    }
}