package com.zxcx.zhizhe.ui.card

import com.zxcx.zhizhe.mvpBase.BaseModel
import com.zxcx.zhizhe.mvpBase.BaseRxJava
import com.zxcx.zhizhe.retrofit.AppClient
import com.zxcx.zhizhe.retrofit.BaseSubscriber
import com.zxcx.zhizhe.ui.welcome.ADBean

/**
 * @author : MarkFrank01
 * @Description :
 */
class HomeCardModel(present: HomeCardContract.Presenter) : BaseModel<HomeCardContract.Presenter>() {

    init {
        this.mPresenter = present
    }

    fun getAD() {
        mDisposable = AppClient.getAPIService().getAD("101")
                .compose(BaseRxJava.io_main())
                .compose(BaseRxJava.handleArrayResult())
                .subscribeWith(object : BaseSubscriber<MutableList<ADBean>>(mPresenter) {
                    override fun onNext(list: MutableList<ADBean>) {
                        mPresenter?.getADSuccess(list)
                    }
                })
    }

}