package com.zxcx.shitang.ui.card.card.dialog;

import android.app.DialogFragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

import com.zxcx.shitang.R;
import com.zxcx.shitang.utils.ScreenUtils;

import java.util.HashMap;

import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.onekeyshare.OnekeyShare;
import cn.sharesdk.sina.weibo.SinaWeibo;
import cn.sharesdk.tencent.qq.QQ;
import cn.sharesdk.tencent.qzone.QZone;
import cn.sharesdk.wechat.friends.Wechat;
import cn.sharesdk.wechat.moments.WechatMoments;

/**
 * Created by anm on 2017/5/27.
 */

public class ShareCardDialog extends DialogFragment {


    private Unbinder mUnbinder;
    private String mImagePath;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_share_channel, container);
        mUnbinder = ButterKnife.bind(this, view);
        mImagePath = getArguments().getString("imagePath");
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();

        Window window = getDialog().getWindow();
        window.setBackgroundDrawableResource(R.color.translate);
        window.getDecorView().setPadding(ScreenUtils.dip2px(12), 0, ScreenUtils.dip2px(12), ScreenUtils.dip2px(10));
        WindowManager.LayoutParams lp = getDialog().getWindow().getAttributes();
        lp.gravity = Gravity.BOTTOM;
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        window.setAttributes(lp);
    }

    @Override
    public void onStop() {
        super.onStop();
        mUnbinder.unbind();
    }

    @OnClick({R.id.ll_share_wechat, R.id.ll_share_moments, R.id.ll_share_qq, R.id.ll_share_qzone, R.id.ll_share_weibo, R.id.ll_share_save, R.id.ll_share_more, R.id.tv_dialog_cancel})
    public void onViewClicked(View view) {
        Platform plat;
        switch (view.getId()) {
            case R.id.ll_share_wechat:
                plat = ShareSDK.getPlatform(Wechat.NAME);
                showShare(plat.getName());
                break;
            case R.id.ll_share_moments:
                plat = ShareSDK.getPlatform(WechatMoments.NAME);
                showShare(plat.getName());
                break;
            case R.id.ll_share_qq:
                plat = ShareSDK.getPlatform(QQ.NAME);
                showShare(plat.getName());
                break;
            case R.id.ll_share_qzone:
                plat = ShareSDK.getPlatform(QZone.NAME);
                showShare(plat.getName());
                break;
            case R.id.ll_share_weibo:
                plat = ShareSDK.getPlatform(SinaWeibo.NAME);
                showShare(plat.getName());
                break;
            case R.id.ll_share_save:
                break;
            case R.id.ll_share_more:
                break;
            case R.id.tv_dialog_cancel:
                dismiss();
                break;
        }
    }

    private void showShare(String platform) {
        final OnekeyShare oks = new OnekeyShare();
        //指定分享的平台，如果为空，还是会调用九宫格的平台列表界面
        if (platform != null) {
            oks.setPlatform(platform);
        }
        //关闭sso授权
        oks.disableSSOWhenAuthorize();
        // title标题，印象笔记、邮箱、信息、微信、人人网和QQ空间使用
//        oks.setTitle("标题");
        // titleUrl是标题的网络链接，仅在Linked-in,QQ和QQ空间使用
//        oks.setTitleUrl("http://sharesdk.cn");
        // text是分享文本，所有平台都需要这个字段
//        oks.setText("我是分享文本");
        //分享网络图片，新浪微博分享网络图片需要通过审核后申请高级写入接口，否则请注释掉测试新浪微博
//        oks.setImageUrl("http://f1.sharesdk.cn/imgs/2014/02/26/owWpLZo_638x960.jpg");
        // imagePath是图片的本地路径，Linked-In以外的平台都支持此参数
        oks.setImagePath("/sdcard/test.jpg");//确保SDcard下面存在此张图片
        // url仅在微信（包括好友和朋友圈）中使用
//        oks.setUrl("http://sharesdk.cn");
        // comment是我对这条分享的评论，仅在人人网和QQ空间使用
//        oks.setComment("我是测试评论文本");
        // site是分享此内容的网站名称，仅在QQ空间使用
//        oks.setSite("ShareSDK");
        // siteUrl是分享此内容的网站地址，仅在QQ空间使用
//        oks.setSiteUrl("http://sharesdk.cn");

        //回调
        oks.setCallback(new PlatformActionListener() {
            @Override
            public void onComplete(Platform platform, int i, HashMap<String, Object> hashMap) {

            }

            @Override
            public void onError(Platform platform, int i, Throwable throwable) {

            }

            @Override
            public void onCancel(Platform platform, int i) {

            }
        });

        //启动分享
        oks.show(getActivity());
    }
}
