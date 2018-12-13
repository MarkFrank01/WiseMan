package com.zxcx.zhizhe.ui.my.daily

import com.zxcx.zhizhe.mvpBase.GetView
import com.zxcx.zhizhe.mvpBase.IGetPresenter

/**
 * @author : MarkFrank01
 * @Created on 2018/12/13
 * @Description :
 */
class DailyContract {

    interface View : GetView<MutableList<DailyBean>> {

    }

    interface Presenter : IGetPresenter<MutableList<DailyBean>> {

    }
}