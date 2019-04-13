package com.zxcx.zhizhe.event;

import com.zxcx.zhizhe.ui.card.hot.CardBean;

import java.util.List;

/**
 * @author : MarkFrank01
 * @Created on 2019/3/11
 * @Description :
 */
public class GetBackNumAndDataEvent2 {
    private int type;
    private List<Integer> contentList;
    private List<CardBean> cardBeanList;

    public GetBackNumAndDataEvent2(int type, List<Integer> contentList) {
        this.type = type;
        this.contentList = contentList;
    }

    public GetBackNumAndDataEvent2(int type, List<Integer> contentList, List<CardBean> cardBeanList){
        this.type = type;
        this.contentList = contentList;
        this.cardBeanList = cardBeanList;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public List<Integer> getContentList() {
        return contentList;
    }

    public void setContentList(List<Integer> contentList) {
        this.contentList = contentList;
    }

    public List<CardBean> getCardBeanList() {
        return cardBeanList;
    }

    public void setCardBeanList(List<CardBean> cardBeanList) {
        this.cardBeanList = cardBeanList;
    }
}
