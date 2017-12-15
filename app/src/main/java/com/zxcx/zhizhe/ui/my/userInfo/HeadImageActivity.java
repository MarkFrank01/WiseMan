package com.zxcx.zhizhe.ui.my.userInfo;

import android.Manifest;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.tbruyelle.rxpermissions2.RxPermissions;
import com.zxcx.zhizhe.R;
import com.zxcx.zhizhe.event.ImageCropSuccessEvent;
import com.zxcx.zhizhe.mvpBase.BaseActivity;
import com.zxcx.zhizhe.mvpBase.BaseRxJava;
import com.zxcx.zhizhe.mvpBase.IPostPresenter;
import com.zxcx.zhizhe.retrofit.AppClient;
import com.zxcx.zhizhe.retrofit.PostSubscriber;
import com.zxcx.zhizhe.utils.FileUtil;
import com.zxcx.zhizhe.utils.ImageLoader;
import com.zxcx.zhizhe.utils.SVTSConstants;
import com.zxcx.zhizhe.utils.SharedPreferencesUtil;
import com.zxcx.zhizhe.utils.ZhiZheUtils;
import com.zxcx.zhizhe.widget.GetPicBottomDialog;
import com.zxcx.zhizhe.widget.OSSDialog;
import com.zxcx.zhizhe.widget.PermissionDialog;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by anm on 2017/12/13.
 */

public class HeadImageActivity extends BaseActivity implements GetPicBottomDialog.GetPicDialogListener,
        IPostPresenter<UserInfoBean>,OSSDialog.OSSUploadListener, OSSDialog.OSSDeleteListener {

    @BindView(R.id.iv_toolbar_right)
    ImageView mIvToolbarRight;
    @BindView(R.id.iv_head_image)
    ImageView mIvHeadImage;

    private OSSDialog mOSSDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_head_image);
        initToolBar("头像");
        ButterKnife.bind(this);

        ViewGroup.LayoutParams layoutParams = mIvHeadImage.getLayoutParams();
        layoutParams.height = layoutParams.width;
        mIvHeadImage.setLayoutParams(layoutParams);

        mIvToolbarRight.setVisibility(View.VISIBLE);
        mIvToolbarRight.setImageResource(R.drawable.iv_card_bag_list);

        String imageUrl = SharedPreferencesUtil.getString(SVTSConstants.imgUrl,"");
        ImageLoader.load(this,imageUrl,R.drawable.iv_my_head_placeholder,mIvHeadImage);

        mOSSDialog = new OSSDialog();
        mOSSDialog.setUploadListener(this);
        mOSSDialog.setDeleteListener(this);
    }

    @Override
    public void onResume() {
        EventBus.getDefault().register(this);
        super.onResume();
    }

    @Override
    public void onPause() {
        EventBus.getDefault().unregister(this);
        super.onPause();
    }

    @OnClick(R.id.iv_toolbar_right)
    public void onViewClicked() {
        RxPermissions rxPermissions = new RxPermissions(this);
        rxPermissions
                .requestEach(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .subscribe(permission -> {
                    if (permission.granted) {
                        // `permission.name` is granted !
                        GetPicBottomDialog getPicBottomDialog = new GetPicBottomDialog();
                        getPicBottomDialog.setListener(HeadImageActivity.this);
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

    @Override
    public void onGetSuccess(GetPicBottomDialog.UriType UriType, Uri uri, String imagePath) {
        String path = FileUtil.getRealFilePathFromUri(mActivity,uri);
        Intent intent = new Intent(mActivity,ClipImageActivity.class);
        intent.putExtra("path",path);
        intent.putExtra("aspectX",1);
        intent.putExtra("aspectY",1);
        startActivity(intent);
    }

    @Subscribe(threadMode = ThreadMode.MAIN,sticky = true)
    public void onMessageEvent(ImageCropSuccessEvent event) {
        uploadImageToOSS(event.getPath());
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
                .compose(BaseRxJava.handleResult())
                .compose(BaseRxJava.io_main_loading(this))
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
        deleteImageFromOSS(imageUrl);
        ZhiZheUtils.saveUserInfo(bean);
        ImageLoader.load(this,bean.getAvatar(),R.drawable.iv_my_head_placeholder,mIvHeadImage);
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

    }

    @Override
    public void deleteFail() {

    }
}
