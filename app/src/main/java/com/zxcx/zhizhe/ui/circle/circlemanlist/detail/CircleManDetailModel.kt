package com.zxcx.zhizhe.ui.circle.circlemanlist.detail

import com.zxcx.zhizhe.mvpBase.BaseModel
import com.zxcx.zhizhe.mvpBase.BaseRxJava
import com.zxcx.zhizhe.retrofit.AppClient
import com.zxcx.zhizhe.retrofit.BaseBean
import com.zxcx.zhizhe.retrofit.BaseSubscriber
import com.zxcx.zhizhe.ui.card.hot.CardBean
import com.zxcx.zhizhe.ui.circle.circlehome.CircleBean
import com.zxcx.zhizhe.ui.circle.circlehome.CircleUserBean

/**
 * @author : MarkFrank01
 * @Created on 2019/3/27
 * @Description :
 */
class CircleManDetailModel(presenter:CircleManDetailContract.Presenter):BaseModel<CircleManDetailContract.Presenter>() {

    init {
        this.mPresenter = presenter
    }

    fun getCircleListByAuthorId(page:Int,pageSize:Int,userId:Int){
        mDisposable = AppClient.getAPIService().getCircleListByAuthorId(page,pageSize, userId)
                .compose(BaseRxJava.handleArrayResult())
                .compose(BaseRxJava.io_main())
                .subscribeWith(object :BaseSubscriber<MutableList<CircleBean>>(mPresenter){
                    override fun onNext(t: MutableList<CircleBean>) {
                        mPresenter?.getCircleListByAuthorIdSuccess(t)
                    }
                })
        addSubscription(mDisposable)
    }

    fun getAuthorInfo(userId: Int){
        mDisposable = AppClient.getAPIService().getAuthorInfo2(userId)
                .compose(BaseRxJava.handleResult())
                .compose(BaseRxJava.io_main())
                .subscribeWith(object : BaseSubscriber<CircleUserBean>(mPresenter){
                    override fun onNext(t: CircleUserBean) {
                        mPresenter?.getAuthorInfoSuccess(t)
                    }
                })
        addSubscription(mDisposable)
    }

    fun getOtherUserCreationSuccess(userId:Int,orderType:Int,page:Int,pageSize:Int){
        mDisposable = AppClient.getAPIService().getOtherUserCreation(userId,orderType,page, pageSize)
                .compose(BaseRxJava.handleArrayResult())
                .compose(BaseRxJava.io_main())
                .subscribeWith(object :BaseSubscriber<MutableList<CardBean>>(mPresenter){
                    override fun onNext(t: MutableList<CardBean>) {
                        mPresenter?.getOtherUserCreationSuccess(t)
                    }
                })
        addSubscription(mDisposable)
    }

    fun setUserFollow(authorId:Int,followType:Int){
        mDisposable = AppClient.getAPIService().setUserFollow(authorId,followType)
                .compose(BaseRxJava.handlePostResult())
                .compose(BaseRxJava.io_main())
                .subscribeWith(object :BaseSubscriber<BaseBean<*>>(mPresenter){
                    override fun onNext(t: BaseBean<*>) {
                        mPresenter?.followSuccess()
                    }
                })
        addSubscription(mDisposable)
    }
}