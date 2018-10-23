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
    fun pushLinkList(articleLinks: Array<String>) {
        mDisposable = AppClient.getAPIService().pushArticleLink(articleLinks)
                .compose(BaseRxJava.io_main_loading(mPresenter))
                .compose(BaseRxJava.handlePostResult())
                .subscribeWith(object : NullPostSubscriber<BaseBean<*>>(mPresenter) {
                    override fun onNext(t: BaseBean<*>?) {
                        mPresenter?.postSuccess()
                    }
                })
        addSubscription(mDisposable)
    }
}