package com.zxcx.zhizhe.ui.my.userInfo.userSafety;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.zxcx.zhizhe.R;
import com.zxcx.zhizhe.event.RemoveBindingEvent;
import com.zxcx.zhizhe.mvpBase.MvpActivity;
import com.zxcx.zhizhe.ui.my.userInfo.LogoutDialog;
import com.zxcx.zhizhe.utils.SVTSConstants;
import com.zxcx.zhizhe.utils.SharedPreferencesUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.framework.PlatformDb;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.sina.weibo.SinaWeibo;
import cn.sharesdk.tencent.qq.QQ;
import cn.sharesdk.wechat.friends.Wechat;


public class UserSafetyActivity extends MvpActivity<UserSafetyPresenter> implements UserSafetyContract.View {

    @BindView(R.id.tv_user_safety_wechat)
    TextView mTvUserSafetyWechat;
    @BindView(R.id.tv_user_safety_qq)
    TextView mTvUserSafetyQq;
    @BindView(R.id.tv_user_safety_weibo)
    TextView mTvUserSafetyWeibo;

    private boolean isBindingWechat;
    private boolean isBindingQQ;
    private boolean isBindingWeibo;
    private int channelType; // 1-QQ 2-WeChat 3-Weibo
    PlatformActionListener mChannelLoginListener = new ChannelLoginListener();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_safety);
        ButterKnife.bind(this);
        EventBus.getDefault().register(this);
        initToolBar("账户安全");

        isBindingWechat = SharedPreferencesUtil.getBoolean(SVTSConstants.isBindingWX,false);
        isBindingQQ = SharedPreferencesUtil.getBoolean(SVTSConstants.isBindingQQ,false);
        isBindingWeibo = SharedPreferencesUtil.getBoolean(SVTSConstants.isBindingWB,false);

        updateView();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Override
    protected UserSafetyPresenter createPresenter() {
        return new UserSafetyPresenter(this);
    }

    @Override
    public void postSuccess() {
        switch (channelType){
            case 1:
                isBindingQQ = !isBindingQQ;
                break;
            case 2:
                isBindingWechat = !isBindingWechat;
                break;
            case 3:
                isBindingWeibo = !isBindingWeibo;
                break;
        }
        SharedPreferencesUtil.saveData(SVTSConstants.isBindingWX, isBindingWechat);
        SharedPreferencesUtil.saveData(SVTSConstants.isBindingQQ, isBindingQQ);
        SharedPreferencesUtil.saveData(SVTSConstants.isBindingWB, isBindingWeibo);
        updateView();
    }

    @Override
    public void postFail(String msg) {
        toastShow(msg);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(RemoveBindingEvent event) {
        mPresenter.channelBinding(channelType,2,null);
    }

    @OnClick(R.id.tv_user_info_logout)
    public void onMTvUserInfoLogoutClicked() {
        LogoutDialog dialog = new LogoutDialog();
        dialog.show(getFragmentManager(), "");
    }

    @OnClick(R.id.rl_user_safety_qq)
    public void onMRlUserSafetyQqClicked() {
        channelType = 1;
        if (!isBindingQQ) {
            Platform qq = ShareSDK.getPlatform(QQ.NAME);
            qq.setPlatformActionListener(mChannelLoginListener);
            qq.showUser(null);
        }else {
            RemoveBindingDialog dialog = new RemoveBindingDialog();
            dialog.show(getFragmentManager(),"");
        }
    }

    @OnClick(R.id.rl_user_safety_wechat)
    public void onMRlUserSafetyWechatClicked() {
        channelType = 2;
        if (!isBindingWechat) {
            Platform wechat = ShareSDK.getPlatform(Wechat.NAME);
            wechat.setPlatformActionListener(mChannelLoginListener);
            wechat.showUser(null);
        }else {
            RemoveBindingDialog dialog = new RemoveBindingDialog();
            dialog.show(getFragmentManager(),"");
        }
    }

    @OnClick(R.id.rl_user_safety_weibo)
    public void onMRlUserSafetyWeiboClicked() {
        channelType = 3;
        if (!isBindingWeibo) {
            Platform weibo = ShareSDK.getPlatform(SinaWeibo.NAME);
            weibo.setPlatformActionListener(mChannelLoginListener);
            weibo.showUser(null);
        }else {
            RemoveBindingDialog dialog = new RemoveBindingDialog();
            dialog.show(getFragmentManager(),"");
        }
    }

    @OnClick(R.id.rl_user_safety_change_password)
    public void onMRlUserSafetyChangePasswordClicked() {
        Intent intent = new Intent(this,ChangePasswordActivity.class);
        startActivity(intent);
    }

    private void updateView() {
        mTvUserSafetyWechat.setText(isBindingWechat ? R.string.is_binding : R.string.no_binding);
        mTvUserSafetyQq.setText(isBindingQQ ? R.string.is_binding : R.string.no_binding);
        mTvUserSafetyWeibo.setText(isBindingWeibo ? R.string.is_binding : R.string.no_binding);
    }

    class ChannelLoginListener implements PlatformActionListener {

        @Override
        public void onComplete(Platform platform, int i, HashMap<String, Object> hashMap) {
            if (i == Platform.ACTION_USER_INFOR) {
                PlatformDb platDB = platform.getDb();//获取数平台数据DB
                //通过DB获取各种数据
                String userId = platDB.getUserId();
                mPresenter.channelBinding(channelType,1,userId);
            }
        }

        @Override
        public void onError(Platform platform, int i, Throwable throwable) {
            throwable.printStackTrace();
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    toastShow("授权失败");
                }
            });
        }

        @Override
        public void onCancel(Platform platform, int i) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    toastShow("授权取消");
                }
            });
        }
    }
}
