package com.zxcx.shitang.ui.my.userInfo;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.DatePicker;

import com.zxcx.shitang.event.ChangeBirthdayDialogEvent;
import com.zxcx.shitang.mvpBase.BaseDialog;
import com.zxcx.shitang.mvpBase.IPostPresenter;
import com.zxcx.shitang.retrofit.AppClient;
import com.zxcx.shitang.retrofit.BaseBean;
import com.zxcx.shitang.retrofit.PostSubscriber;
import com.zxcx.shitang.ui.loginAndRegister.login.LoginActivity;
import com.zxcx.shitang.utils.DateTimeUtils;

import org.greenrobot.eventbus.EventBus;


/**
 * Created by anm on 2017/7/6.
 */

public class ChangeBirthdayDialog extends BaseDialog implements IPostPresenter<UserInfoBean>{

    private int mYear;
    private int mMonthOfYear;
    private int mDayOfMonth;
    private String mBirth;
    private int userId;

    public ChangeBirthdayDialog() {
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        Bundle bundle = getArguments();

        mYear = bundle.getInt("year");
        mMonthOfYear = bundle.getInt("month");
        mDayOfMonth = bundle.getInt("day");

        DatePickerDialog pickerDialog = new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                mBirth = DateTimeUtils.getBirthdayTime(year, monthOfYear, dayOfMonth);
                changeBirthday(mBirth);
            }
        },mYear, mMonthOfYear, mDayOfMonth);
        return pickerDialog;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    public void changeBirthday(String birth){
        subscription = AppClient.getAPIService().changeUserInfo(null, null, null, birth)
                .compose(this.<BaseBean<UserInfoBean>>io_main())
                .compose(this.<UserInfoBean>handleResult())
                .subscribeWith(new PostSubscriber<UserInfoBean>(this) {
                    @Override
                    public void onNext(UserInfoBean bean) {
                        ChangeBirthdayDialog.this.postSuccess(bean);
                    }
                });
    }

    @Override
    public void postSuccess(UserInfoBean bean) {
        EventBus.getDefault().post(new ChangeBirthdayDialogEvent(bean));
    }

    @Override
    public void postFail(String msg) {
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
