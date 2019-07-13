package com.example.project3.Networking;

import java.util.ArrayList;
import java.util.List;

public class ResponseInfo {
    public Boolean success;
    public String message;
    public String errors;
    public List<VideoInfo> data;

    public List<VideoInfo> getData() {
        return data;
    }
    public void setData(ArrayList<VideoInfo> data) {
        this.data = data;
    }
}
