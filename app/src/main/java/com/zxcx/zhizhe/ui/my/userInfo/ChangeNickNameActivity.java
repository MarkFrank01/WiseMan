package com.zxcx.zhizhe.ui.my.userInfo;

import android.os.Bundle;
import android.os.Handler;
import android.widget.EditText;
import android.widget.TextView;

import com.zxcx.zhizhe.R;
import com.zxcx.zhizhe.mvpBase.BaseActivity;
import com.zxcx.zhizhe.mvpBase.BaseRxJava;
import com.zxcx.zhizhe.mvpBase.IPostPresenter;
import com.zxcx.zhizhe.retrofit.AppClient;
import com.zxcx.zhizhe.retrofit.PostSubscriber;
import com.zxcx.zhizhe.utils.SVTSConstants;
import com.zxcx.zhizhe.utils.SharedPreferencesUtil;
import com.zxcx.zhizhe.utils.Utils;
import com.zxcx.zhizhe.utils.ZhiZheUtils;

import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnTextChanged;

/**
 * Created by anm on 2017/7/13.
 */

public class ChangeNickNameActivity extends BaseActivity implements IPostPresenter<UserInfoBean> {

    @BindView(R.id.et_dialog_change_nick_name)
    EditText mEtDialogChangeNickName;
    @BindView(R.id.tv_complete)
    TextView mTvComplete;
    private String name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_nick_name);
        ButterKnife.bind(this);

        name = SharedPreferencesUtil.getString(SVTSConstants.nickName,"");
        mEtDialogChangeNickName.setText(name);
        mEtDialogChangeNickName.setSelection(mEtDialogChangeNickName.length());

        //延迟弹出软键盘
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Utils.showInputMethod(mEtDialogChangeNickName);
            }
        }, 100);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Utils.closeInputMethod(mEtDialogChangeNickName);
    }

    @Override
    public void postSuccess(UserInfoBean bean) {
        ZhiZheUtils.saveUserInfo(bean);
        toastShow(R.string.user_info_change);
        onBackPressed();
    }

    @Override
    public void postFail(String msg) {
        toastFail(msg);
    }

    @OnClick(R.id.tv_cancel)
    public void onMTvCancelClicked() {
        onBackPressed();
    }

    @OnClick(R.id.tv_complete)
    public void onMTvCompleteClicked() {
        if (!Objects.equals(mEtDialogChangeNickName.getText().toString(), name)) {
            changeNickName(mEtDialogChangeNickName.getText().toString());
        }else {
            toastShow(R.string.user_info_change);
            onBackPressed();
        }
    }

    @OnTextChanged(R.id.et_dialog_change_nick_name)
    public void onEtNickNameChanged(){
        if (mEtDialogChangeNickName.length() > 0){
            mTvComplete.setEnabled(true);
        }else {
            mTvComplete.setEnabled(false);
        }
    }

    public void changeNickName(String name) {
        mDisposable = AppClient.getAPIService().changeUserInfo(null, name, null, null, null)
                .compose(BaseRxJava.INSTANCE.<UserInfoBean>handleResult())
                .compose(BaseRxJava.INSTANCE.<UserInfoBean>io_main_loading(this))
                .subscribeWith(new PostSubscriber<UserInfoBean>(this) {
                    @Override
                    public void onNext(UserInfoBean bean) {
                        postSuccess(bean);
                    }
                });
        addSubscription(mDisposable);
    }
}
