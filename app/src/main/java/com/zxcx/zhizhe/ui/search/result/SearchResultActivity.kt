package com.zxcx.zhizhe.ui.search.result

import android.content.Intent
import android.os.Bundle
import android.support.v4.app.ActivityOptionsCompat
import android.support.v4.util.Pair
import android.support.v7.widget.LinearLayoutManager
import android.view.KeyEvent
import android.view.View
import android.view.inputmethod.EditorInfo
import com.chad.library.adapter.base.BaseQuickAdapter
import com.kingja.loadsir.core.LoadSir
import com.scwang.smartrefresh.layout.api.RefreshLayout
import com.zxcx.zhizhe.R
import com.zxcx.zhizhe.loadCallback.NetworkErrorCallback
import com.zxcx.zhizhe.mvpBase.RefreshMvpActivity
import com.zxcx.zhizhe.room.AppDatabase
import com.zxcx.zhizhe.room.SearchHistory
import com.zxcx.zhizhe.ui.card.card.cardDetails.CardDetailsActivity
import com.zxcx.zhizhe.ui.card.cardBag.CardBagActivity
import com.zxcx.zhizhe.ui.classify.subject.SubjectCardActivity
import com.zxcx.zhizhe.ui.home.hot.CardBean
import com.zxcx.zhizhe.ui.home.hot.HomeCardItemDecoration
import com.zxcx.zhizhe.ui.my.creation.CreationAgreementDialog
import com.zxcx.zhizhe.ui.my.creation.newCreation.CreationEditorActivity
import com.zxcx.zhizhe.ui.my.writer_status_writer
import com.zxcx.zhizhe.utils.*
import com.zxcx.zhizhe.widget.CustomLoadMoreView
import com.zxcx.zhizhe.widget.EmptyView
import io.reactivex.Flowable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import io.reactivex.subscribers.DisposableSubscriber
import kotlinx.android.synthetic.main.activity_search_result.*
import java.util.*

