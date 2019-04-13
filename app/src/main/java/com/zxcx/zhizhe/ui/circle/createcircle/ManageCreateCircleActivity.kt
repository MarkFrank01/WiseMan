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
            mAdapter2.loadMoreEnd(false)
        } else {
            mAdapter2.loadMoreComplete()
            mAdapter2.setEnableLoadMore(false)
            mAdapter2.setEnableLoadMore(true)
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

                //首先判断新增还还是减少
                if (cb.isChecked) {
                    //点亮
                    //判断是否够数
                    if (bean.cardType==1){
                        //是卡片
                        //判断卡片是否够数
                        if (mNumBerCard==4){
                            //够数，不给他点
                            cb.isChecked=false
//                            tv_daily_title_num.setTextColor(this.getColorForKotlin(R.color.button_blue))

                        }else{
                            //不够数
                            mNumBerCard++
                            mBackList.add(bean.id)
//                            tv_daily_title_num.setTextColor(this.getColorForKotlin(R.color.button_blue))
                        }
                    }else{
                        //是长文
                        if (mNumArc==2){
                            //够数，不给他点
                            cb.isChecked=false
//                            tv_daily_title_num.setTextColor(this.getColorForKotlin(R.color.button_blue))
                        }else{
                            //不够数
                            mNumArc++
                            mBackList.add(bean.id)
//                            tv_daily_title_num.setTextColor(this.getColorForKotlin(R.color.button_blue))
                        }
                    }
                }else{
                    //点灭
                    //判断是卡片还是长文
                    if (bean.cardType==1){
                        //是卡片 判断是否穿底
                        if (mNumBerCard==0){
                            cb.isChecked=false
                        }else{
                            mNumBerCard--
                            mBackList.remove(bean.id)
                        }
                    }else{
                        //是长文
                        if (mNumArc==0){
                            cb.isChecked=false
                        }else{
                            mNumArc--
                            mBackList.remove(bean.id)
                        }
                    }

                }

                tv_daily_title_num.text = "$mNumBerCard/4"
                tv_daily_title_num2.text = "$mNumArc/2"

                tv_toolbar_right.isEnabled = mBackList.size>0

                if (mNumBerCard==4){
                    tv_daily_title_num.setTextColor(this.getColorForKotlin(R.color.button_blue))
                }else{
                    tv_daily_title_num.setTextColor(this.getColorForKotlin(R.color.text_color_1))
                }
                if (mNumArc==2){
                    tv_daily_title_num2.setTextColor(this.getColorForKotlin(R.color.button_blue))
                }else{
                    tv_daily_title_num2.setTextColor(this.getColorForKotlin(R.color.text_color_1))
                }

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