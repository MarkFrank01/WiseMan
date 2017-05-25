package com.zxcx.shitang.utils;

/**
 * User: Picasso
 * Date: 2016-03-16
 * Time: 17:51
 * FIXME
 */

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;

import com.zxcx.shitang.App;

import java.io.ByteArrayOutputStream;
import java.io.File;


public class Utils {
    private static final double EARTH_RADIUS = 6378137.0;

    public static void setImageTransparent(Activity activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            activity.getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        }
    }

    public static void closeInputMethod(Context context) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(InputMethodManager.SHOW_IMPLICIT, InputMethodManager.HIDE_NOT_ALWAYS);
    }

    // 返回单位是米
    public static double getDistance(double longitude1, double latitude1,
                                     double longitude2, double latitude2) {
        double Lat1 = rad(latitude1);
        double Lat2 = rad(latitude2);
        double a = Lat1 - Lat2;
        double b = rad(longitude1) - rad(longitude2);
        double s = 2 * Math.asin(Math.sqrt(Math.pow(Math.sin(a / 2), 2)
                + Math.cos(Lat1) * Math.cos(Lat2)
                * Math.pow(Math.sin(b / 2), 2)));
        s = s * EARTH_RADIUS;
        s = Math.round(s * 10000) / 10000;
        return s;
    }

    private static double rad(double d) {
        return d * Math.PI / 180.0;
    }

    /**
     * 判断是否安装目标应用
     *
     * @param packageName 目标应用安装后的包名
     * @return 是否已安装目标应用
     */
    public static boolean isInstallByread(String packageName) {
        return new File("/data/data/" + packageName).exists();
    }

    public static int[] getScreenSize(Context context) {
        DisplayMetrics dm ;
        dm = context.getResources().getDisplayMetrics();
        int[] size = {dm.widthPixels, dm.heightPixels};
        return size;
    }

    public static void setImageHeight(View view) {
        DisplayMetrics dm ;
        dm = App.getContext().getResources().getDisplayMetrics();
        int height = dm.widthPixels *3/4;
        ViewGroup.LayoutParams para = view.getLayoutParams();
        para.height = height;
        view.setLayoutParams(para);
    }

    public static void setLunBoImageHeight(View view) {
        DisplayMetrics dm ;
        dm = App.getContext().getResources().getDisplayMetrics();
        int height = dm.widthPixels *1/2;
        ViewGroup.LayoutParams para = view.getLayoutParams();
        para.height = height;
        view.setLayoutParams(para);
    }

    public static void setDepotImageHeight(View view) {
        DisplayMetrics dm ;
        dm = App.getContext().getResources().getDisplayMetrics();
        int defaultMargin = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,60,App.getContext().getResources().getDisplayMetrics());
        int height = (dm.widthPixels-defaultMargin) *3/4;
        ViewGroup.LayoutParams para = view.getLayoutParams();
        para.height = height;
        view.setLayoutParams(para);
    }

    public static Boolean getIsFirstLaunchApp(Context mThis){
        return SharedPreferencesUtil.getBoolean(mThis, SVTSConstants.isFirstLaunchApp, true);
    }

    public static void setIsFirstLaunchApp(Activity mThis, Boolean value){
        SharedPreferencesUtil.setBoolean(mThis,SVTSConstants.isFirstLaunchApp,value);
    }

    public static String getAppVersionName(Context context) {
        try {
            PackageInfo info = context.getPackageManager().getPackageInfo(context.getPackageName(), PackageManager.GET_CONFIGURATIONS);
            return info.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            return "";
        }
    }

    /**
     * 获取软件版本号
     *
     * @param context
     * @return
     */
    public static int getVersionCode(Context context) {
        try {
            PackageInfo info = context.getPackageManager().getPackageInfo(context.getPackageName(), PackageManager.GET_CONFIGURATIONS);
            return info.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            return 0;
        }
    }

    /**
     * 由指定的路径和文件名创建文件
     */
    public static boolean checkFile(String path, String name) {
        File folder = new File(path);
        if (!folder.exists()) {
            folder.mkdirs();
        }
        File file = new File(path + name);
        if (file.exists()) {
            return true;
        }
        return false;

    }

    /**
     * 放大缩小图片(基本不会出现锯齿，比{@linkplain #}耗多一点点时间)
     *
     * @param bitmap
     * @param reqWidth 要求的宽度
     * @param reqHeight 要求的高度
     * @return
     */
    public static Bitmap zoomBitmap(Bitmap bitmap, int reqWidth, int reqHeight) {
        ByteArrayOutputStream bout = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 95, bout);// 可以是CompressFormat.PNG

        // 图片原始数据
        byte[] byteArr = bout.toByteArray();

        // 计算sampleSize
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;

        // 调用方法后，option已经有图片宽高信息
        BitmapFactory.decodeByteArray(byteArr, 0, byteArr.length, options);

        // 计算最相近缩放比例
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);
        options.inJustDecodeBounds = false;

        // 这个Bitmap out只有宽高
        Bitmap out = BitmapFactory.decodeByteArray(byteArr, 0, byteArr.length, options);

        return out;
    }

    /** 计算图片的缩放值 */
    public static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        final int height       = options.outHeight;
        final int width        = options.outWidth;
        int       inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {
            final int heightRatio = Math.round((float) height / (float) reqHeight);
            final int widthRatio = Math.round((float) width / (float) reqWidth);
            inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
        }
        return inSampleSize;
    }
}