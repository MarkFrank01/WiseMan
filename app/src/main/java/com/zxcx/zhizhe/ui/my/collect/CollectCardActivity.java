package com.zxcx.zhizhe.ui.my.collect;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.kingja.loadsir.core.LoadSir;
import com.zxcx.zhizhe.R;
import com.zxcx.zhizhe.event.UnCollectEvent;
import com.zxcx.zhizhe.loadCallback.LoadingCallback;
import com.zxcx.zhizhe.loadCallback.NetworkErrorCallback;
import com.zxcx.zhizhe.mvpBase.MvpActivity;
import com.zxcx.zhizhe.ui.card.card.cardDetails.CardDetailsActivity;
import com.zxcx.zhizhe.ui.home.hot.itemDecoration.HomeCardItemDecoration;
import com.zxcx.zhizhe.utils.Constants;
import com.zxcx.zhizhe.widget.CustomLoadMoreView;
import com.zxcx.zhizhe.widget.EmptyView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class CollectCardActivity extends MvpActivity<CollectCardPresenter> implements CollectCardContract.View,
        BaseQuickAdapter.RequestLoadMoreListener, BaseQuickAdapter.OnItemClickListener {

    @BindView(R.id.rv_collect_card)
    RecyclerView mRvCollectCard;
    @BindView(R.id.iv_toolbar_right)
    ImageView mIvToolbarRight;

    private CollectCardAdapter mAdapter;
    private int page = 0;
    private int mSortType = 0;//0倒序 1正序

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_collect_card);
        ButterKnife.bind(this);
        EventBus.getDefault().register(this);

        initToolBar("收藏");
        initRecyclerView();
        getCollectCard();

        mIvToolbarRight.setVisibility(View.VISIBLE);
        mIvToolbarRight.setImageResource(R.drawable.iv_order_sequence);

        initLoadSir();
    }

    public void initLoadSir() {
        loadService = LoadSir.getDefault().register(this, this);
    }

    @Override
    public void onReload(View v) {
        super.onReload(v);
        loadService.showCallback(LoadingCallback.class);
        getCollectCard();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Override
    protected CollectCardPresenter createPresenter() {
        return new CollectCardPresenter(this);
    }

    @Override
    public void onLoadMoreRequested() {
        getCollectCard();
    }

    @Override
    public void getDataSuccess(List<CollectCardBean> list) {
        if (page == 0) {
            loadService.showSuccess();
            mAdapter.setNewData(list);
        } else {
            mAdapter.addData(list);
        }
        page++;
        if (list.size() < Constants.PAGE_SIZE) {
            mAdapter.loadMoreEnd(false);
        } else {
            mAdapter.loadMoreComplete();
        }
        if (mAdapter.getData().size() == 0) {
            //占空图
            View emptyView = EmptyView.getEmptyView(mActivity,"点点收藏 回味无穷","快去首页收藏你喜欢的卡片吧～",null,null);
            mAdapter.setEmptyView(emptyView);
        }
    }

    @Override
    public void toastFail(String msg) {
        super.toastFail(msg);
        mAdapter.loadMoreFail();
        if (page == 0) {
            loadService.showCallback(NetworkErrorCallback.class);
        }
    }

    private void getCollectCard() {
        mPresenter.getCollectCard(mSortType,page, Constants.PAGE_SIZE);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(UnCollectEvent event) {
        CollectCardBean bean = new CollectCardBean();
        bean.setId(event.getCardId());
        mAdapter.remove(mAdapter.getData().indexOf(bean));
    }

    @OnClick(R.id.iv_toolbar_right)
    public void onViewClicked() {
        if (mSortType == 1){
            mSortType = 0;
            mIvToolbarRight.setImageResource(R.drawable.iv_order_sequence);
        }else if (mSortType == 0){
            mSortType = 1;
            mIvToolbarRight.setImageResource(R.drawable.iv_order_inverted);
        }
        page = 0;
        getCollectCard();
    }

    @Override
    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
        CollectCardBean bean = (CollectCardBean) adapter.getData().get(position);
        Intent intent = new Intent(this, CardDetailsActivity.class);
        intent.putExtra("id", bean.getId());
        intent.putExtra("name", bean.getName());
        startActivity(intent);
    }

    private void initRecyclerView() {
        mAdapter = new CollectCardAdapter(new ArrayList<>());
        mAdapter.setLoadMoreView(new CustomLoadMoreView());
        mAdapter.setOnLoadMoreListener(this, mRvCollectCard);
        mAdapter.setOnItemClickListener(this);
        mRvCollectCard.setLayoutManager(new LinearLayoutManager(mActivity, LinearLayoutManager.VERTICAL,false));
        mRvCollectCard.setAdapter(mAdapter);
        mRvCollectCard.addItemDecoration(new HomeCardItemDecoration());
        mAdapter.notifyDataSetChanged();
    }
}
