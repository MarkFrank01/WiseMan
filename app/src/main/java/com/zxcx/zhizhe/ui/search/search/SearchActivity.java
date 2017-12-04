package com.zxcx.zhizhe.ui.search.search;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.gyf.barlibrary.ImmersionBar;
import com.zxcx.zhizhe.R;
import com.zxcx.zhizhe.mvpBase.MvpActivity;
import com.zxcx.zhizhe.room.AppDatabase;
import com.zxcx.zhizhe.room.SearchHistory;
import com.zxcx.zhizhe.ui.search.result.SearchResultActivity;
import com.zxcx.zhizhe.utils.Constants;
import com.zxcx.zhizhe.utils.LogCat;
import com.zxcx.zhizhe.utils.StringUtils;
import com.zxcx.zhizhe.utils.Utils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.Flowable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subscribers.DisposableSubscriber;

public class SearchActivity extends MvpActivity<SearchPresenter> implements SearchContract.View {

    @BindView(R.id.et_search)
    EditText mEtSearch;
    @BindView(R.id.rv_search_pre)
    RecyclerView mRvSearchPre;
    @BindView(R.id.iv_search_clear_history)
    ImageView mIvSearchClearHistory;
    @BindView(R.id.rv_search_history)
    RecyclerView mRvSearchHistory;
    @BindView(R.id.tv_search_history_type)
    TextView mTvSearchHistoryType;
    @BindView(R.id.ll_search_history)
    LinearLayout mLlSearchHistory;
    @BindView(R.id.iv_search_clear)
    ImageView mIvSearchClear;

    private HotSearchAdapter mHotSearchAdapter;
    private SearchHistoryAdapter mSearchHistoryAdapter;
    private SearchPreAdapter mSearchPreAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        ButterKnife.bind(this);

