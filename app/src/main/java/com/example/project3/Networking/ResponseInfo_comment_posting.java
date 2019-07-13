package com.example.project3.Networking;

import com.example.project3.Comment.Comment;

import org.json.JSONObject;

public class ResponseInfo_comment_posting {
    public Boolean success;
    public String message;
    public JSONObject errors;
    public Comment data;

    public Comment getData() {
        return this.data;
    }
    public void setData(Comment data) {
        this.data = data;
    }
}
