package com.zxcx.zhizhe.loadCallback

import android.content.Context
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.TextView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.kingja.loadsir.callback.Callback
import com.zxcx.zhizhe.R
import com.zxcx.zhizhe.event.ChangeAttentionEvent
import com.zxcx.zhizhe.ui.classify.ClassifyBean
import com.zxcx.zhizhe.ui.classify.ClassifyCardBean
import com.zxcx.zhizhe.ui.my.selectAttention.SelectAttentionAdapter
import org.greenrobot.eventbus.EventBus

class SelectAttentionCallback(private val mList: List<ClassifyBean>) : Callback(), BaseQuickAdapter.OnItemChildClickListener {
	private lateinit var mAdapter: SelectAttentionAdapter
	private val mCheckedList = ArrayList<ClassifyCardBean>()
	private lateinit var mTvComplete: TextView
	private lateinit var mRvSelectAttention: RecyclerView
	private lateinit var mContext: Context

	override fun onCreateView(): Int {
		return R.layout.view_select_attention
	}

	override fun onAttach(context: Context, view: View) {
		super.onAttach(context, view)
		mContext = context
		mTvComplete = view.findViewById(R.id.tv_select_attention_complete)
		mRvSelectAttention = view.findViewById(R.id.rv_select_attention)
		initRecyclerView()
		for (bean in mList) {
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
		mTvComplete.isEnabled = mCheckedList.isNotEmpty()
		mTvComplete.setOnClickListener {
			EventBus.getDefault().post(ChangeAttentionEvent(mCheckedList))
		}
	}

	override fun onReloadEvent(context: Context, view: View): Boolean {
		return true
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
		mTvComplete.isEnabled = mCheckedList.isNotEmpty()
	}

	private fun initRecyclerView() {
		mAdapter = SelectAttentionAdapter(arrayListOf())
		mAdapter.onItemChildClickListener = this
		val manager = GridLayoutManager(mContext, 4)
		mRvSelectAttention.layoutManager = manager
		mRvSelectAttention.adapter = mAdapter
		manager.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
			override fun getSpanSize(position: Int): Int {
				return if (mAdapter.getItemViewType(position) == ClassifyCardBean.TYPE_CARD_BAG) {
					1
				} else {
					manager.spanCount
				}
			}
		}
	}
}
