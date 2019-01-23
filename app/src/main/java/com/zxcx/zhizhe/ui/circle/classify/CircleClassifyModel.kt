package com.zxcx.zhizhe.ui.circle.classify

import com.zxcx.zhizhe.mvpBase.BaseModel
import com.zxcx.zhizhe.mvpBase.BaseRxJava
import com.zxcx.zhizhe.retrofit.AppClient
import com.zxcx.zhizhe.retrofit.BaseSubscriber
import com.zxcx.zhizhe.retrofit.PostSubscriber
import com.zxcx.zhizhe.ui.circle.circlehome.CircleBean

/**
 * @author : MarkFrank01
 * @Created on 2019/1/23
 * @Description :
 */
class CircleClassifyModel(presenter: CircleClassifyContract.Presenter):BaseModel<CircleClassifyContract.Presenter>(){

    init {
        this.mPresenter = presenter
    }

    //通过分类获取的圈子
    fun getCircleListByClassifyId(classifyId:Int,orderType:Int,pageIndex:Int,pageSize:Int){
        mDisposable = AppClient.getAPIService().getCircleListByClassifyId(classifyId,orderType,pageIndex,pageSize)
                .compose(BaseRxJava.io_main())
                .compose(BaseRxJava.handleArrayResult())
                .subscribeWith(object :BaseSubscriber<MutableList<CircleBean>>(mPresenter){
                    override fun onNext(t: MutableList<CircleBean>) {
                        mPresenter?.getDataSuccess(t)
                    }
                })
        addSubscription(mDisposable)
    }


    //加入或退出圈子 1退出 0加入
    fun setjoinCircle(circleId:Int,joinType:Int){
        mDisposable = AppClient.getAPIService().setjoinCircle(circleId, joinType)
                .compose(BaseRxJava.io_main())
                .compose(BaseRxJava.handleArrayResult())
                .subscribeWith(object :PostSubscriber<MutableList<CircleBean>>(mPresenter){
                    override fun onNext(t: MutableList<CircleBean>) {
                        if (joinType == 1) {
                            mPresenter?.QuitCircleSuccess(t)
                        }else{
                            mPresenter?.JoinCircleSuccess(t)
                        }
                    }
                })
        addSubscription(mDisposable)
    }
}