package com.zxcx.zhizhe.mvpBase;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.StringRes;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.zxcx.zhizhe.R;
import com.zxcx.zhizhe.ui.loginAndRegister.login.LoginActivity;
import com.zxcx.zhizhe.widget.LoadingDialog;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

/**
 * A simple {@link Fragment} subclass.
 */
public class BaseFragment extends Fragment implements BaseView{
    private LoadingDialog mLoadingDialog;
    public Activity mActivity;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return null;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mActivity = getActivity();
        mLoadingDialog = new LoadingDialog();
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
        onUnsubscribe();
        mActivity = null;
        clearLeaks();
        super.onDestroy();
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

    public void toastShow(int resId) {
        LinearLayout linearLayout = (LinearLayout) LayoutInflater.from(mActivity).inflate(R.layout.toast, null);
        TextView tvToast = (TextView) linearLayout.findViewById(R.id.tv_toast);
        Toast toast = new Toast(mActivity);
        toast.setView(linearLayout);
        tvToast.setText(resId);
        toast.setDuration(Toast.LENGTH_LONG);
        toast.show();
    }

    public void toastShow(String text) {
        LinearLayout linearLayout = (LinearLayout) LayoutInflater.from(mActivity).inflate(R.layout.toast, null);
        TextView tvToast = (TextView) linearLayout.findViewById(R.id.tv_toast);
        Toast toast = new Toast(mActivity);
        toast.setView(linearLayout);
        tvToast.setText(text);
        toast.setDuration(Toast.LENGTH_LONG);
        toast.show();
    }

    public void clearLeaks() {

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
        if (mLoadingDialog != null && !mLoadingDialog.isAdded())
            mLoadingDialog.show(mActivity.getFragmentManager(),"");
    }

    @Override
    public void hideLoading() {
        if (mLoadingDialog != null && mLoadingDialog.isAdded())
            mLoadingDialog.dismiss();
    }

    @Override
    public void startLogin() {
        toastShow(R.string.login_timeout);
        startActivity(new Intent(mActivity, LoginActivity.class));
    }

    public void toastFail(String msg) {
        toastShow(msg);
    }
}
