package com.zxcx.zhizhe.ui.card.cardDetails;

import android.app.Dialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
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
import com.zxcx.zhizhe.R;
import com.zxcx.zhizhe.mvpBase.BaseDialog;
import com.zxcx.zhizhe.utils.ScreenUtils;
import java.util.HashMap;

/**
 * Created by anm on 2017/5/27.
 */

public class ShareCardDialog extends BaseDialog {
	
	@BindView(R.id.tv_dialog_cancel)
	TextView mTvDialogCancel;
	private Unbinder mUnbinder;
	private String url;
	private String title;
	private String text;
	private String imagePath;
	
	@Nullable
	@Override
	public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
		Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.dialog_share_channel, container);
		mUnbinder = ButterKnife.bind(this, view);
		imagePath = getArguments().getString("imagePath");
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
		R.id.ll_share_weibo, R.id.ll_share_copy, R.id.tv_dialog_cancel})
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
		oks.setImagePath(imagePath);//确保SDcard下面存在此张图片
		//启动分享
		hideLoading();
		oks.show(getActivity());
		dismiss();
	}
}
