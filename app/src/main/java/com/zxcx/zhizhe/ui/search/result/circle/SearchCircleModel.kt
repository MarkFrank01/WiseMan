package com.zxcx.zhizhe.ui.search.result.circle

import com.zxcx.zhizhe.mvpBase.BaseModel
import com.zxcx.zhizhe.mvpBase.BaseRxJava
import com.zxcx.zhizhe.retrofit.AppClient
import com.zxcx.zhizhe.retrofit.BaseSubscriber
import com.zxcx.zhizhe.ui.circle.circlehome.CircleBean
import com.zxcx.zhizhe.utils.Constants

/**
 * @author : MarkFrank01
 * @Created on 2019/3/20
 * @Description :
 */
class SearchCircleModel(presenter:SearchCircleContract.Presenter):BaseModel<SearchCircleContract.Presenter>() {

    init {
        this.mPresenter = presenter
    }

    fun searchCircle(keyword:String,page:Int){
        mDisposable = AppClient.getAPIService().searchCircle(keyword,page,Constants.PAGE_SIZE)
                .compose(BaseRxJava.io_main())
                .compose(BaseRxJava.handleArrayResult())
                .subscribeWith(object :BaseSubscriber<MutableList<CircleBean>>(mPresenter){
                    override fun onNext(t: MutableList<CircleBean>) {
                        mPresenter?.getDataSuccess(t)
                    }
                })
        addSubscription(mDisposable)
    }
}