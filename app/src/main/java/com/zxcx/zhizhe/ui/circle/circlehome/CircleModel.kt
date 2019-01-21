package com.zxcx.zhizhe.ui.circle.circlehome

import com.zxcx.zhizhe.mvpBase.BaseModel

/**
 * @author : MarkFrank01
 * @Created on 2019/1/21
 * @Description :
 */
class CircleModel(presenter: CircleContract.Presenter):BaseModel<CircleContract.Presenter>(){

    init {
        this.mPresenter = presenter
    }


}