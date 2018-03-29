package com.zxcx.zhizhe.ui.home.attention;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.util.Pair;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.kingja.loadsir.core.LoadSir;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.zxcx.zhizhe.R;
import com.zxcx.zhizhe.event.FollowUserRefreshEvent;
import com.zxcx.zhizhe.event.HomeClickRefreshEvent;
import com.zxcx.zhizhe.event.LoginEvent;
import com.zxcx.zhizhe.event.LogoutEvent;
import com.zxcx.zhizhe.event.SelectAttentionEvent;
import com.zxcx.zhizhe.loadCallback.AttentionNeedLoginCallback;
import com.zxcx.zhizhe.loadCallback.HomeLoadingCallback;
import com.zxcx.zhizhe.loadCallback.HomeNetworkErrorCallback;
import com.zxcx.zhizhe.mvpBase.RefreshMvpFragment;
import com.zxcx.zhizhe.ui.card.card.cardDetails.CardDetailsActivity;
import com.zxcx.zhizhe.ui.card.cardBag.CardBagActivity;
import com.zxcx.zhizhe.ui.home.hot.CardBean;
import com.zxcx.zhizhe.ui.home.hot.HomeCardItemDecoration;
import com.zxcx.zhizhe.ui.home.hot.HotBean;
import com.zxcx.zhizhe.ui.home.hot.HotCardAdapter;
import com.zxcx.zhizhe.ui.loginAndRegister.login.LoginActivity;
import com.zxcx.zhizhe.ui.my.selectAttention.SelectAttentionActivity;
import com.zxcx.zhizhe.ui.search.result.subject.SubjectBean;
import com.zxcx.zhizhe.ui.search.result.subject.SubjectOnClickListener;
import com.zxcx.zhizhe.utils.Constants;
import com.zxcx.zhizhe.utils.DateTimeUtils;
import com.zxcx.zhizhe.utils.ZhiZheUtils;
import com.zxcx.zhizhe.widget.CustomLoadMoreView;
import com.zxcx.zhizhe.widget.EmptyView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class AttentionFragment extends RefreshMvpFragment<AttentionPresenter> implements AttentionContract.View,
        BaseQuickAdapter.RequestLoadMoreListener {

    @BindView(R.id.rv_attention_card)
    RecyclerView mRvAttentionCard;
    Unbinder unbinder;

    private HotCardAdapter mCardAdapter;
    private int page = 0;
    private boolean mHidden = true;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_attention, container, false);
        unbinder = ButterKnife.bind(this, root);

        mRefreshLayout = root.findViewById(R.id.refresh_layout);

        LoadSir loadSir = new LoadSir.Builder()
                .addCallback(new AttentionNeedLoginCallback())
                .addCallback(new HomeLoadingCallback())
                .addCallback(new HomeNetworkErrorCallback())
                .setDefaultCallback(HomeLoadingCallback.class)
                .build();
        loadService = loadSir.register(root, v -> {
            if (checkLogin()) {
                loadService.showCallback(HomeLoadingCallback.class);
                onRefresh();
            }
        });
        return loadService.getLoadLayout();
    }

    @Override
    protected AttentionPresenter createPresenter() {
        return new AttentionPresenter(this);
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        mHidden = hidden;
        super.onHiddenChanged(hidden);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        EventBus.getDefault().register(this);
        initView();
        mHidden = false;
    }

    @Override
    public void onDestroyView() {
        EventBus.getDefault().unregister(this);
        unbinder.unbind();
        super.onDestroyView();
    }

    @Override
    public void clearLeaks() {
        mCardAdapter = null;
        loadService = null;
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(HomeClickRefreshEvent event) {
        if (!mHidden) {
            mRvAttentionCard.scrollToPosition(0);
            mRefreshLayout.autoRefresh();
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(LoginEvent event) {
        loadService.showCallback(HomeLoadingCallback.class);
        onRefresh();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(LogoutEvent event) {
        loadService.showCallback(AttentionNeedLoginCallback.class);
        mCardAdapter.getData().clear();
        mCardAdapter.notifyDataSetChanged();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(SelectAttentionEvent event) {
        loadService.showCallback(HomeLoadingCallback.class);
        onRefresh();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(FollowUserRefreshEvent event) {
        loadService.showCallback(HomeLoadingCallback.class);
        onRefresh();
    }

    @Override
    public void onRefresh(RefreshLayout refreshLayout) {
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
    public void getDataSuccess(List<HotBean> list) {
        loadService.showSuccess();
        mRefreshLayout.finishRefresh();
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
        super.toastFail(msg);
        mCardAdapter.loadMoreFail();
        if (page == 0){
            loadService.showCallback(HomeNetworkErrorCallback.class);
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

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        mCardAdapter = new HotCardAdapter(new ArrayList<>(),new CardBagCardClickListener(mActivity));
        mCardAdapter.setLoadMoreView(new CustomLoadMoreView());
        mCardAdapter.setOnLoadMoreListener(this, mRvAttentionCard);
        mCardAdapter.setOnItemClickListener(new CardItemClickListener(mActivity));
        //todo 修改占位图
        View emptyView = EmptyView.getEmptyViewAndClick(mActivity,"暂无关注","看看你喜欢什么",R.color.button_blue,v -> {
            Intent intent = new Intent(getActivity(), SelectAttentionActivity.class);
            startActivity(intent);
        });

        mCardAdapter.setEmptyView(emptyView);
        mRvAttentionCard.setLayoutManager(layoutManager);
        mRvAttentionCard.setAdapter(mCardAdapter);
        mRvAttentionCard.addItemDecoration(new HomeCardItemDecoration());
        if (checkLogin()){
            onRefresh();
        }else {
            loadService.showCallback(AttentionNeedLoginCallback.class);
        }
    }

    private void getHotCard() {
        mPresenter.getHotCard(page, Constants.PAGE_SIZE);
    }

    static class CardItemClickListener implements BaseQuickAdapter.OnItemClickListener{

        private Activity mContext;

        CardItemClickListener(Activity context) {
            mContext  = context;
        }

        @Override
        public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
            CardBean bean = (CardBean) adapter.getData().get(position);
            Bundle bundle = ActivityOptionsCompat.makeSceneTransitionAnimation(mContext,
                    Pair.create(view.findViewById(R.id.iv_item_card_icon), "cardImage"),
                    Pair.create(view.findViewById(R.id.tv_item_card_title), "cardTitle"),
                    Pair.create(view.findViewById(R.id.tv_item_card_card_bag), "cardBag")).toBundle();
            Intent intent = new Intent(mContext, CardDetailsActivity.class);
            intent.putExtra("id",bean.getId());
            intent.putExtra("name",bean.getName());
            intent.putExtra("imageUrl", bean.getImageUrl());
            intent.putExtra("date", DateTimeUtils.getDateString(bean.getDate()));
            intent.putExtra("author", bean.getAuthor());
            mContext.startActivity(intent,bundle);
        }
    }

    private static class CardBagCardClickListener implements SubjectOnClickListener {

        private Context mContext;

        CardBagCardClickListener(Context context) {
            mContext = context;
        }

        @Override
        public void cardOnClick(CardBean bean) {
            Intent intent = new Intent(mContext, CardDetailsActivity.class);
            intent.putExtra("id", bean.getId());
            intent.putExtra("name", bean.getName());
            intent.putExtra("imageUrl", bean.getImageUrl());
            intent.putExtra("date", DateTimeUtils.getDateString(bean.getDate()));
            intent.putExtra("author", bean.getAuthor());
            mContext.startActivity(intent);
        }

        @Override
        public void subjectOnClick(SubjectBean bean) {
            Intent intent = new Intent(mContext, CardBagActivity.class);
            intent.putExtra("id", bean.getId());
            intent.putExtra("name", bean.getName());
            mContext.startActivity(intent);
        }
    }
}