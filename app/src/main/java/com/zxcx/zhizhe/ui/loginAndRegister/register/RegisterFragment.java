package com.zxcx.zhizhe.ui.loginAndRegister.register;

import android.os.Bundle;
import android.os.Handler;
import android.support.percent.PercentRelativeLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.gyf.barlibrary.ImmersionBar;
import com.meituan.android.walle.WalleChannelReader;
import com.zxcx.zhizhe.R;
import com.zxcx.zhizhe.event.PhoneConfirmEvent;
import com.zxcx.zhizhe.event.RegisterEvent;
import com.zxcx.zhizhe.mvpBase.FragmentBackHandler;
import com.zxcx.zhizhe.mvpBase.MvpFragment;
import com.zxcx.zhizhe.ui.loginAndRegister.login.LoginBean;
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
import butterknife.Unbinder;
import cn.jpush.android.api.JPushInterface;
import cn.smssdk.EventHandler;
import cn.smssdk.SMSSDK;

public class RegisterFragment extends MvpFragment<RegisterPresenter> implements RegisterContract.View, FragmentBackHandler {

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
    @BindView(R.id.btn_next)
    Button mBtnNext;
    @BindView(R.id.prl_register_phone)
    PercentRelativeLayout mPrlRegisterPhone;
    @BindView(R.id.prl_register_password)
    PercentRelativeLayout mPrlRegisterPassword;

    private int count = 60;
    Handler handler = new Handler();
    String phoneRules = "^1\\d{10}$";
    String passwordRules = "^.{8,20}$";
    Pattern phonePattern = Pattern.compile(phoneRules);
    Pattern passwordPattern = Pattern.compile(passwordRules);
    private Unbinder unbinder;
    private String jpushRID;
    private String verifyKey;
    private ImmersionBar mImmersionBar;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_register, container, false);
        unbinder = ButterKnife.bind(this, root);
        return root;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        EventBus.getDefault().register(this);
        initStatusBar();

        jpushRID = JPushInterface.getRegistrationID(mActivity);
        SMSSDK.registerEventHandler(new EventHandle());
    }

    public void initStatusBar(){
        mImmersionBar = ImmersionBar.with(this)
                .keyboardEnable(true);
        if (!Constants.IS_NIGHT) {
            mImmersionBar
                    .statusBarColor(R.color.background)
                    .statusBarDarkFont(true, 0.2f)
                    .flymeOSStatusBarFontColor(R.color.text_color_1);
        } else {
            mImmersionBar
                    .statusBarColor(R.color.background)
                    .flymeOSStatusBarFontColor(R.color.text_color_1);
        }
        mImmersionBar.init();
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        if (hidden) {
            EventBus.getDefault().unregister(this);
        } else {
            EventBus.getDefault().register(this);
        }
    }

    @Override
    public boolean onBackPressed() {
        if (mPrlRegisterPassword.getVisibility() == View.VISIBLE) {
            mPrlRegisterPassword.setVisibility(View.GONE);
            mPrlRegisterPhone.setVisibility(View.VISIBLE);
            return true;
        }
        return false;
    }

    @Override
    public void onDestroy() {
        handler.removeCallbacks(setDjs);
        setDjs = null;
        handler = null;
        unbinder.unbind();
        if (mImmersionBar != null)
            mImmersionBar.destroy();
        EventBus.getDefault().unregister(this);
        super.onDestroy();
        SMSSDK.unregisterAllEventHandler();
    }

    @Override
    protected RegisterPresenter createPresenter() {
        return new RegisterPresenter(this);
    }

    @Override
    public void getDataSuccess(LoginBean bean) {
        ZhiZheUtils.saveLoginData(bean);
        EventBus.getDefault().post(new RegisterEvent());
    }

    @Override
    public void getPhoneStatusSuccess(boolean isRegistered) {
        if (isRegistered) {
            //手机号已注册提示框
            PhoneRegisteredDialog registeredDialog = new PhoneRegisteredDialog();
            Bundle bundle = new Bundle();
            bundle.putString("phone", mEtRegisterPhone.getText().toString());
            registeredDialog.setArguments(bundle);
            registeredDialog.show(mActivity.getFragmentManager(), "");
        } else {
            //手机号确认提示框
            PhoneConfirmDialog confirmDialog = new PhoneConfirmDialog();
            Bundle bundle = new Bundle();
            bundle.putString("phone", mEtRegisterPhone.getText().toString());
            confirmDialog.setArguments(bundle);
            confirmDialog.show(mActivity.getFragmentManager(), "");
        }
    }

    @Override
    public void smsCodeVerificationSuccess(SMSCodeVerificationBean bean) {
        verifyKey = bean.getVerifyKey();
        mPrlRegisterPhone.setVisibility(View.GONE);
        mPrlRegisterPassword.setVisibility(View.VISIBLE);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(PhoneConfirmEvent event) {
        //手机号确认成功,发送验证码
        showLoading();
        SMSSDK.getVerificationCode("86", mEtRegisterPhone.getText().toString());
    }

    @OnClick(R.id.iv_register_phone_clear)
    public void onMIvRegisterPhoneClearClicked() {
        mEtRegisterPhone.setText("");
    }

    @OnClick(R.id.tv_register_send_verification)
    public void onMTvRegisterSendVerificationClicked() {
        mPresenter.checkPhoneRegistered(mEtRegisterPhone.getText().toString());
    }

    @OnClick(R.id.btn_next)
    public void onMBtnNextClicked() {
        //验证验证码
        mPresenter.smsCodeVerification(mEtRegisterPhone.getText().toString()
                , mEtRegisterVerificationCode.getText().toString());
    }

    @OnClick(R.id.btn_register)
    public void onMBtnRegisterClicked() {
        String phone = mEtRegisterPhone.getText().toString();
        String password = MD5Utils.md5(mEtRegisterPassword.getText().toString());
        int appType = Constants.APP_TYPE;
        String appChannel = WalleChannelReader.getChannel(mActivity);
        String appVersion = Utils.getAppVersionName(mActivity);
        mPresenter.phoneRegister(phone, verifyKey, jpushRID, password, appType, appChannel, appVersion);
    }

    @OnTextChanged({R.id.et_register_phone, R.id.et_register_verification_code})
    public void onRegisterNextTextChange() {
        mBtnNext.setEnabled(checkPhone() && mEtRegisterVerificationCode.length() > 0);
    }

    @OnTextChanged(R.id.et_register_password)
    public void onRegisterPasswordTextChange() {
        mBtnRegister.setEnabled(checkPassword());
    }

    @OnTextChanged(R.id.et_register_phone)
    public void onRegisterPhoneTextChange() {
        mTvRegisterSendVerification.setEnabled(checkPhone());
        if (mEtRegisterPhone.length() > 0) {
            mIvRegisterPhoneClear.setVisibility(View.VISIBLE);
        } else {
            mIvRegisterPhoneClear.setVisibility(View.GONE);
        }
    }

    private boolean checkPhone() {
        if (phonePattern.matcher(mEtRegisterPhone.getText().toString()).matches()) {
            return true;
        } else {
            return false;
        }
    }

    private boolean checkPassword() {
        if (passwordPattern.matcher(mEtRegisterPassword.getText().toString()).matches()) {
            return true;
        } else {
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
