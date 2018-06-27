package com.zxcx.zhizhe.event;

import com.zxcx.zhizhe.ui.card.hot.CardBean;

import java.util.ArrayList;

public class AddCardDetailsListEvent {
	private String sourceName;
	private ArrayList<CardBean> list;

	public AddCardDetailsListEvent(String sourceName, ArrayList<CardBean> list) {
		this.sourceName = sourceName;
		this.list = list;
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
