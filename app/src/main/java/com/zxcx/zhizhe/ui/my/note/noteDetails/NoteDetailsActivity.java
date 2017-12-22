package com.zxcx.zhizhe.ui.my.note.noteDetails;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextPaint;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zxcx.zhizhe.R;
import com.zxcx.zhizhe.event.CommitNoteReviewEvent;
import com.zxcx.zhizhe.mvpBase.MvpActivity;
import com.zxcx.zhizhe.retrofit.APIService;
import com.zxcx.zhizhe.ui.card.card.cardDetails.CardDetailsActivity;
import com.zxcx.zhizhe.ui.my.creation.newCreation.NewCreationTitleActivity;
import com.zxcx.zhizhe.utils.Constants;
import com.zxcx.zhizhe.utils.DateTimeUtils;
import com.zxcx.zhizhe.utils.ImageLoader;
import com.zxcx.zhizhe.utils.SVTSConstants;
import com.zxcx.zhizhe.utils.ScreenUtils;
import com.zxcx.zhizhe.utils.SharedPreferencesUtil;
import com.zxcx.zhizhe.utils.WebViewUtils;

import org.greenrobot.eventbus.EventBus;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class NoteDetailsActivity extends MvpActivity<NoteDetailsPresenter> implements NoteDetailsContract.View {

    @BindView(R.id.tv_note_details_title)
    TextView mTvNoteDetailsTitle;
    @BindView(R.id.fl_note_details)
    FrameLayout mFlNoteDetails;
    @BindView(R.id.iv_note_details)
    ImageView mIvNoteDetails;
    @BindView(R.id.tv_note_details_info)
    TextView mTvNoteDetailsInfo;
    @BindView(R.id.view_line)
    View mViewLine;
    @BindView(R.id.ll_note_details_bottom)
    LinearLayout mLlNoteDetailsBottom;
    @BindView(R.id.iv_note_details_edit)
    ImageView mIvNoteDetailsEdit;
    @BindView(R.id.iv_note_details_source)
    ImageView mIvNoteDetailsSource;

    private WebView mWebView;
    private int noteId;
    private int cardBagId;
    private int withCardId;
    private String name;
    private String imageUrl;
    private String date;
    private String mUrl;
    private int noteType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_note_details);
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
        EventBus.getDefault().register(this);

        initData();
        initView();

        mPresenter.getNoteDetails(noteId, noteType);

        ViewGroup.LayoutParams para = mIvNoteDetails.getLayoutParams();
        int screenWidth = ScreenUtils.getScreenWidth(); //屏幕宽度
        para.height = (screenWidth * 9 / 16);
        mIvNoteDetails.setLayoutParams(para);
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
        EventBus.getDefault().unregister(this);
    }

    @Override
    protected NoteDetailsPresenter createPresenter() {
        return new NoteDetailsPresenter(this);
    }

    @Override
    public void getDataSuccess(NoteDetailsBean bean) {
        imageUrl = bean.getImageUrl();
        date = DateTimeUtils.getDateString(bean.getDate());
        mTvNoteDetailsInfo.setText(getString(R.string.tv_note_info, date));
        cardBagId = bean.getCardBagId();
        withCardId = bean.getWithCardId();
        ImageLoader.load(mActivity, imageUrl, R.drawable.default_card, mIvNoteDetails);
    }

    @Override
    public void postSuccess() {
        EventBus.getDefault().post(new CommitNoteReviewEvent(noteId));
        onBackPressed();
        toastShow("提交成功");
    }

    @Override
    public void postFail(String msg) {
        toastShow(msg);
    }

    @OnClick(R.id.iv_note_details_edit)
    public void onMIvNoteDetailsEditClicked() {
        Intent intent = new Intent(mActivity, NewCreationTitleActivity.class);
        intent.putExtra("cardId", noteId);
        intent.putExtra("title", name);
        intent.putExtra("imageUrl", imageUrl);
        intent.putExtra("cardBagId", cardBagId);
        startActivity(intent);
    }

    @OnClick(R.id.iv_note_details_source)
    public void onMIvNoteDetailsSourceClicked() {
        if (noteType == Constants.NOTE_TYPE_CARD) {
            Intent intent = new Intent(mActivity, CardDetailsActivity.class);
            intent.putExtra("id", withCardId);
            startActivity(intent);
        } else if (noteType == Constants.NOTE_TYPE_FREEDOM) {
            mPresenter.submitReview(noteId);
        }

    }

    @OnClick(R.id.iv_note_details_share)
    public void onMIvNoteDetailsShareClicked() {
        gotoShare(mUrl);
    }

    @OnClick(R.id.iv_note_details_back)
    public void onMIvNoteDetailsBackClicked() {
        onBackPressed();
    }

    private void initData() {
        noteId = getIntent().getIntExtra("id", 0);
        noteType = getIntent().getIntExtra("noteType", 0);
        name = getIntent().getStringExtra("name");
    }

    private void initView() {
        TextPaint paint = mTvNoteDetailsTitle.getPaint();
        paint.setFakeBoldText(true);
        mTvNoteDetailsTitle.setText(name);
        if (noteType == Constants.NOTE_TYPE_CARD) {
            mIvNoteDetailsEdit.setVisibility(View.GONE);
            mIvNoteDetailsSource.setImageResource(R.drawable.iv_note_details_source);
        } else if (noteType == Constants.NOTE_TYPE_FREEDOM) {
            mIvNoteDetailsEdit.setVisibility(View.VISIBLE);
            mIvNoteDetailsSource.setImageResource(R.drawable.iv_note_details_commit);
        }

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
                mLlNoteDetailsBottom.setVisibility(View.VISIBLE);
                mViewLine.setVisibility(View.VISIBLE);
            }
        });
        mFlNoteDetails.addView(mWebView);
        boolean isNight = SharedPreferencesUtil.getBoolean(SVTSConstants.isNight, false);
        if (isNight) {
            mUrl = APIService.API_SERVER_URL + "/view/articleDark/" + noteId;
        } else {
            mUrl = APIService.API_SERVER_URL + "/view/articleLight/" + noteId;
//            mUrl = "http://192.168.1.149/articleView/192";

        }
        mWebView.loadUrl(mUrl);
    }

    private void gotoShare(String url) {
        ShareNoteDialog shareDialog = new ShareNoteDialog();
        Bundle bundle = new Bundle();
        bundle.putString("name", name);
        bundle.putString("url", url);
        bundle.putString("imageUrl", imageUrl);
        bundle.putString("date", date);
        shareDialog.setArguments(bundle);
        shareDialog.show(getFragmentManager(), "");
    }
}
