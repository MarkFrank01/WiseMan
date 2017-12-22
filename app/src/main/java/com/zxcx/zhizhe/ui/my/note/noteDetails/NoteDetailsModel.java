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
                .compose(BaseRxJava.io_main())
                .compose(BaseRxJava.handleResult())
                .subscribeWith(new BaseSubscriber<NoteDetailsBean>(mPresenter) {
                    @Override
                    public void onNext(NoteDetailsBean bean) {
                        mPresenter.getDataSuccess(bean);
                    }
                });
        addSubscription(mDisposable);
    }

    public void submitReview(int noteId) {
        mDisposable = AppClient.getAPIService().saveFreeNode(noteId,null,null, null,null,1)
                .compose(BaseRxJava.io_main_loading(mPresenter))
                .compose(BaseRxJava.handlePostResult())
                .subscribeWith(new NullPostSubscriber<BaseBean>(mPresenter) {

                    @Override
                    public void onNext(BaseBean bean) {
                        mPresenter.postSuccess();
                    }
                });
        addSubscription(mDisposable);
    }
}


