package com.zxcx.zhizhe.ui.my.message.system

import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.chad.library.adapter.base.BaseQuickAdapter
import com.zxcx.zhizhe.R
import com.zxcx.zhizhe.mvpBase.MvpFragment
import com.zxcx.zhizhe.ui.card.card.cardDetails.CardDetailsActivity
import com.zxcx.zhizhe.ui.home.rank.moreRank.RankActivity
import com.zxcx.zhizhe.ui.my.creation.ApplyForCreationActivity
import com.zxcx.zhizhe.ui.my.creation.creationDetails.RejectDetailsActivity
import com.zxcx.zhizhe.ui.my.creation.newCreation.NewCreationTitleActivity
import com.zxcx.zhizhe.ui.my.writer_status_reject
import com.zxcx.zhizhe.ui.my.writer_status_user
import com.zxcx.zhizhe.utils.Constants
import com.zxcx.zhizhe.utils.SVTSConstants
import com.zxcx.zhizhe.utils.SharedPreferencesUtil
import com.zxcx.zhizhe.widget.CustomLoadMoreView
import com.zxcx.zhizhe.widget.EmptyView
import kotlinx.android.synthetic.main.fragment_system_message.*

class SystemMessageFragment : MvpFragment<SystemMessagePresenter>(), SystemMessageContract.View ,
        BaseQuickAdapter.RequestLoadMoreListener,BaseQuickAdapter.OnItemChildClickListener{

    private var mPage = 0
    private val mPageSize = Constants.PAGE_SIZE
    private lateinit var mAdapter: SystemMessageAdapter

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater!!.inflate(R.layout.fragment_system_message, container, false)
    }

    override fun createPresenter(): SystemMessagePresenter {
        return SystemMessagePresenter(this)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        SharedPreferencesUtil.saveData(SVTSConstants.hasDynamicMessage,true)
        initRecyclerView()
        mPresenter.getSystemMessage(mPage,mPageSize)
    }

    override fun getDataSuccess(list: List<SystemMessageBean>) {
        if (mPage == 0) {
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

    override fun onLoadMoreRequested() {
        mPresenter.getSystemMessage(mPage,mPageSize)
    }

    override fun onItemChildClick(adapter: BaseQuickAdapter<*, *>?, view: View?, position: Int) {
        val bean = mAdapter.data[position]
        val intent = Intent()
        when(bean.messageType){
            message_card_pass -> {
                intent.setClass(mActivity,CardDetailsActivity::class.java)
                intent.putExtra("id",bean.relatedCardId)
            }
            message_card_reject -> {
                intent.setClass(mActivity,RejectDetailsActivity::class.java)
                intent.putExtra("id",bean.relatedCardId)
            }
            message_apply_pass -> {
                intent.setClass(mActivity,NewCreationTitleActivity::class.java)
            }
            message_apply_reject -> {
                if (SharedPreferencesUtil.getInt(SVTSConstants.writerStatus,0) == writer_status_user
                        || SharedPreferencesUtil.getInt(SVTSConstants.writerStatus,0) == writer_status_reject) {
                    //没有创作资格
                    intent.setClass(mActivity, ApplyForCreationActivity::class.java)
                }
                else {
                    return
                }
            }
            message_rank -> {
                intent.setClass(mActivity,RankActivity::class.java)
            }
            message_recommend -> {
                intent.setClass(mActivity,CardDetailsActivity::class.java)
                intent.putExtra("id",bean.relatedCardId)
            }
        }
        startActivity(intent)
    }

    private fun initRecyclerView() {
        mAdapter = SystemMessageAdapter(ArrayList())
        mAdapter.onItemChildClickListener = this
        mAdapter.setLoadMoreView(CustomLoadMoreView())
        mAdapter.setOnLoadMoreListener(this,rv_system_message)
        rv_system_message.layoutManager = LinearLayoutManager(mActivity, LinearLayoutManager.VERTICAL,false)
        rv_system_message.adapter = mAdapter
        val emptyView = EmptyView.getEmptyView(mActivity,"暂时没有更多消息","前往我的页面-系统设置开启推送",null,null)
        mAdapter.emptyView = emptyView
    }
}