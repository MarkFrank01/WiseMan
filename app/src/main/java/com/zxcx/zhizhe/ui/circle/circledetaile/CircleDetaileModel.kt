package com.zxcx.zhizhe.ui.circle.circledetaile

import com.zxcx.zhizhe.mvpBase.BaseModel
import com.zxcx.zhizhe.mvpBase.BaseRxJava
import com.zxcx.zhizhe.retrofit.*
import com.zxcx.zhizhe.ui.card.hot.CardBean
import com.zxcx.zhizhe.ui.circle.circlehome.CircleBean
import com.zxcx.zhizhe.ui.my.money.MoneyBean

/**
 * @author : MarkFrank01
 * @Created on 2019/1/25
 * @Description :
 */
class CircleDetaileModel(presenter: CircleDetaileContract.Presenter):BaseModel<CircleDetaileContract.Presenter>(){
    init {
        this.mPresenter = presenter
    }

    //通过圈子id获取圈子详情
    fun getCircleBasicInfo(circleId:Int){
        mDisposable = AppClient.getAPIService().getCircleBasicInfo(circleId)
                .compose(BaseRxJava.io_main())
                .compose(BaseRxJava.handleResult())
                .subscribeWith(object :BaseSubscriber<CircleBean>(mPresenter){
                    override fun onNext(t: CircleBean) {
                        mPresenter?.getCircleBasicInfoSuccess(t)
                    }
                })
        addSubscription(mDisposable)
    }

    //获取圈子成员
    fun getCircleMemberByCircleId(orderType:Int,circleId:Int,pageIndex:Int,pageSize:Int){
        mDisposable = AppClient.getAPIService().getCircleMemberByCircleId(orderType, circleId, pageIndex, pageSize)
                .compose(BaseRxJava.io_main())
                .compose(BaseRxJava.handleArrayResult())
                .subscribeWith(object :BaseSubscriber<MutableList<CircleBean>>(mPresenter){
                    override fun onNext(t: MutableList<CircleBean>) {
                        mPresenter?.getCircleMemberByCircleIdSuccess(t)
                    }
                })
        addSubscription(mDisposable)
    }

    //获取圈子话题
    fun getCircleQAByCircleId(orderType:Int,circleId:Int,pageIndex:Int,pageSize:Int){
        mDisposable = AppClient.getAPIService().getCircleQAByCircleId2(orderType, circleId, pageIndex, pageSize)
                .compose(BaseRxJava.io_main())
                .compose(BaseRxJava.handleArrayResult())
                .subscribeWith(object :BaseSubscriber<MutableList<CircleDetailBean>>(mPresenter){
                    override fun onNext(t: MutableList<CircleDetailBean>) {
                        mPresenter?.getCircleQAByCircleIdSuccess(t)
                    }
                })
        addSubscription(mDisposable)
    }

    //举报圈子
    fun reportCircle(circleId:Int,reportType:Int){
        mDisposable = AppClient.getAPIService().reportCircle(circleId, reportType)
                .compose(BaseRxJava.io_main())
                .compose(BaseRxJava.handleResult())
                .subscribeWith(object : BaseSubscriber<HintBean>(mPresenter){
                    override fun onNext(t: HintBean) {
                        mPresenter?.reportCircleSuccess()
                    }
                })
        addSubscription(mDisposable)
    }

    //是否置顶话题
    fun setQAFixTop(qaId:Int,fixType:Int){
        mDisposable = AppClient.getAPIService().setQAFixTop(qaId, fixType)
                .compose(BaseRxJava.io_main())
                .compose(BaseRxJava.handleResult())
                .subscribeWith(object :BaseSubscriber<CardBean>(mPresenter){
                    override fun onNext(t: CardBean) {
                        mPresenter?.setQAFixTopSuccess()
                    }

                    override fun onError(t: Throwable) {
                        mPresenter?.setQAFixTopSuccess()
                    }
                })
        addSubscription(mDisposable)
    }

    //删除话题
    fun deleteQa(qaId: Int){
        mDisposable = AppClient.getAPIService().deleteQa(qaId)
                .compose(BaseRxJava.io_main())
                .compose(BaseRxJava.handleResult())
                .subscribeWith(object :BaseSubscriber<HintBean>(mPresenter){
                    override fun onNext(t: HintBean) {
                        mPresenter?.deleteQaSuccess()
                    }
                })
        addSubscription(mDisposable)
    }

    //获取自己的余额
    fun getAccountDetails(){
        mDisposable = AppClient.getAPIService().accountDetails
                .compose(BaseRxJava.io_main())
                .compose(BaseRxJava.handleResult())
                .subscribeWith(object :BaseSubscriber<MoneyBean>(mPresenter){
                    override fun onNext(t: MoneyBean) {
                        mPresenter?.getAccountDetailsSuccess(t)
                    }
                })
        addSubscription(mDisposable)
    }

    //Android用智者币进圈
    fun joinCircleByZzbForAndroid(circleId:Int){
        mDisposable = AppClient.getAPIService().joinCircleByZzbForAndroid(circleId)
                .compose(BaseRxJava.io_main())
                .compose(BaseRxJava.handlePostResult())
                .subscribeWith(object :NullPostSubscriber<BaseBean<*>>(mPresenter){
                    override fun onNext(t: BaseBean<*>?) {
                        mPresenter?.postSuccess()
                    }

                    override fun onError(t: Throwable?) {
                        mPresenter?.postFail(t?.message.toString())
                    }
                })
        addSubscription(mDisposable)
    }

    //free
    fun freeAddCircle(circleId: Int){
        mDisposable = AppClient.getAPIService().joinCircleByFreeLimitedTimeForAndroid(circleId)
                .compose(BaseRxJava.io_main())
                .compose(BaseRxJava.handlePostResult())
                .subscribeWith(object :NullPostSubscriber<BaseBean<*>>(mPresenter){
                    override fun onNext(t: BaseBean<*>?) {
                        mPresenter?.postSuccess()
                    }

                    override fun onError(t: Throwable?) {
                        mPresenter?.postFail(t?.message.toString())
                    }
                })
        addSubscription(mDisposable)
    }
}