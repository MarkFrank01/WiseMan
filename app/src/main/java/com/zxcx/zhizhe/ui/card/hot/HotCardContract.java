package com.zxcx.zhizhe.ui.card.hot;

import com.zxcx.zhizhe.mvpBase.GetView;
import com.zxcx.zhizhe.mvpBase.IGetPresenter;

import java.util.List;

public interface HotCardContract {

    interface View extends GetView<List<CardBean>> {
    }

    interface Presenter extends IGetPresenter<List<CardBean>> {
    }
}

