package com.zxcx.shitang.ui.card.card.cardBagCardDetails;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.zxcx.shitang.R;
import com.zxcx.shitang.event.CollectSuccessEvent;
import com.zxcx.shitang.mvpBase.MvpFragment;
import com.zxcx.shitang.ui.card.card.collect.SelectCollectFolderActivity;
import com.zxcx.shitang.utils.ScreenUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class CardBagCardDetailsFragment extends MvpFragment<CardBagCardDetailsPresenter> implements CardBagCardDetailsContract.View {

    @BindView(R.id.tv_card_details_name)
    TextView mTvCardDetailsName;
    @BindView(R.id.tv_card_details_subhead)
    TextView mTvCardDetailsSubhead;
    @BindView(R.id.tv_card_details_content)
    TextView mTvCardDetailsContent;
    @BindView(R.id.cb_card_details_collect)
    CheckBox mCbCardDetailsCollect;
    @BindView(R.id.cb_card_details_like)
    CheckBox mCbCardDetailsLike;
    @BindView(R.id.iv_card_details)
    ImageView mIvCardDetails;

    SelectCollectFolderActivity mSelectCollectFolderActivity;

    public static final String CARD_ID = "CARD_ID";

    Unbinder unbinder;


    public static CardBagCardDetailsFragment newInstance(String cardId) {
        /*if (fragment == null || !fragment.getArguments().getString(CARD_ID).equals(cardId)) {
            Bundle args = new Bundle();
            args.putString(CARD_ID, cardId);
            fragment = new CardBagCardDetailsFragment();
            fragment.setArguments(args);
        }*/
        CardBagCardDetailsFragment fragment = new CardBagCardDetailsFragment();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.activity_card_details, container, false);
        unbinder = ButterKnife.bind(this, root);
        EventBus.getDefault().register(this);
        return root;
    }

    @Override
    protected CardBagCardDetailsPresenter createPresenter() {
        return new CardBagCardDetailsPresenter(this);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mSelectCollectFolderActivity = new SelectCollectFolderActivity();

        ViewGroup.LayoutParams para = mIvCardDetails.getLayoutParams();
        para.height = (ScreenUtils.getScreenWidth() - ScreenUtils.dip2px(12*2)) * 3 / 4;
        mIvCardDetails.setLayoutParams(para);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void onDestroy() {
        mActivity = null;
        super.onDestroy();
    }

    @Override
    public void getDataSuccess(CardBagCardDetailsBean bean) {

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(CollectSuccessEvent event) {
        mCbCardDetailsCollect.setChecked(true);
    }

    @OnClick(R.id.tv_card_details_share)
    public void onShareClicked() {
    }

    @OnClick(R.id.cb_card_details_collect)
    public void onCollectClicked() {
        if (mCbCardDetailsCollect.isChecked()) {
            startActivity(new Intent(mActivity,SelectCollectFolderActivity.class));
        }
    }

    @OnClick(R.id.iv_card_details_back)
    public void onBackClicked() {
        getActivity().finish();
    }
}