package com.zxcx.zhizhe.ui.circle.circlesearch.inside.user

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.chad.library.adapter.base.BaseQuickAdapter
import com.zxcx.zhizhe.R
import com.zxcx.zhizhe.mvpBase.MvpFragment
import com.zxcx.zhizhe.ui.my.followUser.UnFollowConfirmDialog
import com.zxcx.zhizhe.ui.search.result.user.SearchUserBean
import com.zxcx.zhizhe.utils.Constants

/**
 * @author : MarkFrank01
 * @Created on 2019/3/20
 * @Description :
 */
class SearchInsideUserFragment : MvpFragment<SearchInsideUserPresenter>(), SearchInsideUserContract.View,
        BaseQuickAdapter.RequestLoadMoreListener, BaseQuickAdapter.OnItemChildClickListener, BaseQuickAdapter.OnItemClickListener {

    //circleId
    var circleId = 0

    private var mPage = 0
    private lateinit var mAdapter:SearchInsideUserAdapter
    private lateinit var mDialog: UnFollowConfirmDialog

    var mKeyword = ""
        set(value) {
            field = value
            mPage = 0
            mPresenter?.searchUser(mPage,Constants.PAGE_SIZE,circleId,mKeyword)
        }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_search_result, container, false)
    }



    override fun createPresenter(): SearchInsideUserPresenter {
        return SearchInsideUserPresenter(this)
    }

    override fun unFollowUserSuccess(bean: SearchUserBean) {
    }

    override fun postSuccess(bean: SearchUserBean?) {
    }

    override fun postFail(msg: String?) {
    }

    override fun getDataSuccess(bean: List<SearchUserBean>?) {
    }

    override fun onLoadMoreRequested() {
    }

    override fun onItemChildClick(adapter: BaseQuickAdapter<*, *>?, view: View?, position: Int) {
    }

    override fun onItemClick(adapter: BaseQuickAdapter<*, *>?, view: View?, position: Int) {
    }
}