package com.zxcx.zhizhe.ui.circle.circlemessage.zan

import com.zxcx.zhizhe.mvpBase.BaseModel
import com.zxcx.zhizhe.mvpBase.BaseRxJava
import com.zxcx.zhizhe.retrofit.AppClient
import com.zxcx.zhizhe.retrofit.BaseSubscriber
import com.zxcx.zhizhe.ui.circle.circlemessage.MyCircleTabBean

/**
 * @author : MarkFrank01
 * @Created on 2019/3/20
 * @Description :
 */
class CircleMesZanModel(presenter: CircleMesZanContract.Presenter) : BaseModel<CircleMesZanContract.Presenter>() {

    init {
        this.mPresenter = presenter
    }

    fun getLikeMessageList(page: Int, pageSize: Int) {
        mDisposable =AppClient.getAPIService().getLikeMessageList(page,pageSize)
                .compose(BaseRxJava.io_main())
                .compose(BaseRxJava.handleArrayResult())
                .subscribeWith(object :BaseSubscriber<MutableList<MyCircleTabBean>>(mPresenter){
                    override fun onNext(t: MutableList<MyCircleTabBean>) {
                        mPresenter?.getLikeMessageListSuccess(t)
                    }
                })
        addSubscription(mDisposable)
    }
}