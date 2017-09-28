package com.zxcx.shitang.ui.card.card.share;

import android.Manifest;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.widget.NestedScrollView;
import android.text.TextPaint;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.tbruyelle.rxpermissions2.Permission;
import com.tbruyelle.rxpermissions2.RxPermissions;
import com.zxcx.shitang.R;
import com.zxcx.shitang.mvpBase.BaseActivity;
import com.zxcx.shitang.utils.FileUtil;
import com.zxcx.shitang.utils.ImageLoader;
import com.zxcx.shitang.utils.ScreenUtils;
import com.zxcx.shitang.widget.GetPicBottomDialog;
import com.zxcx.shitang.widget.PermissionDialog;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;
import jp.wasabeef.blurry.Blurry;

public class DiyShareActivity extends BaseActivity implements GetPicBottomDialog.GetPicDialogListener {


    @BindView(R.id.iv_card_details_back)
    ImageView mIvCardDetailsBack;
    @BindView(R.id.tv_card_details_title)
    TextView mTvCardDetailsTitle;
    @BindView(R.id.tv_card_details_number)
    TextView mTvCardDetailsNumber;
    @BindView(R.id.toolbar)
    RelativeLayout mLlCardDetails;
    @BindView(R.id.cb_card_details_collect)
    CheckBox mCbCardDetailsCollect;
    @BindView(R.id.cb_card_details_like)
    CheckBox mCbCardDetailsLike;
    @BindView(R.id.tv_card_details_share)
    ImageView mTvCardDetailsShare;
    @BindView(R.id.rl_card_details)
    RelativeLayout mRlCardDetails;
    @BindView(R.id.tv_card_details_name)
    TextView mTvCardDetailsName;
    @BindView(R.id.tv_card_details_subhead)
    TextView mTvCardDetailsSubhead;
    @BindView(R.id.iv_card_details)
    ImageView mIvCardDetails;
    @BindView(R.id.ll_diy_share)
    LinearLayout mLlDiyShare;
    @BindView(R.id.tv_card_details_content)
    TextView mTvCardDetailsContent;
    @BindView(R.id.scv_card_details)
    NestedScrollView mScvCardDetails;

    private boolean isFirst = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_diy_share);
        ButterKnife.bind(this);

        TextPaint tp = mTvCardDetailsSubhead.getPaint();
        tp.setFakeBoldText(true);
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus) {
//            TextViewUtils.adjustTvTextSize(mTvCardDetailsContent);
            ViewGroup.LayoutParams para = mIvCardDetails.getLayoutParams();
            para.height = mIvCardDetails.getWidth() * 3 / 4;
            mIvCardDetails.setLayoutParams(para);

            if (isFirst) {
                isFirst = false;
                Blurry.with(this)
                        .radius(10)
                        .sampling(2)
                        .capture(mIvCardDetails)
                        .into(mIvCardDetails);
            }
        }
    }

    @OnClick(R.id.tv_card_details_share)
    public void onShareClicked() {
        Bitmap bitmap = ScreenUtils.getBitmapByView(mScvCardDetails);
        String fileName = FileUtil.getFileName();
        String imagePath = FileUtil.PATH_BASE + fileName;
        FileUtil.saveBitmapToSDCard(bitmap, FileUtil.PATH_BASE, fileName);

        ShareCardDialog shareCardDialog = new ShareCardDialog();
        Bundle bundle = new Bundle();
        bundle.putString("imagePath", imagePath);
        shareCardDialog.setArguments(bundle);
        shareCardDialog.show(getFragmentManager(), "");
    }

    @OnClick(R.id.iv_card_details_back)
    public void onBackClicked() {
        finish();
    }

    @OnClick(R.id.fl_diy_share)
    public void onMFlDiyShareClicked() {
        RxPermissions rxPermissions = new RxPermissions(this);
        rxPermissions
                .requestEach(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .subscribe(new Consumer<Permission>() {
                    @Override
                    public void accept(@NonNull Permission permission) throws Exception {
                        if (permission.granted) {
                            // `permission.name` is granted !
                            GetPicBottomDialog getPicBottomDialog = new GetPicBottomDialog();
                            Bundle bundle = new Bundle();
                            bundle.putInt("cutX",4);
                            bundle.putInt("cutY",3);
                            getPicBottomDialog.setArguments(bundle);
                            getPicBottomDialog.setListener(DiyShareActivity.this);
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
    public void onGetSuccess(GetPicBottomDialog.UriType uriType, Uri uri, String imagePath) {
        ImageLoader.loadWithClear(this,uri,R.drawable.iv_my_head_icon,mIvCardDetails);
        mLlDiyShare.setVisibility(View.GONE);
    }
}
