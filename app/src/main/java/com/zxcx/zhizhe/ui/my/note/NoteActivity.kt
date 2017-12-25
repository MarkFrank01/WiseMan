package com.zxcx.zhizhe.ui.my.note

import android.content.Intent
import android.os.Bundle
import android.support.design.widget.TabLayout
import android.support.v4.app.Fragment
import android.widget.TextView
import butterknife.ButterKnife
import com.zxcx.zhizhe.R
import com.zxcx.zhizhe.mvpBase.BaseActivity
import com.zxcx.zhizhe.ui.my.creation.newCreation.NewCreationTitleActivity
import com.zxcx.zhizhe.ui.my.note.freedomNote.FreedomNoteFragment
import com.zxcx.zhizhe.ui.search.result.card.CardNoteFragment
import com.zxcx.zhizhe.utils.ScreenUtils
import kotlinx.android.synthetic.main.activity_note.*

/**
 * Created by anm on 2017/12/14.
 */
class NoteActivity : BaseActivity() {
    private val titles = arrayOf("卡片笔记", "自由笔记")
    private var mSortType = 1

    private val cardNoteFragment = CardNoteFragment()
    private val freedomNoteFragment = FreedomNoteFragment()
    private var mCurrentFragment = Fragment()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_note)
        ButterKnife.bind(this)

        initView()
        initListener()
    }

    private fun initListener() {
        iv_toolbar_back.setOnClickListener {
            onBackPressed()
        }
        iv_toolbar_creation.setOnClickListener {
            val intent = Intent(mActivity, NewCreationTitleActivity::class.java)
            startActivity(intent)
        }
        iv_toolbar_sort.setOnClickListener {
            if (mSortType == 1) {
                mSortType = 0
                iv_toolbar_sort.setImageResource(R.drawable.iv_order_sequence)
            } else if (mSortType == 0) {
                mSortType = 1
                iv_toolbar_sort.setImageResource(R.drawable.iv_order_inverted)
            }
            cardNoteFragment.mSortType = mSortType
            freedomNoteFragment.mSortType = mSortType
        }
    }

    private fun initView() {
        for (i in titles.indices) {
            val tab = tl_note.newTab()
            tab.setCustomView(R.layout.tab_note)
            val textView = tab.customView?.findViewById(R.id.tv_tab_note) as TextView
            textView.text = titles[i]
            tl_note.addTab(tab)
            //            tab.setText(titles[i]);
        }
        tl_note.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                when (tab.position) {
                    0 -> switchFragment(cardNoteFragment)
                    1 -> switchFragment(freedomNoteFragment)
                }
            }

            override fun onTabUnselected(tab: TabLayout.Tab) {}

            override fun onTabReselected(tab: TabLayout.Tab) {

            }
        })

        val para = tl_note.layoutParams
        val screenWidth = ScreenUtils.getScreenWidth() //屏幕宽度
        para.width = screenWidth * 2 / 3
        tl_note.layoutParams = para
        val isFreedomNote = intent.getBooleanExtra("isFreedomNote",false)
        if (isFreedomNote){
            tl_note.getTabAt(1)?.select()
            switchFragment(freedomNoteFragment)
        }else {
            tl_note.getTabAt(0)?.select()
            switchFragment(cardNoteFragment)
        }
    }

    private fun switchFragment(newFragment: Fragment) {

        val fm = supportFragmentManager
        val transaction = fm.beginTransaction()

        if (newFragment.isAdded) {
            //.setCustomAnimations(R.anim.fragment_anim_left_in,R.anim.fragment_anim_right_out)
            transaction.hide(mCurrentFragment).show(newFragment).commitAllowingStateLoss()
        } else {
            transaction.hide(mCurrentFragment).add(R.id.fl_note, newFragment).commitAllowingStateLoss()
        }
        mCurrentFragment = newFragment
    }
}