package com.zxcx.shitang.ui.card.card.cardDetails;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.TextPaint;
import android.view.View;
import android.view.WindowManager;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.zxcx.shitang.R;
import com.zxcx.shitang.event.CollectSuccessEvent;
import com.zxcx.shitang.mvpBase.MvpActivity;
import com.zxcx.shitang.ui.card.card.dialog.SelectCollectFolderDialog;
import com.zxcx.shitang.ui.card.card.dialog.ShareWayDialog;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class CardDetailsActivity extends MvpActivity<CardDetailsPresenter> implements CardDetailsContract.View ,
        ShareWayDialog.DefaultShareDialogListener{

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

    SelectCollectFolderDialog mSelectCollectFolderDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_card_details);
        ButterKnife.bind(this);
        EventBus.getDefault().register(this);

        TextPaint tp = mTvCardDetailsSubhead.getPaint();
        tp.setFakeBoldText(true);

        mSelectCollectFolderDialog = new SelectCollectFolderDialog();
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus){
//            TextViewUtils.adjustTvTextSize(mTvCardDetailsContent);
        }
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

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(CollectSuccessEvent event) {
        mCbCardDetailsCollect.setChecked(true);
    }

    @OnClick(R.id.tv_card_details_share)
    public void onShareClicked() {
        ShareWayDialog shareWayDialog = new ShareWayDialog();
        shareWayDialog.show(getFragmentManager(),"");
    }

    @OnClick(R.id.cb_card_details_collect)
    public void onCollectClicked() {
        if (mCbCardDetailsCollect.isChecked()){
            mSelectCollectFolderDialog.show(getFragmentManager(),"1");
            mCbCardDetailsCollect.setChecked(false);
        }
    }

    @OnClick(R.id.iv_card_details_back)
    public void onBackClicked() {
        finish();
    }

    @Override
    public void onDefaultShareClick() {
        View tempView = getWindow().getDecorView();
//View tempView = button; //获取 Button 的截图
        tempView.setDrawingCacheEnabled(true);

        Bitmap bitmap = tempView.getDrawingCache();
        tempView.setDrawingCacheEnabled(false);
    }
}
