package com.zxcx.shitang.ui.loginAndRegister.forget;

import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.zxcx.shitang.R;
import com.zxcx.shitang.mvpBase.MvpActivity;

import java.util.regex.Pattern;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.smssdk.EventHandler;
import cn.smssdk.SMSSDK;

public class ForgetPasswordActivity extends MvpActivity<ForgetPasswordPresenter> implements ForgetPasswordContract.View {

    @BindView(R.id.et_forget_password_phone)
    EditText mEtForgetPasswordPhone;
    @BindView(R.id.iv_forget_password_phone_clear)
    ImageView mIvForgetPasswordPhoneClear;
    @BindView(R.id.et_forget_password_verification_code)
    EditText mEtForgetPasswordVerificationCode;
    @BindView(R.id.tv_forget_password_send_verification)
    TextView mTvForgetPasswordSendVerification;
    @BindView(R.id.btn_forget_password_next)
    Button mBtnForgetPasswordNext;
    @BindView(R.id.ll_forget_password_next)
    LinearLayout mLlForgetPasswordNext;
    @BindView(R.id.et_forget_password_password)
    EditText mEtForgetPasswordPassword;
    @BindView(R.id.btn_forget_password_complete)
    Button mBtnForgetPasswordComplete;
    @BindView(R.id.ll_forget_password_complete)
    LinearLayout mLlForgetPasswordComplete;
    @BindView(R.id.tv_forget_password_send_over)
    TextView mTvForgetPasswordSendOver;

    private int count = 60;
    Handler handler = new Handler();
    String phoneRules = "^1\\d{10}$";
    String passwordRules = "^[a-zA-Z0-9]{6,16}$";
    Pattern phonePattern = Pattern.compile(phoneRules);
    Pattern passwordPattern = Pattern.compile(passwordRules);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_password);
        ButterKnife.bind(this);

        SMSSDK.registerEventHandler(new EventHandle());

        mEtForgetPasswordPhone.addTextChangedListener(new NextCheckNullTextWatcher());
        mEtForgetPasswordPhone.addTextChangedListener(new PhoneTextWatcher());
        mEtForgetPasswordVerificationCode.addTextChangedListener(new NextCheckNullTextWatcher());
        mEtForgetPasswordPassword.addTextChangedListener(new CompleteCheckNullTextWatcher());

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        SMSSDK.unregisterAllEventHandler();
    }

    @Override
    protected ForgetPasswordPresenter createPresenter() {
        return new ForgetPasswordPresenter(this);
    }

    @Override
    public void getDataSuccess(ForgetPasswordBean bean) {

    }

    @OnClick(R.id.iv_forget_password_close)
    public void onMIvForgetPasswordCloseClicked() {
        onBackPressed();
    }

    @OnClick(R.id.iv_forget_password_phone_clear)
    public void onMIvForgetPasswordPhoneClearClicked() {
        mEtForgetPasswordPhone.setText("");
    }

    @OnClick(R.id.tv_forget_password_send_verification)
    public void onMTvForgetPasswordSendVerificationClicked() {
        if (checkPhone()) {
            SMSSDK.getVerificationCode("86",mEtForgetPasswordPhone.getText().toString());
        }
    }

    @OnClick(R.id.btn_forget_password_next)
    public void onMBtnForgetPasswordNextClicked() {
        if (checkPhone()) {
            SMSSDK.submitVerificationCode("86",mEtForgetPasswordPhone.getText().toString(),
                    mEtForgetPasswordVerificationCode.getText().toString());
        }
    }

    @OnClick(R.id.btn_forget_password_complete)
    public void onMBtnForgetPasswordCompleteClicked() {
        if (checkPassword()) {
            finish();
        }
    }

    @OnClick(R.id.tv_forget_password_send_over)
    public void onViewClicked() {
    }

    private boolean checkPhone() {
        if (phonePattern.matcher(mEtForgetPasswordPhone.getText().toString()).matches()) {
            return true;
        } else {
            toastShow("手机号格式错误!");
            return false;
        }
    }

    private boolean checkPassword() {
        if (passwordPattern.matcher(mEtForgetPasswordPassword.getText().toString()).matches()) {
            return true;
        } else {
            toastShow("密码格式错误!");
            return false;
        }
    }

    Runnable setDjs = new Runnable() {
        @Override
        public void run() {
            mTvForgetPasswordSendVerification.setText(count + " S");
            count--;

            if (count < 0) {
                handler.removeCallbacks(setDjs);
                mTvForgetPasswordSendVerification.setEnabled(true);
                mTvForgetPasswordSendVerification.setText("重新发送验证码");

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
            if (mEtForgetPasswordPhone.length() > 0 && mEtForgetPasswordVerificationCode.length() > 0) {
                mBtnForgetPasswordNext.setEnabled(true);
            } else {
                mBtnForgetPasswordNext.setEnabled(false);
            }
        }
    }

    class CompleteCheckNullTextWatcher implements TextWatcher {

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            if (mEtForgetPasswordPassword.length() > 0) {
                mBtnForgetPasswordComplete.setEnabled(true);
            } else {
                mBtnForgetPasswordComplete.setEnabled(false);
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
            if (mEtForgetPasswordPhone.length() > 0) {
                mIvForgetPasswordPhoneClear.setVisibility(View.VISIBLE);
            } else {
                mIvForgetPasswordPhoneClear.setVisibility(View.GONE);
            }
            mTvForgetPasswordSendVerification.setVisibility(View.VISIBLE);
            mTvForgetPasswordSendOver.setVisibility(View.GONE);
        }
    }

    class EventHandle extends EventHandler {
        @Override
        public void afterEvent(final int event, final int result, final Object data) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (result == SMSSDK.RESULT_COMPLETE) {
                        //回调完成
                        if (event == SMSSDK.EVENT_SUBMIT_VERIFICATION_CODE) {
                            //提交验证码成功
                            mLlForgetPasswordNext.setVisibility(View.GONE);
                            mLlForgetPasswordComplete.setVisibility(View.VISIBLE);
                        } else if (event == SMSSDK.EVENT_GET_VERIFICATION_CODE) {
                            //获取验证码成功
                            mTvForgetPasswordSendVerification.setEnabled(false);
                            handler.post(setDjs);
                        } else if (event == SMSSDK.EVENT_GET_SUPPORTED_COUNTRIES) {
                            //返回支持发送验证码的国家列表
                        }
                    } else {
                        // 根据服务器返回的网络错误，给toast提示
                        try {
                            Throwable throwable = (Throwable) data;
                            throwable.printStackTrace();
                            JSONObject object = JSON.parseObject(throwable.getMessage());
                            String des = object.getString("detail");//错误描述
                            int status = object.getInteger("status");//错误代码
                            switch (status) {
                                case 457:
                                    toastShow("手机号格式错误!");
                                    break;
                                case 463:
                                case 464:
                                case 465:
                                    mTvForgetPasswordSendVerification.setVisibility(View.GONE);
                                    mTvForgetPasswordSendOver.setVisibility(View.VISIBLE);
                                    break;
                            }
                        } catch (Exception e) {
                            //do something
                        }
                    }
                }
            });
        }
    }
}
