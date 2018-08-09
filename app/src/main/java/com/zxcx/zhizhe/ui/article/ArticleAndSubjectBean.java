package com.zxcx.zhizhe.ui.article;

import com.chad.library.adapter.base.entity.MultiItemEntity;
import com.google.gson.annotations.SerializedName;
import com.zxcx.zhizhe.retrofit.RetrofitBean;
import com.zxcx.zhizhe.ui.card.hot.CardBean;

/**
 * Created by anm on 2017/12/4.
 */

public class ArticleAndSubjectBean extends RetrofitBean implements MultiItemEntity {
	
	public static final int TYPE_ARTICLE = 1;
	public static final int TYPE_SUBJECT = 2;
	
	@SerializedName("articleContent")
	private CardBean mCardBean;
	@SerializedName("topicVO")
	private SubjectBean mSubjectBean;
	
	public CardBean getCardBean() {
		return mCardBean;
	}
	
	public void setCardBean(CardBean cardBean) {
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
		if (mCardBean == null) {
			return TYPE_SUBJECT;
		} else {
			return TYPE_ARTICLE;
		}
	}
}
