package com.zxcx.zhizhe.ui.my.userInfo.userSafety;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.zxcx.zhizhe.R;
import com.zxcx.zhizhe.mvpBase.MvpActivity;
import com.zxcx.zhizhe.ui.loginAndRegister.forget.ForgetPasswordContract;
import com.zxcx.zhizhe.ui.loginAndRegister.forget.ForgetPasswordPresenter;
import com.zxcx.zhizhe.ui.loginAndRegister.login.LoginBean;
import com.zxcx.zhizhe.ui.loginAndRegister.register.SMSCodeVerificationBean;
import com.zxcx.zhizhe.utils.SVTSConstants;
import com.zxcx.zhizhe.utils.SharedPreferencesUtil;

import java.util.regex.Pattern;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnTextChanged;
import cn.smssdk.EventHandler;
import cn.smssdk.SMSSDK;

public class ChangePhoneActivity extends MvpActivity<ForgetPasswordPresenter> implements ForgetPasswordContract.View {


    @BindView(R.id.tv_change_phone_old_phone)
    TextView mTvChangePhoneOldPhone;
    @BindView(R.id.et_change_phone_verification_code)
    EditText mEtChangePhoneVerificationCode;
    @BindView(R.id.tv_change_phone_send_verification)
    TextView mTvChangePhoneSendVerification;
    @BindView(R.id.btn_next)
    Button mBtnNext;
    @BindView(R.id.ll_change_phone_old_phone)
    LinearLayout mLlChangePhoneOldPhone;
    @BindView(R.id.et_change_phone_new_phone)
    EditText mEtChangePhoneNewPhone;
    @BindView(R.id.btn_change_phone)
    Button mBtnChangePhone;
    @BindView(R.id.ll_change_phone_new_phone)
    LinearLayout mLlChangePhoneNewPhone;
    private int count = 60;
    Handler handler = new Handler();
    String phoneRules = "^1\\d{10}$";
    Pattern phonePattern = Pattern.compile(phoneRules);
    private String phone;
    private String verifyKey;
    private String newPhone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_phone);
        ButterKnife.bind(this);
        SMSSDK.registerEventHandler(new EventHandle());

        phone = SharedPreferencesUtil.getString(SVTSConstants.phone,"");
        mTvChangePhoneOldPhone.setText(phone);
    }

    @Override
    public void onBackPressed() {
        if (mLlChangePhoneNewPhone.getVisibility() == View.VISIBLE) {
            mLlChangePhoneNewPhone.setVisibility(View.GONE);
            mLlChangePhoneOldPhone.setVisibility(View.VISIBLE);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public void onDestroy() {
        handler.removeCallbacks(setDjs);
        setDjs = null;
        handler = null;
        super.onDestroy();
        SMSSDK.unregisterAllEventHandler();
    }

    @Override
    protected ForgetPasswordPresenter createPresenter() {
        return new ForgetPasswordPresenter(this);
    }

    @Override
    public void getDataSuccess(LoginBean bean) {
        SharedPreferencesUtil.saveData(SVTSConstants.phone,newPhone);
        finish();
    }

    @Override
    public void getPhoneStatusSuccess(boolean isRegistered) {
        //不需要获取手机号状态
    }

    @Override
    public void smsCodeVerificationSuccess(SMSCodeVerificationBean bean) {
        //验证码验证成功
        verifyKey = bean.getVerifyKey();
        mLlChangePhoneOldPhone.setVisibility(View.GONE);
        mLlChangePhoneNewPhone.setVisibility(View.VISIBLE);
    }

    @OnClick(R.id.iv_change_phone_close)
    public void onMIvForgetCloseClicked() {
        finish();
    }

    @OnClick(R.id.tv_change_phone_send_verification)
    public void onMTvRegisterSendVerificationClicked() {
        //发送验证码
        showLoading();
        SMSSDK.getVerificationCode("86", phone);
    }

    @OnClick(R.id.btn_next)
    public void onMBtnNextClicked() {
        //验证验证码
        mPresenter.smsCodeVerification(phone, mEtChangePhoneVerificationCode.getText().toString());
    }

    @OnClick(R.id.btn_change_phone)
    public void onMBtnRegisterClicked() {
        newPhone = mEtChangePhoneNewPhone.getText().toString();
        mPresenter.changePhone(newPhone, verifyKey);
    }

    @OnTextChanged(R.id.et_change_phone_verification_code)
    public void onRegisterNextTextChange() {
        mBtnNext.setEnabled(mEtChangePhoneVerificationCode.length() > 0);
    }

    @OnTextChanged(R.id.et_change_phone_new_phone)
    public void onRegisterPasswordTextChange() {
        mBtnChangePhone.setEnabled(checkPhone());
    }

    private boolean checkPhone() {
        if (phonePattern.matcher(mEtChangePhoneNewPhone.getText().toString()).matches()) {
            return true;
        } else {
            return false;
        }
    }

    Runnable setDjs = new Runnable() {
        @Override
        public void run() {
            mTvChangePhoneSendVerification.setText(count + " S");
            count--;

            if (count < 0) {
                handler.removeCallbacks(setDjs);
                mTvChangePhoneSendVerification.setEnabled(true);
                mTvChangePhoneSendVerification.setText(R.string.re_get_verification);

                count = 60;

                return;
            }

            handler.postDelayed(setDjs, 1000);
        }
    };

    class EventHandle extends EventHandler {
        @Override
        public void afterEvent(final int event, final int result, final Object data) {
            mActivity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    hideLoading();
                    if (result == SMSSDK.RESULT_COMPLETE) {
                        //回调完成
                        if (event == SMSSDK.EVENT_SUBMIT_VERIFICATION_CODE) {
                            //提交验证码成功

                        } else if (event == SMSSDK.EVENT_GET_VERIFICATION_CODE) {
                            //获取验证码成功
                            mTvChangePhoneSendVerification.setEnabled(false);
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
                                case 477:
                                case 478:
                                    toastShow("获取验证码次数频繁，请稍后重试");
                                    break;
                                default:
                                    toastShow(des);
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
