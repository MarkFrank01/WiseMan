package com.zxcx.zhizhe.ui.my.setting;

import android.os.Bundle;
import android.widget.CheckBox;

import com.zxcx.zhizhe.R;
import com.zxcx.zhizhe.mvpBase.BaseActivity;
import com.zxcx.zhizhe.mvpBase.BaseRxJava;
import com.zxcx.zhizhe.mvpBase.INullGetPostPresenter;
import com.zxcx.zhizhe.retrofit.AppClient;
import com.zxcx.zhizhe.retrofit.BaseBean;
import com.zxcx.zhizhe.retrofit.NullPostSubscriber;
import com.zxcx.zhizhe.utils.SVTSConstants;
import com.zxcx.zhizhe.utils.SharedPreferencesUtil;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnCheckedChanged;
import butterknife.OnClick;

/**
 * Created by anm on 2017/12/13.
 */

public class MessageModeActivity extends BaseActivity implements INullGetPostPresenter<MessageModeBean>{

    @BindView(R.id.cb_message_mode_system)
    CheckBox mCbMessageModeSystem;
    @BindView(R.id.cb_message_mode_dynamic)
    CheckBox mCbMessageModeDynamic;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message_mode);
        ButterKnife.bind(this);
        initToolBar("消息提示");
        mCbMessageModeSystem.setChecked(SharedPreferencesUtil.getBoolean(SVTSConstants.systemMessageIsOpen,true));
        mCbMessageModeDynamic.setChecked(SharedPreferencesUtil.getBoolean(SVTSConstants.dynamicMessageIsOpen,true));
        getMessageSetting();
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

    public void getMessageSetting(){
        mDisposable = AppClient.getAPIService().getMessageSetting()
                .compose(BaseRxJava.handleResult())
                .compose(BaseRxJava.io_main())
                .subscribeWith(new NullPostSubscriber<MessageModeBean>(this){

                    @Override
                    public void onNext(MessageModeBean bean) {
                        getDataSuccess(bean);
                    }
                });
        addSubscription(mDisposable);
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
        SharedPreferencesUtil.saveData(SVTSConstants.systemMessageIsOpen,mCbMessageModeSystem.isChecked());
        SharedPreferencesUtil.saveData(SVTSConstants.dynamicMessageIsOpen,mCbMessageModeDynamic.isChecked());
    }

    @Override
    public void postFail(String msg) {
        toastShow(msg);
    }

    @Override
    public void getDataSuccess(MessageModeBean bean) {
        mCbMessageModeSystem.setChecked(bean.isSystemMessageSetting());
        mCbMessageModeDynamic.setChecked(bean.isDynamicMessageSetting());
    }

    @Override
    public void getDataFail(String msg) {
        toastShow(msg);
    }
}
