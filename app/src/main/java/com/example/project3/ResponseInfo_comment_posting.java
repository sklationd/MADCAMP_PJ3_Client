package com.example.project3;

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
