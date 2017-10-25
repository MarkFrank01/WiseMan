package com.zxcx.zhizhe.retrofit;

import com.alibaba.fastjson.annotation.JSONField;

import java.util.List;

public class BaseArrayBean<T> {

    @JSONField(name = "data")
    private List<T> data;
    @JSONField(name = "code")
    private int code;
    @JSONField(name = "message")
    private String message;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
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
                ", code='" + code + '\'' +
                ", message='" + message + '\'' +
                '}';
    }
}
