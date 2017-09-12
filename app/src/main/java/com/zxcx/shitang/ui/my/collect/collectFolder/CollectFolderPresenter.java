package com.zxcx.shitang.ui.my.collect.collectFolder;

import android.support.annotation.NonNull;

import com.zxcx.shitang.mvpBase.BasePresenter;
import com.zxcx.shitang.mvpBase.PostBean;

import java.util.List;

public class CollectFolderPresenter extends BasePresenter<CollectFolderContract.View> implements CollectFolderContract.Presenter {

    private final CollectFolderModel mModel;

    public CollectFolderPresenter(@NonNull CollectFolderContract.View view) {
        attachView(view);
        mModel = new CollectFolderModel(this);
    }

    public void getCollectFolder(int userId, int page, int pageSize){
        mModel.getCollectFolder(userId, page,pageSize);
    }

    public void deleteCollectFolder(int userId, List<Integer> idList){
        mModel.deleteCollectFolder(userId, idList);
    }

    public void addCollectFolder(int userId, String name){
        mModel.addCollectFolder(userId, name);
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

    public void detachView() {
        super.detachView();
        mModel.onDestroy();
    }

    @Override
    public void postSuccess(PostBean bean) {
        mView.postSuccess(bean);
    }

    @Override
    public void postFail(String msg) {
        mView.postFail(msg);
    }
}

