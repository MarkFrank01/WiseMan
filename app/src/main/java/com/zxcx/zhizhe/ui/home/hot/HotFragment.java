package com.zxcx.zhizhe.ui.home.hot;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.util.Pair;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.kingja.loadsir.core.LoadSir;
import com.zxcx.zhizhe.R;
import com.zxcx.zhizhe.event.HomeClickRefreshEvent;
import com.zxcx.zhizhe.loadCallback.HomeLoadingCallback;
import com.zxcx.zhizhe.loadCallback.LoginTimeoutCallback;
import com.zxcx.zhizhe.loadCallback.HomeNetworkErrorCallback;
import com.zxcx.zhizhe.mvpBase.RefreshMvpFragment;
import com.zxcx.zhizhe.ui.card.card.cardDetails.CardDetailsActivity;
import com.zxcx.zhizhe.ui.card.cardBag.CardBagActivity;
import com.zxcx.zhizhe.ui.home.hot.adapter.HotCardAdapter;
import com.zxcx.zhizhe.ui.home.hot.itemDecoration.HomeCardItemDecoration;
import com.zxcx.zhizhe.utils.DateTimeUtils;
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

    private HotCardAdapter mCardAdapter;
    private int mPage = 0;
    private int mAppBarLayoutVerticalOffset;
    private boolean mHidden = true;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_hot, container, false);
        unbinder = ButterKnife.bind(this, root);
        mRefreshLayout = (PtrFrameLayout) root.findViewById(R.id.refresh_layout);
        mRefreshLayout.disableWhenHorizontalMove(true);
        LoadSir loadSir = new LoadSir.Builder()
                .addCallback(new HomeLoadingCallback())
                .addCallback(new LoginTimeoutCallback())
                .addCallback(new HomeNetworkErrorCallback())
                .setDefaultCallback(HomeLoadingCallback.class)
                .build();
        loadService = loadSir.register(root, v -> {
            loadService.showCallback(HomeLoadingCallback.class);
            getHotCard();
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
        EventBus.getDefault().register(this);
        initRecyclerView();

        getHotCard();
        mHidden = true;
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.setUserVisibleHint(hidden);
        mHidden = hidden;
    }

    @Override
    public void onDestroyView() {
        unbinder.unbind();
        EventBus.getDefault().unregister(this);
        super.onDestroyView();
    }

    @Override
    public void clearLeaks() {
        mCardAdapter = null;
        mRvHotCardBag = null;
        loadService = null;
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(HomeClickRefreshEvent event) {
        if (!mHidden) {
            mRvHotCard.scrollToPosition(0);
            mRefreshLayout.autoRefresh();
        }
    }

    @Override
    public boolean checkCanDoRefresh(PtrFrameLayout frame, View content, View header) {
        return mAppBarLayoutVerticalOffset == 0 && !ViewCompat.canScrollVertically(frame.getContentView(), -1);
    }

    @Override
    public void onRefreshBegin(PtrFrameLayout frame) {
        mPage = 0;
        getHotCard();
    }

    @Override
    public void onLoadMoreRequested() {
        getHotCard();
    }

    @Override
    public void getDataSuccess(List<RecommendBean> list) {
        //loadService.showSuccess()的调用必须在PtrFrameLayout.refreshComplete()之前，因为loadService的调用会使得界面重新加载，这将导致PtrFrameLayout移除
        loadService.showSuccess();
        mRefreshLayout.refreshComplete();
        if (mPage == 0) {
            mCardAdapter.setNewData(list);
            mRvHotCard.scrollToPosition(0);
        } else {
            mCardAdapter.addData(list);
        }
        mPage++;
        if (list.size() == 0) {
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
        if (mPage == 0) {
            loadService.showCallback(HomeNetworkErrorCallback.class);
        }
    }

    private void initRecyclerView() {

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        mCardAdapter = new HotCardAdapter(new ArrayList<>(),new CardBagCardClickListener(mActivity));
        mCardAdapter.setLoadMoreView(new CustomLoadMoreView());
        mCardAdapter.setOnLoadMoreListener(this, mRvHotCard);
        mCardAdapter.setOnItemClickListener(new CardItemClickListener(mActivity));
        mRvHotCard.setLayoutManager(layoutManager);
        mRvHotCard.setAdapter(mCardAdapter);
        mRvHotCard.addItemDecoration(new HomeCardItemDecoration());
    }

    private void getHotCard() {
        mPresenter.getHotCard(mPage);
    }

    public void setAppBarLayoutVerticalOffset(int appBarLayoutVerticalOffset) {
        this.mAppBarLayoutVerticalOffset = appBarLayoutVerticalOffset;
    }

    private static class CardItemClickListener implements BaseQuickAdapter.OnItemClickListener {

        private Activity mContext;

        public CardItemClickListener(Activity context) {
            mContext = context;
        }

        @Override
        public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
            RecommendBean recommendBean = (RecommendBean) adapter.getData().get(position);
            HotCardBean bean = recommendBean.getCardBean();
            Bundle bundle = ActivityOptionsCompat.makeSceneTransitionAnimation(mContext,
                    Pair.create(view.findViewById(R.id.iv_item_home_card_icon), "cardImage"),
                    Pair.create(view.findViewById(R.id.tv_item_home_card_title), "cardTitle"),
                    Pair.create(view.findViewById(R.id.tv_item_home_card_info), "cardInfo")).toBundle();
            Intent intent = new Intent(mContext, CardDetailsActivity.class);
            intent.putExtra("id", bean.getId());
            intent.putExtra("name", bean.getName());
            intent.putExtra("imageUrl", bean.getImageUrl());
            intent.putExtra("date", DateTimeUtils.getDateString(bean.getDate()));
            intent.putExtra("author", bean.getAuthor());
            mContext.startActivity(intent,bundle);
        }
    }

    private static class CardBagCardClickListener implements HotCardAdapter.HotCardBagCardOnClickListener {

        private Context mContext;

        public CardBagCardClickListener(Context context) {
            mContext = context;
        }

        @Override
        public void hotCardBagCardOnClick(HotCardBean bean) {
            Intent intent = new Intent(mContext, CardDetailsActivity.class);
            intent.putExtra("id", bean.getId());
            intent.putExtra("name", bean.getName());
            intent.putExtra("imageUrl", bean.getImageUrl());
            intent.putExtra("date", DateTimeUtils.getDateString(bean.getDate()));
            intent.putExtra("author", bean.getAuthor());
            mContext.startActivity(intent);
        }

        @Override
        public void hotCardBagOnClick(HotCardBagBean bean) {
            Intent intent = new Intent(mContext, CardBagActivity.class);
            intent.putExtra("id", bean.getId());
            intent.putExtra("name", bean.getName());
            mContext.startActivity(intent);
        }
    }
}