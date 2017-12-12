package com.zxcx.zhizhe.ui.home.rank

import `in`.srain.cube.views.ptr.PtrFrameLayout
import android.content.Intent
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.kingja.loadsir.core.LoadSir
import com.zxcx.zhizhe.R
import com.zxcx.zhizhe.event.HomeClickRefreshEvent
import com.zxcx.zhizhe.event.LoginEvent
import com.zxcx.zhizhe.loadCallback.AttentionNeedLoginCallback
import com.zxcx.zhizhe.loadCallback.HomeLoadingCallback
import com.zxcx.zhizhe.loadCallback.NetworkErrorCallback
import com.zxcx.zhizhe.mvpBase.RefreshMvpFragment
import com.zxcx.zhizhe.ui.loginAndRegister.login.LoginActivity
import com.zxcx.zhizhe.ui.rank.RankActivity
import com.zxcx.zhizhe.utils.ImageLoader
import com.zxcx.zhizhe.utils.SVTSConstants
import com.zxcx.zhizhe.utils.SharedPreferencesUtil
import com.zxcx.zhizhe.utils.ZhiZheUtils
import kotlinx.android.synthetic.main.fragment_rank.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

class RankFragment : RefreshMvpFragment<RankPresenter>(), RankContract.View , BaseQuickAdapter.OnItemClickListener{

    private var isFirst: Boolean = true
    private var mUserId : Int = 0
    private lateinit var mRankAdapter : RankAdapter
    private lateinit var rvRank : RecyclerView
    var mAppBarLayoutVerticalOffset: Int = 0

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val root = inflater.inflate(R.layout.fragment_rank, container, false)
        rvRank = root.findViewById(R.id.rv_rank)
        root.findViewById<TextView>(R.id.tv_rank_more).setOnClickListener { gotoMoreRank() }
        mRefreshLayout = root.findViewById(R.id.refresh_layout)

        val loadSir = LoadSir.Builder()
                .addCallback(HomeLoadingCallback())
                .addCallback(AttentionNeedLoginCallback())
                .addCallback(NetworkErrorCallback())
                .build()
        loadService = loadSir.register(root) {
            onRefresh()
        }
        return loadService.loadLayout
    }

    override fun createPresenter(): RankPresenter {
        return RankPresenter(this)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        loadService.showCallback(HomeLoadingCallback::class.java)
        initRecyclerView()
        initView()
        onRefresh()
    }

    override fun onHiddenChanged(hidden: Boolean) {
        if (!hidden) {
            EventBus.getDefault().register(this)
            initView()
        } else {
            EventBus.getDefault().unregister(this)
        }
        super.onHiddenChanged(hidden)
    }

    override fun checkCanDoRefresh(frame: PtrFrameLayout?, content: View?, header: View?): Boolean {
        return mAppBarLayoutVerticalOffset == 0 && !nsv_rank.canScrollVertically(-1)
    }

    override fun onRefreshBegin(frame: PtrFrameLayout?) {
        onRefresh()
    }

    private fun onRefresh() {
        mPresenter.getMyRank()
        mPresenter.getTopTenRank()
    }

    override fun getMyRankSuccess(bean: UserRankBean) {
        tv_rank_my_user_name.setTextColor(ContextCompat.getColor(mActivity,R.color.text_color_1))
        tv_rank_my_card.visibility = View.VISIBLE
        tv_rank_my_fans.visibility = View.VISIBLE
        tv_rank_my_read.visibility = View.VISIBLE
        tv_rank_my_no_login.visibility = View.GONE
        rl_rank_my.setOnClickListener(null)

        tv_rank_my_user_name.text = bean.name
        tv_rank_my_card.text = (bean.cardNum?:0).toString()
        tv_rank_my_fans.text = (bean.fansNum?:0).toString()
        tv_rank_my_read.text = (bean.readNum?:0).toString()
        val imageUrl = ZhiZheUtils.getHDImageUrl(bean.imageUrl)
        ImageLoader.load(mActivity, imageUrl, R.drawable.default_header, iv_rank_my_header)
        //todo 排名显示逻辑未完成
    }

    override fun getDataSuccess(list: List<UserRankBean>) {
        loadService.showSuccess()
        mRefreshLayout.refreshComplete()
        mRankAdapter.setNewData(list)
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onMessageEvent(event: HomeClickRefreshEvent) {
        nsv_rank.fullScroll(-1)
        mRefreshLayout.autoRefresh()
    }

    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    fun onMessageEvent(event: LoginEvent) {
        rv_rank.scrollToPosition(0)
        mRefreshLayout.autoRefresh()
        EventBus.getDefault().removeStickyEvent(event)
    }

    fun gotoMoreRank(){
        val intent = Intent(mActivity, RankActivity::class.java)
        startActivity(intent)
    }

    override fun startLogin() {
        ZhiZheUtils.logout()
        toastShow(R.string.login_timeout)
        startActivity(Intent(mActivity, LoginActivity::class.java))
        if (loadService != null) {
            loadService.showCallback(AttentionNeedLoginCallback::class.java)
        }
    }

    override fun onItemClick(adapter: BaseQuickAdapter<*, *>, view: View, position: Int) {
        /*val bean = adapter.data[position] as SearchCardBean
        val intent = Intent(mActivity, CardDetailsActivity::class.java)
        intent.putExtra("id", bean.id)
        intent.putExtra("name", bean.name)
        startActivity(intent)*/
    }

    private fun initRecyclerView() {
        mRankAdapter = RankAdapter(ArrayList())
        mRankAdapter.onItemClickListener = this
        rvRank.layoutManager = LinearLayoutManager(mActivity, LinearLayoutManager.VERTICAL,false)
        rvRank.adapter = mRankAdapter
    }

    private fun initView() {
        mUserId = SharedPreferencesUtil.getInt(SVTSConstants.userId, 0)
        if (mUserId == 0){
            tv_rank_my_user_name.text = "注册/登录"
            tv_rank_my_user_name.setTextColor(ContextCompat.getColor(mActivity,R.color.button_blue))
            tv_rank_my_card.visibility = View.GONE
            tv_rank_my_fans.visibility = View.GONE
            tv_rank_my_read.visibility = View.GONE
            tv_rank_my_no_login.visibility = View.VISIBLE
            iv_rank_my_header.setImageResource(R.drawable.iv_my_head_placeholder)
            rl_rank_my.setOnClickListener { startActivity(Intent(mActivity, LoginActivity::class.java)) }
        }else{
            if (isFirst) {
                mPresenter.getMyRank()
                isFirst = false;
            }
        }
    }
}