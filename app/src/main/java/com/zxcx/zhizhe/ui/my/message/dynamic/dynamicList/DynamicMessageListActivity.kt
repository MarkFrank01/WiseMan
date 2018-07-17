package com.zxcx.zhizhe.ui.my.message.dynamic.dynamicList

import android.os.Bundle
import android.support.v4.util.ArrayMap
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import com.chad.library.adapter.base.BaseQuickAdapter
import com.zxcx.zhizhe.R
import com.zxcx.zhizhe.event.ClearMessageEvent
import com.zxcx.zhizhe.mvpBase.MvpActivity
import com.zxcx.zhizhe.ui.article.articleDetails.ArticleDetailsActivity
import com.zxcx.zhizhe.ui.card.cardDetails.SingleCardDetailsActivity
import com.zxcx.zhizhe.ui.card.hot.CardBean
import com.zxcx.zhizhe.ui.my.message.system.message_collect
import com.zxcx.zhizhe.ui.my.message.system.message_comment
import com.zxcx.zhizhe.ui.my.message.system.message_follow
import com.zxcx.zhizhe.ui.my.message.system.message_like
import com.zxcx.zhizhe.ui.otherUser.OtherUserActivity
import com.zxcx.zhizhe.utils.Constants
import com.zxcx.zhizhe.utils.startActivity
import com.zxcx.zhizhe.widget.CustomLoadMoreView
import com.zxcx.zhizhe.widget.EmptyView
import io.reactivex.Flowable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.functions.Function
import io.reactivex.schedulers.Schedulers
import io.reactivex.subscribers.DisposableSubscriber
import kotlinx.android.synthetic.main.activity_follow_message.*
import kotlinx.android.synthetic.main.toolbar.*
import org.greenrobot.eventbus.EventBus
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

/**
 * 动态消息列表
 */

