package com.zxcx.zhizhe.ui.otherUser

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import com.chad.library.adapter.base.BaseQuickAdapter
import com.kingja.loadsir.core.LoadSir
import com.zxcx.zhizhe.R
import com.zxcx.zhizhe.event.FollowUserRefreshEvent
import com.zxcx.zhizhe.event.UnFollowConfirmEvent
import com.zxcx.zhizhe.loadCallback.NetworkErrorCallback
import com.zxcx.zhizhe.mvpBase.MvpActivity
import com.zxcx.zhizhe.ui.article.articleDetails.ArticleDetailsActivity
import com.zxcx.zhizhe.ui.card.cardDetails.SingleCardDetailsActivity
import com.zxcx.zhizhe.ui.card.hot.CardBean
import com.zxcx.zhizhe.ui.my.followUser.UnFollowConfirmDialog
import com.zxcx.zhizhe.ui.my.userInfo.AuthorHeadImageActivity
import com.zxcx.zhizhe.utils.*
import com.zxcx.zhizhe.widget.CustomLoadMoreView
import com.zxcx.zhizhe.widget.EmptyView
import kotlinx.android.synthetic.main.activity_other_user.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

/**
 * 其他用户主页页面
 */

class OtherUserActivity : MvpActivity<OtherUserPresenter>(), OtherUserContract.View,
        BaseQuickAdapter.OnItemClickListener, BaseQuickAdapter.RequestLoadMoreListener {

    private var mPage = 0
    private lateinit var mAdapter: OtherUserCardsAdapter
    private var id: Int = 0
    private var mUserId: Int = 0
    private var mImageAvatar: String? = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_other_user)
        EventBus.getDefault().register(this)
        id = intent.getIntExtra("id", 0)
        initView()
        initLoadSir()
        mPresenter.getOtherUserInfo(id)
        mPresenter.getOtherUserCreation(id, mPage)
    }

    override fun onResume() {
        mUserId = SharedPreferencesUtil.getInt(SVTSConstants.userId, 0)
        super.onResume()
    }

    override fun onDestroy() {
        EventBus.getDefault().unregister(this)
        super.onDestroy()
    }

    private fun initLoadSir() {
        loadService = LoadSir.getDefault().register(this, this)
    }

    override fun onReload(v: View?) {
        super.onReload(v)
        mPresenter.getOtherUserCreation(id, mPage)
    }

    override fun createPresenter(): OtherUserPresenter {
        return OtherUserPresenter(this)
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onMessageEvent(event: UnFollowConfirmEvent) {
        mPresenter.setUserFollow(event.userId, 1)
    }

    override fun getDataSuccess(bean: OtherUserInfoBean?) {
        loadService.showSuccess()
        ImageLoader.load(mActivity, bean?.imageUrl, R.drawable.default_header, iv_other_user_head)
        mImageAvatar = bean?.imageUrl
        tv_other_user_nick_name.text = bean?.name
        tv_other_user_signature.text = bean?.signature
        tv_other_user_lv.text = getString(R.string.tv_level, bean?.level ?: 1)
        bean?.isFollow?.let { cb_other_user_follow.isChecked = it }

        LogCat.e("actionTYpe"+bean?.actionType)

        if (bean?.actionType != 0 && bean?.actionType == 1) {
            iv_item_card_officials.visibility = View.VISIBLE
        }
    }

    override fun postSuccess() {
        if (cb_other_user_follow.isChecked) {
            //取消成功
            cb_other_user_follow.isChecked = false
        } else {
            //关注成功
            toastShow("关注成功")
            cb_other_user_follow.isChecked = true
        }
        EventBus.getDefault().post(FollowUserRefreshEvent())
    }

    override fun postFail(msg: String) {
        super.toastFail(msg)
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
        mPresenter.getOtherUserCreation(id, mPage)
    }

    override fun onItemClick(adapter: BaseQuickAdapter<*, *>, view: View, position: Int) {
        val bean = adapter.data[position] as CardBean
        if (bean.cardType == 1) {
            mActivity.startActivity(SingleCardDetailsActivity::class.java) {
                it.putExtra("cardBean", bean)
            }
        } else {
            mActivity.startActivity(ArticleDetailsActivity::class.java) {
                it.putExtra("cardBean", bean)
            }
        }
    }

    private fun initView() {
        val name = intent.getStringExtra("name")
        tv_other_user_nick_name.text = name
        cb_other_user_follow.expandViewTouchDelegate(ScreenUtils.dip2px(10f))

        mAdapter = OtherUserCardsAdapter(ArrayList())
        mAdapter.onItemClickListener = this
        mAdapter.setLoadMoreView(CustomLoadMoreView())
        mAdapter.setOnLoadMoreListener(this, rv_other_user)
        rv_other_user.layoutManager = LinearLayoutManager(mActivity, LinearLayoutManager.VERTICAL, false)
        rv_other_user.adapter = mAdapter
        val emptyView = EmptyView.getEmptyView(mActivity, "暂无内容", R.drawable.no_data)
        mAdapter.emptyView = emptyView
    }

    override fun setListener() {
        super.setListener()

        iv_other_user_head.setOnClickListener {
            mActivity.startActivity(AuthorHeadImageActivity::class.java) {
                it.putExtra("avatarAvatar", mImageAvatar)
            }
        }
        iv_toolbar_back.setOnClickListener {
            onBackPressed()
        }
        cb_other_user_follow.setOnClickListener {
            cb_other_user_follow.isChecked = !cb_other_user_follow.isChecked
            if (!checkLogin()) {
                return@setOnClickListener
            }
            if (mUserId == id) {
                toastShow("无法关注自己")
                return@setOnClickListener
            }
            if (!cb_other_user_follow.isChecked) {
                //关注
                mPresenter.setUserFollow(id, 0)
            } else {
                //取消关注弹窗
                val dialog = UnFollowConfirmDialog()
                val bundle = Bundle()
                bundle.putInt("userId", id)
                dialog.arguments = bundle
                dialog.show(supportFragmentManager, "")
            }
        }
    }
}
