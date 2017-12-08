package com.zxcx.zhizhe.ui.my.creation;

import android.annotation.TargetApi;
import android.content.ContentValues;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.LinearLayout;

import com.zxcx.zhizhe.R;
import com.zxcx.zhizhe.mvpBase.BaseFragment;
import com.zxcx.zhizhe.richeditor.RichEditor;
import com.zxcx.zhizhe.utils.AndroidBug5497Workaround;
import com.zxcx.zhizhe.utils.FileUtil;
import com.zxcx.zhizhe.utils.ScreenUtils;
import com.zxcx.zhizhe.widget.OSSDialog;

import java.io.File;
import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import cn.dreamtobe.kpswitch.util.StatusBarHeightUtil;
import top.zibin.luban.Luban;
import top.zibin.luban.OnCompressListener;

import static android.app.Activity.RESULT_OK;

public class RichTextEditorFragment extends BaseFragment implements OSSDialog.OSSUploadListener {

    private static final int REQUEST_CODE_USER_ALBUM = 1;
    private static final int REQUEST_CODE_TAKE_PHOTO = 2;

    @BindView(R.id.editor)
    RichEditor mEditor;
    @BindView(R.id.ll_rte)
    LinearLayout mLlRte;
    private String title;
    private String content;
    private File imageFile;
    private OSSDialog mOSSDialog;
    private Unbinder unbinder;
    private Uri tempUri;
    private UriType mUriType;
    private String mImagePath;
    private View root;

    public enum UriType {
        file, media
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_rich_text_editor, container, false);
        unbinder = ButterKnife.bind(this, root);
        return root;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        AndroidBug5497Workaround.assistActivity(getActivity());
        mOSSDialog = new OSSDialog();
        mOSSDialog.setUploadListener(this);
        mEditor.setOnTextChangeListener(new RichEditor.OnTextChangeListener() {
            @Override
            public void onTextChange(String text) {
                if (text.contains("<p>") && text.indexOf("<p>") != 0) {
                    content = "<p>" + text.substring(0, text.indexOf("<p>")) + "</p>" + text.substring(text.indexOf("<p>"));
                } else if (!text.contains("<p>")) {
                    content = "<p>" + text + "</p>";
                }
            }
        });

