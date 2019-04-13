package com.zxcx.zhizhe.ui.circle.circlemessage

import com.zxcx.zhizhe.mvpBase.BaseModel
import com.zxcx.zhizhe.mvpBase.BaseRxJava
import com.zxcx.zhizhe.retrofit.AppClient
import com.zxcx.zhizhe.retrofit.BaseSubscriber

/**
 * @author : MarkFrank01
 * @Created on 2019/3/19
 * @Description :
 */
class CircleMessageModel(presenter:CircleMessageContract.Presenter):BaseModel<CircleMessageContract.Presenter>() {

    init {
        this.mPresenter = presenter
    }

    fun getCircleTabInfo(){
        mDisposable = AppClient.getAPIService().circleTabInfo
                .compose(BaseRxJava.io_main())
                .compose(BaseRxJava.handleResult())
                .subscribeWith(object :BaseSubscriber<MyCircleTabBean>(mPresenter){
                    override fun onNext(t: MyCircleTabBean) {
                        mPresenter?.getRedPointStatusSuccess(t)
                    }
                })
        addSubscription(mDisposable)
    }

    fun getCommentMessageList(page:Int,pageSize:Int){
        mDisposable = AppClient.getAPIService().getCommentMessageList(page,pageSize)
                .compose(BaseRxJava.io_main())
                .compose(BaseRxJava.handleArrayResult())
                .subscribeWith(object :BaseSubscriber<MutableList<MyCircleTabBean>>(mPresenter){
                    override fun onNext(t: MutableList<MyCircleTabBean>) {
                        mPresenter?.getCommentMessageListSuccess(t)
                    }
                })
        addSubscription(mDisposable)
    }
}