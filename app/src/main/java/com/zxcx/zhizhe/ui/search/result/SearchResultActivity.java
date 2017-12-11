package com.zxcx.zhizhe.ui.search.result;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.zxcx.zhizhe.R;
import com.zxcx.zhizhe.mvpBase.BaseActivity;
import com.zxcx.zhizhe.room.AppDatabase;
import com.zxcx.zhizhe.room.SearchHistory;
import com.zxcx.zhizhe.ui.search.result.card.SearchCardFragment;
import com.zxcx.zhizhe.ui.search.result.user.SearchUserFragment;
import com.zxcx.zhizhe.utils.LogCat;
import com.zxcx.zhizhe.utils.ScreenUtils;
import com.zxcx.zhizhe.utils.StringUtils;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.Flowable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subscribers.DisposableSubscriber;

public class SearchResultActivity extends BaseActivity {

    @BindView(R.id.et_search_result)
    EditText mEtSearchResult;
    @BindView(R.id.tl_search_result)
    TabLayout mTlSearchResult;
    @BindView(R.id.vp_search_result)
    ViewPager mVpSearchResult;
    @BindView(R.id.iv_search_result_clear)
    ImageView mIvSearchResultClear;

    private String[] titles = new String[]{"卡片", "用户"};

    private SearchCardFragment mSearchCardFragment = new SearchCardFragment();
    private SearchUserFragment mSearchUserFragment = new SearchUserFragment();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_result);
        ButterKnife.bind(this);

        initView();
        initData();
    }

    @OnClick(R.id.iv_search_result_clear)
    public void onMIvSearchResultClearClicked() {
        mEtSearchResult.setText("");
    }

    @OnClick(R.id.tv_left_close)
    public void onMTvLeftCloseClicked() {
        onBackPressed();
    }

    private void initData() {
        String keyword = getIntent().getStringExtra("mKeyword");
        mEtSearchResult.setText(keyword);
        mSearchCardFragment.setMKeyword(keyword);
        mSearchUserFragment.setMKeyword(keyword);
    }

    private void initView() {
        mVpSearchResult.setAdapter(new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                if (position == 0) {
                    return mSearchCardFragment;
                } else {
                    return mSearchUserFragment;
                }
            }

            @Override
            public int getCount() {
                return 2;
            }
        });
        mTlSearchResult.setupWithViewPager(mVpSearchResult);
        for (int i = 0; i < mTlSearchResult.getTabCount(); i++) {
            TabLayout.Tab tab = mTlSearchResult.getTabAt(i);
            tab.setCustomView(R.layout.tab_home);
            TextView textView = (TextView) tab.getCustomView().findViewById(R.id.tv_tab_home);
            textView.setText(titles[i]);
//            tab.setText(titles[i]);
        }

        ViewGroup.LayoutParams para = mTlSearchResult.getLayoutParams();
        int screenWidth = ScreenUtils.getScreenWidth(); //屏幕宽度
        para.width = screenWidth * 2 / 3;
        mTlSearchResult.setLayoutParams(para);

        mEtSearchResult.addTextChangedListener(new SearchResultTextWatch());
        mEtSearchResult.setOnEditorActionListener(new SearchResultListener());
    }

    class SearchResultListener implements TextView.OnEditorActionListener {
        @Override
        public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
            //此处会响应2次 分别为ACTION_DOWN和ACTION_UP
            if (actionId == EditorInfo.IME_ACTION_SEARCH
                    || actionId == EditorInfo.IME_ACTION_DONE
                    || (event != null && KeyEvent.KEYCODE_ENTER == event.getKeyCode() && KeyEvent.ACTION_DOWN == event.getAction())) {

                String keyword = v.getText().toString();
                if (StringUtils.isEmpty(keyword)) {
                    toastShow("搜索内容不能为空！");
                    return true;
                }
                mSearchCardFragment.setMKeyword(keyword);
                mSearchUserFragment.setMKeyword(keyword);
                mDisposable = Flowable.just(keyword)
                        .subscribeOn(AndroidSchedulers.mainThread())
                        .observeOn(Schedulers.io())
                        .filter(s -> {
                            List<SearchHistory> historyList = AppDatabase.getInstance().mSearchHistoryDao().getAll();
                            return !historyList.contains(new SearchHistory(s));
                        })
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

                            }
                        });
                addSubscription(mDisposable);

                return true;
            }
            return false;
        }
    }

    class SearchResultTextWatch implements TextWatcher {

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            mIvSearchResultClear.setVisibility(s.length() > 0 ? View.VISIBLE : View.GONE);
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    }
}
