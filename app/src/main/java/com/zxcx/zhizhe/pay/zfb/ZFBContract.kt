package com.zxcx.zhizhe.pay.zfb

import com.zxcx.zhizhe.mvpBase.IPostPresenter
import com.zxcx.zhizhe.mvpBase.PostView

/**
 * @author : MarkFrank01
 * @Created on 2019/4/1
 * @Description :
 */
interface ZFBContract {

    interface View : PostView<ZFBBean> {
        fun getAlOrderPayForJoinCircle(key:String)
    }

    interface Presenter : IPostPresenter<String> {
        fun getAlOrderPayForJoinCircle(key:String)
    }
}