class DynamicMessageListActivity : MvpActivity<DynamicMessageListPresenter>(), DynamicMessageListContract.View,
		BaseQuickAdapter.RequestLoadMoreListener, BaseQuickAdapter.OnItemChildClickListener {

	private var mMessageType = 0
	private var mPage = 0
	private val mPageSize = Constants.PAGE_SIZE
	private lateinit var mAdapter: DynamicMessageAdapter
	private val map: ArrayMap<String, ArrayList<DynamicMessageListBean>> = ArrayMap()

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(R.layout.activity_follow_message)
		mMessageType = intent.getIntExtra("messageType", 0)
		when (mMessageType) {
			message_follow -> {
				initToolBar("关注")
			}
			message_comment -> {
				initToolBar("评论")
			}
			message_like -> {
				initToolBar("点赞")
			}
			message_collect -> {
				initToolBar("收藏")
			}
		}

		initRecyclerView()
		mPresenter.getDynamicMessageList(mMessageType, mPage, mPageSize)
	}

	override fun createPresenter(): DynamicMessageListPresenter {
		return DynamicMessageListPresenter(this)
	}

	override fun getDataSuccess(list: List<DynamicMessageListBean>) {
		mDisposable = Flowable.just(list)
				.observeOn(Schedulers.computation())
				.map(PackData(map))
				.observeOn(AndroidSchedulers.mainThread())
				.subscribeWith(object : DisposableSubscriber<List<DynamicBean>>() {
					override fun onError(t: Throwable?) {
					}

					override fun onComplete() {
					}

					override fun onNext(list: List<DynamicBean>) {
						mAdapter.data.clear()
						for (dynamicBean in list) {
							mAdapter.data.add(dynamicBean)
							mAdapter.data.addAll(dynamicBean.list)
						}
						mAdapter.notifyDataSetChanged()

						mPage++
						if (list.size < mPageSize) {
							mAdapter.loadMoreEnd(false)
						} else {
							mAdapter.loadMoreComplete()
							mAdapter.setEnableLoadMore(false)
							mAdapter.setEnableLoadMore(true)
						}
					}

				})
	}

	override fun postSuccess() {
		mAdapter.data.clear()
		mAdapter.notifyDataSetChanged()
		iv_toolbar_right.visibility = View.GONE
		EventBus.getDefault().post(ClearMessageEvent(mMessageType))
	}

	override fun postFail(msg: String?) {
		toastShow(msg)
	}

	override fun onLoadMoreRequested() {
		mPresenter.getDynamicMessageList(mMessageType, mPage, mPageSize)
	}

	override fun onItemChildClick(adapter: BaseQuickAdapter<*, *>?, view: View?, position: Int) {
		if (mAdapter.data[position].itemType == dynamic_content) {
			val bean = mAdapter.data[position] as DynamicMessageListBean
			when (view?.id) {
				R.id.iv_item_dynamic_message -> {
					startActivity(OtherUserActivity::class.java) {
						it.putExtra("id", bean.relatedUserId)
						it.putExtra("name", bean.relatedUserName)
					}
				}
				R.id.tv_item_dynamic_message_name -> {
					startActivity(OtherUserActivity::class.java) {
						it.putExtra("id", bean.relatedUserId)
						it.putExtra("name", bean.relatedUserName)
					}
				}
				R.id.tv_item_dynamic_message_content -> {
					val cardBean = CardBean()
					cardBean.id = bean.relatedCardId
					if (bean.relatedCardType == 1) {
						mActivity.startActivity(SingleCardDetailsActivity::class.java) {
							it.putExtra("cardBean", cardBean)
							it.putExtra("gotoComment", true)
						}
					} else {
						mActivity.startActivity(ArticleDetailsActivity::class.java) {
							it.putExtra("cardBean", cardBean)
							it.putExtra("gotoComment", true)
						}
					}
				}
				R.id.tv_item_dynamic_message_card -> {
					val cardBean = CardBean()
					cardBean.id = bean.relatedCardId
					if (bean.relatedCardType == 1) {
						mActivity.startActivity(SingleCardDetailsActivity::class.java) {
							it.putExtra("cardBean", cardBean)
						}
					} else {
						mActivity.startActivity(ArticleDetailsActivity::class.java) {
							it.putExtra("cardBean", cardBean)
						}
					}
				}
			}
		}
	}

	override fun setListener() {
	}

	private fun initRecyclerView() {
		mAdapter = DynamicMessageAdapter(ArrayList())
		mAdapter.onItemChildClickListener = this
		mAdapter.setLoadMoreView(CustomLoadMoreView())
		mAdapter.setOnLoadMoreListener(this, rv_follow_message)
		rv_follow_message.layoutManager = LinearLayoutManager(mActivity, LinearLayoutManager.VERTICAL, false)
		rv_follow_message.adapter = mAdapter
		val emptyView = EmptyView.getEmptyView(mActivity, "暂无消息", R.drawable.no_data)
		mAdapter.emptyView = emptyView
	}

	class PackData(private val map: ArrayMap<String, ArrayList<DynamicMessageListBean>>) : Function<List<DynamicMessageListBean>, List<DynamicBean>> {
		private val dateFormat = SimpleDateFormat("yyyy/MM/dd", Locale.CHINA)
		private val timeFormat = SimpleDateFormat("HH:mm", Locale.CHINA)
		override fun apply(list: List<DynamicMessageListBean>): List<DynamicBean> {
			for (bean in list) {
				val date = dateFormat.format(bean.date)
				val time = timeFormat.format(bean.date)
				bean.newTime = time
				if (map.containsKey(date)) {
					map[date]?.add(bean)
				} else {
					val newList: ArrayList<DynamicMessageListBean> = ArrayList()
					newList.add(bean)
					map[date] = newList
				}
			}
			val dynamicBeanList = ArrayList<DynamicBean>()
			for (mutableEntry in map) {
				mutableEntry.value.sortWith(Comparator { o1, o2 ->
					o2?.date?.compareTo(o1?.date)!!
				})
				val dynamicBean = DynamicBean(mutableEntry.key, mutableEntry.value)
				dynamicBeanList.add(dynamicBean)
			}
			dynamicBeanList.sortWith(Comparator { o1, o2 ->
				o2?.list?.get(0)?.date?.compareTo(o1?.list?.get(0)?.date)!!
			})
			return dynamicBeanList
		}
	}
}
