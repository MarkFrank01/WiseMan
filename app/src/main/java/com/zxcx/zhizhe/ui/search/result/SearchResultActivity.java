package com.zxcx.zhizhe.ui.search.result;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.zxcx.zhizhe.R;
import com.zxcx.zhizhe.mvpBase.MvpActivity;
import com.zxcx.zhizhe.ui.card.card.newCardDetails.CardDetailsActivity;
import com.zxcx.zhizhe.ui.card.cardBag.CardBagActivity;
import com.zxcx.zhizhe.ui.home.hot.itemDecoration.HomeCardBagItemDecoration;
import com.zxcx.zhizhe.ui.search.result.adapter.SearchResultCardAdapter;
import com.zxcx.zhizhe.ui.search.result.adapter.SearchResultCardBagAdapter;
import com.zxcx.zhizhe.utils.Constants;
import com.zxcx.zhizhe.utils.StringUtils;
import com.zxcx.zhizhe.utils.Utils;
import com.zxcx.zhizhe.widget.CustomLoadMoreView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SearchResultActivity extends MvpActivity<SearchResultPresenter> implements SearchResultContract.View,
        BaseQuickAdapter.RequestLoadMoreListener, SwipeRefreshLayout.OnRefreshListener {

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
    private String mKeyword;
    private int page = 0;
    private View mHeaderView;
    private TextView mTvHeadCard;
    private View mViewHeadInterval;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_result);
        ButterKnife.bind(this);

        initData();

        initRecyclerView();
        mEtSearchResult.setOnEditorActionListener(new SearchListener());
        mSrlSearchResult.setOnRefreshListener(this);
        mSrlSearchResult.setColorSchemeColors(ContextCompat.getColor(mActivity, R.color.button_blue));
        onRefresh();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    private void initData() {
        mKeyword = getIntent().getStringExtra("keyword");
        mEtSearchResult.setText(mKeyword);
    }

    @Override
    protected SearchResultPresenter createPresenter() {
        return new SearchResultPresenter(this);
    }

    @Override
    public void onRefresh() {
        page = 0;
        searchCardBag();
        searchCard();
    }

    @Override
    public void onLoadMoreRequested() {
        mSrlSearchResult.setEnabled(false);
        searchCard();
        mSrlSearchResult.setEnabled(true);
    }

    @Override
    public void searchCardBagSuccess(List<SearchCardBagBean> list) {
        mCardBagAdapter.setNewData(list);
        checkNullShow();
    }

    @Override
    public void getDataSuccess(List<SearchCardBean> list) {
        if (mSrlSearchResult.isRefreshing()) {
            mSrlSearchResult.setRefreshing(false);
        }
        if (page == 0){
            mCardAdapter.setNewData(new ArrayList<SearchCardBean>());
        }
        page++;
        mCardAdapter.addData(list);
        if (list.size() < Constants.PAGE_SIZE){
            mCardAdapter.loadMoreEnd(false);
        }else {
            mCardAdapter.loadMoreComplete();
            mCardAdapter.setEnableLoadMore(false);
            mCardAdapter.setEnableLoadMore(true);
        }
        checkNullShow();
    }

    private void checkNullShow() {
        if (mCardBagAdapter.getData().size() == 0 && mCardAdapter.getData().size() == 0){
            mHeaderView.setVisibility(View.GONE);
            View emptyView = LayoutInflater.from(mActivity).inflate(R.layout.view_no_data, null);
            mCardAdapter.setEmptyView(emptyView);
        }else if (mCardBagAdapter.getData().size() == 0){
            mLLHeadSearchResultCardBag.setVisibility(View.GONE);
            mTvHeadCard.setVisibility(View.VISIBLE);
            mViewHeadInterval.setVisibility(View.VISIBLE);
        }else if (mCardAdapter.getData().size() == 0){
            mLLHeadSearchResultCardBag.setVisibility(View.VISIBLE);
            mCardAdapter.isUseEmpty(false);
            mTvHeadCard.setVisibility(View.GONE);
            mViewHeadInterval.setVisibility(View.GONE);
        }else {

        }
    }

    private void initRecyclerView() {

        LinearLayoutManager hotCardBagLayoutManager = new LinearLayoutManager(mActivity, LinearLayoutManager.HORIZONTAL, false);
        LinearLayoutManager hotCardLayoutManager = new LinearLayoutManager(mActivity, LinearLayoutManager.VERTICAL, false);
        mCardAdapter = new SearchResultCardAdapter(new ArrayList<SearchCardBean>(), mKeyword);
        mCardAdapter.setLoadMoreView(new CustomLoadMoreView());
        mCardAdapter.setOnLoadMoreListener(this, mRvSearchResultCard);
        mCardAdapter.setOnItemClickListener(new CardItemClickListener(mActivity));
        mRvSearchResultCard.setLayoutManager(hotCardLayoutManager);
        mRvSearchResultCard.setAdapter(mCardAdapter);

        mHeaderView = LayoutInflater.from(mActivity).inflate(R.layout.head_search_result, null);
        mRvSearchResultCardBag = (RecyclerView) mHeaderView.findViewById(R.id.rv_search_result_card_bag);
        mLLHeadSearchResultCardBag = (LinearLayout) mHeaderView.findViewById(R.id.ll_head_search_result_card_bag);
        mTvHeadCard = (TextView) mHeaderView.findViewById(R.id.tv_head_search_card);
        mViewHeadInterval = mHeaderView.findViewById(R.id.view_head_search_result);

        mCardBagAdapter = new SearchResultCardBagAdapter(new ArrayList<SearchCardBagBean>(),mKeyword);
        mCardBagAdapter.setOnItemClickListener(new CardBagItemClickListener(mActivity));
        mRvSearchResultCardBag.setLayoutManager(hotCardBagLayoutManager);
        mRvSearchResultCardBag.setAdapter(mCardBagAdapter);
        mRvSearchResultCardBag.addItemDecoration(new HomeCardBagItemDecoration());

        mCardAdapter.addHeaderView(mHeaderView);
        View emptyView = LayoutInflater.from(mActivity).inflate(R.layout.view_no_data, null);
        mCardAdapter.setEmptyView(emptyView);
        mCardAdapter.setHeaderAndEmpty(true);
    }

    @OnClick(R.id.tv_search_result_cancel)
    public void onViewClicked() {
        Utils.closeInputMethod(mEtSearchResult);
        finish();
    }

    private void searchCard() {
        mPresenter.searchCard(mKeyword,page, Constants.PAGE_SIZE);
    }

    private void searchCardBag() {
        mPresenter.searchCardBag(mKeyword);
    }

    class SearchListener implements TextView.OnEditorActionListener {
        @Override
        public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
            //此处会响应2次 分别为ACTION_DOWN和ACTION_UP
            if (actionId == EditorInfo.IME_ACTION_SEARCH
                    || actionId == EditorInfo.IME_ACTION_DONE
                    || (event != null && KeyEvent.KEYCODE_ENTER == event.getKeyCode() && KeyEvent.ACTION_DOWN == event.getAction())) {

                if (StringUtils.isEmpty(v.getText().toString())){
                    toastShow("搜索内容不能为空！");
                    return true;
                }
                mKeyword = v.getText().toString();
                mCardAdapter.setKeyword(mKeyword);
                mCardBagAdapter.setKeyword(mKeyword);
                onRefresh();
                Utils.closeInputMethod(mEtSearchResult);

                return true;
            }
            return false;
        }
    }

    private static class CardBagItemClickListener implements BaseQuickAdapter.OnItemClickListener {

        private Context mContext;

        public CardBagItemClickListener(Context context) {
            mContext = context;
        }

        @Override
        public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
            SearchCardBagBean bean = (SearchCardBagBean) adapter.getData().get(position);
            Intent intent = new Intent(mContext, CardBagActivity.class);
            intent.putExtra("id",bean.getId());
            intent.putExtra("name",bean.getName());
            mContext.startActivity(intent);
        }
    }

    private static class CardItemClickListener implements BaseQuickAdapter.OnItemClickListener {

        private Context mContext;

        public CardItemClickListener(Context context) {
            mContext = context;
        }

        @Override
        public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
            SearchCardBean bean = (SearchCardBean) adapter.getData().get(position);
            Intent intent = new Intent(mContext, CardDetailsActivity.class);
            intent.putExtra("id",bean.getId());
            intent.putExtra("name",bean.getName());
            mContext.startActivity(intent);
        }
    }
}
