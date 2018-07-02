package com.zxcx.zhizhe.ui.article.attention

import com.zxcx.zhizhe.mvpBase.INullGetPostPresenter
import com.zxcx.zhizhe.mvpBase.NullGetPostView
import com.zxcx.zhizhe.ui.article.ArticleAndSubjectBean
import com.zxcx.zhizhe.ui.classify.ClassifyBean

interface AttentionArticleContract {

	interface View : NullGetPostView<List<ArticleAndSubjectBean>> {
		fun getClassifySuccess(list: List<ClassifyBean>)
	}

	interface Presenter : INullGetPostPresenter<List<ArticleAndSubjectBean>> {
		fun getClassifySuccess(list: List<ClassifyBean>)
	}
}

