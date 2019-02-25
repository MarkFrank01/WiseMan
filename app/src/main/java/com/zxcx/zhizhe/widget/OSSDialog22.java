package com.zxcx.zhizhe.widget;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

import com.alibaba.sdk.android.oss.ClientException;
import com.alibaba.sdk.android.oss.OSS;
import com.alibaba.sdk.android.oss.OSSClient;
import com.alibaba.sdk.android.oss.ServiceException;
import com.alibaba.sdk.android.oss.callback.OSSCompletedCallback;
import com.alibaba.sdk.android.oss.callback.OSSProgressCallback;
import com.alibaba.sdk.android.oss.common.auth.OSSCredentialProvider;
import com.alibaba.sdk.android.oss.common.auth.OSSStsTokenCredentialProvider;
import com.alibaba.sdk.android.oss.internal.OSSAsyncTask;
import com.alibaba.sdk.android.oss.model.DeleteObjectRequest;
import com.alibaba.sdk.android.oss.model.DeleteObjectResult;
import com.alibaba.sdk.android.oss.model.PutObjectRequest;
import com.alibaba.sdk.android.oss.model.PutObjectResult;
import com.zxcx.zhizhe.R;
import com.zxcx.zhizhe.mvpBase.BaseDialog;
import com.zxcx.zhizhe.mvpBase.BaseRxJava;
import com.zxcx.zhizhe.mvpBase.IGetPresenter;
import com.zxcx.zhizhe.retrofit.AppClient;
import com.zxcx.zhizhe.retrofit.BaseSubscriber;
import com.zxcx.zhizhe.ui.my.userInfo.OSSTokenBean;
import com.zxcx.zhizhe.utils.DateTimeUtils;
import com.zxcx.zhizhe.utils.FileUtil;
import com.zxcx.zhizhe.utils.LogCat;
import com.zxcx.zhizhe.utils.MD5Utils;
import com.zxcx.zhizhe.utils.SVTSConstants;
import com.zxcx.zhizhe.utils.ScreenUtils;
import com.zxcx.zhizhe.utils.SharedPreferencesUtil;
import com.zxcx.zhizhe.utils.StringUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by anm on 2017/5/27.
 * OSS图片上传弹窗封装
 */

public class OSSDialog22 extends BaseDialog implements IGetPresenter<OSSTokenBean> {

    @BindView(R.id.iv_loading)
    ImageView mIvLoading;
    Unbinder unbinder;
    private int mOSSAction; //1-上传，2-删除
    private String mFolderName;
    private String mFilePath;
    private String mUrl;
    private OSSTokenBean mOSSTokenBean;
    private OSSUploadListener mUploadListener;
    private OSSDeleteListener mDeleteListener;

    private List<String> urls = new ArrayList<>();

    public void setUploadListener(OSSUploadListener uploadListener) {
        mUploadListener = uploadListener;
    }

    public void setDeleteListener(OSSDeleteListener deleteListener) {
        mDeleteListener = deleteListener;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             Bundle savedInstanceState) {
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        View view = inflater.inflate(R.layout.layout_oss_loading, container);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
//		((AnimationDrawable) mIvLoading.getDrawable()).start();
        setCancelable(false);

        mOSSAction = getArguments().getInt("OSSAction");
        mFilePath = getArguments().getString("filePath");
        mFolderName = getArguments().getString("folderName");

        urls = getArguments().getStringArrayList("photoList");

        if (StringUtils.isEmpty(mFolderName)) {
            mFolderName = "user/";
        }
        mUrl = getArguments().getString("url");
        if (mOSSTokenBean == null) {
            getOSS(MD5Utils.md5(String.valueOf(DateTimeUtils.getNowTimestamp())));
        } else {
            getDataSuccess(mOSSTokenBean);
        }
    }

    @Override
    public void onStart() {
        super.onStart();

        Dialog dialog = getDialog();
        if (dialog != null) {
            Window window = dialog.getWindow();
            window.setBackgroundDrawableResource(R.color.translate);
            WindowManager.LayoutParams lp = window.getAttributes();
            lp.dimAmount = 0.0f;
            lp.width = ScreenUtils.dip2px(140);
            lp.height = ScreenUtils.dip2px(140);
            lp.gravity = Gravity.CENTER;
            window.setAttributes(lp);
        }
    }

    @Override
    public void onDestroyView() {
//		((AnimationDrawable) mIvLoading.getDrawable()).stop();
        super.onDestroyView();
        unbinder.unbind();
    }

