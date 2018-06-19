package com.zxcx.zhizhe.ui

import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import com.zxcx.zhizhe.R
import com.zxcx.zhizhe.event.ChangeNightModeEvent
import com.zxcx.zhizhe.event.ClassifyClickRefreshEvent
import com.zxcx.zhizhe.event.HomeClickRefreshEvent
import com.zxcx.zhizhe.mvpBase.BaseActivity
import com.zxcx.zhizhe.ui.article.articleDetails.ArticleDetailsActivity
import com.zxcx.zhizhe.ui.card.cardBag.CardBagActivity
import com.zxcx.zhizhe.ui.classify.ClassifyFragment
import com.zxcx.zhizhe.ui.home.HomeFragment
import com.zxcx.zhizhe.ui.loginAndRegister.login.LoginActivity
import com.zxcx.zhizhe.ui.my.MyFragment
import com.zxcx.zhizhe.ui.my.creation.CreationAgreementDialog
import com.zxcx.zhizhe.ui.my.creation.newCreation.CreationEditorActivity
import com.zxcx.zhizhe.ui.my.writer_status_writer
import com.zxcx.zhizhe.ui.welcome.WebViewActivity
import com.zxcx.zhizhe.utils.Constants
import com.zxcx.zhizhe.utils.SVTSConstants
import com.zxcx.zhizhe.utils.SharedPreferencesUtil
import com.zxcx.zhizhe.utils.startActivity
import kotlinx.android.synthetic.main.activity_main.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

class MainActivity : BaseActivity() {

    private var mCurrentFragment = Fragment()
    private var mHomeFragment: HomeFragment? = HomeFragment()
    private var mClassifyFragment: ClassifyFragment? = ClassifyFragment()
    private var mMyFragment: MyFragment? = MyFragment()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        EventBus.getDefault().register(this)

        home_tab_card.performClick()

        val intent = intent
        //判断是否点击了广告或通知
        gotoLoginActivity(intent)
        gotoADActivity(intent)
        gotoNotificationActivity(intent)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        //避免恢复视图状态
    }

    override fun recreate() {
        val intent = Intent()
        setIntent(intent)
        val fm = supportFragmentManager
        val transaction = fm.beginTransaction()
        transaction.remove(mHomeFragment)
                .remove(mClassifyFragment)
                .remove(mMyFragment)
                .commitAllowingStateLoss()
        mHomeFragment = null
        mClassifyFragment = null
        mMyFragment = null
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
                        mActivity.startActivity(CreationEditorActivity::class.java,{})
                    }
                    else -> {
                        val dialog = CreationAgreementDialog()
                        dialog.show(mActivity.fragmentManager, "")
                    }
                }
            }
        }
        home_tab_card.setOnClickListener { switchFragment(mHomeFragment) }
        home_tab_article.setOnClickListener { switchFragment(mClassifyFragment) }
        home_tab_rank.setOnClickListener {
            //todo 替换成榜单
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
            if (newFragment === mHomeFragment) {
                EventBus.getDefault().post(HomeClickRefreshEvent())
            } else if (newFragment === mClassifyFragment) {
                EventBus.getDefault().post(ClassifyClickRefreshEvent())
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

    private fun gotoNotificationActivity(intent: Intent) {
        val bundle = intent.getBundleExtra("push")
        if (bundle != null) {
            val type = bundle.getString("type")
            val detailIntent = Intent()

            when (type) {
                Constants.PUSH_TYPE_CARD_BAG -> detailIntent.setClass(this, CardBagActivity::class.java)
                Constants.PUSH_TYPE_CARD -> detailIntent.setClass(this, ArticleDetailsActivity::class.java)
                Constants.PUSH_TYPE_AD -> detailIntent.setClass(this, WebViewActivity::class.java)
            }
            detailIntent.putExtras(bundle)
            startActivity(detailIntent)
        }
    }
}
