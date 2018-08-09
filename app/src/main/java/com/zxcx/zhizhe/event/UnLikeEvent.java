package com.zxcx.zhizhe.event;

/**
 * Created by anm on 2017/7/4.
 * 取消点赞事件
 */

public class UnLikeEvent {
	
	private int cardId;
	
	public UnLikeEvent(int cardId) {
		this.cardId = cardId;
	}
	
	public int getCardId() {
		return cardId;
	}
	
	public void setCardId(int id) {
		this.cardId = id;
	}
}
