package com.zxcx.zhizhe.ui.my.userInfo;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import com.tbruyelle.rxpermissions2.RxPermissions;
import com.zxcx.zhizhe.R;
import com.zxcx.zhizhe.mvpBase.BaseActivity;
import com.zxcx.zhizhe.mvpBase.BaseRxJava;
import com.zxcx.zhizhe.mvpBase.IPostPresenter;
import com.zxcx.zhizhe.retrofit.AppClient;
import com.zxcx.zhizhe.retrofit.PostSubscriber;
import com.zxcx.zhizhe.utils.Constants;
import com.zxcx.zhizhe.utils.FileUtil;
import com.zxcx.zhizhe.utils.ImageLoader;
import com.zxcx.zhizhe.utils.SVTSConstants;
import com.zxcx.zhizhe.utils.SharedPreferencesUtil;
import com.zxcx.zhizhe.utils.ZhiZheUtils;
import com.zxcx.zhizhe.widget.CircleImageView;
import com.zxcx.zhizhe.widget.GetPicBottomDialog;
import com.zxcx.zhizhe.widget.OSSDialog;
import com.zxcx.zhizhe.widget.PermissionDialog;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by anm on 2017/12/13.
 */

public class ChangeHeadImageActivity extends BaseActivity implements GetPicBottomDialog.GetPicDialogListener,
        IPostPresenter<UserInfoBean>,OSSDialog.OSSUploadListener, OSSDialog.OSSDeleteListener {

    @BindView(R.id.iv_head_image)
    CircleImageView mIvHeadImage;

    private OSSDialog mOSSDialog;
    private boolean isChanged = false;
    private String path;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_head_image);
        ButterKnife.bind(this);

        String imageUrl = SharedPreferencesUtil.getString(SVTSConstants.imgUrl,"");
        ImageLoader.load(this,imageUrl,R.drawable.iv_my_head_placeholder,mIvHeadImage);

        mOSSDialog = new OSSDialog();
        mOSSDialog.setUploadListener(this);
        mOSSDialog.setDeleteListener(this);
    }

    @Override
    public void onBackPressed() {
        if (isChanged){
            toastShow(R.string.user_info_change);
        }
        super.onBackPressed();
    }

    @SuppressLint("CheckResult")
    @OnClick(R.id.iv_head_image)
    public void onViewClicked() {
        RxPermissions rxPermissions = new RxPermissions(this);
        rxPermissions
                .requestEach(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .subscribe(permission -> {
                    if (permission.granted) {
                        // `permission.name` is granted !
                        GetPicBottomDialog getPicBottomDialog = new GetPicBottomDialog();
                        Bundle bundle = new Bundle();
                        bundle.putString("title","更换头像");
                        getPicBottomDialog.setArguments(bundle);
                        getPicBottomDialog.setListener(ChangeHeadImageActivity.this);
                        getPicBottomDialog.setNoCrop(true);
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
                });
    }

    @OnClick(R.id.tv_head_save)
    public void onSaveClicked() {
        if (path != null) {
            uploadImageToOSS(path);
        }else {
            onBackPressed();
        }
    }

    @OnClick(R.id.iv_common_close)
    public void onCloseClicked() {
        onBackPressed();
    }

    @Override
    public void onGetSuccess(GetPicBottomDialog.UriType uriType, Uri uri, String imagePath) {
        String path = FileUtil.getRealFilePathFromUri(mActivity,uri);
        if (path == null){
            path = imagePath;
        }
        Intent intent = new Intent(mActivity,ImageCropActivity.class);
        intent.putExtra("path",path);
        intent.putExtra("aspectX",1);
        intent.putExtra("aspectY",1);
        startActivityForResult(intent, Constants.CLIP_IMAGE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && data != null) {
            if (requestCode == Constants.CLIP_IMAGE) {
                //图片裁剪完成
                path = data.getStringExtra("path");
                ImageLoader.load(mActivity,path,mIvHeadImage);
            }
        }
    }

    private void uploadImageToOSS(String path) {
        Bundle bundle = new Bundle();
        bundle.putInt("OSSAction", 1);
        bundle.putString("filePath", path);
        mOSSDialog.setArguments(bundle);
        mOSSDialog.show(getFragmentManager(), "");
    }

    @Override
    public void uploadSuccess(String url) {
        changeImageUrl(url);
    }

    public void changeImageUrl(String imageUrl){
        mDisposable = AppClient.getAPIService().changeUserInfo(imageUrl, null, null, null, null)
                .compose(BaseRxJava.INSTANCE.handleResult())
                .compose(BaseRxJava.INSTANCE.io_main_loading(this))
                .subscribeWith(new PostSubscriber<UserInfoBean>(this) {
                    @Override
                    public void onNext(UserInfoBean bean) {
                        postSuccess(bean);
                    }
                });
    }

    @Override
    public void postSuccess(UserInfoBean bean) {
        String imageUrl = SharedPreferencesUtil.getString(SVTSConstants.imgUrl,"");
        ZhiZheUtils.saveUserInfo(bean);
        ImageLoader.load(this,bean.getAvatar(),R.drawable.iv_my_head_placeholder,mIvHeadImage);
        deleteImageFromOSS(imageUrl);
    }

    @Override
    public void postFail(String msg) {
        toastFail(msg);
    }

    private void deleteImageFromOSS(String oldImageUrl) {
        Bundle bundle = new Bundle();
        bundle.putInt("OSSAction", 2);
        bundle.putString("url", oldImageUrl);
        mOSSDialog.setArguments(bundle);
        mOSSDialog.show(getFragmentManager(), "");
    }

    @Override
    public void deleteSuccess() {
        isChanged = true;
        onBackPressed();
    }

    @Override
    public void deleteFail() {
        isChanged = true;
        onBackPressed();
    }
}
