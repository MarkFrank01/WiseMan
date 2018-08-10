package com.zxcx.zhizhe.ui.rank

import android.content.Intent
import android.os.Bundle
import android.support.v4.view.ViewPager
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.chad.library.adapter.base.BaseQuickAdapter
import com.kingja.loadsir.callback.Callback
import com.kingja.loadsir.core.LoadSir
import com.youth.banner.BannerConfig
import com.zxcx.zhizhe.R
import com.zxcx.zhizhe.event.LoginEvent
import com.zxcx.zhizhe.event.LogoutEvent
import com.zxcx.zhizhe.mvpBase.MvpFragment
import com.zxcx.zhizhe.ui.loginAndRegister.login.LoginActivity
import com.zxcx.zhizhe.ui.otherUser.OtherUserActivity
import com.zxcx.zhizhe.ui.rank.moreRank.AllRankActivity
import com.zxcx.zhizhe.ui.welcome.ADBean
import com.zxcx.zhizhe.ui.welcome.WebViewActivity
import com.zxcx.zhizhe.utils.*
import kotlinx.android.synthetic.main.fragment_rank.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

/**
 * 首页-榜单页面
 */

class RankFragment : MvpFragment<RankPresenter>(), RankContract.View, BaseQuickAdapter.OnItemClickListener, Callback.OnReloadListener {

	private var mAdList: MutableList<ADBean> = mutableListOf()
	private val imageList: MutableList<String> = mutableListOf()
	private var mUserId: Int = 0
	private lateinit var mAdapter: RankAdapter

