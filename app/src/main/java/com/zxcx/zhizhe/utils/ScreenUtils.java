package com.zxcx.zhizhe.utils;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.util.DisplayMetrics;
import android.view.ViewGroup;
import android.view.WindowManager;

import com.zxcx.zhizhe.App;

/**
 * Created by anm on 2017/6/20.
 */

public class ScreenUtils {

    /**
     * 获取屏幕尺寸
     * @return
     */
    public static int[] getScreenSize() {
        DisplayMetrics dm = new DisplayMetrics();
        WindowManager windowMgr = (WindowManager)App.getContext().getSystemService(Context.WINDOW_SERVICE);
        windowMgr.getDefaultDisplay().getRealMetrics(dm);
        int[] size = {dm.widthPixels, dm.heightPixels};
        return size;
    }

    /**
     * 获取屏幕尺寸Width
     * @return
     */
    public static int getScreenWidth() {
        DisplayMetrics dm = new DisplayMetrics();
        WindowManager windowMgr = (WindowManager)App.getContext().getSystemService(Context.WINDOW_SERVICE);
        windowMgr.getDefaultDisplay().getRealMetrics(dm);
        // 获取高度
        int height = dm.heightPixels;
        // 获取宽度
        int width = dm.widthPixels;
        return width;
    }

    /**
     * 获取屏幕尺寸Height
     * @return
     */
    public static int getScreenHeight() {
        DisplayMetrics dm = new DisplayMetrics();
        WindowManager windowMgr = (WindowManager)App.getContext().getSystemService(Context.WINDOW_SERVICE);
        windowMgr.getDefaultDisplay().getRealMetrics(dm);
        // 获取高度
        int height = dm.heightPixels;
        // 获取宽度
        int width = dm.widthPixels;
        return height;
    }

    /**
     * 获取屏幕尺寸
     * @return
     */
    public static int[] getDisplaySize() {
        DisplayMetrics dm ;
        dm = App.getContext().getResources().getDisplayMetrics();
        int[] size = {dm.widthPixels, dm.heightPixels};
        return size;
    }

    /**
     * 获取显示尺寸Width
     * @return
     */
    public static int getDisplayWidth() {
        DisplayMetrics dm ;
        dm = App.getContext().getResources().getDisplayMetrics();
        int size = dm.widthPixels;
        return size;
    }

    /**
     * 获取显示尺寸Height
     * @return
     */
    public static int getDisplayHeight() {
        DisplayMetrics dm ;
        dm = App.getContext().getResources().getDisplayMetrics();
        int size = dm.heightPixels;
        return size;
    }

    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     */
    public static int dip2px(float dpValue) {
        final float scale = App.getContext().getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    /**
     * 根据手机的分辨率从 px(像素) 的单位 转成为 dp
     */
    public static int px2dip(float pxValue) {
        final float scale = App.getContext().getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }
    
    /**
     * @param activity
     * @return 判断当前手机是否是全屏
     */
    public static boolean isFullScreen(Activity activity) {
        int flag = activity.getWindow().getAttributes().flags;
        if((flag & WindowManager.LayoutParams.FLAG_FULLSCREEN)
                == WindowManager.LayoutParams.FLAG_FULLSCREEN) {
            return true;
        }else {
            return false;
        }
    }

    /**
     * 截取控件的屏幕
     * @param viewGroup
     * @return
     */
    public static Bitmap getBitmapByView(ViewGroup viewGroup) {
        float h = 0;
        Bitmap bitmap = null;
        // 获取scrollview实际高度
        for (int i = 0; i < viewGroup.getChildCount(); i++) {
            h += viewGroup.getChildAt(i).getHeight();
        }
        // 创建对应大小的bitmap
        bitmap = Bitmap.createBitmap(viewGroup.getWidth(), (int) h,
                Bitmap.Config.RGB_565);
        Canvas canvas = new Canvas(bitmap);
        viewGroup.draw(canvas);
        return bitmap;
    }
}
