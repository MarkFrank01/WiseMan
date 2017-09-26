package com.zxcx.shitang.ui.my.collect.collectFolder;

import com.zxcx.shitang.mvpBase.INullGetPostPresenter;
import com.zxcx.shitang.mvpBase.NullGetPostView;

import java.util.List;

public interface CollectFolderContract {

    interface View extends NullGetPostView<List<CollectFolderBean>> {

    }

    interface Presenter extends INullGetPostPresenter<List<CollectFolderBean>> {

    }
}

