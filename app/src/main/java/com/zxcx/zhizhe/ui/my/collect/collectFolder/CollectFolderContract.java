package com.zxcx.zhizhe.ui.my.collect.collectFolder;

import com.zxcx.zhizhe.mvpBase.INullGetPostPresenter;
import com.zxcx.zhizhe.mvpBase.NullGetPostView;

import java.util.List;

public interface CollectFolderContract {

    interface View extends NullGetPostView<List<CollectFolderBean>> {

    }

    interface Presenter extends INullGetPostPresenter<List<CollectFolderBean>> {

    }
}

