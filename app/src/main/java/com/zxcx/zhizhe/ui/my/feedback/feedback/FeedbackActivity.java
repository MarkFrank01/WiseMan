package com.zxcx.zhizhe.ui.my.feedback.feedback;

import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.widget.EditText;
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
import butterknife.OnCheckedChanged;
import butterknife.OnClick;

public class FeedbackActivity extends MvpActivity<FeedbackPresenter> implements FeedbackContract.View {


    @BindView(R.id.tv_toolbar_right)
    TextView mTvToolbarRight;
    @BindView(R.id.et_feedback_content)
    EditText mEtFeedbackContent;
    @BindView(R.id.tv_feedback_residue)
    TextView mTvFeedbackResidue;
    @BindView(R.id.et_feedback_phone)
    EditText mEtFeedbackPhone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);
        ButterKnife.bind(this);
        EventBus.getDefault().register(this);

        initToolBar(R.string.tv_my_feedback);
        mTvToolbarRight.setText("提交");
        mTvToolbarRight.setTextColor(ContextCompat.getColor(mActivity,R.color.color_text_enable_blue));
    }

    @Override
    public void onBackPressed() {
        if (mEtFeedbackContent.length() > 0) {
            CancelFeedbackConfirmDialog dialog = new CancelFeedbackConfirmDialog();
            dialog.show(getFragmentManager(), "");
        } else {
            super.onBackPressed();
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
        finish();
    }

    @Override
    public void postFail(String msg) {
        toastShow(msg);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(CancelFeedbackConfirmEvent event) {
        finish();
    }

    @OnClick(R.id.tv_toolbar_right)
    public void onMTvToolbarRightClicked() {
        String content = mEtFeedbackContent.getText().toString();
        String contact = mEtFeedbackPhone.getText().toString();
        int appType = Constants.APP_TYPE;
        String appChannel = WalleChannelReader.getChannel(this);
        String appVersion = Utils.getAppVersionName(this);
        mPresenter.feedback(content, contact, appType, appChannel, appVersion);
    }

    @OnCheckedChanged(R.id.et_feedback_content)
    public void onMEtFeedbackContentChanged() {
        if (mEtFeedbackContent.length() > 0) {
            mTvToolbarRight.setEnabled(true);
        } else {
            mTvToolbarRight.setEnabled(false);
        }
        mTvFeedbackResidue.setText(String.valueOf(199-mEtFeedbackContent.length()));
    }
}
