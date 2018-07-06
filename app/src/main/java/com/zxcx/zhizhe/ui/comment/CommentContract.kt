package com.zxcx.zhizhe.ui.comment

import com.zxcx.zhizhe.mvpBase.INullGetPostPresenter
import com.zxcx.zhizhe.mvpBase.NullGetPostView

interface CommentContract {

	interface View : NullGetPostView<MutableList<CommentBean>> {
		fun likeSuccess()
		fun unlikeSuccess()
	}

	interface Presenter : INullGetPostPresenter<MutableList<CommentBean>> {
		fun likeSuccess()
		fun unlikeSuccess()
	}
}

