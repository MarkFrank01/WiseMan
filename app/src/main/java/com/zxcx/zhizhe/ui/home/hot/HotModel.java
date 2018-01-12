package com.zxcx.zhizhe.ui.home.hot;

import android.support.annotation.NonNull;

import com.zxcx.zhizhe.mvpBase.BaseModel;
import com.zxcx.zhizhe.mvpBase.BaseRxJava;
import com.zxcx.zhizhe.retrofit.AppClient;
import com.zxcx.zhizhe.retrofit.BaseSubscriber;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class HotModel extends BaseModel<HotContract.Presenter> {

    public HotModel(@NonNull HotContract.Presenter present) {
        this.mPresenter = present;
    }

    public void getHotCard(int page){
        mDisposable = AppClient.getAPIService().getHot(page)
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.computation())
                .compose(BaseRxJava.INSTANCE.handleResult())
                .map(bean -> {
                    //组装数据
                    List<RecommendBean> recommendBeanList = new ArrayList<>();
                    for (HotCardBean cardBean : bean.getCardList()){
                        RecommendBean recommendBean = new RecommendBean(false,cardBean);
                        recommendBeanList.add(recommendBean);
                    }
                    if (bean.getCardBagList() == null || bean.getCardBagList().size() == 0){
                        return recommendBeanList;
                    }else if (bean.getCardBagList().size() > 5){
                        //把卡包列表分两段插入到卡片列表中
                        List<HotCardBagBean> list = bean.getCardBagList().subList(0,5);
                        List<HotCardBagBean> list1 = bean.getCardBagList().subList(5,bean.getCardBagList().size());
                        RecommendBean recommendBean = new RecommendBean(true,list);
                        RecommendBean recommendBean1 = new RecommendBean(true,list1);
                        recommendBeanList.add(2,recommendBean);
                        if (recommendBeanList.size()>=5)
                            recommendBeanList.add(5,recommendBean1);
                        else
                            recommendBeanList.add(recommendBean1);
                    }else {
                        List<HotCardBagBean> list = bean.getCardBagList().subList(0,bean.getCardBagList().size());
                        RecommendBean recommendBean = new RecommendBean(true,list);
                        recommendBeanList.add(2,recommendBean);
                    }
                    return recommendBeanList;
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new BaseSubscriber<List<RecommendBean>>(mPresenter) {
                    @Override
                    public void onNext(List<RecommendBean> list) {
                        mPresenter.getDataSuccess(list);
                    }
                });
        addSubscription(mDisposable);
    }
}


