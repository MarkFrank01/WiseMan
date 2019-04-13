package com.zxcx.zhizhe.ui.topchange

import com.zxcx.zhizhe.mvpBase.BaseModel
import com.zxcx.zhizhe.mvpBase.BaseRxJava
import com.zxcx.zhizhe.retrofit.AppClient
import com.zxcx.zhizhe.retrofit.BaseBean
import com.zxcx.zhizhe.retrofit.BaseSubscriber
import com.zxcx.zhizhe.retrofit.NullPostSubscriber
import com.zxcx.zhizhe.ui.my.selectAttention.ClassifyBean

/**
 * @author : MarkFrank01
 * @Created on 2019/3/29
 * @Description :
 */
class TopChangeModel(presenter:TopChangeContract.Presenter):BaseModel<TopChangeContract.Presenter>() {

    init {
        this.mPresenter = presenter
    }

    fun setClassifyMenu(classifyList:MutableList<Int>){
        mDisposable = AppClient.getAPIService().setClassifyMenu(classifyList)
                .compose(BaseRxJava.io_main())
                .compose(BaseRxJava.handlePostResult())
                .subscribeWith(object :NullPostSubscriber<BaseBean<*>>(mPresenter){
                    override fun onNext(t: BaseBean<*>?) {
                        mPresenter?.setClassifyMenuSuccess()
                    }

                    override fun onError(t: Throwable?) {
                        mPresenter?.setClassifyMenuSuccess()
                    }
                })
        addSubscription(mDisposable)
    }

    fun getAllNavClassify(){
        mDisposable = AppClient.getAPIService().allNavClassify
                .compose(BaseRxJava.handleArrayResult())
                .compose(BaseRxJava.io_main())
                .subscribeWith(object :BaseSubscriber<MutableList<ClassifyBean>>(mPresenter){
                    override fun onNext(t: MutableList<ClassifyBean>) {
                        mPresenter?.getAllNavClassifySuccess(t)
                    }
                })
        addSubscription(mDisposable)
    }
}