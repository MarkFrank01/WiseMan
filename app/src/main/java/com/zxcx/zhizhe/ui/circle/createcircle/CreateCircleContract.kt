package com.zxcx.zhizhe.ui.circle.createcircle

import com.zxcx.zhizhe.mvpBase.GetPostView
import com.zxcx.zhizhe.mvpBase.IGetPostPresenter
import com.zxcx.zhizhe.ui.circle.circlehome.CircleBean

/**
 * @author : MarkFrank01
 * @Created on 2019/1/26
 * @Description :
 */
class CreateCircleContract{

    interface View:GetPostView<CircleBean,CircleBean>

    interface Presenter:IGetPostPresenter<CircleBean,CircleBean>
}