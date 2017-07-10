package com.zxcx.shitang.utils;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.widget.TextView;

/**
 * Created by chenf on 2016/9/1.
 */
public class TextViewUtils {
    //清除
    public static void clearTextRightDrawable(Context context,
                                            TextView tvName) {
        Drawable[] drawables = tvName.getCompoundDrawables();
        drawables[2] = null;
        tvName.setCompoundDrawables(drawables[0],drawables[1],drawables[2],drawables[3]);
    }
    //资源ID
    public static void setTextRightDrawable(Context context, int drawableRes,
                                            TextView tvName) {
        Drawable drawableRight = ContextCompat.getDrawable(context,drawableRes);
        Drawable[] drawables = tvName.getCompoundDrawables();
        drawables[2] = drawableRight;
        // 必须设置图片大小，否则不显示
        drawableRight.setBounds(0, 0, drawableRight.getMinimumWidth(),
                drawableRight.getMinimumHeight());
        tvName.setCompoundDrawables(drawables[0],drawables[1],drawables[2],drawables[3]);
    }
    //bitmap,
    public static void setTextRightDrawable(Context context, Bitmap bitmap,
                                            TextView tvName) {
        BitmapDrawable drawableRight = new BitmapDrawable(Resources.getSystem(),bitmap);
        Drawable[] drawables = tvName.getCompoundDrawables();
        drawables[2] = drawableRight;
        // 必须设置图片大小，否则不显示
        drawableRight.setBounds(0, 0, drawableRight.getMinimumWidth(),
                drawableRight.getMinimumHeight());
        tvName.setCompoundDrawables(drawables[0],drawables[1],drawables[2],drawables[3]);
    }
    //资源ID
    public static void setTextLeftDrawable(Context context, int drawableRes,
                                           TextView tvName) {
        Drawable drawableLeft = ContextCompat.getDrawable(context,drawableRes);
        Drawable[] drawables = tvName.getCompoundDrawables();
        drawables[0] = drawableLeft;
        // 必须设置图片大小，否则不显示
        drawableLeft.setBounds(0, 0, drawableLeft.getMinimumWidth(),
                drawableLeft.getMinimumHeight());
        tvName.setCompoundDrawables(drawables[0],drawables[1],drawables[2],drawables[3]);
    }
    //bitmap,
    public static void setTextLeftDrawable(Context context, Bitmap bitmap,
                                           TextView tvName) {
        BitmapDrawable drawableLeft = new BitmapDrawable(Resources.getSystem(),bitmap);
        Drawable[] drawables = tvName.getCompoundDrawables();
        drawables[0] = drawableLeft;
        // 必须设置图片大小，否则不显示
        drawableLeft.setBounds(0, 0, drawableLeft.getMinimumWidth(),
                drawableLeft.getMinimumHeight());
        tvName.setCompoundDrawables(drawables[0],drawables[1],drawables[2],drawables[3]);
    }

}
