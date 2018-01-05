package com.zxcx.zhizhe.ui.my.creation.creationDetails;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextPaint;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.kingja.loadsir.core.LoadSir;
import com.zxcx.zhizhe.R;
import com.zxcx.zhizhe.event.SaveFreedomNoteSuccessEvent;
import com.zxcx.zhizhe.loadCallback.CardDetailsLoadingCallback;
import com.zxcx.zhizhe.loadCallback.CardDetailsNetworkErrorCallback;
import com.zxcx.zhizhe.mvpBase.MvpActivity;
import com.zxcx.zhizhe.retrofit.APIService;
import com.zxcx.zhizhe.ui.my.creation.newCreation.NewCreationTitleActivity;
import com.zxcx.zhizhe.utils.DateTimeUtils;
import com.zxcx.zhizhe.utils.ImageLoader;
import com.zxcx.zhizhe.utils.SVTSConstants;
import com.zxcx.zhizhe.utils.ScreenUtils;
import com.zxcx.zhizhe.utils.SharedPreferencesUtil;
import com.zxcx.zhizhe.utils.StringUtils;
import com.zxcx.zhizhe.utils.WebViewUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class RejectDetailsActivity extends MvpActivity<RejectDetailsPresenter> implements RejectDetailsContract.View {

    @BindView(R.id.tv_reject_details_title)
    TextView mTvRejectDetailsTitle;
    @BindView(R.id.fl_reject_details)
    FrameLayout mFlRejectDetails;
    @BindView(R.id.iv_reject_details)
    ImageView mIvRejectDetails;
    @BindView(R.id.tv_reject_details_info)
    TextView mTvRejectDetailsInfo;
    @BindView(R.id.tv_reject_reason)
    TextView mTvRejectReason;
    @BindView(R.id.tv_reject_reedit)
    TextView mTvRejectReedit;
    @BindView(R.id.tv_reject_details_reject_bag)
    TextView mTvRejectDetailsRejectBag;

    private WebView mWebView;
    private int cardId;
    private int cardBagId;
    private String name;
    private String author;
    private String imageUrl;
    private String date;
    private String mUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_reject_details);
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
        EventBus.getDefault().register(this);

        initData();
        initView();

        mPresenter.getRejectDetails(cardId);

        ViewGroup.LayoutParams para = mIvRejectDetails.getLayoutParams();
        int screenWidth = ScreenUtils.getScreenWidth(); //屏幕宽度
        para.height = (screenWidth * 9 / 16);
        mIvRejectDetails.setLayoutParams(para);
    }

    @Override
    public void initStatusBar() {

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

    @Override
    protected RejectDetailsPresenter createPresenter() {
        return new RejectDetailsPresenter(this);
    }

    @Override
    public void getDataSuccess(RejectDetailsBean bean) {
        //进入时只有id的时候，在这里初始化界面
        name = bean.getName();
        imageUrl = bean.getImageUrl();
        date = DateTimeUtils.getDateString(bean.getDate());
        author = bean.getAuthorName();
        cardBagId = bean.getCardBagId();
        String rejectReason = bean.getRejectReason();
        mTvRejectReason.setText(rejectReason);
        mTvRejectDetailsTitle.setText(name);
        mTvRejectDetailsInfo.setText(getString(R.string.tv_card_info, date, author));
        mTvRejectDetailsRejectBag.setText(bean.getCardBagName());
        ImageLoader.load(mActivity, imageUrl, R.drawable.default_card, mIvRejectDetails);
    }

    @Override
    public void toastFail(String msg) {
        super.toastFail(msg);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(SaveFreedomNoteSuccessEvent event) {
        finish();
    }

    @OnClick(R.id.iv_reject_details_back)
    public void onMIvRejectDetailsBackClicked() {
        onBackPressed();
    }

    @OnClick(R.id.tv_reject_reedit)
    public void onMTvRejectReeditClicked() {
        Intent intent = new Intent(mActivity, NewCreationTitleActivity.class);
        intent.putExtra("cardId", cardId);
        intent.putExtra("cardBagId", cardBagId);
        intent.putExtra("title", name);
        intent.putExtra("imageUrl", imageUrl);
        startActivity(intent);
    }

    private void initData() {
        cardId = getIntent().getIntExtra("id", 0);
        name = getIntent().getStringExtra("name");
        imageUrl = getIntent().getStringExtra("imageUrl");
        date = getIntent().getStringExtra("date");
        author = getIntent().getStringExtra("author");
    }

    private void initView() {
        TextPaint paint = mTvRejectDetailsTitle.getPaint();
        paint.setFakeBoldText(true);
        if (!StringUtils.isEmpty(name))
            mTvRejectDetailsTitle.setText(name);
        if (!StringUtils.isEmpty(author) && !StringUtils.isEmpty(date))
            mTvRejectDetailsInfo.setText(getString(R.string.tv_card_info, date, author));
        if (!StringUtils.isEmpty(imageUrl))
            ImageLoader.load(mActivity, imageUrl, R.drawable.default_card, mIvRejectDetails);

        //获取WebView，并将WebView高度设为WRAP_CONTENT
        mWebView = WebViewUtils.getWebView(this);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        mWebView.setLayoutParams(params);

        mWebView.setWebViewClient(new WebViewClient() {

            boolean isError = false;

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                return true;
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                if (isError) return;
                loadService.showSuccess();
                loadService = null;
                mTvRejectReedit.setVisibility(View.VISIBLE);
            }

            @Override
            public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
                super.onReceivedError(view, request, error);
                isError = true;
                loadService.showCallback(CardDetailsNetworkErrorCallback.class);
            }
        });
        mFlRejectDetails.addView(mWebView);
        boolean isNight = SharedPreferencesUtil.getBoolean(SVTSConstants.isNight, false);
        int fontSize = SharedPreferencesUtil.getInt(SVTSConstants.textSizeValue, 1);
        if (isNight) {
            mUrl = APIService.API_SERVER_URL + getString(R.string.card_details_dark_url) + cardId+"?fontSize="+fontSize;
        } else {
            mUrl = APIService.API_SERVER_URL + getString(R.string.card_details_light_url) + cardId+"?fontSize="+fontSize;

        }
        mWebView.loadUrl(mUrl);

        initLoadSir();
    }

    private void initLoadSir() {
        LoadSir loadSir = new LoadSir.Builder()
                .addCallback(new CardDetailsLoadingCallback())
                .addCallback(new CardDetailsNetworkErrorCallback())
                .setDefaultCallback(CardDetailsLoadingCallback.class)
                .build();
        loadService = loadSir.register(mWebView, this);
    }
}
