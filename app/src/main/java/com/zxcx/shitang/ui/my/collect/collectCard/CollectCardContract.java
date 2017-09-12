package com.zxcx.shitang.ui.my.collect.collectCard;

import com.zxcx.shitang.mvpBase.GetPostView;
import com.zxcx.shitang.mvpBase.IGetPostPresenter;
import com.zxcx.shitang.mvpBase.PostBean;

import java.util.List;

public interface CollectCardContract {

    interface View extends GetPostView<List<CollectCardBean>,PostBean> {

    }

    interface Presenter extends IGetPostPresenter<List<CollectCardBean>,PostBean> {

    }
}

