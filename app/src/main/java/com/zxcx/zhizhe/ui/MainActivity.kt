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
import com.zxcx.zhizhe.ui.my.creation.newCreation.CreationEditorLongActivity
import com.zxcx.zhizhe.ui.my.pastelink.PasteLinkActivity
import com.zxcx.zhizhe.ui.my.writer_status_writer
import com.zxcx.zhizhe.ui.rank.RankFragment
import com.zxcx.zhizhe.ui.welcome.WebViewActivity
import com.zxcx.zhizhe.utils.SVTSConstants
import com.zxcx.zhizhe.utils.SharedPreferencesUtil
import com.zxcx.zhizhe.utils.startActivity
import com.zxcx.zhizhe.widget.PublishDialog
import kotlinx.android.synthetic.main.activity_main.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

/**
 * 首页
 */

class MainActivity : BaseActivity() {

    private var mCurrentFragment = Fragment()
    private var mHomeCardFragment = HomeCardFragment()
    private var mHomeArticleFragment = HomeArticleFragment()
    private var mRankFragment = RankFragment()
    private var mMyFragment: MyFragment? = MyFragment()
    private var mIsReenter = false

    //////
//    var advList:ArrayList<AdInfo> = ArrayList()

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

//        showFirstDialog()
//        val adManager = AdManager(this,advList)
//        adManager.setOverScreen(true)
//                .setPageTransformer(DepthPageTransformer())
//                .setOnImageClickListener { view, advInfo ->
//                    toastShow("get AD")
//                }
//
//        adManager.showAdDialog(AdConstant.ANIM_DOWN_TO_UP)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        //避免恢复视图状态
    }

    override fun recreate() {
        val intent = Intent()
        intent.putExtra("isNight", true)
        setIntent(intent)
        try {//避免重启太快 恢复
            val fragmentTransaction = supportFragmentManager.beginTransaction()
            for (fragment in supportFragmentManager.fragments) {
                fragmentTransaction.remove(fragment)
            }
            fragmentTransaction.commitAllowingStateLoss()
        } catch (e: Exception) {
        }

        super.recreate()
    }

    override fun onDestroy() {
        EventBus.getDefault().unregister(this)
        super.onDestroy()
    }

    override fun setListener() {
        super.setListener()

//        iv_home_creation.setOnClickListener {
//            if (checkLogin()) {
//                when (SharedPreferencesUtil.getInt(SVTSConstants.writerStatus, 0)) {
//                    writer_status_writer -> {
//                        //选择模板
//                        mActivity.startActivity(TemplateCardActivity::class.java) {}
//                    }
//                    else -> {
//                        val dialog = CreationAgreementDialog()
//                        dialog.mListener = {
//                            mActivity.startActivity(TemplateCardActivity::class.java){}
//                        }
//                        dialog.show(mActivity.supportFragmentManager,"")
//                    }
//                }
//            }
//        }

        //暂时注释，可能版本仍需确认
//        iv_home_creation.setOnClickListener {
//            if (checkLogin()) {
//                when (SharedPreferencesUtil.getInt(SVTSConstants.writerStatus, 0)) {
//                    writer_status_writer -> {
//                        //创作界面
//                        mActivity.startActivity(CreationEditorActivity::class.java) {}
//                    }
//                    else -> {
//                        val dialog = CreationAgreementDialog()
//                        dialog.mListener = {
//                            mActivity.startActivity(CreationEditorActivity::class.java) {}
//                        }
//                        dialog.show(mActivity.supportFragmentManager, "")
//                    }
//                }
//            }
//        }

        iv_home_creation.setOnClickListener {
            val mHomeDialog = PublishDialog(this)
            mHomeDialog.setFabuClickListener {
                //                toastShow("开始创作")
                if (checkLogin()) {
                    when (SharedPreferencesUtil.getInt(SVTSConstants.writerStatus, 0)) {
                        writer_status_writer -> {
                            //创作界面
                            mActivity.startActivity(CreationEditorActivity::class.java) {}
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
                mHomeDialog.outDia()
            }

            mHomeDialog.setShenDuClickListener {
                if (checkLogin()) {
                    when (SharedPreferencesUtil.getInt(SVTSConstants.writerStatus, 0)) {
                        writer_status_writer -> {
                            //创作界面
                            mActivity.startActivity(CreationEditorLongActivity::class.java) {}
                        }

                        else -> {
                            val dialog = CreationAgreementDialog()
                            dialog.mListener = {
                                mActivity.startActivity(CreationEditorLongActivity::class.java) {}
                            }
                            dialog.show(mActivity.supportFragmentManager, "")
                        }
                    }
                }
                mHomeDialog.outDia()
            }

            mHomeDialog.setHuishouClickListener {

                if (checkLogin()) {
                    when (SharedPreferencesUtil.getInt(SVTSConstants.writerStatus, 0)) {
                        writer_status_writer -> {
                            //一键转载
                            mActivity.startActivity(PasteLinkActivity::class.java) {}
                        }

                        else -> {
                            val dialog = CreationAgreementDialog()
                            dialog.mListener = {
                                mActivity.startActivity(PasteLinkActivity::class.java) {}
                            }
                            dialog.show(mActivity.supportFragmentManager,"")
                        }
                    }
                }

//                mActivity.startActivity(PasteLinkActivity::class.java) {}

                mHomeDialog.outDia()
            }

            mHomeDialog.show()
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
        if (intent.getBooleanExtra("needLogin", false)) {
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
        if (mCurrentFragment == mHomeCardFragment) {
            mIsReenter = true
            postponeEnterTransition()
            mHomeCardFragment.onActivityReenter()
        }
    }

    fun getSharedView(names: MutableList<String>): MutableMap<String, View>? {
        return if (mCurrentFragment == mHomeCardFragment) {
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

    ////////////////////////////
//    private fun showFirstDialog() {
//        val adInfo = AdInfo()
//        adInfo.activityImg = "https://raw.githubusercontent.com/yipianfengye/android-adDialog/master/images/testImage1.png"
//        advList.add(adInfo)
//    }
}
