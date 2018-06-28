package com.zxcx.zhizhe.ui.my.selectAttention

import com.zxcx.zhizhe.mvpBase.BaseModel
import com.zxcx.zhizhe.mvpBase.BaseRxJava
import com.zxcx.zhizhe.retrofit.AppClient
import com.zxcx.zhizhe.retrofit.BaseBean
import com.zxcx.zhizhe.retrofit.BaseSubscriber
import com.zxcx.zhizhe.retrofit.NullPostSubscriber
import com.zxcx.zhizhe.ui.classify.ClassifyBean

class SelectAttentionModel(present: SelectAttentionContract.Presenter) : BaseModel<SelectAttentionContract.Presenter>() {

	init {
		this.mPresenter = present
	}

	fun getClassify() {
		mDisposable = AppClient.getAPIService().classify
				.compose(BaseRxJava.io_main())
				.compose(BaseRxJava.handleArrayResult())
				.subscribeWith(object : BaseSubscriber<MutableList<ClassifyBean>>(mPresenter) {
					override fun onNext(list: MutableList<ClassifyBean>) {
						mPresenter?.getDataSuccess(list)
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


