package com.zxcx.zhizhe.ui.circle.circleowner.ownercreate

import com.zxcx.zhizhe.mvpBase.BaseModel
import com.zxcx.zhizhe.mvpBase.BaseRxJava
import com.zxcx.zhizhe.retrofit.AppClient
import com.zxcx.zhizhe.retrofit.BaseSubscriber
import com.zxcx.zhizhe.ui.card.hot.CardBean

/**
 * @author : MarkFrank01
 * @Created on 2019/3/11
 * @Description :
 */
class OwnerCreateManageModel(presenter:OwnerCreateManageContract.Presenter):BaseModel<OwnerCreateManageContract.Presenter>() {

    init {
        this.mPresenter = presenter
    }

    fun getLockableArticleForCreate(styleType:Int,classifyId:Int,pageIndex:Int,pageSize:Int){
        mDisposable = AppClient.getAPIService().getLockableArticleForCreate(styleType, classifyId, pageIndex, pageSize)
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