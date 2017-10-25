package com.zxcx.zhizhe.ui.classify;

import com.zxcx.zhizhe.mvpBase.GetView;
import com.zxcx.zhizhe.mvpBase.IGetPresenter;

import java.util.List;

public interface ClassifyContract {

    interface View extends GetView<List<ClassifyBean>> {

    }

    interface Presenter extends IGetPresenter<List<ClassifyBean>> {

    }
}

