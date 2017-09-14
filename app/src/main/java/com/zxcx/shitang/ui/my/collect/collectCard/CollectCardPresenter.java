package com.zxcx.shitang.ui.my.collect.collectCard;

import android.support.annotation.NonNull;

import com.zxcx.shitang.mvpBase.BasePresenter;
import com.zxcx.shitang.mvpBase.PostBean;

import java.util.List;

public class CollectCardPresenter extends BasePresenter<CollectCardContract.View> implements CollectCardContract.Presenter {

    private final CollectCardModel mModel;

    public CollectCardPresenter(@NonNull CollectCardContract.View view) {
        attachView(view);
        mModel = new CollectCardModel(this);
    }

    public void getCollectCard(int userId, int id, int page, int pageSize){
        mModel.getCollectCard(userId, id, page,pageSize);
    }

    public void deleteCollectCard(int userId, int id, List<Integer> idList){
        mModel.deleteCollectCard(userId, id, idList);
    }

    public void changeCollectFolderName(int userId, int id, String name){
        mModel.changeCollectFolderName(userId, id, name);
    }

    @Override
    public void getDataSuccess(List<CollectCardBean> bean) {
        mView.getDataSuccess(bean);
    }

    @Override
    public void getDataFail(String msg) {
        mView.toastFail(msg);
    }

    @Override
    public void postSuccess(PostBean bean) {
        mView.postSuccess(bean);
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

