package com.zxcx.zhizhe.ui.home.attention;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.kingja.loadsir.callback.Callback;
import com.kingja.loadsir.core.LoadSir;
import com.zxcx.zhizhe.R;
import com.zxcx.zhizhe.event.HomeClickRefreshEvent;
import com.zxcx.zhizhe.event.LoginEvent;
import com.zxcx.zhizhe.event.SelectAttentionEvent;
import com.zxcx.zhizhe.loadCallback.HomeLoadingCallback;
import com.zxcx.zhizhe.loadCallback.LoginTimeoutCallback;
import com.zxcx.zhizhe.loadCallback.NetworkErrorCallback;
import com.zxcx.zhizhe.mvpBase.MvpFragment;
import com.zxcx.zhizhe.ui.card.card.newCardDetails.CardDetailsActivity;
import com.zxcx.zhizhe.ui.card.cardBag.CardBagActivity;
import com.zxcx.zhizhe.ui.home.attention.adapter.AttentionCardBagAdapter;
import com.zxcx.zhizhe.ui.home.hot.HotCardBagBean;
import com.zxcx.zhizhe.ui.home.hot.HotCardBean;
import com.zxcx.zhizhe.ui.home.hot.adapter.HotCardAdapter;
import com.zxcx.zhizhe.ui.home.hot.itemDecoration.HomeCardBagItemDecoration;
import com.zxcx.zhizhe.ui.home.hot.itemDecoration.HomeCardItemDecoration;
import com.zxcx.zhizhe.ui.loginAndRegister.login.LoginActivity;
import com.zxcx.zhizhe.ui.my.selectAttention.SelectAttentionActivity;
import com.zxcx.zhizhe.utils.Constants;
import com.zxcx.zhizhe.utils.SVTSConstants;
import com.zxcx.zhizhe.utils.SharedPreferencesUtil;
import com.zxcx.zhizhe.widget.CustomLoadMoreView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class AttentionFragment extends MvpFragment<AttentionPresenter> implements AttentionContract.View,
        BaseQuickAdapter.RequestLoadMoreListener, SwipeRefreshLayout.OnRefreshListener {

    RecyclerView mRvAttentionCardBag;
    @BindView(R.id.rv_attention_card)
    RecyclerView mRvAttentionCard;
    Unbinder unbinder;
    @BindView(R.id.srl_attention_card)
    SwipeRefreshLayout mSrlAttentionCard;

    private AttentionCardBagAdapter mCardBagAdapter;
    private HotCardAdapter mCardAdapter;
    private int mUserId = SharedPreferencesUtil.getInt(SVTSConstants.userId,0);
    private View mEmptyView;
    private int page = 0;
    private boolean isFirst = true;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_attention, container, false);
        unbinder = ButterKnife.bind(this, root);

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
    public void setUserVisibleHint(boolean isVisibleToUser) {
        if (isVisibleToUser){
            EventBus.getDefault().register(this);
            if (isFirst) {
                if (mUserId == 0){
                    toastShow(getString(R.string.need_login));
                    Intent intent = new Intent(getActivity(), LoginActivity.class);
                    startActivity(intent);
                    loadService.showSuccess();
                }else {
                    getHotCardBag();
                }
                isFirst = false;
            }
        }else {
            EventBus.getDefault().unregister(this);
        }
        super.setUserVisibleHint(isVisibleToUser);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

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
        mCardBagAdapter = null;
        mRvAttentionCardBag = null;
        mEmptyView = null;
        loadService = null;
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(HomeClickRefreshEvent event) {
        mRvAttentionCard.scrollToPosition(0);
        mSrlAttentionCard.setRefreshing(true);
        onRefresh();
    }

    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    public void onMessageEvent(LoginEvent event) {
        mRvAttentionCard.scrollToPosition(0);
        mSrlAttentionCard.setRefreshing(true);
        onRefresh();
        EventBus.getDefault().removeStickyEvent(event);
    }

    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    public void onMessageEvent(SelectAttentionEvent event) {
        mRvAttentionCard.scrollToPosition(0);
        mSrlAttentionCard.setRefreshing(true);
        onRefresh();
        EventBus.getDefault().removeStickyEvent(event);
    }

    @Override
    public void onRefresh() {
        page = 0;
        mUserId = SharedPreferencesUtil.getInt(SVTSConstants.userId,0);
        getHotCardBag();
    }

    @Override
    public void onLoadMoreRequested() {
        mSrlAttentionCard.setEnabled(false);
        getHotCard();
        mSrlAttentionCard.setEnabled(true);
    }

    @Override
    public void getHotCardBagSuccess(List<HotCardBagBean> list) {
        if (page == 0){
            mCardBagAdapter.setNewData(list);
        }else {
            mCardBagAdapter.addData(list);
        }
        loadService.showSuccess();
        if (mCardBagAdapter.getData().size() == 0){
            //占空图
            mCardAdapter.setHeaderAndEmpty(false);
        }else {
            mCardAdapter.setHeaderAndEmpty(true);
            getHotCard();
        }
    }

    @Override
    public void getDataSuccess(List<HotCardBean> list) {
        if (mSrlAttentionCard.isRefreshing()) {
            mSrlAttentionCard.setRefreshing(false);
        }
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
        if (mCardAdapter.getData().size() == 0){
            View view = LayoutInflater.from(mActivity).inflate(R.layout.view_no_data, null);
            mCardAdapter.setEmptyView(view);
        }
    }

    @Override
    public void toastFail(String msg) {
        if (mSrlAttentionCard.isRefreshing()) {
            mSrlAttentionCard.setRefreshing(false);
        }
        super.toastFail(msg);
        mCardAdapter.loadMoreFail();
        if (page == 0){
            loadService.showCallback(NetworkErrorCallback.class);
        }
    }

    @Override
    public void startLogin() {
        if (mSrlAttentionCard.isRefreshing()) {
            mSrlAttentionCard.setRefreshing(false);
        }
        super.startLogin();
    }

    private void initView() {
        mSrlAttentionCard.setOnRefreshListener(this);
        int color = ContextCompat.getColor(getContext(), R.color.button_blue);
        mSrlAttentionCard.setColorSchemeColors(ContextCompat.getColor(getContext(), R.color.button_blue));

        mEmptyView = LayoutInflater.from(mActivity).inflate(R.layout.empty_attention,null);
        mEmptyView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mUserId = SharedPreferencesUtil.getInt(SVTSConstants.userId,0);
                if (mUserId == 0){
                    toastShow(getString(R.string.need_login));
                    Intent intent = new Intent(getActivity(), LoginActivity.class);
                    startActivity(intent);
                }else {
                    Intent intent = new Intent(getActivity(), SelectAttentionActivity.class);
                    startActivity(intent);
                }
            }
        });

        LinearLayoutManager hotCardBagLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        StaggeredGridLayoutManager hotCardLayoutManager = new StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL);
        mCardAdapter = new HotCardAdapter(new ArrayList<HotCardBean>());
        mCardAdapter.setLoadMoreView(new CustomLoadMoreView());
        mCardAdapter.setOnLoadMoreListener(this, mRvAttentionCard);
        mCardAdapter.setOnItemClickListener(new CardItemClickListener(mActivity));
        mCardAdapter.setOnItemChildClickListener(new CardTypeClickListener(mActivity));
        mRvAttentionCard.setLayoutManager(hotCardLayoutManager);
        mRvAttentionCard.setAdapter(mCardAdapter);
        mRvAttentionCard.addItemDecoration(new HomeCardItemDecoration());

        View view = LayoutInflater.from(mActivity).inflate(R.layout.head_home_attention,null);
        mRvAttentionCardBag = (RecyclerView) view.findViewById(R.id.rv_attention_card_bag);
        mCardBagAdapter = new AttentionCardBagAdapter(new ArrayList<HotCardBagBean>());
        mCardBagAdapter.setOnItemClickListener(new CardBagItemClickListener(mActivity));
        mRvAttentionCardBag.setLayoutManager(hotCardBagLayoutManager);
        mRvAttentionCardBag.setAdapter(mCardBagAdapter);
        mRvAttentionCardBag.addItemDecoration(new HomeCardBagItemDecoration());

        mCardAdapter.addHeaderView(view);
        mCardAdapter.setEmptyView(mEmptyView);
    }

    private void getHotCard() {
        mPresenter.getHotCard(page, Constants.PAGE_SIZE);
    }

    private void getHotCardBag() {
        mPresenter.getHotCardBag();
    }

    static class CardBagItemClickListener implements BaseQuickAdapter.OnItemClickListener{

        private Context mContext;

        public CardBagItemClickListener(Context context) {
            mContext  = context;
        }

        @Override
        public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
            HotCardBagBean bean = (HotCardBagBean) adapter.getData().get(position);
            Intent intent = new Intent(mContext, CardBagActivity.class);
            intent.putExtra("id",bean.getId());
            intent.putExtra("name",bean.getName());
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
            HotCardBean bean = (HotCardBean) adapter.getData().get(position);
            Intent intent = new Intent(mContext, CardDetailsActivity.class);
            intent.putExtra("id",bean.getId());
            intent.putExtra("name",bean.getName());
            mContext.startActivity(intent);
        }
    }

    private static class CardTypeClickListener implements BaseQuickAdapter.OnItemChildClickListener{

        private Context mContext;

        public CardTypeClickListener(Context context) {
            mContext  = context;
        }

        @Override
        public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
            HotCardBean bean = (HotCardBean) adapter.getData().get(position);
            Intent intent = new Intent(mContext, CardBagActivity.class);
            intent.putExtra("id",bean.getBagId());
            intent.putExtra("name",bean.getBagName());
            mContext.startActivity(intent);
        }
    }
}