package com.zxcx.zhizhe.ui.loginAndRegister.login;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextPaint;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.meituan.android.walle.WalleChannelReader;
import com.zxcx.zhizhe.R;
import com.zxcx.zhizhe.event.LoginEvent;
import com.zxcx.zhizhe.event.RegisterEvent;
import com.zxcx.zhizhe.mvpBase.MvpActivity;
import com.zxcx.zhizhe.ui.loginAndRegister.channelRegister.ChannelRegisterActivity;
import com.zxcx.zhizhe.ui.loginAndRegister.forget.ForgetPasswordActivity;
import com.zxcx.zhizhe.ui.loginAndRegister.register.RegisterActivity;
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
import cn.jiguang.analytics.android.api.JAnalyticsInterface;
import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.framework.PlatformDb;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.sina.weibo.SinaWeibo;
import cn.sharesdk.tencent.qq.QQ;
import cn.sharesdk.wechat.friends.Wechat;

public class LoginActivity extends MvpActivity<LoginPresenter> implements LoginContract.View {

    @BindView(R.id.et_login_phone)
    EditText mEtLoginPhone;
    @BindView(R.id.et_login_password)
    EditText mEtLoginPassword;
    @BindView(R.id.btn_login)
    Button mBtnLogin;
    @BindView(R.id.iv_login_phone_clear)
    ImageView mIvLoginPhoneClear;

    String phoneRules = "^1\\d{10}$";
    String passwordRules = "^[a-zA-Z0-9]{6,16}$";
    Pattern phonePattern = Pattern.compile(phoneRules);
    Pattern passwordPattern = Pattern.compile(passwordRules);

    PlatformActionListener mChannelLoginListener = new ChannelLoginListener();
    private int channelType; // 1-QQ 2-WeChat 3-Weibo
    private int appType;
    private String userName, userId, userIcon, userGender, appChannel, appVersion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        EventBus.getDefault().register(this);
        initView();

        appType = Constants.APP_TYPE;
        appChannel = WalleChannelReader.getChannel(this);
        appVersion = Utils.getAppVersionName(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    private void initView() {
        mEtLoginPhone.addTextChangedListener(new LoginTextWatcher());
        mEtLoginPhone.addTextChangedListener(new PhoneTextWatcher());
        mEtLoginPassword.addTextChangedListener(new LoginTextWatcher());
        mEtLoginPassword.setOnEditorActionListener(new LoginListener());
        TextPaint paint = mBtnLogin.getPaint();
        paint.setFakeBoldText(true);
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
        lEvent.addKeyValue("appChannel", WalleChannelReader.getChannel(this)).addKeyValue("appVersion", Utils.getAppVersionName(this));
        JAnalyticsInterface.onEvent(this, lEvent);
        //登录成功通知
        EventBus.getDefault().post(new LoginEvent());
        EventBus.getDefault().postSticky(new LoginEvent());
        Utils.closeInputMethod(mEtLoginPassword);
        finish();
    }

    @Override
    public void channelLoginSuccess(LoginBean bean) {
        getDataSuccess(bean);
    }

    @Override
    public void channelLoginNeedRegister() {
        toastShow("请绑定手机号码");
        Intent intent = new Intent(this, ChannelRegisterActivity.class);
        intent.putExtra("userId",userId);
        intent.putExtra("userName",userName);
        intent.putExtra("userIcon",userIcon);
        intent.putExtra("userGender",userGender);
        intent.putExtra("channelType",channelType);
        startActivity(intent);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(RegisterEvent event) {
        //登录成功通知
        EventBus.getDefault().post(new LoginEvent());
        EventBus.getDefault().postSticky(new LoginEvent());
        finish();
    }

    @OnClick(R.id.iv_login_phone_clear)
    public void onMIvLoginPhoneClearClicked() {
        mEtLoginPhone.setText("");
    }

    @OnClick(R.id.tv_login_register)
    public void onMTvLoginRegisterClicked() {
        Intent intent = new Intent(this, RegisterActivity.class);
        startActivity(intent);
    }

    @OnClick(R.id.tv_login_forget_password)
    public void onMTvLoginForgetPasswordClicked() {
        Intent intent = new Intent(this, ForgetPasswordActivity.class);
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

    @OnClick(R.id.iv_login_close)
    public void onViewClicked() {
        finish();
    }

    private void login() {
        if (checkPhone() && checkPassword()) {
            String phone = mEtLoginPhone.getText().toString();
            String password = MD5Utils.md5(mEtLoginPassword.getText().toString());
            mPresenter.phoneLogin(phone, password, appType, appChannel, appVersion);
        }
    }

    private boolean checkPhone() {
        if (phonePattern.matcher(mEtLoginPhone.getText().toString()).matches()) {
            return true;
        } else {
            toastShow("手机号格式错误!");
            return false;
        }
    }

    private boolean checkPassword() {
        if (passwordPattern.matcher(mEtLoginPassword.getText().toString()).matches()) {
            return true;
        } else {
            toastShow("密码格式错误!");
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
                mPresenter.channelLogin(channelType,userId,appType,appChannel,appVersion);
            }
        }

        @Override
        public void onError(Platform platform, int i, Throwable throwable) {
            throwable.printStackTrace();
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    toastShow("登录失败");
                }
            });
        }

        @Override
        public void onCancel(Platform platform, int i) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    toastShow("登录取消");
                }
            });
        }
    }

    class LoginTextWatcher implements TextWatcher {

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            if (mEtLoginPhone.length() > 0 && mEtLoginPassword.length() > 0) {
                mBtnLogin.setEnabled(true);
            } else {
                mBtnLogin.setEnabled(false);
            }
        }
    }


    class PhoneTextWatcher implements TextWatcher {

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            if (mEtLoginPhone.length() > 0) {
                mIvLoginPhoneClear.setVisibility(View.VISIBLE);
            } else {
                mIvLoginPhoneClear.setVisibility(View.GONE);
            }
        }
    }

    private class LoginListener implements TextView.OnEditorActionListener {
        @Override
        public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
            //此处会响应2次 分别为ACTION_DOWN和ACTION_UP
            if (actionId == EditorInfo.IME_ACTION_SEARCH
                    || actionId == EditorInfo.IME_ACTION_DONE
                    || (event != null && KeyEvent.KEYCODE_ENTER == event.getKeyCode() && KeyEvent.ACTION_DOWN == event.getAction())) {
                login();
                return true;
            }
            return false;
        }
    }
}
