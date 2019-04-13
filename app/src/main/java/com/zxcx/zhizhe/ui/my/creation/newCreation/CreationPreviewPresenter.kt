package com.zxcx.zhizhe.ui.my.creation.newCreation

import com.zxcx.zhizhe.mvpBase.BasePresenter

/**
 * @author : MarkFrank01
 * @Description :
 */
class CreationPreviewPresenter(view:CreationPreviewContract.View):BasePresenter<CreationPreviewContract.View>(),CreationPreviewContract.Presenter{

    private val mModel:CreationPreviewModel

    init {
        attachView(view)
        mModel = CreationPreviewModel(this)
    }

    fun deleteNote(noteId:Int){
        mModel.deleteNote(noteId)
    }

    override fun showLoading() {
        mView.showLoading()
    }

    override fun hideLoading() {
        mView.hideLoading()
    }

    override fun postSuccess() {
    }

    override fun postFail(msg: String?) {
    }


    override fun startLogin() {
    }

}