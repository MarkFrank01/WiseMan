package com.zxcx.zhizhe.ui.classify.subject

import com.zxcx.zhizhe.mvpBase.BaseModel
import com.zxcx.zhizhe.mvpBase.BaseRxJava
import com.zxcx.zhizhe.retrofit.AppClient
import com.zxcx.zhizhe.retrofit.BaseSubscriber
import com.zxcx.zhizhe.ui.search.result.subject.SubjectBean

class SubjectModel(presenter: SubjectContract.Presenter) : BaseModel<SubjectContract.Presenter>() {
    init {
        this.mPresenter = presenter
    }

    fun getSubject(id: Int, page: Int, pageSize: Int) {
        mDisposable = AppClient.getAPIService().getSubject(id, page, pageSize)
                .compose(BaseRxJava.io_main())
                .compose(BaseRxJava.handleArrayResult())
                .subscribeWith(object : BaseSubscriber<List<SubjectBean>>(mPresenter) {
                    override fun onNext(list: List<SubjectBean>) {
                        mPresenter?.getDataSuccess(list)
                    }
                })
        addSubscription(mDisposable)
    }
}


