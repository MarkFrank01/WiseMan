package com.zxcx.zhizhe.ui.my.selectAttention

import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v7.widget.GridLayoutManager
import android.view.View
import butterknife.ButterKnife
import com.chad.library.adapter.base.BaseQuickAdapter
import com.zxcx.zhizhe.R
import com.zxcx.zhizhe.event.SelectAttentionEvent
import com.zxcx.zhizhe.mvpBase.MvpActivity
import kotlinx.android.synthetic.main.activity_select_attention.*
import kotlinx.android.synthetic.main.toolbar.*
import org.greenrobot.eventbus.EventBus

class SelectAttentionActivity : MvpActivity<SelectAttentionPresenter>(), SelectAttentionContract.View,
		BaseQuickAdapter.OnItemChildClickListener {

	private val mCheckedList = ArrayList<ClassifyCardBean>()
	private lateinit var mAdapter: SelectAttentionAdapter

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(R.layout.activity_select_attention)
		ButterKnife.bind(this)
		initRecyclerView()
		initView()
		mPresenter.getClassify()
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
			val idList = mutableListOf<Int>()
			mCheckedList.forEach {
				idList.add(it.id)
			}
			mPresenter.changeAttentionList(idList)
		}
	}

	override fun createPresenter(): SelectAttentionPresenter {
		return SelectAttentionPresenter(this)
	}

	override fun getDataSuccess(list: MutableList<ClassifyBean>) {
		for (bean in list) {
			mAdapter.data.add(bean)
			for (bagBean in bean.dataList) {
				if (bagBean.isChecked) {
					if (!mCheckedList.contains(bagBean)) {
						mCheckedList.add(bagBean)
					}
				}
				mAdapter.data.add(bagBean)
			}
		}
		mAdapter.notifyDataSetChanged()
		tv_toolbar_right.isEnabled = mCheckedList.isNotEmpty()
	}

	override fun postSuccess() {
		EventBus.getDefault().post(SelectAttentionEvent())
		toastShow(R.string.user_info_change)
		finish()
	}

	override fun postFail(msg: String) {
		toastShow(msg)
	}

	override fun onItemChildClick(adapter: BaseQuickAdapter<*, *>, view: View?, position: Int) {
		val bean = adapter.data[position] as ClassifyCardBean
		bean.isChecked = !bean.isChecked
		mAdapter.notifyItemChanged(position)

		if (bean.isChecked) {
			if (!mCheckedList.contains(bean)) {
				mCheckedList.add(bean)
			}
		} else {
			if (mCheckedList.contains(bean)) {
				mCheckedList.remove(bean)
			}
		}
		tv_toolbar_right.isEnabled = mCheckedList.isNotEmpty()
	}

	private fun initRecyclerView() {
		mAdapter = SelectAttentionAdapter(ArrayList())
		mAdapter.onItemChildClickListener = this
		val manager = GridLayoutManager(mActivity, 4)
		manager.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
			override fun getSpanSize(position: Int): Int {
				return if (mAdapter.getItemViewType(position) == ClassifyCardBean.TYPE_CARD_BAG) {
					1
				} else {
					manager.spanCount
				}
			}
		}
		rv_select_attention.adapter = mAdapter
		rv_select_attention.layoutManager = manager
	}
}
