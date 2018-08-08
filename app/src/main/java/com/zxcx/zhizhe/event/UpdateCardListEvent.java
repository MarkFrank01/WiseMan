package com.zxcx.zhizhe.event;

import com.zxcx.zhizhe.ui.card.hot.CardBean;

public class UpdateCardListEvent {
	
	private int currentPosition;
	private String sourceName;
	private CardBean cardBean;
	
	public UpdateCardListEvent(int currentPosition, String sourceName, CardBean cardBean) {
		this.currentPosition = currentPosition;
		this.sourceName = sourceName;
		this.cardBean = cardBean;
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
	
	public CardBean getCardBean() {
		return cardBean;
	}
	
	public void setCardBean(CardBean cardBean) {
		this.cardBean = cardBean;
	}
}
