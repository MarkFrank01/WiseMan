package com.zxcx.shitang.event;

/**
 * Created by anm on 2017/7/4.
 */

public class ChangeNickNameDialogEvent {
    private String nickName;

    public ChangeNickNameDialogEvent(String nickName) {
        this.nickName = nickName;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }
}
