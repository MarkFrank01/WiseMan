package com.zxcx.zhizhe.ui.circle.circlesearch

import com.zxcx.zhizhe.mvpBase.GetView
import com.zxcx.zhizhe.mvpBase.IGetPresenter

/**
 * @author : MarkFrank01
 * @Created on 2019/3/20
 * @Description :
 */
class CircleSearchContract {

    interface View : GetView<List<String>> {


    }

    interface Presenter : IGetPresenter<List<String>> {


    }
}