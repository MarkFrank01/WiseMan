package com.zxcx.zhizhe.ui.search.result.label

import com.zxcx.zhizhe.mvpBase.BaseModel
import com.zxcx.zhizhe.mvpBase.BaseRxJava
import com.zxcx.zhizhe.retrofit.AppClient
import com.zxcx.zhizhe.retrofit.BaseSubscriber
import com.zxcx.zhizhe.utils.Constants

/**
 * @author : MarkFrank01
 * @Created on 2019/1/16
 * @Description :
 */
class SearchLabelModel(presenter: SearchLabelContract.Presenter):BaseModel<SearchLabelContract.Presenter>(){

    init {
        this.mPresenter = presenter
    }

    fun searchLabel(keyword: String,page:Int) {
        mDisposable = AppClient.getAPIService().searchLabel(keyword,page,Constants.PAGE_SIZE)
                .compose(BaseRxJava.io_main())
                .compose(BaseRxJava.handleArrayResult())
                .subscribeWith(object :BaseSubscriber<List<SearchLabelBean>>(mPresenter){
                    override fun onNext(t: List<SearchLabelBean>) {
                        mPresenter?.getDataSuccess(t)
                    }
                })
        addSubscription(mDisposable)
    }
}