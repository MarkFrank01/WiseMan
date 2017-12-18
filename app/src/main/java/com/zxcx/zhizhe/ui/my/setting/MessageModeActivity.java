package com.zxcx.zhizhe.ui.my.setting;

import android.os.Bundle;
import android.widget.CheckBox;

import com.zxcx.zhizhe.R;
import com.zxcx.zhizhe.mvpBase.BaseActivity;
import com.zxcx.zhizhe.mvpBase.BaseRxJava;
import com.zxcx.zhizhe.mvpBase.INullPostPresenter;
import com.zxcx.zhizhe.retrofit.AppClient;
import com.zxcx.zhizhe.retrofit.BaseBean;
import com.zxcx.zhizhe.retrofit.NullPostSubscriber;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnCheckedChanged;
import butterknife.OnClick;

/**
 * Created by anm on 2017/12/13.
 */

public class MessageModeActivity extends BaseActivity implements INullPostPresenter{

    @BindView(R.id.cb_message_mode_system)
    CheckBox mCbMessageModeSystem;
    @BindView(R.id.cb_message_mode_dynamic)
    CheckBox mCbMessageModeDynamic;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message_mode);
        ButterKnife.bind(this);


    }

    @OnClick(R.id.ll_message_mode_system)
    public void onMLlImageLoadModeWifiClicked() {
        mCbMessageModeSystem.setChecked(!mCbMessageModeSystem.isChecked());
    }

    @OnClick(R.id.ll_message_mode_dynamic)
    public void onMLlImageLoadModeAllClicked() {
        mCbMessageModeDynamic.setChecked(!mCbMessageModeDynamic.isChecked());
    }

    @OnCheckedChanged(R.id.cb_message_mode_system)
    public void onCbSystemChanged() {
        int systemMessageSetting = mCbMessageModeSystem.isChecked()?0:1;
        int dynamicMessageSetting = mCbMessageModeDynamic.isChecked()?0:1;
        setMessageSetting(systemMessageSetting,dynamicMessageSetting);
    }

    @OnCheckedChanged(R.id.cb_message_mode_dynamic)
    public void onCbDynamicChanged() {
        int systemMessageSetting = mCbMessageModeSystem.isChecked()?0:1;
        int dynamicMessageSetting = mCbMessageModeDynamic.isChecked()?0:1;
        setMessageSetting(systemMessageSetting,dynamicMessageSetting);
    }

    public void setMessageSetting(int systemMessageSetting,int dynamicMessageSetting){
        mDisposable = AppClient.getAPIService().setMessageSetting(systemMessageSetting,dynamicMessageSetting)
                .compose(BaseRxJava.handlePostResult())
                .compose(BaseRxJava.io_main())
                .subscribeWith(new NullPostSubscriber<BaseBean>(this){

                    @Override
                    public void onNext(BaseBean baseBean) {
                        postSuccess();
                    }
                });
        addSubscription(mDisposable);
    }

    @Override
    public void postSuccess() {

    }

    @Override
    public void postFail(String msg) {
        toastShow(msg);
    }
}
