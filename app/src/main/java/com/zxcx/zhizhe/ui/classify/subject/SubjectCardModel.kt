package com.zxcx.zhizhe.ui.classify.subject

import com.zxcx.zhizhe.mvpBase.BaseModel
import com.zxcx.zhizhe.mvpBase.BaseRxJava
import com.zxcx.zhizhe.retrofit.AppClient
import com.zxcx.zhizhe.retrofit.BaseArrayBean
import com.zxcx.zhizhe.retrofit.BaseSubscriber
import com.zxcx.zhizhe.ui.card.hot.CardBean

class SubjectCardModel(presenter: SubjectCardContract.Presenter) : BaseModel<SubjectCardContract.Presenter>() {
    init {
        this.mPresenter = presenter
    }

    fun getSubjectCardList(id: Int, page: Int, pageSize: Int) {
        mDisposable = AppClient.getAPIService().getSubjectCardList(id, page, pageSize)
                .compose(BaseRxJava.io_main<BaseArrayBean<CardBean>>())
                .compose(BaseRxJava.handleArrayResult())
                .subscribeWith(object : BaseSubscriber<List<CardBean>>(mPresenter) {
                    override fun onNext(list: List<CardBean>) {
                        mPresenter!!.getDataSuccess(list)
                    }
                })
        addSubscription(mDisposable)
    }
}


