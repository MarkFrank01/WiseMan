package com.zxcx.zhizhe.mvpBase;

import android.app.DialogFragment;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.zxcx.zhizhe.ui.loginAndRegister.login.LoginActivity;
import com.zxcx.zhizhe.widget.LoadingDialog;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

/**
 * Created by anm on 2017/9/11.
 */

public class BaseDialog extends DialogFragment implements IBasePresenter{

    private LoadingDialog mLoadingDialog;

    protected Disposable mDisposable;
    //    public ApiStores apiStores = AppClient.retrofit().create(ApiStores.class);
    private CompositeDisposable mCompositeSubscription = null;

    public void toastShow(int resId) {
        Toast.makeText(getActivity(), resId, Toast.LENGTH_SHORT).show();
    }

    public void toastShow(String resId) {
        Toast.makeText(getActivity(), resId, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mLoadingDialog = new LoadingDialog();
    }

    public void onDestroy() {
        super.onDestroy();
        if (mCompositeSubscription != null) {
            mCompositeSubscription.clear();//取消注册，以避免内存泄露
        }
    }

    protected void addSubscription(Disposable subscription) {
        if (mCompositeSubscription == null) {
            mCompositeSubscription = new CompositeDisposable();
        }
        mCompositeSubscription.add(subscription);
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
        toastShow("登陆超时，请重新登录");
        startActivity(new Intent(getActivity(), LoginActivity.class));
    }
}
