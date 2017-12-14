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
import com.zxcx.zhizhe.event.ChangeHeadImageEvent;
import com.zxcx.zhizhe.mvpBase.BaseActivity;
import com.zxcx.zhizhe.utils.ImageLoader;
import com.zxcx.zhizhe.utils.SVTSConstants;
import com.zxcx.zhizhe.utils.SharedPreferencesUtil;
import com.zxcx.zhizhe.widget.GetPicBottomDialog;
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
        EventBus.getDefault().register(this);

        ViewGroup.LayoutParams layoutParams = mIvHeadImage.getLayoutParams();
        layoutParams.height = layoutParams.width;
        mIvHeadImage.setLayoutParams(layoutParams);

        mIvToolbarRight.setVisibility(View.VISIBLE);
        mIvToolbarRight.setImageResource(R.drawable.iv_card_bag_list);

        String imageUrl = SharedPreferencesUtil.getString(SVTSConstants.imgUrl,"");
        ImageLoader.load(this,imageUrl,R.drawable.iv_my_head_placeholder,mIvHeadImage);
    }

    @Override
    protected void onDestroy() {
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(ChangeHeadImageEvent event) {
        String imageUrl = SharedPreferencesUtil.getString(SVTSConstants.imgUrl,"");
        ImageLoader.load(this,imageUrl,R.drawable.iv_my_head_placeholder,mIvHeadImage);
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
        Intent intent = new Intent(mActivity,HeadImageCropActivity.class);
        intent.setData(uri);
        startActivity(intent);
    }
}
