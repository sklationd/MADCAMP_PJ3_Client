package com.example.project3;

import org.json.JSONObject;

public class ResponseInfo_posting {
    public Boolean success;
    public String message;
    public JSONObject errors;
    public VideoInfo data;

    public VideoInfo getData() {
        return data;
    }
    public void setData(VideoInfo data) {
        this.data = data;
    }
}
