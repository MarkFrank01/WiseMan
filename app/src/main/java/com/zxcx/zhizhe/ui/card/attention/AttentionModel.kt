package com.zxcx.zhizhe.ui.card.attention

import com.zxcx.zhizhe.mvpBase.BaseModel
import com.zxcx.zhizhe.mvpBase.BaseRxJava
import com.zxcx.zhizhe.retrofit.*
import com.zxcx.zhizhe.ui.card.hot.CardBean
import com.zxcx.zhizhe.ui.classify.ClassifyBean

class AttentionModel(present: AttentionContract.Presenter) : BaseModel<AttentionContract.Presenter>() {

    init {
        this.mPresenter = present
    }

    fun getAttentionCard(page: Int, pageSize: Int) {
        mDisposable = AppClient.getAPIService().getAttentionCard(page, pageSize)
                .compose(BaseRxJava.io_main<BaseArrayBean<CardBean>>())
                .compose(BaseRxJava.handleArrayResult())
                .subscribeWith(object : BaseSubscriber<List<CardBean>>(mPresenter) {
                    override fun onNext(list: List<CardBean>) {
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


