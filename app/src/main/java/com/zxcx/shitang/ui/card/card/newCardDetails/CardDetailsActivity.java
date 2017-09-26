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
import com.zxcx.shitang.retrofit.APIService;
import com.zxcx.shitang.ui.card.card.collect.SelectCollectFolderActivity;
import com.zxcx.shitang.ui.card.card.share.DiyShareActivity;
import com.zxcx.shitang.ui.card.card.share.ShareCardDialog;
import com.zxcx.shitang.ui.card.card.share.ShareWayDialog;
import com.zxcx.shitang.ui.loginAndRegister.login.LoginActivity;
import com.zxcx.shitang.utils.FileUtil;
import com.zxcx.shitang.utils.SVTSConstants;
import com.zxcx.shitang.utils.ScreenUtils;
import com.zxcx.shitang.utils.SharedPreferencesUtil;
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

    private WebView mWebView;
    private int cardId;
    private String name;
    private int likeNum;
    private int collectNum;
    private Action mAction;

    private enum Action{
        collect,
        unCollect,
        like,
        unLike
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_card_details);
        ButterKnife.bind(this);
        EventBus.getDefault().register(this);

        cardId = getIntent().getIntExtra("id", 0);
        name = getIntent().getStringExtra("name");

        mTvCardDetailsTitle.setText(name);
        mWebView = WebViewUtils.getWebView(this);
        mFlCardDetails.addView(mWebView);
        boolean isNight = SharedPreferencesUtil.getBoolean(SVTSConstants.isNight,false);
        if (isNight){
            mWebView.loadUrl(APIService.API_SERVER_URL + "/view/articleDark/" + cardId);
        }else {
            mWebView.loadUrl(APIService.API_SERVER_URL + "/view/articleLight/" + cardId);
        }

        mPresenter.getCardDetails(cardId);
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

        collectNum = bean.getCollectNum();
        likeNum = bean.getLikeNum();
        mCbCardDetailsCollect.setText(collectNum + "");
        mCbCardDetailsLike.setText(likeNum + "");
        mCbCardDetailsCollect.setChecked(bean.getIsCollect());
        mCbCardDetailsLike.setChecked(bean.getIsLike());
    }

    @Override
    public void postSuccess(PostBean bean) {
        if (mAction == Action.collect){

        }else if (mAction == Action.unCollect){
            collectNum--;
            mCbCardDetailsCollect.setText(collectNum + "");
        }else if (mAction == Action.like){
            likeNum++;
            mCbCardDetailsLike.setText(likeNum + "");
        }else if (mAction == Action.unLike){
            likeNum--;
            mCbCardDetailsLike.setText(likeNum + "");
        }
    }

    @Override
    public void postFail(String msg) {
        toastShow(msg);
        if (mAction == Action.collect){

        }else if (mAction == Action.unCollect){
            mCbCardDetailsCollect.setChecked(true);
        }else if (mAction == Action.like){
            mCbCardDetailsLike.setChecked(false);
        }else if (mAction == Action.unLike){
            mCbCardDetailsLike.setChecked(true);
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(CollectSuccessEvent event) {
        toastShow("收藏成功");
        mCbCardDetailsCollect.setChecked(true);
        collectNum++;
        mCbCardDetailsCollect.setText(collectNum + "");
    }

    @OnClick(R.id.tv_card_details_share)
    public void onShareClicked() {
        ShareWayDialog shareWayDialog = new ShareWayDialog();
        shareWayDialog.setListener(this);
        shareWayDialog.show(getFragmentManager(), "");
    }

    @OnClick(R.id.cb_card_details_collect)
    public void onCollectClicked() {
        //checkBox点击之后选中状态就已经更改了
        if (mCbCardDetailsCollect.isChecked()) {
            mCbCardDetailsCollect.setChecked(false);
            if (SharedPreferencesUtil.getInt(SVTSConstants.userId,0) != 0){
                Intent intent = new Intent(mActivity,SelectCollectFolderActivity.class);
                intent.putExtra("cardId", cardId);
                startActivity(intent);
            }else {
                toastShow("请先登录");
                startActivity(new Intent(mActivity, LoginActivity.class));
            }
        } else {
            mAction = Action.unCollect;
            mCbCardDetailsCollect.setChecked(false);
            mPresenter.removeCollectCard(cardId);
        }
    }

    @OnClick(R.id.cb_card_details_like)
    public void onMCbCardDetailsLikeClicked() {
        //checkBox点击之后选中状态就已经更改了
        if (mCbCardDetailsLike.isChecked()) {
            if (SharedPreferencesUtil.getInt(SVTSConstants.userId,0) != 0){
                mAction = Action.like;
                mPresenter.likeCard(cardId);
            }else {
                toastShow("请先登录");
                startActivity(new Intent(mActivity, LoginActivity.class));
            }
        } else {
            mAction = Action.unLike;
            mCbCardDetailsLike.setChecked(false);
            mPresenter.unLikeCard(cardId);
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
