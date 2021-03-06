package com.zxcx.zhizhe.widget;

import android.app.Dialog;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.content.FileProvider;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.zxcx.zhizhe.R;
import com.zxcx.zhizhe.mvpBase.BaseDialog;
import com.zxcx.zhizhe.utils.FileUtil;
import com.zxcx.zhizhe.utils.ScreenUtils;

import java.io.File;
import java.io.IOException;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

import static android.app.Activity.RESULT_OK;

/**
 * Created by anm on 2017/5/27.
 * 选择相机/相册弹窗
 */

public class GetPicBottomDialog extends BaseDialog {
	
	private static final int REQUEST_CODE_USER_ALBUM = 1;
	private static final int REQUEST_CODE_TAKE_PHOTO = 2;
	private static final int REQUEST_CODE_CUT_PHOTO = 3;
	@BindView(R.id.tv_dialog_camera)
	TextView mTvDialogCamera;
	@BindView(R.id.tv_dialog_album)
	TextView mTvDialogAlbum;
	@BindView(R.id.tv_dialog_cancel)
	TextView mTvDialogCancel;
	
	private Unbinder mUnbinder;
	private GetPicDialogListener mListener;
	private Uri tempUri;
	private File file;
	private int cutX = 1;
	private int cutY = 1;
	private UriType mUriType;
	private String mImagePath;
	private String title;
	private boolean mNoCrop = false;
	
	public void setListener(GetPicDialogListener listener) {
		mListener = listener;
	}
	
