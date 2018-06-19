package com.zxcx.zhizhe.ui.article.articleDetails;

import android.Manifest;
import android.app.Dialog;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.text.TextPaint;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
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
import com.tbruyelle.rxpermissions2.RxPermissions;
import com.zxcx.zhizhe.R;
import com.zxcx.zhizhe.mvpBase.BaseDialog;
import com.zxcx.zhizhe.utils.FileUtil;
import com.zxcx.zhizhe.utils.GlideApp;
import com.zxcx.zhizhe.utils.SVTSConstants;
import com.zxcx.zhizhe.utils.ScreenUtils;
import com.zxcx.zhizhe.utils.SharedPreferencesUtil;
import com.zxcx.zhizhe.utils.StringUtils;
import com.zxcx.zhizhe.utils.WebViewUtils;
import com.zxcx.zhizhe.widget.CardImageView;
import com.zxcx.zhizhe.widget.PermissionDialog;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.onekeyshare.OnekeyShare;
import cn.sharesdk.sina.weibo.SinaWeibo;
import cn.sharesdk.tencent.qq.QQ;
import cn.sharesdk.wechat.friends.Wechat;
import cn.sharesdk.wechat.moments.WechatMoments;
import io.reactivex.Flowable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subscribers.DisposableSubscriber;
import top.zibin.luban.Luban;

public class ShareCardDialog extends BaseDialog {


