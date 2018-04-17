package com.zxcx.zhizhe.ui.my.note.newNote

import com.zxcx.zhizhe.mvpBase.BaseModel
import com.zxcx.zhizhe.mvpBase.BaseRxJava
import com.zxcx.zhizhe.retrofit.AppClient
import com.zxcx.zhizhe.retrofit.BaseBean
import com.zxcx.zhizhe.retrofit.NullPostSubscriber

class NoteEditorModel(presenter: NoteEditorContract.Presenter) : BaseModel<NoteEditorContract.Presenter>() {
    init {
        this.mPresenter = presenter
    }

    fun saveNote(cardId: Int, title: String?, content: String?) {
        mDisposable = AppClient.getAPIService().saveNote(cardId,title,content)
                .compose(BaseRxJava.io_main_loading(mPresenter))
                .compose(BaseRxJava.handlePostResult())
                .subscribeWith(object : NullPostSubscriber<BaseBean<*>>(mPresenter) {
                    override fun onNext(bean: BaseBean<*>) {
                        mPresenter?.saveFreedomNoteSuccess()
                    }
                })
        addSubscription(mDisposable)
    }
}


