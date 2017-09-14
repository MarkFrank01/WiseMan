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
import com.zxcx.shitang.mvpBase.PostBean;
import com.zxcx.shitang.retrofit.AppClient;
import com.zxcx.shitang.retrofit.BaseBean;
import com.zxcx.shitang.retrofit.PostSubscriber;
import com.zxcx.shitang.ui.loginAndRegister.login.LoginActivity;
import com.zxcx.shitang.utils.DateTimeUtils;
import com.zxcx.shitang.utils.SVTSConstants;
import com.zxcx.shitang.utils.SharedPreferencesUtil;

import org.greenrobot.eventbus.EventBus;


/**
 * Created by anm on 2017/7/6.
 */

public class ChangeBirthdayDialog extends BaseDialog implements IPostPresenter<PostBean>{

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
        userId = SharedPreferencesUtil.getInt(SVTSConstants.userId,0);

        DatePickerDialog pickerDialog = new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                mBirth = DateTimeUtils.getBirthdayTime(year, monthOfYear, dayOfMonth);
                changeBirthday(userId,mBirth);
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

    public void changeBirthday(int userId, String birth){
        subscription = AppClient.getAPIService().changeUserInfo(userId, null, null, null, birth)
                .compose(this.<BaseBean<PostBean>>io_main())
                .compose(this.<PostBean>handleResult())
                .subscribeWith(new PostSubscriber<PostBean>(this) {
                    @Override
                    public void onNext(PostBean bean) {
                        ChangeBirthdayDialog.this.postSuccess(bean);
                    }
                });
        addSubscription(subscription);
    }

    @Override
    public void postSuccess(PostBean bean) {
        EventBus.getDefault().post(new ChangeBirthdayDialogEvent(mBirth));
        this.dismiss();
    }

    @Override
    public void postFail(String msg) {
        this.dismiss();
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
