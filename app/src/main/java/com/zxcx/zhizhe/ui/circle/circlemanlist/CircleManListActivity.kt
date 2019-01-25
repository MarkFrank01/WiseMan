package com.zxcx.zhizhe.ui.circle.circlemanlist

import android.os.Bundle
import android.view.View
import com.chad.library.adapter.base.BaseQuickAdapter
import com.scwang.smartrefresh.layout.api.RefreshLayout
import com.zxcx.zhizhe.R
import com.zxcx.zhizhe.mvpBase.RefreshMvpActivity
import com.zxcx.zhizhe.ui.circle.circlehome.CircleBean
import com.zxcx.zhizhe.ui.search.result.user.SearchUserBean

/**
 * @author : MarkFrank01
 * @Created on 2019/1/25
 * @Description :
 */
class CircleManListActivity:RefreshMvpActivity<CircleManListPresenter>(),CircleManListContract.View, BaseQuickAdapter.OnItemClickListener,
        BaseQuickAdapter.OnItemChildClickListener, BaseQuickAdapter.RequestLoadMoreListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.layout_circle_man_list)
    }

    override fun onRefresh(refreshLayout: RefreshLayout?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun createPresenter(): CircleManListPresenter {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getCircleMemberByCircleIdSuccess(bean: MutableList<CircleBean>) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun mFollowUserSuccess(bean: SearchUserBean) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun unFollowUserSuccess(bean: SearchUserBean) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun postSuccess(bean: CircleBean?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getDataFail(msg: String?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun postFail(msg: String?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getDataSuccess(bean: MutableList<CircleBean>?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onItemClick(adapter: BaseQuickAdapter<*, *>?, view: View?, position: Int) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onItemChildClick(adapter: BaseQuickAdapter<*, *>?, view: View?, position: Int) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onLoadMoreRequested() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }


}