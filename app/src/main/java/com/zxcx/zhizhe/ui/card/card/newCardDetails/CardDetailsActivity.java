package com.zxcx.zhizhe.ui.card.card.newCardDetails;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.CheckBox;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.kingja.loadsir.core.LoadSir;
import com.zxcx.zhizhe.R;
import com.zxcx.zhizhe.event.CollectSuccessEvent;
import com.zxcx.zhizhe.event.UnCollectEvent;
import com.zxcx.zhizhe.loadCallback.NetworkErrorCallback;
import com.zxcx.zhizhe.mvpBase.MvpActivity;
import com.zxcx.zhizhe.retrofit.APIService;
import com.zxcx.zhizhe.ui.card.card.collect.SelectCollectFolderActivity;
import com.zxcx.zhizhe.ui.card.card.share.ShareCardDialog;
import com.zxcx.zhizhe.ui.loginAndRegister.login.LoginActivity;
import com.zxcx.zhizhe.utils.SVTSConstants;
import com.zxcx.zhizhe.utils.SharedPreferencesUtil;
import com.zxcx.zhizhe.utils.WebViewUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class CardDetailsActivity extends MvpActivity<CardDetailsPresenter> implements CardDetailsContract.View {

    @BindView(R.id.iv_card_details_back)
    ImageView mIvCardDetailsBack;
    @BindView(R.id.tv_card_details_title)
    TextView mTvCardDetailsTitle;
    @BindView(R.id.cb_card_details_collect)
    CheckBox mCbCardDetailsCollect;
    @BindView(R.id.cb_card_details_like)
    CheckBox mCbCardDetailsLike;
    @BindView(R.id.iv_card_details_share)
    ImageView mTvCardDetailsShare;
    @BindView(R.id.fl_card_details)
    FrameLayout mFlCardDetails;
    @BindView(R.id.ll_toolbar)
    RelativeLayout mLlToolbar;
    @BindView(R.id.cb_card_details_un_like)
    CheckBox mCbCardDetailsUnLike;

    private WebView mWebView;
    private int cardId;
    private String name;
    private String cardBagName;
    private int collectNum;
    private String imageUrl;
    private boolean isUnCollect = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_card_details);
        ButterKnife.bind(this);
        EventBus.getDefault().register(this);

        cardId = getIntent().getIntExtra("id", 0);

        mWebView = WebViewUtils.getWebView(this);
        mFlCardDetails.addView(mWebView);
        boolean isNight = SharedPreferencesUtil.getBoolean(SVTSConstants.isNight, false);
        if (isNight) {
            mWebView.loadUrl(APIService.API_SERVER_URL + "/view/articleDark/" + cardId);
        } else {
            mWebView.loadUrl(APIService.API_SERVER_URL + "/view/articleLight/" + cardId);
        }
        mPresenter.getCardDetails(cardId);

        initLoadSir();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (isUnCollect) {
            EventBus.getDefault().post(new UnCollectEvent(cardId));
        }
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

    public void initLoadSir() {
        loadService = LoadSir.getDefault().register(this, this);
    }

    @Override
    public void onReload(View v) {
        mPresenter.getCardDetails(cardId);
    }

    @Override
    public void getDataSuccess(CardDetailsBean bean) {
        loadService.showSuccess();
        collectNum = bean.getCollectNum();
        int likeNum = bean.getLikeNum();
        int unLikeNum = bean.getUnLikeNum();
        imageUrl = bean.getImageUrl();
        name = bean.getName();
        cardBagName = bean.getCardBagName();
        mTvCardDetailsTitle.setText(cardBagName);
        mCbCardDetailsCollect.setText(collectNum + "");
        mCbCardDetailsLike.setText(likeNum + "");
        mCbCardDetailsUnLike.setText(unLikeNum + "");
        mCbCardDetailsCollect.setChecked(bean.getIsCollect());
        mCbCardDetailsLike.setChecked(bean.getIsLike());
        mCbCardDetailsUnLike.setChecked(bean.isUnLike());
    }

    @Override
    public void toastFail(String msg) {
        super.toastFail(msg);
        loadService.showCallback(NetworkErrorCallback.class);
    }

    @Override
    public void UnCollectSuccess() {
        mCbCardDetailsCollect.setChecked(false);
        isUnCollect = true;
        collectNum--;
        mCbCardDetailsCollect.setText(collectNum + "");
    }

    @Override
    public void postSuccess() {

    }

    @Override
    public void postFail(String msg) {
        toastShow(msg);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(CollectSuccessEvent event) {
        toastShow("收藏成功");
        isUnCollect = false;
        mCbCardDetailsCollect.setChecked(true);
        collectNum++;
        mCbCardDetailsCollect.setText(collectNum + "");
    }

    @OnClick(R.id.iv_card_details_share)
    public void onShareClicked() {
        ShareCardDialog shareCardDialog = new ShareCardDialog();
        Bundle bundle = new Bundle();
        bundle.putString("title", name);
        bundle.putString("text", cardBagName);
        bundle.putString("url", APIService.API_SERVER_URL + "/view/articleLight/" + cardId);
        bundle.putString("imageUrl", imageUrl);
        shareCardDialog.setArguments(bundle);
        shareCardDialog.show(getFragmentManager(), "");
    }

    @OnClick(R.id.ll_card_details_collect)
    public void onCollectClicked() {
        if (!mCbCardDetailsCollect.isChecked()) {
            if (SharedPreferencesUtil.getInt(SVTSConstants.userId, 0) != 0) {
                Intent intent = new Intent(mActivity, SelectCollectFolderActivity.class);
                intent.putExtra("cardId", cardId);
                startActivity(intent);
            } else {
                toastShow("请先登录");
                startActivity(new Intent(mActivity, LoginActivity.class));
            }
        } else {
            mPresenter.removeCollectCard(cardId);
        }
    }

    @OnClick(R.id.ll_card_details_like)
    public void onLikeClicked() {
        if (!mCbCardDetailsLike.isChecked()) {
            if (SharedPreferencesUtil.getInt(SVTSConstants.userId, 0) != 0) {
                mPresenter.likeCard(cardId);
            } else {
                toastShow("请先登录");
                startActivity(new Intent(mActivity, LoginActivity.class));
            }
        } else {
            mPresenter.removeLikeCard(cardId);
        }
    }

    @OnClick(R.id.ll_card_details_un_like)
    public void onUnLikeClicked() {
        if (!mCbCardDetailsUnLike.isChecked()) {
            if (SharedPreferencesUtil.getInt(SVTSConstants.userId, 0) != 0) {
                mPresenter.unLikeCard(cardId);
            } else {
                toastShow("请先登录");
                startActivity(new Intent(mActivity, LoginActivity.class));
            }
        } else {
            mPresenter.removeUnLikeCard(cardId);
        }
    }

    @OnClick(R.id.cb_card_details_collect)
    public void onCbCollectClicked() {
        //checkBox点击之后选中状态就已经更改了
        mCbCardDetailsCollect.setChecked(!mCbCardDetailsCollect.isChecked());
        onCollectClicked();
    }

    @OnClick(R.id.cb_card_details_like)
    public void onCbLikeClicked() {
        //checkBox点击之后选中状态就已经更改了
        mCbCardDetailsLike.setChecked(!mCbCardDetailsLike.isChecked());
        onLikeClicked();
    }

    @OnClick(R.id.cb_card_details_un_like)
    public void onCbUnLikeClicked() {
        //checkBox点击之后选中状态就已经更改了
        mCbCardDetailsUnLike.setChecked(!mCbCardDetailsUnLike.isChecked());
        onUnLikeClicked();
    }

    @OnClick(R.id.iv_card_details_back)
    public void onBackClicked() {
        onBackPressed();
    }
}
