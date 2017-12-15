package com.zxcx.zhizhe.ui.my.creation.newCreation

import com.zxcx.zhizhe.ui.my.creation.newCreation.NewCreationEditorContract
import com.zxcx.zhizhe.ui.my.creation.newCreation.NewCreationEditorModel
import com.zxcx.zhizhe.mvpBase.BasePresenter

class NewCreationEditorPresenter(view: NewCreationEditorContract.View) : BasePresenter<NewCreationEditorContract.View>(), NewCreationEditorContract.Presenter {

    private val mModel: NewCreationEditorModel

    init {
        attachView(view)
        mModel = NewCreationEditorModel(this)
    }

    override fun getDataSuccess(bean: NewCreationEditorBean) {
        mView.getDataSuccess(bean)
    }

    override fun getDataFail(msg: String) {
        mView.toastFail(msg)
    }

    override fun showLoading() {
        mView.showLoading()
    }

    override fun hideLoading() {
        mView.hideLoading()
    }

    override fun startLogin() {
        mView.startLogin()
    }

    override fun detachView() {
        super.detachView()
        mModel.onDestroy()
    }
}

