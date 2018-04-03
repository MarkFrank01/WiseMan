package com.zxcx.zhizhe.ui.my.userInfo.userSafety;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.zxcx.zhizhe.R;
import com.zxcx.zhizhe.mvpBase.BaseActivity;
import com.zxcx.zhizhe.mvpBase.BaseRxJava;
import com.zxcx.zhizhe.mvpBase.INullPostPresenter;
import com.zxcx.zhizhe.retrofit.AppClient;
import com.zxcx.zhizhe.retrofit.BaseBean;
import com.zxcx.zhizhe.retrofit.NullPostSubscriber;
import com.zxcx.zhizhe.ui.loginAndRegister.login.LoginActivity;
import com.zxcx.zhizhe.utils.MD5Utils;
import com.zxcx.zhizhe.utils.Utils;

import java.util.regex.Pattern;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by anm on 2017/7/4.
 */

public class ChangePasswordActivity extends BaseActivity implements INullPostPresenter {


    @BindView(R.id.et_current_password)
    EditText mEtCurrentPassword;
    @BindView(R.id.iv_current_password_clear)
    ImageView mIvCurrentPasswordClear;
    @BindView(R.id.et_new_password)
    EditText mEtNewPassword;
    @BindView(R.id.iv_new_password_clear)
    ImageView mIvNewPasswordClear;
    @BindView(R.id.et_repetition_password)
    EditText mEtRepetitionPassword;
    @BindView(R.id.iv_repetition_password_clear)
    ImageView mIvRepetitionPasswordClear;
    @BindView(R.id.btn_change_password_complete)
    Button mBtnChangePasswordComplete;

    String passwordRules = "^[a-zA-Z0-9]{8,20}$";
    Pattern passwordPattern = Pattern.compile(passwordRules);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);
        ButterKnife.bind(this);

        CheckNullTextWatcher checkNullTextWatcher = new CheckNullTextWatcher();
        mEtCurrentPassword.addTextChangedListener(new CurrentPasswordTextWatcher());
        mEtCurrentPassword.addTextChangedListener(checkNullTextWatcher);
        mEtNewPassword.addTextChangedListener(new NewPasswordTextWatcher());
        mEtNewPassword.addTextChangedListener(checkNullTextWatcher);
        mEtRepetitionPassword.addTextChangedListener(new RepetitionPasswordTextWatcher());
        mEtRepetitionPassword.addTextChangedListener(checkNullTextWatcher);
    }

    @Override
    public void onBackPressed() {
        Utils.closeInputMethod(mEtCurrentPassword);
        Utils.closeInputMethod(mEtNewPassword);
        Utils.closeInputMethod(mEtRepetitionPassword);
        super.onBackPressed();
    }

    @Override
    public void postSuccess() {
        toastShow("修改成功");
        finish();
    }

    @Override
    public void postFail(String msg) {
        toastShow(msg);
    }

    @Override
    public void startLogin() {
        startActivity(new Intent(mActivity, LoginActivity.class));
    }

    @OnClick(R.id.iv_current_password_clear)
    public void onMIvCurrentPasswordClearClicked() {
        mEtCurrentPassword.setText("");
    }

    @OnClick(R.id.iv_new_password_clear)
    public void onMIvNewPasswordClearClicked() {
        mEtNewPassword.setText("");
    }

    @OnClick(R.id.iv_repetition_password_clear)
    public void onMIvRepetitionPasswordClearClicked() {
        mEtRepetitionPassword.setText("");
    }

    @OnClick(R.id.btn_change_password_complete)
    public void onMBtnChangePasswordCompleteClicked() {
        String currentPassword = mEtCurrentPassword.getText().toString();
        String newPassword = mEtNewPassword.getText().toString();
        String repetitionPassword = mEtRepetitionPassword.getText().toString();

        if (checkPassword()) {
            if (newPassword.equals(repetitionPassword)) {
                changePassword(MD5Utils.md5(currentPassword), MD5Utils.md5(newPassword));
            } else {
                toastShow("两次输入密码不一致");
            }
        }
    }

    public void changePassword(String oldPassword, String newPassword) {
        mDisposable = AppClient.getAPIService().changePassword(oldPassword, newPassword)
                .compose(BaseRxJava.INSTANCE.handlePostResult())
                .compose(BaseRxJava.INSTANCE.<BaseBean>io_main_loading(this))
                .subscribeWith(new NullPostSubscriber<BaseBean>(this) {
                    @Override
                    public void onNext(BaseBean bean) {
                        ChangePasswordActivity.this.postSuccess();
                    }
                });
        addSubscription(mDisposable);
    }

    private boolean checkPassword() {
        if (passwordPattern.matcher(mEtNewPassword.getText().toString()).matches()) {
            return true;
        } else {
            toastShow("新密码格式错误!");
            return false;
        }
    }

    class CheckNullTextWatcher implements TextWatcher {

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            if (mEtCurrentPassword.length() > 0 && mEtNewPassword.length() > 0 && mEtRepetitionPassword.length() > 0) {
                mBtnChangePasswordComplete.setEnabled(true);
            } else {
                mBtnChangePasswordComplete.setEnabled(false);
            }
        }
    }

    class CurrentPasswordTextWatcher implements TextWatcher {

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            if (mEtCurrentPassword.length() > 0) {
                mIvCurrentPasswordClear.setVisibility(View.VISIBLE);
            } else {
                mIvCurrentPasswordClear.setVisibility(View.GONE);
            }
        }
    }

    class NewPasswordTextWatcher implements TextWatcher {

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            if (mEtNewPassword.length() > 0) {
                mIvNewPasswordClear.setVisibility(View.VISIBLE);
            } else {
                mIvNewPasswordClear.setVisibility(View.GONE);
            }
        }
    }

    class RepetitionPasswordTextWatcher implements TextWatcher {

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            if (mEtRepetitionPassword.length() > 0) {
                mIvRepetitionPasswordClear.setVisibility(View.VISIBLE);
            } else {
                mIvRepetitionPasswordClear.setVisibility(View.GONE);
            }
        }
    }
}
