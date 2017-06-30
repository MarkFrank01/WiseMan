package com.zxcx.shitang.ui.card.cardBag;

import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.zxcx.shitang.R;
import com.zxcx.shitang.mvpBase.MvpActivity;
import com.zxcx.shitang.ui.card.cardBag.adapter.CardBagCardAdapter;
import com.zxcx.shitang.ui.card.cardBag.adapter.CardBagListAdapter;
import com.zxcx.shitang.ui.card.cardBag.itemDecoration.CardBagCardItemDecoration;
import com.zxcx.shitang.ui.my.collect.collectFolder.CollectFolderBean;
import com.zxcx.shitang.widget.CustomLoadMoreView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.zxcx.shitang.App.getContext;

public class CardBagActivity extends MvpActivity<CardBagPresenter> implements CardBagContract.View,
        BaseQuickAdapter.RequestLoadMoreListener, SwipeRefreshLayout.OnRefreshListener {

    private static final int TOTAL_COUNTER = 20;
    @BindView(R.id.rv_card_bag_card)
    RecyclerView mRvCardBagCard;
    @BindView(R.id.rv_card_bag_list)
    RecyclerView mRvCardBagList;
    @BindView(R.id.srl_card_bag)
    SwipeRefreshLayout mSrlCardBag;
    @BindView(R.id.iv_toolbar_right)
    ImageView mIvToolbarRight;
    private List<CollectFolderBean> mList = new ArrayList<>();
    private CardBagCardAdapter mCardBagCardAdapter;
    private CardBagListAdapter mCardBagListAdapter;
    private boolean isErr = false;
    private boolean isCard = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_card_bag);
        ButterKnife.bind(this);
        initToolBar("笔记本");

        getData();
        initRecyclerView();

        mSrlCardBag.setOnRefreshListener(this);
        mSrlCardBag.setColorSchemeColors(ContextCompat.getColor(getContext(), R.color.colorPrimary));
        mIvToolbarRight.setImageResource(R.drawable.iv_card_bag_list);
        mIvToolbarRight.setOnClickListener(this);
    }

    private void initRecyclerView() {
        mCardBagCardAdapter = new CardBagCardAdapter(mList);
        mCardBagCardAdapter.setLoadMoreView(new CustomLoadMoreView());
        mCardBagCardAdapter.setOnLoadMoreListener(this, mRvCardBagCard);
        mRvCardBagCard.setLayoutManager(new GridLayoutManager(getContext(), 2));
        mRvCardBagCard.setAdapter(mCardBagCardAdapter);
        mRvCardBagCard.addItemDecoration(new CardBagCardItemDecoration());

        mCardBagListAdapter = new CardBagListAdapter(mList);
        mCardBagListAdapter.setLoadMoreView(new CustomLoadMoreView());
        mCardBagListAdapter.setOnLoadMoreListener(this, mRvCardBagCard);
        mRvCardBagList.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        mRvCardBagList.setAdapter(mCardBagListAdapter);
    }

    @Override
    protected CardBagPresenter createPresenter() {
        return new CardBagPresenter(this);
    }

    @Override
    public void getDataSuccess(CardBagBean bean) {

    }

    @Override
    public void onRefresh() {
        isErr = false;
        mList.clear();
        getData();
        mCardBagCardAdapter.notifyDataSetChanged();
        mCardBagCardAdapter.setEnableLoadMore(false);
        mCardBagCardAdapter.setEnableLoadMore(true);
        if (mSrlCardBag.isRefreshing()) {
            mSrlCardBag.setRefreshing(false);
        }
    }

    @Override
    public void onLoadMoreRequested() {
        mSrlCardBag.setEnabled(false);
        if (mCardBagCardAdapter.getData().size() > TOTAL_COUNTER) {
            mCardBagCardAdapter.loadMoreEnd(false);
        } else {
            if (isErr) {
                getData();
//                mHotCardAdapter.addData();
                mCardBagCardAdapter.notifyDataSetChanged();
                mCardBagCardAdapter.loadMoreComplete();
            } else {
                isErr = true;
                Toast.makeText(getContext(), "网络错误", Toast.LENGTH_LONG).show();
                mCardBagCardAdapter.loadMoreFail();
            }
        }
        mSrlCardBag.setEnabled(true);
    }

    private void getData() {
        for (int i = 0; i < 10; i++) {
            mList.add(new CollectFolderBean());
        }
    }

    class OnRightClickListener implements View.OnClickListener{

        @Override
        public void onClick(View v) {
            if (isCard){
                isCard = !isCard;
                mIvToolbarRight.setImageResource(R.drawable.iv_card_bag_card);

            }else {
                isCard = !isCard;
                mIvToolbarRight.setImageResource(R.drawable.iv_card_bag_list);
            }
        }
    }

}
