package com.zxcx.zhizhe.ui.card

import com.zxcx.zhizhe.mvpBase.BaseModel
import com.zxcx.zhizhe.mvpBase.BaseRxJava
import com.zxcx.zhizhe.retrofit.AppClient
import com.zxcx.zhizhe.retrofit.BaseSubscriber
import com.zxcx.zhizhe.service.version_update.entity.UpdateApk
import com.zxcx.zhizhe.ui.welcome.ADBean
import com.zxcx.zhizhe.utils.LogCat

/**
 * @author : MarkFrank01
 * @Description :
 */
class HomeCardModel(present: HomeCardContract.Presenter) : BaseModel<HomeCardContract.Presenter>() {

    init {
        this.mPresenter = present
    }

    fun getAD(lastOpenedTime: Long, lastOpenedAdId: Long) {
        mDisposable = AppClient.getAPIService().getAD("101", lastOpenedTime, lastOpenedAdId)
                .compose(BaseRxJava.io_main())
                .compose(BaseRxJava.handleArrayResult())
                .subscribeWith(object : BaseSubscriber<MutableList<ADBean>>(mPresenter) {
                    override fun onNext(list: MutableList<ADBean>) {
                        mPresenter?.getADSuccess(list)
                    }

                    override fun onComplete() {
//                        LogCat.e("Complete")
                    }

                    override fun onError(t: Throwable) {
//                        LogCat.e("onError")
                    }
                })
        addSubscription(mDisposable)
    }

    fun getUpdate(platformType: Int) {
        mDisposable = AppClient.getAPIService().getUpdateApk(platformType)
                .compose(BaseRxJava.io_main())
                .compose(BaseRxJava.handleResult())
                .subscribeWith(object : BaseSubscriber<UpdateApk>(mPresenter) {
                    override fun onNext(t: UpdateApk) {
                        LogCat.e("updateApk version ${t.version}")
                        mPresenter?.getCheckUpdateApk(t)
                    }

                    override fun onComplete() {
                        LogCat.e("complete check")
                    }

                    override fun onError(t: Throwable) {
                        LogCat.e("check error")
                    }
                })

        addSubscription(mDisposable)
    }

}