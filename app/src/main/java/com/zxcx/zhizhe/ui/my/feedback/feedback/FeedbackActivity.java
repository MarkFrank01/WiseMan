package com.zxcx.zhizhe.ui.my.feedback.feedback;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.meituan.android.walle.WalleChannelReader;
import com.zxcx.zhizhe.R;
import com.zxcx.zhizhe.event.CancelFeedbackConfirmEvent;
import com.zxcx.zhizhe.mvpBase.MvpActivity;
import com.zxcx.zhizhe.utils.Constants;
import com.zxcx.zhizhe.utils.Utils;
import com.zxcx.zhizhe.widget.CancelFeedbackConfirmDialog;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

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
        EventBus.getDefault().register(this);

        initToolBar(R.string.tv_my_feedback);
        mEtFeedbackContent.addTextChangedListener(new CommitTextWatcher());
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (mEtFeedbackContent.length() > 0){
            CancelFeedbackConfirmDialog dialog = new CancelFeedbackConfirmDialog();
            dialog.show(getFragmentManager(),"");
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Override
    protected FeedbackPresenter createPresenter() {
        return new FeedbackPresenter(this);
    }

    @Override
    public void postSuccess() {
        mEtFeedbackContent.setText("");
        mLlFeedbackCommit.setVisibility(View.GONE);
        mLlFeedbackSuccess.setVisibility(View.VISIBLE);
    }

    @Override
    public void postFail(String msg) {
        toastShow(msg);
    }

    @OnClick(R.id.btn_feedback_commit)
    public void onMBtnFeedbackCommitClicked() {
        String content = mEtFeedbackContent.getText().toString();
        String contact = mEtFeedbackPhone.getText().toString();
        int appType = Constants.APP_TYPE;
        String appChannel = WalleChannelReader.getChannel(this);
        String appVersion = Utils.getAppVersionName(this);
        mPresenter.feedback(content, contact, appType, appChannel, appVersion);
    }

    @OnClick(R.id.btn_feedback_close)
    public void onMBtnFeedbackCloseClicked() {
        onBackPressed();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(CancelFeedbackConfirmEvent event) {
        finish();
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
