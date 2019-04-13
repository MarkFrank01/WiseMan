package com.zxcx.zhizhe.ui.my.selectAttention.interest

import com.zxcx.zhizhe.mvpBase.BaseModel
import com.zxcx.zhizhe.mvpBase.BaseRxJava
import com.zxcx.zhizhe.retrofit.AppClient
import com.zxcx.zhizhe.retrofit.BaseBean
import com.zxcx.zhizhe.retrofit.BaseSubscriber
import com.zxcx.zhizhe.retrofit.NullPostSubscriber
import com.zxcx.zhizhe.ui.search.result.user.SearchUserBean
import com.zxcx.zhizhe.utils.LogCat

/**
 * @author : MarkFrank01
 * @Created on 2019/1/16
 * @Description :
 */
class SelectInterestModel(presenter:SelectInterestContract.Presenter):BaseModel<SelectInterestContract.Presenter>(){

    init {
        this.mPresenter = presenter
    }


    //新版获取兴趣部分
    fun getInterestRecommend(){
        mDisposable = AppClient.getAPIService().interestRecommend
                .compose(BaseRxJava.io_main())
                .compose(BaseRxJava.handleResult())
                .subscribeWith(object : BaseSubscriber<InterestRecommendBean>(mPresenter){
                    override fun onNext(t: InterestRecommendBean) {
                        LogCat.e("Get"+t.collectionList?.size+"Get"+t.usersList?.size)
                        mPresenter?.getDataSuccess(t)
                    }
                })
        addSubscription(mDisposable)
    }

    //提交选择兴趣
    fun changeAttentionList(idList:List<Int>){
        mDisposable = AppClient.getAPIService().changeAttentionList(idList)
                .compose(BaseRxJava.handlePostResult())
                .compose(BaseRxJava.io_main_loading(mPresenter))
                .subscribeWith(object :NullPostSubscriber<BaseBean<*>>(mPresenter){
                    override fun onNext(t: BaseBean<*>?) {
                        mPresenter?.postSuccess()
                    }
                })
        addSubscription(mDisposable)
    }

    //关注用户
    fun followUser(authorId: Int) {
        mDisposable = AppClient.getAPIService().setUserFollow(authorId,0)
                .compose(BaseRxJava.handleResult())
                .compose(BaseRxJava.io_main())
                .subscribeWith(object :BaseSubscriber<SearchUserBean>(mPresenter){
                    override fun onNext(t: SearchUserBean) {
                        mPresenter?.mFollowUserSuccess(t)
                    }
                })
        addSubscription(mDisposable)
    }

    //取消关注用户
    fun unFollowUser(authorId: Int) {
        mDisposable = AppClient.getAPIService().setUserFollow(authorId,1)
                .compose(BaseRxJava.handleResult())
                .compose(BaseRxJava.io_main())
                .subscribeWith(object :BaseSubscriber<SearchUserBean>(mPresenter){
                    override fun onNext(t: SearchUserBean) {
                        mPresenter?.unFollowUserSuccess(t)
                    }
                })
        addSubscription(mDisposable)
    }
}