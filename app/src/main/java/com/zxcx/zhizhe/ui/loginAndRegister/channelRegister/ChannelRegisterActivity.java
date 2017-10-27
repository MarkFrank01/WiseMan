package com.zxcx.zhizhe.ui.loginAndRegister.channelRegister;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextPaint;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.meituan.android.walle.WalleChannelReader;
import com.zxcx.zhizhe.R;
import com.zxcx.zhizhe.event.RegisterEvent;
import com.zxcx.zhizhe.mvpBase.MvpActivity;
import com.zxcx.zhizhe.ui.loginAndRegister.forget.SMSSendOverDialog;
import com.zxcx.zhizhe.ui.loginAndRegister.login.LoginBean;
import com.zxcx.zhizhe.ui.my.selectAttention.SelectAttentionActivity;
import com.zxcx.zhizhe.utils.Constants;
import com.zxcx.zhizhe.utils.MD5Utils;
import com.zxcx.zhizhe.utils.Utils;
import com.zxcx.zhizhe.utils.ZhiZheUtils;

import org.greenrobot.eventbus.EventBus;

import java.util.regex.Pattern;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.smssdk.EventHandler;
import cn.smssdk.SMSSDK;

public class ChannelRegisterActivity extends MvpActivity<ChannelRegisterPresenter> implements ChannelRegisterContract.View {


    @BindView(R.id.iv_register_close)
    ImageView mIvRegisterClose;
    @BindView(R.id.et_register_phone)
    EditText mEtRegisterPhone;
    @BindView(R.id.iv_register_phone_clear)
    ImageView mIvRegisterPhoneClear;
    @BindView(R.id.et_register_verification_code)
    EditText mEtRegisterVerificationCode;
    @BindView(R.id.tv_register_send_verification)
    TextView mTvRegisterSendVerification;
    @BindView(R.id.tv_register_send_over)
    TextView mTvRegisterSendOver;
    @BindView(R.id.btn_register)
    Button mBtnRegister;
    @BindView(R.id.et_register_password)
    EditText mEtRegisterPassword;

    private int count = 60;
    Handler handler = new Handler();
    String phoneRules = "^1\\d{10}$";
    String passwordRules = "^[a-zA-Z0-9]{6,16}$";
    Pattern phonePattern = Pattern.compile(phoneRules);
    Pattern passwordPattern = Pattern.compile(passwordRules);
    private int channelType; // 1-QQ 2-WeChat 3-Weibo
    private int appType, sex;
    private String userName, userId, userIcon, userGender, appChannel, appVersion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_channel_register);
        ButterKnife.bind(this);

        SMSSDK.registerEventHandler(new EventHandle());

        CheckNullTextWatcher textWatcher = new CheckNullTextWatcher();
        mEtRegisterPhone.addTextChangedListener(textWatcher);
        mEtRegisterPhone.addTextChangedListener(new PhoneTextWatcher());
        mEtRegisterVerificationCode.addTextChangedListener(textWatcher);
        mEtRegisterPassword.addTextChangedListener(textWatcher);
        TextPaint paint = mBtnRegister.getPaint();
        paint.setFakeBoldText(true);

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
    protected void onDestroy() {
        super.onDestroy();
        SMSSDK.unregisterAllEventHandler();
        handler.removeCallbacks(setDjs);
    }

    @Override
    protected ChannelRegisterPresenter createPresenter() {
        return new ChannelRegisterPresenter(this);
    }

    @Override
    public void getDataSuccess(LoginBean bean) {
        ZhiZheUtils.saveLoginData(bean);
        EventBus.getDefault().post(new RegisterEvent());
        Intent intent = new Intent(this, SelectAttentionActivity.class);
        startActivity(intent);
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
            showLoading();
            SMSSDK.getVerificationCode("86", mEtRegisterPhone.getText().toString());
        }
    }

    @OnClick(R.id.btn_register)
    public void onMBtnRegisterClicked() {
        if (checkPhone() && checkPassword()) {
            String phone = mEtRegisterPhone.getText().toString();
            String code = mEtRegisterVerificationCode.getText().toString();
            String password = MD5Utils.md5(mEtRegisterPassword.getText().toString());
            mPresenter.channelRegister(channelType, userId, password, userIcon, userName, sex, null, phone, code, appType, appChannel, appVersion);
        }
    }

    @OnClick(R.id.tv_register_agreement)
    public void onMTvRegisterAgreementClicked() {
    }

    @OnClick(R.id.tv_register_send_over)
    public void onViewClicked() {
        SMSSendOverDialog dialog = new SMSSendOverDialog();
        dialog.show(getFragmentManager(), "");
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
                mTvRegisterSendVerification.setText(R.string.re_get_verification);

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
            if (mEtRegisterPhone.length() > 0 && mEtRegisterVerificationCode.length() > 0 && mEtRegisterPassword.length() > 0) {
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
                    hideLoading();
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
                                case 477:
                                case 478:
                                    mTvRegisterSendVerification.setVisibility(View.GONE);
                                    mTvRegisterSendOver.setVisibility(View.VISIBLE);
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
