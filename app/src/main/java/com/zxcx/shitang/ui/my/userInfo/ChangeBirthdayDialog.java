package com.zxcx.shitang.ui.my.userInfo;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.DatePicker;

import com.zxcx.shitang.event.ChangeBirthdayDialogEvent;
import com.zxcx.shitang.utils.DateTimeUtils;

import org.greenrobot.eventbus.EventBus;


/**
 * Created by anm on 2017/7/6.
 */

public class ChangeBirthdayDialog extends DialogFragment {

    private int mYear;
    private int mMonthOfYear;
    private int mDayOfMonth;

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
                String mBirth = DateTimeUtils.getBirthdayTime(year, monthOfYear, dayOfMonth);
                EventBus.getDefault().post(new ChangeBirthdayDialogEvent(mBirth));
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
}
