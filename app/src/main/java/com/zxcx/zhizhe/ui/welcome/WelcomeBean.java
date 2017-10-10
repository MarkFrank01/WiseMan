package com.zxcx.zhizhe.ui.welcome;

import com.alibaba.fastjson.annotation.JSONField;
import com.zxcx.zhizhe.retrofit.RetrofitBaen;

/**
 * Created by anm on 2017/9/25.
 */

public class WelcomeBean extends RetrofitBaen {

    /**
     * adNum : 0
     * behavior : string
     * content : string
     * createTime : 2017-09-25T09:18:25.424Z
     * description : string
     * id : 0
     * platformType : 0
     */

    @JSONField(name = "adNum")
    private int adNum;
    @JSONField(name = "behavior")
    private String behavior;
    @JSONField(name = "content")
    private String content;
    @JSONField(name = "createTime")
    private String createTime;
    @JSONField(name = "description")
    private String description;
    @JSONField(name = "id")
    private int id;
    @JSONField(name = "platformType")
    private int platformType;

    public int getAdNum() {
        return adNum;
    }

    public void setAdNum(int adNum) {
        this.adNum = adNum;
    }

    public String getBehavior() {
        return behavior;
    }

    public void setBehavior(String behavior) {
        this.behavior = behavior;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getPlatformType() {
        return platformType;
    }

    public void setPlatformType(int platformType) {
        this.platformType = platformType;
    }
}
