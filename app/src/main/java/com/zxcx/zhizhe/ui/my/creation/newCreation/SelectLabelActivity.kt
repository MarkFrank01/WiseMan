package com.zxcx.zhizhe.ui.my.creation.newCreation

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
import com.zxcx.zhizhe.ui.my.selectAttention.ClassifyBean
import com.zxcx.zhizhe.ui.my.selectAttention.ClassifyCardBean
import com.zxcx.zhizhe.ui.my.selectAttention.SelectAttentionContract
import com.zxcx.zhizhe.ui.my.selectAttention.SelectAttentionPresenter
import kotlinx.android.synthetic.main.activity_select_label.*
import kotlinx.android.synthetic.main.toolbar.*

/**
 * 创作-选择标签页面
 */

class SelectLabelActivity : MvpActivity<SelectAttentionPresenter>(), SelectAttentionContract.View {

    private var mSelectedClassify: ClassifyBean? = null
    private var mSelectedLabel: ClassifyCardBean? = null
    private var mNewLabelSelect: Boolean = false
    private var mNewLabelName: String = ""
    private lateinit var mClassifyAdapter: SelectClassifyAdapter
    private lateinit var mLabelAdapter: SelectLabelAdapter

    private var classifyId = 0

    private var labelName = ""
    private var mSelectName: String = ""

    private var mSelectItem: Int = -1


