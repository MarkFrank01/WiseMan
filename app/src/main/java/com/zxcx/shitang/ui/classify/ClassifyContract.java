package com.zxcx.shitang.ui.classify;

import com.zxcx.shitang.mvpBase.MvpView;
import com.zxcx.shitang.mvpBase.IBasePresenter;

import java.util.List;

public interface ClassifyContract {

    interface View extends MvpView<List<ClassifyBean>> {

    }

    interface Presenter extends IBasePresenter<List<ClassifyBean>> {

    }
}

