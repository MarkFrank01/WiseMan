package com.zxcx.shitang.ui.my.collect.collectFolder;

import com.zxcx.shitang.mvpBase.GetPostView;
import com.zxcx.shitang.mvpBase.IGetPostPresenter;

import java.util.List;

public interface CollectFolderContract {

    interface View extends GetPostView<List<CollectFolderBean>> {

    }

    interface Presenter extends IGetPostPresenter<List<CollectFolderBean>> {

    }
}

