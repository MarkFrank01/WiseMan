package com.zxcx.zhizhe.event;

/**
 * @author : MarkFrank01
 * @Created on 2019/1/23
 * @Description :
 */
public class QuitCircleConfirmEvent {

    private int circleID;

    public QuitCircleConfirmEvent(int circleID) {
        this.circleID = circleID;
    }

    public int getCircleID() {
        return circleID;
    }

    public void setCircleID(int circleID) {
        this.circleID = circleID;
    }
}
