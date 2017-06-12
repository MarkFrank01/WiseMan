package com.zxcx.shitang.retrofit;

import java.util.List;

public class BaseArrayBean<T> {

    private List<T> data;
    private String success;
    private String message;

    public String getSuccess() {
        return success;
    }

    public void setSuccess(String success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<T> getData() {
        return data;
    }

    public void setData(List<T> data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "JsonObjectResult{" +
                "data=" + data +
                ", success='" + success + '\'' +
                ", message='" + message + '\'' +
                '}';
    }
}
