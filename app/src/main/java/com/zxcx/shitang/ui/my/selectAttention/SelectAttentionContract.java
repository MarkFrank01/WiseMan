package com.zxcx.shitang.ui.my.selectAttention;

import com.zxcx.shitang.mvpBase.GetPostView;
import com.zxcx.shitang.mvpBase.IGetPostPresenter;
import com.zxcx.shitang.mvpBase.PostBean;

import java.util.List;

public interface SelectAttentionContract {

    interface View extends GetPostView<List<SelectAttentionBean>, PostBean> {

    }

    interface Presenter extends IGetPostPresenter<List<SelectAttentionBean>,PostBean> {

    }
}

