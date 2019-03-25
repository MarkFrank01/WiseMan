package com.zxcx.zhizhe.pay.wx

import com.zxcx.zhizhe.mvpBase.GetView
import com.zxcx.zhizhe.mvpBase.IGetPresenter

/**
 * @author : MarkFrank01
 * @Created on 2019/2/23
 * @Description :
 */
interface WXEntryContract{

    interface View: GetView<WXBean>{
        fun getWxOrderPaySuccess(bean: WXBean)
    }

    interface Presenter: IGetPresenter<WXBean>{
        fun getWxOrderPaySuccess(bean: WXBean)
    }
}