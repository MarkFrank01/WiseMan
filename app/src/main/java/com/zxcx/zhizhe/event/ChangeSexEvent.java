package com.zxcx.zhizhe.event;

/**
 * Created by anm on 2017/7/4.
 */

public class ChangeSexEvent {
	
	private int sex;
	
	public ChangeSexEvent(int sex) {
		this.sex = sex;
	}
	
	public int getSex() {
		return sex;
	}
	
	public void setSex(int sex) {
		this.sex = sex;
	}
}
