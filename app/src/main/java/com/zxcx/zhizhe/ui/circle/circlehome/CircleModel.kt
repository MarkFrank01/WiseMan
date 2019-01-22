package com.zxcx.zhizhe.ui.circle.circlehome

import com.zxcx.zhizhe.mvpBase.BaseModel
import com.zxcx.zhizhe.mvpBase.BaseRxJava
import com.zxcx.zhizhe.retrofit.AppClient
import com.zxcx.zhizhe.retrofit.BaseSubscriber
import com.zxcx.zhizhe.ui.circle.bean.CircleClassifyBean
import com.zxcx.zhizhe.ui.welcome.ADBean

/**
 * @author : MarkFrank01
 * @Created on 2019/1/21
 * @Description :
 */
class CircleModel(presenter: CircleContract.Presenter):BaseModel<CircleContract.Presenter>(){

    init {
        this.mPresenter = presenter
    }

    //获取圈子顶部的广告
    fun getAD(){
        mDisposable = AppClient.getAPIService().getAD("401")
                .compose(BaseRxJava.io_main())
                .compose(BaseRxJava.handleArrayResult())
                .subscribeWith(object : BaseSubscriber<MutableList<ADBean>>(mPresenter){
                    override fun onNext(list: MutableList<ADBean>) {
                        mPresenter?.getADSuccess(list)
                    }
                })
        addSubscription(mDisposable)
    }

    //获取圈子选项分类
//    fun getClassify() {
//        mDisposable = AppClient.getAPIService().classify
//                .compose(BaseRxJava.io_main())
//                .compose(BaseRxJava.handleArrayResult())
//                .subscribeWith(object : BaseSubscriber<MutableList<ClassifyBean>>(mPresenter) {
//                    override fun onNext(list: MutableList<ClassifyBean>) {
//                        mPresenter?.getClassifySuccess(list)
//                    }
//                })
//        addSubscription(mDisposable)
//    }
    fun getClassify(page:Int,pageSize:Int){
        mDisposable = AppClient.getAPIService().getCircleClassify(page,pageSize)
                .compose(BaseRxJava.io_main())
                .compose(BaseRxJava.handleArrayResult())
                .subscribeWith(object  :BaseSubscriber<MutableList<CircleClassifyBean>>(mPresenter){
                    override fun onNext(t: MutableList<CircleClassifyBean>) {
                        mPresenter?.getClassifySuccess(t)
                    }
                })
        addSubscription(mDisposable)
    }
}