package com.zxcx.shitang.ui.search.result;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.zxcx.shitang.R;
import com.zxcx.shitang.mvpBase.MvpActivity;
import com.zxcx.shitang.ui.card.cardBag.CardBagActivity;
import com.zxcx.shitang.ui.home.hot.itemDecoration.HomeCardBagItemDecoration;
import com.zxcx.shitang.ui.search.result.adapter.SearchResultCardAdapter;
import com.zxcx.shitang.ui.search.result.adapter.SearchResultCardBagAdapter;
import com.zxcx.shitang.utils.Utils;
import com.zxcx.shitang.widget.CustomLoadMoreView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SearchResultActivity extends MvpActivity<SearchResultPresenter> implements SearchResultContract.View,
        BaseQuickAdapter.RequestLoadMoreListener, SwipeRefreshLayout.OnRefreshListener {

    private static final int TOTAL_COUNTER = 30;
    @BindView(R.id.rv_search_result_card)
    RecyclerView mRvSearchResultCard;
    @BindView(R.id.srl_search_result)
    SwipeRefreshLayout mSrlSearchResult;
    RecyclerView mRvSearchResultCardBag;
    LinearLayout mLLHeadSearchResultCardBag;
    @BindView(R.id.et_search_result)
    EditText mEtSearchResult;

    private SearchResultCardBagAdapter mCardBagAdapter;
    private SearchResultCardAdapter mCardAdapter;
    private List<SearchResultBean> mList = new ArrayList<>();
    private boolean isErr = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_result);
        ButterKnife.bind(this);

        getData();

        initRecyclerView();
        mEtSearchResult.setOnEditorActionListener(new SearchListener());
        mSrlSearchResult.setOnRefreshListener(this);
        mSrlSearchResult.setColorSchemeColors(ContextCompat.getColor(mActivity, R.color.colorPrimary));
    }

    @Override
    protected SearchResultPresenter createPresenter() {
        return new SearchResultPresenter(this);
    }

    @Override
    public void getDataSuccess(SearchResultBean bean) {

    }

    @Override
    public void onRefresh() {
        isErr = false;
        mCardAdapter.setEnableLoadMore(false);
        mCardAdapter.setEnableLoadMore(true);
        mList.clear();
        getData();
        mCardAdapter.notifyDataSetChanged();
        if (mSrlSearchResult.isRefreshing()) {
            mSrlSearchResult.setRefreshing(false);
        }
    }

    @Override
    public void onLoadMoreRequested() {
        mSrlSearchResult.setEnabled(false);
        if (mCardAdapter.getData().size() > TOTAL_COUNTER) {
            mCardAdapter.loadMoreEnd(false);
        } else {
            if (isErr) {
                getData();
//                mCardAdapter.addData();
                mCardAdapter.notifyDataSetChanged();
                mCardAdapter.loadMoreComplete();
            } else {
                isErr = true;
                Toast.makeText(mActivity, "网络错误", Toast.LENGTH_LONG).show();
                mCardAdapter.loadMoreFail();
            }
        }
        mSrlSearchResult.setEnabled(true);
    }

    private void initRecyclerView() {

        LinearLayoutManager hotCardBagLayoutManager = new LinearLayoutManager(mActivity, LinearLayoutManager.HORIZONTAL, false);
        LinearLayoutManager hotCardLayoutManager = new LinearLayoutManager(mActivity, LinearLayoutManager.VERTICAL, false);
        mCardAdapter = new SearchResultCardAdapter(mList);
        mCardAdapter.setLoadMoreView(new CustomLoadMoreView());
        mCardAdapter.setOnLoadMoreListener(this, mRvSearchResultCard);
        mCardAdapter.setOnItemClickListener(new CardItemClickListener(mActivity));
        mRvSearchResultCard.setLayoutManager(hotCardLayoutManager);
        mRvSearchResultCard.setAdapter(mCardAdapter);

        View view = View.inflate(mActivity, R.layout.head_search_result, null);
        mRvSearchResultCardBag = (RecyclerView) view.findViewById(R.id.rv_search_result_card_bag);
        mLLHeadSearchResultCardBag = (LinearLayout) view.findViewById(R.id.ll_head_search_result_card_bag);
        mCardBagAdapter = new SearchResultCardBagAdapter(mList);
        mCardBagAdapter.setOnItemClickListener(new CardBagItemClickListener(mActivity));
        mRvSearchResultCardBag.setLayoutManager(hotCardBagLayoutManager);
        mRvSearchResultCardBag.setAdapter(mCardBagAdapter);
        mRvSearchResultCardBag.addItemDecoration(new HomeCardBagItemDecoration());

        mCardAdapter.addHeaderView(view);
    }

    private void getData() {
        for (int i = 0; i < 10; i++) {
            mList.add(new SearchResultBean());
        }
    }

    @OnClick(R.id.tv_search_result_cancel)
    public void onViewClicked() {
        finish();
    }

    class SearchListener implements TextView.OnEditorActionListener {
        @Override
        public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
            //此处会响应2次 分别为ACTION_DOWN和ACTION_UP
            if (actionId == EditorInfo.IME_ACTION_SEARCH
                    || actionId == EditorInfo.IME_ACTION_DONE
                    || (event != null && KeyEvent.KEYCODE_ENTER == event.getKeyCode() && KeyEvent.ACTION_DOWN == event.getAction())) {

                Utils.closeInputMethod(mEtSearchResult);

                return true;
            }
            return false;
        }
    }

    static class CardBagItemClickListener implements BaseQuickAdapter.OnItemClickListener {

        private Context mContext;

        public CardBagItemClickListener(Context context) {
            mContext = context;
        }

        @Override
        public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
            Intent intent = new Intent(mContext, CardBagActivity.class);
            mContext.startActivity(intent);
        }
    }

    static class CardItemClickListener implements BaseQuickAdapter.OnItemClickListener {

        private Context mContext;

        public CardItemClickListener(Context context) {
            mContext = context;
        }

        @Override
        public void onItemClick(BaseQuickAdapter adapter, View view, int position) {

        }
    }
}
