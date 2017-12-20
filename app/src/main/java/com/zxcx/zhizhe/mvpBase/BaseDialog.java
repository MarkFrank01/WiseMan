package com.zxcx.zhizhe.mvpBase;

import android.app.DialogFragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.zxcx.zhizhe.R;
import com.zxcx.zhizhe.ui.loginAndRegister.login.LoginFragment;
import com.zxcx.zhizhe.utils.ScreenUtils;
import com.zxcx.zhizhe.utils.ZhiZheUtils;
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
        LinearLayout linearLayout = (LinearLayout) LayoutInflater.from(getActivity()).inflate(R.layout.toast, null);
        TextView tvToast = linearLayout.findViewById(R.id.tv_toast);
        ViewGroup.LayoutParams params = tvToast.getLayoutParams();
        params.width = ScreenUtils.getScreenWidth();
        tvToast.setLayoutParams(params);
        Toast toast = new Toast(getActivity());
        toast.setView(linearLayout);
        tvToast.setText(resId);
        toast.setDuration(Toast.LENGTH_LONG);
        toast.setGravity(Gravity.TOP|Gravity.CENTER_HORIZONTAL,0,0);
        toast.show();
    }

    public void toastShow(String text) {
        LinearLayout linearLayout = (LinearLayout) LayoutInflater.from(getActivity()).inflate(R.layout.toast, null);
        TextView tvToast = linearLayout.findViewById(R.id.tv_toast);
        ViewGroup.LayoutParams params = tvToast.getLayoutParams();
        params.width = ScreenUtils.getScreenWidth();
        tvToast.setLayoutParams(params);
        Toast toast = new Toast(getActivity());
        toast.setView(linearLayout);
        tvToast.setText(text);
        toast.setDuration(Toast.LENGTH_LONG);
        toast.setGravity(Gravity.TOP|Gravity.CENTER_HORIZONTAL,0,0);
        toast.show();
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
        ZhiZheUtils.logout();
        toastShow(R.string.login_timeout);
        startActivity(new Intent(getActivity(), LoginFragment.class));
    }
}
