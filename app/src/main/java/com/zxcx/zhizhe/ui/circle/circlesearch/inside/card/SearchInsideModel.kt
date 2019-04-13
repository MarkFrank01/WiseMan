package com.zxcx.zhizhe.ui.circle.circlesearch.inside.card

import com.zxcx.zhizhe.mvpBase.BaseModel
import com.zxcx.zhizhe.mvpBase.BaseRxJava
import com.zxcx.zhizhe.retrofit.AppClient
import com.zxcx.zhizhe.retrofit.BaseSubscriber
import com.zxcx.zhizhe.ui.card.hot.CardBean

/**
 * @author : MarkFrank01
 * @Created on 2019/3/20
 * @Description :
 */
class SearchInsideModel(presenter: SearchInsideContract.Presenter):BaseModel<SearchInsideContract.Presenter>() {

    init {
        this.mPresenter = presenter
    }

    fun searchCard(page:Int,pageSize:Int,circleId:Int,cardType:Int,keyword:String) {

        mDisposable = AppClient.getAPIService().searchCircleArticle(page,pageSize, circleId, cardType, keyword)
                .compose(BaseRxJava.io_main())
                .compose(BaseRxJava.handleArrayResult())
                .subscribeWith(object : BaseSubscriber<MutableList<CardBean>>(mPresenter) {
                    override fun onNext(list: MutableList<CardBean>) {
                        mPresenter?.getDataSuccess(list)
                    }
                })
        addSubscription(mDisposable)
    }
}