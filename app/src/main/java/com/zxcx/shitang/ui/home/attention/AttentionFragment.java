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

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.zxcx.shitang.R;
import com.zxcx.shitang.event.HomeClickRefreshEvent;
import com.zxcx.shitang.event.SelectAttentionEvent;
import com.zxcx.shitang.mvpBase.MvpFragment;
import com.zxcx.shitang.ui.card.card.cardDetails.CardDetailsActivity;
import com.zxcx.shitang.ui.card.cardBag.CardBagActivity;
import com.zxcx.shitang.ui.home.attention.adapter.AttentionCardBagAdapter;
import com.zxcx.shitang.ui.home.hot.HotCardBagBean;
import com.zxcx.shitang.ui.home.hot.HotCardBean;
import com.zxcx.shitang.ui.home.hot.adapter.HotCardAdapter;
import com.zxcx.shitang.ui.home.hot.itemDecoration.HomeCardBagItemDecoration;
import com.zxcx.shitang.ui.home.hot.itemDecoration.HomeCardItemDecoration;
import com.zxcx.shitang.ui.loginAndRegister.login.LoginActivity;
import com.zxcx.shitang.ui.my.selectAttention.SelectAttentionActivity;
import com.zxcx.shitang.utils.Constants;
import com.zxcx.shitang.utils.SVTSConstants;
import com.zxcx.shitang.utils.SharedPreferencesUtil;
import com.zxcx.shitang.widget.CustomLoadMoreView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import cn.jiguang.analytics.android.api.LoginEvent;

public class AttentionFragment extends MvpFragment<AttentionPresenter> implements AttentionContract.View,
        BaseQuickAdapter.RequestLoadMoreListener, SwipeRefreshLayout.OnRefreshListener {

    private static AttentionFragment fragment = null;
    RecyclerView mRvAttentionCardBag;
    @BindView(R.id.rv_attention_card)
    RecyclerView mRvAttentionCard;
    Unbinder unbinder;
    @BindView(R.id.srl_attention_card)
    SwipeRefreshLayout mSrlAttentionCard;

    private AttentionCardBagAdapter mCardBagAdapter;
    private HotCardAdapter mCardAdapter;
    private boolean isErr = false;
    private int mUserId = SharedPreferencesUtil.getInt(SVTSConstants.userId,0);
    private View mEmptyView;
    private int page = 1;

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
    public void setUserVisibleHint(boolean isVisibleToUser) {
        if (isVisibleToUser){
            EventBus.getDefault().register(this);
        }else {
            EventBus.getDefault().unregister(this);
        }
        super.setUserVisibleHint(isVisibleToUser);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initView();
        if (mUserId == 0){
            toastShow(getString(R.string.need_login));
            Intent intent = new Intent(getActivity(), LoginActivity.class);
            startActivity(intent);
        }else {
            getHotCard(mUserId);
            getHotCardBag(mUserId);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(HomeClickRefreshEvent event) {
        mRvAttentionCard.smoothScrollToPosition(0);
        mSrlAttentionCard.setRefreshing(true);
        onRefresh();
    }

    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    public void onMessageEvent(LoginEvent event) {
        mRvAttentionCard.smoothScrollToPosition(0);
        mSrlAttentionCard.setRefreshing(true);
        onRefresh();
        EventBus.getDefault().removeStickyEvent(event);
    }

    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    public void onMessageEvent(SelectAttentionEvent event) {
        mRvAttentionCard.smoothScrollToPosition(0);
        mSrlAttentionCard.setRefreshing(true);
        onRefresh();
        EventBus.getDefault().removeStickyEvent(event);
    }

    @Override
    public void onRefresh() {
        page = 1;
        mUserId = SharedPreferencesUtil.getInt(SVTSConstants.userId,0);
        mCardAdapter.setEnableLoadMore(false);
        mCardAdapter.setEnableLoadMore(true);
        mCardAdapter.getData().clear();
        mCardBagAdapter.getData().clear();
        getHotCard(mUserId);
        getHotCardBag(mUserId);
    }

    @Override
    public void onLoadMoreRequested() {
        mSrlAttentionCard.setEnabled(false);
        getHotCard(mUserId);
        mSrlAttentionCard.setEnabled(true);
    }

    @Override
    public void getHotCardBagSuccess(List<HotCardBagBean> list) {
        if (page == 1){
            mCardBagAdapter.notifyDataSetChanged();
        }
        mCardBagAdapter.addData(list);
        if (mCardBagAdapter.getData().size() == 0){
            //占空图
        }
    }

    @Override
    public void getDataSuccess(List<HotCardBean> list) {
        if (mSrlAttentionCard.isRefreshing()) {
            mSrlAttentionCard.setRefreshing(false);
        }
        if (page == 1){
            mCardAdapter.notifyDataSetChanged();
        }
        page++;
        mCardAdapter.addData(list);
        if (list.size() < Constants.PAGE_SIZE){
            mCardAdapter.loadMoreEnd(false);
        }else {
            mCardAdapter.loadMoreComplete();
        }
        if (mCardBagAdapter.getData().size() == 0){
            //占空图
        }
    }

    @Override
    public void toastFail(String msg) {
        super.toastFail(msg);
        if ("未选择兴趣".equals(msg)) {
            mCardAdapter.setEmptyView(mEmptyView);
        }else {
            mCardAdapter.loadMoreFail();
        }
    }

    private void initView() {
        mSrlAttentionCard.setOnRefreshListener(this);
        mSrlAttentionCard.setColorSchemeColors(ContextCompat.getColor(getContext(), R.color.colorPrimaryFinal));

        mEmptyView = View.inflate(getContext(),R.layout.empty_attention,null);
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
        GridLayoutManager hotCardLayoutManager = new GridLayoutManager(getContext(), 2);
        mCardAdapter = new HotCardAdapter(new ArrayList<HotCardBean>());
        mCardAdapter.setLoadMoreView(new CustomLoadMoreView());
        mCardAdapter.setOnLoadMoreListener(this, mRvAttentionCard);
        mCardAdapter.setOnItemClickListener(new CardItemClickListener(mActivity));
        mCardAdapter.setOnItemChildClickListener(new CardTypeClickListener(mActivity));
        mRvAttentionCard.setLayoutManager(hotCardLayoutManager);
        mRvAttentionCard.setAdapter(mCardAdapter);
        mRvAttentionCard.addItemDecoration(new HomeCardItemDecoration());

        View view = View.inflate(getContext(),R.layout.head_home_attention,null);
        mRvAttentionCardBag = (RecyclerView) view.findViewById(R.id.rv_attention_card_bag);
        mCardBagAdapter = new AttentionCardBagAdapter(new ArrayList<HotCardBagBean>());
        mCardBagAdapter.setOnItemClickListener(new CardBagItemClickListener(mActivity));
        mRvAttentionCardBag.setLayoutManager(hotCardBagLayoutManager);
        mRvAttentionCardBag.setAdapter(mCardBagAdapter);
        mRvAttentionCardBag.addItemDecoration(new HomeCardBagItemDecoration());

        mCardAdapter.addHeaderView(view);
        mCardAdapter.setEmptyView(mEmptyView);
    }

    private void getHotCard(int userId) {
        mPresenter.getHotCard(userId, page, Constants.PAGE_SIZE);
    }

    private void getHotCardBag(int userId) {
        mPresenter.getHotCardBag(userId);
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
            Intent intent = new Intent(mContext, CardDetailsActivity.class);
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