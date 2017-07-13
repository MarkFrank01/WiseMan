package com.zxcx.shitang.ui.card.card.cardDetails;

import android.os.Bundle;
import android.text.TextPaint;
import android.widget.CheckBox;
import android.widget.TextView;

import com.makeramen.roundedimageview.RoundedImageView;
import com.zxcx.shitang.R;
import com.zxcx.shitang.event.CollectSuccessEvent;
import com.zxcx.shitang.mvpBase.MvpActivity;
import com.zxcx.shitang.ui.card.card.collectDialog.SelectCollectFolderDialog;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class CardDetailsActivity extends MvpActivity<CardDetailsPresenter> implements CardDetailsContract.View {

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
    RoundedImageView mIvCardDetails;

    SelectCollectFolderDialog mSelectCollectFolderDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_card_details);
        ButterKnife.bind(this);
        EventBus.getDefault().register(this);

        TextPaint tp = mTvCardDetailsSubhead.getPaint();
        tp.setFakeBoldText(true);

        mSelectCollectFolderDialog = new SelectCollectFolderDialog();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Override
    protected CardDetailsPresenter createPresenter() {
        return new CardDetailsPresenter(this);
    }

    @Override
    public void getDataSuccess(CardDetailsBean bean) {

    }

    @OnClick(R.id.tv_card_details_share)
    public void onShareClicked() {
    }

    @OnClick(R.id.cb_card_details_collect)
    public void onCollectClicked() {
        if (mCbCardDetailsCollect.isChecked()){
            mSelectCollectFolderDialog.show(getFragmentManager(),"1");
            mCbCardDetailsCollect.setChecked(false);
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(CollectSuccessEvent event) {
        mCbCardDetailsCollect.setChecked(true);
    }
}
