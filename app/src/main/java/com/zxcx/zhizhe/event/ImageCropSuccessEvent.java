package com.zxcx.zhizhe.event;

/**
 * Created by anm on 2017/7/4.
 */

public class ImageCropSuccessEvent {

    private String path;

    public ImageCropSuccessEvent(String path) {
        this.path = path;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }
}
