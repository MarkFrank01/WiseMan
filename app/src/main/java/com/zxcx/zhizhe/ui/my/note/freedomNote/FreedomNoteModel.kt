package com.zxcx.zhizhe.ui.my.note.freedomNote

import com.zxcx.zhizhe.mvpBase.BaseModel
import com.zxcx.zhizhe.mvpBase.BaseRxJava
import com.zxcx.zhizhe.retrofit.AppClient
import com.zxcx.zhizhe.retrofit.BaseSubscriber
import com.zxcx.zhizhe.ui.search.result.card.NoteBean

class FreedomNoteModel(presenter: FreedomNoteContract.Presenter) : BaseModel<FreedomNoteContract.Presenter>() {
    init {
        this.mPresenter = presenter
    }

    fun getFreedomNote(sortType: Int, page: Int, pageSize: Int) {
        mDisposable = AppClient.getAPIService().getNoteList(0,sortType, page, pageSize)
                .compose(BaseRxJava.io_main())
                .compose(BaseRxJava.handleArrayResult())
                .subscribeWith(object : BaseSubscriber<List<NoteBean>>(mPresenter) {
                    override fun onNext(list: List<NoteBean>) {
                        mPresenter.getDataSuccess(list)
                    }
                })
        addSubscription(mDisposable)
    }
}


