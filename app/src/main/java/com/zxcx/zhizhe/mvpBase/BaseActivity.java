package com.zxcx.zhizhe.mvpBase;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.gyf.barlibrary.ImmersionBar;
import com.kingja.loadsir.callback.Callback;
import com.kingja.loadsir.core.LoadService;
import com.zxcx.zhizhe.R;
import com.zxcx.zhizhe.loadCallback.LoginTimeoutCallback;
import com.zxcx.zhizhe.ui.loginAndRegister.login.LoginActivity;
import com.zxcx.zhizhe.utils.Constants;
import com.zxcx.zhizhe.utils.SVTSConstants;
import com.zxcx.zhizhe.utils.SharedPreferencesUtil;
import com.zxcx.zhizhe.utils.StringUtils;
import com.zxcx.zhizhe.utils.ZhiZheUtils;
import com.zxcx.zhizhe.widget.LoadingDialog;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

public class BaseActivity extends AppCompatActivity implements BaseView ,Callback.OnReloadListener{
    public Activity mActivity;
    private LoadingDialog mLoadingDialog;
    public boolean isFirst = true;
    public LoadService loadService;

    @Override
    public void setContentView(@LayoutRes int layoutResID) {
        super.setContentView(layoutResID);
        mActivity = this;
    }


    @Override
    public void setContentView(View view) {
        super.setContentView(view);
        mActivity = this;
    }

    @Override
    public void setContentView(View view, ViewGroup.LayoutParams params) {
        super.setContentView(view, params);
        mActivity = this;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        initStatusBar();
        super.onCreate(savedInstanceState);
        mLoadingDialog = new LoadingDialog();

    }

    public void initStatusBar() {
        if (!Constants.IS_NIGHT){
            ImmersionBar.with(this)
                    .statusBarColor(R.color.background)
                    .statusBarDarkFont(true, 0.2f)
                    .flymeOSStatusBarFontColor(R.color.black)
                    .fitsSystemWindows(true)
                    .init();
        }else {

        }
    }

    public void onResume() {
        super.onResume();

    }

    public void onPause() {
        super.onPause();
        // （仅有Activity的应用中SDK自动调用，不需要单独写）保证 onPageEnd 在onPause 之前调用,因为 onPause 中会保存信息。
        // "SplashScreen"为页面名称，可自定义

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ImmersionBar.with(this).destroy();
        if (mCompositeSubscription != null) {
            mCompositeSubscription.dispose();//取消注册，以避免内存泄露
        }
    }

    protected Disposable mDisposable;
    private CompositeDisposable mCompositeSubscription;

    public void onUnsubscribe() {
        if (mCompositeSubscription != null) {
            mCompositeSubscription.clear();//取消注册，以避免内存泄露
        }
    }

    public void addSubscription(Disposable subscription) {
        if (mCompositeSubscription == null) {
            mCompositeSubscription = new CompositeDisposable();
        }
        mCompositeSubscription.add(subscription);
    }

    public Toolbar initToolBar(String title) {
        Toolbar toolbar = initToolBar();
        TextView toolbar_title = (TextView) toolbar.findViewById(R.id.toolbar_title);
        if (!StringUtils.isEmpty(title)) {
            toolbar_title.setText(title);
        }
        return toolbar;
    }

    public Toolbar initToolBar(int title) {
        Toolbar toolbar = initToolBar();
        TextView toolbar_title = (TextView) toolbar.findViewById(R.id.toolbar_title);
        toolbar_title.setText(title);
        return toolbar;
    }

    public Toolbar initToolBar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("");
        ImageView ivBack = (ImageView) toolbar.findViewById(R.id.iv_toolbar_back);
        ivBack.setOnClickListener(new BackListener());
        return toolbar;
    }

    public void toastShow(int resId) {
        LinearLayout linearLayout = (LinearLayout) LayoutInflater.from(mActivity).inflate(R.layout.toast, null);
        TextView tvToast = (TextView) linearLayout.findViewById(R.id.tv_toast);
        Toast toast = new Toast(this);
        toast.setView(linearLayout);
        tvToast.setText(resId);
        toast.setDuration(Toast.LENGTH_LONG);
        toast.setGravity(Gravity.BOTTOM|Gravity.CENTER_HORIZONTAL,0,200);
        toast.show();
//        Toast.makeText(mActivity, resId, Toast.LENGTH_SHORT).show();
    }

    public void toastShow(String text) {
//        Toast.makeText(mActivity, resId, Toast.LENGTH_SHORT).show();
        LinearLayout linearLayout = (LinearLayout) LayoutInflater.from(mActivity).inflate(R.layout.toast, null);
        TextView tvToast = (TextView) linearLayout.findViewById(R.id.tv_toast);
        Toast toast = new Toast(this);
        toast.setView(linearLayout);
        tvToast.setText(text);
        toast.setDuration(Toast.LENGTH_LONG);
        toast.setGravity(Gravity.BOTTOM|Gravity.CENTER_HORIZONTAL,0,200);
        toast.show();
    }

    @Override
    public void showLoading() {
        if (mLoadingDialog != null && !mLoadingDialog.isAdded())
        mLoadingDialog.show(getFragmentManager(),"");
    }

    @Override
    public void hideLoading() {
        if (mLoadingDialog != null && mLoadingDialog.isAdded())
        mLoadingDialog.dismiss();
    }

    @Override
    public void startLogin() {
        ZhiZheUtils.logout();
        toastShow(R.string.login_timeout);
        startActivity(new Intent(mActivity, LoginActivity.class));
        if (loadService != null){
            loadService.showCallback(LoginTimeoutCallback.class);
        }
    }

    public boolean checkLogin(){
        if (SharedPreferencesUtil.getInt(SVTSConstants.userId, 0) != 0) {
            return true;
        } else {
            startActivity(new Intent(mActivity, LoginActivity.class));
            return false;
        }
    }

    public void toastFail(String msg) {
        toastShow(msg);
    }

    @Override
    public void onReload(View v) {

    }

    // 点击空白区域 自动隐藏软键盘
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if(null != this.getCurrentFocus()){
            /**
             * 点击空白位置 隐藏软键盘
             */
            InputMethodManager mInputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            if (mInputMethodManager != null) {
                return mInputMethodManager.hideSoftInputFromWindow(this.getCurrentFocus().getWindowToken(), 0);
            }
        }
        return super .onTouchEvent(event);
    }

    private class BackListener implements View.OnClickListener{
        @Override
        public void onClick(View v) {
            if (v.getId() == R.id.iv_toolbar_back){
                onBackPressed();//返回
            }
        }
    }
}
