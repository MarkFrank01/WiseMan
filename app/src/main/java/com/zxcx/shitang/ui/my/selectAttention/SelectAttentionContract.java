package com.zxcx.shitang.ui.my.selectAttention;

import com.zxcx.shitang.mvpBase.GetPostView;
import com.zxcx.shitang.mvpBase.IGetPostPresenter;

import java.util.List;

public interface SelectAttentionContract {

    interface View extends GetPostView<List<SelectAttentionBean>> {

    }

    interface Presenter extends IGetPostPresenter<List<SelectAttentionBean>> {

    }
}

