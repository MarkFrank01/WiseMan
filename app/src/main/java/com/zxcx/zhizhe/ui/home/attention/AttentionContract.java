package com.zxcx.zhizhe.ui.home.attention;

import com.zxcx.zhizhe.mvpBase.GetView;
import com.zxcx.zhizhe.mvpBase.IGetPresenter;
import com.zxcx.zhizhe.ui.home.hot.HotCardBean;

import java.util.List;

public interface AttentionContract {

    interface View extends GetView<List<HotCardBean>> {
    }

    interface Presenter extends IGetPresenter<List<HotCardBean>> {
    }
}

