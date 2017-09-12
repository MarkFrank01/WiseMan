package com.zxcx.shitang.ui.search.result;

import com.zxcx.shitang.mvpBase.IGetPresenter;
import com.zxcx.shitang.mvpBase.MvpView;

import java.util.List;

public interface SearchResultContract {

    interface View extends MvpView<List<SearchCardBean>> {
        void searchCardBagSuccess(List<SearchCardBagBean> list);
    }

    interface Presenter extends IGetPresenter<List<SearchCardBean>> {
        void searchCardBagSuccess(List<SearchCardBagBean> list);
    }
}

