package com.zxcx.zhizhe.event;

/**
 * Created by anm on 2017/7/4.
 * 取消收藏卡片事件
 */

public class UnCollectEvent {
	
	private int cardId;
	
	public UnCollectEvent(int cardId) {
		this.cardId = cardId;
	}
	
	public int getCardId() {
		return cardId;
	}
	
	public void setCardId(int id) {
		this.cardId = id;
	}
}
