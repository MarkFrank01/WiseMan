package com.zxcx.zhizhe.ui.circle.circlemessage.zan

import com.zxcx.zhizhe.mvpBase.GetView
import com.zxcx.zhizhe.mvpBase.IGetPresenter
import com.zxcx.zhizhe.ui.circle.circlemessage.MyCircleTabBean

/**
 * @author : MarkFrank01
 * @Created on 2019/3/20
 * @Description :
 */
class CircleMesZanContract {
    interface View:GetView<MyCircleTabBean>{
        fun getLikeMessageListSuccess(bean:MutableList<MyCircleTabBean>)
    }

    interface Presenter:IGetPresenter<MyCircleTabBean>{
        fun getLikeMessageListSuccess(bean:MutableList<MyCircleTabBean>)
    }
}