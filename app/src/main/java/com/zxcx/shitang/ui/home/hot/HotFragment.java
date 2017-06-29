package com.zxcx.shitang.ui.home.hot;

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

public class HotFragment extends MvpFragment<HotPresenter> implements HotContract.View,
        BaseQuickAdapter.RequestLoadMoreListener, SwipeRefreshLayout.OnRefreshListener {

    private static final int TOTAL_COUNTER = 30;
    private static HotFragment fragment = null;
    RecyclerView mRvHotCardBag;
    @BindView(R.id.rv_hot_card)
    RecyclerView mRvHotCard;
    Unbinder unbinder;
    @BindView(R.id.srl_hot_card)
    SwipeRefreshLayout mSrlHotCard;

    private HotCardBagAdapter mHotCardBagAdapter;
    private HotCardAdapter mHotCardAdapter;
    private List<HotBean> mList = new ArrayList<>();
    private boolean isErr = false;

    public static HotFragment newInstance() {
        if (fragment == null) {
            fragment = new HotFragment();
        }
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_hot, container, false);
        unbinder = ButterKnife.bind(this, root);
        return root;
    }

    @Override
    protected HotPresenter createPresenter() {
        return new HotPresenter(this);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        getData();

        initRecyclerView();
        mSrlHotCard.setOnRefreshListener(this);
        mSrlHotCard.setColorSchemeColors(ContextCompat.getColor(getContext(), R.color.colorPrimary));
    }

    @Override
    public void getDataSuccess(HotBean bean) {

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public void onRefresh() {
        isErr = false;
        mHotCardAdapter.setEnableLoadMore(true);
        mList.clear();
        getData();
        mHotCardAdapter.notifyDataSetChanged();
        if (mSrlHotCard.isRefreshing()) {
            mSrlHotCard.setRefreshing(false);
        }
    }

    @Override
    public void onLoadMoreRequested() {
        mSrlHotCard.setEnabled(false);
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
        mSrlHotCard.setEnabled(true);
    }

    private void initRecyclerView() {

        LinearLayoutManager hotCardBagLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        GridLayoutManager hotCardLayoutManager = new GridLayoutManager(getContext(), 2);
        mHotCardAdapter = new HotCardAdapter(R.layout.item_home_card, mList);
        mHotCardAdapter.setLoadMoreView(new CustomLoadMoreView());
        mHotCardAdapter.setOnLoadMoreListener(this, mRvHotCard);
        mRvHotCard.setLayoutManager(hotCardLayoutManager);
        mRvHotCard.setAdapter(mHotCardAdapter);
        mRvHotCard.addItemDecoration(new HomeCardItemDecoration());

        View view = View.inflate(getContext(),R.layout.head_home_hot,null);
        mRvHotCardBag = (RecyclerView) view.findViewById(R.id.rv_hot_card_bag);
        mHotCardBagAdapter = new HotCardBagAdapter(R.layout.item_home_card_bag, mList);
        mRvHotCardBag.setLayoutManager(hotCardBagLayoutManager);
        mRvHotCardBag.setAdapter(mHotCardBagAdapter);
        mRvHotCardBag.addItemDecoration(new HomeCardBagItemDecoration());

        mHotCardAdapter.addHeaderView(view);
    }

    private void getData() {
        for (int i = 0; i < 10; i++) {
            mList.add(new HotBean());
        }
    }
}