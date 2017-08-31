package com.zxcx.shitang.mvpBase;


import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.StringRes;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.zxcx.shitang.App;
import com.zxcx.shitang.R;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

/**
 * A simple {@link Fragment} subclass.
 */
public class BaseFragment extends Fragment implements BaseView{
    public Activity mActivity;
    public String TAG = "putong";
    public ProgressDialog progressDialog;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return null;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mActivity = getActivity();
    }

    public Toolbar initToolBar(View view, String title) {
        Toolbar toolbar = (Toolbar) view.findViewById(R.id.toolbar);
        TextView toolbar_title = (TextView) toolbar.findViewById(R.id.toolbar_title);
        toolbar_title.setText(title);
        return toolbar;
    }

    public Toolbar initToolBar(View view,@StringRes int title) {
        Toolbar toolbar = (Toolbar) view.findViewById(R.id.toolbar);
        TextView toolbar_title = (TextView) toolbar.findViewById(R.id.toolbar_title);
        toolbar_title.setText(title);
        return toolbar;
    }


    //    @Override
//    public void onDestroy() {
//        super.onDestroy();
//        Log.d(TAG, mActivity + "=onDestroy");
//    }
    public void toastShow(int resId) {
        Toast.makeText(mActivity, resId, Toast.LENGTH_SHORT).show();
    }

    public void toastShow(String resId) {
        Toast.makeText(mActivity, resId, Toast.LENGTH_SHORT).show();
    }
//
//    CustomLoading customLoading;
//
//    public CustomLoading showProgressDialog() {
////        progressDialog = new ProgressDialog(mActivity);
////        progressDialog.setMessage("加载中");
////        progressDialog.show();
////        return progressDialog;
//        customLoading = new CustomLoading(mActivity, R.style.CustomDialog);
//        customLoading.show();
//        return customLoading;
//    }

    public void dismissProgressDialog() {
//        if (customLoading != null && customLoading.isShowing()) {
//            customLoading.dismiss();
//        }
//        if (progressDialog != null && progressDialog.isShowing()) {
//            progressDialog.dismiss();// progressDialog.hide();会导致android.view.WindowLeaked
//        }
    }

    public void startLogin() {
        toastShow("登陆超时，请重新登录");

    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        onUnsubscribe();
        App.getRefWatcher(getActivity()).watch(this);
    }

    private CompositeDisposable mCompositeSubscription;

    public void onUnsubscribe() {
        if (mCompositeSubscription != null) {
            mCompositeSubscription.dispose();//取消注册，以避免内存泄露
        }
    }

    public void addSubscription(Disposable subscription) {
        if (mCompositeSubscription == null) {
            mCompositeSubscription = new CompositeDisposable();
        }
        mCompositeSubscription.add(subscription);
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
    public void setText(String text) {
//        customLoading.setText(text);
    }

    @Override
    public void toastFail(String msg) {
        toastShow(msg);
    }
}
