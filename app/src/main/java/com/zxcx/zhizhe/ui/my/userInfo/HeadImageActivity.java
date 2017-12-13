package com.zxcx.zhizhe.ui.my.userInfo;

import android.Manifest;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.widget.ImageView;

import com.tbruyelle.rxpermissions2.Permission;
import com.tbruyelle.rxpermissions2.RxPermissions;
import com.zxcx.zhizhe.R;
import com.zxcx.zhizhe.mvpBase.BaseActivity;
import com.zxcx.zhizhe.utils.FileUtil;
import com.zxcx.zhizhe.utils.ImageLoader;
import com.zxcx.zhizhe.utils.SVTSConstants;
import com.zxcx.zhizhe.utils.SharedPreferencesUtil;
import com.zxcx.zhizhe.widget.GetPicBottomDialog;
import com.zxcx.zhizhe.widget.PermissionDialog;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.functions.Consumer;

/**
 * Created by anm on 2017/12/13.
 */

public class HeadImageActivity extends BaseActivity implements GetPicBottomDialog.GetPicDialogListener{
    @BindView(R.id.iv_toolbar_right)
    ImageView mIvToolbarRight;
    @BindView(R.id.iv_head_image)
    ImageView mIvHeadImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_head_image);
        ButterKnife.bind(this);

        String imageUrl = SharedPreferencesUtil.getString(SVTSConstants.imgUrl,"");
        ImageLoader.load(this,imageUrl,R.drawable.iv_my_head_placeholder,mIvHeadImage);
    }

    @OnClick(R.id.iv_toolbar_right)
    public void onViewClicked() {
        RxPermissions rxPermissions = new RxPermissions(this);
        rxPermissions
                .requestEach(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .subscribe(new Consumer<Permission>() {
                    @Override
                    public void accept(@NonNull Permission permission) throws Exception {
                        if (permission.granted) {
                            // `permission.name` is granted !
                            GetPicBottomDialog getPicBottomDialog = new GetPicBottomDialog();
                            getPicBottomDialog.setListener(HeadImageActivity.this);
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
        }/*
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
                }).launch();    //启动压缩*/
    }
}
