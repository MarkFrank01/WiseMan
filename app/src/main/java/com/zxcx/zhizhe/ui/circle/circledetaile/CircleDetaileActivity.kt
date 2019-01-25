package com.zxcx.zhizhe.ui.circle.circledetaile

import android.os.Bundle
import android.view.View
import com.chad.library.adapter.base.BaseQuickAdapter
import com.scwang.smartrefresh.layout.api.RefreshLayout
import com.zxcx.zhizhe.R
import com.zxcx.zhizhe.mvpBase.RefreshMvpActivity
import com.zxcx.zhizhe.ui.circle.circlehome.CircleBean
import com.zxcx.zhizhe.ui.circle.circlehome.CircleUserBean
import com.zxcx.zhizhe.utils.ImageLoader
import com.zxcx.zhizhe.utils.LogCat
import kotlinx.android.synthetic.main.layout_circle_detail.*

/**
 * @author : MarkFrank01
 * @Created on 2019/1/25
 * @Description :
 */
class CircleDetaileActivity : RefreshMvpActivity<CircleDetailePresenter>(), CircleDetaileContract.View, BaseQuickAdapter.OnItemClickListener,
        BaseQuickAdapter.OnItemChildClickListener, BaseQuickAdapter.RequestLoadMoreListener {

    private var circleID:Int = 0

    //存放自己是否加入圈子
    private var hasJoinBoolean:Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.layout_circle_detail)
        initData()
        initRecycleView()

        mPresenter.getCircleBasicInfo(circleID)
    }

    override fun onRefresh(refreshLayout: RefreshLayout?) {
    }

    override fun createPresenter(): CircleDetailePresenter {
        return CircleDetailePresenter(this)
    }

    override fun getCircleBasicInfoSuccess(bean: CircleBean) {
        detail_name.text  = bean.title
        t1.text = ""+bean.likeCount
        t2.text = ""+bean.qaCount
        t3.text = ""+bean.joinUserCount
        ImageLoader.load(this,bean.titleImage,R.drawable.default_card,detail_src_img)

        hasJoinBoolean = bean.hasJoin

        LogCat.e("member ${bean.memberList.size}")
        val memberList:MutableList<CircleUserBean> = bean.memberList

    }

    override fun getCircleMemberByCircleIdSuccess(bean: MutableList<CircleBean>) {
    }

    override fun getCircleQAByCircleIdSuccess(bean: MutableList<CircleBean>) {
    }

    override fun getDataSuccess(bean: MutableList<CircleBean>?) {
    }

    override fun onItemClick(adapter: BaseQuickAdapter<*, *>?, view: View?, position: Int) {
    }

    override fun onItemChildClick(adapter: BaseQuickAdapter<*, *>?, view: View?, position: Int) {
    }

    override fun onLoadMoreRequested() {
    }

    private fun initData(){
        circleID = intent.getIntExtra("circleID",0)
        LogCat.e("circleID $circleID")
    }

    private fun initRecycleView(){

    }
}
