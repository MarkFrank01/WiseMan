package com.zxcx.zhizhe.ui.my.setting

import android.os.Bundle
import butterknife.ButterKnife
import com.zxcx.zhizhe.R
import com.zxcx.zhizhe.mvpBase.BaseActivity
import com.zxcx.zhizhe.mvpBase.BaseRxJava
import com.zxcx.zhizhe.mvpBase.INullGetPostPresenter
import com.zxcx.zhizhe.retrofit.AppClient
import com.zxcx.zhizhe.retrofit.BaseBean
import com.zxcx.zhizhe.retrofit.NullPostSubscriber
import com.zxcx.zhizhe.utils.SVTSConstants
import com.zxcx.zhizhe.utils.SharedPreferencesUtil
import kotlinx.android.synthetic.main.activity_message_mode.*

/**
 * Created by anm on 2017/12/13.
 */

class MessageModeActivity : BaseActivity(), INullGetPostPresenter<MessageModeBean> {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_message_mode)
        ButterKnife.bind(this)
        cb_message_mode_system.isChecked = SharedPreferencesUtil.getBoolean(SVTSConstants.systemMessageIsOpen, true)
        cb_message_mode_dynamic.isChecked = SharedPreferencesUtil.getBoolean(SVTSConstants.dynamicMessageIsOpen, true)
        getMessageSetting()
    }

    override fun setListener() {
        iv_common_close.setOnClickListener {
            onBackPressed()
        }

        ll_message_mode_system.setOnClickListener {
            cb_message_mode_system.isChecked = !cb_message_mode_system.isChecked
        }

        cb_message_mode_system.setOnCheckedChangeListener { buttonView, isChecked ->
            val systemMessageSetting = if (cb_message_mode_system.isChecked) 0 else 1
            val dynamicMessageSetting = if (cb_message_mode_dynamic.isChecked) 0 else 1
            setMessageSetting(systemMessageSetting, dynamicMessageSetting)
        }

        ll_message_mode_dynamic.setOnClickListener {
            cb_message_mode_dynamic.isChecked = !cb_message_mode_dynamic.isChecked
        }

        cb_message_mode_dynamic.setOnCheckedChangeListener { buttonView, isChecked ->
            val systemMessageSetting = if (cb_message_mode_system.isChecked) 0 else 1
            val dynamicMessageSetting = if (cb_message_mode_dynamic.isChecked) 0 else 1
            setMessageSetting(systemMessageSetting, dynamicMessageSetting)
        }
    }

    private fun getMessageSetting() {
        mDisposable = AppClient.getAPIService().messageSetting
                .compose(BaseRxJava.handleResult())
                .compose(BaseRxJava.io_main())
                .subscribeWith(object : NullPostSubscriber<MessageModeBean>(this) {

                    override fun onNext(bean: MessageModeBean) {
                        getDataSuccess(bean)
                    }
                })
        addSubscription(mDisposable)
    }

    private fun setMessageSetting(systemMessageSetting: Int, dynamicMessageSetting: Int) {
        mDisposable = AppClient.getAPIService().setMessageSetting(systemMessageSetting, dynamicMessageSetting)
                .compose(BaseRxJava.handlePostResult())
                .compose(BaseRxJava.io_main())
                .subscribeWith(object : NullPostSubscriber<BaseBean<*>>(this) {

                    override fun onNext(baseBean: BaseBean<*>) {
                        postSuccess()
                    }
                })
        addSubscription(mDisposable)
    }

    override fun postSuccess() {
        SharedPreferencesUtil.saveData(SVTSConstants.systemMessageIsOpen, cb_message_mode_system.isChecked)
        SharedPreferencesUtil.saveData(SVTSConstants.dynamicMessageIsOpen, cb_message_mode_dynamic.isChecked)
    }

    override fun postFail(msg: String) {
        toastShow(msg)
    }

    override fun getDataSuccess(bean: MessageModeBean) {
        SharedPreferencesUtil.saveData(SVTSConstants.systemMessageIsOpen, bean.isSystemMessageSetting)
        SharedPreferencesUtil.saveData(SVTSConstants.dynamicMessageIsOpen, bean.isDynamicMessageSetting)
        cb_message_mode_system.isChecked = bean.isSystemMessageSetting
        cb_message_mode_dynamic.isChecked = bean.isDynamicMessageSetting
    }

    override fun getDataFail(msg: String) {
        toastShow(msg)
    }
}
