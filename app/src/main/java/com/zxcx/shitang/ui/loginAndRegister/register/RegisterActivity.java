package com.zxcx.shitang.ui.loginAndRegister.register;

import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.zxcx.shitang.R;
import com.zxcx.shitang.mvpBase.MvpActivity;

import java.util.regex.Pattern;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class RegisterActivity extends MvpActivity<RegisterPresenter> implements RegisterContract.View {

    @BindView(R.id.et_register_phone)
    EditText mEtRegisterPhone;
    @BindView(R.id.iv_register_phone_clear)
    ImageView mIvRegisterPhoneClear;
    @BindView(R.id.et_register_verification_code)
    EditText mEtRegisterVerificationCode;
    @BindView(R.id.tv_register_send_verification)
    TextView mTvRegisterSendVerification;
    @BindView(R.id.et_register_password)
    EditText mEtRegisterPassword;
    @BindView(R.id.btn_register)
    Button mBtnRegister;

    private int count = 60;
    Handler handler = new Handler();
    String phoneRules = "^1\\d{10}$";
    String passwordRules = "^[a-zA-Z0-9]{6,16}$";
    Pattern phonePattern = Pattern.compile(phoneRules);
    Pattern passwordPattern = Pattern.compile(passwordRules);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_register);
        ButterKnife.bind(this);


        CheckNullTextWatcher textWatcher = new CheckNullTextWatcher();
        mEtRegisterPhone.addTextChangedListener(textWatcher);
        mEtRegisterPhone.addTextChangedListener(new PhoneTextWatcher());
        mEtRegisterPassword.addTextChangedListener(textWatcher);
        mEtRegisterVerificationCode.addTextChangedListener(textWatcher);
    }

    @Override
    protected RegisterPresenter createPresenter() {
        return new RegisterPresenter(this);
    }

    @Override
    public void getDataSuccess(RegisterBean bean) {

    }

    @OnClick(R.id.iv_register_close)
    public void onMIvRegisterCloseClicked() {
        onBackPressed();
    }

    @OnClick(R.id.iv_register_phone_clear)
    public void onMIvRegisterPhoneClearClicked() {
        mEtRegisterPhone.setText("");
    }

    @OnClick(R.id.tv_register_send_verification)
    public void onMTvRegisterSendVerificationClicked() {
        if (checkPhone()) {
            mTvRegisterSendVerification.setEnabled(false);
            handler.post(setDjs);
        }
    }

    @OnClick(R.id.btn_register)
    public void onMBtnRegisterClicked() {
        if (checkPhone() && checkPassword()) {
            finish();
        }
    }

    @OnClick(R.id.tv_register_agreement)
    public void onMTvRegisterAgreementClicked() {
    }



    private boolean checkPhone() {
        if (phonePattern.matcher(mEtRegisterPhone.getText().toString()).matches()) {
            return true;
        }else {
            toastShow("手机号格式错误!");
            return false;
        }
    }

    private boolean checkPassword() {
        if (passwordPattern.matcher(mEtRegisterPassword.getText().toString()).matches()) {
            return true;
        }else {
            toastShow("密码格式错误!");
            return false;
        }
    }

    Runnable setDjs = new Runnable() {
        @Override
        public void run() {
            mTvRegisterSendVerification.setText(count + " S");
            count--;

            if (count < 0) {
                handler.removeCallbacks(setDjs);
                mTvRegisterSendVerification.setEnabled(true);
                mTvRegisterSendVerification.setText("重新发送验证码");

                count = 60;

                return;
            }

            handler.postDelayed(setDjs, 1000);
        }
    };

    class CheckNullTextWatcher implements TextWatcher{

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            if (mEtRegisterPhone.length() > 0 && mEtRegisterVerificationCode.length() > 0
                    && mEtRegisterPassword.length() > 0){
                mBtnRegister.setEnabled(true);
            }else {
                mBtnRegister.setEnabled(false);
            }
        }
    }

    class PhoneTextWatcher implements TextWatcher{

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            if (mEtRegisterPhone.length() > 0 ){
                mIvRegisterPhoneClear.setVisibility(View.VISIBLE);
            }else {
                mIvRegisterPhoneClear.setVisibility(View.GONE);
            }
        }
    }
}
