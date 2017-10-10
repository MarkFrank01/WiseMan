package com.zxcx.zhizhe.ui.my.selectAttention;

import android.support.annotation.NonNull;

import com.zxcx.zhizhe.mvpBase.BaseModel;
import com.zxcx.zhizhe.retrofit.AppClient;
import com.zxcx.zhizhe.retrofit.BaseArrayBean;
import com.zxcx.zhizhe.retrofit.BaseBean;
import com.zxcx.zhizhe.retrofit.BaseSubscriber;
import com.zxcx.zhizhe.retrofit.NullPostSubscriber;

import java.util.List;

public class SelectAttentionModel extends BaseModel<SelectAttentionContract.Presenter> {

    public SelectAttentionModel(@NonNull SelectAttentionContract.Presenter present) {
        this.mPresent = present;
    }

    public void getAttentionList(){
        mDisposable = AppClient.getAPIService().getAttentionList()
                .compose(this.<BaseArrayBean<SelectAttentionBean>>io_main())
                .compose(this.<SelectAttentionBean>handleArrayResult())
                .subscribeWith(new BaseSubscriber<List<SelectAttentionBean>>(mPresent) {
                    @Override
                    public void onNext(List<SelectAttentionBean> list) {
                        mPresent.getDataSuccess(list);
                    }
                });
        addSubscription(mDisposable);
    }

    public void changeAttentionList(List<Integer> idList){
        mDisposable = AppClient.getAPIService().changeAttentionList(idList)
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


