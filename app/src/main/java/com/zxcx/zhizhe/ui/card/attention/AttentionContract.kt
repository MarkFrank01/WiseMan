package com.zxcx.zhizhe.ui.card.attention

import com.zxcx.zhizhe.mvpBase.INullGetPostPresenter
import com.zxcx.zhizhe.mvpBase.NullGetPostView
import com.zxcx.zhizhe.ui.card.hot.CardBean
import com.zxcx.zhizhe.ui.classify.ClassifyBean

interface AttentionContract {

    interface View : NullGetPostView<List<CardBean>>{
        fun getClassifySuccess(list: List<ClassifyBean>)
    }

    interface Presenter : INullGetPostPresenter<List<CardBean>>{
        fun getClassifySuccess(list: List<ClassifyBean>)
    }
}

