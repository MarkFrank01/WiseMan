package com.zxcx.zhizhe.ui.circle.circlemessage.zan

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import com.chad.library.adapter.base.BaseQuickAdapter
import com.zxcx.zhizhe.R
import com.zxcx.zhizhe.mvpBase.MvpActivity
import com.zxcx.zhizhe.ui.circle.circlemessage.MyCircleTabBean
import com.zxcx.zhizhe.utils.Constants
import com.zxcx.zhizhe.widget.CustomLoadMoreView
import com.zxcx.zhizhe.widget.EmptyView
import kotlinx.android.synthetic.main.activity_msg_zan.*

/**
 * @author : MarkFrank01
 * @Created on 2019/3/20
 * @Description : 点赞的消息
 */
class CircleMesZanActivity :MvpActivity<CircleMesZanPresenter>(),CircleMesZanContract.View,
        BaseQuickAdapter.OnItemChildClickListener, BaseQuickAdapter.RequestLoadMoreListener{

    private var page = 0

    private lateinit var mAdapter:CircleMesZanAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_msg_zan)

        initView()
        initRecycleView()

        mPresenter.getLikeMessageList(page,Constants.PAGE_SIZE)
    }

    override fun createPresenter(): CircleMesZanPresenter {
        return CircleMesZanPresenter(this)
    }

    override fun getLikeMessageListSuccess(list: MutableList<MyCircleTabBean>) {
        val  emptyView = EmptyView.getEmptyView(mActivity,"暂时没有动态",R.drawable.no_data)
        mAdapter.emptyView = emptyView

        if (page == 0){
            mAdapter.setNewData(list)
        }else{
            mAdapter.addData(list)
        }

        mAdapter.notifyDataSetChanged()
        page++

        if (list.isEmpty()){
            mAdapter.loadMoreEnd(false)
        }else{
            mAdapter.loadMoreComplete()
            mAdapter.setEnableLoadMore(false)
            mAdapter.setEnableLoadMore(true)
        }
    }

    override fun getDataSuccess(bean: MyCircleTabBean?) {
    }

    override fun onItemChildClick(adapter: BaseQuickAdapter<*, *>, view: View, position: Int) {
        val bean = adapter.data[position] as MyCircleTabBean
//        mActivity.startActivity(CircleQuestionDetailActivity::class.java){
//            it.putExtra("huatiId",bean.relatedQaId)
//            it.putExtra("CircleId",bean.relatedCircleId)
//        }
    }

    override fun onLoadMoreRequested() {
        mPresenter.getLikeMessageList(page,Constants.PAGE_SIZE)
    }

    private fun initView(){
        initToolBar("点赞")
    }

    private fun initRecycleView(){
        mAdapter = CircleMesZanAdapter(ArrayList())
        mAdapter.setLoadMoreView(CustomLoadMoreView())
        mAdapter.setOnLoadMoreListener(this,rv_circle_meszan)
        mAdapter.onItemChildClickListener = this

        rv_circle_meszan.layoutManager = LinearLayoutManager(mActivity,LinearLayoutManager.VERTICAL,false)
        rv_circle_meszan.adapter = mAdapter
    }
}