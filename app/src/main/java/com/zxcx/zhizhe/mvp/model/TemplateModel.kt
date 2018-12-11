package com.zxcx.zhizhe.mvp.model

import com.zxcx.zhizhe.mvp.contract.TemplateContract
import com.zxcx.zhizhe.mvpBase.BaseModel

/**
 * @author : MarkFrank01
 * @Created on 2018/12/6
 * @Description :
 */
class TemplateModel(presenter: TemplateContract.Presenter) : BaseModel<TemplateContract.Presenter>() {

    init {
        this.mPresenter = presenter
    }

}