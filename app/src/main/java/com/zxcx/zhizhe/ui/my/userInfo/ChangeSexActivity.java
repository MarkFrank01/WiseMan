package com.zxcx.zhizhe.ui.my.userInfo;

import android.os.Bundle;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.zxcx.zhizhe.R;
import com.zxcx.zhizhe.mvpBase.BaseActivity;
import com.zxcx.zhizhe.mvpBase.BaseRxJava;
import com.zxcx.zhizhe.mvpBase.IPostPresenter;
import com.zxcx.zhizhe.retrofit.AppClient;
import com.zxcx.zhizhe.retrofit.BaseBean;
import com.zxcx.zhizhe.retrofit.PostSubscriber;
import com.zxcx.zhizhe.utils.SVTSConstants;
import com.zxcx.zhizhe.utils.SharedPreferencesUtil;
import com.zxcx.zhizhe.utils.ZhiZheUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by anm on 2017/7/13.
 */

public class ChangeSexActivity extends BaseActivity implements IPostPresenter<UserInfoBean> {

    @BindView(R.id.rb_change_sex_man)
    RadioButton mRbChangeSexMan;
    @BindView(R.id.rb_change_sex_woman)
    RadioButton mRbChangeSexWoman;
    @BindView(R.id.rg_change_sex)
    RadioGroup mRgChangeSex;
    @BindView(R.id.tv_dialog_cancel)
    TextView mTvDialogCancel;
    @BindView(R.id.tv_dialog_confirm)
    TextView mTvDialogConfirm;
    private int sex;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_sex);
        ButterKnife.bind(this);

        sex = SharedPreferencesUtil.getInt(SVTSConstants.sex,1);
        mRbChangeSexMan.setChecked(sex == 1);
    }

    @OnClick(R.id.tv_complete)
    public void onMTvDialogConfirmClicked() {
        sex = mRbChangeSexMan.isChecked() ? 1 : 0;
        changeSex(sex);
    }

    public void changeSex(int sex) {
        mDisposable = AppClient.getAPIService().changeUserInfo(null, null, sex, null, null)
                .compose(BaseRxJava.<BaseBean<UserInfoBean>>io_main())
                .compose(BaseRxJava.<UserInfoBean>handleResult())
                .subscribeWith(new PostSubscriber<UserInfoBean>(this) {
                    @Override
                    public void onNext(UserInfoBean bean) {
                        postSuccess(bean);
                    }
                });
        addSubscription(mDisposable);
    }

    @Override
    public void postSuccess(UserInfoBean bean) {
        ZhiZheUtils.saveUserInfo(bean);
        onBackPressed();
    }

    @Override
    public void postFail(String msg) {
        toastShow(msg);
    }
}
