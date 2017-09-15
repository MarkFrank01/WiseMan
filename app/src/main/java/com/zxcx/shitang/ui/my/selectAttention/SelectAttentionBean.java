package com.zxcx.shitang.ui.my.selectAttention;

import com.alibaba.fastjson.annotation.JSONField;

public class SelectAttentionBean {
    @JSONField(name = "interested")
    private boolean isChecked;
    @JSONField(name = "id")
    private int id;
    @JSONField(name = "title")
    private String name;
    @JSONField(name = "titleImage")
    private String imageUrl;

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}

