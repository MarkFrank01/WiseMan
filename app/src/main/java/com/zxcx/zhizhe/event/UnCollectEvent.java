package com.zxcx.zhizhe.event;

/**
 * Created by anm on 2017/7/4.
 */

public class UnCollectEvent {

    private int cardId;
    private int cardBagId;

    public UnCollectEvent(int cardId, int cardBagId) {
        this.cardId = cardId;
        cardBagId = cardBagId;
    }

    public int getCardId() {
        return cardId;
    }

    public void setCardId(int id) {
        this.cardId = id;
    }

    public int getCardBagId() {
        return cardBagId;
    }

    public void setCardBagId(int cardBagId) {
        this.cardBagId = cardBagId;
    }
}
