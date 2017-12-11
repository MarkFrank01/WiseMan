package com.zxcx.zhizhe.ui.home.attention;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.kingja.loadsir.core.LoadSir;
import com.zxcx.zhizhe.R;
import com.zxcx.zhizhe.event.HomeClickRefreshEvent;
import com.zxcx.zhizhe.event.LoginEvent;
import com.zxcx.zhizhe.event.SelectAttentionEvent;
import com.zxcx.zhizhe.loadCallback.AttentionNeedLoginCallback;
import com.zxcx.zhizhe.loadCallback.HomeLoadingCallback;
import com.zxcx.zhizhe.loadCallback.NetworkErrorCallback;
import com.zxcx.zhizhe.mvpBase.RefreshMvpFragment;
import com.zxcx.zhizhe.ui.card.card.newCardDetails.CardDetailsActivity;
import com.zxcx.zhizhe.ui.home.hot.HotCardBean;
import com.zxcx.zhizhe.ui.loginAndRegister.login.LoginActivity;
import com.zxcx.zhizhe.ui.my.selectAttention.SelectAttentionActivity;
import com.zxcx.zhizhe.utils.Constants;
import com.zxcx.zhizhe.utils.DateTimeUtils;
import com.zxcx.zhizhe.utils.ZhiZheUtils;
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

public class AttentionFragment extends RefreshMvpFragment<AttentionPresenter> implements AttentionContract.View,
        BaseQuickAdapter.RequestLoadMoreListener {

    @BindView(R.id.rv_attention_card)
    RecyclerView mRvAttentionCard;
    Unbinder unbinder;

    private AttentionCardAdapter mCardAdapter;
    private View mEmptyView;
    private int page = 0;
    private int mAppBarLayoutVerticalOffset;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_attention, container, false);
        unbinder = ButterKnife.bind(this, root);

        mRefreshLayout = root.findViewById(R.id.refresh_layout);

        LoadSir loadSir = new LoadSir.Builder()
                .addCallback(new HomeLoadingCallback())
                .addCallback(new NetworkErrorCallback())
                .setDefaultCallback(HomeLoadingCallback.class)
                .build();
        loadService = loadSir.register(root, v -> {
            loadService.showCallback(HomeLoadingCallback.class);
            onRefresh();
        });
        return loadService.getLoadLayout();
    }

    @Override
    protected AttentionPresenter createPresenter() {
        return new AttentionPresenter(this);
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        if (!hidden){
            EventBus.getDefault().register(this);
        }else {
            EventBus.getDefault().unregister(this);
        }
        super.onHiddenChanged(hidden);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        EventBus.getDefault().register(this);
        initView();
    }

    @Override
    public void onDestroyView() {
        unbinder.unbind();
        super.onDestroyView();
    }

    @Override
    public void clearLeaks() {
        mCardAdapter = null;
        mEmptyView = null;
        loadService = null;
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(HomeClickRefreshEvent event) {
        mRvAttentionCard.scrollToPosition(0);
        mRefreshLayout.autoRefresh();
    }

    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    public void onMessageEvent(LoginEvent event) {
        mRvAttentionCard.scrollToPosition(0);
        mRefreshLayout.autoRefresh();
        EventBus.getDefault().removeStickyEvent(event);
    }

    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    public void onMessageEvent(SelectAttentionEvent event) {
        mRvAttentionCard.scrollToPosition(0);
        mRefreshLayout.autoRefresh();
        EventBus.getDefault().removeStickyEvent(event);
    }

    @Override
    public boolean checkCanDoRefresh(PtrFrameLayout frame, View content, View header) {
        return mAppBarLayoutVerticalOffset == 0 && !frame.getContentView().canScrollVertically(-1);
    }

    @Override
    public void onRefreshBegin(PtrFrameLayout frame) {
        onRefresh();
    }

    private void onRefresh(){
        page = 0;
        getHotCard();
    }

    @Override
    public void onLoadMoreRequested() {
        getHotCard();
    }

    @Override
    public void getDataSuccess(List<HotCardBean> list) {
        loadService.showSuccess();
        mRefreshLayout.refreshComplete();
        if (page == 0){
            mCardAdapter.setNewData(list);
            mRvAttentionCard.scrollToPosition(0);
        }else {
            mCardAdapter.addData(list);
        }
        page++;
        if (list.size() < Constants.PAGE_SIZE){
            mCardAdapter.loadMoreEnd(false);
        }else {
            mCardAdapter.loadMoreComplete();
            mCardAdapter.setEnableLoadMore(false);
            mCardAdapter.setEnableLoadMore(true);
        }
    }

    @Override
    public void toastFail(String msg) {
        mRefreshLayout.refreshComplete();
        super.toastFail(msg);
        mCardAdapter.loadMoreFail();
        if (page == 0){
            loadService.showCallback(NetworkErrorCallback.class);
        }
    }

    @Override
    public void startLogin() {
        ZhiZheUtils.logout();
        toastShow(R.string.login_timeout);
        startActivity(new Intent(mActivity, LoginActivity.class));
        if (loadService != null){
            loadService.showCallback(AttentionNeedLoginCallback.class);
        }
    }

    private void initView() {
        mEmptyView = LayoutInflater.from(mActivity).inflate(R.layout.empty_attention,null);
        mEmptyView.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), SelectAttentionActivity.class);
            startActivity(intent);
        });

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        mCardAdapter = new AttentionCardAdapter(new ArrayList<>());
        mCardAdapter.setLoadMoreView(new CustomLoadMoreView());
        mCardAdapter.setOnLoadMoreListener(this, mRvAttentionCard);
        mCardAdapter.setOnItemClickListener(new CardItemClickListener(mActivity));
        mCardAdapter.setEmptyView(mEmptyView);
        mRvAttentionCard.setLayoutManager(layoutManager);
        mRvAttentionCard.setAdapter(mCardAdapter);
        onRefresh();
    }

    private void getHotCard() {
        mPresenter.getHotCard(page, Constants.PAGE_SIZE);
    }

    public void setAppBarLayoutVerticalOffset(int appBarLayoutVerticalOffset) {
        this.mAppBarLayoutVerticalOffset = appBarLayoutVerticalOffset;
    }

    static class CardItemClickListener implements BaseQuickAdapter.OnItemClickListener{

        private Context mContext;

        public CardItemClickListener(Context context) {
            mContext  = context;
        }

        @Override
        public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
            HotCardBean bean = (HotCardBean) adapter.getData().get(position);
            Intent intent = new Intent(mContext, CardDetailsActivity.class);
            intent.putExtra("id",bean.getId());
            intent.putExtra("name",bean.getName());
            intent.putExtra("imageUrl", bean.getImageUrl());
            intent.putExtra("date", DateTimeUtils.getDateString(bean.getDate()));
            intent.putExtra("author", bean.getAuthor());
            mContext.startActivity(intent);
        }
    }
}