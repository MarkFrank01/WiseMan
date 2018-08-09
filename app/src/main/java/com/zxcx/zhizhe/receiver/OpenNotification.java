package com.zxcx.zhizhe.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import com.zxcx.zhizhe.ui.my.message.MessageActivity;

/**
 * Created by anm on 2017/8/8.
 * 用户点击推送通知后接收到的广播
 */

public class OpenNotification extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		Intent messageIntent = new Intent(context, MessageActivity.class);
		messageIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		context.startActivity(messageIntent);
	}
}
