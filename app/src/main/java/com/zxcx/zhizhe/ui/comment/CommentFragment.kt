package com.zxcx.zhizhe.ui.comment

import android.os.Bundle
import android.support.design.widget.BottomSheetBehavior
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.TextView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.entity.MultiItemEntity
import com.zxcx.zhizhe.R
import com.zxcx.zhizhe.mvpBase.MvpFragment
import com.zxcx.zhizhe.utils.SVTSConstants
import com.zxcx.zhizhe.utils.SharedPreferencesUtil
import com.zxcx.zhizhe.utils.Utils
import com.zxcx.zhizhe.utils.afterTextChanged
import com.zxcx.zhizhe.widget.CommentLoadMoreView
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
		mPresenter.getComment(cardId, mPage)
	}

	override fun onResume() {
		super.onResume()
		val userId = SharedPreferencesUtil.getInt(SVTSConstants.userId, 0)
		mAdapter.userId = userId
		mAdapter.notifyDataSetChanged()
	}

	private fun initBottomSheet() {
		behavior = BottomSheetBehavior.from(cl_fragment_comment)
		behavior.setBottomSheetCallback(object : BottomSheetBehavior.BottomSheetCallback() {
			override fun onSlide(bottomSheet: View, slideOffset: Float) {

			}

			override fun onStateChanged(bottomSheet: View, newState: Int) {
				if (newState == BottomSheetBehavior.STATE_HIDDEN) {
					Utils.closeInputMethod(et_comment)
					mActivity.onBackPressed()
				} else if (newState == BottomSheetBehavior.STATE_COLLAPSED) {
					behavior.state = BottomSheetBehavior.STATE_EXPANDED
				}
			}
		})
		behavior.state = BottomSheetBehavior.STATE_EXPANDED
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

	override fun postSuccess(bean: CommentBean) {
		if (parentCommentId == null) {
			mAdapter.addData(bean)
		} else {
			var parentBean = CommentBean()
			parentBean.id = parentCommentId ?: 0
			val position = mAdapter.data.indexOf(parentBean)
			parentBean = mAdapter.data[position] as CommentBean
			parentBean.childCommentList.add(bean.toChildCommentBean())
			mAdapter.notifyItemChanged(position)
		}
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
			if (!checkLogin()) {
				return
			}
			bean = bean as CommentBean
			Utils.showInputMethod(et_comment)
			rv_comment.scrollToPosition(position)
			parentCommentId = bean.id
			et_comment.hint = getString(R.string.et_comment_hint, bean.userName)
		}
	}

	override fun onItemChildClick(adapter: BaseQuickAdapter<*, *>, view: View, position: Int) {
		if (!checkLogin()) {
			return
		}
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
				val tvLikeNm = adapter.getViewByPosition(position, R.id.tv_item_comment_like_num) as TextView
				tvLikeNm.text = (tvLikeNm.text.toString().toInt() + 1).toString()
				mPresenter.likeComment(commentId)
			} else {
				val tvLikeNm = adapter.getViewByPosition(position, R.id.tv_item_comment_like_num) as TextView
				tvLikeNm.text = (tvLikeNm.text.toString().toInt() - 1).toString()
				mPresenter.unlikeComment(commentId)
			}
		}
	}

	override fun setListener() {
		super.setListener()
		iv_comment_close.setOnClickListener {
			Utils.closeInputMethod(et_comment)
			mActivity.onBackPressed()
		}
		et_comment.afterTextChanged {
			tv_comment_send.isEnabled = it.isNotEmpty()
		}
		et_comment.setOnClickListener {
			if (!checkLogin()) {
				Utils.closeInputMethod(et_comment)
			}
		}
		tv_comment_send.setOnClickListener {
			if (checkLogin()) {
				mPresenter.sendComment(cardId, parentCommentId, et_comment.text.toString())
			}
			Utils.closeInputMethod(et_comment)
		}
		fl_fragment_comment.setOnClickListener {
			parentCommentId = null
			et_comment.hint = "表达是智慧的体现"
		}
	}

	private fun initRecyclerView() {
		val layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
		mAdapter = CommentAdapter(arrayListOf())
		mAdapter.setLoadMoreView(CommentLoadMoreView())
		mAdapter.setOnLoadMoreListener(this, rv_comment)
		mAdapter.onItemClickListener = this
		mAdapter.onItemChildClickListener = this
		rv_comment.layoutManager = layoutManager
		rv_comment.adapter = mAdapter
	}


}