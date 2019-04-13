package com.zxcx.zhizhe.ui.circle.circleowner.ownermanage

import com.zxcx.zhizhe.mvpBase.GetView
import com.zxcx.zhizhe.mvpBase.IGetPresenter
import com.zxcx.zhizhe.ui.card.hot.CardBean

/**
 * @author : MarkFrank01
 * @Created on 2019/3/11
 * @Description :
 */
class OwnerManageContentContract {
    interface View:GetView<MutableList<CardBean>>{
        fun setArticleFixTopSuccess(hint: String)
        fun removeArticleSuccess()
    }

    interface Presenter: IGetPresenter<MutableList<CardBean>>{
        fun setArticleFixTopSuccess(hint:String)
        fun removeArticleSuccess()
    }
}
