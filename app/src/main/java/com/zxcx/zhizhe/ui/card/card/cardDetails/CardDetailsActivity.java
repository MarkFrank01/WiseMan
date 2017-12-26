package com.zxcx.zhizhe.ui.card.card.cardDetails;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextPaint;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.ValueCallback;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.CheckBox;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.makeramen.roundedimageview.RoundedImageView;
import com.zxcx.zhizhe.R;
import com.zxcx.zhizhe.event.SaveCardNoteSuccessEvent;
import com.zxcx.zhizhe.event.UnCollectEvent;
import com.zxcx.zhizhe.event.UnFollowConfirmEvent;
import com.zxcx.zhizhe.event.UnLikeEvent;
import com.zxcx.zhizhe.mvpBase.MvpActivity;
import com.zxcx.zhizhe.retrofit.APIService;
import com.zxcx.zhizhe.ui.card.cardBag.CardBagActivity;
import com.zxcx.zhizhe.ui.my.followUser.UnFollowConfirmDialog;
import com.zxcx.zhizhe.ui.otherUser.OtherUserActivity;
import com.zxcx.zhizhe.utils.DateTimeUtils;
import com.zxcx.zhizhe.utils.ImageLoader;
import com.zxcx.zhizhe.utils.LogCat;
import com.zxcx.zhizhe.utils.SVTSConstants;
import com.zxcx.zhizhe.utils.ScreenUtils;
import com.zxcx.zhizhe.utils.SharedPreferencesUtil;
import com.zxcx.zhizhe.utils.StringUtils;
import com.zxcx.zhizhe.utils.WebViewUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.Flowable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.subscribers.DisposableSubscriber;

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
    @BindView(R.id.cb_card_details_un_like)
    CheckBox mCbCardDetailsUnLike;
    @BindView(R.id.iv_card_details)
    ImageView mIvCardDetails;
    @BindView(R.id.tv_card_details_info)
    TextView mTvCardDetailsInfo;
    @BindView(R.id.tv_card_details_card_bag)
    TextView mTvCardDetailsCardBag;
    @BindView(R.id.iv_item_rank_user)
    RoundedImageView mIvItemRankUser;
    @BindView(R.id.tv_item_rank_user_name)
    TextView mTvItemRankUserName;
    @BindView(R.id.tv_item_rank_user_card)
    TextView mTvItemRankUserCard;
    @BindView(R.id.tv_item_rank_user_fans)
    TextView mTvItemRankUserFans;
    @BindView(R.id.tv_item_rank_user_read)
    TextView mTvItemRankUserRead;
    @BindView(R.id.cb_card_details_follow)
    CheckBox mCbCardDetailsFollow;
    @BindView(R.id.rl_card_details_bottom)
    RelativeLayout mRlCardDetailsBottom;
    @BindView(R.id.view_line)
    View mViewLine;
    @BindView(R.id.ll_card_details_bottom)
    LinearLayout mLlCardDetailsBottom;
    @BindView(R.id.sv_card_details)
    ScrollView mSvCardDetails;
    @BindView(R.id.tv_toast)
    TextView mTvToast;

    private WebView mWebView;
    private int cardId;
    private int cardBagId;
    private int mAuthorId;
    private String name;
    private String cardBagName;
    private String imageUrl;
    private boolean collectStatus = false;
    private boolean isUnCollect = false;
    private boolean likeStatus = false;
    private boolean isUnLike = false;
    private String date;
    private String author;
    private ActionMode mActionMode;
    private static final int MENU_ITEM_NOTE = 0;
    private static final int MENU_ITEM_SHARE = 1;
    private String mUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_card_details);
        super.onCreate(savedInstanceState);
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
        if (isUnLike) {
            EventBus.getDefault().post(new UnLikeEvent(cardId));
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
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }

    @Nullable
    @Override
    public void onActionModeStarted(ActionMode mode) {
        if (mActionMode == null) {
            super.onActionModeStarted(mode);
            mActionMode = mode;
            Menu menu = mode.getMenu();
            menu.clear();
            MenuItemClickListener menuItemClickListener = new MenuItemClickListener();
            menu.add(0, MENU_ITEM_NOTE, 0, "存为笔记").setOnMenuItemClickListener(menuItemClickListener);
            menu.add(0, MENU_ITEM_SHARE, 0, "摘取分享").setOnMenuItemClickListener(menuItemClickListener);
        }
    }


    @Override
    public void onActionModeFinished(ActionMode mode) {
        mActionMode = null;
        mWebView.clearFocus();//移除高亮显示,如果不移除在三星s6手机上会崩溃
        super.onActionModeFinished(mode);
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
        collectStatus = bean.isCollect();
        likeStatus = bean.isLike();
        postSuccess(bean);
        //进入时只有id的时候，在这里初始化界面
        name = bean.getName();
        imageUrl = bean.getImageUrl();
        date = DateTimeUtils.getDateString(bean.getDate());
        author = bean.getAuthorName();
        mTvCardDetailsTitle.setText(name);
        mTvCardDetailsInfo.setText(getString(R.string.tv_card_info, date, author));
        ImageLoader.load(mActivity, imageUrl, R.drawable.default_card, mIvCardDetails);
    }

    @Override
    public void toastFail(String msg) {
        super.toastFail(msg);
    }

    @Override
    public void postSuccess(CardDetailsBean bean) {
        isUnCollect = collectStatus != bean.isCollect() && !bean.isCollect();
        isUnLike = likeStatus != bean.isLike() && !bean.isLike();

        int collectNum = bean.getCollectNum();
        int likeNum = bean.getLikeNum();
        int unLikeNum = bean.getUnLikeNum();
        cardBagName = bean.getCardBagName();
        cardBagId = bean.getCardBagId();
        mTvCardDetailsCardBag.setText(cardBagName);
        mCbCardDetailsCollect.setText(collectNum);
        mCbCardDetailsLike.setText(likeNum);
        mCbCardDetailsUnLike.setText(unLikeNum);
        mCbCardDetailsCollect.setChecked(bean.getIsCollect());
        mCbCardDetailsLike.setChecked(bean.getIsLike());
        mCbCardDetailsUnLike.setChecked(bean.isUnLike());
        mAuthorId = bean.getAuthorId();
        ImageLoader.load(mActivity, bean.getAuthorIcon(), R.drawable.default_header, mIvItemRankUser);
        mTvItemRankUserName.setText(bean.getAuthorName());
        mTvItemRankUserCard.setText(bean.getAuthorCardNum());
        mTvItemRankUserFans.setText(bean.getAuthorFansNum());
        mTvItemRankUserRead.setText(bean.getAuthorReadNum());
        if (bean.getFollowType() == 0) {
            mCbCardDetailsFollow.setText("关注");
            mCbCardDetailsFollow.setChecked(false);
        } else {
            mCbCardDetailsFollow.setText("已关注");
            mCbCardDetailsFollow.setChecked(true);
        }
    }

    @Override
    public void postFail(String msg) {
        toastShow(msg);
    }

    @Override
    public void followSuccess() {
        if (mCbCardDetailsFollow.isChecked()) {
            //取消成功
            mCbCardDetailsFollow.setText("关注");
            mCbCardDetailsFollow.setChecked(false);
        } else {
            //关注成功
            mCbCardDetailsFollow.setText("已关注");
            mCbCardDetailsFollow.setChecked(true);
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(UnFollowConfirmEvent event) {
        //取消关注
        mPresenter.setUserFollow(mAuthorId, 1);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(SaveCardNoteSuccessEvent event) {
        //卡片笔记保存成功
        toastShow("保存成功");
    }

    @OnClick(R.id.cb_card_details_follow)
    public void onMCbCardDetailsFollowClicked() {
        mCbCardDetailsFollow.setChecked(!mCbCardDetailsFollow.isChecked());
        if (!mCbCardDetailsFollow.isChecked()) {
            //关注
            mPresenter.setUserFollow(mAuthorId, 0);
        } else {
            //取消关注弹窗
            UnFollowConfirmDialog dialog = new UnFollowConfirmDialog();
            Bundle bundle = new Bundle();
            bundle.putInt("userId", mAuthorId);
            dialog.setArguments(bundle);
            dialog.show(getFragmentManager(), "");
        }
    }

    @OnClick(R.id.rl_card_details_bottom)
    public void onMRlCardDetailsBottomClicked() {
        Intent intent = new Intent(this, OtherUserActivity.class);
        intent.putExtra("name", author);
        intent.putExtra("id", mAuthorId);
        startActivity(intent);
    }

    @OnClick(R.id.fl_card_details_card_bag)
    public void onMFlCardDetailsCardBagClicked() {
        Intent intent = new Intent(this, CardBagActivity.class);
        intent.putExtra("name", cardBagName);
        intent.putExtra("id", cardBagId);
        startActivity(intent);
    }

    @OnClick(R.id.iv_card_details_share)
    public void onShareClicked() {
        gotoShare(mUrl, null);
    }

    @OnClick(R.id.cb_card_details_collect)
    public void onCbCollectClicked() {
        //checkBox点击之后选中状态就已经更改了
        mCbCardDetailsCollect.setChecked(!mCbCardDetailsCollect.isChecked());
        if (!mCbCardDetailsCollect.isChecked()) {
            mPresenter.addCollectCard(cardId);
        } else {
            mPresenter.removeCollectCard(cardId);
        }
    }

    @OnClick(R.id.cb_card_details_like)
    public void onCbLikeClicked() {
        //checkBox点击之后选中状态就已经更改了
        mCbCardDetailsLike.setChecked(!mCbCardDetailsLike.isChecked());
        if (!mCbCardDetailsLike.isChecked()) {
            if (checkLogin()) {
                mPresenter.likeCard(cardId);
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
            if (checkLogin()) {
                mPresenter.unLikeCard(cardId);
            }
        } else {
            mPresenter.removeUnLikeCard(cardId);
        }
    }

    @OnClick(R.id.iv_card_details_back)
    public void onBackClicked() {
        onBackPressed();
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
        if (!StringUtils.isEmpty(name))
            mTvCardDetailsTitle.setText(name);
        if (!StringUtils.isEmpty(author) && !StringUtils.isEmpty(date))
            mTvCardDetailsInfo.setText(getString(R.string.tv_card_info, date, author));
        if (!StringUtils.isEmpty(imageUrl))
            ImageLoader.load(mActivity, imageUrl, R.drawable.default_card, mIvCardDetails);
        /*GlideApp
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
                });*/

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
            mUrl = APIService.API_SERVER_URL + "/view/articleDark/" + cardId;
        } else {
            mUrl = APIService.API_SERVER_URL + "/view/articleLight/" + cardId;
//            mUrl = "http://192.168.1.149:8043/view/articleDark/192";

        }
        mWebView.loadUrl(mUrl);
    }

    @Override
    public void toastShow(String text) {
        mTvToast.setVisibility(View.VISIBLE);
        mTvToast.setText(text);
        mDisposable = Flowable.timer(2, TimeUnit.SECONDS, AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableSubscriber<Long>() {

                    @Override
                    public void onNext(Long aLong) {
                        mTvToast.setVisibility(View.GONE);
                    }

                    @Override
                    public void onError(Throwable t) {
                        mTvToast.setVisibility(View.GONE);
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    private void gotoShare(String url, String content) {
        ShareCardDialog shareDialog = new ShareCardDialog();
        Bundle bundle = new Bundle();
        bundle.putString("name", name);
        if (!StringUtils.isEmpty(content)) {
            bundle.putString("content", content);
        } else {
            bundle.putString("url", url);
        }
        bundle.putString("imageUrl", imageUrl);
        bundle.putString("date", date);
        bundle.putString("author", author);
        bundle.putString("cardBagName", cardBagName);
        bundle.putInt("cardBagId", cardBagId);
        shareDialog.setArguments(bundle);
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            transaction.addSharedElement(mIvCardDetails, "cardImage");
            transaction.addSharedElement(mTvCardDetailsTitle, "cardTitle");
            transaction.addSharedElement(mTvCardDetailsInfo, "cardInfo");
            transaction.addSharedElement(mTvCardDetailsCardBag, "cardBag");

        }
        shareDialog.show(transaction, "");
    }

    private class MenuItemClickListener implements MenuItem.OnMenuItemClickListener {

        @Override
        public boolean onMenuItemClick(MenuItem item) {
            mWebView.evaluateJavascript("getValue()", new ValueCallback<String>() {
                @Override
                public void onReceiveValue(String value) {
                    String content = value.replaceAll("\\\\u003C", "<").replaceAll("\\\\\"","");
                    content = content != null ? content.substring(1, content.length() - 1) : null;
                    LogCat.d(content);
                    switch (item.getItemId()) {
                        case MENU_ITEM_NOTE:
                            if (checkLogin()) {
                                NoteTitleDialog noteTitleDialog = new NoteTitleDialog();
                                Bundle bundle = new Bundle();
                                bundle.putInt("withCardId", cardId);
                                bundle.putString("title", name);
                                bundle.putString("imageUrl", imageUrl);
                                bundle.putString("content", content);
                                noteTitleDialog.setArguments(bundle);
                                noteTitleDialog.show(getFragmentManager(), "");
                            }
                            break;
                        case MENU_ITEM_SHARE:
                            gotoShare(mUrl, content);
                            break;
                    }
                }
            });
            mActionMode.finish();
            return true;
        }
    }
}
