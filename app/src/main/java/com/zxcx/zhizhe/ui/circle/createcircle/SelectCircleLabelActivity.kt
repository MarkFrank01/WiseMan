package com.zxcx.zhizhe.ui.circle.createcircle

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v7.widget.GridLayoutManager
import android.util.Log
import android.view.View
import butterknife.ButterKnife
import com.zxcx.zhizhe.R
import com.zxcx.zhizhe.mvpBase.MvpActivity
import com.zxcx.zhizhe.ui.my.creation.newCreation.SelectClassifyAdapter1
import com.zxcx.zhizhe.ui.my.creation.newCreation.SelectClassifyOtherAdapter1
import com.zxcx.zhizhe.ui.my.creation.newCreation.SelectLabelAdapter1
import com.zxcx.zhizhe.ui.my.selectAttention.ClassifyBean
import com.zxcx.zhizhe.ui.my.selectAttention.ClassifyCardBean
import com.zxcx.zhizhe.ui.my.selectAttention.SelectAttentionContract
import com.zxcx.zhizhe.ui.my.selectAttention.SelectAttentionPresenter
import com.zxcx.zhizhe.utils.LogCat
import kotlinx.android.synthetic.main.activity_select_label2.*
import kotlinx.android.synthetic.main.toolbar.*

/**
 * 创作-选择标签页面
 */

class SelectCircleLabelActivity : MvpActivity<SelectAttentionPresenter>(), SelectAttentionContract.View {

    private var mSelectedClassify: ClassifyBean? = null
    private var mSelectedLabel: ClassifyCardBean? = null
    private var mNewLabelSelect: Boolean = false
    private var mNewLabelName: String = ""
    private var mNewLabelName2: String = ""
    private lateinit var mClassifyAdapter: SelectClassifyAdapter1
    private lateinit var mLabelAdapter: SelectLabelAdapter1

    private lateinit var mOtherAdapter: SelectClassifyOtherAdapter1

    //数据处理后热门分类
    private var mNewHotClassify: MutableList<ClassifyBean> = ArrayList()
    //数据处理后其它分类
    private var mNewOtherClassify: MutableList<ClassifyBean> = ArrayList()

    private var classifyId = 0

    //选择标签值
    private var labelName = ""
    //自定义标签值
    private var mSelectName: String = ""

    private var mSelectItem: Int = -1


