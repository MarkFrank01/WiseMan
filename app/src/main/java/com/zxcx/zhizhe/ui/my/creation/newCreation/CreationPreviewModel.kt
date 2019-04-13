package com.zxcx.zhizhe.ui.my.creation.newCreation

import com.zxcx.zhizhe.mvpBase.BaseModel
import com.zxcx.zhizhe.mvpBase.BaseRxJava
import com.zxcx.zhizhe.retrofit.AppClient
import com.zxcx.zhizhe.retrofit.BaseBean
import com.zxcx.zhizhe.retrofit.NullPostSubscriber

/**
 * @author : MarkFrank01
 * @Description :
 */
class CreationPreviewModel(present:CreationPreviewContract.Presenter):BaseModel<CreationPreviewContract.Presenter>(){
    init {
        this.mPresenter = present
    }

    fun deleteNote(noteId:Int){
        mDisposable = AppClient.getAPIService().removeNote(noteId)
                .compose(BaseRxJava.handlePostResult())
                .compose(BaseRxJava.io_main())
                .subscribeWith(object : NullPostSubscriber<BaseBean<*>>(mPresenter){
                    override fun onNext(t: BaseBean<*>?) {
                        mPresenter?.postSuccess()
                    }
                })
        addSubscription(mDisposable)
    }
}