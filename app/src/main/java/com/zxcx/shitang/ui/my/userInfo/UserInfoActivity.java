package com.zxcx.shitang.ui.my.userInfo;

import android.Manifest;
import android.net.Uri;
import android.os.Bundle;
import android.widget.TextView;

import com.makeramen.roundedimageview.RoundedImageView;
import com.tbruyelle.rxpermissions2.Permission;
import com.tbruyelle.rxpermissions2.RxPermissions;
import com.zxcx.shitang.R;
import com.zxcx.shitang.event.ChangeBirthdayDialogEvent;
import com.zxcx.shitang.event.ChangeNickNameDialogEvent;
import com.zxcx.shitang.event.ChangeSexDialogEvent;
import com.zxcx.shitang.event.UpdataUserInfoEvent;
import com.zxcx.shitang.mvpBase.MvpActivity;
import com.zxcx.shitang.utils.DateTimeUtils;
import com.zxcx.shitang.utils.ImageLoader;
import com.zxcx.shitang.utils.SVTSConstants;
import com.zxcx.shitang.utils.SharedPreferencesUtil;
import com.zxcx.shitang.utils.StringUtils;
import com.zxcx.shitang.widget.GetPicBottomDialog;
import com.zxcx.shitang.widget.PermissionDialog;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;

public class UserInfoActivity extends MvpActivity<UserInfoPresenter> implements UserInfoContract.View,
        GetPicBottomDialog.GetPicDialogListener {

    @BindView(R.id.tv_user_info_head)
    TextView mTvUserInfoHead;
    @BindView(R.id.tv_user_info_nick_name)
    TextView mTvUserInfoNickName;
    @BindView(R.id.tv_user_info_sex)
    TextView mTvUserInfoSex;
    @BindView(R.id.tv_user_info_birthday)
    TextView mTvUserInfoBirthday;
    @BindView(R.id.iv_user_info_head)
    RoundedImageView mIvUserInfoHead;

    private String mHeadImg;
    private String mNickName;
    private int mSex;
    private String mBirth;

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
        mHeadImg = SharedPreferencesUtil.getString(SVTSConstants.imgUrl, "");
        ImageLoader.load(mActivity,mHeadImg,R.drawable.iv_my_head_placeholder,mIvUserInfoHead);
        mNickName = SharedPreferencesUtil.getString(SVTSConstants.nickName, "");
        mTvUserInfoNickName.setText(mNickName);
        mSex = SharedPreferencesUtil.getInt(SVTSConstants.sex, 0);
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

    @Override
    public void onBackPressed() {
        SharedPreferencesUtil.saveData(SVTSConstants.nickName, mNickName);
        SharedPreferencesUtil.saveData(SVTSConstants.sex, mSex);
        SharedPreferencesUtil.saveData(SVTSConstants.birthday, mBirth);
        EventBus.getDefault().post(new UpdataUserInfoEvent());
        super.onBackPressed();
    }

    @Override
    protected UserInfoPresenter createPresenter() {
        return new UserInfoPresenter(this);
    }

    @Override
    public void getDataSuccess(UserInfoBean bean) {

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(ChangeNickNameDialogEvent event) {
        mNickName = event.getNickName();
        mTvUserInfoNickName.setText(mNickName);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(ChangeSexDialogEvent event) {
        mSex = event.getSex();
        if (mSex == 1) {
            mTvUserInfoSex.setText("男");
        } else {
            mTvUserInfoSex.setText("女");
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(ChangeBirthdayDialogEvent event) {
        mBirth = event.getBirthday();
        mTvUserInfoBirthday.setText(mBirth);
    }

    @OnClick(R.id.rl_user_info_head)
    public void onMRlUserInfoHeadClicked() {
        RxPermissions rxPermissions = new RxPermissions(this);
        rxPermissions
                .requestEach(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .subscribe(new Consumer<Permission>() {
                    @Override
                    public void accept(@NonNull Permission permission) throws Exception {
                        if (permission.granted) {
                            // `permission.name` is granted !
                            GetPicBottomDialog getPicBottomDialog = new GetPicBottomDialog();
                            getPicBottomDialog.setListener(UserInfoActivity.this);
                            getPicBottomDialog.show(getFragmentManager(),"UserInfo");
                        } else if (permission.shouldShowRequestPermissionRationale){
                            // Denied permission without ask never again
                            toastShow("权限已被拒绝！无法进行操作");
                        } else {
                            // Denied permission with ask never again
                            // Need to go to the settings
                            PermissionDialog permissionDialog = new PermissionDialog();
                            permissionDialog.show(getFragmentManager(),"");
                        }
                    }
                });
    }

    @OnClick(R.id.rl_user_info_nick_name)
    public void onMRlUserInfoNickNameClicked() {
        ChangeNickNameDialog changeNickNameDialog = new ChangeNickNameDialog();
        changeNickNameDialog.show(getFragmentManager(), "");
    }

    @OnClick(R.id.rl_user_info_sex)
    public void onMRlUserInfoSexClicked() {
        ChangeSexDialog changeSexDialog = new ChangeSexDialog();
        changeSexDialog.show(getFragmentManager(), "");
    }

    @OnClick(R.id.rl_user_info_birthday)
    public void onMRlUserInfoBirthdayClicked() {

        if (StringUtils.isEmpty(mBirth)) {
            mBirth = DateTimeUtils.getDate();
        }

        int year = DateTimeUtils.getYear(mBirth);
        int month = DateTimeUtils.getMonth(mBirth);
        int day = DateTimeUtils.getDay(mBirth);

        ChangeBirthdayDialog changeBirthdayDialog = new ChangeBirthdayDialog();
        Bundle bundle = new Bundle();
        bundle.putInt("year", year);
        bundle.putInt("month", month);
        bundle.putInt("day", day);
        changeBirthdayDialog.setArguments(bundle);
        changeBirthdayDialog.show(getFragmentManager(), "");

    }

    @OnClick(R.id.tv_user_info_logout)
    public void onMTvUserInfoLogoutClicked() {
        LogoutDialog dialog = new LogoutDialog();
        dialog.show(getFragmentManager(), "");
    }

    @Override
    public void onGetSuccess(Uri file) {
        ImageLoader.load(this,file,R.drawable.iv_my_head_icon,mIvUserInfoHead);
    }
}
