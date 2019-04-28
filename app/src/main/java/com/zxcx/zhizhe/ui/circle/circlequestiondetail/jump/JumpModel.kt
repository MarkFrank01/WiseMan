package com.zxcx.zhizhe.ui.circle.circlequestiondetail.jump

import com.zxcx.zhizhe.mvpBase.BaseModel
import com.zxcx.zhizhe.mvpBase.BaseRxJava
import com.zxcx.zhizhe.retrofit.AppClient
import com.zxcx.zhizhe.retrofit.BaseBean
import com.zxcx.zhizhe.retrofit.PostSubscriber

/**
 * @author : MarkFrank01
 * @Created on 2019/4/28
 * @Description :
 */
class JumpModel(presenter: JumpContract.Presenter):BaseModel<JumpContract.Presenter>() {

    init {
        mPresenter = presenter
    }

    //评论中的点赞
    fun likeQAOrQAComment_comment(qaId: Int, commentId: Int) {
        mDisposable = AppClient.getAPIService().likeQAOrQAComment_comment(qaId, commentId, 1)
                .compose(BaseRxJava.handlePostResult())
                .compose(BaseRxJava.io_main())
                .subscribeWith(object : PostSubscriber<BaseBean<*>>(mPresenter){
                    override fun onNext(t: BaseBean<*>?) {
                        mPresenter?.likeSuccess()
                    }
                })
        addSubscription(mDisposable)
    }

    //取消评论中的点赞
    fun unlikeQAOrQAComment_comment(qaId: Int, commentId: Int) {
        mDisposable = AppClient.getAPIService().likeQAOrQAComment_comment(qaId, commentId, 0)
                .compose(BaseRxJava.handlePostResult())
                .compose(BaseRxJava.io_main())
                .subscribeWith(object : PostSubscriber<BaseBean<*>>(mPresenter){
                    override fun onNext(t: BaseBean<*>?) {
                        mPresenter?.unlikeSuccess()
                    }
                })
        addSubscription(mDisposable)
    }
}