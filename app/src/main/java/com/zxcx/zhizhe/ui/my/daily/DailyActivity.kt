package com.zxcx.zhizhe.ui.my.daily

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import com.zxcx.zhizhe.R
import com.zxcx.zhizhe.mvpBase.MvpActivity
import com.zxcx.zhizhe.ui.card.hot.DailyAdapter
import kotlinx.android.synthetic.main.activity_daily.*

/**
 * @author : MarkFrank01
 * @Created on 2018/12/13
 * @Description :
 */
class DailyActivity:MvpActivity<DailyPresenter>(),DailyContract.View{

    private lateinit var mAdapter : DailyAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_daily)
        initToolBar("实用头条")
        initView()
    }

    override fun createPresenter(): DailyPresenter {
        return DailyPresenter(this)
    }

    override fun getDataSuccess(bean: MutableList<DailyBean>?) {
        mAdapter.setNewData(bean)
    }

    private fun initView(){
        mAdapter = DailyAdapter(ArrayList())
        rv_daily_card.layoutManager = LinearLayoutManager(mActivity,LinearLayoutManager.VERTICAL,false)
        rv_daily_card.adapter = mAdapter

        mPresenter.getDailyList(0)
    }

}