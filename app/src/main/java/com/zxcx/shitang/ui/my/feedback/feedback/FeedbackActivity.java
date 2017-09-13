package com.zxcx.shitang.ui.my.feedback.feedback;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zxcx.shitang.R;
import com.zxcx.shitang.mvpBase.MvpActivity;
import com.zxcx.shitang.mvpBase.PostBean;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class FeedbackActivity extends MvpActivity<FeedbackPresenter> implements FeedbackContract.View {

    @BindView(R.id.tv_toolbar_right)
    TextView mTvToolbarRight;
    @BindView(R.id.et_feedback_content)
    EditText mEtFeedbackContent;
    @BindView(R.id.et_feedback_phone)
    EditText mEtFeedbackPhone;
    @BindView(R.id.btn_feedback_commit)
    Button mBtnFeedbackCommit;
    @BindView(R.id.ll_feedback_commit)
    LinearLayout mLlFeedbackCommit;
    @BindView(R.id.ll_feedback_success)
    LinearLayout mLlFeedbackSuccess;
    @BindView(R.id.iv_toolbar_back)
    ImageView mIvToolbarBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);
        ButterKnife.bind(this);

        initToolBar(R.string.tv_my_feedback);
        mEtFeedbackContent.addTextChangedListener(new CommitTextWatcher());
    }

    @Override
    protected FeedbackPresenter createPresenter() {
        return new FeedbackPresenter(this);
    }

    @OnClick(R.id.btn_feedback_commit)
    public void onMBtnFeedbackCommitClicked() {
        mLlFeedbackCommit.setVisibility(View.GONE);
        mLlFeedbackSuccess.setVisibility(View.VISIBLE);
    }

    @Override
    public void postSuccess(PostBean bean) {

    }

    @Override
    public void postFail(String msg) {

    }

    private class CommitTextWatcher implements TextWatcher {

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            if (mEtFeedbackContent.length() > 0) {
                mBtnFeedbackCommit.setEnabled(true);
            } else {
                mBtnFeedbackCommit.setEnabled(false);
            }
        }
    }
}
