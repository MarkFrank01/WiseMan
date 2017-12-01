package com.zxcx.zhizhe.ui.home.hot;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.kingja.loadsir.callback.Callback;
import com.kingja.loadsir.core.LoadSir;
import com.zxcx.zhizhe.R;
import com.zxcx.zhizhe.event.GotoClassifyEvent;
import com.zxcx.zhizhe.event.HomeTopClickRefreshEvent;
import com.zxcx.zhizhe.loadCallback.HomeLoadingCallback;
import com.zxcx.zhizhe.loadCallback.LoginTimeoutCallback;
import com.zxcx.zhizhe.loadCallback.NetworkErrorCallback;
import com.zxcx.zhizhe.mvpBase.RefreshMvpFragment;
import com.zxcx.zhizhe.ui.card.card.newCardDetails.CardDetailsActivity;
import com.zxcx.zhizhe.ui.card.cardBag.CardBagActivity;
import com.zxcx.zhizhe.ui.home.hot.adapter.HotCardAdapter;
import com.zxcx.zhizhe.ui.home.hot.adapter.HotCardBagAdapter;
import com.zxcx.zhizhe.ui.home.hot.itemDecoration.HomeCardBagItemDecoration;
import com.zxcx.zhizhe.ui.home.hot.itemDecoration.HomeCardItemDecoration;
import com.zxcx.zhizhe.utils.Constants;
import com.zxcx.zhizhe.widget.CustomLoadMoreView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import in.srain.cube.views.ptr.PtrFrameLayout;

