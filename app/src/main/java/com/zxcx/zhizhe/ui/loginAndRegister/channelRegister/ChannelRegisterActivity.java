package com.zxcx.zhizhe.ui.loginAndRegister.channelRegister;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.meituan.android.walle.WalleChannelReader;
import com.zxcx.zhizhe.R;
import com.zxcx.zhizhe.event.PhoneConfirmEvent;
import com.zxcx.zhizhe.event.RegisterEvent;
import com.zxcx.zhizhe.mvpBase.MvpActivity;
import com.zxcx.zhizhe.ui.loginAndRegister.forget.ForgetPasswordContract;
import com.zxcx.zhizhe.ui.loginAndRegister.forget.ForgetPasswordPresenter;
import com.zxcx.zhizhe.ui.loginAndRegister.forget.PhoneUnRegisteredDialog;
import com.zxcx.zhizhe.ui.loginAndRegister.login.LoginBean;
import com.zxcx.zhizhe.ui.loginAndRegister.register.PhoneConfirmDialog;
import com.zxcx.zhizhe.ui.loginAndRegister.register.SMSCodeVerificationBean;
import com.zxcx.zhizhe.utils.Constants;
import com.zxcx.zhizhe.utils.MD5Utils;
import com.zxcx.zhizhe.utils.Utils;
import com.zxcx.zhizhe.utils.ZhiZheUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.regex.Pattern;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnTextChanged;
import cn.jpush.android.api.JPushInterface;
import cn.smssdk.EventHandler;
import cn.smssdk.SMSSDK;

public class ChannelRegisterActivity extends MvpActivity<ForgetPasswordPresenter> implements ForgetPasswordContract.View {

