package com.zxcx.zhizhe.ui.search.result.circle

import com.zxcx.zhizhe.mvpBase.GetView
import com.zxcx.zhizhe.mvpBase.IGetPresenter
import com.zxcx.zhizhe.ui.circle.circlehome.CircleBean

/**
 * @author : MarkFrank01
 * @Created on 2019/1/15
 * @Description :
 */
class SearchCircleContract {

    interface View : GetView<MutableList<CircleBean>>

    interface Presenter : IGetPresenter<MutableList<CircleBean>>

}