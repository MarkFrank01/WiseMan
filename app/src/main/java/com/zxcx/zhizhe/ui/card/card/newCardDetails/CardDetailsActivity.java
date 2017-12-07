package com.zxcx.zhizhe.ui.card.card.newCardDetails;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.text.TextPaint;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.CheckBox;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.makeramen.roundedimageview.RoundedImageView;
import com.zxcx.zhizhe.R;
import com.zxcx.zhizhe.event.CollectSuccessEvent;
import com.zxcx.zhizhe.event.UnCollectEvent;
import com.zxcx.zhizhe.mvpBase.MvpActivity;
import com.zxcx.zhizhe.retrofit.APIService;
import com.zxcx.zhizhe.ui.card.card.collect.SelectCollectFolderActivity;
import com.zxcx.zhizhe.ui.card.card.share.ShareCardDialog;
import com.zxcx.zhizhe.ui.loginAndRegister.login.LoginActivity;
import com.zxcx.zhizhe.utils.GlideApp;
import com.zxcx.zhizhe.utils.SVTSConstants;
import com.zxcx.zhizhe.utils.ScreenUtils;
import com.zxcx.zhizhe.utils.SharedPreferencesUtil;
import com.zxcx.zhizhe.utils.WebViewUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class CardDetailsActivity extends MvpActivity<CardDetailsPresenter> implements CardDetailsContract.View {

    @BindView(R.id.iv_card_details_back) ImageView mIvCardDetailsBack;
    @BindView(R.id.tv_card_details_title) TextView mTvCardDetailsTitle;
    @BindView(R.id.cb_card_details_collect) CheckBox mCbCardDetailsCollect;
    @BindView(R.id.cb_card_details_like) CheckBox mCbCardDetailsLike;
    @BindView(R.id.iv_card_details_share) ImageView mTvCardDetailsShare;
    @BindView(R.id.fl_card_details) FrameLayout mFlCardDetails;
    @BindView(R.id.cb_card_details_un_like) CheckBox mCbCardDetailsUnLike;
    @BindView(R.id.iv_card_details) ImageView mIvCardDetails;
    @BindView(R.id.tv_card_details_info) TextView mTvCardDetailsInfo;
    @BindView(R.id.tv_card_details_card_bag) TextView mTvCardDetailsCardBag;
    @BindView(R.id.iv_item_rank_user) RoundedImageView mIvItemRankUser;
    @BindView(R.id.tv_item_rank_user_name) TextView mTvItemRankUserName;
    @BindView(R.id.tv_item_rank_user_card) TextView mTvItemRankUserCard;
    @BindView(R.id.tv_item_rank_user_fans) TextView mTvItemRankUserFans;
    @BindView(R.id.tv_item_rank_user_read) TextView mTvItemRankUserRead;
    @BindView(R.id.cb_card_details_follow) CheckBox mCbCardDetailsFollow;
    @BindView(R.id.rl_card_details_bottom) RelativeLayout mRlCardDetailsBottom;
    @BindView(R.id.view_line) View mViewLine;
    @BindView(R.id.ll_card_details_bottom) LinearLayout mLlCardDetailsBottom;

    private WebView mWebView;
    private int cardId;
    private String name;
    private String cardBagName;
    private int collectNum;
    private String imageUrl;
    private boolean isUnCollect = false;
    private String date;
    private String author;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_card_details);
        ButterKnife.bind(this);
        EventBus.getDefault().register(this);

        initData();
        initView();


        mPresenter.getCardDetails(cardId);

        ViewGroup.LayoutParams para = mIvCardDetails.getLayoutParams();
        int screenWidth = ScreenUtils.getScreenWidth(); //屏幕宽度
        para.height = (screenWidth * 9 / 16);
        mIvCardDetails.setLayoutParams(para);
    }

    @Override
    public void initStatusBar() {

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

    @Override
    public void onReload(View v) {
        mPresenter.getCardDetails(cardId);
    }

    @Override
    public void getDataSuccess(CardDetailsBean bean) {
        postSuccess(bean);
    }

    @Override
    public void toastFail(String msg) {
        super.toastFail(msg);
    }

    @Override
    public void UnCollectSuccess() {
        mCbCardDetailsCollect.setChecked(false);
        isUnCollect = true;
        collectNum--;
        mCbCardDetailsCollect.setText(collectNum + "");
    }

    @Override
    public void postSuccess(CardDetailsBean bean) {
        collectNum = bean.getCollectNum();
        int likeNum = bean.getLikeNum();
        int unLikeNum = bean.getUnLikeNum();
        cardBagName = bean.getCardBagName();
        mTvCardDetailsCardBag.setText(cardBagName);
        mCbCardDetailsCollect.setText(collectNum + "");
        mCbCardDetailsLike.setText(likeNum + "");
        mCbCardDetailsUnLike.setText(unLikeNum + "");
        mCbCardDetailsCollect.setChecked(bean.getIsCollect());
        mCbCardDetailsLike.setChecked(bean.getIsLike());
        mCbCardDetailsUnLike.setChecked(bean.isUnLike());
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

    @OnClick(R.id.cb_card_details_collect)
    public void onCbCollectClicked() {
        //checkBox点击之后选中状态就已经更改了
        mCbCardDetailsCollect.setChecked(!mCbCardDetailsCollect.isChecked());
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

    @OnClick(R.id.cb_card_details_like)
    public void onCbLikeClicked() {
        //checkBox点击之后选中状态就已经更改了
        mCbCardDetailsLike.setChecked(!mCbCardDetailsLike.isChecked());
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

    @OnClick(R.id.cb_card_details_un_like)
    public void onCbUnLikeClicked() {
        //checkBox点击之后选中状态就已经更改了
        mCbCardDetailsUnLike.setChecked(!mCbCardDetailsUnLike.isChecked());
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

    @OnClick(R.id.iv_card_details_back)
    public void onBackClicked() {
        onBackPressed();
    }

    @OnClick(R.id.cb_card_details_follow)
    public void onCbFollowClicked(){
        //checkBox点击之后选中状态就已经更改了
        mCbCardDetailsFollow.setChecked(!mCbCardDetailsFollow.isChecked());
    }

    private void initData() {
        cardId = getIntent().getIntExtra("id", 0);
        name = getIntent().getStringExtra("name");
        imageUrl = getIntent().getStringExtra("imageUrl");
        date = getIntent().getStringExtra("date");
        author = getIntent().getStringExtra("author");
    }

    private void initView() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            // 延迟共享动画的执行
            //postponeEnterTransition();
        }
        TextPaint paint = mTvCardDetailsTitle.getPaint();
        paint.setFakeBoldText(true);
        mTvCardDetailsTitle.setText(name);
        mTvCardDetailsInfo.setText(getString(R.string.tv_card_info, date, author));

        GlideApp
                .with(this)
                .load(imageUrl)
                .placeholder(R.drawable.default_card)
                .error(R.drawable.default_card)
                .into(new SimpleTarget<Drawable>() {
                    @Override
                    public void onResourceReady(Drawable resource, Transition<? super Drawable> transition) {
                        mIvCardDetails.setImageDrawable(resource);
                        //图片加载完成的回调中，启动过渡动画
                        //supportStartPostponedEnterTransition();
                    }
                });

        //获取WebView，并将WebView高度设为WRAP_CONTENT
        mWebView = WebViewUtils.getWebView(this);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        mWebView.setLayoutParams(params);

        mWebView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                return true;
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                mLlCardDetailsBottom.setVisibility(View.VISIBLE);
                mRlCardDetailsBottom.setVisibility(View.VISIBLE);
                mViewLine.setVisibility(View.VISIBLE);
            }
        });
        mFlCardDetails.addView(mWebView);
        boolean isNight = SharedPreferencesUtil.getBoolean(SVTSConstants.isNight, false);
        if (isNight) {
            mWebView.loadUrl(APIService.API_SERVER_URL + "/view/articleDark/" + cardId);
        } else {
            mWebView.loadUrl(APIService.API_SERVER_URL + "/view/articleLight/" + cardId);
        }
    }
}
