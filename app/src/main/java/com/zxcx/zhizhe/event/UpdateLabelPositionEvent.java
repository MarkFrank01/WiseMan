package com.zxcx.zhizhe.event;

/**
 * @author : MarkFrank01
 * @Created on 2019/1/16
 * @Description :
 */
public class UpdateLabelPositionEvent {

    private int currentPosition;
    private String sourceName;

    public UpdateLabelPositionEvent(int currentPosition, String sourceName) {
        this.currentPosition = currentPosition;
        this.sourceName = sourceName;
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
}
