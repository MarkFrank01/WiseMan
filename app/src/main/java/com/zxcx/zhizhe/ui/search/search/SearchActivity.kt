package com.zxcx.zhizhe.ui.search.search

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.support.v7.widget.LinearLayoutManager
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.FrameLayout
import android.widget.TextView
import butterknife.ButterKnife
import com.zxcx.zhizhe.R
import com.zxcx.zhizhe.mvpBase.MvpActivity
import com.zxcx.zhizhe.room.AppDatabase
import com.zxcx.zhizhe.room.SearchHistory
import com.zxcx.zhizhe.ui.search.result.SearchResultActivity
import com.zxcx.zhizhe.utils.*
import io.reactivex.Flowable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import io.reactivex.subscribers.DisposableSubscriber
import kotlinx.android.synthetic.main.activity_search.*
import java.util.*

class SearchActivity : MvpActivity<SearchPresenter>(), SearchContract.View ,View.OnClickListener{

    private var mHistoryList: MutableList<String> = mutableListOf()
    private var mSearchPreAdapter: SearchPreAdapter? = null
    private var mHotList: MutableList<String> = mutableListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)
        ButterKnife.bind(this)

        initRecyclerView()
        mPresenter.getSearchBean()
    }

    override fun onBackPressed() {
        Utils.closeInputMethod(et_search)
        super.onBackPressed()
    }

    override fun onWindowFocusChanged(hasFocus: Boolean) {
        super.onWindowFocusChanged(hasFocus)
        if (hasFocus) {
            //延迟弹出软键盘
            Handler().postDelayed({ Utils.showInputMethod(et_search) }, 100)
        }
    }

    override fun onResume() {
        et_search.setSelection(et_search.length())
        super.onResume()
    }

    override fun createPresenter(): SearchPresenter {
        return SearchPresenter(this)
    }

    override fun getDataSuccess(bean: SearchBean) {
        mHotList = bean.hotSearchList
        mHistoryList = bean.searchHistoryList

        tv_search_history.visibility = if (mHistoryList.isNotEmpty()) View.VISIBLE else View.GONE
        iv_search_clear_history.visibility = if (mHistoryList.isNotEmpty()) View.VISIBLE else View.GONE
        fl_search_history.visibility = if (mHistoryList.isNotEmpty()) View.VISIBLE else View.GONE

        if (mHistoryList.size > 0){
            addHistoryLabel(mHistoryList)
        }

        addHotLabel(mHotList)
    }

    override fun getSearchPreSuccess(list: MutableList<String>) {
        rv_search_pre.visibility = View.VISIBLE
        mSearchPreAdapter?.setKeyword(et_search.text.toString())
        mSearchPreAdapter?.setNewData(list)
    }

    override fun deleteHistorySuccess() {
        tv_search_history.visibility = View.GONE
        iv_search_clear_history.visibility = View.GONE
        fl_search_history.visibility = View.GONE
    }

    override fun setListener() {
        tv_search_cancel.setOnClickListener {
            onBackPressed()
        }
        iv_search_clear_history.setOnClickListener {
            mPresenter.deleteAllSearchHistory()
        }
        et_search.afterTextChanged {
            iv_search_input_clear.visibility = if (it.isNotEmpty()) View.VISIBLE else View.GONE
            rv_search_pre.visibility = View.GONE
            mPresenter.getSearchPre(it)
        }
        et_search.setOnEditorActionListener(SearchListener())
        iv_search_input_clear.setOnClickListener {
            et_search.setText("")
        }
    }

    private fun addHistoryLabel(list: MutableList<String>) {
        fl_search_history.removeAllViews()
        for (i in list.indices) {
            val frameLayout = LayoutInflater.from(mActivity).inflate(R.layout.item_search_hot, null) as FrameLayout
            val textView =frameLayout.findViewById<TextView>(R.id.tv_item_search_hot)
            textView.text = list[i]
            textView.setOnClickListener(this)
            fl_search_history.addView(frameLayout)
            val mlp = frameLayout.layoutParams as ViewGroup.MarginLayoutParams
            mlp.setMargins(ScreenUtils.dip2px(7.5f), ScreenUtils.dip2px(15f), ScreenUtils.dip2px(7.5f), 0)
        }
    }

    private fun addHotLabel(list: MutableList<String>) {
        fl_hot_search.removeAllViews()
        for (i in list.indices) {
            val frameLayout = LayoutInflater.from(mActivity).inflate(R.layout.item_search_hot, null) as FrameLayout
            val textView =frameLayout.findViewById<TextView>(R.id.tv_item_search_hot)
            textView.text = list[i]
            textView.setOnClickListener(this)
            fl_hot_search.addView(frameLayout)
            val mlp = frameLayout.layoutParams as ViewGroup.MarginLayoutParams
            mlp.setMargins(ScreenUtils.dip2px(7.5f), ScreenUtils.dip2px(15f), ScreenUtils.dip2px(7.5f), 0)
        }
    }

    override fun onClick(v: View?) {
        gotoSearchResult((v as TextView).text.toString())
    }

    fun gotoSearchResult(keyword: String) {
        mDisposable = Flowable.just(keyword)
                .subscribeOn(AndroidSchedulers.mainThread())
                .filter { s -> !mHistoryList.contains(s) }
                .observeOn(Schedulers.io())
                .map { s -> AppDatabase.getInstance().mSearchHistoryDao().insertAll(SearchHistory(s)) }
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSubscriber<MutableList<Long>>() {

                    override fun onNext(aVoid: MutableList<Long>) {
                        LogCat.e("搜索记录插入成功")
                    }

                    override fun onError(t: Throwable) {
                        LogCat.e("搜索记录插入失败", t)
                    }

                    override fun onComplete() {
                        Utils.closeInputMethod(et_search)
                        val intent = Intent(this@SearchActivity, SearchResultActivity::class.java)
                        intent.putExtra("mKeyword", keyword)
                        startActivity(intent)
                        finish()
                    }
                })
        addSubscription(mDisposable)
    }

    private fun initRecyclerView() {
        val layoutManager = LinearLayoutManager(mActivity, LinearLayoutManager.VERTICAL, false)
        mSearchPreAdapter = SearchPreAdapter(ArrayList())
        rv_search_pre.layoutManager = layoutManager
        rv_search_pre.adapter = mSearchPreAdapter
        mSearchPreAdapter?.setOnItemClickListener { adapter, view, position ->
            val bean = adapter.data[position] as String
            gotoSearchResult(bean)
        }
    }

    internal inner class SearchListener : TextView.OnEditorActionListener {
        override fun onEditorAction(v: TextView, actionId: Int, event: KeyEvent?): Boolean {
            //此处会响应2次 分别为ACTION_DOWN和ACTION_UP
            if (actionId == EditorInfo.IME_ACTION_SEARCH
                    || actionId == EditorInfo.IME_ACTION_DONE
                    || event != null && KeyEvent.KEYCODE_ENTER == event.keyCode && KeyEvent.ACTION_DOWN == event.action) {

                if (StringUtils.isEmpty(v.text.toString())) {
                    toastShow("搜索内容不能为空！")
                    return true
                }

                gotoSearchResult(v.text.toString())

                return true
            }
            return false
        }
    }

}
