package com.zxcx.zhizhe.ui.my.userInfo;

import android.os.Bundle;
import android.os.Handler;
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
import com.zxcx.zhizhe.mvpBase.BaseRxJava;
import com.zxcx.zhizhe.mvpBase.IPostPresenter;
import com.zxcx.zhizhe.retrofit.AppClient;
import com.zxcx.zhizhe.retrofit.PostSubscriber;
import com.zxcx.zhizhe.utils.ScreenUtils;
import com.zxcx.zhizhe.utils.Utils;

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
        setCancelable(false);
    }

    @Override
    public void onStart() {
        super.onStart();

        Window window = getDialog().getWindow();
        window.setBackgroundDrawableResource(R.color.translate);
        window.getDecorView().setPadding(ScreenUtils.dip2px(27.5f), 0, ScreenUtils.dip2px(27.5f), 0);
        WindowManager.LayoutParams lp = window.getAttributes();
        lp.gravity = Gravity.CENTER;
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        window.setAttributes(lp);

        //延迟弹出软键盘
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Utils.showInputMethod(mEtDialogChangeNickName);
            }
        },100);
    }

    @Override
    public void dismiss() {
        Utils.closeInputMethod(mEtDialogChangeNickName);
        super.dismiss();
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
                .compose(BaseRxJava.<UserInfoBean>handleResult())
                .compose(BaseRxJava.<UserInfoBean>io_main_loading(this))
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
}
