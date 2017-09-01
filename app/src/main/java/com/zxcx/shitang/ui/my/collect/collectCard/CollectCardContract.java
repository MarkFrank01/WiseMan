package com.zxcx.shitang.ui.my.collect.collectCard;

import com.zxcx.shitang.mvpBase.GetPostView;
import com.zxcx.shitang.mvpBase.IGetPostPresenter;

import java.util.List;

public interface CollectCardContract {

    interface View extends GetPostView<List<CollectCardBean>> {

    }

    interface Presenter extends IGetPostPresenter<List<CollectCardBean>> {

    }
}

