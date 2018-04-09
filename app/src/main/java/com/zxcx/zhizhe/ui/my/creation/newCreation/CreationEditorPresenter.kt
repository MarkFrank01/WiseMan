package com.zxcx.zhizhe.ui.my.creation.newCreation

import com.zxcx.zhizhe.mvpBase.BasePresenter

class CreationEditorPresenter(view: CreationEditorContract.View) : BasePresenter<CreationEditorContract.View>(), CreationEditorContract.Presenter {

    private val mModel: CreationEditorModel

    init {
        attachView(view)
        mModel = CreationEditorModel(this)
    }

    fun saveDraft(cardId: Int, title: String, imageUrl: String, cardBagId: Int, content: String) {
        mModel.saveDraft(cardId,title,imageUrl,cardBagId,content)
    }

    fun submitReview(cardId: Int, title: String,imageUrl: String,cardBagId: Int,content: String) {
        mModel.submitReview(cardId,title,imageUrl,cardBagId,content)
    }

    override fun postSuccess() {
        mView.postSuccess()
    }

    override fun saveDraftSuccess() {
        mView.saveDraftSuccess()
    }

    override fun postFail(msg: String) {
        mView.postFail(msg)
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

