package com.zxcx.zhizhe.ui.circle.circledetaile

import com.zxcx.zhizhe.mvpBase.BaseModel
import com.zxcx.zhizhe.mvpBase.BaseRxJava
import com.zxcx.zhizhe.retrofit.AppClient
import com.zxcx.zhizhe.retrofit.BaseSubscriber
import com.zxcx.zhizhe.ui.circle.circlehome.CircleBean

/**
 * @author : MarkFrank01
 * @Created on 2019/1/25
 * @Description :
 */
class CircleDetaileModel(presenter: CircleDetaileContract.Presenter):BaseModel<CircleDetaileContract.Presenter>(){
    init {
        this.mPresenter = presenter
    }

    //通过圈子id获取圈子详情
    fun getCircleBasicInfo(circleId:Int){
        mDisposable = AppClient.getAPIService().getCircleBasicInfo(circleId)
                .compose(BaseRxJava.io_main())
                .compose(BaseRxJava.handleResult())
                .subscribeWith(object :BaseSubscriber<CircleBean>(mPresenter){
                    override fun onNext(t: CircleBean) {
                        mPresenter?.getCircleBasicInfoSuccess(t)
                    }
                })
        addSubscription(mDisposable)
    }

    //获取圈子成员
    fun getCircleMemberByCircleId(orderType:Int,circleId:Int,pageIndex:Int,pageSize:Int){
        mDisposable = AppClient.getAPIService().getCircleMemberByCircleId(orderType, circleId, pageIndex, pageSize)
                .compose(BaseRxJava.io_main())
                .compose(BaseRxJava.handleArrayResult())
                .subscribeWith(object :BaseSubscriber<MutableList<CircleBean>>(mPresenter){
                    override fun onNext(t: MutableList<CircleBean>) {
                        mPresenter?.getCircleMemberByCircleIdSuccess(t)
                    }
                })
        addSubscription(mDisposable)
    }

    //获取圈子话题
    fun getCircleQAByCircleId(orderType:Int,circleId:Int,pageIndex:Int,pageSize:Int){
        mDisposable = AppClient.getAPIService().getCircleQAByCircleId(orderType, circleId, pageIndex, pageSize)
                .compose(BaseRxJava.io_main())
                .compose(BaseRxJava.handleArrayResult())
                .subscribeWith(object :BaseSubscriber<MutableList<CircleBean>>(mPresenter){
                    override fun onNext(t: MutableList<CircleBean>) {
                        mPresenter?.getCircleQAByCircleIdSuccess(t)
                    }
                })
        addSubscription(mDisposable)
    }
}