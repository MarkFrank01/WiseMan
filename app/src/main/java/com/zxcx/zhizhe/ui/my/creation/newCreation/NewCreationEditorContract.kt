package com.zxcx.zhizhe.ui.my.creation.newCreation

import com.zxcx.zhizhe.mvpBase.GetView
import com.zxcx.zhizhe.mvpBase.IGetPresenter
import com.zxcx.zhizhe.ui.my.creation.newCreation.NewCreationEditorBean

interface NewCreationEditorContract {

    interface View : GetView<NewCreationEditorBean>

    interface Presenter : IGetPresenter<NewCreationEditorBean>
}

