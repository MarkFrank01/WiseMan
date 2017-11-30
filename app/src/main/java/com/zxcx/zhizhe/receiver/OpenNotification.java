package com.zxcx.zhizhe.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.zxcx.zhizhe.ui.MainActivity;
import com.zxcx.zhizhe.ui.card.card.newCardDetails.CardDetailsActivity;
import com.zxcx.zhizhe.ui.card.cardBag.CardBagActivity;
import com.zxcx.zhizhe.ui.welcome.WebViewActivity;
import com.zxcx.zhizhe.utils.Constants;
import com.zxcx.zhizhe.utils.StringUtils;
import com.zxcx.zhizhe.utils.SystemUtils;

import cn.jpush.android.api.JPushInterface;

/**
 * Created by anm on 2017/8/8.
 */

public class OpenNotification extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {

        Bundle bundle = intent.getExtras();
        String extra = bundle.getString(JPushInterface.EXTRA_EXTRA);
        if (!StringUtils.isEmpty(extra)) {
            JSONObject json = JSON.parseObject(extra);
            String type = json.getString("type");
            int id = json.getIntValue("id");
            String name = json.getString("name");
            String url = json.getString("url");
            gotoActivity(context, type, id, name, url);
        }
        gotoActivity(context, null,0,null,null);
    }

    private void gotoActivity(Context context, String type, int id, String name, String url) {
        //判断app进程是否存活
        if(SystemUtils.isProessRunning(context, context.getPackageName())){
            //如果存活的话，就直接启动DetailActivity，但要考虑一种情况，就是app的进程虽然仍然在
            //但Task栈已经空了，比如用户点击Back键退出应用，但进程还没有被系统回收，如果直接启动
            //DetailActivity,再按Back键就不会返回MainActivity了。所以在启动
            //DetailActivity前，要先启动MainActivity。
            Intent mainIntent = new Intent(context, MainActivity.class);
            //将MainAtivity的launchMode设置成SingleTask, 或者在下面flag中加上Intent.FLAG_CLEAR_TOP,
            //如果Task栈中有MainActivity的实例，就会把它移到栈顶，把在它之上的Activity都清理出栈，
            //如果Task栈不存在MainActivity实例，则在栈顶创建
            mainIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

            Intent detailIntent = new Intent();
            switch (type){
                case Constants.PUSH_TYPE_CARD_BAG:
                    detailIntent.setClass(context, CardBagActivity.class);
                    break;
                case Constants.PUSH_TYPE_CARD:
                    detailIntent.setClass(context, CardDetailsActivity.class);
                    break;
                case Constants.PUSH_TYPE_AD:
                    detailIntent.setClass(context, WebViewActivity.class);
                    break;
                default:
                    context.startActivity(mainIntent);
                    return;
            }
            detailIntent.putExtra("id", id);
            detailIntent.putExtra("name", name);
            detailIntent.putExtra("url", url);
            context.startActivity(detailIntent);
        }else {
            //如果app进程已经被杀死，先重新启动app，将DetailActivity的启动参数传入Intent中，参数经过
            //SplashActivity传入MainActivity，此时app的初始化已经完成，在MainActivity中就可以根据传入
            // 参数跳转到DetailActivity中去了
            Intent launchIntent = context.getPackageManager().
                    getLaunchIntentForPackage(context.getPackageName());
            launchIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK );
            Bundle args = new Bundle();
            args.putString("type", type);
            args.putInt("id", id);
            args.putString("url", url);
            launchIntent.putExtra("push", args);
            context.startActivity(launchIntent);
        }
    }
}
