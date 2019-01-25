package com.zxcx.zhizhe.ui.circle.createcircle

import com.zxcx.zhizhe.mvpBase.BaseModel
import com.zxcx.zhizhe.mvpBase.BaseRxJava
import com.zxcx.zhizhe.retrofit.AppClient
import com.zxcx.zhizhe.retrofit.BaseSubscriber
import com.zxcx.zhizhe.ui.card.hot.CardBean
import com.zxcx.zhizhe.utils.LogCat

/**
 * @author : MarkFrank01
 * @Created on 2019/1/24
 * @Description :
 */
class ManageCreateModel(presenter:ManageCreateContract.Presenter):BaseModel<ManageCreateContract.Presenter>(){

    init {
        this.mPresenter = presenter
    }

    //获取作品
    fun getPublishableArticle(page:Int,pageSize:Int){
        LogCat.e("MODEL FUCK")
        mDisposable = AppClient.getAPIService().getPublishableArticle(page, pageSize)
                .compose(BaseRxJava.io_main())
                .compose(BaseRxJava.handleArrayResult())
                .subscribeWith(object :BaseSubscriber<MutableList<CardBean>>(mPresenter){
                    override fun onNext(t: MutableList<CardBean>) {
                        mPresenter?.getDataSuccess(t)
                    }
                })
        addSubscription(mDisposable)
    }

}