        initRecyclerView();
        mEtSearch.addTextChangedListener(new SearchTextWatcher());
        mEtSearch.setOnEditorActionListener(new SearchListener());
        mPresenter.getSearchHot();
    }

    @Override
    public void onBackPressed() {
        Utils.closeInputMethod(mEtSearch);
        super.onBackPressed();
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus) {
            //延迟弹出软键盘
            new Handler().postDelayed(() -> Utils.showInputMethod(mEtSearch), 100);
        }
    }

    @Override
    protected SearchPresenter createPresenter() {
        return new SearchPresenter(this);
    }

    @Override
    public void getDataSuccess(List<SearchBean> list) {
        mHotSearchAdapter.setNewData(list);
        mIvSearchClearHistory.setVisibility(View.GONE);
    }

    @Override
    public void getSearchHistorySuccess(List<SearchHistory> list) {
        mSearchHistoryAdapter.setNewData(list);
        mTvSearchHistoryType.setText(R.string.search_search_history);
        mIvSearchClearHistory.setVisibility(View.VISIBLE);
        mRvSearchHistory.setAdapter(mSearchHistoryAdapter);
    }

    @Override
    public void getSearchPreSuccess(List<String> list) {
        mRvSearchPre.setVisibility(View.VISIBLE);
        mSearchPreAdapter.setKeyword(mEtSearch.getText().toString());
        mSearchPreAdapter.setNewData(list);
    }

    @OnClick(R.id.iv_search_clear)
    public void onMIvSearchClearClicked() {
        mEtSearch.setText("");
    }

    @OnClick(R.id.tv_left_close)
    public void onMTvLeftCloseClicked() {
        onBackPressed();
    }

    @OnClick(R.id.iv_search_clear_history)
    public void onMIvSearchClearHistoryClicked() {
        mDisposable = Flowable.empty()
                .subscribeOn(AndroidSchedulers.mainThread())
                .observeOn(Schedulers.io())
                .map(o -> AppDatabase.getInstance().mSearchHistoryDao().deleteAll())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableSubscriber<Integer>() {

                    @Override
                    public void onNext(Integer aVoid) {
                        mSearchHistoryAdapter.getData().clear();
                        mSearchHistoryAdapter.notifyDataSetChanged();
                        mTvSearchHistoryType.setText(R.string.search_search_history);
                        mIvSearchClearHistory.setVisibility(View.GONE);
                        mRvSearchHistory.setAdapter(mHotSearchAdapter);
                    }

                    @Override
                    public void onError(Throwable t) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
        addSubscription(mDisposable);
    }

    public void gotoSearchResult(String keyword) {
        mDisposable = Flowable.just(keyword)
                .subscribeOn(AndroidSchedulers.mainThread())
                .observeOn(Schedulers.io())
                .map(s -> AppDatabase.getInstance().mSearchHistoryDao().insertAll(new SearchHistory(s)))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableSubscriber<List<Long>>() {

                    @Override
                    public void onNext(List<Long> aVoid) {
                        LogCat.e("搜索记录插入成功");
                    }

                    @Override
                    public void onError(Throwable t) {
                        LogCat.e("搜索记录插入失败", t);
                    }

                    @Override
                    public void onComplete() {
                        Intent intent = new Intent(SearchActivity.this, SearchResultActivity.class);
                        intent.putExtra("mKeyword", keyword);
                        startActivity(intent);
                        Utils.closeInputMethod(mEtSearch);
                        finish();
                    }
                });
        addSubscription(mDisposable);
    }

    private void initRecyclerView() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(mActivity, LinearLayoutManager.VERTICAL, false);
        LinearLayoutManager layoutManager1 = new LinearLayoutManager(mActivity, LinearLayoutManager.VERTICAL, false);
        mHotSearchAdapter = new HotSearchAdapter(new ArrayList<>());
        mSearchHistoryAdapter = new SearchHistoryAdapter(new ArrayList<>());
        mSearchPreAdapter = new SearchPreAdapter(new ArrayList<>());
        mRvSearchHistory.setLayoutManager(layoutManager);
        mRvSearchPre.setLayoutManager(layoutManager1);
        mRvSearchHistory.setAdapter(mHotSearchAdapter);
        mRvSearchPre.setAdapter(mSearchPreAdapter);
        mHotSearchAdapter.setOnItemClickListener((adapter, view, position) -> {
            SearchBean bean = (SearchBean) adapter.getData().get(position);
            gotoSearchResult(bean.getConent());
        });
        mSearchHistoryAdapter.setOnItemClickListener((adapter, view, position) -> {
            SearchHistory bean = (SearchHistory) adapter.getData().get(position);
            gotoSearchResult(bean.getKeyword());
        });
        mSearchPreAdapter.setOnItemClickListener((adapter, view, position) -> {
            String bean = (String) adapter.getData().get(position);
            gotoSearchResult(bean);
        });
    }

    class SearchListener implements TextView.OnEditorActionListener {
        @Override
        public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
            //此处会响应2次 分别为ACTION_DOWN和ACTION_UP
            if (actionId == EditorInfo.IME_ACTION_SEARCH
                    || actionId == EditorInfo.IME_ACTION_DONE
                    || (event != null && KeyEvent.KEYCODE_ENTER == event.getKeyCode() && KeyEvent.ACTION_DOWN == event.getAction())) {

                if (StringUtils.isEmpty(v.getText().toString())) {
                    toastShow("搜索内容不能为空！");
                    return true;
                }

                gotoSearchResult(v.getText().toString());

                return true;
            }
            return false;
        }
    }

    class SearchTextWatcher implements TextWatcher {

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            mRvSearchPre.setVisibility(View.GONE);
            mLlSearchHistory.setVisibility(s.length() > 0 ? View.GONE : View.VISIBLE);
            mRvSearchHistory.setVisibility(s.length() > 0 ? View.GONE : View.VISIBLE);
            mIvSearchClear.setVisibility(s.length() > 0 ? View.VISIBLE : View.GONE);
            mPresenter.getSearchPre(s.toString());
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    }

}
