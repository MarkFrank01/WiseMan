package com.zxcx.shitang.ui.my.collect.collectFolder;

import com.zxcx.shitang.mvpBase.GetPostView;
import com.zxcx.shitang.mvpBase.IGetPostPresenter;
import com.zxcx.shitang.mvpBase.PostBean;

import java.util.List;

public interface CollectFolderContract {

    interface View extends GetPostView<List<CollectFolderBean>,PostBean> {

    }

    interface Presenter extends IGetPostPresenter<List<CollectFolderBean>,PostBean> {

    }
}

