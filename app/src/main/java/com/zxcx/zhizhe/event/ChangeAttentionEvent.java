package com.zxcx.zhizhe.event;

import com.zxcx.zhizhe.ui.my.selectAttention.ClassifyCardBean;
import java.util.List;

/**
 * Created by anm on 2017/7/4.
 * 修改关注标签事件，发出位置为关注界面内嵌的Callback
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
