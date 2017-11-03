package com.zxcx.zhizhe.utils;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import com.zxcx.zhizhe.App;

import java.io.ByteArrayOutputStream;
import java.io.File;


public class Utils {
    private static final double EARTH_RADIUS = 6378137.0;

    public static void showInputMethod(EditText editText) {
        editText.requestFocus();
        InputMethodManager imm = (InputMethodManager) editText.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(editText,0);
//        imm.toggleSoftInput(0, InputMethodManager.SHOW_FORCED);
    }

    public static void closeInputMethod(EditText editText) {
        InputMethodManager imm = (InputMethodManager) editText.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(editText.getWindowToken(),0);
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

    public static void setImageHeight(View view) {
        DisplayMetrics dm ;
        dm = App.getContext().getResources().getDisplayMetrics();
        int height = dm.widthPixels *3/4;
        ViewGroup.LayoutParams para = view.getLayoutParams();
        para.height = height;
        view.setLayoutParams(para);
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

    /**
     * 判断网络是否可用
     *
     * @param context Context对象
     */
    public static Boolean isNetworkReachable(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo current = cm.getActiveNetworkInfo();
        if (current == null) {
            return false;
        }
        return (current.isAvailable());
    }

    public static Boolean getIsFirstLaunchApp(){
        return SharedPreferencesUtil.getBoolean(SVTSConstants.isFirstLaunchApp, true);
    }

    public static void setIsFirstLaunchApp(Boolean value){
        SharedPreferencesUtil.saveData(SVTSConstants.isFirstLaunchApp,value);
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