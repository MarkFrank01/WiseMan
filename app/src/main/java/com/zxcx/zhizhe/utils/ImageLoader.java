package com.zxcx.zhizhe.utils;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.text.TextUtils;
import android.widget.ImageView;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.zxcx.zhizhe.R;
import java.io.File;

/**
 * 图片加载封装
 */

public class ImageLoader {
	
	public static void load(Activity activity, String url, ImageView imageView) {
		load(activity, url, R.color.background, imageView);
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
	
	public static void load(Context context, int resInt, int defaultImage, ImageView imageView) {
		GlideApp
			.with(context)
			.load(resInt)
			.placeholder(defaultImage)
			.error(defaultImage)
			.into(imageView);
	}
	
	public static void load(Context context, int resInt, ImageView imageView) {
		GlideApp
			.with(context)
			.load(resInt)
			.into(imageView);
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
	
	public static void loadWithClear(Activity activity, Uri uri, int defaultImage,
		ImageView imageView) {
		GlideApp
			.with(activity)
			.load(uri)
			.placeholder(defaultImage)
			.error(defaultImage)
			.diskCacheStrategy(DiskCacheStrategy.NONE)
			.skipMemoryCache(true)
			.into(imageView);
	}
	
	public static void loadWithClear(Activity activity, String url, int defaultImage,
		ImageView imageView) {
		GlideApp
			.with(activity)
			.load(url)
			.placeholder(defaultImage)
			.error(defaultImage)
			.diskCacheStrategy(DiskCacheStrategy.NONE)
			.skipMemoryCache(true)
			.into(imageView);
	}
	
	public static void loadWithClear(Activity activity, File file, int defaultImage,
		ImageView imageView) {
		GlideApp
			.with(activity)
			.load(file)
			.placeholder(defaultImage)
			.error(defaultImage)
			.diskCacheStrategy(DiskCacheStrategy.NONE)
			.skipMemoryCache(true)
			.into(imageView);
	}
}
