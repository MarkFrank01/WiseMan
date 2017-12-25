package com.zxcx.zhizhe.ui.my.creation.newCreation

import com.zxcx.zhizhe.mvpBase.BasePresenter

class NewCreationEditorPresenter(view: NewCreationEditorContract.View) : BasePresenter<NewCreationEditorContract.View>(), NewCreationEditorContract.Presenter {

    private val mModel: NewCreationEditorModel

    init {
        attachView(view)
        mModel = NewCreationEditorModel(this)
    }

    fun saveFreeNode(cardId: Int?, title: String?, imageUrl: String?, cardBagId: Int?, content: String?) {
        mModel.saveFreeNode(cardId,title,imageUrl,cardBagId,content)
    }

    fun submitReview(cardId: Int?, title: String?,imageUrl: String?,cardBagId: Int?,content: String?) {
        mModel.submitReview(cardId,title,imageUrl,cardBagId,content)
    }

    override fun postSuccess() {
        mView.postSuccess()
    }

    override fun saveFreedomNoteSuccess() {
        mView.saveFreedomNoteSuccess()
    }

    override fun postFail(msg: String?) {
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

