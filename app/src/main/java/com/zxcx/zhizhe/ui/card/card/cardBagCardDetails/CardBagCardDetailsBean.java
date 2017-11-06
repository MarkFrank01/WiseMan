package com.zxcx.zhizhe.ui.card.card.cardBagCardDetails;

import android.os.Parcel;
import android.os.Parcelable;

import com.alibaba.fastjson.annotation.JSONField;
import com.zxcx.zhizhe.retrofit.RetrofitBaen;

public class CardBagCardDetailsBean extends RetrofitBaen implements Parcelable {


    /**
     * id : 1
     * title : 高考生吃红参致心情烦躁 年轻人无需额外的补药
     */

    @JSONField(name = "id")
    private int id;
    @JSONField(name = "title")
    private String title;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        CardBagCardDetailsBean that = (CardBagCardDetailsBean) o;

        return id == that.id;
    }

    @Override
    public int hashCode() {
        return id;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeString(this.title);
    }

    public CardBagCardDetailsBean() {
    }

    public CardBagCardDetailsBean(int id) {
        this.id = id;
    }

    protected CardBagCardDetailsBean(Parcel in) {
        this.id = in.readInt();
        this.title = in.readString();
    }

    public static final Parcelable.Creator<CardBagCardDetailsBean> CREATOR = new Parcelable.Creator<CardBagCardDetailsBean>() {
        @Override
        public CardBagCardDetailsBean createFromParcel(Parcel source) {
            return new CardBagCardDetailsBean(source);
        }

        @Override
        public CardBagCardDetailsBean[] newArray(int size) {
            return new CardBagCardDetailsBean[size];
        }
    };
}

