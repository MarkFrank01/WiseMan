package com.zxcx.zhizhe.ui.my.userInfo;

import android.Manifest;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.TextView;

import com.makeramen.roundedimageview.RoundedImageView;
import com.tbruyelle.rxpermissions2.Permission;
import com.tbruyelle.rxpermissions2.RxPermissions;
import com.zxcx.zhizhe.R;
import com.zxcx.zhizhe.event.ChangeBirthdayDialogEvent;
import com.zxcx.zhizhe.event.ChangeNickNameDialogEvent;
import com.zxcx.zhizhe.event.ChangeSexDialogEvent;
import com.zxcx.zhizhe.event.LogoutEvent;
import com.zxcx.zhizhe.event.UpdataUserInfoEvent;
import com.zxcx.zhizhe.mvpBase.MvpActivity;
import com.zxcx.zhizhe.ui.my.userInfo.userSafety.UserSafetyActivity;
import com.zxcx.zhizhe.utils.DateTimeUtils;
import com.zxcx.zhizhe.utils.FileUtil;
import com.zxcx.zhizhe.utils.ImageLoader;
import com.zxcx.zhizhe.utils.SVTSConstants;
import com.zxcx.zhizhe.utils.SharedPreferencesUtil;
import com.zxcx.zhizhe.utils.StringUtils;
import com.zxcx.zhizhe.utils.ZhiZheUtils;
import com.zxcx.zhizhe.widget.CustomDatePicker;
import com.zxcx.zhizhe.widget.GetPicBottomDialog;
import com.zxcx.zhizhe.widget.OSSDialog;
import com.zxcx.zhizhe.widget.PermissionDialog;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;
import top.zibin.luban.Luban;
import top.zibin.luban.OnCompressListener;

public class UserInfoActivity extends MvpActivity<UserInfoPresenter> implements UserInfoContract.View,
        GetPicBottomDialog.GetPicDialogListener , OSSDialog.OSSUploadListener, OSSDialog.OSSDealeteListener{

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
    private File imageFile;
    private int mUserId;
    private OSSDialog mOSSDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_info);
        ButterKnife.bind(this);
        EventBus.getDefault().register(this);
        initToolBar(R.string.title_user_info);

        initData();
        mOSSDialog = new OSSDialog();
        mOSSDialog.setUploadListener(this);
    }

    private void initData() {
        mUserId = SharedPreferencesUtil.getInt(SVTSConstants.userId, 0);
        mHeadImg = SharedPreferencesUtil.getString(SVTSConstants.imgUrl, "");
        ImageLoader.load(mActivity,mHeadImg,R.drawable.iv_my_head_placeholder,mIvUserInfoHead);
        mNickName = SharedPreferencesUtil.getString(SVTSConstants.nickName, "");
        mTvUserInfoNickName.setText(mNickName);
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

    @Override
    public void onBackPressed() {
        EventBus.getDefault().post(new UpdataUserInfoEvent());
        super.onBackPressed();
    }

    @Override
    protected UserInfoPresenter createPresenter() {
        return new UserInfoPresenter(this);
    }

    @Override
    public void changeImageSuccess(UserInfoBean bean) {
        deleteImageFromOSS(mHeadImg);
        ZhiZheUtils.saveUserInfo(bean);
        initData();
    }

    @Override
    public void postSuccess(UserInfoBean bean) {
        ZhiZheUtils.saveUserInfo(bean);
        initData();
    }

    @Override
    public void postFail(String msg) {
        toastShow(msg);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(LogoutEvent event) {
        finish();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(ChangeNickNameDialogEvent event) {
        UserInfoBean bean = event.getUserInfoBean();
        ZhiZheUtils.saveUserInfo(bean);
        initData();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(ChangeSexDialogEvent event) {
        UserInfoBean bean = event.getUserInfoBean();
        ZhiZheUtils.saveUserInfo(bean);
        initData();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(ChangeBirthdayDialogEvent event) {
        UserInfoBean bean = event.getUserInfoBean();
        ZhiZheUtils.saveUserInfo(bean);
        initData();
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
        mBirth = mBirth + " 00:00";
        /*ChangeBirthdayDialog changeBirthdayDialog = new ChangeBirthdayDialog();
        Bundle bundle = new Bundle();
        bundle.putInt("year", year);
        bundle.putInt("month", month);
        bundle.putInt("day", day);
        changeBirthdayDialog.setArguments(bundle);
        changeBirthdayDialog.show(getFragmentManager(), "");*/
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.CHINA);
        String now = sdf.format(new Date());
        CustomDatePicker customDatePicker1 = new CustomDatePicker(this, new CustomDatePicker.ResultHandler() {
            @Override
            public void handle(String time) { // 回调接口，获得选中的时间
                String date = time.split(" ")[0];
                mPresenter.changeBirth(date);
            }
        }, "1900-01-01 00:00", now); // 初始化日期格式请用：yyyy-MM-dd HH:mm，否则不能正常运行
        customDatePicker1.showSpecificTime(false); // 不显示时和分
        customDatePicker1.setIsLoop(false); // 不允许循环滚动
        customDatePicker1.show(mBirth);
    }

    @OnClick(R.id.rl_user_info_safety)
    public void onMRlUserInfoSafetyClicked() {
        Intent intent = new Intent(this, UserSafetyActivity.class);
        startActivity(intent);
    }

    @Override
    public void onGetSuccess(GetPicBottomDialog.UriType UriType, Uri uri, String imagePath) {
        String path;
        if (UriType == GetPicBottomDialog.UriType.file){
            path = imagePath;
        }else {
            if (uri.getScheme().equals("content")){
                path = FileUtil.getImagePathFromUriOnKitKat(uri);
            }else {
                toastShow("获取图片出错");
                return;
            }
        }
        imageFile = new File(path);
        Luban.with(this)
                .load(imageFile)                     //传入要压缩的图片
                .setCompressListener(new OnCompressListener() { //设置回调
                    @Override
                    public void onStart() {
                        // 压缩开始前调用，可以在方法内启动 loading UI
                    }
                    @Override
                    public void onSuccess(File file) {
                        //  压缩成功后调用，返回压缩后的图片文件
                        imageFile = file;
                        uploadImageToOSS();
                    }

                    @Override
                    public void onError(Throwable e) {
                        //  当压缩过程出现问题时调用
                    }
                }).launch();    //启动压缩
    }

    private void uploadImageToOSS() {
        Bundle bundle = new Bundle();
        bundle.putInt("OSSAction", 1);
        bundle.putString("filePath", imageFile.getPath());
        mOSSDialog.setArguments(bundle);
        mOSSDialog.show(getFragmentManager(), "");
    }

    private void deleteImageFromOSS(String oldImageUrl) {
        Bundle bundle = new Bundle();
        bundle.putInt("OSSAction", 2);
        bundle.putString("url", oldImageUrl);
        mOSSDialog.setArguments(bundle);
        mOSSDialog.show(getFragmentManager(), "");
    }

    @Override
    public void uploadSuccess(String url) {
        SharedPreferencesUtil.saveData(SVTSConstants.imgUrl,url);
        mPresenter.changeImageUrl(url);
        ImageLoader.loadWithClear(mActivity,imageFile, R.drawable.iv_my_head_icon,mIvUserInfoHead);
    }

    @Override
    public void deleteSuccess() {

    }
}
