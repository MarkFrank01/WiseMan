package com.zxcx.zhizhe.ui.article

import com.zxcx.zhizhe.mvpBase.GetView
import com.zxcx.zhizhe.mvpBase.IGetPresenter
import com.zxcx.zhizhe.ui.welcome.ADBean

/**
 * @author : MarkFrank01
 * @Description :
 */
class ArticleListItemContract{
    interface View : GetView<MutableList<ArticleAndSubjectBean>> {
        fun getADSuccess(list: MutableList<ADBean>)
        fun closeAD()
    }

    interface Presenter : IGetPresenter<MutableList<ArticleAndSubjectBean>> {
        fun getADSuccess(list: MutableList<ADBean>)
    }
}