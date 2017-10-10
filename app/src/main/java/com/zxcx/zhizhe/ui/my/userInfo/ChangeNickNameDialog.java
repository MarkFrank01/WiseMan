package com.zxcx.zhizhe.ui.my.userInfo;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextPaint;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.zxcx.zhizhe.R;
import com.zxcx.zhizhe.event.ChangeNickNameDialogEvent;
import com.zxcx.zhizhe.mvpBase.BaseDialog;
import com.zxcx.zhizhe.mvpBase.IPostPresenter;
import com.zxcx.zhizhe.retrofit.AppClient;
import com.zxcx.zhizhe.retrofit.BaseBean;
import com.zxcx.zhizhe.retrofit.PostSubscriber;
import com.zxcx.zhizhe.ui.loginAndRegister.login.LoginActivity;
import com.zxcx.zhizhe.utils.ScreenUtils;

import org.greenrobot.eventbus.EventBus;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * Created by anm on 2017/7/13.
 */

public class ChangeNickNameDialog extends BaseDialog implements IPostPresenter<UserInfoBean> {

    Unbinder unbinder;
    @BindView(R.id.et_dialog_change_nick_name)
    EditText mEtDialogChangeNickName;
    @BindView(R.id.view_line)
    View mViewLine;
    @BindView(R.id.tv_dialog_cancel)
    TextView mTvDialogCancel;
    @BindView(R.id.tv_dialog_confirm)
    TextView mTvDialogConfirm;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        View view = inflater.inflate(R.layout.dialog_change_nick_name, container);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        TextPaint tp = mTvDialogCancel.getPaint();
        tp.setFakeBoldText(true);
        tp = mTvDialogConfirm.getPaint();
        tp.setFakeBoldText(true);
    }

    @Override
    public void onStart() {
        super.onStart();

        Window window = getDialog().getWindow();
        window.setBackgroundDrawableResource(R.color.translate);
        window.getDecorView().setPadding(ScreenUtils.dip2px(27.5f), 0, ScreenUtils.dip2px(27.5f), 0);
        WindowManager.LayoutParams lp = getDialog().getWindow().getAttributes();
        lp.gravity = Gravity.CENTER;
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        window.setAttributes(lp);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick(R.id.tv_dialog_cancel)
    public void onMTvDialogCancelClicked() {
        this.dismiss();
    }

    @OnClick(R.id.tv_dialog_confirm)
    public void onMTvDialogConfirmClicked() {
        if (mEtDialogChangeNickName.length() > 0) {
            changeNickName(mEtDialogChangeNickName.getText().toString());
        }
    }

    public void changeNickName(String name) {
        mDisposable = AppClient.getAPIService().changeUserInfo(null, name, null, null)
                .compose(this.<BaseBean<UserInfoBean>>io_main())
                .compose(this.<UserInfoBean>handleResult())
                .subscribeWith(new PostSubscriber<UserInfoBean>(this) {
                    @Override
                    public void onNext(UserInfoBean bean) {
                        ChangeNickNameDialog.this.postSuccess(bean);
                    }
                });
        addSubscription(mDisposable);
    }

    @Override
    public void postSuccess(UserInfoBean bean) {
        EventBus.getDefault().post(new ChangeNickNameDialogEvent(bean));
        this.dismiss();
    }

    @Override
    public void postFail(String msg) {
        Toast.makeText(getActivity(), msg, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showLoading() {

    }

    @Override
    public void hideLoading() {

    }

    @Override
    public void startLogin() {
        startActivity(new Intent(getActivity(), LoginActivity.class));
    }
}
