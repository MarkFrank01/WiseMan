package com.zxcx.zhizhe.ui.my.readCards

import com.zxcx.zhizhe.mvpBase.BaseModel
import com.zxcx.zhizhe.mvpBase.BaseRxJava
import com.zxcx.zhizhe.retrofit.AppClient
import com.zxcx.zhizhe.retrofit.BaseBean
import com.zxcx.zhizhe.retrofit.BaseSubscriber
import com.zxcx.zhizhe.retrofit.NullPostSubscriber

class ReadCardsModel(presenter: ReadCardsContract.Presenter) : BaseModel<ReadCardsContract.Presenter>() {
    init {
        this.mPresenter = presenter
    }

    fun getReadCard(page: Int, pageSize: Int) {
        mDisposable = AppClient.getAPIService().getReadCard(0, page, pageSize)
                .compose(BaseRxJava.io_main())
                .compose(BaseRxJava.handleArrayResult())
                .subscribeWith(object : BaseSubscriber<List<ReadCardsBean>>(mPresenter) {
                    override fun onNext(list: List<ReadCardsBean>) {
                        mPresenter?.getDataSuccess(list)
                    }
                })
        addSubscription(mDisposable)
    }

    fun deleteReadCard(realId: Int,cardId: Int) {
        mDisposable = AppClient.getAPIService().removeReadCard(realId,cardId)
                .compose(BaseRxJava.handlePostResult())
                .compose(BaseRxJava.io_main())
                .subscribeWith(object : NullPostSubscriber<BaseBean<*>>(mPresenter) {
                    override fun onNext(t: BaseBean<*>?) {
                        mPresenter?.postSuccess()
                    }
                })
        addSubscription(mDisposable)
    }
}


