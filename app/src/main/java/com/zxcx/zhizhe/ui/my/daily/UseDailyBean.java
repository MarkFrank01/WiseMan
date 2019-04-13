package com.zxcx.zhizhe.ui.my.daily;

import com.chad.library.adapter.base.entity.MultiItemEntity;
import com.google.gson.annotations.SerializedName;
import com.zxcx.zhizhe.retrofit.RetrofitBean;

/**
 * @author : MarkFrank01
 * @Created on 2018/12/13
 * @Description :
 */
public class UseDailyBean extends RetrofitBean implements MultiItemEntity{

    public static final int TYPE_TITLE = 1;
    public static final int TYPE_CONTENT = 2;

    @SerializedName("title")
    private String title;
    @SerializedName("articleVOArrayList")
    private DailyBean mDailyBean;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public DailyBean getDailyBean() {
        return mDailyBean;
    }

    public void setDailyBean(DailyBean dailyBean) {
        mDailyBean = dailyBean;
    }

    @Override
    public int getItemType() {
        return 0;
    }
}
