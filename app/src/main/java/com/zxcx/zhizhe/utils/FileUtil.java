package com.zxcx.zhizhe.utils;

import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import com.zxcx.zhizhe.App;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * 文件创建保存工具
 */

public class FileUtil {
	
	/**
	 * 缓存路径，没有设为常量，在应用初始化的时候，根据具体应用做相应的目录变更
	 */
	public static String PATH_CACHE = Environment.getExternalStorageDirectory()
		.getPath() + "/%s/cache/";
	
	/**
	 * 下载路径，没有设为常量，在应用初始化的时候，根据具体应用做相应的目录变更
	 */
	public static String PATH_DOWNLOAD = Environment
		.getExternalStorageDirectory().getPath() + "/%s/DOWNLOAD/";
	
	public static String SDCard_PATH_BASE =
		Environment.getExternalStorageDirectory().getPath() + "/";
	
	public static String PATH_BASE = Environment.getExternalStorageDirectory()
		.getPath() + "/智者/";
	/**
	 * 拍照路径
	 */
	
	public static String PATH_PHOTOGRAPH = PATH_BASE + "Photograph/";
	/**
	 * 相册路径
	 */
	public static String PATH_ALBUM = PATH_BASE + "/Album/";
	public static String PATH_RECORD = PATH_BASE + "/Record/";
	
	private static boolean isExternalStorageWritable() {
		String state = Environment.getExternalStorageState();
		return Environment.MEDIA_MOUNTED.equals(state);
	}
	
	public static File getAvailableCacheDir() {
		if (isExternalStorageWritable()) {
			return App.getContext().getExternalCacheDir();
		} else {
			return App.getContext().getCacheDir();
		}
	}
	
	/**
	 * 路径初始化
	 */
	public static void initPath(String path) {
		PATH_CACHE = String.format(PATH_CACHE, path);
		PATH_DOWNLOAD = String.format(PATH_DOWNLOAD, path);
	}
	
	/**
	 * 由指定的路径和文件名创建文件
	 */
	public static File createFile(String path, String name) throws IOException {
		File folder = new File(path);
		if (!folder.exists()) {
			folder.mkdirs();
		}
		File file = new File(path + name);
		if (!file.exists()) {
			file.createNewFile();
		}
		return file;
	}
	
	/**
	 * 根据指定的路径创建文件
	 */
	public static File createFile(String filepath) throws IOException {
		int last_seperate = filepath.lastIndexOf("/") + 1;
		String path = filepath.substring(0, last_seperate);
		String name = filepath.substring(last_seperate);
		return createFile(path, name);
	}
	
