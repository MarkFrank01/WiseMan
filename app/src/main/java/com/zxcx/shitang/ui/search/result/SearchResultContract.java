package com.zxcx.shitang.ui.search.result;

import com.zxcx.shitang.mvpBase.MvpView;
import com.zxcx.shitang.mvpBase.IBasePresenter;
import com.zxcx.shitang.ui.search.result.SearchResultBean;

public interface SearchResultContract {

    interface View extends MvpView<SearchResultBean> {

    }

    interface Presenter extends IBasePresenter<SearchResultBean> {

    }
}

