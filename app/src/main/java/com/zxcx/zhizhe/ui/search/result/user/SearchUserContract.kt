package com.zxcx.zhizhe.ui.search.result.user

import com.zxcx.zhizhe.mvpBase.GetView
import com.zxcx.zhizhe.mvpBase.IGetPresenter

interface SearchUserContract {

	interface View : GetView<List<SearchUserBean>>

	interface Presenter : IGetPresenter<List<SearchUserBean>>
}

