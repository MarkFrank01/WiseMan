package com.zxcx.zhizhe.ui.circle.allmycircle

import com.zxcx.zhizhe.mvpBase.BaseModel
import com.zxcx.zhizhe.mvpBase.BaseRxJava
import com.zxcx.zhizhe.retrofit.AppClient
import com.zxcx.zhizhe.retrofit.BaseSubscriber
import com.zxcx.zhizhe.ui.circle.circlehome.CircleBean

/**
 * @author : MarkFrank01
 * @Created on 2019/1/24
 * @Description :
 */
class AllMyCircleModel(presenter:AllMyCircleContract.Presenter):BaseModel<AllMyCircleContract.Presenter>(){

    init {
        this.mPresenter = presenter
    }

    //获取加入圈子
    fun getMyJoinCircleList(page:Int,pageSize:Int){
        mDisposable = AppClient.getAPIService().getMyJoinCircleList(page, pageSize)
                .compose(BaseRxJava.io_main())
                .compose(BaseRxJava.handleArrayResult())
                .subscribeWith(object : BaseSubscriber<MutableList<CircleBean>>(mPresenter){
                    override fun onNext(t: MutableList<CircleBean>) {
                        mPresenter?.getDataSuccess(t)
                    }
                })
        addSubscription(mDisposable)
    }

    //获取创建的圈子
    fun getMyCreateCircleList(page:Int,pageSize: Int){
        mDisposable = AppClient.getAPIService().getMyCreateCircleList(page, pageSize)
                .compose(BaseRxJava.io_main())
                .compose(BaseRxJava.handleArrayResult())
                .subscribeWith(object : BaseSubscriber<MutableList<CircleBean>>(mPresenter){
                    override fun onNext(t: MutableList<CircleBean>) {
                        mPresenter?.getDataSuccess(t)
                    }
                })
        addSubscription(mDisposable)
    }

    //获取推荐圈子，当加入的圈子为空的时候
    fun getRecommendCircleListWhenNoData(){
        mDisposable = AppClient.getAPIService().recommendCircleListWhenNoData
                .compose(BaseRxJava.io_main())
                .compose(BaseRxJava.handleArrayResult())
                .subscribeWith(object :BaseSubscriber<MutableList<CircleBean>>(mPresenter){
                    override fun onNext(t: MutableList<CircleBean>) {
                        mPresenter?.emptyCircle(t)
                    }
                })
        addSubscription(mDisposable)
    }
}