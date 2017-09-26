package com.zxcx.shitang.ui.my.collect.collectCard;

import com.zxcx.shitang.mvpBase.INullGetPostPresenter;
import com.zxcx.shitang.mvpBase.NullGetPostView;

import java.util.List;

public interface CollectCardContract {

    interface View extends NullGetPostView<List<CollectCardBean>> {

    }

    interface Presenter extends INullGetPostPresenter<List<CollectCardBean>> {

    }
}

