package com.zxcx.zhizhe.ui.search.result;

import com.chad.library.adapter.base.entity.MultiItemEntity;
import com.google.gson.annotations.SerializedName;
import com.zxcx.zhizhe.retrofit.RetrofitBaen;

/**
 * Created by anm on 2017/12/4.
 */

public class SearchResultBean extends RetrofitBaen implements MultiItemEntity {

    public static final int TYPE_CARD = 1;
    public static final int TYPE_SUBJECT = 2;

    @SerializedName("articleContent")
    private SearchCardBean mCardBean;
    @SerializedName("collectionContent")
    private SubjectBean mSubjectBean;

    public SearchCardBean getCardBean() {
        return mCardBean;
    }

    public void setCardBean(SearchCardBean cardBean) {
        mCardBean = cardBean;
    }

    public SubjectBean getSubjectBean() {
        return mSubjectBean;
    }

    public void setSubjectBean(SubjectBean subjectBean) {
        mSubjectBean = subjectBean;
    }

    @Override
    public int getItemType() {
        if (mCardBean == null){
            return TYPE_SUBJECT;
        }else {
            return TYPE_CARD;
        }
    }
}