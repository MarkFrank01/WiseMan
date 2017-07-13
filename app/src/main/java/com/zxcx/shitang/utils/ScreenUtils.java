package com.zxcx.shitang.utils;

import android.util.DisplayMetrics;

import com.zxcx.shitang.App;

/**
 * Created by anm on 2017/6/20.
 */

public class ScreenUtils {

    /**
     * 获取屏幕尺寸
     * @return
     */
    public static int[] getScreenSize() {
        DisplayMetrics dm ;
        dm = App.getContext().getResources().getDisplayMetrics();
        int[] size = {dm.widthPixels, dm.heightPixels};
        return size;
    }

    /**
     * 获取屏幕尺寸Width
     * @return
     */
    public static int getScreenWidth() {
        DisplayMetrics dm ;
        dm = App.getContext().getResources().getDisplayMetrics();
        int size = dm.widthPixels;
        return size;
    }

    /**
     * 获取屏幕尺寸Height
     * @return
     */
    public static int getScreenHeight() {
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
}
