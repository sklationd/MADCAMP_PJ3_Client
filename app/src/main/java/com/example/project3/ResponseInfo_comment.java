package com.example.project3;

import java.util.ArrayList;
import java.util.List;

public class ResponseInfo_comment {
    public Boolean success;
    public String message;
    public String errors;
    public List<Comment> data;

    public List<Comment> getData() {
        return this.data;
    }
    public void setData(ArrayList<Comment> data) {
        this.data = data;
    }
}
