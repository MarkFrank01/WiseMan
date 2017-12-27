package com.zxcx.zhizhe.ui.my.creation.creationDetails;

import android.os.Bundle;
import android.text.TextPaint;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zxcx.zhizhe.R;
import com.zxcx.zhizhe.mvpBase.MvpActivity;
import com.zxcx.zhizhe.retrofit.APIService;
import com.zxcx.zhizhe.utils.DateTimeUtils;
import com.zxcx.zhizhe.utils.ImageLoader;
import com.zxcx.zhizhe.utils.SVTSConstants;
import com.zxcx.zhizhe.utils.ScreenUtils;
import com.zxcx.zhizhe.utils.SharedPreferencesUtil;
import com.zxcx.zhizhe.utils.StringUtils;
import com.zxcx.zhizhe.utils.WebViewUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ReviewDetailsActivity extends MvpActivity<RejectDetailsPresenter> implements RejectDetailsContract.View {

    @BindView(R.id.tv_reject_details_title)
    TextView mTvRejectDetailsTitle;
    @BindView(R.id.fl_reject_details)
    FrameLayout mFlRejectDetails;
    @BindView(R.id.iv_reject_details)
    ImageView mIvRejectDetails;
    @BindView(R.id.tv_reject_details_info)
    TextView mTvRejectDetailsInfo;
    @BindView(R.id.tv_reject_details_reject_bag)
    TextView mTvRejectDetailsRejectBag;

    private WebView mWebView;
    private int cardId;
    private int cardBagId;
    private String name;
    private String author;
    private String imageUrl;
    private String date;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_review_card_details);
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);

        initData();
        initView();

        mPresenter.getReviewDetails(cardId);

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
        mTvRejectDetailsTitle.setText(name);
        mTvRejectDetailsInfo.setText(getString(R.string.tv_card_info, date, author));
        mTvRejectDetailsRejectBag.setText(bean.getCardBagName());
        ImageLoader.load(mActivity, imageUrl, R.drawable.default_card, mIvRejectDetails);
    }

    @Override
    public void toastFail(String msg) {
        super.toastFail(msg);
    }

    @OnClick(R.id.iv_reject_details_back)
    public void onMIvRejectDetailsBackClicked() {
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
        mFlRejectDetails.addView(mWebView);
        boolean isNight = SharedPreferencesUtil.getBoolean(SVTSConstants.isNight, false);
        String url;
        if (isNight) {
            url = APIService.API_SERVER_URL + "/view/articleDark/" + cardId;
        } else {
            url = APIService.API_SERVER_URL + "/view/articleLight/" + cardId;
//            mUrl = "http://192.168.1.149/articleView/192";

        }
        mWebView.loadUrl(url);
    }
}
