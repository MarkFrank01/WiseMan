package com.zxcx.shitang.ui.loginAndRegister.login;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;

import com.zxcx.shitang.R;
import com.zxcx.shitang.event.LoginEvent;
import com.zxcx.shitang.mvpBase.MvpActivity;
import com.zxcx.shitang.ui.loginAndRegister.forget.ForgetPasswordActivity;
import com.zxcx.shitang.ui.loginAndRegister.register.RegisterActivity;
import com.zxcx.shitang.utils.SVTSConstants;
import com.zxcx.shitang.utils.SharedPreferencesUtil;

import org.greenrobot.eventbus.EventBus;

import java.util.regex.Pattern;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class LoginActivity extends MvpActivity<LoginPresenter> implements LoginContract.View {

    @BindView(R.id.et_login_phone)
    EditText mEtLoginPhone;
    @BindView(R.id.et_login_password)
    EditText mEtLoginPassword;
    @BindView(R.id.btn_login)
    Button mBtnLogin;

    String phoneRules = "^1\\d{10}$";
    String passwordRules = "^[a-zA-Z0-9]{6,16}$";
    Pattern phonePattern = Pattern.compile(phoneRules);
    Pattern passwordPattern = Pattern.compile(passwordRules);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);

        initView();
    }

    private void initView() {
        mEtLoginPhone.addTextChangedListener(new LoginTextWatcher());
        mEtLoginPassword.addTextChangedListener(new LoginTextWatcher());
    }

    @Override
    protected LoginPresenter createPresenter() {
        return new LoginPresenter(this);
    }

    @Override
    public void getDataSuccess(LoginBean bean) {

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
            SharedPreferencesUtil.saveData(SVTSConstants.userId, "asdasd16545");
            SharedPreferencesUtil.saveData(SVTSConstants.nickName, "一叶知秋");
            SharedPreferencesUtil.saveData(SVTSConstants.sex, 1);
            SharedPreferencesUtil.saveData(SVTSConstants.birthday, "1992-06-09");
            EventBus.getDefault().post(new LoginEvent());
            finish();
        }
    }

    @OnClick(R.id.ll_login_qq)
    public void onMLlLoginQqClicked() {
    }

    @OnClick(R.id.ll_login_wechat)
    public void onMLlLoginWechatClicked() {
    }

    @OnClick(R.id.ll_login_weibo)
    public void onMLlLoginWeiboClicked() {
    }

    @OnClick(R.id.iv_login_close)
    public void onViewClicked() {
        finish();
    }

    private boolean checkPhone() {
        if (phonePattern.matcher(mEtLoginPhone.getText().toString()).matches()) {
            return true;
        }else {
            toastShow("手机号格式错误!");
            return false;
        }
    }

    private boolean checkPassword() {
        if (passwordPattern.matcher(mEtLoginPassword.getText().toString()).matches()) {
            return true;
        }else {
            toastShow("密码格式错误!");
            return false;
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
}
