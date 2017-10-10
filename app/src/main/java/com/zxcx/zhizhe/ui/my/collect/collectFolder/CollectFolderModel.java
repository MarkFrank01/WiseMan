package com.zxcx.zhizhe.ui.my.collect.collectFolder;

import android.support.annotation.NonNull;

import com.zxcx.zhizhe.mvpBase.BaseModel;
import com.zxcx.zhizhe.retrofit.AppClient;
import com.zxcx.zhizhe.retrofit.BaseArrayBean;
import com.zxcx.zhizhe.retrofit.BaseBean;
import com.zxcx.zhizhe.retrofit.BaseSubscriber;
import com.zxcx.zhizhe.retrofit.NullPostSubscriber;

import java.util.List;

public class CollectFolderModel extends BaseModel<CollectFolderContract.Presenter> {
    public CollectFolderModel(@NonNull CollectFolderContract.Presenter present) {
        this.mPresent = present;
    }

    public void getCollectFolder(int page, int pageSize){
        mDisposable = AppClient.getAPIService().getCollectFolder(page,pageSize)
                .compose(this.<BaseArrayBean<CollectFolderBean>>io_main())
                .compose(this.<CollectFolderBean>handleArrayResult())
                .subscribeWith(new BaseSubscriber<List<CollectFolderBean>>(mPresent) {
                    @Override
                    public void onNext(List<CollectFolderBean> list) {
                        mPresent.getDataSuccess(list);
                    }
                });
        addSubscription(mDisposable);
    }

    public void deleteCollectFolder(List<Integer> idList){
        mDisposable = AppClient.getAPIService().deleteCollectFolder(idList)
                .compose(this.<BaseBean>io_main())
                .compose(handlePostResult())
                .subscribeWith(new NullPostSubscriber<BaseBean>(mPresent) {
                    @Override
                    public void onNext(BaseBean bean) {
                        mPresent.postSuccess();
                    }
                });
        addSubscription(mDisposable);
    }

    public void addCollectFolder(String name){
        mDisposable = AppClient.getAPIService().addCollectFolder(name)
                .compose(this.<BaseBean>io_main())
                .compose(handlePostResult())
                .subscribeWith(new NullPostSubscriber<BaseBean>(mPresent) {
                    @Override
                    public void onNext(BaseBean bean) {
                        mPresent.postSuccess();
                    }
                });
        addSubscription(mDisposable);
    }
}


