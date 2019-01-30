package com.zxcx.zhizhe.ui.circle.createcircle

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import android.widget.CheckBox
import com.chad.library.adapter.base.BaseQuickAdapter
import com.scwang.smartrefresh.layout.api.RefreshLayout
import com.zxcx.zhizhe.R
import com.zxcx.zhizhe.event.PushManageListEvent
import com.zxcx.zhizhe.mvpBase.RefreshMvpActivity
import com.zxcx.zhizhe.ui.card.hot.CardBean
import com.zxcx.zhizhe.ui.circle.adapter.ManageCreateCircle2Adapter
import com.zxcx.zhizhe.ui.circle.adapter.ManageCreateCircleAdapter
import com.zxcx.zhizhe.utils.Constants
import com.zxcx.zhizhe.utils.LogCat
import com.zxcx.zhizhe.utils.getColorForKotlin
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
    private lateinit var mAdapter2: ManageCreateCircle2Adapter


    private lateinit var listCard: MutableList<CardBean>
    private lateinit var listArc: MutableList<CardBean>


    private var isFirstLong = true

    //回传值
    private var mBackList: MutableList<Int> = ArrayList()

    //显示数值
    private var mNumBerCard: Int = 0
    private var mNumArc: Int = 0


    private lateinit var mDialog: PushManageConfirmDialog

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
        list.forEach {
            if (it.cardType == 1) {
                listCard.add(it)
            } else {
                listArc.add(it)
            }
        }

        if (mPage == 0) {
            mAdapter.setNewData(listCard)
            mAdapter2.setNewData(listArc)
        } else {
            mAdapter.addData(listCard)
            mAdapter2.addData(listArc)
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
    fun onMessageEvent(event: PushManageListEvent) {
        val intent = Intent()
        intent.putIntegerArrayListExtra("manageCreateList", mBackList as java.util.ArrayList<Int>?)
        setResult(Activity.RESULT_OK, intent)
        finish()
    }

    override fun onItemClick(adapter: BaseQuickAdapter<*, *>?, view: View?, position: Int) {

    }

    override fun onItemChildClick(adapter: BaseQuickAdapter<*, *>, view: View, position: Int) {


        when (view.id) {
            R.id.con -> {
                toastShow("已被审核")
            }

            R.id.cb_choose_push_manage -> {
                val cb = view.findViewById<CheckBox>(R.id.cb_choose_push_manage)
                val bean = adapter.data[position] as CardBean

//                if (mNumArc < 2 && mNumBerCard < 4) {

                bean.mIfCheckOrNot = !bean.mIfCheckOrNot

                if (bean.mIfCheckOrNot) {
                    if (mNumArc < 2 && mNumBerCard < 4) {

                        if (!mBackList.contains(bean.id)) {
                            mBackList.add(bean.id)
                        }
                    }
                    if (bean.cardType == 1) {
                        if (mNumBerCard < 4) {
                            mNumBerCard++
                            LogCat.e("NUMCARD $mNumBerCard")
                            tv_daily_title_num.setTextColor(this.getColorForKotlin(R.color.text_color_1))
                        } else {
                            tv_daily_title_num.setTextColor(this.getColorForKotlin(R.color.button_blue))

                        }
                    } else {
                        if (mNumArc < 2) {
                            mNumArc++
                            LogCat.e("NUMARC $mNumArc")
                            tv_daily_title_num2.setTextColor(this.getColorForKotlin(R.color.text_color_1))
                        } else {
                            tv_daily_title_num2.setTextColor(this.getColorForKotlin(R.color.button_blue))
                        }
                    }

                } else {
                    if (mBackList.contains(bean.id)) {
                        mBackList.remove(bean.id)
                    }
                    if (bean.cardType == 1) {
                        mNumBerCard--
                    } else {
                        mNumArc--
                    }
                }

                tv_toolbar_right.isEnabled = mBackList.isNotEmpty()
                tv_daily_title_num.text = "$mNumBerCard/4"
//                tv_daily_title.setTextColor(this.getColorForKotlin(R.color.button_blue))
                tv_daily_title_num2.text = "$mNumArc/2"
//                tv_daily_title2.setTextColor(this.getColorForKotlin(R.color.button_blue))
//                }else{
//                    if (cb.isChecked){
//
//                        if (mBackList.contains(bean.id)) {
//                            mBackList.remove(bean.id)
//                        }
//                        if (bean.cardType == 1) {
//                            mNumBerCard--
//                        } else {
//                            mNumArc--
//                        }
//                    }else{
//                        cb.isChecked = false
//                    }
//                }
            }
        }
    }

    override fun setListener() {
        tv_toolbar_right.setOnClickListener {

            val bundle = Bundle()
            mDialog.arguments = bundle
            mDialog.show(mActivity.supportFragmentManager, "")

//            val intent = Intent()
//            intent.putIntegerArrayListExtra("manageCreateList", mBackList as java.util.ArrayList<Int>?)
//            setResult(Activity.RESULT_OK,intent)
//            finish()

        }
    }

    override fun onLoadMoreRequested() {
    }

    private fun initView() {

        listCard = ArrayList()
        listArc = ArrayList()

        initToolBar("作品审核")
        tv_toolbar_right.visibility = View.VISIBLE
        tv_toolbar_right.text = "完成"
        tv_toolbar_right.isEnabled = false
        tv_toolbar_right.setTextColor(ContextCompat.getColorStateList(mActivity, R.color.color_text_enable_blue))

        mAdapter = ManageCreateCircleAdapter(ArrayList())
        mAdapter.onItemChildClickListener = this
        rv_manage_circle_create.layoutManager = LinearLayoutManager(mActivity, LinearLayoutManager.VERTICAL, false)
        rv_manage_circle_create.adapter = mAdapter

        mAdapter2 = ManageCreateCircle2Adapter(ArrayList())
        mAdapter2.onItemChildClickListener = this
        rv_manage_circle_create2.layoutManager = LinearLayoutManager(mActivity, LinearLayoutManager.VERTICAL, false)
        rv_manage_circle_create2.adapter = mAdapter2

        rv_manage_circle_create.isNestedScrollingEnabled = false
        rv_manage_circle_create.setHasFixedSize(true)
        rv_manage_circle_create.isFocusable = false

        rv_manage_circle_create2.isNestedScrollingEnabled = false
        rv_manage_circle_create2.setHasFixedSize(true)
        rv_manage_circle_create2.isFocusable = false
    }

    fun onRefresh1() {
        getPushArc()
    }

    private fun getPushArc() {
        mPresenter.getPushArc(mPage, Constants.PAGE_SIZE)
    }
}