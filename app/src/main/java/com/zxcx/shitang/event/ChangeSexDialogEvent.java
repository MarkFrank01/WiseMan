package com.zxcx.shitang.event;

/**
 * Created by anm on 2017/7/4.
 */

public class ChangeSexDialogEvent {
    private int sex;

    public ChangeSexDialogEvent(int sex) {
        this.sex = sex;
    }

    public int getSex() {
        return sex;
    }

    public void setSex(int sex) {
        this.sex = sex;
    }
}
