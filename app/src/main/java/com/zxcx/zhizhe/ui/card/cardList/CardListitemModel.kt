package com.zxcx.zhizhe.ui.card.cardList

import com.zxcx.zhizhe.mvpBase.BaseModel
import com.zxcx.zhizhe.mvpBase.BaseRxJava
import com.zxcx.zhizhe.retrofit.AppClient
import com.zxcx.zhizhe.retrofit.BaseSubscriber
import com.zxcx.zhizhe.ui.welcome.ADBean

/**
 * @author : MarkFrank01
 * @Description :
 */
class CardListitemModel(present:CardListitemContract.Presenter):BaseModel<CardListitemContract.Presenter>(){

    init {
        this.mPresenter = present
    }

    fun getAD(position:Int){
        mDisposable = AppClient.getAPIService().getAD("402",position,1)
                .compose(BaseRxJava.io_main())
                .compose(BaseRxJava.handleArrayResult())
                .subscribeWith(object :BaseSubscriber<MutableList<ADBean>>(mPresenter){
                    override fun onNext(t: MutableList<ADBean>) {
                        mPresenter?.getADSuccess(t)
                    }
                })
    }
}