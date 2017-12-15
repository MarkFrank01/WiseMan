package com.zxcx.zhizhe.ui.my.userInfo;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapRegionDecoder;
import android.graphics.Matrix;
import android.graphics.Rect;
import android.graphics.RectF;
import android.media.ExifInterface;
import android.os.Bundle;

import com.zxcx.zhizhe.R;
import com.zxcx.zhizhe.event.ImageCropSuccessEvent;
import com.zxcx.zhizhe.mvpBase.BaseActivity;
import com.zxcx.zhizhe.utils.FileUtil;
import com.zxcx.zhizhe.widget.ClipImageView;

import org.greenrobot.eventbus.EventBus;

import java.io.File;
import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import top.zibin.luban.Luban;
import top.zibin.luban.OnCompressListener;

public class ClipImageActivity extends BaseActivity {

    @BindView(R.id.cvl_head_image_crop)
    ClipImageView mCvlHeadImageCrop;

    private String mInput;
    private int mMaxWidth;

    // 图片被旋转的角度
    private int mDegree;
    // 大图被设置之前的缩放比例
    private int mSampleSize;
    private int mSourceWidth;
    private int mSourceHeight;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.activity_head_image_crop);
        ButterKnife.bind(this);

        mInput = getIntent().getStringExtra("path");
        mMaxWidth = getIntent().getIntExtra("maxWidth", 1080);
        mCvlHeadImageCrop.setAspect(getIntent().getIntExtra("aspectX", 1), getIntent().getIntExtra("aspectY", 1));
        mCvlHeadImageCrop.setMaxOutputWidth(mMaxWidth);

        setImageAndClipParams(); //大图裁剪
    }

    @Override
    public void initStatusBar() {

    }

    private void setImageAndClipParams() {
        mCvlHeadImageCrop.post(() -> {
            mCvlHeadImageCrop.setMaxOutputWidth(mMaxWidth);

            mDegree = readPictureDegree(mInput);

            final boolean isRotate = (mDegree == 90 || mDegree == 270);

            final BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = true;
            BitmapFactory.decodeFile(mInput, options);

            mSourceWidth = options.outWidth;
            mSourceHeight = options.outHeight;

            // 如果图片被旋转，则宽高度置换
            int w = isRotate ? options.outHeight : options.outWidth;

            // 裁剪是宽高比例3:2，只考虑宽度情况，这里按border宽度的两倍来计算缩放。
            mSampleSize = findBestSample(w, mCvlHeadImageCrop.getClipBorder().width());

            options.inJustDecodeBounds = false;
            options.inSampleSize = mSampleSize;
            options.inPreferredConfig = Bitmap.Config.RGB_565;
            final Bitmap source = BitmapFactory.decodeFile(mInput, options);

            // 解决图片被旋转的问题
            Bitmap target;
            if (mDegree == 0) {
                target = source;
            } else {
                final Matrix matrix = new Matrix();
                matrix.postRotate(mDegree);
                target = Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(), matrix, false);
                if (target != source && !source.isRecycled()) {
                    source.recycle();
                }
            }
            mCvlHeadImageCrop.setImageBitmap(target);
        });
    }

    /**
     * 计算最好的采样大小。
     *
     * @param origin 当前宽度
     * @param target 限定宽度
     * @return sampleSize
     */
    private static int findBestSample(int origin, int target) {
        int sample = 1;
        for (int out = origin / 2; out > target; out /= 2) {
            sample *= 2;
        }
        return sample;
    }

    /**
     * 读取图片属性：旋转的角度
     *
     * @param path 图片绝对路径
     * @return degree旋转的角度
     */
    public static int readPictureDegree(String path) {
        int degree = 0;
        try {
            ExifInterface exifInterface = new ExifInterface(path);
            int orientation = exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
            switch (orientation) {
                case ExifInterface.ORIENTATION_ROTATE_90:
                    degree = 90;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    degree = 180;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_270:
                    degree = 270;
                    break;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return degree;
    }

    private void clipImage() {
        Bitmap bitmap = createClippedBitmap();
        String fileName = FileUtil.getRandomImageName();
        FileUtil.saveBitmapToSDCard(bitmap, FileUtil.PATH_BASE, fileName);
        String path = FileUtil.PATH_BASE + fileName;
        Luban.with(this)
                .load(new File(path))                     //传入要压缩的图片
                .setCompressListener(new OnCompressListener() { //设置回调
                    @Override
                    public void onStart() {
                        // 压缩开始前调用，可以在方法内启动 loading UI
                    }

                    @Override
                    public void onSuccess(File file) {
                        //  压缩成功后调用，返回压缩后的图片文件
                        finish();
                        EventBus.getDefault().postSticky(new ImageCropSuccessEvent(file.getPath()));
                    }

                    @Override
                    public void onError(Throwable e) {
                        //  当压缩过程出现问题时调用
                    }
                }).launch();    //启动压缩
    }

    private Bitmap createClippedBitmap() {
        if (mSampleSize <= 1) {
            return mCvlHeadImageCrop.clip();
        }

        // 获取缩放位移后的矩阵值
        final float[] matrixValues = mCvlHeadImageCrop.getClipMatrixValues();
        final float scale = matrixValues[Matrix.MSCALE_X];
        final float transX = matrixValues[Matrix.MTRANS_X];
        final float transY = matrixValues[Matrix.MTRANS_Y];

        // 获取在显示的图片中裁剪的位置
        final Rect border = mCvlHeadImageCrop.getClipBorder();
        final float cropX = ((-transX + border.left) / scale) * mSampleSize;
        final float cropY = ((-transY + border.top) / scale) * mSampleSize;
        final float cropWidth = (border.width() / scale) * mSampleSize;
        final float cropHeight = (border.height() / scale) * mSampleSize;

        // 获取在旋转之前的裁剪位置
        final RectF srcRect = new RectF(cropX, cropY, cropX + cropWidth, cropY + cropHeight);
        final Rect clipRect = getRealRect(srcRect);

        final BitmapFactory.Options ops = new BitmapFactory.Options();
        final Matrix outputMatrix = new Matrix();

        outputMatrix.setRotate(mDegree);
        // 如果裁剪之后的图片宽高仍然太大,则进行缩小
        if (mMaxWidth > 0 && cropWidth > mMaxWidth) {
            ops.inSampleSize = findBestSample((int) cropWidth, mMaxWidth);

            final float outputScale = mMaxWidth / (cropWidth / ops.inSampleSize);
            outputMatrix.postScale(outputScale, outputScale);
        }

        // 裁剪
        BitmapRegionDecoder decoder = null;
        try {
            decoder = BitmapRegionDecoder.newInstance(mInput, false);
            final Bitmap source = decoder.decodeRegion(clipRect, ops);
            recycleImageViewBitmap();
            return Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(), outputMatrix, false);
        } catch (Exception e) {
            return mCvlHeadImageCrop.clip();
        } finally {
            if (decoder != null && !decoder.isRecycled()) {
                decoder.recycle();
            }
        }
    }

    private Rect getRealRect(RectF srcRect) {
        switch (mDegree) {
            case 90:
                return new Rect((int) srcRect.top, (int) (mSourceHeight - srcRect.right),
                        (int) srcRect.bottom, (int) (mSourceHeight - srcRect.left));
            case 180:
                return new Rect((int) (mSourceWidth - srcRect.right), (int) (mSourceHeight - srcRect.bottom),
                        (int) (mSourceWidth - srcRect.left), (int) (mSourceHeight - srcRect.top));
            case 270:
                return new Rect((int) (mSourceWidth - srcRect.bottom), (int) srcRect.left,
                        (int) (mSourceWidth - srcRect.top), (int) srcRect.right);
            default:
                return new Rect((int) srcRect.left, (int) srcRect.top, (int) srcRect.right, (int) srcRect.bottom);
        }
    }

    private void recycleImageViewBitmap() {
        mCvlHeadImageCrop.post(new Runnable() {
            @Override
            public void run() {
                mCvlHeadImageCrop.setImageBitmap(null);
            }
        });
    }

    @Override
    public void onBackPressed() {
        setResult(Activity.RESULT_CANCELED, getIntent());
        super.onBackPressed();
    }

    @OnClick(R.id.tv_cancel)
    public void onMTvCancelClicked() {
        onBackPressed();
    }

    @OnClick(R.id.tv_complete)
    public void onMTvCompleteClicked() {
        clipImage();
    }
}