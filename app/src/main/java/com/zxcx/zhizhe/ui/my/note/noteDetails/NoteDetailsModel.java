package com.zxcx.zhizhe.ui.my.note.noteDetails;

import android.support.annotation.NonNull;

import com.zxcx.zhizhe.mvpBase.BaseModel;
import com.zxcx.zhizhe.mvpBase.BaseRxJava;
import com.zxcx.zhizhe.retrofit.AppClient;
import com.zxcx.zhizhe.retrofit.BaseBean;
import com.zxcx.zhizhe.retrofit.BaseSubscriber;
import com.zxcx.zhizhe.retrofit.NullPostSubscriber;

public class NoteDetailsModel extends BaseModel<NoteDetailsContract.Presenter> {
    public NoteDetailsModel(@NonNull NoteDetailsContract.Presenter present) {
        this.mPresenter = present;
    }

    public void getNoteDetails(int noteId, int noteType){
        mDisposable = AppClient.getAPIService().getNoteDetails(noteId,noteType)
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


