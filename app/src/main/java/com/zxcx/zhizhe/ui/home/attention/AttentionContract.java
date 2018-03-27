package com.zxcx.zhizhe.ui.home.attention;

import com.zxcx.zhizhe.mvpBase.GetView;
import com.zxcx.zhizhe.mvpBase.IGetPresenter;
import com.zxcx.zhizhe.ui.home.hot.HotBean;

import java.util.List;

public interface AttentionContract {

    interface View extends GetView<List<HotBean>> {
    }

    interface Presenter extends IGetPresenter<List<HotBean>> {
    }
}

