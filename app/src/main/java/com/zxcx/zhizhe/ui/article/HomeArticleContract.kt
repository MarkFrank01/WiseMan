package com.zxcx.zhizhe.ui.article

import com.zxcx.zhizhe.mvpBase.GetView
import com.zxcx.zhizhe.mvpBase.IGetPresenter
import com.zxcx.zhizhe.ui.card.cardList.CardCategoryBean
import com.zxcx.zhizhe.ui.welcome.ADBean

/**
 * @author : MarkFrank01
 * @Description :
 */
interface HomeArticleContract{

    interface View : GetView<MutableList<CardCategoryBean>> {
        fun getADSuccess(list: MutableList<ADBean>)
    }

    interface Presenter : IGetPresenter<MutableList<CardCategoryBean>> {
        fun getADSuccess(list: MutableList<ADBean>)
    }
}