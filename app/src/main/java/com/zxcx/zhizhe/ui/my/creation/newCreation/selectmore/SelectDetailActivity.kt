package com.zxcx.zhizhe.ui.my.creation.newCreation.selectmore

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.FrameLayout
import com.zxcx.zhizhe.R
import com.zxcx.zhizhe.mvpBase.BaseActivity
import com.zxcx.zhizhe.ui.my.selectAttention.ClassifyCardBean
import com.zxcx.zhizhe.utils.LogCat
import com.zxcx.zhizhe.utils.ScreenUtils
import com.zxcx.zhizhe.utils.getColorForKotlin
import kotlinx.android.synthetic.main.activity_select_detail.*
import kotlinx.android.synthetic.main.toolbar.*

/**
 * @author : MarkFrank01
 * @Created on 2019/4/1
 * @Description :
 */
class SelectDetailActivity : BaseActivity() {

    //推荐的标签
    private var mCollectionList: MutableList<ClassifyCardBean> = ArrayList()

    private var textBack = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_select_detail)
        initData()

        initRecycleView()
        initView()

    }

    private fun initData() {
        mCollectionList = intent.getParcelableArrayListExtra<ClassifyCardBean>("list")
        LogCat.e("SIZE IS" + mCollectionList.size)
    }

    private fun initView() {
        initToolBar()
        tv_toolbar_right.visibility = View.VISIBLE
        tv_toolbar_right.text = "完成"
        tv_toolbar_right.setTextColor(ContextCompat.getColorStateList(mActivity, R.color.button_blue))
    }

    override fun setListener() {
        tv_toolbar_right.setOnClickListener {
//            toastShow("text is " + textBack)
            LogCat.e("text is " + textBack)
            val intent = Intent()
            intent.putExtra("labelName2",textBack)
            setResult(Activity.RESULT_OK,intent)
            finish()
        }

        //自定义部分


    }

    private fun initRecycleView() {

//
//        for (bean in mCollectionList){
//            mAdapter.data.add(bean)
//        }
//        mAdapter.notifyDataSetChanged()

        rv_hot_label.removeAllViews()
        for (i in mCollectionList.indices) {
            val framLayout = LayoutInflater.from(mActivity).inflate(R.layout.item_select_new_bag_2, null) as FrameLayout
            val checkBox = framLayout.findViewById<CheckBox>(R.id.tv_item_search_hot)
            checkBox.text = "#" + mCollectionList[i].name

            checkBox.setOnClickListener {
//                for (x in mCollectionList.indices) {
//                    val checkBox2 = framLayout.findViewById<CheckBox>(R.id.tv_item_search_hot)
//                    checkBox2.isChecked = false
//                    checkBox2.setBackgroundResource(R.drawable.select_unlabel)
//                    checkBox2.setTextColor(mActivity.getColorForKotlin(R.color.text_color_2))
//
//                }

                if (checkBox.isChecked) {
                    checkBox.setBackgroundResource(R.drawable.select_label)
                    checkBox.setTextColor(mActivity.getColorForKotlin(R.color.white))
                    textBack = mCollectionList[i].name.toString()
                } else {
                    checkBox.setBackgroundResource(R.drawable.select_unlabel)
                    checkBox.setTextColor(mActivity.getColorForKotlin(R.color.text_color_2))
                }
            }

            rv_hot_label.addView(framLayout)
            val mlp = framLayout.layoutParams as ViewGroup.MarginLayoutParams
            mlp.setMargins(ScreenUtils.dip2px(15f), ScreenUtils.dip2px(15f), ScreenUtils.dip2px(7.5f), 0)
        }
    }
}