package com.zxcx.zhizhe.ui.article.articleDetails;

import com.zxcx.zhizhe.mvpBase.GetPostView;
import com.zxcx.zhizhe.mvpBase.IGetPostPresenter;

public interface ArticleDetailsContract {

	interface View extends GetPostView<ArticleDetailsBean, ArticleDetailsBean> {

		void followSuccess();

		void likeSuccess(ArticleDetailsBean bean);

		void collectSuccess(ArticleDetailsBean bean);
	}

	interface Presenter extends IGetPostPresenter<ArticleDetailsBean, ArticleDetailsBean> {

		void followSuccess();

		void likeSuccess(ArticleDetailsBean bean);

		void collectSuccess(ArticleDetailsBean bean);
	}
}