	override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
		val root = inflater.inflate(R.layout.fragment_rank, container, false)
		loadService = LoadSir.getDefault().register(root, this)
		EventBus.getDefault().register(this)
		return loadService.loadLayout
	}

	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)
		initRecyclerView()
		initView()
		onRefresh()
	}

	override fun createPresenter(): RankPresenter {
		return RankPresenter(this)
	}

	override fun onStart() {
		super.onStart()
		//开始轮播
		banner_rank.startAutoPlay()
	}

	override fun onStop() {
		super.onStop()
		//结束轮播
		banner_rank.stopAutoPlay()
	}

	override fun onDestroy() {
		EventBus.getDefault().unregister(this)
		super.onDestroy()
	}

	override fun onReload(v: View?) {
		onRefresh()
	}

	private fun onRefresh() {
		mUserId = SharedPreferencesUtil.getInt(SVTSConstants.userId, 0)
		if (mUserId != 0) {
			mPresenter.getMyRank()
		}
		mPresenter.getTopTenRank()
		mPresenter.getAD()
	}

	override fun getMyRankSuccess(bean: UserRankBean) {
		loadService.showSuccess()

		tv_item_rank_user_name.text = bean.name
		tv_item_rank_user_card.text = bean.cardNum.toString()
		tv_item_rank_user_fans.text = bean.fansNum.toString()
		tv_item_rank_user_like.text = bean.likeNum.toString()
		tv_item_rank_user_collect.text = bean.collectNum.toString()
		tv_item_rank_user_rank.text = bean.rankIndex.toString()
		tv_item_rank_user_level.text = getString(R.string.tv_level, bean.level)
		val imageUrl = ZhiZheUtils.getHDImageUrl(bean.imageUrl)
		ImageLoader.load(mActivity, imageUrl, R.drawable.default_header, iv_item_rank_user)

		when {
			bean.rankChange > 0 -> {
				TextViewUtils.setTextLeftDrawable(mActivity, R.drawable.tv_rank_up, tv_my_rank_change)
				tv_my_rank_change.text = bean.rankChange.toString()
			}
			bean.rankChange == 0 -> {
				TextViewUtils.setTextLeftDrawable(mActivity, R.drawable.tv_rank_invariability, tv_my_rank_change)
				tv_my_rank_change.text = ""
			}
			else -> {
				TextViewUtils.setTextLeftDrawable(mActivity, R.drawable.tv_rank_down, tv_my_rank_change)
				tv_my_rank_change.text = Math.abs(bean.rankChange).toString()
			}
		}
	}

	override fun getDataSuccess(list: List<UserRankBean>) {
		loadService.showSuccess()
		mAdapter.setNewData(list)
		initView()
	}

	override fun getADSuccess(list: MutableList<ADBean>) {
		loadService.showSuccess()
		if (list.size > 0) {
			mAdList = list
			mAdList.forEach {
				imageList.add(it.titleImage)
			}
			fl_banner_rank.visibility = View.VISIBLE
		} else {
			fl_banner_rank.visibility = View.GONE
		}
		banner_rank.setImages(imageList)
		banner_rank.start()
	}

	@Subscribe(threadMode = ThreadMode.MAIN)
	fun onMessageEvent(event: LoginEvent) {
		refreshView()
		onRefresh()
	}

	@Subscribe(threadMode = ThreadMode.MAIN)
	fun onMessageEvent(event: LogoutEvent) {
		refreshView()
	}

	private fun gotoMoreRank() {
		val intent = Intent(mActivity, AllRankActivity::class.java)
		startActivity(intent)
	}

	override fun startLogin() {
		ZhiZheUtils.logout()
		toastShow(R.string.login_timeout)
		startActivity(Intent(mActivity, LoginActivity::class.java))
	}

	override fun onItemClick(adapter: BaseQuickAdapter<*, *>, view: View, position: Int) {
		val bean = adapter.data[position] as UserRankBean
		val intent = Intent(mActivity, OtherUserActivity::class.java)
		intent.putExtra("id", bean.id)
		intent.putExtra("name", bean.name)
		startActivity(intent)
	}

	override fun setListener() {
		tv_rank_more_rank.setOnClickListener { gotoMoreRank() }
		tv_rank_need_login.setOnClickListener { mActivity.startActivity(LoginActivity::class.java) {} }
	}

	private fun initRecyclerView() {
		mAdapter = RankAdapter(ArrayList())
		mAdapter.onItemClickListener = this
		rv_rank.layoutManager = LinearLayoutManager(mActivity, LinearLayoutManager.VERTICAL, false)
		rv_rank.adapter = mAdapter
		val footer = LayoutInflater.from(mActivity).inflate(R.layout.layout_footer_rank, null)
		mAdapter.addFooterView(footer)
	}

	private fun initView() {
		refreshView()

		banner_rank.setImageLoader(GlideBannerImageLoader())
		banner_rank.setIndicatorGravity(BannerConfig.RIGHT)
		banner_rank.setOnBannerListener {
			val adUrl = mAdList[it].behavior
			val adTitle = mAdList[it].description
			val adImage = mAdList[it].titleImage
			mActivity.startActivity(WebViewActivity::class.java) {
				it.putExtra("isAD", true)
				it.putExtra("title", adTitle)
				it.putExtra("url", adUrl)
				it.putExtra("imageUrl", adImage)
			}
		}
		banner_rank.setOnPageChangeListener(object : ViewPager.OnPageChangeListener {
			override fun onPageScrollStateChanged(state: Int) {

			}

			override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {

			}

			override fun onPageSelected(position: Int) {
				val newPosition = position % 3
				val ad = mAdList[newPosition]
				when (ad.styleType) {
					0 -> {
						iv_ad_label.setImageResource(R.drawable.iv_ad_label_0)
					}
					1 -> {
						iv_ad_label.setImageResource(R.drawable.iv_ad_label_1)
					}
					2 -> {
						iv_ad_label.setImageResource(R.drawable.iv_ad_label_2)
					}
				}
			}

		})
	}

	private fun refreshView() {
		mUserId = SharedPreferencesUtil.getInt(SVTSConstants.userId, 0)
		if (mUserId == 0) {
			tv_rank_need_login.visibility = View.VISIBLE
			tv_my_rank_change.visibility = View.GONE
		} else {
			tv_rank_need_login.visibility = View.GONE
			tv_my_rank_change.visibility = View.VISIBLE
		}
	}
}