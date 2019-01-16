package com.zxcx.zhizhe.ui.my.selectAttention.interest

import com.zxcx.zhizhe.mvpBase.BaseModel
import com.zxcx.zhizhe.mvpBase.BaseRxJava
import com.zxcx.zhizhe.retrofit.AppClient
import com.zxcx.zhizhe.retrofit.BaseSubscriber
import com.zxcx.zhizhe.utils.LogCat

/**
 * @author : MarkFrank01
 * @Created on 2019/1/16
 * @Description :
 */
class SelectInterestModel(presenter:SelectInterestContract.Presenter):BaseModel<SelectInterestContract.Presenter>(){

    init {
        this.mPresenter = presenter
    }


    //新版获取兴趣部分
    fun getInterestRecommend(){
        mDisposable = AppClient.getAPIService().interestRecommend
                .compose(BaseRxJava.io_main())
                .compose(BaseRxJava.handleResult())
                .subscribeWith(object : BaseSubscriber<InterestRecommendBean>(mPresenter){
                    override fun onNext(t: InterestRecommendBean) {
                        LogCat.e("Get"+t.collectionList?.size+"Get"+t.usersList?.size)
                        mPresenter?.getDataSuccess(t)
                    }
                })
        addSubscription(mDisposable)
    }
}