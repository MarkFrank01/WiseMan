package com.zxcx.zhizhe.ui.my.selectAttention;

import com.zxcx.zhizhe.mvpBase.INullGetPostPresenter;
import com.zxcx.zhizhe.mvpBase.NullGetPostView;

import java.util.List;

public interface SelectAttentionContract {

    interface View extends NullGetPostView<List<SelectAttentionBean>> {

    }

    interface Presenter extends INullGetPostPresenter<List<SelectAttentionBean>> {

    }
}

