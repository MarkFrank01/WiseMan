package com.zxcx.zhizhe.ui.circle.circlemore

import android.graphics.Color
import android.os.Bundle
import android.view.View
import com.zxcx.zhizhe.R
import com.zxcx.zhizhe.mvpBase.MvpActivity
import com.zxcx.zhizhe.ui.circle.bean.CheckBean
import com.zxcx.zhizhe.utils.getColorForKotlin
import com.zxcx.zhizhe.widget.ratingbar.CBRatingBar
import kotlinx.android.synthetic.main.activity_pingfen.*
import kotlinx.android.synthetic.main.toolbar.*

/**
 * @author : MarkFrank01
 * @Created on 2019/3/9
 * @Description :
 */
//class CirclePingFenActivity:BaseActivity(){
class CirclePingFenActivity:MvpActivity<CirclePingFenPresenter>(),CirclePingFenContract.View{

    private var circleId:Int = 0
    private var contentQuality:Int = 0
    private var topicQuality:Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pingfen)

        initData()
        initView()

        rating_bar.setStarSize(40)
                .setStarCount(5)
                .setStarSpace(20)
                .setShowStroke(true)
                .setStarStrokeWidth(1)
                .setStarStrokeColor(Color.parseColor("#999999"))
//                .setStarFillColor(R.color.white)
//                .setStarCoverColor(R.color.button_blue)
                .setUseGradient(true)
                .setStartColor(R.color.white)
                .setEndColor(Color.parseColor("#0088AA"))
                .setCanTouch(true)
                .setPathDataId(R.string.star_select)
                .setDefaultPathData()
                .setCoverDir(CBRatingBar.CoverDir.leftToRight)
                .setOnStarTouchListener(object :CBRatingBar.OnStarTouchListener{
                    override fun onStarTouch(touchCount: Int) {
//                        toastShow("1的星星数"+touchCount)
                        contentQuality = touchCount
                    }
                })

        rating_bar_2.setStarSize(40)
                .setStarCount(5)
                .setStarSpace(20)
                .setShowStroke(true)
                .setStarStrokeWidth(1)
                .setStarStrokeColor(Color.parseColor("#999999"))
//                .setStarFillColor(R.color.white)
//                .setStarCoverColor(R.color.button_blue)
                .setUseGradient(true)
                .setStartColor(R.color.white)
                .setEndColor(Color.parseColor("#0088AA"))
                .setCanTouch(true)
                .setPathDataId(R.string.star_select)
//                .setDefaultPathData()
                .setCoverDir(CBRatingBar.CoverDir.leftToRight)
                .setOnStarTouchListener(object :CBRatingBar.OnStarTouchListener{
                    override fun onStarTouch(touchCount: Int) {
//                        toastShow("2的星星数"+touchCount)
                        topicQuality = touchCount
                    }
                })
    }

    private fun initView(){
        initToolBar("圈子评分")
        tv_toolbar_right.visibility = View.VISIBLE
        tv_toolbar_right.text = "提交"
        tv_toolbar_right.setTextColor(mActivity.getColorForKotlin(R.color.button_blue))

    }

    private fun initData(){
        circleId = intent.getIntExtra("circleId",0)
    }

    override fun setListener() {
        tv_toolbar_right.setOnClickListener {
            mPresenter.evaluationCircle(circleId, contentQuality, topicQuality)
        }
    }

    override fun createPresenter(): CirclePingFenPresenter {
        return CirclePingFenPresenter(this)
    }

    override fun evaluationCircleSuccess() {
        toastShow("评分成功")
        finish()
    }

    override fun postSuccess() {
    }

    override fun postFail(msg: String?) {
    }

    override fun getDataSuccess(bean: CheckBean?) {
    }
}