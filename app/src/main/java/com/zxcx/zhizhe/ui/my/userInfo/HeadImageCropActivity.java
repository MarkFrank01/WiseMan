package com.zxcx.zhizhe.ui.my.userInfo;

import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;

import com.zxcx.zhizhe.R;
import com.zxcx.zhizhe.event.ChangeHeadImageEvent;
import com.zxcx.zhizhe.mvpBase.BaseActivity;
import com.zxcx.zhizhe.mvpBase.BaseRxJava;
import com.zxcx.zhizhe.mvpBase.IPostPresenter;
import com.zxcx.zhizhe.retrofit.AppClient;
import com.zxcx.zhizhe.retrofit.PostSubscriber;
import com.zxcx.zhizhe.utils.FileUtil;
import com.zxcx.zhizhe.utils.SVTSConstants;
import com.zxcx.zhizhe.utils.SharedPreferencesUtil;
import com.zxcx.zhizhe.utils.ZhiZheUtils;
import com.zxcx.zhizhe.widget.ClipViewLayout;
import com.zxcx.zhizhe.widget.OSSDialog;

import org.greenrobot.eventbus.EventBus;

import java.io.File;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import top.zibin.luban.Luban;
import top.zibin.luban.OnCompressListener;

/**
 * Created by anm on 2017/12/14.
 */

public class HeadImageCropActivity extends BaseActivity implements IPostPresenter<UserInfoBean> ,OSSDialog.OSSUploadListener, OSSDialog.OSSDeleteListener {

    @BindView(R.id.cvl_head_image_crop)
    ClipViewLayout mCvlHeadImageCrop;

    private OSSDialog mOSSDialog;
    private File imageFile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_head_image_crop);
        ButterKnife.bind(this);

        Uri uri = getIntent().getData();
        mCvlHeadImageCrop.initSrcPic(uri);

        mOSSDialog = new OSSDialog();
        mOSSDialog.setUploadListener(this);
        mOSSDialog.setDeleteListener(this);
    }

    @Override
    public void postSuccess(UserInfoBean bean) {
        String imageUrl = SharedPreferencesUtil.getString(SVTSConstants.imgUrl,"");
        deleteImageFromOSS(imageUrl);
        ZhiZheUtils.saveUserInfo(bean);
    }

    @Override
    public void postFail(String msg) {
        toastFail(msg);
    }

    @Override
    public void uploadSuccess(String url) {
        changeImageUrl(url);
    }

    @Override
    public void deleteSuccess() {
        EventBus.getDefault().post(new ChangeHeadImageEvent());
        onBackPressed();
    }

    @Override
    public void deleteFail() {
        EventBus.getDefault().post(new ChangeHeadImageEvent());
        onBackPressed();
    }

    @OnClick(R.id.tv_cancel)
    public void onMTvCancelClicked() {
        onBackPressed();
    }

    @OnClick(R.id.tv_complete)
    public void onMTvCompleteClicked() {
        String fileName = "headImage.png";
        Bitmap bitmap = mCvlHeadImageCrop.clip();
        FileUtil.saveBitmapToSDCard(bitmap,FileUtil.PATH_BASE, fileName);
        String path = FileUtil.PATH_BASE + fileName;
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
}
