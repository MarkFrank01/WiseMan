package com.zxcx.shitang.ui.my.collect.collectCard;

import android.support.annotation.NonNull;

import com.zxcx.shitang.mvpBase.BaseModel;
import com.zxcx.shitang.mvpBase.PostBean;
import com.zxcx.shitang.retrofit.AppClient;
import com.zxcx.shitang.retrofit.BaseArrayBean;
import com.zxcx.shitang.retrofit.BaseBean;
import com.zxcx.shitang.retrofit.BaseSubscriber;
import com.zxcx.shitang.retrofit.PostSubscriber;

import java.util.List;

public class CollectCardModel extends BaseModel<CollectCardContract.Presenter> {
    public CollectCardModel(@NonNull CollectCardContract.Presenter present) {
        this.mPresent = present;
    }

    public void getCollectCard(int id, int page, int pageSize){
        subscription = AppClient.getAPIService().getCollectCard(id, page,pageSize)
                .compose(this.<BaseArrayBean<CollectCardBean>>io_main())
                .compose(this.<CollectCardBean>handleArrayResult())
                .subscribeWith(new BaseSubscriber<List<CollectCardBean>>(mPresent) {
                    @Override
                    public void onNext(List<CollectCardBean> list) {
                        mPresent.getDataSuccess(list);
                    }
                });
        addSubscription(subscription);
    }

    public void deleteCollectCard(int id, List<Integer> idList){
        subscription = AppClient.getAPIService().deleteCollectCard(id, idList)
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

    public void changeCollectFolderName(int id, String name){
        subscription = AppClient.getAPIService().changeCollectFolderName(id, name)
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


