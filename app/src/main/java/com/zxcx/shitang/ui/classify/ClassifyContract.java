package com.zxcx.shitang.ui.classify;

import com.zxcx.shitang.mvpBase.MvpView;
import com.zxcx.shitang.mvpBase.IGetPresenter;

import java.util.List;

public interface ClassifyContract {

    interface View extends MvpView<List<ClassifyBean>> {

    }

    interface Presenter extends IGetPresenter<List<ClassifyBean>> {

    }
}

