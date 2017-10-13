package com.zxcx.zhizhe.ui.home.hot;

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
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.kingja.loadsir.callback.Callback;
import com.kingja.loadsir.core.LoadService;
import com.kingja.loadsir.core.LoadSir;
import com.zxcx.zhizhe.R;
import com.zxcx.zhizhe.event.GotoClassifyEvent;
import com.zxcx.zhizhe.event.HomeClickRefreshEvent;
import com.zxcx.zhizhe.loadCallback.HomeLoadingCallback;
import com.zxcx.zhizhe.loadCallback.NetworkErrorCallback;
import com.zxcx.zhizhe.mvpBase.MvpFragment;
import com.zxcx.zhizhe.ui.card.card.newCardDetails.CardDetailsActivity;
import com.zxcx.zhizhe.ui.card.cardBag.CardBagActivity;
import com.zxcx.zhizhe.ui.home.hot.adapter.HotCardAdapter;
import com.zxcx.zhizhe.ui.home.hot.adapter.HotCardBagAdapter;
import com.zxcx.zhizhe.ui.home.hot.itemDecoration.HomeCardBagItemDecoration;
import com.zxcx.zhizhe.ui.home.hot.itemDecoration.HomeCardItemDecoration;
import com.zxcx.zhizhe.utils.Constants;
import com.zxcx.zhizhe.utils.LogCat;
import com.zxcx.zhizhe.widget.CustomLoadMoreView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class HotFragment extends MvpFragment<HotPresenter> implements HotContract.View,
        BaseQuickAdapter.RequestLoadMoreListener, SwipeRefreshLayout.OnRefreshListener {

    RecyclerView mRvHotCardBag;
    @BindView(R.id.rv_hot_card)
    RecyclerView mRvHotCard;
    Unbinder unbinder;
    @BindView(R.id.srl_hot_card)
    SwipeRefreshLayout mSrlHotCard;

    private HotCardBagAdapter mCardBagAdapter;
    private HotCardAdapter mCardAdapter;
    private int page = 0;
    private LoadService loadService;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_hot, container, false);
        unbinder = ButterKnife.bind(this, root);

        LoadSir loadSir = new LoadSir.Builder()
                .addCallback(new HomeLoadingCallback())
                .addCallback(new NetworkErrorCallback())
                .setDefaultCallback(HomeLoadingCallback.class)
                .build();
        loadService = loadSir.register(root, new Callback.OnReloadListener() {
            @Override
            public void onReload(View v) {
                onRefresh();
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

        mSrlHotCard.setOnRefreshListener(this);
        mSrlHotCard.setColorSchemeColors(ContextCompat.getColor(getContext(), R.color.button_blue));
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser){
            EventBus.getDefault().register(this);
        }else {
            EventBus.getDefault().unregister(this);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
        mCardAdapter = null;
        mCardBagAdapter = null;
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(HomeClickRefreshEvent event) {
        mRvHotCard.smoothScrollToPosition(0);
        mSrlHotCard.setRefreshing(true);
        onRefresh();
    }

    @Override
    public void onRefresh() {
        page = 0;
        getHotCard();
        getHotCardBag();
    }

    @Override
    public void onLoadMoreRequested() {
        mSrlHotCard.setEnabled(false);
        getHotCard();
        mSrlHotCard.setEnabled(true);
    }

    @Override
    public void getHotCardBagSuccess(List<HotCardBagBean> list) {
        mCardBagAdapter.setNewData(new ArrayList<HotCardBagBean>());
        mCardBagAdapter.addData(list);
        checkSuccess();
        if (mCardBagAdapter.getData().size() == 0){
            //占空图
        }
    }

    @Override
    public void getDataSuccess(List<HotCardBean> list) {
        if (mSrlHotCard.isRefreshing()) {
            mSrlHotCard.setRefreshing(false);
        }
        if (page == 0){
            mCardAdapter.setNewData(new ArrayList<HotCardBean>());
            mCardAdapter.addData(list);
            checkSuccess();
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
        if (mCardBagAdapter.getData().size() == 0){
            //占空图
        }
    }

    @Override
    public void toastFail(String msg) {
        super.toastFail(msg);
        mCardAdapter.loadMoreFail();
        LogCat.d("出错了"+msg);
        if (mSrlHotCard.isRefreshing()) {
            mSrlHotCard.setRefreshing(false);
        }
        if (page == 0){
            loadService.showCallback(NetworkErrorCallback.class);
        }
    }

    public void checkSuccess() {
        if (mCardAdapter.getData().size() > 0 && mCardBagAdapter.getData().size() > 0){
            loadService.showSuccess();
        }
    }

    private void initRecyclerView() {

        LinearLayoutManager hotCardBagLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        GridLayoutManager hotCardLayoutManager = new GridLayoutManager(getContext(), 2);
        mCardAdapter = new HotCardAdapter(new ArrayList<HotCardBean>());
        mCardAdapter.setLoadMoreView(new CustomLoadMoreView());
        mCardAdapter.setOnLoadMoreListener(this, mRvHotCard);
        mCardAdapter.setOnItemClickListener(new CardItemClickListener(mActivity));
        mCardAdapter.setOnItemChildClickListener(new CardTypeClickListener(mActivity));
        mRvHotCard.setLayoutManager(hotCardLayoutManager);
        mRvHotCard.setAdapter(mCardAdapter);
        mRvHotCard.addItemDecoration(new HomeCardItemDecoration());

        View view = LayoutInflater.from(mActivity).inflate(R.layout.head_home_hot,null);
        //查看全部按钮
        TextView textView = (TextView) view.findViewById(R.id.tv_hot_goto_classify);
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EventBus.getDefault().post(new GotoClassifyEvent());
            }
        });
        mRvHotCardBag = (RecyclerView) view.findViewById(R.id.rv_hot_card_bag);
        mCardBagAdapter = new HotCardBagAdapter(new ArrayList<HotCardBagBean>());
        mCardBagAdapter.setOnItemClickListener(new CardBagItemClickListener(mActivity));
        mRvHotCardBag.setLayoutManager(hotCardBagLayoutManager);
        mRvHotCardBag.setAdapter(mCardBagAdapter);
        mRvHotCardBag.addItemDecoration(new HomeCardBagItemDecoration());

        mCardAdapter.addHeaderView(view);
        mCardAdapter.setHeaderAndEmpty(true);
    }

    private void getHotCard() {
        mPresenter.getHotCard(page, Constants.PAGE_SIZE);
    }

    private void getHotCardBag() {
        mPresenter.getHotCardBag();
    }

    private static class CardBagItemClickListener implements BaseQuickAdapter.OnItemClickListener{

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

    private static class CardItemClickListener implements BaseQuickAdapter.OnItemClickListener{

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