    public void getOSS(String uuid) {
        mDisposable = AppClient.getAPIService().getOSS(uuid)
                .compose(BaseRxJava.INSTANCE.handleResult())
                .compose(BaseRxJava.INSTANCE.io_main())
                .subscribeWith(new BaseSubscriber<OSSTokenBean>(this) {
                    @Override
                    public void onNext(OSSTokenBean bean) {
                        getDataSuccess(bean);
                    }
                });
        addSubscription(mDisposable);
    }

    @Override
    public void getDataSuccess(OSSTokenBean bean) {
        mOSSTokenBean = bean;
        if (mOSSAction == 1) {
            uploadFileToOSS(mFilePath);
        } else if (mOSSAction == 2) {
            deleteImageFromOSS(mUrl);
        } else if (mOSSAction == 3) {
            ossUpload(urls);
        }
    }

    @Override
    public void getDataFail(String msg) {
        toastShow(msg);
        dismiss();
    }

    private void uploadFileToOSS(String filePath) {
        int userId = SharedPreferencesUtil.getInt(SVTSConstants.userId, 0);
        final String fileName = mFolderName + userId + FileUtil.getRandomImageName();
        final String bucketName = getString(R.string.bucket_name);
        final String endpoint = "http://oss-cn-shenzhen.aliyuncs.com";
        // 在移动端建议使用STS方式初始化OSSClient。更多鉴权模式请参考后面的`访问控制`章节

        OSSCredentialProvider credentialProvider = new OSSStsTokenCredentialProvider(
                mOSSTokenBean.getAccessKeyId(), mOSSTokenBean.getAccessKeySecret(),
                mOSSTokenBean.getSecurityToken());
        OSS oss = new OSSClient(getActivity().getApplicationContext(), endpoint,
                credentialProvider);
        // 构造上传请求
        PutObjectRequest put = new PutObjectRequest(bucketName, fileName, filePath);
        OSSAsyncTask task = oss
                .asyncPutObject(put, new OSSCompletedCallback<PutObjectRequest, PutObjectResult>() {
                    @Override
                    public void onSuccess(PutObjectRequest request, PutObjectResult result) {
                        final String imageUrl =
                                "http://" + bucketName + ".oss-cn-shenzhen.aliyuncs.com/" + fileName;
                        if (mUploadListener != null) {
                            getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    mUploadListener.uploadSuccess(imageUrl);
                                }
                            });
                        }
                        dismiss();
                    }

