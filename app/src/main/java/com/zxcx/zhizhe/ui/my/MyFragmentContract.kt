package com.zxcx.zhizhe.ui.my

import com.zxcx.zhizhe.mvpBase.GetView
import com.zxcx.zhizhe.mvpBase.IGetPresenter
import com.zxcx.zhizhe.ui.welcome.ADBean

/**
 * @author : MarkFrank01
 * @Description :
 */
interface MyFragmentContract {

    interface View : GetView<MyTabBean> {
        fun getADSuccess(list: MutableList<ADBean>)
    }

    interface Presenter : IGetPresenter<MutableList<MyTabBean>> {
        fun getADSuccess(list: MutableList<ADBean>)
    }
}