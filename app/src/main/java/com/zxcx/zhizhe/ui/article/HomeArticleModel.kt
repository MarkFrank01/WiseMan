package com.zxcx.zhizhe.ui.article

import com.zxcx.zhizhe.mvpBase.BaseModel
import com.zxcx.zhizhe.mvpBase.BaseRxJava
import com.zxcx.zhizhe.retrofit.AppClient
import com.zxcx.zhizhe.retrofit.BaseSubscriber
import com.zxcx.zhizhe.ui.welcome.ADBean

/**
 * @author : MarkFrank01
 * @Description :
 */
class HomeArticleModel(present:HomeArticleContract.Presenter):BaseModel<HomeArticleContract.Presenter>(){

    init {
        this.mPresenter = present
    }

    fun getAD(){
        mDisposable = AppClient.getAPIService().getAD("102")
                .compose(BaseRxJava.io_main())
                .compose(BaseRxJava.handleArrayResult())
                .subscribeWith(object :BaseSubscriber<MutableList<ADBean>>(mPresenter){
                    override fun onNext(t: MutableList<ADBean>) {
                        mPresenter?.getADSuccess(t)
                    }
                })
    }
}