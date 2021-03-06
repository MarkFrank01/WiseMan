package com.zxcx.zhizhe.ui.my.creation

import android.content.Intent
import android.os.Bundle
import android.support.design.widget.TabLayout
import android.support.v4.app.Fragment
import android.widget.TextView
import com.zxcx.zhizhe.R
import com.zxcx.zhizhe.mvpBase.BaseActivity
import com.zxcx.zhizhe.ui.my.creation.fragment.CreationDraftsFragment
import com.zxcx.zhizhe.ui.my.creation.fragment.CreationInReviewFragment
import com.zxcx.zhizhe.ui.my.creation.fragment.CreationPassedFragment
import com.zxcx.zhizhe.ui.my.creation.fragment.CreationRejectFragment
import com.zxcx.zhizhe.ui.my.creation.newCreation.CreationEditorActivity
import com.zxcx.zhizhe.ui.my.creation.newCreation.CreationEditorLongActivity
import com.zxcx.zhizhe.ui.my.pastelink.PasteLinkActivity
import com.zxcx.zhizhe.ui.my.writer_status_writer
import com.zxcx.zhizhe.utils.SVTSConstants
import com.zxcx.zhizhe.utils.SharedPreferencesUtil
import com.zxcx.zhizhe.utils.startActivity
import com.zxcx.zhizhe.widget.PublishDialog
import kotlinx.android.synthetic.main.activity_creation.*

/**
 * Created by anm on 2017/12/14.
 * 我的-作品页面
 */
class CreationActivity : BaseActivity() {
    private val titles = arrayOf("已发布", "审核中", "未通过", "草稿箱")
    private var goto: Int = 0 //0已通过，1待审核，2未通过，3草稿箱
    private val passedFragment = CreationPassedFragment()
    private val reviewFragment = CreationInReviewFragment()
    private val rejectFragment = CreationRejectFragment()
    private val draftsFragment = CreationDraftsFragment()
    private var mCurrentFragment = Fragment()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_creation)
        initToolBar("作品")

        initView()
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        goto = intent.getIntExtra("goto", 0)
        tl_creation.getTabAt(goto)?.select()
    }

    override fun setListener() {

        //卡片创作
        iv_add_new.setOnClickListener {
            val mCreateDialog = PublishDialog(this)
            mCreateDialog.setFabuClickListener {
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
                mCreateDialog.outDia()
            }

            //深度创作
            mCreateDialog.setShenDuClickListener {
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
                mCreateDialog.outDia()
            }

            //一键转载
            mCreateDialog.setHuishouClickListener {

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
                            dialog.show(mActivity.supportFragmentManager, "")
                        }
                    }
                }

//                mActivity.startActivity(PasteLinkActivity::class.java) {}

                mCreateDialog.outDia()
            }

            mCreateDialog.show()
        }

        //直接进入创作卡片
//		iv_add_new.setOnClickListener {
//			startActivity(CreationEditorActivity::class.java, {})
//		}


        /*iv_toolbar_creation.setOnClickListener {
            //进入新建卡片流程，修改标题题图页
            startActivity(Intent(mActivity, CreationEditorActivity::class.java))
        }*/
    }

    private fun initView() {
        for (i in titles.indices) {
            val tab = tl_creation.newTab()
            tab.setCustomView(R.layout.tab_creation)
            val textView = tab.customView?.findViewById(R.id.tv_tab_creation) as TextView
            textView.text = titles[i]
            tl_creation.addTab(tab)
            //            tab.setText(titles[i]);
        }
        tl_creation.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                when (tab.position) {
                    0 -> {
                        switchFragment(passedFragment)
                    }
                    1 -> {
                        switchFragment(reviewFragment)
                    }
                    2 -> {
                        switchFragment(rejectFragment)
                    }
                    3 -> {
                        switchFragment(draftsFragment)
                    }
                }
                val textView = tab.customView?.findViewById(R.id.tv_tab_creation) as TextView
                textView.paint.isFakeBoldText = true
            }

            override fun onTabUnselected(tab: TabLayout.Tab) {
                val textView = tab.customView?.findViewById(R.id.tv_tab_creation) as TextView
                textView.paint.isFakeBoldText = false
            }

            override fun onTabReselected(tab: TabLayout.Tab) {

            }
        })
        switchFragment(passedFragment)
        goto = intent.getIntExtra("goto", 0)
        tl_creation.getTabAt(goto)?.select()
        val textView = tl_creation.getTabAt(goto)?.customView?.findViewById(R.id.tv_tab_creation) as TextView
        textView.paint.isFakeBoldText = true
    }

    private fun switchFragment(newFragment: Fragment) {

        val fm = supportFragmentManager
        val transaction = fm.beginTransaction()

        if (newFragment.isAdded) {
            //.setCustomAnimations(R.anim.fragment_anim_left_in,R.anim.fragment_anim_right_out)
            transaction.hide(mCurrentFragment).show(newFragment).commitAllowingStateLoss()
        } else {
            transaction.hide(mCurrentFragment).add(R.id.fl_creation, newFragment).commitAllowingStateLoss()
        }
        mCurrentFragment = newFragment
    }
}