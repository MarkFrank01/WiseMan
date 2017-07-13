package com.zxcx.shitang.ui.my.userInfo;

import android.app.DialogFragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.RadioButton;

import com.zxcx.shitang.R;
import com.zxcx.shitang.event.ChangeSexDialogEvent;
import com.zxcx.shitang.utils.ScreenUtils;

import org.greenrobot.eventbus.EventBus;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * Created by anm on 2017/7/13.
 */

public class ChangeSexDialog extends DialogFragment {

    Unbinder unbinder;
    @BindView(R.id.rb_change_sex_man)
    RadioButton mRbChangeSexMan;
    @BindView(R.id.rb_change_sex_woman)
    RadioButton mRbChangeSexWoman;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        View view = inflater.inflate(R.layout.dialog_change_sex, container);
        unbinder = ButterKnife.bind(this, view);
        return view;
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
        int sex = mRbChangeSexMan.isChecked() ? 1 : 0;
        EventBus.getDefault().post(new ChangeSexDialogEvent(sex));
        this.dismiss();
    }
}
