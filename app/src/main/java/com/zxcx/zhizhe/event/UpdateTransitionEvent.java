package com.zxcx.zhizhe.event;

import com.zxcx.zhizhe.ui.card.hot.CardBean;

import java.util.ArrayList;

public class UpdateTransitionEvent {
	private int page;
	private int currentPosition;
	private String sourceName;
	private ArrayList<CardBean> list;

	public UpdateTransitionEvent(int page, int currentPosition, String sourceName, ArrayList<CardBean> list) {
		this.page = page;
		this.currentPosition = currentPosition;
		this.sourceName = sourceName;
		this.list = list;
	}

	public int getPage() {
		return page;
	}

	public void setPage(int page) {
		this.page = page;
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

	public ArrayList<CardBean> getList() {
		return list;
	}

	public void setList(ArrayList<CardBean> list) {
		this.list = list;
	}
}
