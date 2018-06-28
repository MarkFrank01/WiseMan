package com.zxcx.zhizhe.ui.my.note

import com.zxcx.zhizhe.mvpBase.BasePresenter

class NotePresenter(view: NoteContract.View) : BasePresenter<NoteContract.View>(), NoteContract.Presenter {

	private val mModel: NoteModel

	init {
		attachView(view)
		mModel = NoteModel(this)
	}

	fun getNoteList(page: Int, pageSize: Int) {
		mModel.getNoteList(page, pageSize)
	}

	fun deleteNote(noteId: Int) {
		mModel.deleteNote(noteId)
	}

	override fun getDataSuccess(bean: List<NoteBean>) {
		mView.getDataSuccess(bean)
	}

	override fun getDataFail(msg: String) {
		mView.toastFail(msg)
	}

	override fun postSuccess() {
		mView.postSuccess()
	}

	override fun postFail(msg: String?) {
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

