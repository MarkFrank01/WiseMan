package com.zxcx.zhizhe.mvpBase;

import android.app.DialogFragment;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.zxcx.zhizhe.R;
import com.zxcx.zhizhe.ui.loginAndRegister.login.LoginActivity;
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
        String text = getString(resId);
        toastShow(text);
    }

    public void toastShow(String text) {
        LinearLayout linearLayout = (LinearLayout) LayoutInflater.from(getActivity()).inflate(R.layout.toast, null);
        TextView tvToast = linearLayout.findViewById(R.id.tv_toast);
        ViewGroup.LayoutParams params = tvToast.getLayoutParams();
        params.width = ScreenUtils.getDisplayWidth();
        tvToast.setLayoutParams(params);
        Toast toast = new Toast(getActivity());
        toast.setView(linearLayout);
        tvToast.setText(text);
        toast.setDuration(Toast.LENGTH_LONG);
        toast.setGravity(Gravity.TOP|Gravity.CENTER_HORIZONTAL,0,0);
        toast.show();
    }

    public void toastError(int resId) {
        String text = getString(resId);
        toastError(text);
    }

    public void toastError(String text) {
//        Toast.makeText(mActivity, resId, Toast.LENGTH_SHORT).show();
        LinearLayout linearLayout = (LinearLayout) LayoutInflater.from(getActivity()).inflate(R.layout.toast, null);
        linearLayout.setBackgroundResource(R.color.red);
        TextView tvToast = linearLayout.findViewById(R.id.tv_toast);
        ViewGroup.LayoutParams params = tvToast.getLayoutParams();
        params.width = ScreenUtils.getDisplayWidth();
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
        initFontScale();
        mLoadingDialog = new LoadingDialog();
    }

    private void initFontScale() {
        Configuration configuration = getResources().getConfiguration();
        if (configuration.fontScale != 1) {
            configuration.fontScale = (float) 1;
        }
        //0.85 小, 1 标准大小, 1.15 大，1.3 超大 ，1.45 特大
        DisplayMetrics metrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(metrics);
        metrics.scaledDensity = configuration.fontScale * metrics.density;
        getActivity().getBaseContext().getResources().updateConfiguration(configuration, metrics);
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
        startActivity(new Intent(getActivity(), LoginActivity.class));
    }
}
