package com.zxcx.shitang.ui.my.collect.collectFolder;

import android.support.annotation.NonNull;

import com.zxcx.shitang.mvpBase.BaseModel;
import com.zxcx.shitang.mvpBase.PostBean;
import com.zxcx.shitang.retrofit.AppClient;
import com.zxcx.shitang.retrofit.BaseArrayBean;
import com.zxcx.shitang.retrofit.BaseBean;
import com.zxcx.shitang.retrofit.BaseSubscriber;
import com.zxcx.shitang.retrofit.PostSubscriber;

import java.util.List;

public class CollectFolderModel extends BaseModel<CollectFolderContract.Presenter> {
    public CollectFolderModel(@NonNull CollectFolderContract.Presenter present) {
        this.mPresent = present;
    }

    public void getCollectFolder(int page, int pageSize){
        subscription = AppClient.getAPIService().getCollectFolder(page,pageSize)
                .compose(this.<BaseArrayBean<CollectFolderBean>>io_main())
                .compose(this.<CollectFolderBean>handleArrayResult())
                .subscribeWith(new BaseSubscriber<List<CollectFolderBean>>(mPresent) {
                    @Override
                    public void onNext(List<CollectFolderBean> list) {
                        mPresent.getDataSuccess(list);
                    }
                });
        addSubscription(subscription);
    }

    public void deleteCollectFolder(List<Integer> idList){
        subscription = AppClient.getAPIService().deleteCollectFolder(idList)
                .compose(this.<BaseBean<PostBean>>io_main())
                .compose(this.<PostBean>handleResult())
                .subscribeWith(new PostSubscriber<PostBean>(mPresent) {
                    @Override
                    public void onNext(PostBean bean) {
                        mPresent.postSuccess(bean);
                    }
                });
        addSubscription(subscription);
    }

    public void addCollectFolder(String name){
        subscription = AppClient.getAPIService().addCollectFolder(name)
                .compose(this.<BaseBean>io_main())
                .compose(handlePostResult())
                .subscribeWith(new PostSubscriber<BaseBean>(mPresent) {
                    @Override
                    public void onNext(BaseBean bean) {
                        mPresent.postSuccess(new PostBean());
                    }
                });
        addSubscription(subscription);
    }
}


