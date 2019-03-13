package com.zxcx.zhizhe.ui.circle.circlequestiondetail

import com.zxcx.zhizhe.mvpBase.GetView
import com.zxcx.zhizhe.mvpBase.IGetPresenter
import com.zxcx.zhizhe.ui.circle.circledetaile.CircleDetailBean

/**
 * @author : MarkFrank01
 * @Created on 2019/3/13
 * @Description :
 */
class CircleQuestionDetailContract {
    interface View:GetView<CircleDetailBean>{
        fun getBasicQuestionSuccess(bean:CircleDetailBean)
    }

    interface Presenter:IGetPresenter<CircleDetailBean>{
        fun getBasicQuestionSuccess(bean:CircleDetailBean)
    }
}