class SearchResultActivity : RefreshMvpActivity<SearchResultPresenter>(), SearchResultContract.View,
        BaseQuickAdapter.RequestLoadMoreListener, BaseQuickAdapter.OnItemClickListener,
        BaseQuickAdapter.OnItemChildClickListener, SubjectOnClickListener {

    private lateinit var mAdapter : SearchResultAdapter
    private var keyword = ""
    private var mPage = 0
    private val mPageSize = Constants.PAGE_SIZE

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search_result)
        initData()
        initRecyclerView()
        initView()
    }

    override fun createPresenter(): SearchResultPresenter {
        return SearchResultPresenter(this)
    }

    override fun onBackPressed() {
        Utils.closeInputMethod(et_search_result)
        super.onBackPressed()
    }

    override fun onReload(v: View?) {
        super.onReload(v)
        mPage = 0
        search()
    }

    override fun onRefresh(refreshLayout: RefreshLayout) {
        mPage = 0
        search()
    }

    override fun onLoadMoreRequested() {
        search()
    }

    override fun getDataSuccess(list: MutableList<SearchResultBean>) {
        //loadService.showSuccess()的调用必须在PtrFrameLayout.refreshComplete()之前，因为loadService的调用会使得界面重新加载，这将导致PtrFrameLayout移除
        mAdapter.mKeyword = keyword
        loadService.showSuccess()
        mRefreshLayout.finishRefresh()
        if (mPage == 0) {
            mAdapter.setNewData(list)
            rv_search_result.scrollToPosition(0)
        } else {
            mAdapter.addData(list)
        }
        mPage++
        if (list.size == 0) {
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

    private fun search() {
        mPresenter.search(keyword,mPage,mPageSize)
    }

    override fun onItemClick(adapter: BaseQuickAdapter<*, *>, view: View, position: Int) {
        val hotBean = adapter.data[position] as SearchResultBean
        if (hotBean.itemType == SearchResultBean.TYPE_CARD) {
            val bean = hotBean.cardBean
            val bundle = ActivityOptionsCompat.makeSceneTransitionAnimation(mActivity,
                    Pair.create(view.findViewById(R.id.iv_item_search_result_icon), "cardImage"),
                    Pair.create(view.findViewById(R.id.tv_item_search_result_title), "cardTitle"),
                    Pair.create(view.findViewById(R.id.tv_item_search_result_card_bag), "cardBag")).toBundle()
            val intent = Intent(mActivity, CardDetailsActivity::class.java)
            intent.putExtra("id", bean.id)
            intent.putExtra("name", bean.name)
            intent.putExtra("imageUrl", bean.imageUrl)
            intent.putExtra("date", DateTimeUtils.getDateString(bean.date))
            intent.putExtra("author", bean.author)
            mActivity.startActivity(intent, bundle)
        }
    }

    override fun onItemChildClick(adapter: BaseQuickAdapter<*, *>, view: View, position: Int) {
        val hotBean = adapter.data[position] as SearchResultBean
        val bean = hotBean.cardBean
        mActivity.startActivity(CardBagActivity::class.java,{
            it.putExtra("id", bean.cardBagId)
            it.putExtra("name", bean.cardBagName)
        })
    }

    override fun cardOnClick(bean: CardBean) {
        val intent = Intent(mActivity, CardDetailsActivity::class.java)
        intent.putExtra("id", bean.id)
        intent.putExtra("name", bean.name)
        intent.putExtra("imageUrl", bean.imageUrl)
        intent.putExtra("date", DateTimeUtils.getDateString(bean.date))
        intent.putExtra("author", bean.author)
        mActivity.startActivity(intent)
    }

    override fun subjectOnClick(bean: SubjectBean) {
        val intent = Intent(mActivity, SubjectCardActivity::class.java)
        intent.putExtra("id", bean.id)
        intent.putExtra("name", bean.name)
        mActivity.startActivity(intent)
    }

    override fun setListener() {
        iv_search_result_clear.setOnClickListener {
            et_search_result.setText("")
        }
        tv_right_close.setOnClickListener {
            onBackPressed()
        }
        et_search_result.afterTextChanged {
            iv_search_result_clear.visibility = if (it.isNotEmpty()) View.VISIBLE else View.GONE
        }
        et_search_result.setOnEditorActionListener { v, actionId, event ->
            //此处会响应2次 分别为ACTION_DOWN和ACTION_UP
            if (actionId == EditorInfo.IME_ACTION_SEARCH
                    || actionId == EditorInfo.IME_ACTION_DONE
                    || event != null && KeyEvent.KEYCODE_ENTER == event.keyCode && KeyEvent.ACTION_DOWN == event.action) {

                if (v.text.toString().isEmpty()) {
                    toastShow("搜索内容不能为空！")
                    return@setOnEditorActionListener true
                }

                keyword = v.text.toString()
                mPage = 0
                search()

                mDisposable = Flowable.just(keyword)
                        .subscribeOn(AndroidSchedulers.mainThread())
                        .observeOn(Schedulers.io())
                        .filter { s ->
                            val historyList = AppDatabase.getInstance().mSearchHistoryDao().all
                            !historyList.contains(SearchHistory(s))
                        }
                        .map { s -> AppDatabase.getInstance().mSearchHistoryDao().insertAll(SearchHistory(s)) }
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeWith(object : DisposableSubscriber<List<Long>>() {

                            override fun onNext(aVoid: List<Long>) {
                                LogCat.e("搜索记录插入成功")
                            }

                            override fun onError(t: Throwable) {
                                LogCat.e("搜索记录插入失败", t)
                            }

                            override fun onComplete() {

                            }
                        })
                addSubscription(mDisposable)

                Utils.closeInputMethod(et_search_result)

                return@setOnEditorActionListener true
            }
            return@setOnEditorActionListener false
        }
    }

    private fun initData() {
        keyword = intent.getStringExtra("mKeyword")
        et_search_result.setText(keyword)
        search()
    }

    private fun initRecyclerView() {
        val layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        mAdapter = SearchResultAdapter(ArrayList(), this)
        mAdapter.setLoadMoreView(CustomLoadMoreView())
        mAdapter.setOnLoadMoreListener(this, rv_search_result)
        mAdapter.onItemClickListener = this
        mAdapter.onItemChildClickListener = this
        rv_search_result.layoutManager = layoutManager
        rv_search_result.adapter = mAdapter
        rv_search_result.addItemDecoration(HomeCardItemDecoration())
        val emptyView = EmptyView.getEmptyViewAndClick(mActivity,"暂无内容","等你来创作",R.drawable.search_no_data,View.OnClickListener {
            if (checkLogin()) {
                when (SharedPreferencesUtil.getInt(SVTSConstants.writerStatus, 0)) {
                    writer_status_writer -> {
                        //创作界面
                        mActivity.startActivity(CreationEditorActivity::class.java,{})
                    }
                    else -> {
                        val dialog = CreationAgreementDialog()
                        dialog.show(mActivity.fragmentManager, "")
                    }
                }
            }
        })
        mAdapter.emptyView = emptyView
    }

    private fun initView() {
        loadService = LoadSir.getDefault().register(mRefreshLayout, this)
    }
}
