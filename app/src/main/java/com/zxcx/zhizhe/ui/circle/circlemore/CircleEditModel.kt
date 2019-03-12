package com.zxcx.zhizhe.ui.circle.circlemore

import com.zxcx.zhizhe.mvpBase.BaseModel
import com.zxcx.zhizhe.mvpBase.BaseRxJava
import com.zxcx.zhizhe.retrofit.AppClient
import com.zxcx.zhizhe.retrofit.BaseSubscriber
import com.zxcx.zhizhe.ui.circle.circlehome.CircleBean

/**
 * @author : MarkFrank01
 * @Created on 2019/3/12
 * @Description :
 */
class CircleEditModel(presenter: CircleEditContract.Presenter):BaseModel<CircleEditContract.Presenter>() {

    init {
        this.mPresenter = presenter
    }


    fun createCircleNew(title: String, titleImage: String, classifyId: Int, sign: String,levelType: Int,limitedTimeType :Int) {

        mDisposable = AppClient.getAPIService().createCircleNew(title, titleImage, classifyId, sign, levelType, limitedTimeType)
                .compose(BaseRxJava.io_main())
                .compose(BaseRxJava.handleResult())
                .subscribeWith(object : BaseSubscriber<CircleBean>(mPresenter){
                    override fun onNext(t: CircleBean) {
                        mPresenter?.getDataSuccess(t)
                    }
                })
        addSubscription(mDisposable)
    }
}