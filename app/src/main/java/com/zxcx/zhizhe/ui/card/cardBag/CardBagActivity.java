package com.zxcx.zhizhe.ui.card.cardBag;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.kingja.loadsir.core.LoadSir;
import com.zxcx.zhizhe.R;
import com.zxcx.zhizhe.loadCallback.CardBagLoadingCallback;
import com.zxcx.zhizhe.loadCallback.LoginTimeoutCallback;
import com.zxcx.zhizhe.loadCallback.NetworkErrorCallback;
import com.zxcx.zhizhe.mvpBase.RefreshMvpActivity;
import com.zxcx.zhizhe.ui.card.card.newCardDetails.CardDetailsActivity;
import com.zxcx.zhizhe.ui.card.cardBag.adapter.CardBagCardAdapter;
import com.zxcx.zhizhe.ui.card.cardBag.adapter.CardBagListAdapter;
import com.zxcx.zhizhe.utils.Constants;
import com.zxcx.zhizhe.utils.DateTimeUtils;
import com.zxcx.zhizhe.widget.CustomLoadMoreView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import in.srain.cube.views.ptr.PtrFrameLayout;

public class CardBagActivity extends RefreshMvpActivity<CardBagPresenter> implements CardBagContract.View,
        BaseQuickAdapter.RequestLoadMoreListener {


    @BindView(R.id.iv_toolbar_back)
    ImageView mIvToolbarBack;
    @BindView(R.id.toolbar_title)
    TextView mToolbarTitle;
    @BindView(R.id.iv_toolbar_right)
    ImageView mIvToolbarRight;
    @BindView(R.id.rv_card_bag_card)
    RecyclerView mRvCardBagCard;
    private CardBagCardAdapter mCardBagCardAdapter;
    private CardBagListAdapter mCardBagListAdapter;
    private CardBagCardItemDecoration mCardItemDecoration = new CardBagCardItemDecoration();
    private CardBagListItemDecoration mListItemDecoration = new CardBagListItemDecoration();
    private boolean isCard = true;
    private int showFistItem;
    private LinearLayoutManager mCardBagCardManager;
    private int mId;
    private int page = 0;
    private String mName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_card_bag);
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
        initData();
        initRecyclerView();
        mIvToolbarRight.setVisibility(View.VISIBLE);
        mIvToolbarRight.setImageResource(R.drawable.iv_card_bag_list);
        onRefresh();

        initLoadSir();
    }

    @Override
    protected CardBagPresenter createPresenter() {
        return new CardBagPresenter(this);
    }

    public void initLoadSir() {
        LoadSir loadSir = new LoadSir.Builder()
                .addCallback(new CardBagLoadingCallback())
                .addCallback(new NetworkErrorCallback())
                .addCallback(new LoginTimeoutCallback())
                .setDefaultCallback(CardBagLoadingCallback.class)
                .build();
        loadService = loadSir.register(this, this);
    }

    @Override
    public void onReload(View v) {
        loadService.showCallback(CardBagLoadingCallback.class);
        onRefresh();
    }

    @Override
    public void onRefreshBegin(PtrFrameLayout frame) {
        onRefresh();
    }

    public void onRefresh() {
        page = 0;
        getCardBagCardList();
    }

    @Override
    public void onLoadMoreRequested() {
        getCardBagCardList();
    }

    @Override
    public void getDataSuccess(List<CardBagBean> list) {
        loadService.showSuccess();
        mRefreshLayout.refreshComplete();
        if (page == 0) {
            mCardBagCardAdapter.setNewData(list);
            mCardBagListAdapter.setNewData(list);
        } else {
            mCardBagCardAdapter.addData(list);
            mCardBagListAdapter.addData(list);
        }
        page++;
        if (list.size() < Constants.PAGE_SIZE) {
            mCardBagCardAdapter.loadMoreEnd(false);
            mCardBagListAdapter.loadMoreEnd(false);
        } else {
            mCardBagCardAdapter.loadMoreComplete();
            mCardBagListAdapter.loadMoreComplete();
            mCardBagCardAdapter.setEnableLoadMore(false);
            mCardBagListAdapter.setEnableLoadMore(false);
            mCardBagCardAdapter.setEnableLoadMore(true);
            mCardBagListAdapter.setEnableLoadMore(true);
        }
    }

    @Override
    public void toastFail(String msg) {
        mRefreshLayout.refreshComplete();
        super.toastFail(msg);
        if (page == 0) {
            loadService.showCallback(NetworkErrorCallback.class);
        }
    }

    @OnClick(R.id.iv_toolbar_right)
    public void onMIvToolbarRightClicked() {
        if (isCard) {
            isCard = !isCard;
            showFistItem = mCardBagCardManager.findFirstVisibleItemPosition();
            mRvCardBagCard.setAdapter(mCardBagListAdapter);
            mRvCardBagCard.removeItemDecoration(mCardItemDecoration);
            mRvCardBagCard.addItemDecoration(mListItemDecoration);
            mIvToolbarRight.setImageResource(R.drawable.iv_card_bag_card);
            mCardBagCardManager.scrollToPosition(showFistItem);
        } else {
            isCard = !isCard;
            showFistItem = mCardBagCardManager.findFirstVisibleItemPosition();
            mRvCardBagCard.setAdapter(mCardBagCardAdapter);
            mRvCardBagCard.removeItemDecoration(mListItemDecoration);
            mRvCardBagCard.addItemDecoration(mCardItemDecoration);
            mIvToolbarRight.setImageResource(R.drawable.iv_card_bag_list);
            mCardBagCardManager.scrollToPosition(showFistItem);
        }
    }

    private void getCardBagCardList() {
        mPresenter.getCardBagCardList(mId, page, Constants.PAGE_SIZE);
    }

    private void initData() {
        mId = getIntent().getIntExtra("id", 0);
        mName = getIntent().getStringExtra("name");
        initToolBar(mName);
    }

    private void initRecyclerView() {
        mCardBagCardManager = new LinearLayoutManager(mActivity,LinearLayoutManager.VERTICAL,false);
        mCardBagCardAdapter = new CardBagCardAdapter(new ArrayList<>());
        mCardBagCardAdapter.setLoadMoreView(new CustomLoadMoreView());
        mCardBagCardAdapter.setOnLoadMoreListener(this, mRvCardBagCard);
        mCardBagCardAdapter.setOnItemClickListener(new CardItemClickListener(this));
        mRvCardBagCard.setLayoutManager(mCardBagCardManager);
        mRvCardBagCard.setAdapter(mCardBagCardAdapter);
        mRvCardBagCard.addItemDecoration(mCardItemDecoration);
        mCardBagCardAdapter.setOnItemClickListener(new CardItemClickListener(this));

        mCardBagListAdapter = new CardBagListAdapter(new ArrayList<>());
        mCardBagListAdapter.setLoadMoreView(new CustomLoadMoreView());
        mCardBagListAdapter.setOnLoadMoreListener(this, mRvCardBagCard);
        mCardBagListAdapter.setOnItemClickListener(new CardItemClickListener(this));

        View view = LayoutInflater.from(mActivity).inflate(R.layout.layout_no_data, null);
        mCardBagCardAdapter.setEmptyView(view);
    }

    class CardItemClickListener implements BaseQuickAdapter.OnItemClickListener {

        private Context mContext;

        public CardItemClickListener(Context context) {
            mContext = context;
        }

        @Override
        public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
            CardBagBean bean = (CardBagBean) adapter.getData().get(position);
            Intent intent = new Intent(mContext, CardDetailsActivity.class);
            intent.putExtra("id", bean.getId());
            intent.putExtra("name", bean.getName());
            intent.putExtra("imageUrl", bean.getImageUrl());
            intent.putExtra("date", DateTimeUtils.getDateString(bean.getDate()));
            intent.putExtra("author", bean.getAuthor());
            mContext.startActivity(intent);
        }
    }

}
