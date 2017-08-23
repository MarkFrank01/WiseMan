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
import android.widget.Toast;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.zxcx.shitang.R;
import com.zxcx.shitang.event.HomeClickRefreshEvent;
import com.zxcx.shitang.mvpBase.MvpFragment;
import com.zxcx.shitang.ui.card.card.cardDetails.CardDetailsActivity;
import com.zxcx.shitang.ui.card.cardBag.CardBagActivity;
import com.zxcx.shitang.ui.home.attention.adapter.AttentionCardBagAdapter;
import com.zxcx.shitang.ui.home.hot.HotBean;
import com.zxcx.shitang.ui.home.hot.adapter.HotCardAdapter;
import com.zxcx.shitang.ui.home.hot.itemDecoration.HomeCardBagItemDecoration;
import com.zxcx.shitang.ui.home.hot.itemDecoration.HomeCardItemDecoration;
import com.zxcx.shitang.ui.loginAndRegister.login.LoginActivity;
import com.zxcx.shitang.ui.my.selectAttention.SelectAttentionActivity;
import com.zxcx.shitang.utils.SVTSConstants;
import com.zxcx.shitang.utils.SharedPreferencesUtil;
import com.zxcx.shitang.utils.StringUtils;
import com.zxcx.shitang.widget.CustomLoadMoreView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

import static android.app.Activity.RESULT_OK;

public class AttentionFragment extends MvpFragment<AttentionPresenter> implements AttentionContract.View,
        BaseQuickAdapter.RequestLoadMoreListener, SwipeRefreshLayout.OnRefreshListener {

    private static final int TOTAL_COUNTER = 30;
    private static final int REQUEST_LOGIN = 0;
    private static final int REQUEST_SELECT_ATTENTION = 1;
    private static AttentionFragment fragment = null;
    RecyclerView mRvAttentionCardBag;
    @BindView(R.id.rv_attention_card)
    RecyclerView mRvAttentionCard;
    Unbinder unbinder;
    @BindView(R.id.srl_attention_card)
    SwipeRefreshLayout mSrlAttentionCard;

    private AttentionCardBagAdapter mAttentionCardBagAdapter;
    private HotCardAdapter mHotCardAdapter;
    private List<HotBean> mList = new ArrayList<>();
    private boolean isErr = false;
    private String mUserId = SharedPreferencesUtil.getString(SVTSConstants.userId,"");
    private View mEmptyView;

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
    }

    @Override
    public void getDataSuccess(AttentionBean bean) {

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK){
            if (requestCode == REQUEST_LOGIN){
                onRefresh();
            }else if (requestCode == REQUEST_SELECT_ATTENTION) {
                onRefresh();
            }
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(HomeClickRefreshEvent event) {
        mRvAttentionCard.smoothScrollToPosition(0);
        mSrlAttentionCard.setRefreshing(true);
        onRefresh();
    }

    @Override
    public void onRefresh() {
        isErr = false;
        mHotCardAdapter.setEnableLoadMore(false);
        mHotCardAdapter.setEnableLoadMore(true);
        mList.clear();
        getData();
        mHotCardAdapter.notifyDataSetChanged();
        if (mSrlAttentionCard.isRefreshing()) {
            mSrlAttentionCard.setRefreshing(false);
        }
    }

    @Override
    public void onLoadMoreRequested() {
        mSrlAttentionCard.setEnabled(false);
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
        mSrlAttentionCard.setEnabled(true);
    }

    private void initView() {
        mSrlAttentionCard.setOnRefreshListener(this);
        mSrlAttentionCard.setColorSchemeColors(ContextCompat.getColor(getContext(), R.color.colorPrimaryFinal));

        mEmptyView = View.inflate(getContext(),R.layout.empty_attention,null);
        mEmptyView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mUserId = SharedPreferencesUtil.getString(SVTSConstants.userId,"");
                if (StringUtils.isEmpty(mUserId)){
                    toastShow("还未登录，请先登录");
                    Intent intent = new Intent(getActivity(), LoginActivity.class);
                    startActivityForResult(intent,REQUEST_LOGIN);
                }else {
                    Intent intent = new Intent(getActivity(), SelectAttentionActivity.class);
                    startActivityForResult(intent,REQUEST_SELECT_ATTENTION);
                }
            }
        });

        LinearLayoutManager hotCardBagLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        GridLayoutManager hotCardLayoutManager = new GridLayoutManager(getContext(), 2);
        mHotCardAdapter = new HotCardAdapter(mList);
        mHotCardAdapter.setLoadMoreView(new CustomLoadMoreView());
        mHotCardAdapter.setOnLoadMoreListener(this, mRvAttentionCard);
        mHotCardAdapter.setOnItemClickListener(new CardItemClickListener(mActivity));
        mRvAttentionCard.setLayoutManager(hotCardLayoutManager);
        mRvAttentionCard.setAdapter(mHotCardAdapter);
        mRvAttentionCard.addItemDecoration(new HomeCardItemDecoration());

        View view = View.inflate(getContext(),R.layout.head_home_attention,null);
        mRvAttentionCardBag = (RecyclerView) view.findViewById(R.id.rv_attention_card_bag);
        mAttentionCardBagAdapter = new AttentionCardBagAdapter(mList);
        mAttentionCardBagAdapter.setOnItemClickListener(new CardBagItemClickListener(mActivity));
        mRvAttentionCardBag.setLayoutManager(hotCardBagLayoutManager);
        mRvAttentionCardBag.setAdapter(mAttentionCardBagAdapter);
        mRvAttentionCardBag.addItemDecoration(new HomeCardBagItemDecoration());

        mHotCardAdapter.addHeaderView(view);
        mHotCardAdapter.setEmptyView(mEmptyView);
    }

    private void getData() {
        for (int i = 0; i < 10; i++) {
            mList.add(new HotBean());
        }
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
}