package com.zxcx.zhizhe.ui.my

import com.zxcx.zhizhe.mvpBase.BaseModel
import com.zxcx.zhizhe.mvpBase.BaseRxJava
import com.zxcx.zhizhe.retrofit.AppClient
import com.zxcx.zhizhe.retrofit.BaseSubscriber
import com.zxcx.zhizhe.ui.welcome.ADBean

/**
 * @author : MarkFrank01
 * @Description :
 */
class MyFragmentModel(present: MyFragmentContract.Presenter) : BaseModel<MyFragmentContract.Presenter>() {
    init {
        this.mPresenter = present
    }

    fun getAD(lastOpenedTime: Long, lastOpenedAdId: Long) {
        mDisposable = AppClient.getAPIService().getAD("104",lastOpenedTime,lastOpenedAdId)
                .compose(BaseRxJava.io_main())
                .compose(BaseRxJava.handleArrayResult())
                .subscribeWith(object : BaseSubscriber<MutableList<ADBean>>(mPresenter) {
                    override fun onNext(t: MutableList<ADBean>) {
                        mPresenter?.getADSuccess(t)
                    }
                })
    }
}