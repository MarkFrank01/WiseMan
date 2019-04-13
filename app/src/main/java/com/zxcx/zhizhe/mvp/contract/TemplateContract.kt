package com.zxcx.zhizhe.mvp.contract

import com.zxcx.zhizhe.mvpBase.INullGetPostPresenter
import com.zxcx.zhizhe.mvpBase.NullGetPostView
import com.zxcx.zhizhe.ui.card.hot.CardBean

/**
 * @author : MarkFrank01
 * @Created on 2018/12/6
 * @Description :
 */
interface TemplateContract {

    interface View : NullGetPostView<List<CardBean>>

    interface Presenter : INullGetPostPresenter<List<CardBean>>

}