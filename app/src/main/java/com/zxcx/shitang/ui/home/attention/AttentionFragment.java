package com.zxcx.shitang.ui.home.attention;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.zxcx.shitang.R;
import com.zxcx.shitang.mvpBase.MvpFragment;
import com.zxcx.shitang.ui.card.cardBag.CardBagActivity;
import com.zxcx.shitang.ui.home.hot.HotBean;
import com.zxcx.shitang.ui.home.hot.itemDecoration.HomeCardBagItemDecoration;
import com.zxcx.shitang.ui.home.hot.itemDecoration.HomeCardItemDecoration;
import com.zxcx.shitang.ui.home.hot.adapter.HotCardAdapter;
import com.zxcx.shitang.ui.home.hot.adapter.HotCardBagAdapter;
import com.zxcx.shitang.widget.CustomLoadMoreView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class AttentionFragment extends MvpFragment<AttentionPresenter> implements AttentionContract.View,
        BaseQuickAdapter.RequestLoadMoreListener, SwipeRefreshLayout.OnRefreshListener {

    private static final int TOTAL_COUNTER = 30;
    private static AttentionFragment fragment = null;
    RecyclerView mRvAttentionCardBag;
    @BindView(R.id.rv_attention_card)
    RecyclerView mRvAttentionCard;
    Unbinder unbinder;
    @BindView(R.id.srl_attention_card)
    SwipeRefreshLayout mSrlAttentionCard;

    private HotCardBagAdapter mHotCardBagAdapter;
    private HotCardAdapter mHotCardAdapter;
    private List<HotBean> mList = new ArrayList<>();
    private boolean isErr = false;

    public static AttentionFragment newInstance() {
        if (fragment == null) {
            fragment = new AttentionFragment();
        }
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_attention, container, false);
        unbinder = ButterKnife.bind(this, root);
        return root;
    }

    @Override
    protected AttentionPresenter createPresenter() {
        return new AttentionPresenter(this);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        getData();

        initRecyclerView();
        mSrlAttentionCard.setOnRefreshListener(this);
        mSrlAttentionCard.setColorSchemeColors(ContextCompat.getColor(getContext(), R.color.colorPrimary));
    }

    @Override
    public void getDataSuccess(AttentionBean bean) {

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public void onLoadMoreRequested() {
        mSrlAttentionCard.setEnabled(false);
        if (mHotCardAdapter.getData().size() > TOTAL_COUNTER) {
            mHotCardAdapter.loadMoreEnd(false);
        } else {
            if (isErr) {
                getData();
//                mHotCardAdapter.addData();
                mHotCardAdapter.notifyDataSetChanged();
                mHotCardAdapter.loadMoreComplete();
            } else {
                isErr = true;
                Toast.makeText(getContext(), "网络错误", Toast.LENGTH_LONG).show();
                mHotCardAdapter.loadMoreFail();
            }
        }
        mSrlAttentionCard.setEnabled(true);
    }

    private void initRecyclerView() {

        LinearLayoutManager hotCardBagLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        GridLayoutManager hotCardLayoutManager = new GridLayoutManager(getContext(), 2);
        mHotCardAdapter = new HotCardAdapter(mList);
        mHotCardAdapter.setLoadMoreView(new CustomLoadMoreView());
        mHotCardAdapter.setOnLoadMoreListener(this, mRvAttentionCard);
        mHotCardAdapter.setOnItemClickListener(new CardItemClickListener(mActivity));
        mRvAttentionCard.setLayoutManager(hotCardLayoutManager);
        mRvAttentionCard.setAdapter(mHotCardAdapter);
        mRvAttentionCard.addItemDecoration(new HomeCardItemDecoration());

        View view = View.inflate(getContext(),R.layout.head_home_attention,null);
        mRvAttentionCardBag = (RecyclerView) view.findViewById(R.id.rv_attention_card_bag);
        mHotCardBagAdapter = new HotCardBagAdapter(mList);
        mHotCardBagAdapter.setOnItemClickListener(new CardBagItemClickListener(mActivity));
        mRvAttentionCardBag.setLayoutManager(hotCardBagLayoutManager);
        mRvAttentionCardBag.setAdapter(mHotCardBagAdapter);
        mRvAttentionCardBag.addItemDecoration(new HomeCardBagItemDecoration());

        mHotCardAdapter.addHeaderView(view);
    }

    private void getData() {
        for (int i = 0; i < 10; i++) {
            mList.add(new HotBean());
        }
    }

    @Override
    public void onRefresh() {
        isErr = false;
        mHotCardAdapter.setEnableLoadMore(false);
        mHotCardAdapter.setEnableLoadMore(true);
        mList.clear();
        getData();
        mHotCardAdapter.notifyDataSetChanged();
        if (mSrlAttentionCard.isRefreshing()) {
            mSrlAttentionCard.setRefreshing(false);
        }
    }

    static class CardBagItemClickListener implements BaseQuickAdapter.OnItemClickListener{

        private Context mContext;

        public CardBagItemClickListener(Context context) {
            mContext  = context;
        }

        @Override
        public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
            Intent intent = new Intent(mContext, CardBagActivity.class);
            mContext.startActivity(intent);
        }
    }

    static class CardItemClickListener implements BaseQuickAdapter.OnItemClickListener{

        private Context mContext;

        public CardItemClickListener(Context context) {
            mContext  = context;
        }

        @Override
        public void onItemClick(BaseQuickAdapter adapter, View view, int position) {

        }
    }
}