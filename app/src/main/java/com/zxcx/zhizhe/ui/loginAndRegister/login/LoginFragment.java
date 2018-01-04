package com.zxcx.zhizhe.ui.loginAndRegister.login;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.gyf.barlibrary.ImmersionBar;
import com.meituan.android.walle.WalleChannelReader;
import com.zxcx.zhizhe.R;
import com.zxcx.zhizhe.event.LoginEvent;
import com.zxcx.zhizhe.event.PhoneRegisteredEvent;
import com.zxcx.zhizhe.event.RegisterEvent;
import com.zxcx.zhizhe.mvpBase.MvpFragment;
import com.zxcx.zhizhe.ui.loginAndRegister.channelRegister.ChannelRegisterActivity;
import com.zxcx.zhizhe.ui.loginAndRegister.forget.ForgetPasswordActivity;
import com.zxcx.zhizhe.utils.Constants;
import com.zxcx.zhizhe.utils.MD5Utils;
import com.zxcx.zhizhe.utils.Utils;
import com.zxcx.zhizhe.utils.ZhiZheUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.HashMap;
import java.util.regex.Pattern;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnEditorAction;
import butterknife.OnTextChanged;
import butterknife.Unbinder;
import cn.jiguang.analytics.android.api.JAnalyticsInterface;
import cn.jpush.android.api.JPushInterface;
import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.framework.PlatformDb;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.sina.weibo.SinaWeibo;
import cn.sharesdk.tencent.qq.QQ;
import cn.sharesdk.wechat.friends.Wechat;
import cn.sharesdk.wechat.utils.WechatClientNotExistException;

public class LoginFragment extends MvpFragment<LoginPresenter> implements LoginContract.View {

    @BindView(R.id.et_login_phone)
    EditText mEtLoginPhone;
    @BindView(R.id.et_login_password)
    EditText mEtLoginPassword;
    @BindView(R.id.btn_login)
    Button mBtnLogin;
    @BindView(R.id.iv_login_phone_clear)
    ImageView mIvLoginPhoneClear;

    String phoneRules = "^1\\d{10}$";
    String passwordRules = "^.{8,20}$";
    Pattern phonePattern = Pattern.compile(phoneRules);
    Pattern passwordPattern = Pattern.compile(passwordRules);

