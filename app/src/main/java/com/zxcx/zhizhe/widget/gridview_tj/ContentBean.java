package com.zxcx.zhizhe.widget.gridview_tj;

/**
 * @author : MarkFrank01
 * @Created on 2019/3/8
 * @Description :
 */
public class ContentBean {
    public String title;
    public int type;
    public String iconRes;

    public ContentBean(String title, int type) {
        this.title = title;
        this.type = type;
    }

    public ContentBean(String title, String iconRes) {
        this.title = title;
        this.iconRes = iconRes;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getIconRes() {
        return iconRes;
    }

    public void setIconRes(String iconRes) {
        this.iconRes = iconRes;
    }
}
