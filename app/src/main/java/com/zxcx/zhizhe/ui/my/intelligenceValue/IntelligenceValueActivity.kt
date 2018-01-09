package com.zxcx.zhizhe.ui.my.intelligenceValue

import android.content.Intent
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v7.widget.LinearLayoutManager
import com.gyf.barlibrary.ImmersionBar
import com.zxcx.zhizhe.R
import com.zxcx.zhizhe.mvpBase.MvpActivity
import com.zxcx.zhizhe.ui.welcome.WebViewActivity
import com.zxcx.zhizhe.utils.Constants
import kotlinx.android.synthetic.main.activity_intelligence_value.*
import java.util.*

class IntelligenceValueActivity : MvpActivity<IntelligenceValuePresenter>(), IntelligenceValueContract.View {

    private lateinit var mAdapter: IntelligenceValueAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_intelligence_value)
        initView()
        mPresenter.getIntelligenceValue()
        if (Constants.IS_NIGHT){
            val drawable = ColorDrawable(ContextCompat.getColor(mActivity, R.color.opacity_30))
            fl_intelligence_value.foreground = drawable
        }
    }

    override fun initStatusBar() {
        ImmersionBar.with(this)
                .transparentBar()
                .init()
    }

    override fun createPresenter(): IntelligenceValuePresenter {
        return IntelligenceValuePresenter(this)
    }

    override fun getDataSuccess(bean: IntelligenceValueBean) {
        mAdapter.setNewData(bean.missionVOList)
        tv_intelligence_value_total.text = bean.totalValue.toString()
        tv_intelligence_value_today.text = bean.todayValue.toString()
        rpb_intelligence_value_progress.setProgress((bean.todayAchieveMissionCount!! * 100/ bean.todayAllMissionCount!!).toFloat())
    }

    private fun initView() {
        iv_intelligence_value_back.setOnClickListener {
            onBackPressed()
        }
        tv_intelligence_value_help.setOnClickListener {

            val intent = Intent(this, WebViewActivity::class.java)
            intent.putExtra("title", "帮助")
            if(Constants.IS_NIGHT){
                intent.putExtra("url", getString(R.string.base_url) + getString(R.string.intelligence_value_help_dark_url))
            }else {
                intent.putExtra("url", getString(R.string.base_url) + getString(R.string.intelligence_value_help_url))
            }
            startActivity(intent)
        }
        mAdapter = IntelligenceValueAdapter(ArrayList())
        rv_intelligence_value.layoutManager = LinearLayoutManager(mActivity, LinearLayoutManager.VERTICAL,false)
        rv_intelligence_value.adapter = mAdapter
    }
}