    private var mTheFirst: String = ""
    private var mTheSecond: String = ""

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
        setContentView(R.layout.activity_select_label2)
        ButterKnife.bind(this)
        initRecyclerView()
        initData()
        initView()
        mPresenter.getClassify()
    }

    private fun initData() {
        //二次获取
//        labelName = intent.getStringExtra("labelName")
//        classifyId = intent.getIntExtra("classifyId", 0)
//        mSelectName = intent.getStringExtra("twoLabelName")

        //日志
        Log.e("Back", labelName + "!" + mSelectName)
        //是否可直接跳过
        if (labelName.isNotEmpty() || mSelectName.isNotEmpty()) {
            tv_toolbar_right.isEnabled = true
        }

        //必须置为第一个值，方便区分
//        mTheFirst = labelName
        //必须置为第二个值，方便区分
//        mTheSecond = mSelectName
    }

    override fun createPresenter(): SelectAttentionPresenter {
        return SelectAttentionPresenter(this)
    }

    override fun getDataSuccess(list: MutableList<ClassifyBean>) {
        var isNewLabel = true
        list.forEach {
            it.dataList.forEach {

                //判断是否有已选择的值，有则选中，将替换为第一个值
                if (it.name == labelName) {
                    it.isChecked = true
                    mSelectedLabel = it
                    mNewLabelSelect = false
                    isNewLabel = false

                }
//                else {
//                    it.isChecked = false
//                }
            }
            if (it.id == classifyId) {
                it.isChecked = true
                mSelectedClassify = it
//                group_select_label.visibility = View.VISIBLE
                mLabelAdapter.setNewData(it.dataList)
//                tv_select_label_2.visibility = View.VISIBLE
//                tv_select_label_3.visibility = View.GONE

            }

            if (it.isHit == 1) {
                mNewHotClassify.add(it)
            } else if (it.isHit == 0) {
                mNewOtherClassify.add(it)
            }

        }
//        LogCat.e("Hot: " + mNewHotClassify.size + "Other: " + mNewOtherClassify.size)
        mClassifyAdapter.setNewData(mNewHotClassify)
        mOtherAdapter.setNewData(mNewOtherClassify)
//        mClassifyAdapter.setNewData(list)
//        mOtherAdapter.setNewData(list)

        //判断不为空进行操作,存在疑问，bug来源
        if (mTheSecond.isNotEmpty()) {
            mNewLabelName = labelName
//            iv_select_label_new_label.visibility = View.GONE
//            iv_select_label_new_label_delete.visibility = View.VISIBLE
//            cb_item_select_label_new_label.visibility = View.VISIBLE
//            cb_item_select_label_new_label.text = mTheSecond
//            cb_item_select_label_new_label.isChecked = true

//            mTheFirst = labelName
            tv_toolbar_right.isEnabled
        }


    }

    override fun postSuccess() {
    }

    override fun postFail(msg: String) {
    }

    private fun initView() {
        initToolBar()
        tv_toolbar_right.visibility = View.VISIBLE
        tv_toolbar_right.text = "完成"
        tv_toolbar_right.isEnabled = false
        tv_toolbar_right.setTextColor(ContextCompat.getColorStateList(mActivity, R.color.color_text_enable_blue))
    }

    override fun setListener() {
        tv_toolbar_right.setOnClickListener {
            val intent = Intent()
            //传递值，存在疑问，bug导致数据顺序错乱

//            intent.putExtra("labelName", if (mNewLabelSelect) mNewLabelName else mSelectedLabel?.name)

            if (mSingleLable != "" && mSingleLable.isNotEmpty()) {
                LogCat.e("单官方")
                mTheFirst = mSingleLable
            }

            LogCat.e("SIZE" + mPushData.size)
            if (mPushData.size == 1) {

                LogCat.e("单标签或者是")

                if (mSingleLable != "" && mSingleLable.isNotEmpty()) {
                    LogCat.e("单官方和单自定义")
                    mTheSecond = mPushData[0]
                } else if (mSingleLable == "") {
                    LogCat.e("单自定义")
                    mTheFirst = mPushData[0]
                }
            } else if (mPushData.size > 1) {
                LogCat.e("双标签")
                mTheFirst = mPushData[0]
                mTheSecond = mPushData[1]
            }

            if (mSingleLable == "" && mSingleClassify.isNotEmpty() && mSingleClassify != "") {
                LogCat.e("单类别")
                if (mPushData.size == 0) {
//                    mTheFirst = mSingleClassify
                    LogCat.e("+++" + mSelectedClassify?.id)
                }
            }

            intent.putExtra("labelName", mTheFirst)
            intent.putExtra("twoLabelName", mTheSecond)
            intent.putExtra("classifyId", mSelectedClassify?.id)
            intent.putExtra("classifyName", mSelectedClassify?.title)

            LogCat.e("Classify ID " + mSelectedClassify?.id)

            setResult(Activity.RESULT_OK, intent)
            finish()
        }


    }

    //先加载rv
    private fun initRecyclerView() {
        mClassifyAdapter = SelectClassifyAdapter1(ArrayList())
        mLabelAdapter = SelectLabelAdapter1(ArrayList())
        mOtherAdapter = SelectClassifyOtherAdapter1(ArrayList())
//        val manager = GridLayoutManager(mActivity, 4)
//        val manager1 = GridLayoutManager(mActivity, 4)

        val manager = GridLayoutManager(mActivity, 4)
        val manager_other = GridLayoutManager(mActivity, 4)
        val manager1 = GridLayoutManager(mActivity, 4)

        //热门分类
        rv_select_classify.adapter = mClassifyAdapter
        rv_select_classify.layoutManager = manager

        //其它分类
        rv_select_classify_2.adapter = mOtherAdapter
        rv_select_classify_2.layoutManager = manager_other

        //小标签
        rv_select_label.adapter = mLabelAdapter
        rv_select_label.layoutManager = manager1


        //大分类处理，不动
        mClassifyAdapter.setOnItemChildClickListener { adapter, view, position ->

            val bean = adapter.data[position] as ClassifyBean
            val isChecked = !bean.isChecked
            adapter.data.forEach {
                it as ClassifyBean
                it.isChecked = false
            }

            mNewOtherClassify.forEach {
                it.isChecked = false
            }
            mOtherAdapter.setNewData(mNewOtherClassify)
            mOtherAdapter.notifyDataSetChanged()

            bean.isChecked = isChecked
            mClassifyAdapter.notifyDataSetChanged()
            mSelectedLabel = null
            if (bean.isChecked) {
                mSelectedClassify = bean

                bean.dataList.forEach {
                    it.isChecked = false
                }

                mLabelAdapter.setNewData(bean.dataList)


                mSingleClassify = bean.title
                tv_toolbar_right.isEnabled = true

            } else {
                mSelectedClassify = null

                mSingleClassify = ""
                mSingleLable = ""
                tv_toolbar_right.isEnabled = false
            }
            if (mSingleClassify != "" && mSingleClassify.isNotEmpty() && mSelectedClassify != null) {
                tv_toolbar_right.isEnabled = true
            } else {
                tv_toolbar_right.isEnabled = mSelectedClassify != null && (mSelectedLabel != null || mNewLabelSelect)
            }
            mSelectItem = -1


            //////////////////////////////////////
            //新的分类方式

        }

        //其它分类
        mOtherAdapter.setOnItemChildClickListener { adapter, view, position ->
            val bean = adapter.data[position] as ClassifyBean
            val isChecked = !bean.isChecked
            adapter.data.forEach {
                it as ClassifyBean
                it.isChecked = false
            }

            mNewHotClassify.forEach {
                it.isChecked = false
            }
            mClassifyAdapter.setNewData(mNewHotClassify)
            mClassifyAdapter.notifyDataSetChanged()

            bean.isChecked = isChecked
            mOtherAdapter.notifyDataSetChanged()
            mSelectedLabel = null
            if (bean.isChecked) {
                mSelectedClassify = bean
//                group_select_label.visibility = View.VISIBLE
//                if (mNewLabelName.isEmpty()) {
//                    cb_item_select_label_new_label.visibility = View.GONE
////                    iv_select_label_new_label_delete.visibility = View.GONE
//                    iv_select_label_new_label.visibility = View.VISIBLE
//                } else {
//                    cb_item_select_label_new_label.visibility = View.VISIBLE
////                    iv_select_label_new_label_delete.visibility = View.VISIBLE
//                    iv_select_label_new_label.visibility = View.GONE
//                }
                bean.dataList.forEach {
                    it.isChecked = false
                }
                mLabelAdapter.setNewData(bean.dataList)
//                if (bean.dataList.size == 0) {
//                    tv_select_label_2.visibility = View.GONE
//                } else {
//                    tv_select_label_2.visibility = View.VISIBLE
//                }

                mSingleClassify = bean.title
                tv_toolbar_right.isEnabled = true
            } else {
                mSelectedClassify = null
//                group_select_label.visibility = View.GONE
//                tv_select_label_2.visibility = View.GONE
                cb_item_select_label_new_label.visibility = View.GONE
//                iv_select_label_new_label_delete.visibility = View.GONE
//                iv_select_label_new_label.visibility = View.GONE

                mSingleClassify = ""
                mSingleLable = ""
                tv_toolbar_right.isEnabled = false
            }
            if (mSingleClassify != "" && mSingleClassify.isNotEmpty() && mSelectedClassify != null) {
                tv_toolbar_right.isEnabled = true
//                mTheFirst = mSingleClassify
            } else {
                tv_toolbar_right.isEnabled = mSelectedClassify != null && (mSelectedLabel != null || mNewLabelSelect)
            }
            mSelectItem = -1

            ////////////////////////////////////
            //新的
        }
    }

}
