package com.zxcx.zhizhe.ui.my.creation.newCreation;

import android.support.annotation.NonNull;

import com.zxcx.zhizhe.mvpBase.BaseModel;

public class RichTextEditorModel extends BaseModel<RichTextEditorContract.Presenter> {
    public RichTextEditorModel(@NonNull RichTextEditorContract.Presenter presenter) {
        this.mPresenter = presenter;
    }
}


