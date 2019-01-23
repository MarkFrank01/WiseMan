package com.zxcx.zhizhe.ui.circle.classify

import com.zxcx.zhizhe.mvpBase.GetPostView
import com.zxcx.zhizhe.mvpBase.IGetPostPresenter
import com.zxcx.zhizhe.ui.circle.circlehome.CircleBean

/**
 * @author : MarkFrank01
 * @Created on 2019/1/23
 * @Description :
 */
class CircleClassifyContract{
    interface View:GetPostView<MutableList<CircleBean>,CircleBean>{

    }

    interface Presenter : IGetPostPresenter<MutableList<CircleBean>,CircleBean>{

    }
}