package com.zxcx.shitang.mvpBase;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.gyf.barlibrary.ImmersionBar;
import com.zxcx.shitang.R;
import com.zxcx.shitang.ui.loginAndRegister.login.LoginActivity;
import com.zxcx.shitang.utils.Constants;
import com.zxcx.shitang.utils.ScreenUtils;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

public class BaseActivity extends AppCompatActivity implements BaseView {
    public Activity mActivity;
    public ProgressDialog progressDialog;
    private boolean isFirst = true;

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
        super.onCreate(savedInstanceState);
        initStatusBar();
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        if (hasFocus&&isFirst){
            isFirst = false;
        }
    }

    private void initStatusBar() {
        if (!Constants.IS_NIGHT){
            if (ScreenUtils.isFullScreen(this)) {
                ImmersionBar.with(this)
                        .statusBarColor(R.color.white_final)
                        .statusBarDarkFont(true, 0.2f)
                        .flymeOSStatusBarFontColor(R.color.black)
                        .init();
            }else {
                ImmersionBar.with(this)
                        .statusBarColor(R.color.white_final)
                        .statusBarDarkFont(true, 0.2f)
                        .flymeOSStatusBarFontColor(R.color.black)
                        .fitsSystemWindows(true)
                        .init();
            }
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
//        if (mCompositeSubscription == null) {
        mCompositeSubscription = new CompositeDisposable();
//        }
        mCompositeSubscription.add(subscription);
    }

    public Toolbar initToolBar(String title) {

        Toolbar toolbar = initToolBar();
        TextView toolbar_title = (TextView) toolbar.findViewById(R.id.toolbar_title);
        toolbar_title.setText(title);
        ActionBar actionBar = getSupportActionBar();
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

    public void toastShow(String resId) {
//        Toast.makeText(mActivity, resId, Toast.LENGTH_SHORT).show();
        LinearLayout linearLayout = (LinearLayout) LayoutInflater.from(mActivity).inflate(R.layout.toast, null);
        TextView tvToast = (TextView) linearLayout.findViewById(R.id.tv_toast);
        Toast toast = new Toast(this);
        toast.setView(linearLayout);
        tvToast.setText(resId);
        toast.setDuration(Toast.LENGTH_LONG);
        toast.setGravity(Gravity.BOTTOM|Gravity.CENTER_HORIZONTAL,0,200);
        toast.show();
    }


    public ProgressDialog showProgress(Activity activity) {
        progressDialog = new ProgressDialog(activity);
        progressDialog.setMessage("加载中");
        progressDialog.show();
        return progressDialog;
    }

//    CustomLoading customLoading;

//    public CustomLoading showProgressDialog() {
////        progressDialog = new ProgressDialog(mActivity);
////        progressDialog.setMessage("加载中");
//////        progressDialog.setCancelable(false);
////        progressDialog.show();
//        customLoading = new CustomLoading(mActivity, R.style.CustomDialog);
//        customLoading.show();
//        return customLoading;
//    }

    public void dismissProgressDialog() {
//        if (progressDialog != null && progressDialog.isShowing()) {
//            progressDialog.dismiss();// progressDialog.hide();会导致android.view.WindowLeaked
//        }
//        if (customLoading != null && customLoading.isShowing()) {
//            customLoading.dismiss();
//        }
    }

    @Override
    public void showLoading() {
//        showProgressDialog();
    }

    @Override
    public void hideLoading() {
        dismissProgressDialog();
    }

    @Override
    public void startLogin() {
        toastShow("登陆超时，请重新登录");
        startActivity(new Intent(mActivity, LoginActivity.class));
    }

    public void toastFail(String msg) {
        toastShow(msg);
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
