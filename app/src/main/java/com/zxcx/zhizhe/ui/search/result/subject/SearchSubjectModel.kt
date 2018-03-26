package com.zxcx.zhizhe.ui.search.result.subject

import com.zxcx.zhizhe.mvpBase.BaseModel
import com.zxcx.zhizhe.mvpBase.BaseRxJava
import com.zxcx.zhizhe.retrofit.AppClient
import com.zxcx.zhizhe.retrofit.BaseSubscriber

class SearchSubjectModel(presenter: SearchSubjectContract.Presenter) : BaseModel<SearchSubjectContract.Presenter>() {
    init {
        this.mPresenter = presenter
    }

    fun searchSubject(keyword: String, page: Int, pageSize: Int) {
        mDisposable = AppClient.getAPIService().searchSubject(keyword, page, pageSize)
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