                    @Override
                    public void onFailure(PutObjectRequest request, ClientException clientExcepion,
                                          ServiceException serviceException) {
                        String message = "上传失败";
                        // 请求异常
                        if (clientExcepion != null) {
                            // 本地异常如网络异常等
                            clientExcepion.printStackTrace();
                            message = clientExcepion.getMessage();
                        }
                        if (serviceException != null) {
                            // 服务异常
                            message = serviceException.getMessage();
                        }
                        if (mUploadListener != null) {
                            String finalMessage = message;
                            getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    mUploadListener.uploadFail(finalMessage);
                                }
                            });
                        }
                        dismiss();
                    }
                });
    }

    private void deleteImageFromOSS(String url) {
        final String bucketName = getString(R.string.bucket_name);
        final String endpoint = "http://oss-cn-shenzhen.aliyuncs.com";
        final String fileName;
        try {
            fileName = url.split("http://" + bucketName + ".oss-cn-shenzhen.aliyuncs.com/")[1];
        } catch (Exception e) {
            e.printStackTrace();
            if (mDeleteListener != null) {
                mDeleteListener.deleteFail();
            }
            dismiss();
            return;
        }

        // 在移动端建议使用STS方式初始化OSSClient。更多鉴权模式请参考后面的`访问控制`章节
        OSSCredentialProvider credentialProvider = new OSSStsTokenCredentialProvider(
                mOSSTokenBean.getAccessKeyId(), mOSSTokenBean.getAccessKeySecret(),
                mOSSTokenBean.getSecurityToken());
        OSS oss = new OSSClient(getActivity().getApplicationContext(), endpoint,
                credentialProvider);
        // 构造删除请求
        DeleteObjectRequest deleteObjectRequest = new DeleteObjectRequest(bucketName, fileName);
        OSSAsyncTask task = oss.asyncDeleteObject(deleteObjectRequest,
                new OSSCompletedCallback<DeleteObjectRequest, DeleteObjectResult>() {
                    @Override
                    public void onSuccess(DeleteObjectRequest request, DeleteObjectResult result) {
                        if (mDeleteListener != null) {
                            getActivity().runOnUiThread(() -> mDeleteListener.deleteSuccess());
                        }
                        dismiss();
                    }

                    @Override
                    public void onFailure(DeleteObjectRequest request, ClientException clientExcepion,
                                          ServiceException serviceException) {
                        // 请求异常
                        if (clientExcepion != null) {
                            // 本地异常如网络异常等
                            clientExcepion.printStackTrace();
                        }
                        if (serviceException != null) {
                            // 服务异常
                        }
                        if (mDeleteListener != null) {
                            getActivity().runOnUiThread(() -> mDeleteListener.deleteFail());
                        }
                        dismiss();
                    }

                });
    }


    //尝试使用递归体上传多图
    private void ossUpload(final List<String> urls) {

        int userId = SharedPreferencesUtil.getInt(SVTSConstants.userId, 0);
        final String fileName = mFolderName + userId + FileUtil.getRandomImageName();
        final String bucketName = getString(R.string.bucket_name);
        final String endpoint = "http://oss-cn-shenzhen.aliyuncs.com";
        // 在移动端建议使用STS方式初始化OSSClient。更多鉴权模式请参考后面的`访问控制`章节

        OSSCredentialProvider credentialProvider = new OSSStsTokenCredentialProvider(
                mOSSTokenBean.getAccessKeyId(), mOSSTokenBean.getAccessKeySecret(),
                mOSSTokenBean.getSecurityToken());
        OSS oss = new OSSClient(getActivity().getApplicationContext(), endpoint,
                credentialProvider);

        if (urls.size() <= 0) {
            // 文件全部上传完毕，这里编写上传结束的逻辑，如果要在主线程操作，最好用Handler或runOnUiThead做对应逻辑
            return;// 这个return必须有，否则下面报越界异常，原因自己思考下哈
        }
        final String url = urls.get(0);
        if (TextUtils.isEmpty(url)) {
            urls.remove(0);
            // url为空就没必要上传了，这里做的是跳过它继续上传的逻辑。
            ossUpload(urls);
            return;
        }

        File file = new File(url);
        if (null == file || !file.exists()) {
            urls.remove(0);
            // 文件为空或不存在就没必要上传了，这里做的是跳过它继续上传的逻辑。
            ossUpload(urls);
            return;
        }
        // 文件后缀
        String fileSuffix = "";
        if (file.isFile()) {
            // 获取文件后缀名
            fileSuffix = file.getName().substring(file.getName().lastIndexOf("."));
        }
        // 文件标识符objectKey
        final String objectKey = "alioss_" + System.currentTimeMillis() + fileSuffix;
        // 下面3个参数依次为bucket名，ObjectKey名，上传文件路径
        PutObjectRequest put = new PutObjectRequest(bucketName, objectKey, url);

        // 设置进度回调
        put.setProgressCallback(new OSSProgressCallback<PutObjectRequest>() {
            @Override
            public void onProgress(PutObjectRequest request, long currentSize, long totalSize) {
                // 进度逻辑
            }
        });
        // 异步上传
        OSSAsyncTask task = oss.asyncPutObject(put,
                new OSSCompletedCallback<PutObjectRequest, PutObjectResult>() {
                    @Override
                    public void onSuccess(PutObjectRequest request, PutObjectResult result) { // 上传成功
                        mUploadListener.uploadSuccess(urls.get(0));
                        urls.remove(0);
                        ossUpload(urls);// 递归同步效果

                        LogCat.e("嘤嘤嘤");
                    }

                    @Override
                    public void onFailure(PutObjectRequest request, ClientException clientExcepion,
                                          ServiceException serviceException) { // 上传失败

                        // 请求异常
                        if (clientExcepion != null) {
                            // 本地异常如网络异常等
                            clientExcepion.printStackTrace();
                        }
                        if (serviceException != null) {
                            // 服务异常
                            Log.e("ErrorCode", serviceException.getErrorCode());
                            Log.e("RequestId", serviceException.getRequestId());
                            Log.e("HostId", serviceException.getHostId());
                            Log.e("RawMessage", serviceException.getRawMessage());
                        }
                        dismiss();
                    }
                });
        task.waitUntilFinished(); // 可以等待直到任务完成
        dismiss();
        // task.cancel(); // 可以取消任务
    }


    public interface OSSUploadListener {

        void uploadSuccess(String url);

        void uploadFail(String message);
    }

    public interface OSSDeleteListener {

        void deleteSuccess();

        void deleteFail();
    }
}
