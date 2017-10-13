package com.zxcx.zhizhe.ui.my.collect.collectCard;

import android.support.annotation.NonNull;

import com.zxcx.zhizhe.mvpBase.BaseModel;
import com.zxcx.zhizhe.mvpBase.BaseRxJava;
import com.zxcx.zhizhe.retrofit.AppClient;
import com.zxcx.zhizhe.retrofit.BaseArrayBean;
import com.zxcx.zhizhe.retrofit.BaseBean;
import com.zxcx.zhizhe.retrofit.BaseSubscriber;
import com.zxcx.zhizhe.retrofit.NullPostSubscriber;

import java.util.List;

public class CollectCardModel extends BaseModel<CollectCardContract.Presenter> {
    public CollectCardModel(@NonNull CollectCardContract.Presenter present) {
        this.mPresenter = present;
    }

    public void getCollectCard(int id, int page, int pageSize){
        mDisposable = AppClient.getAPIService().getCollectCard(id, page,pageSize)
                .compose(BaseRxJava.<BaseArrayBean<CollectCardBean>>io_main())
                .compose(BaseRxJava.<CollectCardBean>handleArrayResult())
                .subscribeWith(new BaseSubscriber<List<CollectCardBean>>(mPresenter) {
                    @Override
                    public void onNext(List<CollectCardBean> list) {
                        mPresenter.getDataSuccess(list);
                    }
                });
        addSubscription(mDisposable);
    }

    public void deleteCollectCard(int id, List<Integer> idList){
        mDisposable = AppClient.getAPIService().deleteCollectCard(id, idList)
                .compose(BaseRxJava.<BaseBean>io_main())
                .compose(BaseRxJava.handlePostResult())
                .subscribeWith(new NullPostSubscriber<BaseBean>(mPresenter) {
                    @Override
                    public void onNext(BaseBean bean) {
                        mPresenter.postSuccess();
                    }
                });
        addSubscription(mDisposable);
    }

    public void changeCollectFolderName(int id, String name){
        mDisposable = AppClient.getAPIService().changeCollectFolderName(id, name)
                .compose(BaseRxJava.<BaseBean>io_main())
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