    @BindView(R.id.iv_dialog_share)
    CardImageView mIvDialogShare;
    @BindView(R.id.tv_dialog_share_title)
    TextView mTvDialogShareTitle;
    @BindView(R.id.tv_dialog_share_info)
    TextView mTvDialogShareInfo;
    @BindView(R.id.tv_dialog_share_card_bag)
    TextView mTvDialogShareCardBag;
    @BindView(R.id.fl_dialog_share)
    FrameLayout mFlDialogShare;
    @BindView(R.id.sv_dialog_share)
    ScrollView mSvDialogShare;
    @BindView(R.id.iv_dialog_share_qr)
    ImageView mIvDialogShareQR;
    private WebView mWebView;
    private String name;
    private String cardBagName;
    private int cardBagId;
    private String imageUrl;
    private String url;
    private String content;
    private String date;
    private String author;
    private Unbinder unbinder;
    private boolean isReady = false;
    private String SETUP_HTML;
    private Platform plat;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        View view = inflater.inflate(R.layout.dialog_share_card, container);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        Dialog dialog = getDialog();
        if (dialog != null) {
            Window window = getDialog().getWindow();
            window.setBackgroundDrawableResource(R.color.translate);
            window.getDecorView().setPadding(ScreenUtils.dip2px(10), ScreenUtils.dip2px(10),
                    ScreenUtils.dip2px(10), ScreenUtils.dip2px(10));
            WindowManager.LayoutParams lp = window.getAttributes();
            lp.gravity = Gravity.CENTER;
            lp.width = WindowManager.LayoutParams.MATCH_PARENT;
            lp.height = WindowManager.LayoutParams.MATCH_PARENT;
            window.setAttributes(lp);
        }
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initData();
        initView();
    }

    @Override
    public void onDestroy() {
        if (mWebView != null) {
            mWebView.loadDataWithBaseURL(null, "", "text/html", "utf-8", null);
            mWebView.clearHistory();

            ((ViewGroup) mWebView.getParent()).removeView(mWebView);
            mWebView.destroy();
            mWebView = null;
        }
        unbinder.unbind();
        super.onDestroy();
    }

    private void initData() {
        Bundle bundle = getArguments();
        name = getArguments().getString("name");
        url = getArguments().getString("url");
        content = getArguments().getString("content");
        imageUrl = bundle.getString("imageUrl");
        date = bundle.getString("date");
        author = bundle.getString("author");
        cardBagName = bundle.getString("cardCategoryName");
        cardBagId = bundle.getInt("cardBagId", 0);

        boolean isNight = SharedPreferencesUtil.getBoolean(SVTSConstants.isNight, false);
        if (isNight) {
            SETUP_HTML = "file:///android_asset/preview_dark.html";
        } else {
            SETUP_HTML = "file:///android_asset/preview_light.html";
        }
    }

    private void initView() {
        TextPaint paint = mTvDialogShareTitle.getPaint();
        paint.setFakeBoldText(true);
        mTvDialogShareTitle.setText(name);
        mTvDialogShareInfo.setText(getString(R.string.tv_card_info, date, author));
        mTvDialogShareCardBag.setText(cardBagName);

        GlideApp
                .with(this)
                .load(imageUrl)
                .placeholder(R.drawable.default_card)
                .error(R.drawable.default_card)
                .into(new SimpleTarget<Drawable>() {
                    @Override
                    public void onResourceReady(Drawable resource, Transition<? super Drawable> transition) {
                        mIvDialogShare.setImageDrawable(resource);
                        //图片加载完成的回调中，启动过渡动画
                        //supportStartPostponedEnterTransition();
                    }
                });

        //获取WebView，并将WebView高度设为WRAP_CONTENT
        mWebView = WebViewUtils.getWebView(getActivity());
        mWebView.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.bg_details));
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        mWebView.setLayoutParams(params);

        mWebView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                return true;
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                isReady = url.equalsIgnoreCase(SETUP_HTML);
                mIvDialogShareQR.setVisibility(View.VISIBLE);
            }
        });
        mFlDialogShare.addView(mWebView);
        if (!StringUtils.isEmpty(content)) {
            mWebView.loadUrl(SETUP_HTML);
            if (!StringUtils.isEmpty(content)) {
                try {
                    exec("javascript:RE.setBody('" + URLEncoder.encode(content, "UTF-8") + "');");
                } catch (UnsupportedEncodingException e) {
                    // No handling
                }
            }
        } else {
            mWebView.loadUrl(url);
        }
    }

    @OnClick(R.id.iv_dialog_share_wechat)
    public void onMIvDialogShareWechatClicked() {
        plat = ShareSDK.getPlatform(Wechat.NAME);
        checkPermission(plat.getName());
    }

    @OnClick(R.id.iv_dialog_share_moments)
    public void onMIvDialogShareMomentsClicked() {
        plat = ShareSDK.getPlatform(WechatMoments.NAME);
        checkPermission(plat.getName());
    }

    @OnClick(R.id.iv_dialog_share_qq)
    public void onMIvDialogShareQqClicked() {
        plat = ShareSDK.getPlatform(QQ.NAME);
        checkPermission(plat.getName());
    }

    @OnClick(R.id.iv_dialog_share_weibo)
    public void onMIvDialogShareWeiboClicked() {
        plat = ShareSDK.getPlatform(SinaWeibo.NAME);
        checkPermission(plat.getName());
    }

    @OnClick(R.id.iv_dialog_share_back)
    public void onMIvDialogShareBackClicked() {
        dismiss();
    }

    protected void exec(final String trigger) {
        if (isReady) {
            mWebView.evaluateJavascript(trigger, null);
        } else {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    exec(trigger);
                }
            }, 100);
        }
    }

    private void checkPermission(String platform){
        RxPermissions rxPermissions = new RxPermissions(getActivity());
        Disposable subscribe = rxPermissions
                .requestEach(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .subscribe(permission -> {
                    if (permission.granted) {
                        // `permission.name` is granted !
                        showShare(platform);
                    } else if (permission.shouldShowRequestPermissionRationale) {
                        // Denied permission without ask never again
                        toastShow("权限已被拒绝！无法进行操作");
                    } else {
                        // Denied permission with ask never again
                        // Need to go to the settings
                        PermissionDialog permissionDialog = new PermissionDialog();
                        permissionDialog.show(getFragmentManager(), "");
                    }
                });
    }

    private void showShare(String platform) {

        OnekeyShare oks = new OnekeyShare();
        //指定分享的平台，如果为空，还是会调用九宫格的平台列表界面
        if (platform != null) {
            oks.setPlatform(platform);
        }

        oks.setCallback(new PlatformActionListener() {
            @Override
            public void onComplete(Platform platform, int i, HashMap<String, Object> hashMap) {
                //保持为空，覆盖掉提示
            }

            @Override
            public void onError(Platform platform, int i, Throwable throwable) {

            }

            @Override
            public void onCancel(Platform platform, int i) {

            }
        });

        mDisposable = Flowable.just(mSvDialogShare)
                .subscribeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(subscription -> showLoading())
                .observeOn(Schedulers.io())
                .map(view -> {
                    Bitmap bitmap = ScreenUtils.getBitmapByView(view);
                    String fileName = FileUtil.getRandomImageName();
                    FileUtil.saveBitmapToSDCard(bitmap, FileUtil.PATH_BASE, fileName);
                    String path = FileUtil.PATH_BASE + fileName;
                    return Luban.with(getActivity())
                            .load(new File(path))                     //传入要压缩的图片
                            .get()
                            .getPath();//启动压缩
//                    return path;
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableSubscriber<String>() {

                    @Override
                    public void onNext(String s) {
                        // imagePath是图片的本地路径，Linked-In以外的平台都支持此参数
                        oks.setImagePath(s);//确保SDcard下面存在此张图片
                        //启动分享
                        hideLoading();
                        oks.show(getActivity());
                        dismiss();
                    }

                    @Override
                    public void onError(Throwable t) {
                        hideLoading();
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }
}
