package com.zxcx.zhizhe.ui.my.collect.collectFolder;

import android.support.annotation.NonNull;

import com.zxcx.zhizhe.mvpBase.BaseModel;
import com.zxcx.zhizhe.mvpBase.BaseRxJava;
import com.zxcx.zhizhe.retrofit.AppClient;
import com.zxcx.zhizhe.retrofit.BaseArrayBean;
import com.zxcx.zhizhe.retrofit.BaseBean;
import com.zxcx.zhizhe.retrofit.BaseSubscriber;
import com.zxcx.zhizhe.retrofit.NullPostSubscriber;

import java.util.List;

public class CollectFolderModel extends BaseModel<CollectFolderContract.Presenter> {
    public CollectFolderModel(@NonNull CollectFolderContract.Presenter present) {
        this.mPresenter = present;
    }

    public void getCollectFolder(int page, int pageSize){
        mDisposable = AppClient.getAPIService().getCollectFolder(page,pageSize)
                .compose(BaseRxJava.<BaseArrayBean<CollectFolderBean>>io_main())
                .compose(BaseRxJava.<CollectFolderBean>handleArrayResult())
                .subscribeWith(new BaseSubscriber<List<CollectFolderBean>>(mPresenter) {
                    @Override
                    public void onNext(List<CollectFolderBean> list) {
                        mPresenter.getDataSuccess(list);
                    }
                });
        addSubscription(mDisposable);
    }

    public void deleteCollectFolder(List<Integer> idList){
        mDisposable = AppClient.getAPIService().deleteCollectFolder(idList)
                .compose(BaseRxJava.handlePostResult())
                .compose(BaseRxJava.<BaseBean>io_main_loading(mPresenter))
                .subscribeWith(new NullPostSubscriber<BaseBean>(mPresenter) {
                    @Override
                    public void onNext(BaseBean bean) {
                        mPresenter.postSuccess();
                    }
                });
        addSubscription(mDisposable);
    }

    public void addCollectFolder(String name){
        mDisposable = AppClient.getAPIService().addCollectFolder(name)
                .compose(BaseRxJava.handlePostResult())
                .compose(BaseRxJava.<BaseBean>io_main_loading(mPresenter))
                .subscribeWith(new NullPostSubscriber<BaseBean>(mPresenter) {
                    @Override
                    public void onNext(BaseBean bean) {
                        mPresenter.postSuccess();
                    }
                });
        addSubscription(mDisposable);
    }
}


