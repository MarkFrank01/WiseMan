package com.zxcx.zhizhe.ui.my.creation.newCreation

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v7.widget.GridLayoutManager
import android.view.View
import butterknife.ButterKnife
import com.zxcx.zhizhe.R
import com.zxcx.zhizhe.event.SelectAttentionEvent
import com.zxcx.zhizhe.mvpBase.MvpActivity
import com.zxcx.zhizhe.ui.my.selectAttention.ClassifyBean
import com.zxcx.zhizhe.ui.my.selectAttention.ClassifyCardBean
import com.zxcx.zhizhe.ui.my.selectAttention.SelectAttentionContract
import com.zxcx.zhizhe.ui.my.selectAttention.SelectAttentionPresenter
import kotlinx.android.synthetic.main.activity_select_label.*
import kotlinx.android.synthetic.main.toolbar.*
import org.greenrobot.eventbus.EventBus

class SelectLabelActivity : MvpActivity<SelectAttentionPresenter>(), SelectAttentionContract.View {

	private var mSelectedClassify: ClassifyBean? = null
	private var mSelectedLabel: ClassifyCardBean? = null
	private var mNewLabelSelect: Boolean = false
	private lateinit var mNewLabelName: String
	private lateinit var mClassifyAdapter: SelectClassifyAdapter
	private lateinit var mLabelAdapter: SelectLabelAdapter

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(R.layout.activity_select_label)
		ButterKnife.bind(this)
		initRecyclerView()
		initView()
		mPresenter.getClassify()
	}

	override fun createPresenter(): SelectAttentionPresenter {
		return SelectAttentionPresenter(this)
	}

	override fun getDataSuccess(list: MutableList<ClassifyBean>) {
		list.forEach {
			it.dataList.forEach {
				it.isChecked = false
			}
		}
		mClassifyAdapter.setNewData(list)
	}

	override fun postSuccess() {
		EventBus.getDefault().post(SelectAttentionEvent())
		toastShow(R.string.user_info_change)
		finish()
	}

	override fun postFail(msg: String) {
		toastShow(msg)
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
			setResult(Activity.RESULT_OK, intent)
			finish()
		}
		iv_select_label_new_label_delete.setOnClickListener {
			val dialog = DeleteNewLabelDialog()
			dialog.mListener = {
				iv_select_label_new_label.visibility = View.VISIBLE
				iv_select_label_new_label_delete.visibility = View.GONE
				cb_item_select_label_new_label.visibility = View.GONE
				mNewLabelSelect = false
			}
			dialog.show(supportFragmentManager, "")
		}
		iv_select_label_new_label.setOnClickListener {
			val dialog = NewLabelDialog()
			dialog.mListener = {
				mNewLabelName = it
				cb_item_select_label_new_label.text = it
				cb_item_select_label_new_label.isChecked = true
			}
			dialog.show(supportFragmentManager, "")
		}
		cb_item_select_label_new_label.setOnCheckedChangeListener { buttonView, isChecked ->
			if (isChecked) {
				mNewLabelSelect = true
				mSelectedLabel = null
				mLabelAdapter.data.forEach {
					it as ClassifyCardBean
					it.isChecked = false
				}
				mLabelAdapter.notifyDataSetChanged()
			} else {
				mNewLabelSelect = false
			}
			tv_toolbar_right.isEnabled = mSelectedClassify != null && (mSelectedLabel != null || mNewLabelSelect)
		}
	}

	private fun initRecyclerView() {
		mClassifyAdapter = SelectClassifyAdapter(ArrayList())
		mLabelAdapter = SelectLabelAdapter(ArrayList())
		val manager = GridLayoutManager(mActivity, 4)
		rv_select_classify.adapter = mClassifyAdapter
		rv_select_classify.layoutManager = manager
		rv_select_label.adapter = mLabelAdapter
		rv_select_label.layoutManager = manager
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
				mLabelAdapter.setNewData(bean.dataList)
			} else {
				mSelectedClassify = null
				group_select_label.visibility = View.GONE
			}
			tv_toolbar_right.isEnabled = mSelectedClassify != null && (mSelectedLabel != null || mNewLabelSelect)
		}
		mLabelAdapter.setOnItemChildClickListener { adapter, view, position ->
			val bean = adapter.data[position] as ClassifyCardBean
			val isChecked = !bean.isChecked
			adapter.data.forEach {
				it as ClassifyCardBean
				it.isChecked = false
			}
			bean.isChecked = isChecked
			mLabelAdapter.notifyDataSetChanged()

			if (bean.isChecked) {
				mSelectedLabel = bean
				mNewLabelSelect = false
			} else {
				mSelectedLabel = null
			}
			tv_toolbar_right.isEnabled = mSelectedClassify != null && (mSelectedLabel != null || mNewLabelSelect)
		}
	}
}
