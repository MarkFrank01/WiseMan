package com.zxcx.zhizhe.event;

import com.zxcx.zhizhe.ui.card.hot.CardBean;

import java.util.List;

/**
 * @author : MarkFrank01
 * @Created on 2019/3/11
 * @Description :
 */
public class GetNextCardEvent {
    private int type;
    private List<CardBean> cardBeanList;


    public GetNextCardEvent(int type, List<CardBean> cardBeanList) {
        this.type = type;
        this.cardBeanList = cardBeanList;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }


    public List<CardBean> getCardBeanList() {
        return cardBeanList;
    }

    public void setCardBeanList(List<CardBean> cardBeanList) {
        this.cardBeanList = cardBeanList;
    }
}
