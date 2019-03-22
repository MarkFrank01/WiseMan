package com.zxcx.zhizhe.ui.newrank

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import com.chad.library.adapter.base.BaseQuickAdapter
import com.zxcx.zhizhe.R
import com.zxcx.zhizhe.mvpBase.MvpActivity
import com.zxcx.zhizhe.ui.newrank.morerank.MoreRankActivity
import com.zxcx.zhizhe.ui.rank.UserRankBean
import com.zxcx.zhizhe.utils.SVTSConstants
import com.zxcx.zhizhe.utils.SharedPreferencesUtil
import com.zxcx.zhizhe.utils.startActivity
import kotlinx.android.synthetic.main.activity_newrank.*

/**
 * @author : MarkFrank01
 * @Created on 2019/3/22
 * @Description :
 */
class NewRankActivity : MvpActivity<NewRankPresenter>(), NewRankContract.View,
        BaseQuickAdapter.OnItemChildClickListener,BaseQuickAdapter.OnItemClickListener {

    private var mUserId:Int = 0
    private lateinit var mAdapter: NewRankAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_newrank)

        initToolBar("我的榜单")
        initRecyclerView()
        onRefresh()
    }

    override fun createPresenter(): NewRankPresenter {
        return NewRankPresenter(this)
    }

    override fun getMyRankSuccess(bean: UserRankBean) {
    }

    override fun getTopTenRankSuccess(bean: List<UserRankBean>) {
        mAdapter.setNewData(bean)
        mAdapter.remove(0)
        mAdapter.remove(0)
        mAdapter.remove(0)
    }

    override fun getDataSuccess(bean: List<UserRankBean>?) {
    }

    override fun onItemChildClick(adapter: BaseQuickAdapter<*, *>?, view: View?, position: Int) {
    }

    override fun onItemClick(adapter: BaseQuickAdapter<*, *>?, view: View?, position: Int) {
    }

    override fun setListener() {
        load_more_load_end_view.setOnClickListener {
            mActivity.startActivity(MoreRankActivity::class.java){}
        }

        last_week_rank.setOnClickListener {
            mActivity.startActivity(MoreRankActivity::class.java){}

        }
    }

    private fun initRecyclerView() {
        mAdapter = NewRankAdapter(arrayListOf())
        mAdapter.onItemChildClickListener = this
        rv_ht_rank.layoutManager = object : LinearLayoutManager(this) {
            override fun canScrollVertically() = false
        }

        rv_ht_rank.adapter = mAdapter
    }

    private fun onRefresh(){
        mUserId = SharedPreferencesUtil.getInt(SVTSConstants.userId, 0)
        if (mUserId != 0){
            mPresenter.getMyRank()
        }
        mPresenter.getTopTenRank()
    }
}