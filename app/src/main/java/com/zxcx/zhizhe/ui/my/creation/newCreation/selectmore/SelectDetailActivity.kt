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
import com.lxj.xpopup.XPopup
import com.lxj.xpopup.interfaces.OnSelectListener
import com.zxcx.zhizhe.R
import com.zxcx.zhizhe.mvpBase.BaseActivity
import com.zxcx.zhizhe.ui.my.creation.newCreation.NewLabelDialog
import com.zxcx.zhizhe.ui.my.selectAttention.ClassifyCardBean
import com.zxcx.zhizhe.utils.LogCat
import com.zxcx.zhizhe.utils.ScreenUtils
import com.zxcx.zhizhe.utils.expandViewTouchDelegate
import com.zxcx.zhizhe.utils.getColorForKotlin
import com.zxcx.zhizhe.widget.bottominfopopup.BottomInfoPopup
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

//    private var textBack = ""

    private var mNewLabelSelect: Boolean = false

    private var mTheFirst: String = ""
    private var mTheSecond: String = ""

    private var mNewLabelName: String = ""
    private var mNewLabelName2: String = ""

    //第二个自定义标签状态(待定)
    private var mCustomSecond = false
    //存放最后传递的数据
    private var mPushData: MutableList<String> = ArrayList()
    //存放单独的官方类别数据
    private var mSingleLable: String = ""

    //存放单独的分类数据
    private var mSingleClassify: String = ""


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_select_detail)
        initData()

        initRecycleView()
        initView()

    }

    private fun initData() {
        mCollectionList = intent.getParcelableArrayListExtra<ClassifyCardBean>("list")
        mSingleClassify = intent.getStringExtra("classifyName")

        LogCat.e("mSingleClassify is " + mSingleClassify)
    }

    private fun initView() {
        initToolBar()
        tv_toolbar_right.visibility = View.VISIBLE
        tv_toolbar_right.text = "完成"
        tv_toolbar_right.setTextColor(ContextCompat.getColorStateList(mActivity, R.color.button_blue))
    }

    override fun setListener() {
        tv_toolbar_right.setOnClickListener {
            val intent = Intent()

            if (mSingleLable != "" && mSingleLable.isNotEmpty()) {
                LogCat.e("单官方")
                mTheFirst = mSingleLable
            }

            LogCat.e("SIZE" + mPushData.size)
            if (mPushData.size == 1) {

                LogCat.e("单标签或者是")

                if (mSingleLable != "" && mSingleLable.isNotEmpty()) {
                    LogCat.e("单官方和单自定义")
//                    mTheSecond = mPushData[0]
                    mTheFirst = mPushData[0]
                } else if (mSingleLable == "") {
                    LogCat.e("单自定义")
                    mTheFirst = mPushData[0]
                }
            } else if (mPushData.size > 1) {
                LogCat.e("双标签")
                mTheFirst = mPushData[0]
                mTheSecond = mPushData[1]
            }

            intent.putExtra("labelName", mTheFirst)
            intent.putExtra("labelName2", mTheSecond)
            setResult(Activity.RESULT_OK, intent)
            finish()
        }

        //自定义部分
        iv_select_label_new_label.setOnClickListener {
            val dialog = NewLabelDialog()
            dialog.mListener = {
                mNewLabelName = it
                iv_select_label_new_label.visibility = View.GONE
//                iv_select_label_new_label_delete.visibility = View.VISIBLE
                cb_item_select_label_new_label.visibility = View.VISIBLE
                tv_delete_label.visibility = View.VISIBLE
                tv_delete_label.expandViewTouchDelegate(ScreenUtils.dip2px(10f))
                cb_item_select_label_new_label.text = it
                cb_item_select_label_new_label.isChecked = true

//                mPushData.add(it)

                mCustomSecond = true
                if (mSingleLable == "") {
                    iv_select_label_new_label2.visibility = View.VISIBLE
                }
//                mTheSecond = it
            }
            dialog.show(supportFragmentManager, "")
        }

        iv_select_label_new_label2.setOnClickListener {
            val dialog2 = NewLabelDialog()
            dialog2.mListener = {
                mNewLabelName2 = it
                iv_select_label_new_label2.visibility = View.GONE
//                iv_select_label_new_label_delete.visibility = View.VISIBLE
                cb_item_select_label_new_label2.visibility = View.VISIBLE
                tv_delete_label2.visibility = View.VISIBLE
                tv_delete_label2.expandViewTouchDelegate(ScreenUtils.dip2px(10f))
                cb_item_select_label_new_label2.text = it
                cb_item_select_label_new_label2.isChecked = true

//                mPushData.add(it)
            }
            dialog2.show(supportFragmentManager, "")
        }

        tv_delete_label.setOnClickListener {

            XPopup.Builder(mActivity)
                    .asCustom(BottomInfoPopup(this, "是否删除自定义标签", -1,
                            OnSelectListener { position, text ->
                                if (position == 2) {
                                    mPushData.remove(cb_item_select_label_new_label.text.toString())
                                    if (mPushData.size > 0) {
                                        iv_select_label_new_label2.visibility = View.VISIBLE
                                        cb_item_select_label_new_label2.visibility = View.GONE
                                        cb_item_select_label_new_label2.isChecked = false
                                        tv_delete_label2.visibility = View.GONE
                                        cb_item_select_label_new_label.text = cb_item_select_label_new_label2.text.toString()
                                        cb_item_select_label_new_label2.text = ""
                                    }

                                    if (mPushData.size == 0) {
//                                        iv_select_label_new_label.visibility = View.VISIBLE
                                        cb_item_select_label_new_label.visibility = View.GONE
                                        cb_item_select_label_new_label.text = ""
                                        cb_item_select_label_new_label.isChecked = false
                                        tv_delete_label.visibility = View.GONE
                                    }
                                }
                            })
                    ).show()
        }

        tv_delete_label2.setOnClickListener {

            XPopup.Builder(mActivity)
                    .asCustom(BottomInfoPopup(this, "是否删除自定义标签", -1,
                            OnSelectListener { position, text ->
                                if (position == 2) {
                                    mPushData.remove(cb_item_select_label_new_label2.text.toString())
                                    iv_select_label_new_label2.visibility = View.VISIBLE
                                    cb_item_select_label_new_label2.visibility = View.GONE
                                    cb_item_select_label_new_label2.text = ""
                                    cb_item_select_label_new_label2.isChecked = false

                                    tv_delete_label2.visibility = View.GONE
                                }
                            })
                    ).show()
        }

        //选中自定义的标签时
        cb_item_select_label_new_label.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                mPushData.add(cb_item_select_label_new_label.text.toString())

            } else {
                mPushData.remove(cb_item_select_label_new_label.text.toString())
            }

            mNewLabelSelect = isChecked

        }

        cb_item_select_label_new_label2.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                mPushData.add(cb_item_select_label_new_label2.text.toString())

            } else {
                mPushData.remove(cb_item_select_label_new_label2.text.toString())
            }
            mNewLabelSelect = isChecked
        }
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


                if (checkBox.isChecked && mPushData.size < 2) {
                    checkBox.setBackgroundResource(R.drawable.select_label)
                    checkBox.setTextColor(mActivity.getColorForKotlin(R.color.white))
//                    mTheSecond = mCollectionList[i].name.toString()

                    mPushData.add(mCollectionList[i].name + "")

//                    LogCat.e("选中的位置是" + i)

                    mSingleLable = mCollectionList[i].name.toString()

                } else {
                    checkBox.setBackgroundResource(R.drawable.select_unlabel)
                    checkBox.setTextColor(mActivity.getColorForKotlin(R.color.text_color_2))

//                    if (mPushData.find { it == checkBox.text.toString().trim() } != null) {
//                        mPushData.remove(mCollectionList[i].name+"")
//                        LogCat.e("SBBB"+checkBox.text.toString().trim())
//                        LogCat.e("取消选中的位置是" + i)
//                    }

//                    var iterator:Iterator<Int> = arrayListOf<>()
                    val iterator = mPushData.iterator()

                    if (mPushData.size > 0) {

                        while (iterator.hasNext()) {
                            var str = iterator.next()
                            if ("#" + str == checkBox.text.toString().trim()) {
                                iterator.remove()
                            }
                        }

//                        mPushData.forEach {
//                            LogCat.e("in" + it + "--------" + checkBox.text.toString().trim())
//                            if ("#" + it == checkBox.text.toString().trim()) {
//                                mPushData.remove(it)
//                                LogCat.e("SBBB" + checkBox.text.toString().trim())
//                                LogCat.e("取消选中的位置是" + i)
//                            }
//                        }
                    }
//                    mPushData.remove(checkBox.toString().trim())

                    mSingleLable = ""
                }
            }


            rv_hot_label.addView(framLayout)
            val mlp = framLayout.layoutParams as ViewGroup.MarginLayoutParams
            mlp.setMargins(ScreenUtils.dip2px(15f), ScreenUtils.dip2px(15f), ScreenUtils.dip2px(7.5f), 0)
        }

    }
}