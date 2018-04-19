package com.zxcx.zhizhe.ui.search.search

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.support.v7.widget.LinearLayoutManager
import android.text.Editable
import android.text.TextWatcher
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
import com.zxcx.zhizhe.utils.LogCat
import com.zxcx.zhizhe.utils.ScreenUtils
import com.zxcx.zhizhe.utils.StringUtils
import com.zxcx.zhizhe.utils.Utils
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
        et_search.addTextChangedListener(SearchTextWatcher())
        et_search.setOnEditorActionListener(SearchListener())
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
        if (mHistoryList.size > 0){
            tv_search_history_type.setText(R.string.search_search_history)
            iv_search_clear_history.visibility = View.VISIBLE
            addLabel(mHistoryList)
        }else{
            tv_search_history_type.setText(R.string.search_hot_search)
            iv_search_clear_history.visibility = View.GONE
            addLabel(mHotList)
        }
    }

    override fun getSearchPreSuccess(list: MutableList<String>) {
        rv_search_pre.visibility = View.VISIBLE
        mSearchPreAdapter?.setKeyword(et_search.getText().toString())
        mSearchPreAdapter?.setNewData(list)
    }

    override fun setListener() {
        iv_common_close.setOnClickListener {
            onBackPressed()
        }
        iv_search.setOnClickListener {
            if (StringUtils.isEmpty(et_search.text.toString())) {
                toastShow("搜索内容不能为空！")
                return@setOnClickListener
            }

            gotoSearchResult(et_search.text.toString())
        }
        iv_search_clear_history.setOnClickListener {
            mDisposable = Flowable.just(0)
                    .subscribeOn(AndroidSchedulers.mainThread())
                    .observeOn(Schedulers.io())
                    .map { AppDatabase.getInstance().mSearchHistoryDao().deleteAll() }
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeWith(object : DisposableSubscriber<Int>() {
                        override fun onNext(aVoid: Int?) {
                            tv_search_history_type.setText(R.string.search_hot_search)
                            iv_search_clear_history.visibility = View.GONE
                            addLabel(mHotList)
                        }

                        override fun onError(t: Throwable) {
                            LogCat.e("删除失败", t)
                        }

                        override fun onComplete() {}
                    })

            addSubscription(mDisposable)
        }
    }

    private fun addLabel(list: MutableList<String>) {
        fl_search_hot.removeAllViews()
        for (i in list.indices) {
            val frameLayout = LayoutInflater.from(mActivity).inflate(R.layout.item_search_hot, null) as FrameLayout
            val textView =frameLayout.findViewById<TextView>(R.id.tv_item_search_hot)
            textView.text = list[i]
            textView.setOnClickListener(this@SearchActivity)
            fl_search_hot.addView(frameLayout)
            val mlp = frameLayout.layoutParams as ViewGroup.MarginLayoutParams
            mlp.setMargins(ScreenUtils.dip2px(7.5f), ScreenUtils.dip2px(10f), ScreenUtils.dip2px(7.5f), 0)
        }
    }

    override fun onClick(v: View?) {
        gotoSearchResult((v as TextView).text.toString())
    }

    fun gotoSearchResult(keyword: String) {
        et_search.setText(keyword)
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

    internal inner class SearchTextWatcher : TextWatcher {

        override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {

        }

        override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
            rv_search_pre.visibility = View.GONE
            mPresenter.getSearchPre(s.toString())
        }

        override fun afterTextChanged(s: Editable) {

        }
    }

}
