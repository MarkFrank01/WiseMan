package com.zxcx.shitang.retrofit;

public class BaseBean<T> {

    private T data;
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

    public T getData() {
        return data;
    }

    public void setData(T data) {
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
