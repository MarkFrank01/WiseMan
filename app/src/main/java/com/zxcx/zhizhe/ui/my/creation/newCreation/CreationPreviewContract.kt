package com.zxcx.zhizhe.ui.my.creation.newCreation

import com.zxcx.zhizhe.mvpBase.GetView
import com.zxcx.zhizhe.mvpBase.INullPostPresenter

/**
 * @author : MarkFrank01
 * @Description :
 */
interface CreationPreviewContract{
    interface View : GetView<List<String>>

    interface Presenter :  INullPostPresenter {

    }
}