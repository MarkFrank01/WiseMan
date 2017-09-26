package com.zxcx.shitang.utils;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.text.TextUtils;
import android.widget.ImageView;

import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.zxcx.shitang.R;

import java.io.File;

public class ImageLoader {

    public static void load(Activity activity, String url, ImageView imageView) {
        load(activity, url, R.mipmap.image_morenlogo, imageView);
    }

    public static void load(Context context, String url, int defaultImage, ImageView imageView) {
        if (TextUtils.isEmpty(url)) {
            imageView.setImageResource(defaultImage);
        } else {
            GlideApp
                    .with(context)
                    .load(url)
                    .placeholder(defaultImage)
                    .error(defaultImage)
                    .into(imageView);
        }
    }

    public static void load(Activity activity, String url, int defaultImage, ImageView imageView) {
        if (TextUtils.isEmpty(url)) {
            imageView.setImageResource(defaultImage);
        } else {

            GlideApp
                    .with(activity)
                    .load(url)
                    .placeholder(defaultImage)
                    .error(defaultImage)
                    .into(imageView);
        }
    }

    public static void load(Activity activity, Uri uri, int defaultImage, ImageView imageView) {
        GlideApp
                .with(activity)
                .load(uri)
                .placeholder(defaultImage)
                .error(defaultImage)
                .into(imageView);
    }

    public static void load(Activity activity, File file, int defaultImage, ImageView imageView) {
        GlideApp
                .with(activity)
                .load(file)
                .placeholder(defaultImage)
                .error(defaultImage)
                .into(imageView);
    }

    public static void load(Context context, File file, int defaultImage, ImageView imageView) {
        GlideApp
                .with(context)
                .load(file)
                .placeholder(defaultImage)
                .error(defaultImage)
                .into(imageView);
    }

    public static void loadWithClear(Activity activity, Uri uri, int defaultImage, ImageView imageView) {
        GlideApp
                .with(activity)
                .load(uri)
                .placeholder(defaultImage)
                .error(defaultImage)
                .diskCacheStrategy( DiskCacheStrategy.NONE )
                .skipMemoryCache( true )
                .into(imageView);
    }

    public static void loadWithClear(Activity activity, String url, int defaultImage, ImageView imageView) {
        GlideApp
                .with(activity)
                .load(url)
                .placeholder(defaultImage)
                .error(defaultImage)
                .diskCacheStrategy( DiskCacheStrategy.NONE )
                .skipMemoryCache( true )
                .into(imageView);
    }

    public static void loadWithClear(Activity activity, File file, int defaultImage, ImageView imageView) {
        GlideApp
                .with(activity)
                .load(file)
                .placeholder(defaultImage)
                .error(defaultImage)
                .diskCacheStrategy( DiskCacheStrategy.NONE )
                .skipMemoryCache( true )
                .into(imageView);
    }

    public static void download(Activity activity, String url) {
        GlideApp
                .with(activity)
                .load(url)
                .getDownloadOnlyRequest();
    }
}
