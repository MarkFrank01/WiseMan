package com.zxcx.zhizhe.ui.my.userInfo.userSafety;

import android.os.Bundle;
import android.os.Handler;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.zxcx.zhizhe.R;
import com.zxcx.zhizhe.mvpBase.BaseActivity;
import com.zxcx.zhizhe.mvpBase.BaseRxJava;
import com.zxcx.zhizhe.mvpBase.INullPostPresenter;
import com.zxcx.zhizhe.retrofit.AppClient;
import com.zxcx.zhizhe.retrofit.BaseBean;
import com.zxcx.zhizhe.retrofit.NullPostSubscriber;
import com.zxcx.zhizhe.ui.loginAndRegister.forget.SMSSendOverDialog;
import com.zxcx.zhizhe.utils.SVTSConstants;
import com.zxcx.zhizhe.utils.SharedPreferencesUtil;

import java.util.regex.Pattern;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnTextChanged;
import cn.smssdk.EventHandler;
import cn.smssdk.SMSSDK;

public class ChangePhoneActivity extends BaseActivity implements INullPostPresenter{

    @BindView(R.id.tv_toolbar_right)
    TextView mTvToolbarRight;
    @BindView(R.id.tv_change_phone_phone)
    TextView mTvChangePhonePhone;
    @BindView(R.id.et_change_phone_phone)
    EditText mEtChangePhonePhone;
    @BindView(R.id.et_change_phone_verification_code)
    EditText mEtChangePhoneVerificationCode;
    @BindView(R.id.tv_register_send_verification)
    TextView mTvRegisterSendVerification;
    @BindView(R.id.tv_register_send_over)
    TextView mTvRegisterSendOver;
    private int count = 60;
    Handler handler = new Handler();
    String phoneRules = "^1\\d{10}$";
    Pattern phonePattern = Pattern.compile(phoneRules);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_phone);
        ButterKnife.bind(this);

        mTvToolbarRight.setVisibility(View.VISIBLE);
        mTvToolbarRight.setText(R.string.common_complete);
        mTvToolbarRight.setTextColor(ContextCompat.getColor(this,R.color.button_blue));

        String phone = SharedPreferencesUtil.getString(SVTSConstants.phone,"");
        mTvChangePhonePhone.setText(phone);

        SMSSDK.registerEventHandler(new EventHandle());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        SMSSDK.unregisterAllEventHandler();
    }

    @OnClick(R.id.tv_forget_password_send_over)
    public void onViewClicked() {
        SMSSendOverDialog dialog = new SMSSendOverDialog();
        dialog.show(getFragmentManager(), "ForgerPasswordActivity");
    }

    @OnClick(R.id.tv_toolbar_right)
    public void onMTvToolbarRightClicked() {
        //todo 修改手机号
        changePhone(mEtChangePhonePhone.getText().toString(),mEtChangePhoneVerificationCode.getText().toString());
    }

    @OnClick(R.id.tv_register_send_verification)
    public void onMTvRegisterSendVerificationClicked() {
        SMSSDK.getVerificationCode("86", mEtChangePhonePhone.getText().toString());
    }

    @OnClick(R.id.tv_register_send_over)
    public void onMTvRegisterSendOverClicked() {
        SMSSendOverDialog dialog = new SMSSendOverDialog();
        dialog.show(getFragmentManager(), "ChangePhoneActivity");
    }

    @OnTextChanged(R.id.et_change_phone_phone)
    public void onPhoneChanged() {
        mTvRegisterSendVerification.setVisibility(View.VISIBLE);
        mTvRegisterSendOver.setVisibility(View.GONE);
        if (checkPhone()){
            mTvRegisterSendVerification.setEnabled(true);
        }else {
            mTvRegisterSendVerification.setEnabled(false);
        }
    }

    @OnTextChanged({R.id.et_change_phone_phone,R.id.et_change_phone_verification_code})
    public void onChangePhoneEditTextChanged() {
        if (checkPhone() && mEtChangePhoneVerificationCode.length() > 0) {
            mTvToolbarRight.setEnabled(true);
        } else {
            mTvToolbarRight.setEnabled(false);
        }
    }

    public void changePhone(String phone, String code) {
        mDisposable = AppClient.getAPIService().changePhone(phone, code)
                .compose(BaseRxJava.handlePostResult())
                .compose(BaseRxJava.<BaseBean>io_main_loading(this))
                .subscribeWith(new NullPostSubscriber<BaseBean>(this) {
                    @Override
                    public void onNext(BaseBean bean) {
                        postSuccess();
                    }
                });
        addSubscription(mDisposable);
    }

    @Override
    public void postSuccess() {
        toastShow("修改成功");
        finish();
    }

    @Override
    public void postFail(String msg) {
        toastFail(msg);
    }

    private boolean checkPhone() {
        if (phonePattern.matcher(mEtChangePhonePhone.getText().toString()).matches()) {
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
