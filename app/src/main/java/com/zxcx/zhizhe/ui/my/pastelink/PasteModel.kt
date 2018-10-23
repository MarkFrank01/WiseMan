package com.zxcx.zhizhe.ui.my.pastelink

import com.zxcx.zhizhe.mvpBase.BaseModel
import com.zxcx.zhizhe.mvpBase.BaseRxJava
import com.zxcx.zhizhe.retrofit.AppClient
import com.zxcx.zhizhe.retrofit.BaseBean
import com.zxcx.zhizhe.retrofit.NullPostSubscriber

class PasteModel(presenter: PasteLinkContract.Presenter) : BaseModel<PasteLinkContract.Presenter>() {
    init {
        this.mPresenter = presenter
    }

    //提交所有链接
    fun pushLinkList(articleLinks: List<String>) {
        mDisposable = AppClient.getAPIService().pushArticleLink(articleLinks)
                .compose(BaseRxJava.handlePostResult())
                .compose(BaseRxJava.io_main_loading(mPresenter))
                .subscribeWith(object : NullPostSubscriber<BaseBean<*>>(mPresenter) {
                    override fun onNext(t: BaseBean<*>?) {
                        mPresenter?.postSuccess()
                    }

                    override fun onError(t: Throwable?) {
                        mPresenter?.postFail(t?.message)
                    }
                })
        addSubscription(mDisposable)
    }
}