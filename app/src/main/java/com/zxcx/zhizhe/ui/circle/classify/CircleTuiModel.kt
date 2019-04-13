package com.zxcx.zhizhe.ui.circle.classify

import com.zxcx.zhizhe.mvpBase.BaseModel
import com.zxcx.zhizhe.mvpBase.BaseRxJava
import com.zxcx.zhizhe.retrofit.AppClient
import com.zxcx.zhizhe.retrofit.BaseSubscriber
import com.zxcx.zhizhe.retrofit.PostSubscriber
import com.zxcx.zhizhe.ui.circle.circlehome.CircleBean

/**
 * @author : MarkFrank01
 * @Created on 2019/3/6
 * @Description :
 */
class CircleTuiModel(presenter:CircleTuiContract.Presenter):BaseModel<CircleTuiContract.Presenter>(){

    init {
        this.mPresenter = presenter
    }

    //获取推荐的圈子的数据
    fun getRecommendCircleListByPage(page: Int,pageSize: Int){
        mDisposable = AppClient.getAPIService().getRecommendCircleListByPage(page,pageSize)
                .compose(BaseRxJava.io_main())
                .compose(BaseRxJava.handleArrayResult())
                .subscribeWith(object : BaseSubscriber<MutableList<CircleBean>>(mPresenter){
                    override fun onNext(t: MutableList<CircleBean>) {
                        mPresenter?.getDataSuccess(t)
                    }
                })
        addSubscription(mDisposable)
    }

    //加入 0加入
    fun setjoinCircle(circleId:Int,joinType:Int){
        mDisposable = AppClient.getAPIService().setjoinCircle(circleId, joinType)
                .compose(BaseRxJava.io_main())
                .compose(BaseRxJava.handleArrayResult())
                .subscribeWith(object : PostSubscriber<MutableList<CircleBean>>(mPresenter){
                    override fun onNext(t: MutableList<CircleBean>) {
                            mPresenter?.JoinCircleSuccess(t)
                    }
                })
        addSubscription(mDisposable)
    }
}