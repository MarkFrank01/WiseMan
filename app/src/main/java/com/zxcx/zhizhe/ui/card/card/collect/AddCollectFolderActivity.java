package com.zxcx.zhizhe.ui.card.card.collect;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.zxcx.zhizhe.R;
import com.zxcx.zhizhe.event.AddCollectFolderDialogEvent;
import com.zxcx.zhizhe.mvpBase.BaseActivity;
import com.zxcx.zhizhe.mvpBase.BaseRxJava;
import com.zxcx.zhizhe.mvpBase.INullPostPresenter;
import com.zxcx.zhizhe.retrofit.AppClient;
import com.zxcx.zhizhe.retrofit.BaseBean;
import com.zxcx.zhizhe.retrofit.NullPostSubscriber;
import com.zxcx.zhizhe.ui.loginAndRegister.login.LoginActivity;
import com.zxcx.zhizhe.utils.Utils;

import org.greenrobot.eventbus.EventBus;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by anm on 2017/7/4.
 */

public class AddCollectFolderActivity extends BaseActivity implements INullPostPresenter {


    @BindView(R.id.et_add_collect_folder)
    EditText mEtAddCollectFolder;
    @BindView(R.id.iv_toolbar_back)
    ImageView mIvToolbarBack;
    @BindView(R.id.toolbar_title)
    TextView mToolbarTitle;
    @BindView(R.id.iv_toolbar_right)
    ImageView mIvToolbarRight;
    @BindView(R.id.tv_toolbar_right)
    TextView mTvToolbarRight;
    @BindView(R.id.toolbar)
    Toolbar mToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_collect_folder);
        ButterKnife.bind(this);
        initToolBar("添加收藏夹");
        mIvToolbarRight.setVisibility(View.VISIBLE);
        mIvToolbarRight.setEnabled(false);
        mIvToolbarRight.setImageResource(R.drawable.selector_iv_dialog_collect_folder_confirm);
        mEtAddCollectFolder.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() > 0) {
                    mIvToolbarRight.setEnabled(true);
                } else {
                    mIvToolbarRight.setEnabled(false);
                }
            }
        });
        //延迟弹出软键盘
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Utils.showInputMethod(mEtAddCollectFolder);
            }
        }, 100);
    }

    @Override
    public void onBackPressed() {
        Utils.closeInputMethod(mEtAddCollectFolder);
        super.onBackPressed();
    }

    public void addCollectFolder(String name) {
        mDisposable = AppClient.getAPIService().addCollectFolder(name)
                .compose(BaseRxJava.<BaseBean>io_main_loading(this))
                .compose(BaseRxJava.handlePostResult())
                .subscribeWith(new NullPostSubscriber<BaseBean>(this) {
                    @Override
                    public void onNext(BaseBean bean) {
                        AddCollectFolderActivity.this.postSuccess();
                    }
                });
        addSubscription(mDisposable);
    }

    @Override
    public void postSuccess() {
        EventBus.getDefault().post(new AddCollectFolderDialogEvent());
        finish();
    }

    @Override
    public void postFail(String msg) {
        toastShow(msg);
    }

    @Override
    public void startLogin() {
        startActivity(new Intent(mActivity, LoginActivity.class));
    }

    @OnClick(R.id.iv_toolbar_right)
    public void onMIvCollectFolderConfirmClicked() {
        if (mEtAddCollectFolder.length() > 0) {
            String name = mEtAddCollectFolder.getText().toString();
            addCollectFolder(name);
            Utils.closeInputMethod(mEtAddCollectFolder);
        }
    }
}
