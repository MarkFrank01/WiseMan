package com.zxcx.zhizhe.ui.topchange

import com.zxcx.zhizhe.mvpBase.INullGetPostPresenter
import com.zxcx.zhizhe.mvpBase.NullGetPostView
import com.zxcx.zhizhe.ui.my.selectAttention.ClassifyBean

/**
 * @author : MarkFrank01
 * @Created on 2019/3/29
 * @Description :
 */
class TopChangeContract {
    interface View : NullGetPostView<MutableList<ClassifyBean>> {
        fun setClassifyMenuSuccess()
        fun getAllNavClassifySuccess(list:MutableList<ClassifyBean>)
    }


    interface Presenter : INullGetPostPresenter<MutableList<ClassifyBean>> {
        fun setClassifyMenuSuccess()
        fun getAllNavClassifySuccess(list:MutableList<ClassifyBean>)
    }
}