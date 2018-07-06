package com.zxcx.zhizhe.ui.comment

import android.os.Bundle
import android.support.design.widget.BottomSheetBehavior
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.entity.MultiItemEntity
import com.zxcx.zhizhe.R
import com.zxcx.zhizhe.mvpBase.MvpFragment
import com.zxcx.zhizhe.utils.Utils
import com.zxcx.zhizhe.utils.afterTextChanged
import com.zxcx.zhizhe.widget.CustomLoadMoreView
import kotlinx.android.synthetic.main.fragment_comment.*

class CommentFragment : MvpFragment<CommentPresenter>(), CommentContract.View,
		BaseQuickAdapter.RequestLoadMoreListener, BaseQuickAdapter.OnItemClickListener,
		BaseQuickAdapter.OnItemChildClickListener {

	private lateinit var mAdapter: CommentAdapter
	private var mPage = 0
	private var cardId = 0
	private var parentCommentId: Int? = null
	private lateinit var behavior: BottomSheetBehavior<View>

	override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
	                          savedInstanceState: Bundle?): View? {
		return inflater.inflate(R.layout.fragment_comment, container, false)
	}

	override fun createPresenter(): CommentPresenter {
		return CommentPresenter(this)
	}

	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)
		cardId = arguments?.getInt("cardId") ?: 0
		initRecyclerView()
		initBottomSheet()
	}

	private fun initBottomSheet() {
		behavior = BottomSheetBehavior.from(fl_fragment_comment)
		behavior.state = BottomSheetBehavior.STATE_EXPANDED
		behavior.skipCollapsed = true
		behavior.setBottomSheetCallback(object : BottomSheetBehavior.BottomSheetCallback() {
			override fun onSlide(bottomSheet: View, slideOffset: Float) {

			}

			override fun onStateChanged(bottomSheet: View, newState: Int) {
				if (newState == BottomSheetBehavior.STATE_HIDDEN) {
					mActivity.onBackPressed()
				}
			}

		})
	}

	override fun getDataSuccess(list: MutableList<CommentBean>) {

		if (mPage == 0) {
			mAdapter.setNewData(list as List<MultiItemEntity>)
		} else {
			mAdapter.addData(list)
		}
		mPage++
		if (list.isEmpty()) {
			mAdapter.loadMoreEnd(false)
		} else {
			mAdapter.loadMoreComplete()
			mAdapter.setEnableLoadMore(false)
			mAdapter.setEnableLoadMore(true)
		}
	}

	override fun postSuccess() {
		mPage = 0
		mPresenter.getComment(cardId, mPage)
	}

	override fun postFail(msg: String?) {
		toastFail(msg)
	}

	override fun likeSuccess() {

	}

	override fun unlikeSuccess() {

	}

	override fun onLoadMoreRequested() {
		mPresenter.getComment(cardId, mPage)
	}

	override fun onItemClick(adapter: BaseQuickAdapter<*, *>, view: View, position: Int) {
		var bean = adapter.getItem(position) as MultiItemEntity
		if (bean.itemType == CommentBean.TYPE_LEVEL_0) {
			bean = bean as CommentBean
			Utils.showInputMethod(et_comment)
			rv_comment.scrollToPosition(position)
			parentCommentId = bean.id
			et_comment.hint = getString(R.string.et_comment_hint, bean.userName)
		}
	}

	override fun onItemChildClick(adapter: BaseQuickAdapter<*, *>, view: View, position: Int) {
		var bean = adapter.getItem(position) as MultiItemEntity
		val commentId: Int
		if (bean.itemType == CommentBean.TYPE_LEVEL_0) {
			bean = bean as CommentBean
			commentId = bean.id
		} else {
			bean = bean as ChildCommentBean
			commentId = bean.id
		}
		if (view.id == R.id.cb_item_comment_like) {
			val checkBox = view as CheckBox
			if (checkBox.isChecked) {
				mPresenter.likeComment(commentId)
			} else {
				mPresenter.unlikeComment(commentId)
			}
		}
	}

	override fun setListener() {
		super.setListener()
		iv_comment_close.setOnClickListener {
			mActivity.onBackPressed()
		}
		et_comment.afterTextChanged {
			tv_comment_send.isEnabled = it.isNotEmpty()
		}
		tv_comment_send.setOnClickListener {
			mPresenter.sendComment(cardId, parentCommentId, et_comment.text.toString())
		}
		fl_fragment_comment.setOnClickListener {
			parentCommentId = null
			et_comment.hint = "表达是智慧的体现"
		}
	}

	private fun initRecyclerView() {
		val layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
		mAdapter = CommentAdapter(arrayListOf())
		mAdapter.setLoadMoreView(CustomLoadMoreView())
		mAdapter.setOnLoadMoreListener(this, rv_comment)
		mAdapter.onItemClickListener = this
		rv_comment.layoutManager = layoutManager
		rv_comment.adapter = mAdapter
	}


}