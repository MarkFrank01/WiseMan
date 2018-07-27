package com.zxcx.zhizhe.ui

import android.app.SharedElementCallback
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.View
import com.zxcx.zhizhe.R
import com.zxcx.zhizhe.event.ChangeNightModeEvent
import com.zxcx.zhizhe.event.HomeClickRefreshEvent
import com.zxcx.zhizhe.mvpBase.BaseActivity
import com.zxcx.zhizhe.ui.article.HomeArticleFragment
import com.zxcx.zhizhe.ui.card.HomeCardFragment
import com.zxcx.zhizhe.ui.loginAndRegister.login.LoginActivity
import com.zxcx.zhizhe.ui.my.MyFragment
import com.zxcx.zhizhe.ui.my.creation.CreationAgreementDialog
import com.zxcx.zhizhe.ui.my.creation.newCreation.CreationEditorActivity
import com.zxcx.zhizhe.ui.my.writer_status_writer
import com.zxcx.zhizhe.ui.rank.RankFragment
import com.zxcx.zhizhe.ui.welcome.WebViewActivity
import com.zxcx.zhizhe.utils.SVTSConstants
import com.zxcx.zhizhe.utils.SharedPreferencesUtil
import com.zxcx.zhizhe.utils.startActivity
import kotlinx.android.synthetic.main.activity_main.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

class MainActivity : BaseActivity() {

	private var mCurrentFragment = Fragment()
	private var mHomeCardFragment = HomeCardFragment()
	private var mHomeArticleFragment = HomeArticleFragment()
	private var mRankFragment = RankFragment()
	private var mMyFragment: MyFragment? = MyFragment()
	private var mIsReenter = false

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(R.layout.activity_main)
		EventBus.getDefault().register(this)
		initShareElement()
		val intent = intent
		//判断是否点击了广告或通知
		gotoLoginActivity(intent)
		gotoADActivity(intent)
		if (intent.getBooleanExtra("isNight", false)) {
			home_tab_my.performClick()
		} else {
			home_tab_card.performClick()
		}
	}

	override fun onRestoreInstanceState(savedInstanceState: Bundle) {
		//避免恢复视图状态
	}

	override fun recreate() {
		val intent = Intent()
		intent.putExtra("isNight", true)
		setIntent(intent)
		super.recreate()
	}

	override fun onDestroy() {
		super.onDestroy()
		EventBus.getDefault().unregister(this)
	}

	override fun setListener() {
		super.setListener()
		iv_home_creation.setOnClickListener {
			if (checkLogin()) {
				when (SharedPreferencesUtil.getInt(SVTSConstants.writerStatus, 0)) {
					writer_status_writer -> {
						//创作界面
						mActivity.startActivity(CreationEditorActivity::class.java, {})
					}
					else -> {
						val dialog = CreationAgreementDialog()
						dialog.mListener = {
							mActivity.startActivity(CreationEditorActivity::class.java) {}
						}
						dialog.show(mActivity.supportFragmentManager, "")
					}
				}
			}
		}
		home_tab_card.setOnClickListener { switchFragment(mHomeCardFragment) }
		home_tab_article.setOnClickListener { switchFragment(mHomeArticleFragment) }
		home_tab_rank.setOnClickListener {
			switchFragment(mRankFragment)
		}
		home_tab_my.setOnClickListener { switchFragment(mMyFragment) }
	}

	@Subscribe(threadMode = ThreadMode.MAIN)
	fun onMessageEvent(event: ChangeNightModeEvent) {
		this.recreate()
	}

	private fun switchFragment(newFragment: Fragment?) {

		val fm = supportFragmentManager
		val transaction = fm.beginTransaction()

		if (mCurrentFragment === newFragment) {
			if (newFragment === mHomeCardFragment) {
				EventBus.getDefault().post(HomeClickRefreshEvent())
			}
		} else {
			if (newFragment!!.isAdded) {
				//.setCustomAnimations(R.anim.fragment_anim_left_in,R.anim.fragment_anim_right_out)
				transaction.hide(mCurrentFragment).show(newFragment).commitAllowingStateLoss()
			} else {
				transaction.hide(mCurrentFragment).add(R.id.home_fragment_content, newFragment).commitAllowingStateLoss()
			}
			mCurrentFragment = newFragment
		}
	}

	private fun gotoLoginActivity(intent: Intent) {
		if (intent.getBooleanExtra("isFirst", false)) {
			val intent1 = Intent(this, LoginActivity::class.java)
			startActivity(intent1)
		}
	}

	private fun gotoADActivity(intent: Intent) {
		if (intent.getBooleanExtra("hasAd", false)) {
			val intent1 = Intent(this, WebViewActivity::class.java)
			intent1.putExtra("title", intent.getStringExtra("title"))
			intent1.putExtra("url", intent.getStringExtra("url"))
			startActivity(intent1)
		}
	}

	override fun onActivityReenter(resultCode: Int, data: Intent?) {
		super.onActivityReenter(resultCode, data)
		mIsReenter = true
		if (mCurrentFragment == mHomeCardFragment) {
			postponeEnterTransition()
			mHomeCardFragment.onActivityReenter()
		}
	}

	fun getSharedView(names: MutableList<String>): MutableMap<String, View>? {
		return if (mCurrentFragment == mHomeCardFragment) {
			postponeEnterTransition()
			mHomeCardFragment.getSharedView(names)
		} else {
			null
		}
	}

	private fun initShareElement() {
		setExitSharedElementCallback(mExitCallback)
	}

	private val mExitCallback = object : SharedElementCallback() {

		override fun onMapSharedElements(names: MutableList<String>, sharedElements: MutableMap<String, View>) {
			if (mIsReenter) {
				sharedElements.clear()
				sharedElements.putAll(getSharedView(names) ?: mutableMapOf())
				mIsReenter = false
			}
		}
	}
}
