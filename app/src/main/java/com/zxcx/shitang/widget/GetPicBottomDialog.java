package com.zxcx.shitang.widget;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.zxcx.shitang.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * Created by anm on 2017/5/27.
 */

public class GetPicBottomDialog extends Dialog {

    @BindView(R.id.tv_dialog_camera)
    TextView mTvDialogCamera;
    @BindView(R.id.tv_dialog_album)
    TextView mTvDialogAlbum;
    @BindView(R.id.tv_dialog_cancel)
    TextView mTvDialogCancel;
    private Unbinder mUnbinder;
    private Context mContext;
    private GetPicDialogListener mListener;

    public interface GetPicDialogListener {
        void onCameraClick();

        void onAlbumClick();
    }

    public void setListener(GetPicDialogListener listener) {
        mListener = listener;
    }

    public GetPicBottomDialog(@NonNull Context context) {
        super(context, R.style.BottomDialog);
        mContext = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View view = View.inflate(mContext, R.layout.bottom_dialog_change_head_portrait, null);
        mUnbinder = ButterKnife.bind(this,view);
        setContentView(view);
        setCanceledOnTouchOutside(true);
        Window window = getWindow();
        assert window != null;
        WindowManager.LayoutParams lp = window.getAttributes();
        lp.gravity = Gravity.BOTTOM; // 紧贴底部
        lp.width = WindowManager.LayoutParams.MATCH_PARENT; // 宽度持平
        window.setAttributes(lp);
    }

    @Override
    protected void onStop() {
        super.onStop();
        mUnbinder.unbind();
    }

    @OnClick(R.id.tv_dialog_camera)
    public void onMTvDialogCameraClicked() {
        mListener.onCameraClick();
    }

    @OnClick(R.id.tv_dialog_album)
    public void onMTvDialogAlbumClicked() {
        mListener.onAlbumClick();
    }

    @OnClick(R.id.tv_dialog_cancel)
    public void onMTvDialogCancelClicked() {
        dismiss();
    }
}
