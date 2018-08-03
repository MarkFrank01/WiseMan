package com.zxcx.zhizhe.ui.comment

import com.zxcx.zhizhe.mvpBase.BaseModel
import com.zxcx.zhizhe.mvpBase.BaseRxJava
import com.zxcx.zhizhe.retrofit.AppClient
import com.zxcx.zhizhe.retrofit.BaseBean
import com.zxcx.zhizhe.retrofit.BaseSubscriber
import com.zxcx.zhizhe.retrofit.PostSubscriber
import com.zxcx.zhizhe.utils.Constants

class CommentModel(presenter: CommentContract.Presenter) : BaseModel<CommentContract.Presenter>() {

	init {
		this.mPresenter = presenter
	}

	fun getComment(articleId: Int, page: Int) {
		mDisposable = AppClient.getAPIService().getComment(articleId, page, Constants.PAGE_SIZE)
				.compose(BaseRxJava.handleArrayResult())
				.compose(BaseRxJava.io_main())
				.map {
					it.forEach {
						it.addAll()
					}
					it
				}
				.subscribeWith(object : BaseSubscriber<MutableList<CommentBean>>(mPresenter) {
					override fun onNext(list: MutableList<CommentBean>) {
						mPresenter?.getDataSuccess(list)
					}
				})
		addSubscription(mDisposable)
	}

	fun sendComment(articleId: Int, parentCommentId: Int?, content: String) {
		mDisposable = AppClient.getAPIService().sendComment(articleId, parentCommentId, content)
				.compose(BaseRxJava.handleResult())
				.compose(BaseRxJava.io_main_loading(mPresenter))
				.subscribeWith(object : PostSubscriber<CommentBean>(mPresenter) {
					override fun onNext(bean: CommentBean) {
						mPresenter?.postSuccess(bean)
					}
				})
		addSubscription(mDisposable)
	}

	fun likeComment(commentId: Int) {
		mDisposable = AppClient.getAPIService().likeComment(commentId)
				.compose(BaseRxJava.handlePostResult())
				.compose(BaseRxJava.io_main())
				.subscribeWith(object : PostSubscriber<BaseBean<*>>(mPresenter) {
					override fun onNext(bean: BaseBean<*>) {
						mPresenter?.likeSuccess()
					}
				})
		addSubscription(mDisposable)
	}

	fun unlikeComment(commentId: Int) {
		mDisposable = AppClient.getAPIService().unlikeComment(commentId)
				.compose(BaseRxJava.handlePostResult())
				.compose(BaseRxJava.io_main())
				.subscribeWith(object : PostSubscriber<BaseBean<*>>(mPresenter) {
					override fun onNext(bean: BaseBean<*>) {
						mPresenter?.unlikeSuccess()
					}
				})
		addSubscription(mDisposable)
	}
}


