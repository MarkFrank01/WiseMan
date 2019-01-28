package com.zxcx.zhizhe.ui.circle.createcircle

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import com.chad.library.adapter.base.BaseQuickAdapter
import com.scwang.smartrefresh.layout.api.RefreshLayout
import com.zxcx.zhizhe.R
import com.zxcx.zhizhe.event.PushManageListEvent
import com.zxcx.zhizhe.mvpBase.RefreshMvpActivity
import com.zxcx.zhizhe.ui.card.hot.CardBean
import com.zxcx.zhizhe.ui.circle.adapter.ManageCreateCircleAdapter
import com.zxcx.zhizhe.utils.Constants
import com.zxcx.zhizhe.utils.LogCat
import kotlinx.android.synthetic.main.activity_manage_circle_create.*
import kotlinx.android.synthetic.main.toolbar.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

/**
 * @author : MarkFrank01
 * @Created on 2019/1/24
 * @Description :
 */
class ManageCreateCircleActivity : RefreshMvpActivity<ManageCreatePresenter>(), ManageCreateContract.View,
        BaseQuickAdapter.OnItemClickListener, BaseQuickAdapter.OnItemChildClickListener, BaseQuickAdapter.RequestLoadMoreListener {

    private var mPage = 0
    private lateinit var mAdapter: ManageCreateCircleAdapter

    private var isFirstLong = true

    //回传值
    private var mBackList:MutableList<Int> = ArrayList()

    private lateinit var mDialog:PushManageConfirmDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_manage_circle_create)
        EventBus.getDefault().register(this)

        initView()
        onRefresh1()
        mDialog = PushManageConfirmDialog()
    }

    override fun onDestroy() {
        EventBus.getDefault().unregister(this)
        super.onDestroy()
    }

    override fun onRefresh(refreshLayout: RefreshLayout?) {
        onRefresh1()
    }

    override fun createPresenter(): ManageCreatePresenter {
        return ManageCreatePresenter(this)
    }

    override fun getDataSuccess(list: MutableList<CardBean>) {
        LogCat.e("MyManage ${list.size}")
        mRefreshLayout.finishRefresh()
        if (mPage == 0) {
            list[0].showTitle = "卡片"
            for (index in list.indices){
                if (list[index].cardType == 2&&isFirstLong){
                    isFirstLong = false
                    list[index].showTitle = "长文"
                }
            }

            mAdapter.setNewData(list)
        } else {
            for (index in list.indices){
                if (list[index].cardType == 2&&isFirstLong){
                    isFirstLong = false
                    list[index].showTitle = "长文"
                }
            }

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

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onMessageEvent(event:PushManageListEvent){
        val intent = Intent()
        intent.putIntegerArrayListExtra("manageCreateList", mBackList as java.util.ArrayList<Int>?)
        setResult(Activity.RESULT_OK, intent)
        finish()
    }

    override fun onItemClick(adapter: BaseQuickAdapter<*, *>?, view: View?, position: Int) {
    }

    override fun onItemChildClick(adapter: BaseQuickAdapter<*, *>, view: View, position: Int) {
        when(view.id){
            R.id.cb_choose_push_manage->{
                val bean = adapter.data[position] as CardBean
                bean.mIfCheckOrNot = !bean.mIfCheckOrNot

                if (bean.mIfCheckOrNot){
                    if (!mBackList.contains(bean.id)){
                        mBackList.add(bean.id)
                    }
                }else{
                    if (mBackList.contains(bean.id)){
                        mBackList.remove(bean.id)
                    }
                }
                LogCat.e("SIZE FOR MANAGE1"+mBackList.size)

                tv_toolbar_right.isEnabled = mBackList.isNotEmpty()
            }
        }
    }

    override fun setListener() {
        tv_toolbar_right.setOnClickListener {

            val bundle = Bundle()
            mDialog.arguments = bundle
            mDialog.show(mActivity.supportFragmentManager,"")

//            val intent = Intent()
//            intent.putIntegerArrayListExtra("manageCreateList", mBackList as java.util.ArrayList<Int>?)
//            setResult(Activity.RESULT_OK,intent)
//            finish()
        }
    }

    override fun onLoadMoreRequested() {
    }

    private fun initView() {
        initToolBar("作品审核")
        tv_toolbar_right.visibility = View.VISIBLE
        tv_toolbar_right.text = "完成"
        tv_toolbar_right.isEnabled = false
        tv_toolbar_right.setTextColor(ContextCompat.getColorStateList(mActivity, R.color.color_text_enable_blue))

        mAdapter = ManageCreateCircleAdapter(ArrayList())
        mAdapter.onItemChildClickListener = this
        rv_manage_circle_create.layoutManager = LinearLayoutManager(mActivity, LinearLayoutManager.VERTICAL, false)
        rv_manage_circle_create.adapter = mAdapter
    }

    fun onRefresh1() {
        getPushArc()
    }

    private fun getPushArc() {
        mPresenter.getPushArc(mPage, Constants.PAGE_SIZE)
    }
}