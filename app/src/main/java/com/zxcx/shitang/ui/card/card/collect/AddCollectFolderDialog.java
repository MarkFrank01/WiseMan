package com.zxcx.shitang.ui.card.card.collect;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;

import com.zxcx.shitang.R;
import com.zxcx.shitang.event.AddCollectFolderDialogEvent;
import com.zxcx.shitang.mvpBase.BaseDialog;
import com.zxcx.shitang.mvpBase.IPostPresenter;
import com.zxcx.shitang.mvpBase.PostBean;
import com.zxcx.shitang.retrofit.AppClient;
import com.zxcx.shitang.retrofit.BaseBean;
import com.zxcx.shitang.retrofit.PostSubscriber;
import com.zxcx.shitang.utils.SVTSConstants;
import com.zxcx.shitang.utils.ScreenUtils;
import com.zxcx.shitang.utils.SharedPreferencesUtil;

import org.greenrobot.eventbus.EventBus;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * Created by anm on 2017/7/4.
 */

public class AddCollectFolderDialog extends BaseDialog implements IPostPresenter<PostBean>{

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
    public void onStart() {
        super.onStart();

        Window window = getDialog().getWindow();
        window.setBackgroundDrawableResource(R.color.translate);
        window.getDecorView().setPadding(ScreenUtils.dip2px(12), 0, ScreenUtils.dip2px(12), 0);
        WindowManager.LayoutParams lp = getDialog().getWindow().getAttributes();
        lp.gravity = Gravity.CENTER;
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = ScreenUtils.dip2px(327);
        window.setAttributes(lp);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    public void addCollectFolder(int userId, String name){
        subscription = AppClient.getAPIService().addCollectFolder(userId, name)
                .compose(this.<BaseBean<PostBean>>io_main())
                .compose(this.<PostBean>handleResult())
                .subscribeWith(new PostSubscriber<PostBean>(this) {
                    @Override
                    public void onNext(PostBean bean) {
                        AddCollectFolderDialog.this.postSuccess(bean);
                    }
                });
        addSubscription(subscription);
    }

    @Override
    public void postSuccess(PostBean bean) {
        EventBus.getDefault().post(new AddCollectFolderDialogEvent());
        this.dismiss();
    }

    @Override
    public void postFail(String msg) {
        toastShow(msg);
    }

    @OnClick(R.id.iv_common_close)
    public void onMIvCommonCloseClicked() {
        this.dismiss();
    }

    @OnClick(R.id.iv_dialog_collect_folder_confirm)
    public void onMIvDialogCollectFolderConfirmClicked() {
        if (mEtDialogAddCollectFolder.length()>0){
            int userId = SharedPreferencesUtil.getInt(SVTSConstants.userId,0);
            String name = mEtDialogAddCollectFolder.getText().toString();
            addCollectFolder(userId,name);
        }
    }
}
