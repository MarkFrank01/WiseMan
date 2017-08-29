package com.zxcx.shitang.ui.search.search;

import com.zxcx.shitang.mvpBase.IBasePresenter;
import com.zxcx.shitang.mvpBase.MvpView;

import java.util.List;

public interface SearchContract {

    interface View extends MvpView<List<String>> {

    }

    interface Presenter extends IBasePresenter<List<String>> {

    }
}

