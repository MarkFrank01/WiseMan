package com.zxcx.zhizhe.ui.my.selectAttention.now

import com.zxcx.zhizhe.mvpBase.INullGetPostPresenter
import com.zxcx.zhizhe.mvpBase.NullGetPostView
import com.zxcx.zhizhe.ui.my.selectAttention.ClassifyBean

/**
 * @author : MarkFrank01
 * @Created on 2019/3/28
 * @Description :
 */
class NowSelectContract {
    interface View: NullGetPostView<MutableList<ClassifyBean>>{
        fun followClassifySuccess()
    }

    interface Presenter:INullGetPostPresenter<MutableList<ClassifyBean>>{
        fun followClassifySuccess()
    }
}