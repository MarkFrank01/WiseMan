package com.zxcx.shitang.ui.card.card.collectDialog;

import android.app.DialogFragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;

import com.zxcx.shitang.R;
import com.zxcx.shitang.event.AddCollectFolderDialogEvent;

import org.greenrobot.eventbus.EventBus;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * Created by anm on 2017/7/4.
 */

public class AddCollectFolderDialog extends DialogFragment {

    @BindView(R.id.iv_dialog_collect_folder_confirm)
    ImageView mIvDialogCollectFolderConfirm;
    @BindView(R.id.et_dialog_add_collect_folder)
    EditText mEtDialogAddCollectFolder;
    Unbinder unbinder;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        View view = inflater.inflate(R.layout.dialog_add_collect_folder, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mEtDialogAddCollectFolder.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() > 0) {
                    mIvDialogCollectFolderConfirm.setImageResource(R.drawable.iv_dialog_collect_folder_confirm_blue);
                } else {
                    mIvDialogCollectFolderConfirm.setImageResource(R.drawable.iv_dialog_collect_folder_confirm_white);
                }
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick(R.id.iv_common_close)
    public void onMIvCommonCloseClicked() {
        this.dismiss();
    }

    @OnClick(R.id.iv_dialog_collect_folder_confirm)
    public void onMIvDialogCollectFolderConfirmClicked() {
        if (mEtDialogAddCollectFolder.length()>0){
            EventBus.getDefault().post(new AddCollectFolderDialogEvent());
        }
    }
}
