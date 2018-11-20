package com.zxcx.zhizhe.ui.card.cardDetails

import com.zxcx.zhizhe.mvpBase.BaseModel
import com.zxcx.zhizhe.mvpBase.BaseRxJava
import com.zxcx.zhizhe.retrofit.AppClient
import com.zxcx.zhizhe.retrofit.BaseBean
import com.zxcx.zhizhe.retrofit.BaseSubscriber
import com.zxcx.zhizhe.retrofit.PostSubscriber
import com.zxcx.zhizhe.ui.card.hot.CardBean
import io.reactivex.subscribers.DisposableSubscriber

class CardDetailsModel(presenter: CardDetailsContract.Presenter) : BaseModel<CardDetailsContract.Presenter>() {
    init {
        this.mPresenter = presenter
    }

    fun getCardDetails(cardId: Int) {
        mDisposable = AppClient.getAPIService().getCardDetails(cardId)
                .compose(BaseRxJava.io_main())
                .compose(BaseRxJava.handleResult())
                .subscribeWith(object : BaseSubscriber<CardBean>(mPresenter) {
                    override fun onNext(bean: CardBean) {
                        mPresenter?.getDataSuccess(bean)
                    }
                })
        addSubscription(mDisposable)
    }

    fun readCard(cardId: Int) {
        val disposableSubscriber = AppClient.getAPIService().readArticle(cardId)
                .compose(BaseRxJava.io_main())
                .compose(BaseRxJava.handlePostResult())
                .subscribeWith(object : DisposableSubscriber<BaseBean<*>>() {
                    override fun onNext(bean: BaseBean<*>) {
                        //保持为空，不需要返回结果
                    }

                    override fun onError(t: Throwable) {}

                    override fun onComplete() {}
                })
    }

    fun likeCard(cardId: Int) {
        mDisposable = AppClient.getAPIService().likeCard(cardId)
                .compose(BaseRxJava.io_main())
                .compose(BaseRxJava.handleResult())
                .subscribeWith(object : PostSubscriber<Any>(mPresenter) {
                    override fun onNext(bean: Any) {
                        //保持为空，不需要返回结果
                    }
                })
        addSubscription(mDisposable)
    }

    fun removeLikeCard(cardId: Int) {
        mDisposable = AppClient.getAPIService().removeLikeCard(cardId)
                .compose(BaseRxJava.io_main())
                .compose(BaseRxJava.handleResult())
                .subscribeWith(object : PostSubscriber<Any>(mPresenter) {
                    override fun onNext(bean: Any) {
                        //保持为空，不需要返回结果
                    }
                })
        addSubscription(mDisposable)
    }

    fun addCollectCard(cardId: Int) {
        mDisposable = AppClient.getAPIService().addCollectCard(cardId)
                .compose(BaseRxJava.handleResult())
                .compose(BaseRxJava.io_main())
                .subscribeWith(object : PostSubscriber<Any>(mPresenter) {
                    override fun onNext(bean: Any) {
                        //保持为空，不需要返回结果
                    }
                })
        addSubscription(mDisposable)
    }

    fun removeCollectCard(cardId: Int) {
        mDisposable = AppClient.getAPIService().removeCollectCard(cardId)
                .compose(BaseRxJava.handleResult())
                .compose(BaseRxJava.io_main())
                .subscribeWith(object : PostSubscriber<Any>(mPresenter) {
                    override fun onNext(bean: Any) {
                        //保持为空，不需要返回结果
                    }
                })
        addSubscription(mDisposable)
    }

    fun setUserFollow(authorId: Int, followType: Int, bean: CardBean) {
        mDisposable = AppClient.getAPIService().setUserFollow(authorId, followType)
                .compose(BaseRxJava.handlePostResult())
                .compose(BaseRxJava.io_main())
                .subscribeWith(object : PostSubscriber<BaseBean<*>>(mPresenter) {
                    override fun onNext(a: BaseBean<*>) {
                        mPresenter?.followSuccess(bean)
                    }
                })
        addSubscription(mDisposable)
    }

    fun deleteNote(noteId: Int) {
        mDisposable = AppClient.getAPIService().removeNote(noteId)
                .compose(BaseRxJava.handlePostResult())
                .compose(BaseRxJava.io_main())
                .subscribeWith(object : PostSubscriber<BaseBean<*>>(mPresenter) {
                    override fun onNext(t: BaseBean<*>?) {
                        mPresenter?.deleteSuccess()
                    }
                })
        addSubscription(mDisposable)
    }

   fun saveCardNode(title: String?, imageUrl: String?, withCardId: Int, content: String?,bean1: CardBean) {
        mDisposable = AppClient.getAPIService().saveCardNode(title, imageUrl, withCardId, 3, 0, content)
                .compose(BaseRxJava.handlePostResult())
                .compose(BaseRxJava.io_main_loading(mPresenter))
                .subscribeWith(object : PostSubscriber<BaseBean<*>>(mPresenter) {
                    override fun onNext(bean: BaseBean<*>) {
                        mPresenter?.postSuccess(bean1)
                    }
                })
        addSubscription(mDisposable)
    }

    fun saveCardNode( withCardId: Int,styleType:Int,cardBean:CardBean){
        mDisposable = AppClient.getAPIService().saveFreeNode(withCardId,styleType,1)
                .compose(BaseRxJava.io_main())
                .compose(BaseRxJava.handlePostResult())
                .subscribeWith(object : PostSubscriber<BaseBean<*>>(mPresenter) {
                    override fun onNext(bean: BaseBean<*>) {
                        mPresenter?.postSuccess(cardBean)
                    }
                })
    }
}


