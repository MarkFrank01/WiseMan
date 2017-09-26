package com.zxcx.shitang.ui.my.selectAttention;

import com.zxcx.shitang.mvpBase.INullGetPostPresenter;
import com.zxcx.shitang.mvpBase.NullGetPostView;

import java.util.List;

public interface SelectAttentionContract {

    interface View extends NullGetPostView<List<SelectAttentionBean>> {

    }

    interface Presenter extends INullGetPostPresenter<List<SelectAttentionBean>> {

    }
}

