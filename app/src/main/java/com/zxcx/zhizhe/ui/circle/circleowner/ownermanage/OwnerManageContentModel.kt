package com.zxcx.zhizhe.ui.circle.circleowner.ownermanage

import com.zxcx.zhizhe.mvpBase.BaseModel
import com.zxcx.zhizhe.mvpBase.BaseRxJava
import com.zxcx.zhizhe.retrofit.AppClient
import com.zxcx.zhizhe.retrofit.BaseSubscriber
import com.zxcx.zhizhe.retrofit.HintBean
import com.zxcx.zhizhe.ui.card.hot.CardBean

/**
 * @author : MarkFrank01
 * @Created on 2019/3/11
 * @Description :
 */
class OwnerManageContentModel(presenter:OwnerManageContentContract.Presenter):BaseModel<OwnerManageContentContract.Presenter>() {

    init {
        this.mPresenter = presenter
    }

    fun getArticleByCircleId(circleId:Int,orderType:Int,pageIndex:Int,pageSize:Int){
        mDisposable = AppClient.getAPIService().getArticleByCircleId(circleId, orderType, pageIndex, pageSize)
                .compose(BaseRxJava.io_main())
                .compose(BaseRxJava.handleArrayResult())
                .subscribeWith(object :BaseSubscriber<MutableList<CardBean>>(mPresenter){
                    override fun onNext(t: MutableList<CardBean>) {
                        mPresenter?.getDataSuccess(t)
                    }
                })
        addSubscription(mDisposable)

    }

    fun setArticleFixTop(circleId:Int,articleId:Int,fixType:Int){
        mDisposable = AppClient.getAPIService().setArticleFixTop(circleId, articleId, fixType)
                .compose(BaseRxJava.io_main())
                .compose(BaseRxJava.handleResult())
                .subscribeWith(object :BaseSubscriber<CardBean>(mPresenter){
                    override fun onNext(t: CardBean) {
                        mPresenter?.setArticleFixTopSuccess("成功")
                    }
                })
        addSubscription(mDisposable)
    }

    fun removeArticle(circleId:Int,articleId: Int){
        mDisposable = AppClient.getAPIService().removeArticle(circleId, articleId)
                .compose(BaseRxJava.io_main())
                .compose(BaseRxJava.handleResult())
                .subscribeWith(object :BaseSubscriber<HintBean>(mPresenter){
                    override fun onNext(t: HintBean) {
                        mPresenter?.removeArticleSuccess()
                    }
                })
        addSubscription(mDisposable)
    }
}