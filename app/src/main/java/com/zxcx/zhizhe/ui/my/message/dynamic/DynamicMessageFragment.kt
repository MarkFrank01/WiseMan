package com.zxcx.zhizhe.ui.my.message.dynamic

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.zxcx.zhizhe.R
import com.zxcx.zhizhe.event.ClearMessageEvent
import com.zxcx.zhizhe.mvpBase.BaseFragment
import com.zxcx.zhizhe.mvpBase.BaseRxJava
import com.zxcx.zhizhe.mvpBase.IGetPresenter
import com.zxcx.zhizhe.retrofit.AppClient
import com.zxcx.zhizhe.retrofit.BaseSubscriber
import com.zxcx.zhizhe.ui.my.message.dynamic.dynamicList.DynamicMessageListActivity
import com.zxcx.zhizhe.ui.my.message.system.message_collect
import com.zxcx.zhizhe.ui.my.message.system.message_comment
import com.zxcx.zhizhe.ui.my.message.system.message_follow
import com.zxcx.zhizhe.ui.my.message.system.message_like
import com.zxcx.zhizhe.utils.StringUtils
import com.zxcx.zhizhe.utils.TextViewUtils
import kotlinx.android.synthetic.main.fragment_dynamic_message.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

/**
 * 动态消息
 */

class DynamicMessageFragment : BaseFragment(), IGetPresenter<DynamicMessageBean> {

	override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
		return inflater.inflate(R.layout.fragment_dynamic_message, container, false)
	}

	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)
		EventBus.getDefault().register(this)
		ll_dynamic_message_follow.setOnClickListener {
			//进入关注消息列表
			val intent = Intent(mActivity, DynamicMessageListActivity::class.java)
			intent.putExtra("messageType", message_follow)
			startActivity(intent)
		}
		ll_dynamic_message_comment.setOnClickListener {
			//进入评论消息列表
			val intent = Intent(mActivity, DynamicMessageListActivity::class.java)
			intent.putExtra("messageType", message_comment)
			startActivity(intent)
		}
		ll_dynamic_message_like.setOnClickListener {
			//进入点赞消息列表
			val intent = Intent(mActivity, DynamicMessageListActivity::class.java)
			intent.putExtra("messageType", message_like)
			startActivity(intent)
		}
		ll_dynamic_message_collect.setOnClickListener {
			//进入收藏消息列表
			val intent = Intent(mActivity, DynamicMessageListActivity::class.java)
			intent.putExtra("messageType", message_collect)
			startActivity(intent)
		}
	}

	override fun onResume() {
		super.onResume()
		getDynamicMessage()
	}

	override fun onDestroyView() {
		EventBus.getDefault().unregister(this)
		super.onDestroyView()
	}

	@Subscribe(threadMode = ThreadMode.MAIN)
	fun onMessageEvent(event: ClearMessageEvent) {
		when (event.messageType) {
			message_follow -> {
				tv_dynamic_message_follow.setText(R.string.tv_dynamic_message_no_data)
			}
			message_like -> {
				tv_dynamic_message_like.setText(R.string.tv_dynamic_message_no_data)
			}
			message_collect -> {
				tv_dynamic_message_collect.setText(R.string.tv_dynamic_message_no_data)
			}
		}
	}

	private fun getDynamicMessage() {
		mDisposable = AppClient.getAPIService().dynamicMessage
				.compose(BaseRxJava.io_main())
				.compose(BaseRxJava.handleResult())
				.subscribeWith(object : BaseSubscriber<DynamicMessageBean>(this) {
					override fun onNext(bean: DynamicMessageBean) {
						getDataSuccess(bean)
					}
				})
		addSubscription(mDisposable)
	}

	override fun getDataSuccess(bean: DynamicMessageBean) {
		bean.followerUserStr?.let {
			if (!StringUtils.isEmpty(bean.followerUserStr)) {
				val str = getString(R.string.tv_dynamic_message_follow, it)
				TextViewUtils.setTextViewColorBlue(tv_dynamic_message_follow, bean.followerUserStr, str)
			}
		}
		bean.likeUserStr?.let {
			if (!StringUtils.isEmpty(bean.likeUserStr)) {
				val str = getString(R.string.tv_dynamic_message_like, it)
				TextViewUtils.setTextViewColorBlue(tv_dynamic_message_like, bean.likeUserStr, str)
			}
		}
		bean.commentUserStr?.let {
			if (!StringUtils.isEmpty(bean.commentUserStr)) {
				val str = getString(R.string.tv_dynamic_message_comment, it)
				TextViewUtils.setTextViewColorBlue(tv_dynamic_message_comment, bean.commentUserStr, str)
			}
		}
		bean.collectedUserStr?.let {
			if (!StringUtils.isEmpty(bean.collectedUserStr)) {
				val str = getString(R.string.tv_dynamic_message_collect, it)
				TextViewUtils.setTextViewColorBlue(tv_dynamic_message_collect, bean.collectedUserStr, str)
			}
		}
	}

	override fun getDataFail(msg: String?) {
		toastShow(msg)
	}
}