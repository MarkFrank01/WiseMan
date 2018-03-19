package com.zxcx.zhizhe.ui.home.rank

import android.content.Intent
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import com.chad.library.adapter.base.BaseQuickAdapter
import com.kingja.loadsir.core.LoadSir
import com.scwang.smartrefresh.layout.api.RefreshLayout
import com.youth.banner.BannerConfig
import com.zxcx.zhizhe.R
import com.zxcx.zhizhe.event.LoginEvent
import com.zxcx.zhizhe.mvpBase.RefreshMvpActivity
import com.zxcx.zhizhe.ui.home.rank.moreRank.AllRankActivity
import com.zxcx.zhizhe.ui.loginAndRegister.login.LoginActivity
import com.zxcx.zhizhe.ui.otherUser.OtherUserActivity
import com.zxcx.zhizhe.ui.welcome.ADBean
import com.zxcx.zhizhe.ui.welcome.WebViewActivity
import com.zxcx.zhizhe.utils.*
import kotlinx.android.synthetic.main.activity_rank.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

class RankActivity : RefreshMvpActivity<RankPresenter>(), RankContract.View , BaseQuickAdapter.OnItemClickListener{

    private var mAdList: MutableList<ADBean> = mutableListOf()
    private val imageList: MutableList<String> = mutableListOf()
    private var mUserId : Int = 0
    private lateinit var mRankAdapter : RankAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_rank)
        loadService = LoadSir.getDefault().register(this, this)
        initRecyclerView()
        initView()
        onRefresh()
        banner_rank.setImageLoader(GlideBannerImageLoader())
        banner_rank.setIndicatorGravity(BannerConfig.CENTER)
        banner_rank.setOnBannerListener {
            val adUrl = mAdList[it].behavior
            val adTitle = mAdList[it].description
            startActivity(WebViewActivity::class.java,{
                it.putExtra("title", adTitle)
                it.putExtra("url", adUrl)
            })
        }
        banner_rank.setImages(imageList)
        banner_rank.start()
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

    override fun onRefresh(refreshLayout: RefreshLayout?) {
        onRefresh()
    }

    private fun onRefresh() {
        mUserId = SharedPreferencesUtil.getInt(SVTSConstants.userId, 0)
        if (mUserId != 0) {
            mPresenter.getMyRank()
        }
        mPresenter.getTopTenRank()
    }

    override fun getMyRankSuccess(bean: UserRankBean) {
        loadService.showSuccess()
        tv_rank_user_name.setTextColor(ContextCompat.getColor(mActivity,R.color.text_color_1))
        tv_rank_card.visibility = View.VISIBLE
        tv_rank_fans.visibility = View.VISIBLE
        tv_rank_read.visibility = View.VISIBLE
        tv_rank_no_login.visibility = View.GONE
        rl_rank.setOnClickListener(null)

        tv_rank_user_name.text = bean.name
        tv_rank_card.text = (bean.cardNum?:0).toString()
        tv_rank_fans.text = (bean.fansNum?:0).toString()
        tv_rank_read.text = (bean.readNum?:0).toString()
        val imageUrl = ZhiZheUtils.getHDImageUrl(bean.imageUrl)
        ImageLoader.load(mActivity, imageUrl, R.drawable.default_header, iv_rank_header)
        showRank(bean)
    }

    override fun getDataSuccess(list: List<UserRankBean>) {
        loadService.showSuccess()
        mRefreshLayout.finishRefresh()
        mRankAdapter.setNewData(list)
        initView()
    }

    override fun getADSuccess(list: MutableList<ADBean>) {
        mAdList = list
        mAdList.forEach {
            imageList.add(it.content)
        }
        banner_rank.update(imageList)
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onMessageEvent(event: LoginEvent) {
        onRefresh()
    }

    private fun gotoMoreRank(){
        val intent = Intent(mActivity, AllRankActivity::class.java)
        startActivity(intent)
    }

    override fun startLogin() {
        ZhiZheUtils.logout()
        toastShow(R.string.login_timeout)
        startActivity(Intent(mActivity, LoginActivity::class.java))
    }

    override fun onItemClick(adapter: BaseQuickAdapter<*, *>, view: View, position: Int) {
        if (checkLogin()) {
            val bean = adapter.data[position] as UserRankBean
            val intent = Intent(mActivity, OtherUserActivity::class.java)
            intent.putExtra("id", bean.id)
            intent.putExtra("name", bean.name)
            startActivity(intent)
        }
    }

    override fun setListener() {
        iv_rank_close.setOnClickListener { onBackPressed() }


    }

    private fun initRecyclerView() {
        rv_rank.isFocusable = false
        mRankAdapter = RankAdapter(ArrayList())
        mRankAdapter.onItemClickListener = this
        rv_rank.layoutManager = LinearLayoutManager(mActivity, LinearLayoutManager.VERTICAL,false)
        rv_rank.adapter = mRankAdapter
    }

    private fun initView() {
        mUserId = SharedPreferencesUtil.getInt(SVTSConstants.userId, 0)
        if (mUserId == 0){
            tv_rank_user_name.text = "注册/登录"
            tv_rank_user_name.setTextColor(ContextCompat.getColor(mActivity,R.color.button_blue))
            tv_rank_card.visibility = View.GONE
            tv_rank_fans.visibility = View.GONE
            tv_rank_read.visibility = View.GONE
            fl_rank_header_rank.visibility = View.GONE
            tv_rank_no_rank.visibility = View.GONE
            tv_rank_no_login.visibility = View.VISIBLE
            iv_rank_header.setImageResource(R.drawable.iv_my_head_placeholder)
            rl_rank.setOnClickListener { startActivity(Intent(mActivity, LoginActivity::class.java)) }
        }
    }

    private fun showRank(bean: UserRankBean) {
        when (bean.rankIndex) {
            1 -> {
                tv_rank_header_rank.visibility = View.GONE
                tv_rank_no_rank.visibility = View.GONE
                iv_rank_header_rank.setImageResource(R.drawable.rank_1)
            }
            2 -> {
                tv_rank_header_rank.visibility = View.GONE
                tv_rank_no_rank.visibility = View.GONE
                iv_rank_header_rank.setImageResource(R.drawable.rank_2)
            }
            3 -> {
                tv_rank_header_rank.visibility = View.GONE
                tv_rank_no_rank.visibility = View.GONE
                iv_rank_header_rank.setImageResource(R.drawable.rank_3)
            }
            in 4..99 -> {
                tv_rank_no_rank.visibility = View.GONE
                tv_rank_header_rank.visibility = View.VISIBLE
                tv_rank_header_rank.text = bean.rankIndex.toString()
                iv_rank_header_rank.setImageResource(R.drawable.rank_4)
            }
            else -> {
                fl_rank_header_rank.visibility = View.GONE
                tv_rank_no_rank.visibility = View.VISIBLE
            }
        }
    }
}