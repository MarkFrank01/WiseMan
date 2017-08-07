package com.zxcx.shitang.ui.loginAndRegister.register;

import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.zxcx.shitang.R;
import com.zxcx.shitang.mvpBase.MvpActivity;
import com.zxcx.shitang.utils.SVTSConstants;
import com.zxcx.shitang.utils.SharedPreferencesUtil;

import java.util.regex.Pattern;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.smssdk.EventHandler;
import cn.smssdk.SMSSDK;

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
    @BindView(R.id.tv_register_send_over)
    TextView mTvRegisterSendOver;

    private int count = 60;
    Handler handler = new Handler();
    String phoneRules = "^1\\d{10}$";
    String passwordRules = "^[a-zA-Z0-9]{6,16}$";
    Pattern phonePattern = Pattern.compile(phoneRules);
    Pattern passwordPattern = Pattern.compile(passwordRules);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        ButterKnife.bind(this);

        SMSSDK.registerEventHandler(new EventHandle());

        CheckNullTextWatcher textWatcher = new CheckNullTextWatcher();
        mEtRegisterPhone.addTextChangedListener(textWatcher);
        mEtRegisterPhone.addTextChangedListener(new PhoneTextWatcher());
        mEtRegisterPassword.addTextChangedListener(textWatcher);
        mEtRegisterVerificationCode.addTextChangedListener(textWatcher);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        SMSSDK.unregisterAllEventHandler();
    }

    @Override
    protected RegisterPresenter createPresenter() {
        return new RegisterPresenter(this);
    }

    @Override
    public void getDataSuccess(RegisterBean bean) {
        SharedPreferencesUtil.saveData(SVTSConstants.userId, bean.getUser().getId());
        SharedPreferencesUtil.saveData(SVTSConstants.nickName, bean.getUser().getName());
        SharedPreferencesUtil.saveData(SVTSConstants.sex, bean.getUser().getGender());
        SharedPreferencesUtil.saveData(SVTSConstants.birthday, bean.getUser().getBirth());
        finish();
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
            SMSSDK.getVerificationCode("86",mEtRegisterPhone.getText().toString());
        }
    }

    @OnClick(R.id.btn_register)
    public void onMBtnRegisterClicked() {
        if (checkPhone() && checkPassword()) {
            /*String phone = mEtRegisterPhone.getText().toString();
            String password = mEtRegisterPassword.getText().toString();
            String code = mEtRegisterVerificationCode.getText().toString();
            int appType = Constants.APP_TYPE;
            String appChannel = WalleChannelReader.getChannel(this);
            String appVersion = Utils.getAppVersionName(this);
            mPresenter.phoneRegister(phone,code,password,appType,appChannel,appVersion);*/
            finish();
        }
    }

    @OnClick(R.id.tv_register_agreement)
    public void onMTvRegisterAgreementClicked() {
    }

    @OnClick(R.id.tv_register_send_over)
    public void onViewClicked() {
    }


    private boolean checkPhone() {
        if (phonePattern.matcher(mEtRegisterPhone.getText().toString()).matches()) {
            return true;
        } else {
            toastShow("手机号格式错误!");
            return false;
        }
    }

    private boolean checkPassword() {
        if (passwordPattern.matcher(mEtRegisterPassword.getText().toString()).matches()) {
            return true;
        } else {
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

    class CheckNullTextWatcher implements TextWatcher {

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            if (mEtRegisterPhone.length() > 0 && mEtRegisterVerificationCode.length() > 0
                    && mEtRegisterPassword.length() > 0) {
                mBtnRegister.setEnabled(true);
            } else {
                mBtnRegister.setEnabled(false);
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
            if (mEtRegisterPhone.length() > 0) {
                mIvRegisterPhoneClear.setVisibility(View.VISIBLE);
            } else {
                mIvRegisterPhoneClear.setVisibility(View.GONE);
            }
            mTvRegisterSendVerification.setVisibility(View.VISIBLE);
            mTvRegisterSendOver.setVisibility(View.GONE);
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

                        } else if (event == SMSSDK.EVENT_GET_VERIFICATION_CODE) {
                            //获取验证码成功
                            mTvRegisterSendVerification.setEnabled(false);
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
                                    mTvRegisterSendVerification.setVisibility(View.GONE);
                                    mTvRegisterSendOver.setVisibility(View.VISIBLE);
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
