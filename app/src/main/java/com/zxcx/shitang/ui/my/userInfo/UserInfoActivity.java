package com.zxcx.shitang.ui.my.userInfo;

import android.Manifest;
import android.net.Uri;
import android.os.Bundle;
import android.widget.TextView;

import com.alibaba.sdk.android.oss.ClientException;
import com.alibaba.sdk.android.oss.OSS;
import com.alibaba.sdk.android.oss.OSSClient;
import com.alibaba.sdk.android.oss.ServiceException;
import com.alibaba.sdk.android.oss.callback.OSSCompletedCallback;
import com.alibaba.sdk.android.oss.common.auth.OSSCredentialProvider;
import com.alibaba.sdk.android.oss.common.auth.OSSStsTokenCredentialProvider;
import com.alibaba.sdk.android.oss.internal.OSSAsyncTask;
import com.alibaba.sdk.android.oss.model.PutObjectRequest;
import com.alibaba.sdk.android.oss.model.PutObjectResult;
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
import com.zxcx.shitang.utils.FileUtil;
import com.zxcx.shitang.utils.ImageLoader;
import com.zxcx.shitang.utils.MD5Utils;
import com.zxcx.shitang.utils.SVTSConstants;
import com.zxcx.shitang.utils.SharedPreferencesUtil;
import com.zxcx.shitang.utils.StringUtils;
import com.zxcx.shitang.widget.CustomDatePicker;
import com.zxcx.shitang.widget.GetPicBottomDialog;
import com.zxcx.shitang.widget.PermissionDialog;

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
    private File imageFile;
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
        mUserId = SharedPreferencesUtil.getInt(SVTSConstants.imgUrl, 0);
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
        EventBus.getDefault().post(new UpdataUserInfoEvent());
        super.onBackPressed();
    }

    @Override
    protected UserInfoPresenter createPresenter() {
        return new UserInfoPresenter(this);
    }

    @Override
    public void getDataSuccess(OSSTokenBean bean) {

        uploadImageToOSS(bean);
    }

    @Override
    public void postSuccess(UserInfoBean bean) {
        saveData(bean);
        initData();
    }

    @Override
    public void postFail(String msg) {
        toastShow(msg);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(ChangeNickNameDialogEvent event) {
        UserInfoBean bean = event.getUserInfoBean();
        saveData(bean);
        initData();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(ChangeSexDialogEvent event) {
        UserInfoBean bean = event.getUserInfoBean();
        saveData(bean);
        initData();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(ChangeBirthdayDialogEvent event) {
        UserInfoBean bean = event.getUserInfoBean();
        saveData(bean);
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

    @OnClick(R.id.tv_user_info_logout)
    public void onMTvUserInfoLogoutClicked() {
        LogoutDialog dialog = new LogoutDialog();
        dialog.show(getFragmentManager(), "");
    }

    private void saveData(UserInfoBean bean) {
        SharedPreferencesUtil.saveData(SVTSConstants.userId, bean.getId());
        SharedPreferencesUtil.saveData(SVTSConstants.nickName, bean.getName());
        SharedPreferencesUtil.saveData(SVTSConstants.sex, bean.getGender());
        SharedPreferencesUtil.saveData(SVTSConstants.birthday, bean.getBirth());
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
                    }

                    @Override
                    public void onError(Throwable e) {
                        //  当压缩过程出现问题时调用
                    }
                }).launch();    //启动压缩
        mPresenter.getOSS(MD5Utils.md5(String.valueOf(DateTimeUtils.getNowTimestamp())));
// task.cancel(); // 可以取消任务

    }

    private void uploadImageToOSS(OSSTokenBean bean) {
        final String fileName = "user/" + mUserId + FileUtil.getFileName();
        final String bucketName = "zhizhe";
        final String endpoint = "http://oss-cn-shenzhen.aliyuncs.com";
// 在移动端建议使用STS方式初始化OSSClient。更多鉴权模式请参考后面的`访问控制`章节

        OSSCredentialProvider credentialProvider = new OSSStsTokenCredentialProvider(bean.getAccessKeyId(), bean.getAccessKeySecret(), bean.getSecurityToken());
        OSS oss = new OSSClient(getApplicationContext(), endpoint, credentialProvider);
        // 构造上传请求
        PutObjectRequest put = new PutObjectRequest(bucketName, fileName, imageFile.getPath());
        OSSAsyncTask task = oss.asyncPutObject(put, new OSSCompletedCallback<PutObjectRequest, PutObjectResult>() {
            @Override
            public void onSuccess(PutObjectRequest request, PutObjectResult result) {
                String imageUrl = "http://zhizhe.oss-cn-shenzhen.aliyuncs.com/"+fileName;
                SharedPreferencesUtil.saveData(SVTSConstants.imgUrl,imageUrl);
                mPresenter.changeImageUrl(imageUrl);
                ImageLoader.loadWithClear(mActivity,imageFile, R.drawable.iv_my_head_icon,mIvUserInfoHead);
            }
            @Override
            public void onFailure(PutObjectRequest request, ClientException clientExcepion, ServiceException serviceException) {
                // 请求异常
                if (clientExcepion != null) {
                    // 本地异常如网络异常等
                    clientExcepion.printStackTrace();
                }
                if (serviceException != null) {
                    // 服务异常
                }
            }
        });
        task.waitUntilFinished(); // 可以等待直到任务完成
    }
}
