package com.zxcx.zhizhe.ui.card.card.cardDetails;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.widget.NestedScrollView;
import android.text.TextPaint;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.zxcx.zhizhe.R;
import com.zxcx.zhizhe.event.CollectSuccessEvent;
import com.zxcx.zhizhe.mvpBase.MvpActivity;
import com.zxcx.zhizhe.ui.card.card.collect.SelectCollectFolderActivity;
import com.zxcx.zhizhe.ui.card.card.share.DiyShareActivity;
import com.zxcx.zhizhe.ui.card.card.share.ShareCardDialog;
import com.zxcx.zhizhe.ui.card.card.share.ShareWayDialog;
import com.zxcx.zhizhe.utils.FileUtil;
import com.zxcx.zhizhe.utils.ScreenUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class OldCardDetailsActivity extends MvpActivity<OldCardDetailsPresenter> implements OldCardDetailsContract.View,
        ShareWayDialog.DefaultShareDialogListener {

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

    @BindView(R.id.scv_card_details)
    NestedScrollView mScvCardDetails;
    @BindView(R.id.tv_card_details_title)
    TextView mTvCardDetailsTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_card_details);
        ButterKnife.bind(this);
        EventBus.getDefault().register(this);

        TextPaint tp = mTvCardDetailsSubhead.getPaint();
        tp.setFakeBoldText(true);
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus) {
//            TextViewUtils.adjustTvTextSize(mTvCardDetailsContent);
            ViewGroup.LayoutParams para = mIvCardDetails.getLayoutParams();
            para.height = mIvCardDetails.getWidth() * 3 / 4;
            mIvCardDetails.setLayoutParams(para);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Override
    protected OldCardDetailsPresenter createPresenter() {
        return new OldCardDetailsPresenter(this);
    }

    @Override
    public void getDataSuccess(OldCardDetailsBean bean) {

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(CollectSuccessEvent event) {
        mCbCardDetailsCollect.setChecked(true);
    }

    @OnClick(R.id.tv_card_details_share)
    public void onShareClicked() {
        ShareWayDialog shareWayDialog = new ShareWayDialog();
        shareWayDialog.setListener(this);
        shareWayDialog.show(getFragmentManager(), "");
    }

    @OnClick(R.id.cb_card_details_collect)
    public void onCollectClicked() {
        if (mCbCardDetailsCollect.isChecked()) {
            Intent intent = new Intent(mActivity,SelectCollectFolderActivity.class);
            startActivity(intent);
            mCbCardDetailsCollect.setChecked(false);
        }
    }

    @OnClick(R.id.iv_card_details_back)
    public void onBackClicked() {
        finish();
    }

    @Override
    public void onDefaultShareClick() {
        Bitmap bitmap = ScreenUtils.getBitmapByView(mScvCardDetails);
        String fileName = FileUtil.getRandomImageName();
        String imagePath = FileUtil.PATH_BASE + fileName;
        FileUtil.saveBitmapToSDCard(bitmap,FileUtil.PATH_BASE,fileName);

        ShareCardDialog shareCardDialog = new ShareCardDialog();
        Bundle bundle = new Bundle();
        bundle.putString("imagePath", imagePath);
        shareCardDialog.setArguments(bundle);
        shareCardDialog.show(getFragmentManager(),"");
    }

    @Override
    public void onDiyShareClick() {
        Intent intent = new Intent(this, DiyShareActivity.class);
        startActivity(intent);
    }
}
