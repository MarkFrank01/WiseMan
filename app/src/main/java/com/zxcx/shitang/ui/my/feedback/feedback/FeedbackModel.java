package com.zxcx.shitang.ui.my.feedback.feedback;

import android.support.annotation.NonNull;

import com.zxcx.shitang.mvpBase.BaseModel;

public class FeedbackModel extends BaseModel<FeedbackContract.Presenter> {
    public FeedbackModel(@NonNull FeedbackContract.Presenter present) {
        this.mPresent = present;
    }
}


