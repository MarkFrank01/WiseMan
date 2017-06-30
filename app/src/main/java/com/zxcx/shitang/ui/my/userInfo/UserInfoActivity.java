package com.zxcx.shitang.ui.my.userInfo;

import android.os.Bundle;
import android.widget.TextView;

import com.zxcx.shitang.R;
import com.zxcx.shitang.mvpBase.MvpActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class UserInfoActivity extends MvpActivity<UserInfoPresenter> implements UserInfoContract.View {

    @BindView(R.id.tv_user_info_head)
    TextView mTvUserInfoHead;
    @BindView(R.id.tv_user_info_nick_name)
    TextView mTvUserInfoNickName;
    @BindView(R.id.tv_user_info_sex)
    TextView mTvUserInfoSex;
    @BindView(R.id.tv_user_info_birthday)
    TextView mTvUserInfoBirthday;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_info);
        ButterKnife.bind(this);
        initToolBar(R.string.title_user_info);
    }

    @Override
    protected UserInfoPresenter createPresenter() {
        return new UserInfoPresenter(this);
    }

    @Override
    public void getDataSuccess(UserInfoBean bean) {

    }

    @OnClick(R.id.rl_user_info_head)
    public void onMRlUserInfoHeadClicked() {
    }

    @OnClick(R.id.rl_user_info_nick_name)
    public void onMRlUserInfoNickNameClicked() {
    }

    @OnClick(R.id.rl_user_info_sex)
    public void onMRlUserInfoSexClicked() {
    }

    @OnClick(R.id.rl_user_info_birthday)
    public void onMRlUserInfoBirthdayClicked() {
    }

    @OnClick(R.id.tv_user_info_logout)
    public void onMTvUserInfoLogoutClicked() {
    }
}
