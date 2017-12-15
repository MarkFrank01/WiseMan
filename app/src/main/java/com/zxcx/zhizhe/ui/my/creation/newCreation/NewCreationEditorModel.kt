package com.zxcx.zhizhe.ui.my.creation.newCreation

import com.zxcx.zhizhe.mvpBase.BaseModel

class NewCreationEditorModel(presenter: NewCreationEditorContract.Presenter) : BaseModel<NewCreationEditorContract.Presenter>() {
    init {
        this.mPresenter = presenter
    }
}


