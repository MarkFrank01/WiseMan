package com.zxcx.zhizhe.event;

/**
 * Created by anm on 2017/7/4.
 */

public class ClearMessageEvent {
	
	private int messageType;
	
	public ClearMessageEvent(int messageType) {
		this.messageType = messageType;
	}
	
	public int getMessageType() {
		return messageType;
	}
	
	public void setMessageType(int id) {
		this.messageType = id;
	}
}
