package com.zxcx.zhizhe.ui.circle.circlemanlist.detail

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.chad.library.adapter.base.BaseQuickAdapter
import com.zxcx.zhizhe.R
import com.zxcx.zhizhe.mvpBase.MvpFragment
import com.zxcx.zhizhe.ui.card.hot.CardBean
import com.zxcx.zhizhe.ui.circle.circlehome.CircleBean
import com.zxcx.zhizhe.ui.circle.circlehome.CircleUserBean
import com.zxcx.zhizhe.utils.Constants
import com.zxcx.zhizhe.utils.SharedPreferencesUtil
import com.zxcx.zhizhe.widget.CustomLoadMoreView
import kotlinx.android.synthetic.main.fragment_man_card.*

/**
 * @author : MarkFrank01
 * @Created on 2019/3/27
 * @Description :
 */
class CircleManDetailCardFragment : MvpFragment<CircleManDetailPresenter>(), CircleManDetailContract.View,
        BaseQuickAdapter.RequestLoadMoreListener, BaseQuickAdapter.OnItemChildClickListener {

    private var mPage = 0
    private var userId = 0

    private lateinit var mAdapter:CircleManCardAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_man_card, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initData()
        initRecycleView()
        onRefresh()
    }

    override fun createPresenter(): CircleManDetailPresenter {
        return CircleManDetailPresenter(this)
    }

    override fun getCircleListByAuthorIdSuccess(list: MutableList<CircleBean>) {
    }

    override fun getAuthorInfoSuccess(bean: CircleUserBean) {
    }

    override fun getOtherUserCreationSuccess(list: MutableList<CardBean>) {
        if (mPage == 0){
            mAdapter.setNewData(list)
        }else{
            mAdapter.addData(list)
        }

        mPage++
        if (list.size<Constants.PAGE_SIZE){
            mAdapter.loadMoreEnd(false)
        }else{
            mAdapter.loadMoreComplete()
            mAdapter.setEnableLoadMore(false)
            mAdapter.setEnableLoadMore(true)
        }
    }

    override fun followSuccess() {
    }

    override fun getDataSuccess(bean: CircleUserBean?) {
    }

    override fun onLoadMoreRequested() {
        onRefresh()
    }

    override fun onItemChildClick(adapter: BaseQuickAdapter<*, *>?, view: View?, position: Int) {
    }

    fun onRefresh(){
        getHimCard()
    }

    private fun getHimCard(){
        mPresenter.getOtherUserCreationSuccess(userId,0,mPage,Constants.PAGE_SIZE)
    }

    private fun initRecycleView(){
        mAdapter = CircleManCardAdapter(ArrayList())
        mAdapter.onItemChildClickListener = this

        mAdapter.setLoadMoreView(CustomLoadMoreView())
        mAdapter.setOnLoadMoreListener(this,rv_man_card)

//        rv_man_card.layoutManager = LinearLayoutManager(mActivity,LinearLayoutManager.VERTICAL,false)
        rv_man_card.layoutManager = object :LinearLayoutManager(context){
                override fun canScrollVertically() = false
        }

        rv_man_card.adapter = mAdapter
    }

    private fun initData(){
        userId = SharedPreferencesUtil.getInt("userId",0)
    }
}