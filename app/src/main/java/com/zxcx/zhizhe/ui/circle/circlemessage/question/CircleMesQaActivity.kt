package com.zxcx.zhizhe.ui.circle.circlemessage.question

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
import kotlinx.android.synthetic.main.activity_circle_msgqa.*

/**
 * @author : MarkFrank01
 * @Created on 2019/3/20
 * @Description : 提问的消息
 */
class CircleMesQaActivity : MvpActivity<CircleMesQaPresenter>(), CircleMesQaContract.View,
        BaseQuickAdapter.OnItemChildClickListener, BaseQuickAdapter.RequestLoadMoreListener {

    private var page = 0

    private lateinit var mAdapter:CircleMesAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_circle_msgqa)

        initView()
        initRecycleView()

        mPresenter.getQuestionMessageList(page,Constants.PAGE_SIZE)
    }

    override fun createPresenter(): CircleMesQaPresenter {
        return CircleMesQaPresenter(this)
    }

    override fun getQuestionMessageListSuccess(list: MutableList<MyCircleTabBean>) {
        val  emptyView = EmptyView.getEmptyView(mActivity,"暂时没有动态",R.drawable.no_comment)
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

    override fun onItemChildClick(adapter: BaseQuickAdapter<*, *>?, view: View?, position: Int) {
    }

    override fun onLoadMoreRequested() {
        mPresenter.getQuestionMessageList(page,Constants.PAGE_SIZE)
    }

    private fun initView(){
        initToolBar("提问")
    }

    private fun initRecycleView(){
        mAdapter = CircleMesAdapter(ArrayList())
        mAdapter.setLoadMoreView(CustomLoadMoreView())
        mAdapter.setOnLoadMoreListener(this,rv_circle_mesqa)
        mAdapter.onItemChildClickListener = this

        rv_circle_mesqa.layoutManager = LinearLayoutManager(mActivity,LinearLayoutManager.VERTICAL,false)
        rv_circle_mesqa.adapter = mAdapter
    }
}