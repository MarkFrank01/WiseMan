package com.zxcx.zhizhe.ui.my.note.newNote

import com.zxcx.zhizhe.mvpBase.BasePresenter

class NoteEditorPresenter(view: NoteEditorContract.View) : BasePresenter<NoteEditorContract.View>(), NoteEditorContract.Presenter {

    private val mModel: NoteEditorModel

    init {
        attachView(view)
        mModel = NoteEditorModel(this)
    }

    fun saveNote(cardId: Int, title: String?, content: String?) {
        mModel.saveNote(cardId,title,content)
    }

    fun submitReview(cardId: Int, title: String,imageUrl: String,cardBagId: Int,content: String) {
        mModel.submitReview(cardId,title,imageUrl,cardBagId,content)
    }

    override fun postSuccess() {
        mView.postSuccess()
    }

    override fun saveFreedomNoteSuccess() {
        mView.saveFreedomNoteSuccess()
    }

    override fun postFail(msg: String) {
        mView.postFail(msg)
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

