package com.zxcx.zhizhe.ui.otherUser

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import com.chad.library.adapter.base.BaseQuickAdapter
import com.kingja.loadsir.core.LoadSir
import com.zxcx.zhizhe.R
import com.zxcx.zhizhe.loadCallback.NetworkErrorCallback
import com.zxcx.zhizhe.mvpBase.MvpActivity
import com.zxcx.zhizhe.ui.card.card.cardDetails.CardDetailsActivity
import com.zxcx.zhizhe.ui.card.cardBag.CardBagActivity
import com.zxcx.zhizhe.ui.home.attention.AttentionCardAdapter
import com.zxcx.zhizhe.ui.home.hot.CardBean
import com.zxcx.zhizhe.ui.home.hot.HomeCardItemDecoration
import com.zxcx.zhizhe.ui.my.likeCards.MyCardsBean
import com.zxcx.zhizhe.utils.Constants
import com.zxcx.zhizhe.utils.ImageLoader
import com.zxcx.zhizhe.utils.startActivity
import com.zxcx.zhizhe.widget.CustomLoadMoreView
import com.zxcx.zhizhe.widget.EmptyView
import kotlinx.android.synthetic.main.activity_other_user.*

class OtherUserActivity : MvpActivity<OtherUserPresenter>() , OtherUserContract.View,
        BaseQuickAdapter.OnItemClickListener, BaseQuickAdapter.OnItemChildClickListener,
        BaseQuickAdapter.RequestLoadMoreListener{

    private var mPage = 0
    private var mPageSize = Constants.PAGE_SIZE
    private var mSortType = 0//0倒序 1正序
    private lateinit var mAdapter: AttentionCardAdapter
    private var id: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_other_user)
        val name = intent.getStringExtra("name")
        id = intent.getIntExtra("id",0)
        initToolBar(name)
        initView()
        initLoadSir()
        mPresenter.getOtherUserInfo(id)
        mPresenter.getOtherUserCreation(id,mSortType,mPage,mPageSize)
    }

    private fun initLoadSir() {
        loadService = LoadSir.getDefault().register(this, this)
    }

    override fun onReload(v: View?) {
        super.onReload(v)
        mPresenter.getOtherUserCreation(id,mSortType,mPage,mPageSize)
    }

    override fun createPresenter(): OtherUserPresenter {
        return OtherUserPresenter(this)
    }

    override fun getDataSuccess(bean: OtherUserInfoBean?) {
        loadService.showSuccess()
        ImageLoader.load(mActivity,bean?.imageUrl, R.drawable.default_header,iv_other_user_head)
        tv_other_user_nick_name.text = bean?.name
        tv_other_user_lv.text = bean?.intelligenceValueLevel
        tv_other_user_creation.text = bean?.creationNum.toString()
        tv_other_user_fans.text = bean?.fansNum.toString()
        tv_other_user_intelligence.text = bean?.intelligence.toString()
    }

    override fun getOtherUserCreationSuccess(list: MutableList<CardBean>) {
        if (mPage == 0) {
            loadService.showSuccess()
            mAdapter.setNewData(list)
        } else {
            mAdapter.addData(list)
        }
        mPage++
        if (list.size < Constants.PAGE_SIZE) {
            mAdapter.loadMoreEnd(false)
        } else {
            mAdapter.loadMoreComplete()
            mAdapter.setEnableLoadMore(false)
            mAdapter.setEnableLoadMore(true)
        }
    }

    override fun toastFail(msg: String) {
        super.toastFail(msg)
        mAdapter.loadMoreFail()
        if (mPage == 0) {
            loadService.showCallback(NetworkErrorCallback::class.java)
        }
    }

    override fun onLoadMoreRequested() {
        mPresenter.getOtherUserCreation(id,mSortType,mPage,mPageSize)
    }

    override fun onItemClick(adapter: BaseQuickAdapter<*, *>, view: View, position: Int) {
        val bean = adapter.data[position] as MyCardsBean
        mActivity.startActivity(CardDetailsActivity::class.java,{
            it.putExtra("id", bean.id)
            it.putExtra("name", bean.name)
        })
    }

    override fun onItemChildClick(adapter: BaseQuickAdapter<*, *>, view: View, position: Int) {
        val bean = adapter.data[position] as MyCardsBean
        mActivity.startActivity(CardBagActivity::class.java,{
            it.putExtra("id", bean.id)
            it.putExtra("name", bean.name)
        })
    }

    private fun initView() {
        mAdapter = AttentionCardAdapter(ArrayList())
        mAdapter.onItemClickListener = this
        mAdapter.setLoadMoreView(CustomLoadMoreView())
        mAdapter.setOnLoadMoreListener(this,rv_other_user)
        rv_other_user.layoutManager = LinearLayoutManager(mActivity, LinearLayoutManager.VERTICAL,false)
        rv_other_user.adapter = mAdapter
        rv_other_user.addItemDecoration(HomeCardItemDecoration())
        val emptyView = EmptyView.getEmptyView(mActivity, "该用户暂无创作内容", "快去看看其他人吧", null, null)
        mAdapter.emptyView = emptyView

        iv_other_user_close.setOnClickListener {
            onBackPressed()
        }
    }
}
