package com.zxcx.zhizhe.ui.article.articleDetails;

import com.zxcx.zhizhe.mvpBase.GetPostView;
import com.zxcx.zhizhe.mvpBase.IGetPostPresenter;
import com.zxcx.zhizhe.ui.card.hot.CardBean;

public interface ArticleDetailsContract {
	
	interface View extends GetPostView<CardBean, CardBean> {

		void followSuccess();
		
		void likeSuccess(CardBean bean);
		
		void collectSuccess(CardBean bean);
	}
	
	interface Presenter extends IGetPostPresenter<CardBean, CardBean> {

		void followSuccess();
		
		void likeSuccess(CardBean bean);
		
		void collectSuccess(CardBean bean);
	}
}

