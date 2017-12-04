package com.zxcx.zhizhe.ui.home.hot;

import com.zxcx.zhizhe.mvpBase.IGetPresenter;
import com.zxcx.zhizhe.mvpBase.GetView;

import java.util.List;

public interface HotContract {

    interface View extends GetView<List<RecommendBean>> {
    }

    interface Presenter extends IGetPresenter<List<RecommendBean>> {
    }
}

