package com.zxcx.zhizhe.ui.search.result;

import com.zxcx.zhizhe.mvpBase.IGetPresenter;
import com.zxcx.zhizhe.mvpBase.MvpView;

import java.util.List;

public interface SearchResultContract {

    interface View extends MvpView<List<SearchCardBean>> {
        void searchCardBagSuccess(List<SearchCardBagBean> list);
    }

    interface Presenter extends IGetPresenter<List<SearchCardBean>> {
        void searchCardBagSuccess(List<SearchCardBagBean> list);
    }
}

