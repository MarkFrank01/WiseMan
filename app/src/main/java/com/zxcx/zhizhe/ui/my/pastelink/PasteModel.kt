package com.zxcx.zhizhe.ui.my.pastelink

import com.zxcx.zhizhe.mvpBase.BaseModel

class PasteModel(presenter:PasteLinkContract.Presenter):BaseModel<PasteLinkContract.Presenter>(){
    init {
        this.mPresenter = presenter
    }

    //提交所有链接
    fun pushLinkList(articleLinks:Array<String>){
//        mDisposable = AppClient.getAPIService().pushArticleLink(articleLinks)
//                .compose(BaseRxJava.INSTANCE.hande)
    }
}