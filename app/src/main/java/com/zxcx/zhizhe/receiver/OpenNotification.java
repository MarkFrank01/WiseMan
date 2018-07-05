package com.zxcx.zhizhe.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import com.zxcx.zhizhe.ui.my.message.MessageActivity;

/**
 * Created by anm on 2017/8/8.
 */

public class OpenNotification extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {

        /*Bundle bundle = intent.getExtras();
        String extra = bundle.getString(JPushInterface.EXTRA_EXTRA);
        if (!StringUtils.isEmpty(extra)) {
            JSONObject json = JSON.parseObject(extra);
            String type = json.getString("type");
            int id = json.getIntValue("id");
            String name = json.getString("name");
            String url = json.getString("url");
            gotoActivity(context, type, id, name, url);
        }
        gotoActivity(context, null,0,null,null);*/
		Intent messageIntent = new Intent(context, MessageActivity.class);
		messageIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		context.startActivity(messageIntent);
	}
}
