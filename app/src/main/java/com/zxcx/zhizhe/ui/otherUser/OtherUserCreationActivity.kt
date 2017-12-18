package com.zxcx.zhizhe.ui.otherUser

import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import com.chad.library.adapter.base.BaseQuickAdapter
import com.kingja.loadsir.core.LoadSir
import com.zxcx.zhizhe.R
import com.zxcx.zhizhe.loadCallback.NetworkErrorCallback
import com.zxcx.zhizhe.mvpBase.MvpActivity
import com.zxcx.zhizhe.ui.card.card.cardDetails.CardDetailsActivity
import com.zxcx.zhizhe.ui.search.result.card.*
import com.zxcx.zhizhe.utils.Constants
import com.zxcx.zhizhe.widget.CustomLoadMoreView
import kotlinx.android.synthetic.main.activity_other_user_creation.*
import kotlinx.android.synthetic.main.toolbar.*

class OtherUserCreationActivity : MvpActivity<CreationPresenter>(), CreationContract.View,
        BaseQuickAdapter.RequestLoadMoreListener, BaseQuickAdapter.OnItemClickListener{

    private var mPage = 0
    private var mPageSize = Constants.PAGE_SIZE
    private var mSortType = 1//0倒序 1正序
    private lateinit var mAdapter: CreationAdapter
    private var otherUserId = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_read_cards)
        initView()
        initToolBar("Ta的创作")
        initLoadSir()
        getOtherUserCreation()
    }

    private fun initLoadSir() {
        loadService = LoadSir.getDefault().register(this, this)
    }

    override fun createPresenter(): CreationPresenter {
        return CreationPresenter(this)
    }

    private fun getOtherUserCreation() {
        mPresenter.getOtherUserCreation(otherUserId,mSortType,mPage,mPageSize)
    }

    override fun getDataSuccess(list: List<CreationBean>) {
        if (mPage == 0) {
            loadService.showSuccess()
            mAdapter.setNewData(list)
        } else {
            mAdapter.addData(list)
        }
        mPage++
        if (list.size < Constants.PAGE_SIZE) {
            mAdapter.loadMoreEnd(false)
        } else {
            mAdapter.loadMoreComplete()
            mAdapter.setEnableLoadMore(false)
            mAdapter.setEnableLoadMore(true)
        }
    }

    override fun toastFail(msg: String) {
        super.toastFail(msg)
        mAdapter.loadMoreFail()
        if (mPage == 0) {
            loadService.showCallback(NetworkErrorCallback::class.java)
        }
    }

    override fun onLoadMoreRequested() {
        getOtherUserCreation()
    }

    override fun onItemClick(adapter: BaseQuickAdapter<*, *>, view: View, position: Int) {
        val bean = adapter.data[position] as SearchCardBean
        val intent = Intent(mActivity, CardDetailsActivity::class.java)
        intent.putExtra("id", bean.id)
        intent.putExtra("name", bean.name)
        startActivity(intent)
    }

    private fun initView() {
        mAdapter = CreationAdapter(ArrayList())
        mAdapter.onItemClickListener = this
        mAdapter.setLoadMoreView(CustomLoadMoreView())
        mAdapter.setOnLoadMoreListener(this,rv_other_user_creation)
        rv_other_user_creation.layoutManager = LinearLayoutManager(mActivity, LinearLayoutManager.VERTICAL,false)
        rv_other_user_creation.adapter = mAdapter

        iv_toolbar_right.visibility = View.VISIBLE
        iv_toolbar_right.setImageResource(R.drawable.iv_card_bag_card)
        iv_toolbar_right.setOnClickListener {
            if (mSortType == 1) {
                mSortType = 0
                iv_toolbar_right.setImageResource(R.drawable.iv_card_bag_list)
            } else if (mSortType == 0) {
                mSortType = 1
                iv_toolbar_right.setImageResource(R.drawable.iv_card_bag_card)
            }
            mPage = 0
            getOtherUserCreation() }
    }
}