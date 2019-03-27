package com.zxcx.zhizhe.ui.circle.circlemanlist.detail

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentStatePagerAdapter
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.zxcx.zhizhe.R
import com.zxcx.zhizhe.mvpBase.MvpActivity
import com.zxcx.zhizhe.ui.card.hot.CardBean
import com.zxcx.zhizhe.ui.circle.circlehome.CircleBean
import com.zxcx.zhizhe.ui.circle.circlehome.CircleUserBean
import com.zxcx.zhizhe.utils.ImageLoader
import com.zxcx.zhizhe.utils.ScreenUtils
import com.zxcx.zhizhe.utils.SharedPreferencesUtil
import com.zxcx.zhizhe.utils.expandViewTouchDelegate
import kotlinx.android.synthetic.main.activity_circle_man_detail.*

/**
 * @author : MarkFrank01
 * @Created on 2019/3/27
 * @Description :
 */
class CircleManDetailActivity : MvpActivity<CircleManDetailPresenter>(), CircleManDetailContract.View,
        BaseQuickAdapter.OnItemChildClickListener {


    private var mPage = 0
    private var userId = 0

    private val titles = arrayOf("卡片", "圈子")

    private var mAdapter: ViewPagerAdapter2? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_circle_man_detail)
        tl_list.setupWithViewPager(vp_list)

        initData()
        initView()

//        mPresenter.getCreationCircleListByUserId(mPage,Constants.PAGE_SIZE,userId)
        mPresenter.getAuthorInfo(userId)
    }

    override fun createPresenter(): CircleManDetailPresenter {
        return CircleManDetailPresenter(this)
    }

    override fun getCircleListByAuthorIdSuccess(list: MutableList<CircleBean>) {
    }

    override fun getAuthorInfoSuccess(bean: CircleUserBean) {
        ImageLoader.load(mActivity, bean.avatar, R.drawable.default_header, iv_my_head)
        tv_my_lv.text = "Lv." + bean.level.toString()
        tv_my_nick_name.text = bean.name
        tv_my_signature.text = bean.signature
        tv_him_create_num.text = bean.authorCreateArticleCount.toString()
        tv_him_notice_num.text = bean.fansNum.toString()
        tv_him_zan_num.text = bean.likedUsersCount.toString()
        tv_him_collect_num.text = bean.collectNum.toString()

        cb_other_user_follow.isChecked = bean.followType == 1 || bean.followType == 3
    }

    override fun getOtherUserCreationSuccess(list: MutableList<CardBean>) {
    }

    override fun followSuccess() {
        if (cb_other_user_follow.isChecked) {
            //取消成功
            cb_other_user_follow.isChecked = false
        } else {
            toastShow("关注成功")
            cb_other_user_follow.isChecked = true
        }
    }

    override fun getDataSuccess(bean: CircleUserBean?) {
    }

    override fun onItemChildClick(adapter: BaseQuickAdapter<*, *>?, view: View?, position: Int) {
    }

    private fun initView() {
        mAdapter = supportFragmentManager?.let { ViewPagerAdapter2(it) }
        vp_list.adapter = mAdapter

        for (i in titles.indices) {
            val tab = tl_list.newTab()
            tab.setCustomView(R.layout.tab_new_two)
            val textView = tab.customView?.findViewById<TextView>(R.id.tv_tab_card_list)
            textView?.text = titles[i]
            tl_list.addTab(tab)
        }

        cb_other_user_follow.expandViewTouchDelegate(ScreenUtils.dip2px(10f))
    }

    private fun initData() {
        userId = intent.getIntExtra("userId", 0)
        SharedPreferencesUtil.saveData("userId",userId)
    }

    override fun setListener() {

        iv_toolbar_back.setOnClickListener {
            onBackPressed()
        }

        cb_other_user_follow.setOnClickListener {
            cb_other_user_follow.isChecked = !cb_other_user_follow.isChecked
            if (!cb_other_user_follow.isChecked) {
                mPresenter.setUserFollow(userId, 0)
            } else {
                mPresenter.setUserFollow(userId, 1)
            }
        }
    }

    class ViewPagerAdapter2(fm: FragmentManager) : FragmentStatePagerAdapter(fm) {

        override fun getItem(position: Int): Fragment {
            return if (position == 0) {
                CircleManDetailCardFragment()
            } else {
                CircleManDetailCircleFragment()
            }
        }

        override fun instantiateItem(container: ViewGroup, position: Int): Any {

            return if (position == 0) {
                super.instantiateItem(container, position) as CircleManDetailCardFragment
            } else {
                super.instantiateItem(container, position) as CircleManDetailCircleFragment
            }

        }

        override fun getCount(): Int {
            return 2
        }

    }
}