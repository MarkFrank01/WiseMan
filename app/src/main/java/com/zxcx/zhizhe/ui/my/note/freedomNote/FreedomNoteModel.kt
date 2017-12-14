package com.zxcx.zhizhe.ui.search.result.card

import com.zxcx.zhizhe.mvpBase.BaseModel
import com.zxcx.zhizhe.mvpBase.BaseRxJava
import com.zxcx.zhizhe.retrofit.AppClient
import com.zxcx.zhizhe.retrofit.BaseSubscriber

class FreedomNoteModel(presenter: FreedomNoteContract.Presenter) : BaseModel<FreedomNoteContract.Presenter>() {
    init {
        this.mPresenter = presenter
    }

    fun getFreedomNote(sortType: Int, page: Int, pageSize: Int) {
        mDisposable = AppClient.getAPIService().getFreedomNote(sortType, page, pageSize)
                .compose(BaseRxJava.io_main())
                .compose(BaseRxJava.handleArrayResult())
                .subscribeWith(object : BaseSubscriber<List<FreedomNoteBean>>(mPresenter) {
                    override fun onNext(list: List<FreedomNoteBean>) {
                        mPresenter.getDataSuccess(list)
                    }
                })
        addSubscription(mDisposable)
    }
}


