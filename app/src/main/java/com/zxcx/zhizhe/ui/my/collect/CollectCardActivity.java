package com.zxcx.zhizhe.ui.my.collect;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.kingja.loadsir.core.LoadSir;
import com.zxcx.zhizhe.R;
import com.zxcx.zhizhe.event.UnCollectEvent;
import com.zxcx.zhizhe.loadCallback.LoadingCallback;
import com.zxcx.zhizhe.loadCallback.NetworkErrorCallback;
import com.zxcx.zhizhe.mvpBase.MvpActivity;
import com.zxcx.zhizhe.ui.card.card.cardDetails.CardDetailsActivity;
import com.zxcx.zhizhe.utils.Constants;
import com.zxcx.zhizhe.widget.CustomLoadMoreView;

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
    private int mSortType = 1;//0倒序 1正序

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
        mIvToolbarRight.setImageResource(R.drawable.iv_card_bag_card);

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
            View view = LayoutInflater.from(mActivity).inflate(R.layout.layout_no_data, null);
            TextView textView = (TextView) view.findViewById(R.id.tv_no_data);
            textView.setText(R.string.no_collect_card);
            mAdapter.setEmptyView(view);
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
            mIvToolbarRight.setImageResource(R.drawable.iv_card_bag_list);
        }else if (mSortType == 0){
            mSortType = 1;
            mIvToolbarRight.setImageResource(R.drawable.iv_card_bag_card);
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
        mAdapter.notifyDataSetChanged();
    }
}
