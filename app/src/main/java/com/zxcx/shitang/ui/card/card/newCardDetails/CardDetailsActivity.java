package com.zxcx.shitang.ui.card.card.newCardDetails;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.CheckBox;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.zxcx.shitang.R;
import com.zxcx.shitang.event.CollectSuccessEvent;
import com.zxcx.shitang.mvpBase.MvpActivity;
import com.zxcx.shitang.mvpBase.PostBean;
import com.zxcx.shitang.ui.card.card.collect.SelectCollectFolderDialog;
import com.zxcx.shitang.ui.card.card.share.DiyShareActivity;
import com.zxcx.shitang.ui.card.card.share.ShareCardDialog;
import com.zxcx.shitang.ui.card.card.share.ShareWayDialog;
import com.zxcx.shitang.utils.FileUtil;
import com.zxcx.shitang.utils.ScreenUtils;
import com.zxcx.shitang.utils.WebViewUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class CardDetailsActivity extends MvpActivity<CardDetailsPresenter> implements CardDetailsContract.View,
        ShareWayDialog.DefaultShareDialogListener {

    @BindView(R.id.iv_card_details_back)
    ImageView mIvCardDetailsBack;
    @BindView(R.id.tv_card_details_title)
    TextView mTvCardDetailsTitle;
    @BindView(R.id.ll_card_details)
    RelativeLayout mLlCardDetails;
    @BindView(R.id.cb_card_details_collect)
    CheckBox mCbCardDetailsCollect;
    @BindView(R.id.cb_card_details_like)
    CheckBox mCbCardDetailsLike;
    @BindView(R.id.tv_card_details_share)
    ImageView mTvCardDetailsShare;
    @BindView(R.id.rl_card_details)
    RelativeLayout mRlCardDetails;
    @BindView(R.id.fl_card_details)
    FrameLayout mFlCardDetails;

    SelectCollectFolderDialog mSelectCollectFolderDialog;
    private WebView mWebView;
    private int cardId;
    private String name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_card_details);
        ButterKnife.bind(this);
        EventBus.getDefault().register(this);

        cardId = getIntent().getIntExtra("cardId",0);
        name = getIntent().getStringExtra("name");

        mTvCardDetailsTitle.setText(name);
        mWebView = WebViewUtils.getWebView(this);
        mFlCardDetails.addView(mWebView);
        mWebView.loadUrl("https://www.baidu.com");

        mSelectCollectFolderDialog = new SelectCollectFolderDialog();
    }

    @Override
    protected void onDestroy() {
        if (mWebView != null) {
            mWebView.loadDataWithBaseURL(null, "", "text/html", "utf-8", null);
            mWebView.clearHistory();

            ((ViewGroup) mWebView.getParent()).removeView(mWebView);
            mWebView.destroy();
            mWebView = null;
        }
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Override
    protected CardDetailsPresenter createPresenter() {
        return new CardDetailsPresenter(this);
    }

    @Override
    public void getDataSuccess(CardDetailsBean bean) {
        mCbCardDetailsCollect.setText(bean.getCollectNum());
        mCbCardDetailsLike.setText(bean.getLikeNum());
        mCbCardDetailsCollect.setChecked(bean.getIsCollect() == 1);
        mCbCardDetailsLike.setChecked(bean.getIsLike() == 1);
    }

    @Override
    public void postSuccess(PostBean bean) {

    }

    @Override
    public void postFail(String msg) {

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
            mCbCardDetailsCollect.setChecked(false);
        }else {
            mSelectCollectFolderDialog.show(getFragmentManager(), "1");
            mCbCardDetailsCollect.setChecked(false);
        }
    }

    @OnClick(R.id.iv_card_details_back)
    public void onBackClicked() {
        finish();
    }

    @Override
    public void onDefaultShareClick() {
        Bitmap bitmap = ScreenUtils.getBitmapByView(mWebView);
        String fileName = FileUtil.getFileName();
        String imagePath = FileUtil.PATH_BASE + fileName;
        FileUtil.saveBitmapToSDCard(bitmap, FileUtil.PATH_BASE, fileName);

        ShareCardDialog shareCardDialog = new ShareCardDialog();
        Bundle bundle = new Bundle();
        bundle.putString("imagePath", imagePath);
        shareCardDialog.setArguments(bundle);
        shareCardDialog.show(getFragmentManager(), "");
    }

    @Override
    public void onDiyShareClick() {
        Intent intent = new Intent(this, DiyShareActivity.class);
        startActivity(intent);
    }
}
