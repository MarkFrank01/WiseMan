package com.zxcx.zhizhe.ui.search.result.card

import com.zxcx.zhizhe.mvpBase.BaseModel
import com.zxcx.zhizhe.mvpBase.BaseRxJava
import com.zxcx.zhizhe.retrofit.AppClient
import com.zxcx.zhizhe.retrofit.BaseSubscriber
import com.zxcx.zhizhe.ui.my.note.cardNote.NoteBean

class CardNoteModel(presenter: CardNoteContract.Presenter) : BaseModel<CardNoteContract.Presenter>() {
    init {
        this.mPresenter = presenter
    }

    fun getCardNote(sortType: Int, page: Int, pageSize: Int) {
        mDisposable = AppClient.getAPIService().getNoteList(1,sortType, page, pageSize)
                .compose(BaseRxJava.io_main())
                .compose(BaseRxJava.handleArrayResult())
                .subscribeWith(object : BaseSubscriber<List<NoteBean>>(mPresenter) {
                    override fun onNext(list: List<NoteBean>) {
                        mPresenter?.getDataSuccess(list)
                    }
                })
        addSubscription(mDisposable)
    }
}


