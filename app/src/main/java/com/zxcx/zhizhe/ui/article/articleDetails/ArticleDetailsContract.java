package com.zxcx.zhizhe.ui.article.articleDetails;

import com.zxcx.zhizhe.mvpBase.GetPostView;
import com.zxcx.zhizhe.mvpBase.IGetPostPresenter;
import com.zxcx.zhizhe.ui.card.hot.CardBean;

public interface ArticleDetailsContract {
	
	interface View extends GetPostView<CardBean, CardBean> {

		void followSuccess();
	}
	
	interface Presenter extends IGetPostPresenter<CardBean, CardBean> {

		void followSuccess();
	}
}