    PlatformActionListener mChannelLoginListener = new ChannelLoginListener();
    @BindView(R.id.iv_login_password_clear)
    ImageView mIvLoginPasswordClear;
    @BindView(R.id.ll_login_sdk_login)
    LinearLayout mLlLoginSdkLogin;
    private int channelType; // 1-QQ 2-WeChat 3-Weibo
    private int appType;
    private String userName, userId, userIcon, userGender, appChannel, appVersion, jpushID;
    private Unbinder unbinder;
    private ImmersionBar mImmersionBar;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_login, container, false);
        unbinder = ButterKnife.bind(this, root);
        return root;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        EventBus.getDefault().register(this);
        initStatusBar();

        appType = Constants.APP_TYPE;
        appChannel = WalleChannelReader.getChannel(mActivity);
        appVersion = Utils.getAppVersionName(mActivity);
        jpushID = JPushInterface.getRegistrationID(mActivity);
    }

    public void initStatusBar() {
        mImmersionBar = ImmersionBar.with(this)
                .keyboardEnable(true)
                .setOnKeyboardListener((isPopup, keyboardHeight) -> {
                    mLlLoginSdkLogin.setVisibility(isPopup? View.GONE: View.VISIBLE);
                });
        if (!Constants.IS_NIGHT) {
            mImmersionBar
                    .statusBarColor(R.color.background)
                    .statusBarDarkFont(true, 0.2f)
                    .flymeOSStatusBarFontColor(R.color.text_color_1);
        } else {
            mImmersionBar
                    .statusBarColor(R.color.background)
                    .flymeOSStatusBarFontColor(R.color.text_color_1);
        }
        mImmersionBar.init();
    }

    @Override
    public void onDestroy() {
        unbinder.unbind();
        if (mImmersionBar != null)
            mImmersionBar.destroy();
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Override
    protected LoginPresenter createPresenter() {
        return new LoginPresenter(this);
    }

    @Override
    public void getDataSuccess(LoginBean bean) {
        ZhiZheUtils.saveLoginData(bean);
        //极光统计
        cn.jiguang.analytics.android.api.LoginEvent lEvent = new cn.jiguang.analytics.android.api.LoginEvent("defult", true);
        lEvent.addKeyValue("appChannel", WalleChannelReader.getChannel(mActivity)).addKeyValue("appVersion", Utils.getAppVersionName(mActivity));
        JAnalyticsInterface.onEvent(mActivity, lEvent);
        //登录成功通知
        EventBus.getDefault().post(new LoginEvent());
        Utils.closeInputMethod(mEtLoginPassword);
        mActivity.finish();
    }

    @Override
    public void channelLoginSuccess(LoginBean bean) {
        getDataSuccess(bean);
    }

    @Override
    public void channelLoginNeedRegister() {
        Intent intent = new Intent(mActivity, ChannelRegisterActivity.class);
        intent.putExtra("userId", userId);
        intent.putExtra("userName", userName);
        intent.putExtra("userIcon", userIcon);
        intent.putExtra("userGender", userGender);
        intent.putExtra("channelType", channelType);
        startActivity(intent);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(PhoneRegisteredEvent event) {
        //手机号已注册，跳转登录
        mEtLoginPhone.setText(event.getPhone());
        mEtLoginPassword.setText("");
        Utils.showInputMethod(mEtLoginPassword);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(RegisterEvent event) {
        //登录成功通知
        EventBus.getDefault().post(new LoginEvent());
        mActivity.finish();
    }

    @OnClick(R.id.iv_login_phone_clear)
    public void onMIvLoginPhoneClearClicked() {
        mEtLoginPhone.setText("");
    }

    @OnClick(R.id.iv_login_password_clear)
    public void onMIvLoginPasswordClearClicked() {
        mEtLoginPassword.setText("");
    }

    @OnClick(R.id.tv_login_forget_password)
    public void onMTvLoginForgetPasswordClicked() {
        Intent intent = new Intent(mActivity, ForgetPasswordActivity.class);
        startActivity(intent);
    }

    @OnClick(R.id.btn_login)
    public void onMBtnLoginClicked() {
        login();
    }

    @OnClick(R.id.ll_login_qq)
    public void onMLlLoginQqClicked() {
        channelType = 1;
        Platform qq = ShareSDK.getPlatform(QQ.NAME);
        qq.setPlatformActionListener(mChannelLoginListener);
        qq.showUser(null);
    }

    @OnClick(R.id.ll_login_wechat)
    public void onMLlLoginWechatClicked() {
        channelType = 2;
        Platform wechat = ShareSDK.getPlatform(Wechat.NAME);
        wechat.setPlatformActionListener(mChannelLoginListener);
        wechat.showUser(null);
    }

    @OnClick(R.id.ll_login_weibo)
    public void onMLlLoginWeiboClicked() {
        channelType = 3;
        Platform weibo = ShareSDK.getPlatform(SinaWeibo.NAME);
        weibo.setPlatformActionListener(mChannelLoginListener);
        weibo.showUser(null);
    }

    @OnTextChanged({R.id.et_login_phone, R.id.et_login_password})
    public void onTextChange() {
        if (mEtLoginPhone.length() > 0) {
            mIvLoginPhoneClear.setVisibility(View.VISIBLE);
        } else {
            mIvLoginPhoneClear.setVisibility(View.GONE);
        }
        if (mEtLoginPassword.length() > 0) {
            mIvLoginPasswordClear.setVisibility(View.VISIBLE);
        } else {
            mIvLoginPasswordClear.setVisibility(View.GONE);
        }
        if (checkPhone()) {
            mEtLoginPassword.setEnabled(true);
        } else {
            mEtLoginPassword.setEnabled(false);
        }
        if (checkPhone() && checkPassword()) {
            mBtnLogin.setEnabled(true);
        } else {
            mBtnLogin.setEnabled(false);
        }
    }

    @OnEditorAction({R.id.et_login_phone, R.id.et_login_password})
    public boolean onEnterClick(TextView v, int actionId, KeyEvent event) {
        if (actionId == EditorInfo.IME_ACTION_SEARCH
                || actionId == EditorInfo.IME_ACTION_DONE
                || (event != null && KeyEvent.KEYCODE_ENTER == event.getKeyCode() && KeyEvent.ACTION_DOWN == event.getAction())) {
            mBtnLogin.performClick();
            return true;
        }
        return false;
    }

    private void login() {
        if (checkPhone() && checkPassword()) {
            String phone = mEtLoginPhone.getText().toString();
            String password = MD5Utils.md5(mEtLoginPassword.getText().toString());
            mPresenter.phoneLogin(phone, password, jpushID, appType, appChannel, appVersion);
        }
    }

    private boolean checkPhone() {
        if (phonePattern.matcher(mEtLoginPhone.getText().toString()).matches()) {
            return true;
        } else {
            return false;
        }
    }

    private boolean checkPassword() {
        if (passwordPattern.matcher(mEtLoginPassword.getText().toString()).matches()) {
            return true;
        } else {
            return false;
        }
    }

    class ChannelLoginListener implements PlatformActionListener {

        @Override
        public void onComplete(Platform platform, int i, HashMap<String, Object> hashMap) {
            if (i == Platform.ACTION_USER_INFOR) {
                PlatformDb platDB = platform.getDb();//获取数平台数据DB
                //通过DB获取各种数据
                userGender = platDB.getUserGender();
                userIcon = platDB.getUserIcon();
                userId = platDB.getUserId();
                userName = platDB.getUserName();
                mPresenter.channelLogin(channelType, userId, jpushID, appType, appChannel, appVersion);
            }
        }

        @Override
        public void onError(Platform platform, int i, final Throwable throwable) {
            throwable.printStackTrace();
            mActivity.runOnUiThread(() -> {
                if (throwable instanceof WechatClientNotExistException) {
                    toastShow("请先安装微信客户端");
                } else {
                    toastShow("登录失败");
                }
            });
        }

        @Override
        public void onCancel(Platform platform, int i) {
            mActivity.runOnUiThread(() -> toastShow("登录取消"));
        }
    }
}
