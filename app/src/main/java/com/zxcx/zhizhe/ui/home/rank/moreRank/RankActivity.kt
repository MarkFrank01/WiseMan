package com.zxcx.zhizhe.ui.home.rank.moreRank

import `in`.srain.cube.views.ptr.PtrFrameLayout
import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import com.chad.library.adapter.base.BaseQuickAdapter
import com.kingja.loadsir.core.LoadSir
import com.zxcx.zhizhe.R
import com.zxcx.zhizhe.loadCallback.NetworkErrorCallback
import com.zxcx.zhizhe.mvpBase.RefreshMvpActivity
import com.zxcx.zhizhe.ui.home.rank.RankAdapter
import com.zxcx.zhizhe.ui.home.rank.UserRankBean
import com.zxcx.zhizhe.ui.otherUser.OtherUserActivity
import com.zxcx.zhizhe.utils.Constants
import com.zxcx.zhizhe.widget.CustomLoadMoreView
import kotlinx.android.synthetic.main.activity_rank.*

class RankActivity : RefreshMvpActivity<RankPresenter>(), RankContract.View, BaseQuickAdapter.OnItemClickListener,
        BaseQuickAdapter.RequestLoadMoreListener{

    private lateinit var mRankAdapter : RankAdapter
    private var page: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_rank)
        initToolBar("所有榜单")
        initRecyclerView()
        loadService = LoadSir.getDefault().register(this, this)
        onRefresh()
    }

    override fun createPresenter(): RankPresenter {
        return RankPresenter(this)
    }

    override fun onRefreshBegin(frame: PtrFrameLayout?) {
        onRefresh()
    }

    override fun onReload(v: View?) {
        onRefresh()
    }

    private fun onRefresh(){
        page = 0
        mPresenter.getTopHundredRank(page,Constants.PAGE_SIZE)
    }

    override fun onLoadMoreRequested() {
        mPresenter.getTopHundredRank(page,Constants.PAGE_SIZE)
    }

    override fun getDataSuccess(list: List<UserRankBean>) {
        loadService.showSuccess()
        mRefreshLayout.refreshComplete()
        if (page == 0) {
            mRankAdapter.setNewData(list)
        } else {
            mRankAdapter.addData(list)
        }
        page++
        if (list.size < Constants.PAGE_SIZE) {
            mRankAdapter.loadMoreEnd(false)
        } else {
            mRankAdapter.loadMoreComplete()
            mRankAdapter.setEnableLoadMore(false)
            mRankAdapter.setEnableLoadMore(true)
        }
    }

    override fun toastFail(msg: String?) {
        if (page == 0){
            loadService.showCallback(NetworkErrorCallback::class.java)
        }
        super.toastFail(msg)
    }

    override fun onItemClick(adapter: BaseQuickAdapter<*, *>, view: View, position: Int) {
        if (checkLogin()) {
            val bean = adapter.data[position] as UserRankBean
            val intent = Intent(mActivity, OtherUserActivity::class.java)
            intent.putExtra("id", bean.id)
            intent.putExtra("name", bean.name)
            startActivity(intent)
        }
    }

    private fun initRecyclerView() {
        mRankAdapter = RankAdapter(ArrayList())
        mRankAdapter.onItemClickListener = this
        mRankAdapter.setLoadMoreView(CustomLoadMoreView())
        mRankAdapter.setOnLoadMoreListener(this, rv_rank_user)
        rv_rank_user.layoutManager = LinearLayoutManager(mActivity, LinearLayoutManager.VERTICAL,false)
        rv_rank_user.adapter = mRankAdapter
    }
}