    @BindView(R.id.iv_forget_close)
    ImageView mIvForgetClose;
    @BindView(R.id.et_forget_phone)
    EditText mEtForgetPhone;
    @BindView(R.id.iv_forget_phone_clear)
    ImageView mIvForgetPhoneClear;
    @BindView(R.id.et_forget_verification_code)
    EditText mEtForgetVerificationCode;
    @BindView(R.id.tv_forget_send_verification)
    TextView mTvForgetSendVerification;
    @BindView(R.id.btn_next)
    Button mBtnNext;
    @BindView(R.id.ll_forget_phone)
    LinearLayout mLlForgetPhone;
    @BindView(R.id.et_forget_password)
    EditText mEtForgetPassword;
    @BindView(R.id.btn_forget)
    Button mBtnForget;
    @BindView(R.id.ll_forget_password)
    LinearLayout mLlForgetPassword;
    @BindView(R.id.tv_forget_title)
    TextView mTvForgetTitle;
    private int count = 60;
    Handler handler = new Handler();
    String phoneRules = "^1\\d{10}$";
    String passwordRules = "^.{6,16}$";
    Pattern phonePattern = Pattern.compile(phoneRules);
    Pattern passwordPattern = Pattern.compile(passwordRules);
    private String verifyKey,jpushRID,appChannel,appVersion,userId,userName,userIcon,userGender;
    private int channelType,appType,sex;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_password);
        ButterKnife.bind(this);
        jpushRID = JPushInterface.getRegistrationID(mActivity);
        SMSSDK.registerEventHandler(new EventHandle());

        //第三方注册和忘记密码界面的差异
        mTvForgetTitle.setText("绑定手机号");
        mBtnForget.setText("绑定");
        initData();
    }

    private void initData() {
        appType = Constants.APP_TYPE;
        appChannel = WalleChannelReader.getChannel(this);
        appVersion = Utils.getAppVersionName(this);
        Intent intent = getIntent();
        userId = intent.getStringExtra("userId");
        userName = intent.getStringExtra("userName");
        userIcon = intent.getStringExtra("userIcon");
        userGender = intent.getStringExtra("userGender");
        channelType = intent.getIntExtra("channelType", 1);

        if ("m".equals(userGender)) {
            sex = 1;
        } else {
            sex = 2;
        }
    }

    @Override
    public void onBackPressed() {
        if (mLlForgetPassword.getVisibility() == View.VISIBLE) {
            mLlForgetPassword.setVisibility(View.GONE);
            mLlForgetPhone.setVisibility(View.VISIBLE);
            mTvForgetTitle.setText("绑定手机号");
        }
    }

    @Override
    public void onDestroy() {
        EventBus.getDefault().unregister(this);
        super.onDestroy();
        SMSSDK.unregisterAllEventHandler();
    }

    @Override
    protected ForgetPasswordPresenter createPresenter() {
        return new ForgetPasswordPresenter(this);
    }

    @Override
    public void getDataSuccess(LoginBean bean) {
        ZhiZheUtils.saveLoginData(bean);
        EventBus.getDefault().post(new RegisterEvent());
        finish();
    }

    @Override
    public void getPhoneStatusSuccess(boolean isRegistered) {
        if (isRegistered) {
            //手机号确认提示框
            PhoneConfirmDialog confirmDialog = new PhoneConfirmDialog();
            Bundle bundle = new Bundle();
            bundle.putString("phone", mEtForgetPhone.getText().toString());
            confirmDialog.setArguments(bundle);
            confirmDialog.show(mActivity.getFragmentManager(), "");
        } else {
            //手机号未注册提示框
            PhoneUnRegisteredDialog registeredDialog = new PhoneUnRegisteredDialog();
            Bundle bundle = new Bundle();
            bundle.putString("phone", mEtForgetPhone.getText().toString());
            registeredDialog.setArguments(bundle);
            registeredDialog.show(mActivity.getFragmentManager(), "");
        }
    }

    @Override
    public void smsCodeVerificationSuccess(SMSCodeVerificationBean bean) {
        verifyKey = bean.getVerifyKey();
        mLlForgetPhone.setVisibility(View.GONE);
        mLlForgetPassword.setVisibility(View.VISIBLE);
        mTvForgetTitle.setText("设置密码");
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(PhoneConfirmEvent event) {
        //手机号确认成功,发送验证码
        showLoading();
        SMSSDK.getVerificationCode("86", mEtForgetPhone.getText().toString());
    }

    @OnClick(R.id.iv_forget_phone_clear)
    public void onMIvRegisterPhoneClearClicked() {
        mEtForgetPhone.setText("");
    }

    @OnClick(R.id.tv_forget_send_verification)
    public void onMTvRegisterSendVerificationClicked() {
        mPresenter.checkPhoneRegistered(mEtForgetPhone.getText().toString());
    }

    @OnClick(R.id.btn_next)
    public void onMBtnNextClicked() {
        //验证验证码
        mPresenter.smsCodeVerification(mEtForgetPhone.getText().toString()
                , mEtForgetVerificationCode.getText().toString());
    }

    @OnClick(R.id.btn_forget)
    public void onMBtnRegisterClicked() {
        String phone = mEtForgetPhone.getText().toString();
        String password = MD5Utils.md5(mEtForgetPassword.getText().toString());
        mPresenter.channelRegister(channelType, userId, password, userIcon, userName, sex,
                null, phone, verifyKey, jpushRID, appType, appChannel, appVersion);
    }

    @OnTextChanged({R.id.et_forget_phone, R.id.et_forget_verification_code})
    public void onRegisterNextTextChange() {
        mBtnNext.setEnabled(checkPhone() && mEtForgetVerificationCode.length() > 0);
    }

    @OnTextChanged(R.id.et_forget_password)
    public void onRegisterPasswordTextChange() {
        mBtnForget.setEnabled(checkPassword());
    }

    @OnTextChanged(R.id.et_forget_phone)
    public void onRegisterPhoneTextChange() {
        mTvForgetSendVerification.setEnabled(checkPhone());
        if (mEtForgetPhone.length() > 0) {
            mIvForgetPhoneClear.setVisibility(View.VISIBLE);
        } else {
            mIvForgetPhoneClear.setVisibility(View.GONE);
        }
    }

    private boolean checkPhone() {
        if (phonePattern.matcher(mEtForgetPhone.getText().toString()).matches()) {
            return true;
        } else {
            toastShow("手机号格式错误!");
            return false;
        }
    }

    private boolean checkPassword() {
        if (passwordPattern.matcher(mEtForgetPassword.getText().toString()).matches()) {
            return true;
        } else {
            toastShow("密码格式错误!");
            return false;
        }
    }

    Runnable setDjs = new Runnable() {
        @Override
        public void run() {
            mTvForgetSendVerification.setText(count + " S");
            count--;

            if (count < 0) {
                handler.removeCallbacks(setDjs);
                mTvForgetSendVerification.setEnabled(true);
                mTvForgetSendVerification.setText(R.string.re_get_verification);

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
                            mTvForgetSendVerification.setEnabled(false);
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
