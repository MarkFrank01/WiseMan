package com.zxcx.zhizhe.ui.my.userInfo;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.makeramen.roundedimageview.RoundedImageView;
import com.zxcx.zhizhe.R;
import com.zxcx.zhizhe.event.LogoutEvent;
import com.zxcx.zhizhe.event.UserInfoChangeSuccessEvent;
import com.zxcx.zhizhe.mvpBase.BaseActivity;
import com.zxcx.zhizhe.ui.my.selectAttention.SelectAttentionActivity;
import com.zxcx.zhizhe.ui.my.userInfo.userSafety.UserSafetyActivity;
import com.zxcx.zhizhe.utils.ImageLoader;
import com.zxcx.zhizhe.utils.SVTSConstants;
import com.zxcx.zhizhe.utils.SharedPreferencesUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class UserInfoActivity extends BaseActivity {


    @BindView(R.id.tv_user_info_nick_name)
    TextView mTvUserInfoNickName;
    @BindView(R.id.tv_user_info_sex)
    TextView mTvUserInfoSex;
    @BindView(R.id.tv_user_info_birthday)
    TextView mTvUserInfoBirthday;
    @BindView(R.id.iv_user_info_head)
    RoundedImageView mIvUserInfoHead;
    @BindView(R.id.tv_user_info_signature)
    TextView mTvUserInfoSignature;

    private String mHeadImg;
    private String mNickName;
    private String mSignture;
    private int mSex;
    private String mBirth;
    private int mUserId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_info);
        ButterKnife.bind(this);
        EventBus.getDefault().register(this);
        initToolBar(R.string.title_user_info);

        initData();
    }

    private void initData() {
        mUserId = SharedPreferencesUtil.getInt(SVTSConstants.userId, 0);
        mHeadImg = SharedPreferencesUtil.getString(SVTSConstants.imgUrl, "");
        ImageLoader.load(mActivity, mHeadImg, R.drawable.default_header, mIvUserInfoHead);
        mNickName = SharedPreferencesUtil.getString(SVTSConstants.nickName, "");
        mSignture = SharedPreferencesUtil.getString(SVTSConstants.signture, "");
        mTvUserInfoNickName.setText(mNickName);
        mTvUserInfoSignature.setText(mSignture);
        mSex = SharedPreferencesUtil.getInt(SVTSConstants.sex, 1);
        if (mSex == 1) {
            mTvUserInfoSex.setText("男");
        } else {
            mTvUserInfoSex.setText("女");
        }
        mBirth = SharedPreferencesUtil.getString(SVTSConstants.birthday, "");
        mTvUserInfoBirthday.setText(mBirth);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(LogoutEvent event) {
        finish();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(UserInfoChangeSuccessEvent event) {
        initData();
    }

    @OnClick(R.id.ll_user_info_head)
    public void onMLlUserInfoHeadClicked() {
        //头像修改页面
        Intent intent = new Intent(mActivity,ChangeHeadImageActivity.class);
        startActivity(intent);
    }

    @OnClick(R.id.ll_user_info_nick_name)
    public void onMLlUserInfoNickNameClicked() {
        //昵称修改页面
        Intent intent = new Intent(mActivity,ChangeNickNameActivity.class);
        startActivity(intent);
    }

    @OnClick(R.id.ll_user_info_sex)
    public void onMLlUserInfoSexClicked() {
        //性别修改页面
        Intent intent = new Intent(mActivity,ChangeSexActivity.class);
        startActivity(intent);
    }

    @OnClick(R.id.ll_user_info_birthday)
    public void onMLlUserInfoBirthdayClicked() {
        //生日修改页面
        Intent intent = new Intent(mActivity,ChangeBirthdayActivity.class);
        startActivity(intent);
    }

    @OnClick(R.id.ll_user_info_signature)
    public void onMLlUserInfoSignatureClicked() {
        //签名修改页面
        Intent intent = new Intent(mActivity,ChangeSigntureActivity.class);
        startActivity(intent);
    }

    @OnClick(R.id.ll_user_info_attention)
    public void onMLlUserInfoAttentionClicked() {
        //兴趣选择页面
        Intent intent = new Intent(mActivity,SelectAttentionActivity.class);
        startActivity(intent);
    }

    @OnClick(R.id.ll_user_info_safety)
    public void onMLlUserInfoSafetyClicked() {
        //绑定修改页面
        Intent intent = new Intent(this, UserSafetyActivity.class);
        startActivity(intent);
    }

    @OnClick(R.id.ll_user_info_logout)
    public void onMLlUserInfoLogoutClicked() {
        //退出登录弹窗
        LogoutDialog dialog = new LogoutDialog();
        dialog.show(getFragmentManager(),"");
    }
}
