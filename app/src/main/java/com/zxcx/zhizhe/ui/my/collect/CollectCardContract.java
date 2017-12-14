package com.zxcx.zhizhe.ui.my.collect;

import com.zxcx.zhizhe.mvpBase.GetView;
import com.zxcx.zhizhe.mvpBase.IGetPresenter;

import java.util.List;

public interface CollectCardContract {

    interface View extends GetView<List<CollectCardBean>> {

    }

    interface Presenter extends IGetPresenter<List<CollectCardBean>> {

    }
}

