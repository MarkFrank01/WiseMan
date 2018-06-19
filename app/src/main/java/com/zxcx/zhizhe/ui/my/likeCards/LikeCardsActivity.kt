package com.zxcx.zhizhe.ui.my.likeCards

import android.content.Intent
import android.os.Bundle
import android.support.v4.app.ActivityOptionsCompat
import android.support.v4.util.Pair
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.kingja.loadsir.core.LoadSir
import com.zxcx.zhizhe.R
import com.zxcx.zhizhe.event.UnLikeEvent
import com.zxcx.zhizhe.loadCallback.NetworkErrorCallback
import com.zxcx.zhizhe.mvpBase.MvpActivity
import com.zxcx.zhizhe.ui.article.articleDetails.ArticleDetailsActivity
import com.zxcx.zhizhe.ui.card.cardBag.CardBagActivity
import com.zxcx.zhizhe.ui.card.hot.CardBean
import com.zxcx.zhizhe.utils.Constants
import com.zxcx.zhizhe.utils.DateTimeUtils
import com.zxcx.zhizhe.utils.startActivity
import com.zxcx.zhizhe.widget.CustomLoadMoreView
import com.zxcx.zhizhe.widget.EmptyView
import kotlinx.android.synthetic.main.activity_like_cards.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

class LikeCardsActivity : MvpActivity<LikeCardsPresenter>(), LikeCardsContract.View,
        BaseQuickAdapter.RequestLoadMoreListener, SwipeMenuClickListener, BaseQuickAdapter.OnItemChildClickListener {

    private var mPage = 0
    private var mPageSize = Constants.PAGE_SIZE
    private lateinit var mAdapter: MyCardsAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_like_cards)
        EventBus.getDefault().register(this)
        initView()
        initLoadSir()
        mPresenter.getLikeCard(mPage,mPageSize)
        mPresenter.getEmptyRecommendCard()
    }

    override fun onDestroy() {
        EventBus.getDefault().unregister(this)
        super.onDestroy()
    }

    private fun initLoadSir() {
        loadService = LoadSir.getDefault().register(rv_like_card, this)
    }

    override fun createPresenter(): LikeCardsPresenter {
        return LikeCardsPresenter(this)
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onMessageEvent(event: UnLikeEvent) {
        val bean = MyCardsBean()
        bean.id = event.cardId
        mAdapter.remove(mAdapter.data.indexOf(bean))
    }

    override fun getEmptyRecommendCardSuccess(bean: CardBean) {
        val emptyView = EmptyView.getEmptyViewAndCard(mActivity,"涨知识 点点赞", R.drawable.no_data, bean)
        mAdapter.emptyView = emptyView
    }

    override fun getDataSuccess(list: List<MyCardsBean>) {
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

    override fun postSuccess() {
        //删除成功
    }

    override fun postFail(msg: String?) {
        toastError(msg)
    }

    override fun onLoadMoreRequested() {
        mPresenter.getLikeCard(mPage,mPageSize)
    }

    override fun onContentClick(position: Int) {
        val bean = mAdapter.data[position] as MyCardsBean
        val intent = Intent(mActivity, ArticleDetailsActivity::class.java)
        intent.putExtra("id", bean.id)
        intent.putExtra("name", bean.name)
        intent.putExtra("imageUrl", bean.imageUrl)
        intent.putExtra("date", DateTimeUtils.getDateString(bean.date))
        intent.putExtra("author", bean.author)
        val bundle = ActivityOptionsCompat.makeSceneTransitionAnimation(mActivity,
                Pair.create(mAdapter.getViewByPosition(position+mAdapter.headerLayoutCount,R.id.iv_item_card_icon), "cardImage"),
                Pair.create(mAdapter.getViewByPosition(position+mAdapter.headerLayoutCount,R.id.tv_item_card_title), "cardTitle"),
                Pair.create(mAdapter.getViewByPosition(position+mAdapter.headerLayoutCount,R.id.tv_item_card_category), "cardBag")).toBundle()
        mActivity.startActivity(intent, bundle)
    }

    override fun onDeleteClick(position: Int) {
        mPresenter.deleteLikeCard(mAdapter.data[position].id)
    }

    override fun onItemChildClick(adapter: BaseQuickAdapter<*, *>, view: View, position: Int) {
        val bean = adapter.data[position] as CardBean
        mActivity.startActivity(CardBagActivity::class.java,{
            it.putExtra("id", bean.cardBagId)
            it.putExtra("name", bean.cardCategoryName)
        })
    }

    private fun initView() {
        mAdapter = MyCardsAdapter(ArrayList())
        mAdapter.setLoadMoreView(CustomLoadMoreView())
        mAdapter.setOnLoadMoreListener(this,rv_like_card)
        mAdapter.mListener = this
        mAdapter.onItemChildClickListener = this
        rv_like_card.layoutManager = LinearLayoutManager(mActivity, LinearLayoutManager.VERTICAL,false)
        rv_like_card.adapter = mAdapter
        val header = LayoutInflater.from(mActivity).inflate(R.layout.layout_header_title, null)
        header.findViewById<TextView>(R.id.tv_header_title).text = "点赞"
        mAdapter.addHeaderView(header)
    }

    override fun setListener() {
        iv_common_close.setOnClickListener {
            onBackPressed()
        }
    }
}
