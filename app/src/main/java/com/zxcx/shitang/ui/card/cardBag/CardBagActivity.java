package com.zxcx.shitang.ui.card.cardBag;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.zxcx.shitang.R;
import com.zxcx.shitang.mvpBase.MvpActivity;
import com.zxcx.shitang.ui.card.card.newCardDetails.CardDetailsActivity;
import com.zxcx.shitang.ui.card.cardBag.adapter.CardBagCardAdapter;
import com.zxcx.shitang.ui.card.cardBag.adapter.CardBagListAdapter;
import com.zxcx.shitang.ui.card.cardBag.itemDecoration.CardBagCardItemDecoration;
import com.zxcx.shitang.utils.Constants;
import com.zxcx.shitang.widget.CustomLoadMoreView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.zxcx.shitang.App.getContext;

public class CardBagActivity extends MvpActivity<CardBagPresenter> implements CardBagContract.View,
        BaseQuickAdapter.RequestLoadMoreListener, SwipeRefreshLayout.OnRefreshListener {

    @BindView(R.id.rv_card_bag_card)
    RecyclerView mRvCardBagCard;
    @BindView(R.id.rv_card_bag_list)
    RecyclerView mRvCardBagList;
    @BindView(R.id.srl_card_bag)
    SwipeRefreshLayout mSrlCardBag;
    @BindView(R.id.iv_toolbar_right)
    ImageView mIvToolbarRight;
    private List<CardBagBean> mList = new ArrayList<>();
    private CardBagCardAdapter mCardBagCardAdapter;
    private CardBagListAdapter mCardBagListAdapter;
    private boolean isCard = true;
    private int showFistItem;
    private GridLayoutManager mCardBagCardManager;
    private LinearLayoutManager mCardBagListManager;
    private int mId;
    private int page = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_card_bag);
        ButterKnife.bind(this);

        initData();
        initRecyclerView();

        mSrlCardBag.setOnRefreshListener(this);
        mSrlCardBag.setColorSchemeColors(ContextCompat.getColor(getContext(), R.color.button_blue));
        mIvToolbarRight.setVisibility(View.VISIBLE);
        mIvToolbarRight.setImageResource(R.drawable.iv_card_bag_list);
        onRefresh();
    }

    @Override
    protected CardBagPresenter createPresenter() {
        return new CardBagPresenter(this);
    }

    @Override
    public void onRefresh() {
        page = 0;
        mList.clear();
        getCardBagCardList();
    }

    @Override
    public void onLoadMoreRequested() {
        mSrlCardBag.setEnabled(false);
        getCardBagCardList();
        mSrlCardBag.setEnabled(true);
    }

    @Override
    public void getDataSuccess(List<CardBagBean> list) {
        if (mSrlCardBag.isRefreshing()) {
            mSrlCardBag.setRefreshing(false);
        }
        if (page == 1){
            mCardBagCardAdapter.notifyDataSetChanged();
            mCardBagListAdapter.notifyDataSetChanged();
        }
        page++;
        mList.addAll(list);
        mCardBagCardAdapter.notifyDataSetChanged();
        mCardBagListAdapter.notifyDataSetChanged();
        if (list.size() < Constants.PAGE_SIZE){
            mCardBagCardAdapter.loadMoreEnd(false);
            mCardBagListAdapter.loadMoreEnd(false);
        }else {
            mCardBagCardAdapter.loadMoreComplete();
            mCardBagListAdapter.loadMoreComplete();
            mCardBagCardAdapter.setEnableLoadMore(false);
            mCardBagListAdapter.setEnableLoadMore(false);
            mCardBagCardAdapter.setEnableLoadMore(true);
            mCardBagListAdapter.setEnableLoadMore(true);
        }
        if (mCardBagCardAdapter.getData().size() == 0){
            //占空图
            View view = View.inflate(mActivity, R.layout.view_no_data, null);
            mCardBagCardAdapter.setEmptyView(view);
        }
        if (mCardBagListAdapter.getData().size() == 0){
            //占空图
            View view = View.inflate(mActivity, R.layout.view_no_data, null);
            mCardBagListAdapter.setEmptyView(view);
        }
    }

    @OnClick(R.id.iv_toolbar_right)
    public void onMIvToolbarRightClicked() {
        if (isCard) {
            isCard = !isCard;
            mRvCardBagCard.setVisibility(View.GONE);
            mRvCardBagList.setVisibility(View.VISIBLE);
            mIvToolbarRight.setImageResource(R.drawable.iv_card_bag_card);
            showFistItem = mCardBagCardManager.findFirstVisibleItemPosition();
            mCardBagListManager.scrollToPosition(showFistItem);
        } else {
            isCard = !isCard;
            mRvCardBagCard.setVisibility(View.VISIBLE);
            mRvCardBagList.setVisibility(View.GONE);
            mIvToolbarRight.setImageResource(R.drawable.iv_card_bag_list);
            showFistItem = mCardBagListManager.findFirstVisibleItemPosition();
            mCardBagCardManager.scrollToPosition(showFistItem);
        }
    }

    private void getCardBagCardList() {
        mPresenter.getCardBagCardList(mId, page, Constants.PAGE_SIZE);
    }

    private void initData() {
        mId = getIntent().getIntExtra("id", 0);
        initToolBar(getIntent().getStringExtra("name"));
    }

    private void initRecyclerView() {
        mCardBagCardManager = new GridLayoutManager(getContext(), 2);
        mCardBagCardAdapter = new CardBagCardAdapter(mList);
        mCardBagCardAdapter.setLoadMoreView(new CustomLoadMoreView());
        mCardBagCardAdapter.setOnLoadMoreListener(this, mRvCardBagCard);
        mCardBagCardAdapter.setOnItemClickListener(new CardItemClickListener(this));
        mRvCardBagCard.setLayoutManager(mCardBagCardManager);
        mRvCardBagCard.setAdapter(mCardBagCardAdapter);
        mRvCardBagCard.addItemDecoration(new CardBagCardItemDecoration());
        mCardBagCardAdapter.setOnItemClickListener(new CardItemClickListener(this));

        mCardBagListManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        mCardBagListAdapter = new CardBagListAdapter(mList);
        mCardBagListAdapter.setLoadMoreView(new CustomLoadMoreView());
        mCardBagListAdapter.setOnLoadMoreListener(this, mRvCardBagList);
        mCardBagListAdapter.setOnItemClickListener(new CardItemClickListener(this));
        mRvCardBagList.setLayoutManager(mCardBagListManager);
        mRvCardBagList.setAdapter(mCardBagListAdapter);
    }

    static class CardItemClickListener implements BaseQuickAdapter.OnItemClickListener {

        private Context mContext;

        public CardItemClickListener(Context context) {
            mContext = context;
        }

        @Override
        public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
            CardBagBean bean = (CardBagBean) adapter.getData().get(position);
            Intent intent = new Intent(mContext, CardDetailsActivity.class);
            intent.putExtra("id",bean.getId());
            intent.putExtra("name",bean.getName());
            mContext.startActivity(intent);
        }
    }

}
