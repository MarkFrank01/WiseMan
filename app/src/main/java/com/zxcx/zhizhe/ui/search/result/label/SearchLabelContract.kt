package com.zxcx.zhizhe.ui.search.result.label

import com.zxcx.zhizhe.mvpBase.GetPostView
import com.zxcx.zhizhe.mvpBase.IGetPostPresenter

/**
 * @author : MarkFrank01
 * @Created on 2019/1/15
 * @Description :
 */
class SearchLabelContract {

    interface View : GetPostView<List<SearchLabelBean>,SearchLabelBean> {

    }

    interface Presenter : IGetPostPresenter<List<SearchLabelBean>, SearchLabelBean> {

    }
}