    private var FristLable: String = ""
    private var SecondLable: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_select_label)
        ButterKnife.bind(this)
        initRecyclerView()
        initData()
        initView()
        mPresenter.getClassify()
    }

    private fun initData() {
        labelName = intent.getStringExtra("labelName")
        classifyId = intent.getIntExtra("classifyId", 0)
        mSelectName = intent.getStringExtra("twoLabelName")
        Log.e("Back", labelName + "!" + mSelectName)
        if (labelName.isNotEmpty()) {
            tv_toolbar_right.isEnabled = true
        }

        FristLable = labelName
        SecondLable = mSelectName
    }

    override fun createPresenter(): SelectAttentionPresenter {
        return SelectAttentionPresenter(this)
    }

    override fun getDataSuccess(list: MutableList<ClassifyBean>) {
        var isNewLabel = true
        list.forEach {
            it.dataList.forEach {
                if (it.name == labelName) {
                    it.isChecked = true
                    mSelectedLabel = it
                    mNewLabelSelect = false
                    isNewLabel = false
                } else {
                    it.isChecked = false
                }


            }
            if (it.id == classifyId) {
                it.isChecked = true
                mSelectedClassify = it
                group_select_label.visibility = View.VISIBLE
                mLabelAdapter.setNewData(it.dataList)
            }
        }
        mClassifyAdapter.setNewData(list)
        if (isNewLabel && labelName.isNotEmpty()) {
            mNewLabelName = labelName
            iv_select_label_new_label.visibility = View.GONE
            iv_select_label_new_label_delete.visibility = View.VISIBLE
            cb_item_select_label_new_label.visibility = View.VISIBLE
            cb_item_select_label_new_label.text = labelName
            cb_item_select_label_new_label.isChecked = true
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
            intent.putExtra("labelName", if (mNewLabelSelect) mNewLabelName else mSelectedLabel?.name)
            intent.putExtra("classifyId", mSelectedClassify?.id)
            intent.putExtra("twoLabelName", mSelectName)
            setResult(Activity.RESULT_OK, intent)
            finish()
        }
        iv_select_label_new_label_delete.setOnClickListener {
            val dialog = DeleteNewLabelDialog()
            dialog.mListener = {
                mNewLabelName = ""
                iv_select_label_new_label.visibility = View.VISIBLE
                iv_select_label_new_label_delete.visibility = View.GONE
                cb_item_select_label_new_label.visibility = View.GONE
                cb_item_select_label_new_label.text = ""
                cb_item_select_label_new_label.isChecked = false
            }
            dialog.show(supportFragmentManager, "")
        }
        iv_select_label_new_label.setOnClickListener {
            val dialog = NewLabelDialog()
            dialog.mListener = {
                mNewLabelName = it
                iv_select_label_new_label.visibility = View.GONE
                iv_select_label_new_label_delete.visibility = View.VISIBLE
                cb_item_select_label_new_label.visibility = View.VISIBLE
                cb_item_select_label_new_label.text = it
                cb_item_select_label_new_label.isChecked = true
            }
            dialog.show(supportFragmentManager, "")
        }
        cb_item_select_label_new_label.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                mSelectedLabel = null
                mLabelAdapter.data.forEach {
                    it as ClassifyCardBean
                    it.isChecked = false
                }
                if (mSelectItem != -1) {
                    mLabelAdapter.data[mSelectItem].isChecked = true
                }
                mLabelAdapter.notifyDataSetChanged()
            }
            mNewLabelSelect = isChecked
            tv_toolbar_right.isEnabled = mSelectedClassify != null && (mSelectedLabel != null || mNewLabelSelect)
        }
    }

    private fun initRecyclerView() {
        mClassifyAdapter = SelectClassifyAdapter(ArrayList())
        mLabelAdapter = SelectLabelAdapter(ArrayList())
        val manager = GridLayoutManager(mActivity, 4)
        val manager1 = GridLayoutManager(mActivity, 4)
        rv_select_classify.adapter = mClassifyAdapter
        rv_select_classify.layoutManager = manager
        rv_select_label.adapter = mLabelAdapter
        rv_select_label.layoutManager = manager1
        mClassifyAdapter.setOnItemChildClickListener { adapter, view, position ->
            val bean = adapter.data[position] as ClassifyBean
            val isChecked = !bean.isChecked
            adapter.data.forEach {
                it as ClassifyBean
                it.isChecked = false
            }
            bean.isChecked = isChecked
            mClassifyAdapter.notifyDataSetChanged()
            mSelectedLabel = null
            if (bean.isChecked) {
                mSelectedClassify = bean
                group_select_label.visibility = View.VISIBLE
                if (mNewLabelName.isEmpty()) {
                    cb_item_select_label_new_label.visibility = View.GONE
                    iv_select_label_new_label_delete.visibility = View.GONE
                    iv_select_label_new_label.visibility = View.VISIBLE
                } else {
                    cb_item_select_label_new_label.visibility = View.VISIBLE
                    iv_select_label_new_label_delete.visibility = View.VISIBLE
                    iv_select_label_new_label.visibility = View.GONE
                }
                bean.dataList.forEach {
                    it.isChecked = false
                }
                mLabelAdapter.setNewData(bean.dataList)
            } else {
                mSelectedClassify = null
                group_select_label.visibility = View.GONE
                cb_item_select_label_new_label.visibility = View.GONE
                iv_select_label_new_label_delete.visibility = View.GONE
                iv_select_label_new_label.visibility = View.GONE
            }
            tv_toolbar_right.isEnabled = mSelectedClassify != null && (mSelectedLabel != null || mNewLabelSelect)
            mSelectItem = -1
        }
        mLabelAdapter.setOnItemChildClickListener { adapter, view, position ->
            val bean = adapter.data[position] as ClassifyCardBean
            val isClick = !bean.isChecked

            if (!isClick) {
                mSelectItem = -1
                mSelectName = ""
            }

            adapter.data.forEach {
                it as ClassifyCardBean
                it.isChecked = false
            }


            if (mSelectItem != -1) {
                val lastBean = adapter.data[mSelectItem] as ClassifyCardBean
                mSelectName = lastBean.name.toString()
                Log.e("NewName", mSelectName + "!")
            }

            if (mSelectItem != position) {
                mSelectItem = position
            }

            bean.isChecked = isClick
            mLabelAdapter.notifyDataSetChanged()



            if (bean.isChecked) {
                mSelectedLabel = bean
//                mNewLabelSelect = false
                if (mNewLabelName.isNotEmpty()) {
                    cb_item_select_label_new_label.isChecked = false
                }
            } else {
                mSelectedLabel = null
            }
            tv_toolbar_right.isEnabled = mSelectedClassify != null && (mSelectedLabel != null || mNewLabelSelect)


        }
    }
}
