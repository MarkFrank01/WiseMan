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

    //选择标签值
    private var labelName = ""
    //自定义标签值
    private var mSelectName: String = ""

    private var mSelectItem: Int = -1


    private var mTheFirst: String = ""
    private var mTheSecond: String = ""

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
        //二次获取
        labelName = intent.getStringExtra("labelName")
        classifyId = intent.getIntExtra("classifyId", 0)
        mSelectName = intent.getStringExtra("twoLabelName")

        //日志
        Log.e("Back", labelName + "!" + mSelectName)
        //是否可直接跳过
        if (labelName.isNotEmpty() || mSelectName.isNotEmpty()) {
            tv_toolbar_right.isEnabled = true
        }

        //必须置为第一个值，方便区分
        mTheFirst = labelName
        //必须置为第二个值，方便区分
        mTheSecond = mSelectName
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

        //判断不为空进行操作,存在疑问，bug来源
        if (isNewLabel && labelName.isNotEmpty()) {
            mNewLabelName = labelName
            iv_select_label_new_label.visibility = View.GONE
            iv_select_label_new_label_delete.visibility = View.VISIBLE
            cb_item_select_label_new_label.visibility = View.VISIBLE
            cb_item_select_label_new_label.text = labelName
            cb_item_select_label_new_label.isChecked = true

            mTheFirst = labelName
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
            intent.putExtra("labelName", mTheFirst)
            intent.putExtra("twoLabelName", mTheSecond)
            intent.putExtra("classifyId", mSelectedClassify?.id)

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

                mTheSecond = ""
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

                mTheSecond = it
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

    //先加载rv
    private fun initRecyclerView() {
        mClassifyAdapter = SelectClassifyAdapter(ArrayList())
        mLabelAdapter = SelectLabelAdapter(ArrayList())
        val manager = GridLayoutManager(mActivity, 4)
        val manager1 = GridLayoutManager(mActivity, 4)
        rv_select_classify.adapter = mClassifyAdapter
        rv_select_classify.layoutManager = manager
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


        //选择标签点击处理
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


            //尝试在这里获取值和回置值
            if (bean.isChecked) {
//                mSelectedLabel = bean
////                mNewLabelSelect = false
//                if (mNewLabelName.isNotEmpty()) {
//                    cb_item_select_label_new_label.isChecked = false
//                }

                mTheFirst = bean.name.toString()


            } else {
                mSelectedLabel = null
                mTheFirst = ""
            }

            tv_toolbar_right.isEnabled = mSelectedClassify != null && (mSelectedLabel != null || mNewLabelSelect)


            //只取第一个单值
            Log.e("label", mTheFirst + "!")
        }
    }
}
