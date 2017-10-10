package com.zxcx.zhizhe.ui.search.search;

import com.zxcx.zhizhe.mvpBase.IGetPresenter;
import com.zxcx.zhizhe.mvpBase.MvpView;

import java.util.List;

public interface SearchContract {

    interface View extends MvpView<List<SearchBean>> {

    }

    interface Presenter extends IGetPresenter<List<SearchBean>> {

    }
}

