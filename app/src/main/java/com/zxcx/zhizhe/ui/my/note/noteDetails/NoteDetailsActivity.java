package com.zxcx.zhizhe.ui.my.note.noteDetails;

import android.graphics.drawable.Drawable;
import android.os.Build;
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
import android.widget.ScrollView;
import android.widget.TextView;

import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.zxcx.zhizhe.R;
import com.zxcx.zhizhe.mvpBase.RefreshMvpActivity;
import com.zxcx.zhizhe.retrofit.APIService;
import com.zxcx.zhizhe.ui.card.card.cardDetails.ShareDialog;
import com.zxcx.zhizhe.utils.GlideApp;
import com.zxcx.zhizhe.utils.SVTSConstants;
import com.zxcx.zhizhe.utils.ScreenUtils;
import com.zxcx.zhizhe.utils.SharedPreferencesUtil;
import com.zxcx.zhizhe.utils.StringUtils;
import com.zxcx.zhizhe.utils.WebViewUtils;

import org.greenrobot.eventbus.EventBus;

import butterknife.BindView;
import butterknife.ButterKnife;
import in.srain.cube.views.ptr.PtrFrameLayout;

public class NoteDetailsActivity extends RefreshMvpActivity<NoteDetailsPresenter> implements NoteDetailsContract.View {

    @BindView(R.id.iv_note_details_back)
    ImageView mIvNoteDetailsBack;
    @BindView(R.id.tv_note_details_title)
    TextView mTvNoteDetailsTitle;
    @BindView(R.id.iv_note_details_share)
    ImageView mTvNoteDetailsShare;
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
    @BindView(R.id.sv_note_details)
    ScrollView mSvNoteDetails;

    private WebView mWebView;
    private int noteId;
    private String name;
    private String imageUrl;
    private String date;
    private String mUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_note_details);
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
        EventBus.getDefault().register(this);

        initData();
        initView();

        mPresenter.getNoteDetails(noteId);

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
    public boolean checkCanDoRefresh(PtrFrameLayout frame, View content, View header) {
        return !mSvNoteDetails.canScrollVertically(-1);
    }

    @Override
    public void onRefreshBegin(PtrFrameLayout frame) {
        mPresenter.getNoteDetails(noteId);
        mWebView.reload();
    }

    @Override
    public void onReload(View v) {
        mPresenter.getNoteDetails(noteId);
    }

    @Override
    public void getDataSuccess(NoteDetailsBean bean) {
        mRefreshLayout.refreshComplete();
    }

    @Override
    public void toastFail(String msg) {
        super.toastFail(msg);
    }

    private void initData() {
        noteId = getIntent().getIntExtra("id", 0);
        name = getIntent().getStringExtra("name");
        imageUrl = getIntent().getStringExtra("imageUrl");
        date = getIntent().getStringExtra("date");
    }

    private void initView() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            // 延迟共享动画的执行
            //postponeEnterTransition();
        }
        TextPaint paint = mTvNoteDetailsTitle.getPaint();
        paint.setFakeBoldText(true);
        mTvNoteDetailsTitle.setText(name);
        mTvNoteDetailsInfo.setText(getString(R.string.tv_note_info, date));

        GlideApp
                .with(this)
                .load(imageUrl)
                .placeholder(R.drawable.default_card)
                .error(R.drawable.default_card)
                .into(new SimpleTarget<Drawable>() {
                    @Override
                    public void onResourceReady(Drawable resource, Transition<? super Drawable> transition) {
                        mIvNoteDetails.setImageDrawable(resource);
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

    private void gotoShare(String url, String content) {
        ShareDialog shareDialog = new ShareDialog();
        Bundle bundle = new Bundle();
        bundle.putString("name",name);
        if (!StringUtils.isEmpty(content)) {
            bundle.putString("content", content);
        } else {
            bundle.putString("url",url);
        }
        bundle.putString("imageUrl",imageUrl);
        bundle.putString("date",date);
        shareDialog.setArguments(bundle);
        shareDialog.show(getFragmentManager(),"");
    }
}
