package com.zxcx.zhizhe.ui.card.share;

import android.app.Dialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextPaint;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.zxcx.zhizhe.R;
import com.zxcx.zhizhe.mvpBase.BaseDialog;
import com.zxcx.zhizhe.utils.LogCat;
import com.zxcx.zhizhe.utils.ScreenUtils;

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
import cn.sharesdk.tencent.qzone.QZone;
import cn.sharesdk.wechat.friends.Wechat;
import cn.sharesdk.wechat.moments.WechatMoments;

/**
 * Created by anm on 2017/5/27.
 * 常规分享弹窗
 */

public class ShareDialog extends BaseDialog {

	@BindView(R.id.tv_dialog_cancel)
	TextView mTvDialogCancel;
	private Unbinder mUnbinder;
	private String url;
	private String title;
	private String text;
	private String imageUrl;
	private String comment;

	@Nullable
	@Override
	public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
		Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.dialog_share_channel, container);
		mUnbinder = ButterKnife.bind(this, view);
		url = getArguments().getString("url");
		title = getArguments().getString("title");
		text = getArguments().getString("text");
		imageUrl = getArguments().getString("imageUrl");
//		comment = getArguments().getString("comment");
		return view;
	}

	@Override
	public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		TextPaint tp = mTvDialogCancel.getPaint();
		tp.setFakeBoldText(true);
	}

	@Override
	public void onStart() {
		super.onStart();
		Dialog dialog = getDialog();
		if (dialog != null) {
			Window window = getDialog().getWindow();
			window.setBackgroundDrawableResource(R.color.translate);
			window.getDecorView()
				.setPadding(ScreenUtils.dip2px(12), 0, ScreenUtils.dip2px(12),
					ScreenUtils.dip2px(10));
			WindowManager.LayoutParams lp = window.getAttributes();
			lp.gravity = Gravity.BOTTOM;
			lp.width = WindowManager.LayoutParams.MATCH_PARENT;
			lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
			window.setAttributes(lp);
		}
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		mUnbinder.unbind();
	}

	@OnClick({R.id.ll_share_wechat, R.id.ll_share_moments, R.id.ll_share_qq, R.id.ll_share_qzone,
		R.id.ll_share_weibo, R.id.ll_share_copy, R.id.ll_share_more, R.id.tv_dialog_cancel})
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
			case R.id.ll_share_copy:
				copy();
				this.dismiss();
				break;
			case R.id.ll_share_more:
				Uri uri = Uri.parse(url);
				startActivity(new Intent(Intent.ACTION_VIEW, uri));
				break;
			case R.id.tv_dialog_cancel:
				this.dismiss();
				break;
		}
	}

	private void copy() {
		toastShow("复制成功");
		ClipboardManager clipboardManager = (ClipboardManager) getActivity()
			.getSystemService(Context.CLIPBOARD_SERVICE);
		ClipData clip = ClipData.newPlainText(title, title + "\n" + url);
		if (clipboardManager != null) {
			clipboardManager.setPrimaryClip(clip);
		}
	}

	private void showShare(String platform) {
		OnekeyShare oks = new OnekeyShare();
		//指定分享的平台，如果为空，还是会调用九宫格的平台列表界面
		if (platform != null) {
			oks.setPlatform(platform);
		}
		//关闭sso授权
//        oks.disableSSOWhenAuthorize();
		// title标题，印象笔记、邮箱、信息、微信、人人网和QQ空间使用
		oks.setTitle(title);
		// titleUrl是标题的网络链接，仅在Linked-in,QQ和QQ空间使用
		oks.setTitleUrl(url);
		// text是分享文本，所有平台都需要这个字段
		oks.setText(text);
		//分享网络图片，新浪微博分享网络图片需要通过审核后申请高级写入接口，否则请注释掉测试新浪微博
		oks.setImageUrl(imageUrl);
		// imagePath是图片的本地路径，Linked-In以外的平台都支持此参数
//        oks.setImagePath(url);//确保SDcard下面存在此张图片
		// url仅在微信（包括好友和朋友圈）中使用
		oks.setUrl(url);
		// comment是我对这条分享的评论，仅在人人网和QQ空间使用
//        oks.setComment(comment);
		// site是分享此内容的网站名称，仅在QQ空间使用
//        oks.setSite("ShareSDK");
		// siteUrl是分享此内容的网站地址，仅在QQ空间使用
//        oks.setSiteUrl("http://sharesdk.cn");

        LogCat.e("Text-----"+text);

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
		dismiss();
	}
}
