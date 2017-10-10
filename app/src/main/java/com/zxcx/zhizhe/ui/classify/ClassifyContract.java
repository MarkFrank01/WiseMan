package com.zxcx.zhizhe.ui.classify;

import com.zxcx.zhizhe.mvpBase.MvpView;
import com.zxcx.zhizhe.mvpBase.IGetPresenter;

import java.util.List;

public interface ClassifyContract {

    interface View extends MvpView<List<ClassifyBean>> {

    }

    interface Presenter extends IGetPresenter<List<ClassifyBean>> {

    }
}

