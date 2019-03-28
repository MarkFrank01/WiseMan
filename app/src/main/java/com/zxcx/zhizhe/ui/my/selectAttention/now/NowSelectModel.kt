package com.zxcx.zhizhe.ui.my.selectAttention.now

import com.zxcx.zhizhe.mvpBase.BaseModel
import com.zxcx.zhizhe.mvpBase.BaseRxJava
import com.zxcx.zhizhe.retrofit.AppClient
import com.zxcx.zhizhe.retrofit.BaseBean
import com.zxcx.zhizhe.retrofit.BaseSubscriber
import com.zxcx.zhizhe.retrofit.NullPostSubscriber
import com.zxcx.zhizhe.ui.my.selectAttention.ClassifyBean

/**
 * @author : MarkFrank01
 * @Created on 2019/3/28
 * @Description :
 */
class NowSelectModel(presenter: NowSelectContract.Presenter) : BaseModel<NowSelectContract.Presenter>() {

    init {
        this.mPresenter = presenter
    }

    fun getInterestRecommendForClassify() {
        mDisposable = AppClient.getAPIService().interestRecommendForClassify
                .compose(BaseRxJava.io_main())
                .compose(BaseRxJava.handleArrayResult())
                .subscribeWith(object :BaseSubscriber<MutableList<ClassifyBean>>(mPresenter){
                    override fun onNext(t: MutableList<ClassifyBean>) {
                        mPresenter?.getDataSuccess(t)
                    }
                })
        addSubscription(mDisposable)
    }

    fun followClassify(classifyList:MutableList<Int>){
        mDisposable = AppClient.getAPIService().followClassify(classifyList)
                .compose(BaseRxJava.handlePostResult())
                .compose(BaseRxJava.io_main())
                .subscribeWith(object :NullPostSubscriber<BaseBean<*>>(mPresenter){
                    override fun onNext(t: BaseBean<*>?) {
                        mPresenter?.followClassifySuccess()
                    }

                    override fun onError(t: Throwable?) {
                        mPresenter?.followClassifySuccess()
                    }
                })
        addSubscription(mDisposable)
    }
}