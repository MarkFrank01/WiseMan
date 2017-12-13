package com.zxcx.zhizhe.ui.my.userInfo.userSafety;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import com.zxcx.zhizhe.R;
import com.zxcx.zhizhe.event.RemoveBindingEvent;
import com.zxcx.zhizhe.mvpBase.MvpActivity;
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
import cn.sharesdk.wechat.utils.WechatClientNotExistException;


public class UserSafetyActivity extends MvpActivity<UserSafetyPresenter> implements UserSafetyContract.View {


    @BindView(R.id.ll_user_safety_phone_bind)
    LinearLayout mLlUserSafetyPhoneBind;
    @BindView(R.id.line_weibo_bind)
    View mLineWeiboBind;
    @BindView(R.id.ll_user_safety_wechat_bind)
    LinearLayout mLlUserSafetyWechatBind;
    @BindView(R.id.line_wechat_bind)
    View mLineWechatBind;
    @BindView(R.id.ll_user_safety_qq_bind)
    LinearLayout mLlUserSafetyQqBind;
    @BindView(R.id.line_qq_bind)
    View mLineQqBind;
    @BindView(R.id.ll_user_safety_weibo_bind)
    LinearLayout mLlUserSafetyWeiboBind;
    @BindView(R.id.ll_user_safety_phone_unbind)
    LinearLayout mLlUserSafetyPhoneUnbind;
    @BindView(R.id.line_phone_unbind)
    View mLinePhoneUnbind;
    @BindView(R.id.ll_user_safety_wechat_unbind)
    LinearLayout mLlUserSafetyWechatUnbind;
    @BindView(R.id.line_wechat_unbind)
    View mLineWechatUnbind;
    @BindView(R.id.ll_user_safety_qq_unbind)
    LinearLayout mLlUserSafetyQqUnbind;
    @BindView(R.id.line_qq_unbind)
    View mLineQqUnbind;
    @BindView(R.id.ll_user_safety_weibo_unbind)
    LinearLayout mLlUserSafetyWeiboUnbind;
    private boolean isBindingPhone;
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
        initToolBar("绑定");

        isBindingPhone = SharedPreferencesUtil.getBoolean(SVTSConstants.isBindingPhone, false);
        isBindingWechat = SharedPreferencesUtil.getBoolean(SVTSConstants.isBindingWX, false);
        isBindingQQ = SharedPreferencesUtil.getBoolean(SVTSConstants.isBindingQQ, false);
        isBindingWeibo = SharedPreferencesUtil.getBoolean(SVTSConstants.isBindingWB, false);

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
        switch (channelType) {
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
        mPresenter.channelBinding(channelType, 2, null);
    }

    @OnClick(R.id.ll_user_safety_phone_bind)
    public void onMLlUserSafetyPhoneBindClicked() {
        //更换手机界面
        Intent intent = new Intent(this, ChangePhoneActivity.class);
        startActivity(intent);
    }

    @OnClick(R.id.ll_user_safety_wechat_bind)
    public void onMLlUserSafetyWechatBindClicked() {
        //解绑微信
        channelType = 2;
        RemoveBindingDialog dialog = new RemoveBindingDialog();
        dialog.show(getFragmentManager(), "");
    }

    @OnClick(R.id.ll_user_safety_qq_bind)
    public void onMLlUserSafetyQqBindClicked() {
        //解绑QQ
        channelType = 1;
        RemoveBindingDialog dialog = new RemoveBindingDialog();
        dialog.show(getFragmentManager(), "");
    }

    @OnClick(R.id.ll_user_safety_weibo_bind)
    public void onMLlUserSafetyWeiboBindClicked() {
        //解绑微博
        channelType = 3;
        RemoveBindingDialog dialog = new RemoveBindingDialog();
        dialog.show(getFragmentManager(), "");
    }

    @OnClick(R.id.ll_user_safety_phone_unbind)
    public void onMLlUserSafetyPhoneUnbindClicked() {
    }

    @OnClick(R.id.ll_user_safety_wechat_unbind)
    public void onMLlUserSafetyWechatUnbindClicked() {
        //绑定微信
        channelType = 2;
        Platform wechat = ShareSDK.getPlatform(Wechat.NAME);
        wechat.setPlatformActionListener(mChannelLoginListener);
        wechat.showUser(null);
    }

    @OnClick(R.id.ll_user_safety_qq_unbind)
    public void onMLlUserSafetyQqUnbindClicked() {
        //绑定QQ
        channelType = 1;
        Platform qq = ShareSDK.getPlatform(QQ.NAME);
        qq.setPlatformActionListener(mChannelLoginListener);
        qq.showUser(null);
    }

    @OnClick(R.id.ll_user_safety_weibo_unbind)
    public void onMLlUserSafetyWeiboUnbindClicked() {
        //绑定微博
        channelType = 3;
        Platform weibo = ShareSDK.getPlatform(SinaWeibo.NAME);
        weibo.setPlatformActionListener(mChannelLoginListener);
        weibo.showUser(null);
    }

    private void updateView() {
        mLlUserSafetyQqBind.setVisibility(isBindingQQ? View.VISIBLE:View.GONE);
        mLineQqBind.setVisibility(isBindingQQ? View.VISIBLE:View.GONE);
        mLlUserSafetyQqUnbind.setVisibility(!isBindingQQ? View.VISIBLE:View.GONE);
        mLineQqUnbind.setVisibility(!isBindingQQ? View.VISIBLE:View.GONE);
        mLlUserSafetyWechatBind.setVisibility(isBindingWechat? View.VISIBLE:View.GONE);
        mLineWechatBind.setVisibility(isBindingWechat? View.VISIBLE:View.GONE);
        mLlUserSafetyWechatUnbind.setVisibility(!isBindingWechat? View.VISIBLE:View.GONE);
        mLineWechatUnbind.setVisibility(!isBindingWechat? View.VISIBLE:View.GONE);
        mLlUserSafetyWeiboBind.setVisibility(isBindingWeibo? View.VISIBLE:View.GONE);
        mLineWeiboBind.setVisibility(isBindingWeibo? View.VISIBLE:View.GONE);
        mLlUserSafetyWeiboUnbind.setVisibility(!isBindingWeibo? View.VISIBLE:View.GONE);
    }

    class ChannelLoginListener implements PlatformActionListener {

        @Override
        public void onComplete(Platform platform, int i, HashMap<String, Object> hashMap) {
            if (i == Platform.ACTION_USER_INFOR) {
                PlatformDb platDB = platform.getDb();//获取数平台数据DB
                //通过DB获取各种数据
                String userId = platDB.getUserId();
                mPresenter.channelBinding(channelType, 1, userId);
            }
        }

        @Override
        public void onError(Platform platform, int i, final Throwable throwable) {
            throwable.printStackTrace();
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (throwable instanceof WechatClientNotExistException) {
                        toastShow("请先安装微信客户端");
                    } else {
                        toastShow("授权失败");
                    }
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
