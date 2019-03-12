package com.zxcx.zhizhe.ui.circle.circleowner.owneradd

import com.zxcx.zhizhe.mvpBase.BaseModel
import com.zxcx.zhizhe.mvpBase.BaseRxJava
import com.zxcx.zhizhe.retrofit.AppClient
import com.zxcx.zhizhe.retrofit.BaseSubscriber
import com.zxcx.zhizhe.ui.card.hot.CardBean

/**
 * @author : MarkFrank01
 * @Created on 2019/3/12
 * @Description :
 */
class OwnerAddModel(presnter:OwnerAddContract.Presenter):BaseModel<OwnerAddContract.Presenter>() {

    init {
        this.mPresenter = presnter
    }

    fun getLockableArticleForAdd(styleType:Int,circleId:Int,pageIndex:Int,pageSize:Int){
        mDisposable =AppClient.getAPIService().getLockableArticleForAdd(styleType, circleId, pageIndex, pageSize)
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