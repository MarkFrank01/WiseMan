package com.zxcx.zhizhe.ui.my.note.noteDetails;

import android.support.annotation.NonNull;

import com.zxcx.zhizhe.mvpBase.BasePresenter;

public class NoteDetailsPresenter extends BasePresenter<NoteDetailsContract.View> implements NoteDetailsContract.Presenter {

    private final NoteDetailsModel mModel;

    public NoteDetailsPresenter(@NonNull NoteDetailsContract.View view) {
        attachView(view);
        mModel = new NoteDetailsModel(this);
    }

    public void getNoteDetails(int noteId,int noteType){
        mModel.getNoteDetails(noteId,noteType);
    }

    @Override
    public void getDataSuccess(NoteDetailsBean bean) {
        mView.getDataSuccess(bean);
    }

    @Override
    public void getDataFail(String msg) {
        mView.toastFail(msg);
    }

    @Override
    public void postSuccess() {
        mView.postSuccess();
    }

    @Override
    public void postFail(String msg) {
        mView.postFail(msg);
    }

    @Override
    public void showLoading() {
        mView.showLoading();
    }

    @Override
    public void hideLoading() {
        mView.hideLoading();
    }

    @Override
    public void startLogin() {
        mView.startLogin();
    }

    public void detachView() {
        super.detachView();
        mModel.onDestroy();
    }
}