        //initKeyboard();
    }

    public void getImageSuccess() {
        if (tempUri.getScheme().equals("content")) {
            String path = FileUtil.getImagePathFromUriOnKitKat(tempUri);
            getImageSuccess(path);
        } else {
            toastShow("获取图片出错");
        }
    }

    public void getImageSuccess(String imagePath) {
        imageFile = new File(imagePath);
        Luban.with(mActivity)
                .load(imageFile)                     //传入要压缩的图片
                .setCompressListener(new OnCompressListener() { //设置回调
                    @Override
                    public void onStart() {
                        // 压缩开始前调用，可以在方法内启动 loading UI
                    }

                    @Override
                    public void onSuccess(File file) {
                        //  压缩成功后调用，返回压缩后的图片文件
                        imageFile = file;
                        Bundle bundle = new Bundle();
                        bundle.putInt("OSSAction", 1);
                        bundle.putString("filePath", imageFile.getPath());
                        bundle.putString("folderName", "article/");
                        mOSSDialog.setArguments(bundle);
                        mOSSDialog.show(mActivity.getFragmentManager(), "");
                    }

                    @Override
                    public void onError(Throwable e) {
                        //  当压缩过程出现问题时调用
                    }
                }).launch();    //启动压缩
    }

    @Override
    public void uploadSuccess(String url) {
        mEditor.insertImage(url);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick(R.id.iv_rte_keyboard)
    public void onMIvRteKeyboardClicked() {
    }

    @OnClick(R.id.iv_rte_album)
    public void onMIvRteAlbumClicked() {
        // 激活系统图库，选择一张图片
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        // 开启一个带有返回值的Activity，请求码为PHOTO_REQUEST_GALLERY
        startActivityForResult(intent, REQUEST_CODE_USER_ALBUM);
    }

    @OnClick(R.id.iv_rte_camera)
    public void onMIvRteCameraClicked() {
        try {
            String fileName = FileUtil.getRandomImageName();
            mImagePath = FileUtil.PATH_BASE + fileName;
            File file = FileUtil.createFile(FileUtil.PATH_BASE, fileName);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                tempUri = FileProvider.getUriForFile(getActivity(), getActivity().getPackageName() + ".fileProvider", file);
            } else {
                tempUri = Uri.fromFile(file);
            }
            mUriType = UriType.file;
        } catch (IOException e) {
            e.printStackTrace();
            ContentValues values = new ContentValues();
            tempUri = getActivity().getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
            mUriType = UriType.media;
        }
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
                    if (mUriType == UriType.file) {
                        getImageSuccess(mImagePath);
                    } else {
                        getImageSuccess();
                    }
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
                tempUri = photoUri;
                getImageSuccess();
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

                    tempUri = photoUri;
                    getImageSuccess();
                }
            }
        }
    }

    public String getContent() {
        return content;
    }

    public String getTitle() {
        return title;
    }

    class TitleWatcher implements TextWatcher {

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            title = s.toString();
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    }

    private void initKeyboard() {
        ViewGroup contentView = (ViewGroup) mActivity.findViewById(android.R.id.content);
        final Display display = mActivity.getWindowManager().getDefaultDisplay();
        final int screenHeight;
        final Point screenSize = new Point();
        display.getSize(screenSize);
        screenHeight = screenSize.y;
        contentView.getViewTreeObserver().addOnGlobalLayoutListener(new KeyboardStatusListener(contentView,screenHeight));
    }

    private class KeyboardStatusListener implements ViewTreeObserver.OnGlobalLayoutListener {
        private final static String TAG = "KeyboardStatusListener";

        private int previousDisplayHeight = 0;
        private final ViewGroup contentView;
        private final int statusBarHeight;
        private boolean lastKeyboardShowing;
        private final int screenHeight;

        private boolean isOverlayLayoutDisplayHContainStatusBar = false;

        KeyboardStatusListener(ViewGroup contentView,int screenHeight) {
            this.contentView = contentView;
            this.statusBarHeight = StatusBarHeightUtil.getStatusBarHeight(contentView.getContext());
            this.screenHeight = screenHeight;
        }

        @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
        @Override
        public void onGlobalLayout() {
            final View userRootView = contentView.getChildAt(0);
            final View actionBarOverlayLayout = (View) contentView.getParent();

            // Step 1. calculate the current display frame's height.
            Rect r = new Rect();

            final int displayHeight;
            userRootView.getWindowVisibleDisplayFrame(r);
            displayHeight = (r.bottom - r.top);

            int keyboardHeight = calculateKeyboardHeight(displayHeight);
            calculateKeyboardShowing(displayHeight,keyboardHeight);

            previousDisplayHeight = displayHeight;
        }

        private int calculateKeyboardHeight(final int displayHeight) {
            // first result.
            if (previousDisplayHeight == 0) {
                previousDisplayHeight = displayHeight;

                // init the panel height for target.

                return 0;
            }

            int keyboardHeight;
            keyboardHeight = Math.abs(displayHeight - previousDisplayHeight);
            // no change.
            if (keyboardHeight <= ScreenUtils.dip2px(80)) {
                return 0;
            }

            Log.d(TAG, String.format("pre display height: %d display height: %d keyboard: %d ",
                    previousDisplayHeight, displayHeight, keyboardHeight));

            // influence from the layout of the Status-bar.
            if (keyboardHeight == this.statusBarHeight) {
                Log.w(TAG, String.format("On global layout change get keyboard height just equal" +
                        " statusBar height %d", keyboardHeight));
                return 0;
            }

            //键盘高度已确定
            return keyboardHeight;
        }

        private int maxOverlayLayoutHeight;
        private void calculateKeyboardShowing(final int displayHeight, int keyboardHeight) {

            boolean isKeyboardShowing;

            // the height of content parent = contentView.height + actionBar.height
            final View actionBarOverlayLayout = (View) contentView.getParent();
            // in the case of FragmentLayout, this is not real ActionBarOverlayLayout, it is
            // LinearLayout, and is a child of DecorView, and in this case, its top-padding would be
            // equal to the height of status bar, and its height would equal to DecorViewHeight -
            // NavigationBarHeight.
            final int actionBarOverlayLayoutHeight = actionBarOverlayLayout.getHeight() -
                    actionBarOverlayLayout.getPaddingTop();

            final int phoneDisplayHeight = contentView.getResources().getDisplayMetrics().heightPixels;
            if (phoneDisplayHeight == actionBarOverlayLayoutHeight) {
                // no space to settle down the status bar, switch to fullscreen,
                // only in the case of paused and opened the fullscreen page.
                Log.w(TAG, String.format("skip the keyboard status calculate, the current" +
                                " activity is paused. and phone-display-height %d," +
                                " root-height+actionbar-height %d", phoneDisplayHeight,
                        actionBarOverlayLayoutHeight));
                return;

            }

            if (maxOverlayLayoutHeight == 0) {
                // non-used.
                isKeyboardShowing = lastKeyboardShowing;
            } else {
                isKeyboardShowing = displayHeight < maxOverlayLayoutHeight - ScreenUtils.dip2px(80);
            }

            maxOverlayLayoutHeight = Math.max(maxOverlayLayoutHeight, actionBarOverlayLayoutHeight);

            if (lastKeyboardShowing != isKeyboardShowing) {
                //键盘显示逻辑
                ViewGroup.LayoutParams layoutParams = mEditor.getLayoutParams();
                if (isKeyboardShowing) {
                    layoutParams.height = layoutParams.height-keyboardHeight;
                } else {
                    layoutParams.height = layoutParams.height+keyboardHeight;
                }
                mEditor.setLayoutParams(layoutParams);
            }

            lastKeyboardShowing = isKeyboardShowing;

        }
    }
}
