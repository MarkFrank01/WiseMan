package com.zxcx.zhizhe.ui.my.selectAttention

import android.os.Bundle
import android.support.v7.widget.GridLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import butterknife.ButterKnife
import com.chad.library.adapter.base.BaseQuickAdapter
import com.zxcx.zhizhe.R
import com.zxcx.zhizhe.event.SelectAttentionEvent
import com.zxcx.zhizhe.mvpBase.MvpActivity
import com.zxcx.zhizhe.ui.classify.ClassifyBean
import com.zxcx.zhizhe.ui.classify.ClassifyCardBean
import com.zxcx.zhizhe.ui.classify.ClassifyItemDecoration
import kotlinx.android.synthetic.main.activity_select_attention.*
import org.greenrobot.eventbus.EventBus

class SelectAttentionActivity : MvpActivity<SelectAttentionPresenter>(), SelectAttentionContract.View,
		BaseQuickAdapter.OnItemChildClickListener {

	private val mCheckedList = ArrayList<ClassifyCardBean>()
	private lateinit var footer: View
	private lateinit var mAdapter: SelectAttentionAdapter
	private lateinit var mTvStart: TextView

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(R.layout.activity_select_attention)
		ButterKnife.bind(this)
		initRecyclerView()
		initView()
		mPresenter.getClassify()
	}

	private fun initView() {
		val isInit = intent.getBooleanExtra("isInit", false)
		if (!isInit) {
			tv_select_attention_hint.text = "花一点点时间让我们更好的了解你"
		}
	}

	override fun initStatusBar() {

	}

	override fun createPresenter(): SelectAttentionPresenter {
		return SelectAttentionPresenter(this)
	}

	override fun getDataSuccess(list: MutableList<ClassifyBean>) {
		footer.visibility = View.VISIBLE
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
	}

	override fun setListener() {
		iv_select_attention_close.setOnClickListener {
			onBackPressed()
		}
	}

	private fun initRecyclerView() {
		footer = LayoutInflater.from(mActivity).inflate(R.layout.layout_footer_select_attention, null)
		mTvStart = footer.findViewById(R.id.tv_start)
		mTvStart.setOnClickListener {
			val idList = mutableListOf<Int>()
			mCheckedList.forEach {
				idList.add(it.id)
			}
			mPresenter.changeAttentionList(idList)
		}

		mAdapter = SelectAttentionAdapter(ArrayList())
		mAdapter.onItemChildClickListener = this
		footer.visibility = View.GONE
		mAdapter.addFooterView(footer)
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
		rv_select_attention.addItemDecoration(ClassifyItemDecoration())
	}
}