	public void setNoCrop(boolean noCrop) {
		mNoCrop = noCrop;
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		Bundle bundle = getArguments();
		if (bundle != null) {
			cutX = bundle.getInt("cutX", 1);
			cutY = bundle.getInt("cutY", 1);
		}
		try {
			String fileName = "zhizhe_head_image";
			mImagePath = FileUtil.PATH_BASE + fileName;
			file = FileUtil.createFile(FileUtil.PATH_BASE, fileName);
			if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
				tempUri = FileProvider
					.getUriForFile(getActivity(), getActivity().getPackageName() + ".fileProvider",
						file);
			} else {
				tempUri = Uri.fromFile(file);
			}
			mUriType = UriType.file;
		} catch (IOException e) {
			e.printStackTrace();
			ContentValues values = new ContentValues();
			tempUri = getActivity().getContentResolver()
				.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
			mUriType = UriType.media;
		}
	}
	
	@Nullable
	@Override
	public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
		Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.bottom_dialog_get_pic, container);
		mUnbinder = ButterKnife.bind(this, view);
		return view;
	}
	
	@Override
	public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
	}
	
	@Override
	public void onStart() {
		super.onStart();
		
		Dialog dialog = getDialog();
		if (dialog != null) {
			Window window = dialog.getWindow();
			window.setBackgroundDrawableResource(R.color.translate);
			window.getDecorView()
				.setPadding(ScreenUtils.dip2px(20), 0, ScreenUtils.dip2px(20),
					ScreenUtils.dip2px(20));
			WindowManager.LayoutParams lp = window.getAttributes();
			lp.gravity = Gravity.BOTTOM;
			lp.width = WindowManager.LayoutParams.MATCH_PARENT;
			lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
			window.setAttributes(lp);
		}
	}
	
	@Override
	public void onDestroyView() {
		super.onDestroyView();
		mUnbinder.unbind();
	}
	
	@OnClick(R.id.tv_dialog_camera)
	public void onMTvDialogCameraClicked() {
		
		String state = Environment.getExternalStorageState();
		if (state.equals(Environment.MEDIA_MOUNTED)) {
			Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
			intent.putExtra(MediaStore.EXTRA_OUTPUT, tempUri);
			intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
			startActivityForResult(intent, REQUEST_CODE_TAKE_PHOTO);
		} else {
			toastShow("存储空间无法使用");
		}
	}
	
	@OnClick(R.id.tv_dialog_album)
	public void onMTvDialogAlbumClicked() {
		
		// 激活系统图库，选择一张图片
		Intent intent = new Intent(Intent.ACTION_PICK);
		intent.setType("image/*");
		// 开启一个带有返回值的Activity，请求码为PHOTO_REQUEST_GALLERY
		startActivityForResult(intent, REQUEST_CODE_USER_ALBUM);
	}
	
	@OnClick(R.id.tv_dialog_cancel)
	public void onMTvDialogCancelClicked() {
		dismiss();
	}
	
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == RESULT_OK) {
			switch (requestCode) {
				/**
				 * 用户相册
				 */
				case REQUEST_CODE_USER_ALBUM:
					doPhoto(data);
					break;
				
				/**
				 * 拍照
				 */
				case REQUEST_CODE_TAKE_PHOTO:
					startPhotoZoom(tempUri);
					break;
				
				/**
				 * 裁剪处理
				 */
				case REQUEST_CODE_CUT_PHOTO:
					mListener.onGetSuccess(mUriType, tempUri, mImagePath);
					this.dismiss();
					break;
			}
		}
	}
	
	private void doPhoto(Intent data) {
		if (data != null) {
			
			// 这个方法是根据Uri获取Bitmap图片的静态方法
			
			// 取得返回的Uri,基本上选择照片的时候返回的是以Uri形式，但是在拍照中有得机子呢Uri是空的，所以要特别注意
			Uri photoUri = data.getData();
			// 返回的Uri不为空时，那么图片信息数据都会在Uri中获得。如果为空，那么我们就进行下面的方式获取
			if (photoUri != null) {
				startPhotoZoom(photoUri);
			} else {
				// android拍照获得图片URI为空的处理方法http://www.xuebuyuan.com/1929552.html
				// 这样做取得是缩略图,以下链接是取得原始图片
				// http://blog.csdn.net/beyond0525/article/details/8940840
				Bundle extras = data.getExtras();
				if (extras != null) {
					// 这里是有些拍照后的图片是直接存放到Bundle中的所以我们可以从这里面获取Bitmap图片
					// Bitmap imageBitmap =
					// extras.getParcelable("data");
					Bitmap imageBitmap = (Bitmap) extras.get("data");
					photoUri = Uri
						.parse(MediaStore.Images.Media.insertImage(
							getActivity().getContentResolver(), imageBitmap,
							null, null));
					
					startPhotoZoom(photoUri);
				}
			}
		}
	}
	
	/**
	 * 裁剪图片方法实现
	 */
	public void startPhotoZoom(Uri uri) {
		if (!mNoCrop) {
			Intent intent = new Intent("com.android.camera.action.CROP");
			intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
			intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
			intent.setDataAndType(uri, "image/*");
			// 下面这个crop=true是设置在开启的Intent中设置显示的VIEW可裁剪
			intent.putExtra("crop", "true");
			intent.putExtra("return-data", false);
			intent.putExtra(MediaStore.EXTRA_OUTPUT, tempUri);
			// aspectX aspectY 是宽高的比例
			intent.putExtra("aspectX", cutX);
			intent.putExtra("aspectY", cutY);
			// outputX outputY 是裁剪图片宽高
			intent.putExtra("outputX", cutX * 600);
			intent.putExtra("outputY", cutY * 600);
			List<ResolveInfo> resInfoList = getActivity().getPackageManager()
				.queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY);
			for (ResolveInfo resolveInfo : resInfoList) {
				String packageName = resolveInfo.activityInfo.packageName;
				getActivity().grantUriPermission(packageName, tempUri,
					Intent.FLAG_GRANT_WRITE_URI_PERMISSION | Intent.FLAG_GRANT_READ_URI_PERMISSION);
			}
			startActivityForResult(intent, REQUEST_CODE_CUT_PHOTO);
		} else {
			mUriType = UriType.media;
			mListener.onGetSuccess(mUriType, uri, mImagePath);
			this.dismiss();
		}
	}
	
	public enum UriType {
		file, media
	}
	
	public interface GetPicDialogListener {
		
		void onGetSuccess(UriType uriType, Uri uri, String imagePath);
	}


}
