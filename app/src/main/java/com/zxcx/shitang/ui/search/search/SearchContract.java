package com.zxcx.shitang.ui.search.search;

import com.zxcx.shitang.mvpBase.IBasePresenter;
import com.zxcx.shitang.mvpBase.MvpView;

public interface SearchContract {

    interface View extends MvpView<SearchBean> {

    }

    interface Presenter extends IBasePresenter<SearchBean> {

    }
}

