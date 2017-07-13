package com.zxcx.shitang.event;

/**
 * Created by anm on 2017/7/4.
 */

public class ChangeBirthdayDialogEvent {
    private String birthday;

    public ChangeBirthdayDialogEvent(String birthday) {
        this.birthday = birthday;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }
}
