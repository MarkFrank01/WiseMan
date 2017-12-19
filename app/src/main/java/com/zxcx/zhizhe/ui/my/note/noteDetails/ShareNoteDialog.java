package com.zxcx.zhizhe.ui.my.note.noteDetails;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextPaint;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.WebView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.zxcx.zhizhe.R;
import com.zxcx.zhizhe.mvpBase.BaseDialog;
import com.zxcx.zhizhe.utils.FileUtil;
import com.zxcx.zhizhe.utils.GlideApp;
import com.zxcx.zhizhe.utils.ScreenUtils;
import com.zxcx.zhizhe.utils.WebViewUtils;

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
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subscribers.DisposableSubscriber;

public class ShareNoteDialog extends BaseDialog {


    @BindView(R.id.iv_dialog_share)
    ImageView mIvDialogShare;
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
    private WebView mWebView;
    private String name;
    private String imageUrl;
    private String url;
    private String date;
    private Unbinder unbinder;
    private Platform plat;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        View view = inflater.inflate(R.layout.dialog_share_note, container);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();

        Window window = getDialog().getWindow();
        window.setBackgroundDrawableResource(R.color.translate);
        window.getDecorView().setPadding(ScreenUtils.dip2px(10), ScreenUtils.dip2px(10),
                ScreenUtils.dip2px(10), ScreenUtils.dip2px(10));
        WindowManager.LayoutParams lp = window.getAttributes();
        lp.gravity = Gravity.CENTER;
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.MATCH_PARENT;
        window.setAttributes(lp);

        ViewGroup.LayoutParams para = mIvDialogShare.getLayoutParams();
        int screenWidth = ScreenUtils.getScreenWidth(); //屏幕宽度
        para.height = (screenWidth * 9 / 16);
        mIvDialogShare.setLayoutParams(para);
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
        imageUrl = bundle.getString("imageUrl");
        date = bundle.getString("date");
    }

    private void initView() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            // 延迟共享动画的执行
            //postponeEnterTransition();
        }
        TextPaint paint = mTvDialogShareTitle.getPaint();
        paint.setFakeBoldText(true);
        mTvDialogShareTitle.setText(name);
        mTvDialogShareInfo.setText(getString(R.string.tv_note_info, date));

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
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        mWebView.setLayoutParams(params);
        mFlDialogShare.addView(mWebView);
        mWebView.loadUrl(url);
    }

    @OnClick(R.id.iv_dialog_share_wechat)
    public void onMIvDialogShareWechatClicked() {
        plat = ShareSDK.getPlatform(Wechat.NAME);
        showShare(plat.getName());
    }

    @OnClick(R.id.iv_dialog_share_moments)
    public void onMIvDialogShareMomentsClicked() {
        plat = ShareSDK.getPlatform(WechatMoments.NAME);
        showShare(plat.getName());
    }

    @OnClick(R.id.iv_dialog_share_qq)
    public void onMIvDialogShareQqClicked() {
        plat = ShareSDK.getPlatform(QQ.NAME);
        showShare(plat.getName());
    }

    @OnClick(R.id.iv_dialog_share_weibo)
    public void onMIvDialogShareWeiboClicked() {
        plat = ShareSDK.getPlatform(SinaWeibo.NAME);
        showShare(plat.getName());
    }

    @OnClick(R.id.iv_dialog_share_back)
    public void onMIvDialogShareBackClicked() {
        dismiss();
    }

    private void showShare(String platform) {
        Bitmap bitmap = ScreenUtils.getBitmapByView(mSvDialogShare);
        String fileName = FileUtil.getRandomImageName();

        OnekeyShare oks = new OnekeyShare();
        //指定分享的平台，如果为空，还是会调用九宫格的平台列表界面
        if (platform != null) {
            oks.setPlatform(platform);
        }

        //回调
        oks.setCallback(new PlatformActionListener() {
            @Override
            public void onComplete(Platform platform, int i, HashMap<String, Object> hashMap) {
                toastShow("分享成功");
            }

            @Override
            public void onError(Platform platform, int i, Throwable throwable) {
                toastShow(throwable.getMessage());
            }

            @Override
            public void onCancel(Platform platform, int i) {
                toastShow("分享取消");
            }
        });

        mDisposable = Flowable.just(bitmap)
                .subscribeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(subscription -> showLoading())
                .observeOn(Schedulers.io())
                .map(bm ->{
                    FileUtil.saveBitmapToSDCard(bitmap, FileUtil.PATH_BASE, fileName);
                    return FileUtil.PATH_BASE + fileName;
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableSubscriber<String>(){

                    @Override
                    public void onNext(String s) {
                        // imagePath是图片的本地路径，Linked-In以外的平台都支持此参数
                        oks.setImagePath(s);//确保SDcard下面存在此张图片
                        //启动分享
                        hideLoading();
                        oks.show(getActivity());
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
