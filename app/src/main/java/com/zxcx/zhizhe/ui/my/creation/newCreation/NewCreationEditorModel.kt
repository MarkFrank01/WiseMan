package com.zxcx.zhizhe.ui.my.creation.newCreation

import com.zxcx.zhizhe.mvpBase.BaseModel
import com.zxcx.zhizhe.mvpBase.BaseRxJava
import com.zxcx.zhizhe.retrofit.AppClient
import com.zxcx.zhizhe.retrofit.BaseBean
import com.zxcx.zhizhe.retrofit.NullPostSubscriber

class NewCreationEditorModel(presenter: NewCreationEditorContract.Presenter) : BaseModel<NewCreationEditorContract.Presenter>() {
    init {
        this.mPresenter = presenter
    }

    fun saveFreeNode(cardId: Int?, title: String?, imageUrl: String?, cardBagId: Int?, content: String?) {
        mDisposable = AppClient.getAPIService().saveFreeNode(cardId!!,title,imageUrl, cardBagId!!,content,0)
                .compose(BaseRxJava.io_main_loading(mPresenter))
                .compose(BaseRxJava.handlePostResult())
                .subscribeWith(object : NullPostSubscriber<BaseBean<*>>(mPresenter) {
                    override fun onNext(bean: BaseBean<*>) {
                        mPresenter.saveFreedomNoteSuccess()
                    }
                })
        addSubscription(mDisposable)
    }

    fun submitReview(cardId: Int?, title: String?,imageUrl: String?,cardBagId: Int?,content: String?) {
        mDisposable = AppClient.getAPIService().saveFreeNode(cardId!!,title,imageUrl, cardBagId!!,content,1)
                .compose(BaseRxJava.io_main_loading(mPresenter))
                .compose(BaseRxJava.handlePostResult())
                .subscribeWith(object : NullPostSubscriber<BaseBean<*>>(mPresenter) {
                    override fun onNext(bean: BaseBean<*>) {
                        mPresenter.postSuccess()
                    }
                })
        addSubscription(mDisposable)
    }
}


