package com.zxcx.zhizhe.ui.newrank

import android.content.Intent
import android.os.Bundle
import android.support.v4.view.ViewPager
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import com.chad.library.adapter.base.BaseQuickAdapter
import com.youth.banner.BannerConfig
import com.zxcx.zhizhe.R
import com.zxcx.zhizhe.mvpBase.MvpActivity
import com.zxcx.zhizhe.ui.circle.circlemanlist.detail.CircleManDetailActivity
import com.zxcx.zhizhe.ui.loginAndRegister.login.LoginActivity
import com.zxcx.zhizhe.ui.newrank.morerank.MoreRankActivity
import com.zxcx.zhizhe.ui.rank.UserRankBean
import com.zxcx.zhizhe.ui.welcome.ADBean
import com.zxcx.zhizhe.ui.welcome.WebViewActivity
import com.zxcx.zhizhe.utils.*
import kotlinx.android.synthetic.main.activity_newrank.*
import kotlinx.android.synthetic.main.toolbar.*

/**
 * @author : MarkFrank01
 * @Created on 2019/3/22
 * @Description :
 */
class NewRankActivity : MvpActivity<NewRankPresenter>(), NewRankContract.View,
        BaseQuickAdapter.OnItemChildClickListener, BaseQuickAdapter.OnItemClickListener {

    private var id1 = 0
    private var id2 = 0
    private var id3 = 0

    private var mUserId: Int = 0
    private lateinit var mAdapter: NewRankAdapter

    //广告块的数据
    private var mAdList: MutableList<ADBean> = mutableListOf()
    private var imageList: MutableList<String> = mutableListOf()

    override fun onResume() {
        super.onResume()

        if (checkLogin1()) {
            iv_up_or_down.visibility = View.VISIBLE
            show_rank.visibility = View.VISIBLE
            tv_to_login.visibility = View.GONE
        } else {
            iv_up_or_down.visibility = View.GONE
            show_rank.visibility = View.GONE
            tv_to_login.visibility = View.VISIBLE
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_newrank)

        initToolBar("我的榜单")
        iv_toolbar_right.visibility = View.VISIBLE
        iv_toolbar_right.setImageResource(R.drawable.my_que)

        initRecyclerView()
        onRefresh()
        initADView()
        mPresenter.getAD()
    }

    fun checkLogin1(): Boolean {
        return SharedPreferencesUtil.getInt(SVTSConstants.userId, 0) != 0
    }

    override fun createPresenter(): NewRankPresenter {
        return NewRankPresenter(this)
    }

    override fun getMyRankSuccess(bean: UserRankBean) {
//        iv_up_or_down
        tv_up_or_down.text = bean.rankChange.toString()
//        ImageLoader.load(mActivity,bean.imageUrl,R.drawable.default_header,iv_up_or_down)
        ImageLoader.load(mActivity, bean.imageUrl, R.drawable.default_header, iv_my_head)
        iv_zhili.text = bean.intelligence.toString()
        chaoguo.text = bean.percentageOfUsersExceeded.toString() + "%"
        shangban.text = bean.onRankCount.toString()
        tv_my_lv.text = "No." + bean.rankIndex.toString()

    }

    override fun getTopTenRankSuccess(bean: List<UserRankBean>) {

        tv_rank_first.text = bean[0].name
        tv_rank_second.text = bean[1].name
        tv_rank_third.text = bean[2].name

        tv_rank_first_2.text = bean[0].intelligence.toString()
        tv_rank_second_2.text = bean[1].intelligence.toString()
        tv_rank_third_1.text = bean[2].intelligence.toString()

        ImageLoader.load(mActivity, bean[0].imageUrl, R.drawable.default_header, iv_rank_first)
        ImageLoader.load(mActivity, bean[1].imageUrl, R.drawable.default_header, iv_rank_second)
        ImageLoader.load(mActivity, bean[2].imageUrl, R.drawable.default_header, iv_rank_third)

        no1.setOnClickListener {
            mActivity.startActivity(CircleManDetailActivity::class.java){
                it.putExtra("userId",id1)
            }
        }

        no2.setOnClickListener {
            mActivity.startActivity(CircleManDetailActivity::class.java){
                it.putExtra("userId",id2)
            }
        }

        no3.setOnClickListener {
            mActivity.startActivity(CircleManDetailActivity::class.java){
                it.putExtra("userId",id3)
            }
        }



        mAdapter.setNewData(bean)
        id1 = bean[0].id
        mAdapter.remove(0)
        id2 = bean[0].id
        mAdapter.remove(0)
        id3 = bean[0].id
        mAdapter.remove(0)


    }

    override fun getADSuccess(list: MutableList<ADBean>) {

        LogCat.e("圈子获取广告成功 ${list.size}")

        if (list.size > 0) {
            mAdList = list
            imageList.clear()
            mAdList.forEach {
                imageList.add(it.titleImage)
            }
            fl_banner_circle.visibility = View.VISIBLE
            banner_circle.setImages(imageList)
            banner_circle.start()
        } else {
            fl_banner_circle.visibility = View.GONE
        }
    }

    override fun getDataSuccess(bean: List<UserRankBean>?) {
    }

    override fun onItemChildClick(adapter: BaseQuickAdapter<*, *>, view: View, position: Int) {
        when (view.id) {
            R.id.con_la -> {
                val bean = adapter.data[position] as UserRankBean
                mActivity.startActivity(CircleManDetailActivity::class.java){
                    it.putExtra("userId",bean.id)
                }
            }
        }
    }

    override fun onItemClick(adapter: BaseQuickAdapter<*, *>?, view: View?, position: Int) {
    }

    override fun setListener() {
        load_more_load_end_view.setOnClickListener {
            mActivity.startActivity(MoreRankActivity::class.java) {}
        }

        last_week_rank.setOnClickListener {
            mActivity.startActivity(MoreRankActivity::class.java) {}

        }

        iv_toolbar_right.setOnClickListener {
            val intent = Intent(this, WebViewActivity::class.java)
            intent.putExtra("title", "榜单说明")
            intent.putExtra("url", "http://192.168.1.153:8043/pages/list-explain.html")
            startActivity(intent)
        }

        tv_to_login.setOnClickListener {
            mActivity.startActivity(LoginActivity::class.java) {}
        }
    }

    private fun initRecyclerView() {
        mAdapter = NewRankAdapter(arrayListOf())
        mAdapter.onItemChildClickListener = this
        rv_ht_rank.layoutManager = object : LinearLayoutManager(this) {
            override fun canScrollVertically() = false
        }

        rv_ht_rank.adapter = mAdapter
    }

    private fun onRefresh() {
        mUserId = SharedPreferencesUtil.getInt(SVTSConstants.userId, 0)
        if (mUserId != 0) {
            mPresenter.getMyRank()
        }
        mPresenter.getTopTenRank()
    }

    private fun initADView() {
        banner_circle.setImageLoader(GlideBannerImageLoader())
        banner_circle.setIndicatorGravity(BannerConfig.RIGHT)
        banner_circle.setOnBannerListener {
            val adUrl = mAdList[it].behavior
            val adTitle = mAdList[it].description
//            val adImage = mAdList[it].titleImage
            val adImage = mAdList[it].shareImage
            val shareDescription = mAdList[it].shareDescription
            mActivity.startActivity(WebViewActivity::class.java) {
                it.putExtra("isAD", true)
                it.putExtra("title", adTitle)
                it.putExtra("url", adUrl)
                it.putExtra("imageUrl", adImage)
                it.putExtra("shareDescription", shareDescription)
            }
        }

        banner_circle.setOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrollStateChanged(state: Int) {

            }

            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {

            }

            override fun onPageSelected(position: Int) {
                val newPosition = position % 3
                val ad = mAdList[newPosition]


                if (mAdList.size > 0) {

                    if (iv_ad_label_circle != null) {

                        when (ad.styleType) {

                            0 -> {
                                iv_ad_label_circle.setImageResource(R.drawable.iv_ad_label_0)
                            }
                            1 -> {
                                iv_ad_label_circle.setImageResource(R.drawable.iv_ad_label_1)
                            }
                            2 -> {
                                iv_ad_label_circle.setImageResource(R.drawable.iv_ad_label_2)
                            }
                        }
                    }
                }
            }

        })
    }
}