package com.zxcx.zhizhe.ui.circle.circlemanlist.detail

import android.os.Bundle
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

/**
 * @author : MarkFrank01
 * @Created on 2019/3/27
 * @Description :
 */
class CircleManDetailCircleFragment: MvpFragment<CircleManDetailPresenter>(), CircleManDetailContract.View,
        BaseQuickAdapter.RequestLoadMoreListener, BaseQuickAdapter.OnItemChildClickListener {

    private var mPage = 0
    private var userId = 0

    private lateinit var mAdapter:CircleManCircleAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_man_circle,container,false)
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
        if (mPage == 0){

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

    override fun onItemChildClick(adapter: BaseQuickAdapter<*, *>?, view: View?, position: Int) {
    }

    private fun onRefresh(){
        getCircleList()
    }

    private fun getCircleList(){
        mPresenter?.getCircleListByAuthorId(mPage,Constants.PAGE_SIZE,userId)
    }

    private fun initData(){
        userId = SharedPreferencesUtil.getInt("userId",0)

    }

    private fun initRecycleView(){

    }


}