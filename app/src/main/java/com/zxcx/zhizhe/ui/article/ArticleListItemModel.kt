package com.zxcx.zhizhe.ui.article

import com.zxcx.zhizhe.mvpBase.BaseModel
import com.zxcx.zhizhe.mvpBase.BaseRxJava
import com.zxcx.zhizhe.retrofit.AppClient
import com.zxcx.zhizhe.retrofit.BaseSubscriber
import com.zxcx.zhizhe.ui.welcome.ADBean

/**
 * @author : MarkFrank01
 * @Description :
 */
class ArticleListItemModel(present:ArticleListItemContract.Presenter):BaseModel<ArticleListItemContract.Presenter>(){

    init {
        this.mPresenter = present
    }

    fun getAD(position:Int){
        mDisposable = AppClient.getAPIService().getAD("403",position,0)
                .compose(BaseRxJava.io_main())
                .compose(BaseRxJava.handleArrayResult())
                .subscribeWith(object : BaseSubscriber<MutableList<ADBean>>(mPresenter){
                    override fun onNext(t: MutableList<ADBean>) {
                        mPresenter?.getADSuccess(t)
                    }
                })
        addSubscription(mDisposable)
    }
}