	/**
	 * 获取随机图片名
	 */
	public static String getRandomImageName() {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmssSSS",
			Locale.getDefault());
		Date cruDate = Calendar.getInstance().getTime();
		String strDate = sdf.format(cruDate);
		return strDate + ".png";
	}
	
	/**
	 * 获取随机图片名
	 */
	public static String getRandomImageName(String path) {
		String suffixName = path.substring(path.lastIndexOf("."), path.length());
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmssSSS",
			Locale.getDefault());
		Date cruDate = Calendar.getInstance().getTime();
		String strDate = sdf.format(cruDate);
		return strDate + ".png";
	}
	
	/**
	 * 图片基础路径
	 */
	public static File getBaseFile(String filePath) {
		if (Environment.getExternalStorageState().equals(
			Environment.MEDIA_MOUNTED)) { // 文件可用
			File f = new File(Environment.getExternalStorageDirectory(), filePath);
			if (!f.exists()) {
				f.mkdirs();
			}
			return f;
		} else {
			return null;
		}
		
	}
	
	public static String saveFile(Intent data, String filePath) {
		Bundle extras = data.getExtras();
		if (extras != null) {
			Bitmap bitmap = extras.getParcelable("data");
			Log.e("putong", "saveFile bitmap=" + bitmap);
			if (bitmap != null) {
				saveBitmapToSDCard(bitmap, filePath, getRandomImageName());
				if (!bitmap.isRecycled()) {
					bitmap.recycle();
				}
			} else {
			
			}
		}
		return SDCard_PATH_BASE + filePath + getRandomImageName();
	}
	
	/**
	 * 转换图片成圆形
	 *
	 * @param bitmap 传入Bitmap对象
	 */
	public static Bitmap toRoundBitmap(Bitmap bitmap) {
		int width = bitmap.getWidth();
		int height = bitmap.getHeight();
		float roundPx;
		float left, top, right, bottom, dst_left, dst_top, dst_right, dst_bottom;
		if (width <= height) {
			roundPx = width / 2;
			top = 0;
			bottom = width;
			left = 0;
			right = width;
			height = width;
			dst_left = 0;
			dst_top = 0;
			dst_right = width;
			dst_bottom = width;
		} else {
			roundPx = height / 2;
			float clip = (width - height) / 2;
			left = clip;
			right = width - clip;
			top = 0;
			bottom = height;
			width = height;
			dst_left = 0;
			dst_top = 0;
			dst_right = height;
			dst_bottom = height;
		}
		
		Bitmap output = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
		Canvas canvas = new Canvas(output);
		
		final int color = 0xff424242;
		final Paint paint = new Paint();
		final Rect src = new Rect((int) left, (int) top, (int) right, (int) bottom);
		final Rect dst = new Rect((int) dst_left, (int) dst_top, (int) dst_right, (int) dst_bottom);
		final RectF rectF = new RectF(dst);
		
		paint.setAntiAlias(true);
		
		canvas.drawARGB(0, 0, 0, 0);
		paint.setColor(color);
		canvas.drawRoundRect(rectF, roundPx, roundPx, paint);
		
		paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
		canvas.drawBitmap(bitmap, src, dst, paint);
		return output;
	}
	
	
	/**
	 * 删除指定文件
	 */
	public static void deleteFile(String path, String name) {
		if (!fileExist(path, name)) {
			return;
		}
		File file = new File(path + name);
		file.delete();
	}
	
	/**
	 * 删除指定文件
	 */
	public static void deleteFile(String path) {
		if (!fileExist(path)) {
			return;
		}
		File file = new File(path);
		file.delete();
	}
	
	/**
	 * 判断文件是否存在
	 */
	public static boolean fileExist(String path, String name) {
		File file = new File(path + name);
		if (file.exists() & !file.isDirectory()) {
			return true;
		}
		return false;
	}
	
	/**
	 * 判断文件是否存在
	 */
	public static boolean fileExist(String path) {
		File file = new File(path);
		if (file.exists() & !file.isDirectory()) {
			return true;
		}
		return false;
	}
	
	
	/**
	 * SD卡是否可用（挂载）
	 */
	public static boolean isSDCardMounted() {
		/*
		 * Environment.MEDIA_MOUNTED // sd卡在手机上正常使用状态
		 * Environment.MEDIA_UNMOUNTED // 用户手工到手机设置中卸载sd卡之后的状态
		 * Environment.MEDIA_REMOVED // 用户手动卸载，然后将sd卡从手机取出之后的状态
		 * Environment.MEDIA_BAD_REMOVAL // 用户未到手机设置中手动卸载sd卡，直接拨出之后的状态
		 * Environment.MEDIA_SHARED // 手机直接连接到电脑作为u盘使用之后的状态
		 * Environment.MEDIA_CHECKINGS // 手机正在扫描sd卡过程中的状态
		 */
		if (Environment.getExternalStorageState().equals(
			Environment.MEDIA_MOUNTED)) {
			return true;
		}
		return false;
	}
	
	@SuppressLint("NewApi")
	public static long calculateFilePathSize(String filePath) {
		File file = new File(filePath);
		if (!file.exists()) {
			file.mkdirs();
			return 0l;
		}
		return file.getTotalSpace();
	}
	
	/**
	 * 删除路径
	 */
	public static void clearFilePath(String filePath) {
		File file = new File(filePath);
		if (!file.exists()) {
			return;
		}
		if (file.isFile()) {
			file.delete();
			return;
		}
		if (file.isDirectory()) {
			String[] children = file.list();
			if (children == null) {// 该目录没有子元素(空目录)
				file.delete();
				return;
			}
			for (String child : children) {
				clearFilePath(child);
			}
		}
	}
	
	
	/**
	 * Check the SD card
	 */
	public static boolean checkSDCardAvailable() {
		return Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
	}
	
	/**
	 * Save image to the SD card
	 */
	public static void saveBitmapToSDCard(Bitmap photoBitmap, String path, String photoName) {
		if (checkSDCardAvailable()) {
			File dir = new File(path);
			if (!dir.exists()) {
				dir.mkdirs();
			}
			
			File photoFile = new File(path, photoName);
			FileOutputStream fileOutputStream = null;
			try {
				fileOutputStream = new FileOutputStream(photoFile);
				if (photoBitmap != null) {
					if (photoBitmap.compress(Bitmap.CompressFormat.PNG, 100, fileOutputStream)) {
						fileOutputStream.flush();
						//回收掉bitmap
						photoBitmap.recycle();
					}
				}
			} catch (FileNotFoundException e) {
				photoFile.delete();
				e.printStackTrace();
			} catch (IOException e) {
				photoFile.delete();
				e.printStackTrace();
			} finally {
				try {
					fileOutputStream.close();
					
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	/**
	 * 根据Uri返回文件绝对路径 兼容了file:///开头的 和 content://开头的情况
	 */
	public static String getRealFilePathFromUri(final Context context, final Uri uri) {
		if (null == uri) {
			return null;
		}
		final String scheme = uri.getScheme();
		String data = null;
		if (scheme == null) {
			data = uri.getPath();
		} else if (ContentResolver.SCHEME_FILE.equalsIgnoreCase(scheme)) {
			data = uri.getPath();
		} else if (ContentResolver.SCHEME_CONTENT.equalsIgnoreCase(scheme)) {
			Cursor cursor = context.getContentResolver()
				.query(uri, new String[]{MediaStore.Images.ImageColumns.DATA}, null, null, null);
			if (null != cursor) {
				if (cursor.moveToFirst()) {
					int index = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
					if (index > -1) {
						data = cursor.getString(index);
					}
				}
				cursor.close();
			}
		}
		return data;
	}
}
