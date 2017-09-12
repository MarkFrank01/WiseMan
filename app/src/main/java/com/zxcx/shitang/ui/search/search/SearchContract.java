package com.zxcx.shitang.ui.search.search;

import com.zxcx.shitang.mvpBase.IGetPresenter;
import com.zxcx.shitang.mvpBase.MvpView;

import java.util.List;

public interface SearchContract {

    interface View extends MvpView<List<String>> {

    }

    interface Presenter extends IGetPresenter<List<String>> {

    }
}

