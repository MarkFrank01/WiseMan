package com.zxcx.zhizhe.ui.article.attention

import com.zxcx.zhizhe.mvpBase.BaseModel
import com.zxcx.zhizhe.mvpBase.BaseRxJava
import com.zxcx.zhizhe.retrofit.*
import com.zxcx.zhizhe.ui.article.ArticleAndSubjectBean
import com.zxcx.zhizhe.ui.my.selectAttention.ClassifyBean
import com.zxcx.zhizhe.utils.Constants

class AttentionArticleModel(present: AttentionArticleContract.Presenter) : BaseModel<AttentionArticleContract.Presenter>() {

	init {
		this.mPresenter = present
	}

	fun getAttentionCard(page: Int) {
		mDisposable = AppClient.getAPIService().getArticleListForCategory(-1, page, Constants.PAGE_SIZE)
				.compose(BaseRxJava.io_main<BaseArrayBean<ArticleAndSubjectBean>>())
				.compose(BaseRxJava.handleArrayResult())
				.subscribeWith(object : BaseSubscriber<List<ArticleAndSubjectBean>>(mPresenter) {
					override fun onNext(list: List<ArticleAndSubjectBean>) {
						mPresenter?.getDataSuccess(list)
					}

					override fun onError(t: Throwable) {
						super.onError(t)
					}
				})
		addSubscription(mDisposable)
	}

	fun getClassify() {
		mDisposable = AppClient.getAPIService().classify
				.compose(BaseRxJava.io_main())
				.compose(BaseRxJava.handleArrayResult())
				.subscribeWith(object : BaseSubscriber<MutableList<ClassifyBean>>(mPresenter) {
					override fun onNext(list: MutableList<ClassifyBean>) {
						mPresenter?.getClassifySuccess(list)
					}
				})
		addSubscription(mDisposable)
	}

	fun changeAttentionList(idList: List<Int>) {
		mDisposable = AppClient.getAPIService().changeAttentionList(idList)
				.compose(BaseRxJava.handlePostResult())
				.compose(BaseRxJava.io_main_loading(mPresenter))
				.subscribeWith(object : NullPostSubscriber<BaseBean<*>>(mPresenter) {
					override fun onNext(bean: BaseBean<*>) {
						mPresenter?.postSuccess()
					}
				})
		addSubscription(mDisposable)
	}
}


