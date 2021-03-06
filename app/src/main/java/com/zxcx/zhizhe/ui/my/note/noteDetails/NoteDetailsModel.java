package com.zxcx.zhizhe.ui.my.note.noteDetails;

import android.support.annotation.NonNull;

import com.zxcx.zhizhe.mvpBase.BaseModel;
import com.zxcx.zhizhe.mvpBase.BaseRxJava;
import com.zxcx.zhizhe.retrofit.AppClient;
import com.zxcx.zhizhe.retrofit.BaseSubscriber;

public class NoteDetailsModel extends BaseModel<NoteDetailsContract.Presenter> {
	
	public NoteDetailsModel(@NonNull NoteDetailsContract.Presenter present) {
		this.mPresenter = present;
	}
	
	public void getNoteDetails(int noteId) {
		mDisposable = AppClient.getAPIService().getNoteDetails(noteId, 4)
			.compose(BaseRxJava.INSTANCE.io_main())
			.compose(BaseRxJava.INSTANCE.handleResult())
			.subscribeWith(new BaseSubscriber<NoteDetailsBean>(mPresenter) {
				@Override
				public void onNext(NoteDetailsBean bean) {
					mPresenter.getDataSuccess(bean);
				}
			});
		addSubscription(mDisposable);
	}
}


