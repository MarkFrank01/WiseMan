package com.zxcx.zhizhe.ui.circle.circlemanlist.detail

import android.os.Bundle
import android.support.v7.widget.GridLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.chad.library.adapter.base.BaseQuickAdapter
import com.zxcx.zhizhe.R
import com.zxcx.zhizhe.mvpBase.MvpFragment
import com.zxcx.zhizhe.ui.card.hot.CardBean
import com.zxcx.zhizhe.ui.circle.circledetaile.CircleDetaileActivity
import com.zxcx.zhizhe.ui.circle.circlehome.CircleBean
import com.zxcx.zhizhe.ui.circle.circlehome.CircleUserBean
import com.zxcx.zhizhe.utils.Constants
import com.zxcx.zhizhe.utils.SharedPreferencesUtil
import com.zxcx.zhizhe.utils.startActivity
import com.zxcx.zhizhe.widget.CustomLoadMoreView
import com.zxcx.zhizhe.widget.EmptyView
import kotlinx.android.synthetic.main.fragment_man_circle.*

/**
 * @author : MarkFrank01
 * @Created on 2019/3/27
 * @Description :
 */
class CircleManDetailCircleFragment : MvpFragment<CircleManDetailPresenter>(), CircleManDetailContract.View,
        BaseQuickAdapter.RequestLoadMoreListener, BaseQuickAdapter.OnItemChildClickListener {

    private var mPage = 0
    private var userId = 0

    private lateinit var mAdapter: CircleManCircleAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_man_circle, container, false)
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

        val emptyView = EmptyView.getEmptyView(mActivity, "该用户暂无圈子动态", R.drawable.no_circle_data)
        mAdapter.emptyView = emptyView

        if (list.size>1){
            for (bean in list.indices){
                if (bean%2==0){
                    list[bean].ItemTP = 1
                    mAdapter.data.add(list[bean])
                }else{
                    list[bean].ItemTP = 2
                    mAdapter.data.add(list[bean])
                }
            }
        }

//        if (mPage == 0) {
//            mAdapter.setNewData(list as List<MultiItemEntity>?)
//        } else {
//            mAdapter.addData(list)
//        }

        mPage++
        if (list.size < Constants.PAGE_SIZE) {
            mAdapter.loadMoreEnd(false)
        } else {
            mAdapter.loadMoreComplete()
            mAdapter.setEnableLoadMore(false)
            mAdapter.setEnableLoadMore(true)
        }
    }

    override fun getAuthorInfoSuccess(bean: CircleUserBean) {
    }

    override fun getOtherUserCreationSuccess(list: MutableList<CardBean>) {
    }

    override fun followSuccess() {
    }

    override fun getDataSuccess(bean: CircleUserBean?) {
    }

    override fun onLoadMoreRequested() {
        onRefresh()
    }

    override fun onItemChildClick(adapter: BaseQuickAdapter<*, *>, view: View, position: Int) {
        val bean = adapter.data[position] as CircleBean
        mActivity.startActivity(CircleDetaileActivity::class.java) {
            it.putExtra("circleID", bean.id)
        }
    }

    private fun onRefresh() {
        getCircleList()
    }

    private fun getCircleList() {
        mPresenter?.getCircleListByAuthorId(mPage, Constants.PAGE_SIZE, userId)
    }

    private fun initData() {
        userId = SharedPreferencesUtil.getInt("userId", 0)

    }

    private fun initRecycleView() {
        mAdapter = CircleManCircleAdapter(ArrayList())
        mAdapter.onItemChildClickListener = this

        mAdapter.setLoadMoreView(CustomLoadMoreView())
        mAdapter.setOnLoadMoreListener(this, rv_him_circle)

        rv_him_circle.layoutManager = object : GridLayoutManager(context, 2) {
            override fun canScrollVertically() = false
        }
        rv_him_circle.adapter = mAdapter
    }


}