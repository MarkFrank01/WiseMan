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

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnTextChanged;

/**
 * Created by anm on 2017/7/13.
 */

public class ChangeSigntureActivity extends BaseActivity implements IPostPresenter<UserInfoBean> {


    @BindView(R.id.tv_complete)
    TextView mTvComplete;
    @BindView(R.id.et_dialog_change_signture)
    EditText mEtDialogChangeSignture;
    @BindView(R.id.tv_change_signture_residue)
    TextView mTvChangeSigntureResidue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_signture);
        ButterKnife.bind(this);

        String signture = SharedPreferencesUtil.getString(SVTSConstants.nickName, "");
        mEtDialogChangeSignture.setText(signture);
        mEtDialogChangeSignture.setSelection(mEtDialogChangeSignture.length());

        //延迟弹出软键盘
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Utils.showInputMethod(mEtDialogChangeSignture);
            }
        }, 100);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Utils.closeInputMethod(mEtDialogChangeSignture);
    }

    @Override
    public void postSuccess(UserInfoBean bean) {
        ZhiZheUtils.saveUserInfo(bean);
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
        changeNickName(mEtDialogChangeSignture.getText().toString());
    }

    @OnTextChanged(R.id.et_dialog_change_signture)
    public void onEtSigntureChanged() {
        if (mEtDialogChangeSignture.length() > 0) {
            mTvComplete.setEnabled(true);
        } else {
            mTvComplete.setEnabled(false);
        }
        mTvChangeSigntureResidue.setText(String.valueOf(20-mEtDialogChangeSignture.length()));
    }

    public void changeNickName(String name) {
        mDisposable = AppClient.getAPIService().changeUserInfo(null, name, null, null, null)
                .compose(BaseRxJava.<UserInfoBean>handleResult())
                .compose(BaseRxJava.<UserInfoBean>io_main_loading(this))
                .subscribeWith(new PostSubscriber<UserInfoBean>(this) {
                    @Override
                    public void onNext(UserInfoBean bean) {
                        postSuccess(bean);
                    }
                });
        addSubscription(mDisposable);
    }
}
