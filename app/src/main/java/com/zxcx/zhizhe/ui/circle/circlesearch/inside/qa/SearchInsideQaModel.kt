package com.zxcx.zhizhe.ui.circle.circlesearch.inside.qa

import com.zxcx.zhizhe.mvpBase.BaseModel
import com.zxcx.zhizhe.mvpBase.BaseRxJava
import com.zxcx.zhizhe.retrofit.AppClient
import com.zxcx.zhizhe.retrofit.BaseSubscriber
import com.zxcx.zhizhe.ui.circle.circledetaile.CircleDetailBean

/**
 * @author : MarkFrank01
 * @Created on 2019/3/20
 * @Description :
 */
class SearchInsideQaModel(presenter:SearchInsideQaContract.Presenter):BaseModel<SearchInsideQaContract.Presenter>() {

    init {
        this.mPresenter = presenter
    }

    fun searchCircleQA(page:Int,pageSize:Int,circleId:Int,keyword:String){
        mDisposable = AppClient.getAPIService().searchCircleQA(page,pageSize, circleId, keyword)
                .compose(BaseRxJava.io_main())
                .compose(BaseRxJava.handleArrayResult())
                .subscribeWith(object :BaseSubscriber<MutableList<CircleDetailBean>>(mPresenter){
                    override fun onNext(t: MutableList<CircleDetailBean>) {
                        mPresenter?.getCircleQAByCircleIdSuccess(t)
                    }
                })
        addSubscription(mDisposable)
    }
}