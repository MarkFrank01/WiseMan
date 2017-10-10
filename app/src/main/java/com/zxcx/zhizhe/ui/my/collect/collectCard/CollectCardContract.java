package com.zxcx.zhizhe.ui.my.collect.collectCard;

import com.zxcx.zhizhe.mvpBase.INullGetPostPresenter;
import com.zxcx.zhizhe.mvpBase.NullGetPostView;

import java.util.List;

public interface CollectCardContract {

    interface View extends NullGetPostView<List<CollectCardBean>> {

    }

    interface Presenter extends INullGetPostPresenter<List<CollectCardBean>> {

    }
}

