package com.zxcx.zhizhe.ui.my.note

import com.zxcx.zhizhe.mvpBase.BaseModel
import com.zxcx.zhizhe.mvpBase.BaseRxJava
import com.zxcx.zhizhe.retrofit.AppClient
import com.zxcx.zhizhe.retrofit.BaseBean
import com.zxcx.zhizhe.retrofit.BaseSubscriber
import com.zxcx.zhizhe.retrofit.NullPostSubscriber

class NoteModel(presenter: NoteContract.Presenter) : BaseModel<NoteContract.Presenter>() {
	init {
		this.mPresenter = presenter
	}

	fun getNoteList(page: Int, pageSize: Int) {
		mDisposable = AppClient.getAPIService().getNoteList(0, page, pageSize)
				.compose(BaseRxJava.io_main())
				.compose(BaseRxJava.handleArrayResult())
				.subscribeWith(object : BaseSubscriber<List<NoteBean>>(mPresenter) {
					override fun onNext(list: List<NoteBean>) {
						mPresenter?.getDataSuccess(list)
					}
				})
		addSubscription(mDisposable)
	}

	fun deleteNote(noteId: Int) {
		mDisposable = AppClient.getAPIService().removeNote(noteId)
				.compose(BaseRxJava.handlePostResult())
				.compose(BaseRxJava.io_main())
				.subscribeWith(object : NullPostSubscriber<BaseBean<*>>(mPresenter) {
					override fun onNext(t: BaseBean<*>?) {
						mPresenter?.postSuccess()
					}
				})
		addSubscription(mDisposable)
	}
}


