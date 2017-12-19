package com.zxcx.zhizhe.ui.my.note.freedomNote

import com.zxcx.zhizhe.mvpBase.BasePresenter
import com.zxcx.zhizhe.ui.search.result.card.NoteBean

class FreedomNotePresenter(view: FreedomNoteContract.View) : BasePresenter<FreedomNoteContract.View>(), FreedomNoteContract.Presenter {

    private val mModel: FreedomNoteModel

    init {
        attachView(view)
        mModel = FreedomNoteModel(this)
    }

    fun getFreedomNote(sortType: Int, page: Int, pageSize: Int) {
        mModel.getFreedomNote(sortType, page, pageSize)
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

