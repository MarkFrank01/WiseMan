package com.zxcx.zhizhe.event;

import com.zxcx.zhizhe.ui.card.hot.CardBean;

import java.util.List;

/**
 * @author : MarkFrank01
 * @Created on 2019/3/11
 * @Description :
 */
public class GetNextArcEvent {
    private int type;
    private List<CardBean> contentList;

    public GetNextArcEvent(int type, List<CardBean> contentList) {
        this.type = type;
        this.contentList = contentList;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public List<CardBean> getContentList() {
        return contentList;
    }

    public void setContentList(List<CardBean> contentList) {
        this.contentList = contentList;
    }
}
