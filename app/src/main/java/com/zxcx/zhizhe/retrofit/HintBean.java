package com.zxcx.zhizhe.retrofit;

import com.google.gson.annotations.SerializedName;

/**
 * @author : MarkFrank01
 * @Created on 2019/3/15
 * @Description :
 */
public class HintBean extends RetrofitBean{

    @SerializedName("code")
    private int code;
    @SerializedName("message")
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
}
