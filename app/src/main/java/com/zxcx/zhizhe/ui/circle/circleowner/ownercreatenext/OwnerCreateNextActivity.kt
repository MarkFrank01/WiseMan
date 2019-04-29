package com.zxcx.zhizhe.ui.circle.circleowner.ownercreatenext

import android.content.Intent
import android.os.Bundle
import android.support.design.widget.TabLayout
import android.support.v4.app.Fragment
import android.view.View
import android.widget.TextView
import com.zxcx.zhizhe.R
import com.zxcx.zhizhe.event.AANotingEvent
import com.zxcx.zhizhe.event.GetBackNumAndDataEvent2
import com.zxcx.zhizhe.event.GetNextArcEvent
import com.zxcx.zhizhe.event.GetNextCardEvent
import com.zxcx.zhizhe.mvpBase.MvpActivity
import com.zxcx.zhizhe.ui.card.hot.CardBean
import com.zxcx.zhizhe.ui.circle.allmycircle.AllMyCircleActivity
import com.zxcx.zhizhe.ui.circle.circlehome.CircleBean
import com.zxcx.zhizhe.utils.LogCat
import com.zxcx.zhizhe.utils.getColorForKotlin
import com.zxcx.zhizhe.utils.startActivity
import kotlinx.android.synthetic.main.activity_owner_next.*
import kotlinx.android.synthetic.main.toolbar.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

/**
 * @author : MarkFrank01
 * @Created on 2019/3/11
 * @Description :
 */
//class OwnerCreateNextActivity : BaseActivity() {
class OwnerCreateNextActivity : MvpActivity<OwnerCreateNextPresenter>(),OwnerCreateNextContract.View{



    private val titles = arrayOf("卡片", "文章")
    private val mNextCardFragment = OwnerCreateNextCardFragment()
    private val mNextArticleFragment = OwnerCreateNextArticleFragment()
    private var mCurrentFragment = Fragment()

//    private var classifyId = 0

    //检测卡片的数量
    private var mCardNum = 0
    //检查长文的数量
    private var mArcNum = 0

    //收集回传的锁定的Card
    private var mCardList: MutableList<Int> = ArrayList()

    //收集回传的锁定Arc
    private var mArcList: MutableList<Int> = ArrayList()

    //要提交的Card
    private var listcdCard = arrayListOf<CardBean>()

    //要提交的Arc
    private var listcdArc = arrayListOf<CardBean>()

    //将未锁定的聚合
    private var mAllUnLock:MutableList<Int>  =ArrayList()

    //将锁定的聚合
    private var mAllLock:MutableList<Int> = ArrayList()

    ////////////////////////////
    //标题
    private var title = ""

    //封面图
    private var mImageUrl = ""

    //类别名
    private var labelName = ""

    //类别ID
    private var classifyId = 0

    //圈子签名
    private var sign = ""

    //（新）价格选择
    private var levelType = 0

    //(新)限时免费
    private var limitedTimeType = 0
    ///////////////////////////

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_owner_next)
        EventBus.getDefault().register(this)
        //读取上个AC传递过来的值
        initData()
        initToolBar("设置阅读权限")
        tv_toolbar_right.visibility = View.VISIBLE
        tv_toolbar_right.text = "提交"
        tv_toolbar_right.isEnabled = false
        tv_toolbar_right.setTextColor(mActivity.getColorForKotlin(R.color.text_color_d2))


        initView()