public class HotFragment extends RefreshMvpFragment<HotPresenter> implements HotContract.View,
        BaseQuickAdapter.RequestLoadMoreListener {

    RecyclerView mRvHotCardBag;
    @BindView(R.id.rv_hot_card)
    RecyclerView mRvHotCard;
    Unbinder unbinder;

    private HotCardBagAdapter mCardBagAdapter;
    private HotCardAdapter mCardAdapter;
    private int mPage = 0;
    private int mAppBarLayoutVerticalOffset;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_hot, container, false);
        unbinder = ButterKnife.bind(this, root);
        mRefreshLayout = (PtrFrameLayout) root.findViewById(R.id.refresh_layout);
        mRefreshLayout.disableWhenHorizontalMove(true);
        LoadSir loadSir = new LoadSir.Builder()
                .addCallback(new HomeLoadingCallback())
                .addCallback(new LoginTimeoutCallback())
                .addCallback(new NetworkErrorCallback())
                .setDefaultCallback(HomeLoadingCallback.class)
                .build();
        loadService = loadSir.register(root, new Callback.OnReloadListener() {
            @Override
            public void onReload(View v) {
                loadService.showCallback(HomeLoadingCallback.class);
                getHotCard();
                getHotCardBag();
            }
        });
        return loadService.getLoadLayout();
    }

    @Override
    protected HotPresenter createPresenter() {
        return new HotPresenter(this);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initRecyclerView();

        getHotCard();
        getHotCardBag();
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            EventBus.getDefault().register(this);
        } else {
            EventBus.getDefault().unregister(this);
        }
    }

    @Override
    public void onDestroyView() {
        unbinder.unbind();
        super.onDestroyView();
    }

    @Override
    public void clearLeaks() {
        mCardAdapter = null;
        mCardBagAdapter = null;
        mRvHotCardBag = null;
        loadService = null;
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(HomeTopClickRefreshEvent event) {
        mRvHotCard.scrollToPosition(0);
        mRefreshLayout.autoRefresh();
    }

    @Override
    public boolean checkCanDoRefresh(PtrFrameLayout frame, View content, View header) {
        return mAppBarLayoutVerticalOffset == 0 && !ViewCompat.canScrollVertically(frame.getContentView(), -1);
    }

    @Override
    public void onRefreshBegin(PtrFrameLayout frame) {
        mPage = 0;
        getHotCard();
        getHotCardBag();
    }

    @Override
    public void onLoadMoreRequested() {
        getHotCard();
    }

    @Override
    public void getHotCardBagSuccess(List<HotCardBagBean> list) {
        mCardBagAdapter.setNewData(list);
        loadService.showSuccess();
        if (mCardBagAdapter.getData().size() == 0) {
            //占空图
        }
    }

    @Override
    public void getDataSuccess(List<HotCardBean> list) {
        mRefreshLayout.refreshComplete();
        if (mPage == 0) {
            mCardAdapter.setNewData(list);
            mRvHotCard.scrollToPosition(0);
        } else {
            mCardAdapter.addData(list);
        }
        mPage++;
        if (list.size() < Constants.PAGE_SIZE) {
            mCardAdapter.loadMoreEnd(false);
        } else {
            mCardAdapter.loadMoreComplete();
            mCardAdapter.setEnableLoadMore(false);
            mCardAdapter.setEnableLoadMore(true);
        }
        if (mCardAdapter.getData().size() == 0) {
            //占空图
        }
    }

    @Override
    public void toastFail(String msg) {
        super.toastFail(msg);
        mCardAdapter.loadMoreFail();
        mRefreshLayout.refreshComplete();
        if (mPage == 0) {
            loadService.showCallback(NetworkErrorCallback.class);
        }
    }

    private void initRecyclerView() {

        LinearLayoutManager hotCardBagLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        StaggeredGridLayoutManager hotCardLayoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        mCardAdapter = new HotCardAdapter(new ArrayList<>());
        mCardAdapter.setLoadMoreView(new CustomLoadMoreView());
        mCardAdapter.setOnLoadMoreListener(this, mRvHotCard);
        mCardAdapter.setOnItemClickListener(new CardItemClickListener(mActivity));
        mCardAdapter.setOnItemChildClickListener(new CardTypeClickListener(mActivity));
        mRvHotCard.setLayoutManager(hotCardLayoutManager);
        mRvHotCard.setAdapter(mCardAdapter);
        mRvHotCard.addItemDecoration(new HomeCardItemDecoration());

        View view = LayoutInflater.from(mActivity).inflate(R.layout.head_home_hot, null);
        //查看全部按钮
        TextView textView = (TextView) view.findViewById(R.id.tv_hot_goto_classify);
        textView.setOnClickListener(v ->
                EventBus.getDefault().post(new GotoClassifyEvent()));
        mRvHotCardBag = (RecyclerView) view.findViewById(R.id.rv_hot_card_bag);
        mCardBagAdapter = new HotCardBagAdapter(new ArrayList<>());
        mCardBagAdapter.setOnItemClickListener(new CardBagItemClickListener(mActivity));
        mRvHotCardBag.setLayoutManager(hotCardBagLayoutManager);
        mRvHotCardBag.setAdapter(mCardBagAdapter);
        mRvHotCardBag.addItemDecoration(new HomeCardBagItemDecoration());

        mCardAdapter.addHeaderView(view);
        mCardAdapter.setHeaderAndEmpty(true);
    }

    private void getHotCard() {
        mPresenter.getHotCard(mPage, Constants.PAGE_SIZE);
    }

    private void getHotCardBag() {
        mPresenter.getHotCardBag();
    }

    public void setAppBarLayoutVerticalOffset(int appBarLayoutVerticalOffset) {
        this.mAppBarLayoutVerticalOffset = appBarLayoutVerticalOffset;
    }

    private static class CardBagItemClickListener implements BaseQuickAdapter.OnItemClickListener {

        private Context mContext;

        public CardBagItemClickListener(Context context) {
            mContext = context;
        }

        @Override
        public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
            HotCardBagBean bean = (HotCardBagBean) adapter.getData().get(position);
            Intent intent = new Intent(mContext, CardBagActivity.class);
            intent.putExtra("id", bean.getId());
            intent.putExtra("name", bean.getName());
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
            HotCardBean bean = (HotCardBean) adapter.getData().get(position);
            Intent intent = new Intent(mContext, CardDetailsActivity.class);
            intent.putExtra("id", bean.getId());
            intent.putExtra("name", bean.getName());
            mContext.startActivity(intent);
        }
    }

    private static class CardTypeClickListener implements BaseQuickAdapter.OnItemChildClickListener {

        private Context mContext;

        public CardTypeClickListener(Context context) {
            mContext = context;
        }

        @Override
        public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
            HotCardBean bean = (HotCardBean) adapter.getData().get(position);
            Intent intent = new Intent(mContext, CardBagActivity.class);
            intent.putExtra("id", bean.getBagId());
            intent.putExtra("name", bean.getBagName());
            mContext.startActivity(intent);
        }
    }
}