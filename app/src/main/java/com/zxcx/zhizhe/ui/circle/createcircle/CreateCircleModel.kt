package com.zxcx.zhizhe.ui.circle.createcircle

import com.zxcx.zhizhe.mvpBase.BaseModel
import com.zxcx.zhizhe.mvpBase.BaseRxJava
import com.zxcx.zhizhe.retrofit.AppClient
import com.zxcx.zhizhe.retrofit.BaseSubscriber
import com.zxcx.zhizhe.ui.circle.bean.CheckBean
import com.zxcx.zhizhe.ui.circle.circlehome.CircleBean

/**
 * @author : MarkFrank01
 * @Created on 2019/1/26
 * @Description :
 */
class CreateCircleModel(presenter: CreateCircleContract.Presenter) : BaseModel<CreateCircleContract.Presenter>() {
    init {
        this.mPresenter = presenter
    }

    fun createCircle(title: String, titleImage: String, classifyId: Int, sign: String, price: String, articleList: List<Int>, levelType: Int) {
        mDisposable = AppClient.getAPIService().createCircle(title, titleImage, classifyId, sign, price, articleList, levelType)
                .compose(BaseRxJava.io_main())
                .compose(BaseRxJava.handleResult())
                .subscribeWith(object : BaseSubscriber<CircleBean>(mPresenter) {
                    override fun onNext(t: CircleBean) {
                        mPresenter?.getDataSuccess(t)
                    }
                })
        addSubscription(mDisposable)
    }

    fun createCircleNew(title: String, titleImage: String, classifyId: Int, sign: String,levelType: Int,limitedTimeType :Int) {

        mDisposable = AppClient.getAPIService().createCircleNew(title, titleImage, classifyId, sign, levelType, limitedTimeType)
                .compose(BaseRxJava.io_main())
                .compose(BaseRxJava.handleResult())
                .subscribeWith(object :BaseSubscriber<CircleBean>(mPresenter){
                    override fun onNext(t: CircleBean) {
                        mPresenter?.getDataSuccess(t)
                    }
                })
        addSubscription(mDisposable)
    }


    fun checkCircleName(name: String) {
        mDisposable = AppClient.getAPIService().getCheckName(name)
                .compose(BaseRxJava.io_main())
                .compose(BaseRxJava.handleResult())
                .subscribeWith(object : BaseSubscriber<CheckBean>(mPresenter) {
                    override fun onNext(t: CheckBean) {
//                        mPresenter?.checkSuccess()

                    }

                    override fun onError(t: Throwable) {
                        mPresenter?.checkSuccess()
                    }
                })
        addSubscription(mDisposable)
    }
}