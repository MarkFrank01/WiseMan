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

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.zxcx.shitang.R;
import com.zxcx.shitang.mvpBase.MvpActivity;
import com.zxcx.shitang.ui.card.card.newCardDetails.CardDetailsActivity;
import com.zxcx.shitang.ui.card.cardBag.CardBagActivity;
import com.zxcx.shitang.ui.home.hot.itemDecoration.HomeCardBagItemDecoration;
import com.zxcx.shitang.ui.search.result.adapter.SearchResultCardAdapter;
import com.zxcx.shitang.ui.search.result.adapter.SearchResultCardBagAdapter;
import com.zxcx.shitang.utils.Constants;
import com.zxcx.shitang.utils.StringUtils;
import com.zxcx.shitang.utils.Utils;
import com.zxcx.shitang.widget.CustomLoadMoreView;

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
    private int page = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_result);
        ButterKnife.bind(this);

        initData();

        initRecyclerView();
        mEtSearchResult.setOnEditorActionListener(new SearchListener());
        mSrlSearchResult.setOnRefreshListener(this);
        mSrlSearchResult.setColorSchemeColors(ContextCompat.getColor(mActivity, R.color.colorPrimaryFinal));
    }

    @Override
    protected SearchResultPresenter createPresenter() {
        return new SearchResultPresenter(this);
    }

    @Override
    public void onRefresh() {
        page = 1;
        mCardAdapter.setEnableLoadMore(false);
        mCardAdapter.setEnableLoadMore(true);
        mCardAdapter.getData().clear();
        mCardBagAdapter.getData().clear();
        searchCard();
        searchCardBag();
    }

    @Override
    public void onLoadMoreRequested() {
        mSrlSearchResult.setEnabled(false);
        searchCard();
        mSrlSearchResult.setEnabled(true);
    }

    @Override
    public void searchCardBagSuccess(List<SearchCardBagBean> list) {
        if (page == 1){
            mCardBagAdapter.notifyDataSetChanged();
        }
        mCardBagAdapter.addData(list);
        if (mCardBagAdapter.getData().size() == 0){
            View view = View.inflate(mActivity,R.layout.view_no_data,null);
            mCardBagAdapter.setEmptyView(view);
        }
    }

    @Override
    public void getDataSuccess(List<SearchCardBean> list) {
        if (mSrlSearchResult.isRefreshing()) {
            mSrlSearchResult.setRefreshing(false);
        }
        if (page == 1){
            mCardAdapter.notifyDataSetChanged();
        }
        page++;
        mCardAdapter.addData(list);
        if (list.size() < Constants.PAGE_SIZE){
            mCardAdapter.loadMoreEnd(false);
        }else {
            mCardAdapter.loadMoreComplete();
        }
        if (mCardBagAdapter.getData().size() == 0){
            View view = View.inflate(mActivity,R.layout.view_no_data,null);
            mCardAdapter.setEmptyView(view);
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

        View view = View.inflate(mActivity, R.layout.head_search_result, null);
        mRvSearchResultCardBag = (RecyclerView) view.findViewById(R.id.rv_search_result_card_bag);
        mLLHeadSearchResultCardBag = (LinearLayout) view.findViewById(R.id.ll_head_search_result_card_bag);
        mCardBagAdapter = new SearchResultCardBagAdapter(new ArrayList<SearchCardBagBean>(),mKeyword);
        mCardBagAdapter.setOnItemClickListener(new CardBagItemClickListener(mActivity));
        mRvSearchResultCardBag.setLayoutManager(hotCardBagLayoutManager);
        mRvSearchResultCardBag.setAdapter(mCardBagAdapter);
        mRvSearchResultCardBag.addItemDecoration(new HomeCardBagItemDecoration());

        mCardAdapter.addHeaderView(view);
    }

    @OnClick(R.id.tv_search_result_cancel)
    public void onViewClicked() {
        finish();
    }

    private void initData() {
        mKeyword = getIntent().getStringExtra("keyword");
        mEtSearchResult.setText(mKeyword);
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
