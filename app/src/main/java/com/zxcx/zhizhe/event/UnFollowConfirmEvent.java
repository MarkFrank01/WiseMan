package com.zxcx.zhizhe.event;

/**
 * Created by anm on 2017/7/4.
 * 确认取消关注作者事件
 */

public class UnFollowConfirmEvent {
	
	private int userId;
	
	public UnFollowConfirmEvent(int userId) {
		this.userId = userId;
	}
	
	public int getUserId() {
		return userId;
	}
	
	public void setUserId(int userId) {
		this.userId = userId;
	}
}
