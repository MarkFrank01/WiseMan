package com.zxcx.zhizhe.ui.card.cardList

import com.zxcx.zhizhe.mvpBase.BasePresenter
import com.zxcx.zhizhe.ui.card.hot.CardBean
import com.zxcx.zhizhe.ui.welcome.ADBean

/**
 * @author : MarkFrank01
 * @Description :
 */
class CardListitemPresenter(view:CardListitemContract.View):BasePresenter<CardListitemContract.View>(),CardListitemContract.Presenter{

    private val mModel:CardListitemModel

    init {
        attachView(view)
        mModel = CardListitemModel(this)
    }

    fun getAD(position:Int){
        mModel.getAD(position)
    }

    override fun getADSuccess(list: MutableList<ADBean>) {
        if (list.isNotEmpty()) {
            mView.getADSuccess(list)
        }else{
            if (mView!=null) {
                mView.closeAD()
            }
        }
    }

    override fun showLoading() {
        mView.showLoading()
    }

    override fun hideLoading() {
        mView.hideLoading()
    }

    override fun getDataSuccess(bean: MutableList<CardBean>?) {
    }

    override fun getDataFail(msg: String?) {
    }

    override fun startLogin() {
    }

}