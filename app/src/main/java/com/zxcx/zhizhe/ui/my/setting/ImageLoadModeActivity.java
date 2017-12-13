package com.zxcx.zhizhe.ui.my.setting;

import android.os.Bundle;
import android.widget.CheckBox;

import com.zxcx.zhizhe.R;
import com.zxcx.zhizhe.mvpBase.BaseActivity;
import com.zxcx.zhizhe.utils.SVTSConstants;
import com.zxcx.zhizhe.utils.SharedPreferencesUtil;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnCheckedChanged;
import butterknife.OnClick;

/**
 * Created by anm on 2017/12/13.
 */

public class ImageLoadModeActivity extends BaseActivity {
    @BindView(R.id.cb_image_load_mode_wifi)
    CheckBox mCbImageLoadModeWifi;
    @BindView(R.id.cb_image_load_mode_all)
    CheckBox mCbImageLoadModeAll;
    private int imageLoadMode;  //0-仅WIFI下加载高清图，1-所有网络加载高清图，2-永远不加载高清图

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_load_mode);
        ButterKnife.bind(this);

        imageLoadMode = SharedPreferencesUtil.getInt(SVTSConstants.imageLoadMode,0);
        switch (imageLoadMode){
            case 0:
                mCbImageLoadModeWifi.setChecked(true);
                break;
            case 1:
                mCbImageLoadModeAll.setChecked(true);
                break;
            case 2:
                mCbImageLoadModeWifi.setChecked(false);
                mCbImageLoadModeAll.setChecked(false);
                break;
        }
    }

    @OnClick(R.id.ll_image_load_mode_wifi)
    public void onMLlImageLoadModeWifiClicked() {
        mCbImageLoadModeWifi.setChecked(!mCbImageLoadModeWifi.isChecked());
    }

    @OnClick(R.id.ll_image_load_mode_all)
    public void onMLlImageLoadModeAllClicked() {
        mCbImageLoadModeAll.setChecked(!mCbImageLoadModeAll.isChecked());
    }

    @OnCheckedChanged(R.id.cb_image_load_mode_wifi)
    public void onCbWifiChanged() {
        if (mCbImageLoadModeWifi.isChecked()){
            imageLoadMode = 0;
            SharedPreferencesUtil.saveData(SVTSConstants.imageLoadMode,imageLoadMode);
            mCbImageLoadModeAll.setChecked(false);
        }else {
            if (!mCbImageLoadModeWifi.isChecked()&&!mCbImageLoadModeAll.isChecked()){
                imageLoadMode = 2;
                SharedPreferencesUtil.saveData(SVTSConstants.imageLoadMode,imageLoadMode);
            }
        }
    }

    @OnCheckedChanged(R.id.cb_image_load_mode_all)
    public void onCbAllChanged() {
        if (mCbImageLoadModeAll.isChecked()){
            imageLoadMode = 1;
            SharedPreferencesUtil.saveData(SVTSConstants.imageLoadMode,imageLoadMode);
            mCbImageLoadModeWifi.setChecked(false);
        }else {
            if (!mCbImageLoadModeWifi.isChecked()&&!mCbImageLoadModeAll.isChecked()){
                imageLoadMode = 2;
                SharedPreferencesUtil.saveData(SVTSConstants.imageLoadMode,imageLoadMode);
            }
        }
    }


}
