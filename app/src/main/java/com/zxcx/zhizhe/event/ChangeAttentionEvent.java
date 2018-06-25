package com.zxcx.zhizhe.event;

import com.zxcx.zhizhe.ui.classify.ClassifyCardBean;

import java.util.List;

/**
 * Created by anm on 2017/7/4.
 */

public class ChangeAttentionEvent {
	private List<ClassifyCardBean> mList;

	public ChangeAttentionEvent(List<ClassifyCardBean> list) {
		mList = list;
	}

	public List<ClassifyCardBean> getList() {
		return mList;
	}

	public void setList(List<ClassifyCardBean> list) {
		mList = list;
	}
}
