package com.zxcx.zhizhe.event;

/**
 * 卡片详情页通知外部列表当前位置更新事件
 */

public class UpdateCardListPositionEvent {
	
	private int currentPosition;
	private String sourceName;
	
	public UpdateCardListPositionEvent(int currentPosition, String sourceName) {
		this.currentPosition = currentPosition;
		this.sourceName = sourceName;
	}
	
	public int getCurrentPosition() {
		return currentPosition;
	}
	
	public void setCurrentPosition(int currentPosition) {
		this.currentPosition = currentPosition;
	}
	
	public String getSourceName() {
		return sourceName;
	}
	
	public void setSourceName(String sourceName) {
		this.sourceName = sourceName;
	}
}