//        val runAB = Runnable {
//            tl_circle_next.getTabAt(0)?.select()
//        }
//        var runPlease = Thread(runAB)
//        runPlease.start()
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        //避免恢复视图状态
    }

    override fun recreate() {
        LogCat.e("生效")
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

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onMessageEvent(event: AANotingEvent) {

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onMessageEvent(event:GetBackNumAndDataEvent2){
        if (event.type == 0) {
            mCardNum = event.contentList.size
            mCardList = event.contentList
        } else if (event.type == 1) {
            mArcNum = event.contentList.size
            mArcList = event.contentList
        }

        //之后调整为8和4
        if (mCardNum < listcdCard.size / 2 && mArcNum < listcdArc.size / 2) {
            tv_toolbar_right.isEnabled = true
            tv_toolbar_right.setTextColor(mActivity.getColorForKotlin(R.color.button_blue))
        }else{
            tv_toolbar_right.isEnabled = false
            tv_toolbar_right.setTextColor(mActivity.getColorForKotlin(R.color.text_color_d2))
        }
    }

    override fun setListener() {
        tv_toolbar_right.setOnClickListener {
//            toastShow("ready")
            mPresenter.createCircleNew(title, mImageUrl, classifyId, sign, levelType, limitedTimeType)
        }
    }

    private fun initData() {
        listcdCard = intent.getParcelableArrayListExtra<CardBean>("listCard")
        listcdArc = intent.getParcelableArrayListExtra<CardBean>("listArc")
        LogCat.e("Next card" + listcdCard.size)
        LogCat.e("Next arc" + listcdArc.size)
//        EventBus.getDefault().post(GetNextCardEvent(0,listcdCard))
//        EventBus.getDefault().post(GetNextArcEvent(0,listcdArc))

        classifyId = intent.getIntExtra("classifyId", 0)
        title = intent.getStringExtra("title")
        levelType = intent.getIntExtra("levelType", 0)
        sign = intent.getStringExtra("sign")
        mImageUrl = intent.getStringExtra("mImageUrl")
        labelName = intent.getStringExtra("labelName")
        classifyId = intent.getIntExtra("classifyId", 0)
        limitedTimeType = intent.getIntExtra("limitedTimeType", 0)
    }

    private fun initView() {

        tl_circle_next.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                when (tab.position) {
                    0 -> {
//                        show_first_tv.text = "在此页面选择4张卡片在圈外公开阅读"
                        show_first_tv.text = "在此页面选择" + listcdCard.size / 2 + "张卡片在圈外公开阅读"
                        switchFragment(mNextCardFragment)
                        EventBus.getDefault().post(GetNextCardEvent(0, listcdCard))
                    }
                    1 -> {
                        switchFragment(mNextArticleFragment)
//                        show_first_tv.text = "在此页面选择2篇文章在圈外公开阅读"
                        show_first_tv.text = "在此页面选择" + listcdArc.size / 2 + "篇文章在圈外公开阅读"
                        EventBus.getDefault().post(GetNextArcEvent(0, listcdArc))

                    }
                }
//                val textView = tab.customView?.findViewById(R.id.tv_tab_creation) as TextView
//                textView.paint.isFakeBoldText = true
            }

            override fun onTabReselected(tab: TabLayout.Tab) {
//                val textView = tab.customView?.findViewById(R.id.tv_tab_creation) as TextView
//                textView.paint.isFakeBoldText = false
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {
            }
        })



        for (i in titles.indices) {
            val tab = tl_circle_next.newTab()
            tab.setCustomView(R.layout.tab_creation)
            val textView = tab.customView?.findViewById(R.id.tv_tab_creation) as TextView
            textView.text = titles[i]
            tl_circle_next.addTab(tab)
        }

        switchFragment(mNextCardFragment)
        tl_circle_next.getTabAt(0)?.select()
        EventBus.getDefault().post(GetNextCardEvent(0, listcdCard))


        val textView = tl_circle_next.getTabAt(0)?.customView?.findViewById(R.id.tv_tab_creation) as TextView
        textView.paint.isFakeBoldText = true

    }

    private fun switchFragment(newFragment: Fragment) {
        val fm = supportFragmentManager
        val transaction = fm.beginTransaction()

        if (mCurrentFragment == newFragment){
            if (newFragment == mNextCardFragment){
                EventBus.getDefault().post(GetNextCardEvent(0, listcdCard))
            }else if (newFragment == mNextArticleFragment){
                EventBus.getDefault().post(GetNextArcEvent(0, listcdArc))
            }
        }else {
            if (newFragment.isAdded) {
                //.setCustomAnimations(R.anim.fragment_anim_left_in,R.anim.fragment_anim_right_out)
                transaction.hide(mCurrentFragment).show(newFragment).commitAllowingStateLoss()
            } else {
                transaction.hide(mCurrentFragment).add(R.id.fl_circle, newFragment).commitAllowingStateLoss()
            }
            mCurrentFragment = newFragment
        }
    }

    override fun createPresenter(): OwnerCreateNextPresenter {
        return OwnerCreateNextPresenter(this)
    }

    override fun getDataSuccess(bean: MutableList<CardBean>?) {
        toastShow("提交成功，等待审核")
        startActivity(AllMyCircleActivity::class.java){}
        finish()
    }

    override fun createCircleSuccess(bean:CircleBean) {
        //聚合锁内容
        for (i in mCardList){
            mAllLock.add(i)
        }

        for (i in mArcList){
            mAllLock.add(i)
        }

        LogCat.e("最后统计上锁的文章"+mAllLock.size)

        //聚合未锁内容
        for (i in listcdCard){
            mAllUnLock.add(i.id)
        }

        for (i in listcdArc){
            mAllUnLock.add(i.id)
        }

        LogCat.e("最后统计未上锁的文章"+mAllUnLock.size)

        //创建圈子回来后传递文章
        mPresenter.setCircleArticle(bean.id,mAllUnLock,mAllLock)
    }

    override fun onActivityReenter(resultCode: Int, data: Intent?) {
        super.onActivityReenter(resultCode, data)
        if (mCurrentFragment == mNextCardFragment){
            postponeEnterTransition()
            onActivityReenter()
        }
    }

    public fun onActivityReenter() {
        when(mCurrentFragment){
            mNextCardFragment -> mNextCardFragment.onActivityReenter()
        }
    }
}