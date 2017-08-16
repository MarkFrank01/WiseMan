package com.zxcx.shitang.ui.loginAndRegister.login;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.meituan.android.walle.WalleChannelReader;
import com.zxcx.shitang.R;
import com.zxcx.shitang.event.LoginEvent;
import com.zxcx.shitang.mvpBase.MvpActivity;
import com.zxcx.shitang.ui.loginAndRegister.forget.ForgetPasswordActivity;
import com.zxcx.shitang.ui.loginAndRegister.register.RegisterActivity;
import com.zxcx.shitang.utils.Constants;
import com.zxcx.shitang.utils.SVTSConstants;
import com.zxcx.shitang.utils.SharedPreferencesUtil;
import com.zxcx.shitang.utils.Utils;

import org.greenrobot.eventbus.EventBus;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);

        initView();
    }

    private void initView() {
        mEtLoginPhone.addTextChangedListener(new LoginTextWatcher());
        mEtLoginPhone.addTextChangedListener(new PhoneTextWatcher());
        mEtLoginPassword.addTextChangedListener(new LoginTextWatcher());
    }

    @Override
    protected LoginPresenter createPresenter() {
        return new LoginPresenter(this);
    }

    @Override
    public void getDataSuccess(LoginBean bean) {
        SharedPreferencesUtil.saveData(SVTSConstants.userId, bean.getUser().getId());
        SharedPreferencesUtil.saveData(SVTSConstants.nickName, bean.getUser().getName());
        SharedPreferencesUtil.saveData(SVTSConstants.sex, bean.getUser().getGender());
        SharedPreferencesUtil.saveData(SVTSConstants.birthday, bean.getUser().getBirth());
        //极光统计
        cn.jiguang.analytics.android.api.LoginEvent lEvent = new cn.jiguang.analytics.android.api.LoginEvent("defult",true);
        lEvent.addKeyValue("appChannel", WalleChannelReader.getChannel(this)).addKeyValue("appVersion", Utils.getAppVersionName(this));
        JAnalyticsInterface.onEvent(this, lEvent);
        //登录成功通知
        EventBus.getDefault().post(new LoginEvent());
        finish();
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
        if (checkPhone() && checkPassword()) {
            String phone = mEtLoginPhone.getText().toString();
            String password = mEtLoginPassword.getText().toString();
            int appType = Constants.APP_TYPE;
            String appChannel = WalleChannelReader.getChannel(this);
            String appVersion = Utils.getAppVersionName(this);
            mPresenter.phoneLogin(phone,password,appType,appChannel,appVersion);
        }
    }

    @OnClick(R.id.ll_login_qq)
    public void onMLlLoginQqClicked() {
        Platform qq = ShareSDK.getPlatform(QQ.NAME);
        qq.setPlatformActionListener(mChannelLoginListener);
        qq.showUser(null);
    }

    @OnClick(R.id.ll_login_wechat)
    public void onMLlLoginWechatClicked() {
        Platform wechat = ShareSDK.getPlatform(Wechat.NAME);
        wechat.setPlatformActionListener(mChannelLoginListener);
        wechat.showUser(null);
    }

    @OnClick(R.id.ll_login_weibo)
    public void onMLlLoginWeiboClicked() {
        Platform weibo = ShareSDK.getPlatform(SinaWeibo.NAME);
        weibo.setPlatformActionListener(mChannelLoginListener);
        weibo.showUser(null);
    }

    @OnClick(R.id.iv_login_close)
    public void onViewClicked() {
        finish();
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

    class ChannelLoginListener implements PlatformActionListener{

        @Override
        public void onComplete(Platform platform, int i, HashMap<String, Object> hashMap) {
            if (i == Platform.ACTION_USER_INFOR) {
                PlatformDb platDB = platform.getDb();//获取数平台数据DB
                //通过DB获取各种数据
                platDB.getToken();
                platDB.getUserGender();
                platDB.getUserIcon();
                platDB.getUserId();
                platDB.getUserName();
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
}
