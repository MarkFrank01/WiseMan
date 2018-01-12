package com.zxcx.zhizhe.ui.card.card.cardDetails;

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

import com.zxcx.zhizhe.R;
import com.zxcx.zhizhe.event.SaveCardNoteSuccessEvent;
import com.zxcx.zhizhe.mvpBase.BaseDialog;
import com.zxcx.zhizhe.mvpBase.BaseRxJava;
import com.zxcx.zhizhe.mvpBase.INullPostPresenter;
import com.zxcx.zhizhe.retrofit.AppClient;
import com.zxcx.zhizhe.retrofit.BaseBean;
import com.zxcx.zhizhe.retrofit.NullPostSubscriber;
import com.zxcx.zhizhe.utils.ScreenUtils;

import org.greenrobot.eventbus.EventBus;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * Created by anm on 2017/7/21.
 */

public class NoteTitleDialog extends BaseDialog implements INullPostPresenter{

    Unbinder unbinder;
    @BindView(R.id.tv_dialog_cancel)
    TextView mTvDialogCancel;
    @BindView(R.id.tv_dialog_confirm)
    TextView mTvDialogConfirm;
    @BindView(R.id.et_dialog_note_title)
    EditText mEtDialogNoteTitle;
    @BindView(R.id.tv_dialog_note_title)
    TextView mTvDialogNoteTitle;
    int withCardId;
    String title;
    String imageUrl;
    String content;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        View view = inflater.inflate(R.layout.dialog_note_title, container);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();

        Window window = getDialog().getWindow();
        window.setBackgroundDrawableResource(R.color.translate);
        window.getDecorView().setPadding(ScreenUtils.dip2px(52), 0, ScreenUtils.dip2px(52), 0);
        WindowManager.LayoutParams lp = window.getAttributes();
        lp.gravity = Gravity.CENTER;
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        window.setAttributes(lp);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        TextPaint tp = mTvDialogConfirm.getPaint();
        tp.setFakeBoldText(true);
        tp = mTvDialogNoteTitle.getPaint();
        tp.setFakeBoldText(true);
        initData();
    }

    private void initData() {
        Bundle bundle = getArguments();
        withCardId = bundle.getInt("withCardId",0);
        title = bundle.getString("title");
        imageUrl = bundle.getString("imageUrl");
        content = bundle.getString("content");
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
        // 保存笔记
        if (mEtDialogNoteTitle.length()==0){
            saveCardNode(title,imageUrl,withCardId,content);
        }else {
            saveCardNode(mEtDialogNoteTitle.getText().toString(),imageUrl,withCardId,content);
        }
    }

    public void saveCardNode(String title,String imageUrl, int withCardId,String content) {
        mDisposable = AppClient.getAPIService().saveCardNode(null,title,imageUrl,withCardId,content)
                .compose(BaseRxJava.INSTANCE.handlePostResult())
                .compose(BaseRxJava.INSTANCE.<BaseBean>io_main_loading(this))
                .subscribeWith(new NullPostSubscriber<BaseBean>(this) {
                    @Override
                    public void onNext(BaseBean bean) {
                        postSuccess();
                    }
                });
        addSubscription(mDisposable);
    }

    @Override
    public void postSuccess() {
        EventBus.getDefault().post(new SaveCardNoteSuccessEvent());
        dismiss();
    }

    @Override
    public void postFail(String msg) {
        toastShow(msg);
        dismiss();
    }
}
