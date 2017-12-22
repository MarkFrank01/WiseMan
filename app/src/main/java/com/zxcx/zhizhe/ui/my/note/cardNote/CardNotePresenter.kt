package com.zxcx.zhizhe.ui.search.result.card

import com.zxcx.zhizhe.mvpBase.BasePresenter
import com.zxcx.zhizhe.ui.my.note.cardNote.NoteBean

class CardNotePresenter(view: CardNoteContract.View) : BasePresenter<CardNoteContract.View>(), CardNoteContract.Presenter {

    private val mModel: CardNoteModel

    init {
        attachView(view)
        mModel = CardNoteModel(this)
    }

    fun getCardNote(sortType: Int, page: Int, pageSize: Int) {
        mModel.getCardNote(sortType, page, pageSize)
    }

    override fun getDataSuccess(bean: List<NoteBean>) {
        mView.getDataSuccess(bean)
    }

    override fun getDataFail(msg: String) {
        mView.toastFail(msg)
    }

    override fun showLoading() {
        mView.showLoading()
    }

    override fun hideLoading() {
        mView.hideLoading()
    }

    override fun startLogin() {
        mView.startLogin()
    }

    override fun detachView() {
        super.detachView()
        mModel.onDestroy()
    }
}

