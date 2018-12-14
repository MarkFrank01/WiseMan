package com.zxcx.zhizhe.ui.my.daily

import com.zxcx.zhizhe.mvpBase.BaseModel
import com.zxcx.zhizhe.mvpBase.BaseRxJava
import com.zxcx.zhizhe.retrofit.AppClient
import com.zxcx.zhizhe.retrofit.BaseSubscriber
import com.zxcx.zhizhe.ui.card.hot.CardBean

/**
 * @author : MarkFrank01
 * @Created on 2018/12/13
 * @Description :
 */
class DailyModel(presenter:DailyContract.Presenter):BaseModel<DailyContract.Presenter>(){

    init {
        this.mPresenter = presenter
    }

//    fun getDailyList(termIndex:Int){
//        mDisposable = AppClient.getAPIService().getDailyList(termIndex)
//                .compose(BaseRxJava.handleArrayResult())
//                .compose(BaseRxJava.io_main())
//                .subscribeWith(object : BaseSubscriber<MutableList<DailyBean>>(mPresenter) {
//                    override fun onNext(list: MutableList<DailyBean>) {
//                        mPresenter!!.getDataSuccess(list)
//                    }
//                })
//        addSubscription(mDisposable)
//    }

    fun getDailyList1(termIndex: Int){
        mDisposable = AppClient.getAPIService().getDaliyList1(termIndex)
                .compose(BaseRxJava.handleArrayResult())
                .compose(BaseRxJava.io_main())
                .subscribeWith(object:BaseSubscriber<MutableList<CardBean>>(mPresenter){
                    override fun onNext(t: MutableList<CardBean>) {
                        mPresenter!!.getDataSuccess(t)
                    }
                })

    }
}