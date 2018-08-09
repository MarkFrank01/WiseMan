package com.zxcx.zhizhe.retrofit;

import com.google.gson.annotations.SerializedName;

/**
 * 普通数据封装
 */

public class BaseBean<T> extends RetrofitBean {
	
	@SerializedName("data")
	private T data;
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
			", code='" + code + '\'' +
			", message='" + message + '\'' +
			'}';
	}
}
