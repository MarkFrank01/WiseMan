package com.zxcx.zhizhe.ui.loginAndRegister.login.bindingPhone;

import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.zxcx.zhizhe.R;
import com.zxcx.zhizhe.mvpBase.MvpActivity;

import java.util.regex.Pattern;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class BindingPhoneActivity extends MvpActivity<BindingPhonePresenter> implements BindingPhoneContract.View {

    @BindView(R.id.iv_binding_phone_close)
    ImageView mIvBindingPhoneClose;
    @BindView(R.id.et_binding_phone_phone)
    EditText mEtBindingPhonePhone;
    @BindView(R.id.iv_binding_phone_phone_clear)
    ImageView mIvBindingPhonePhoneClear;
    @BindView(R.id.et_binding_phone_verification_code)
    EditText mEtBindingPhoneVerificationCode;
    @BindView(R.id.tv_binding_phone_send_verification)
    TextView mTvBindingPhoneSendVerification;
    @BindView(R.id.btn_binding_phone_complete)
    Button mBtnBindingPhoneComplete;

    private int count = 60;
    Handler handler = new Handler();
    String phoneRules = "^1\\d{10}$";
    Pattern phonePattern = Pattern.compile(phoneRules);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_binding_phone);
        ButterKnife.bind(this);

        mEtBindingPhonePhone.addTextChangedListener(new NextCheckNullTextWatcher());
        mEtBindingPhonePhone.addTextChangedListener(new PhoneTextWatcher());
        mEtBindingPhoneVerificationCode.addTextChangedListener(new NextCheckNullTextWatcher());
    }

    @Override
    protected BindingPhonePresenter createPresenter() {
        return new BindingPhonePresenter(this);
    }

    @Override
    public void getDataSuccess(BindingPhoneBean bean) {

    }

    @OnClick(R.id.iv_binding_phone_close)
    public void onMIvBindingPhoneCloseClicked() {
    }

    @OnClick(R.id.iv_binding_phone_phone_clear)
    public void onMIvBindingPhonePhoneClearClicked() {
    }

    @OnClick(R.id.btn_binding_phone_complete)
    public void onMBtnBindingPhoneCompleteClicked() {
    }

    @OnClick(R.id.tv_binding_phone_send_verification)
    public void onMTvBindingPhoneSendVerificationClicked() {
        if (checkPhone()) {
            mTvBindingPhoneSendVerification.setEnabled(false);
            handler.post(setDjs);
        }
    }

    private boolean checkPhone() {
        if (phonePattern.matcher(mEtBindingPhonePhone.getText().toString()).matches()) {
            return true;
        } else {
            return false;
        }
    }

    Runnable setDjs = new Runnable() {
        @Override
        public void run() {
            mTvBindingPhoneSendVerification.setText(count + " S");
            count--;

            if (count < 0) {
                handler.removeCallbacks(setDjs);
                mTvBindingPhoneSendVerification.setEnabled(true);
                mTvBindingPhoneSendVerification.setText(R.string.re_get_verification);

                count = 60;

                return;
            }

            handler.postDelayed(setDjs, 1000);
        }
    };

    class NextCheckNullTextWatcher implements TextWatcher {

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            if (mEtBindingPhonePhone.length() > 0 && mEtBindingPhoneVerificationCode.length() > 0) {
                mBtnBindingPhoneComplete.setEnabled(true);
            } else {
                mBtnBindingPhoneComplete.setEnabled(false);
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
            if (mEtBindingPhonePhone.length() > 0) {
                mIvBindingPhonePhoneClear.setVisibility(View.VISIBLE);
            } else {
                mIvBindingPhonePhoneClear.setVisibility(View.GONE);
            }
        }
    }
}
