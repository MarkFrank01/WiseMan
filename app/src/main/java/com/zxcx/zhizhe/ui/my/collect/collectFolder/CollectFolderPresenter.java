package com.zxcx.zhizhe.ui.my.collect.collectFolder;

import android.support.annotation.NonNull;

import com.zxcx.zhizhe.mvpBase.BasePresenter;

import java.util.List;

public class CollectFolderPresenter extends BasePresenter<CollectFolderContract.View> implements CollectFolderContract.Presenter {

    private final CollectFolderModel mModel;

    public CollectFolderPresenter(@NonNull CollectFolderContract.View view) {
        attachView(view);
        mModel = new CollectFolderModel(this);
    }

    public void getCollectFolder(int page, int pageSize){
        mModel.getCollectFolder(page,pageSize);
    }

    public void deleteCollectFolder(List<Integer> idList){
        mModel.deleteCollectFolder(idList);
    }

    public void addCollectFolder(String name){
        mModel.addCollectFolder(name);
    }

    @Override
    public void getDataSuccess(List<CollectFolderBean> bean) {
        mView.getDataSuccess(bean);
    }

    @Override
    public void getDataFail(String msg) {
        mView.toastFail(msg);
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

    @Override
    public void postSuccess() {
        mView.postSuccess();
    }

    @Override
    public void postFail(String msg) {
        mView.postFail(msg);
    }
}

