package com.zxcx.zhizhe.ui.my.creation

import android.os.Bundle
import butterknife.ButterKnife
import cn.smssdk.SMSSDK
import com.zxcx.zhizhe.R
import com.zxcx.zhizhe.event.UserInfoChangeSuccessEvent
import com.zxcx.zhizhe.mvpBase.BaseActivity
import com.zxcx.zhizhe.mvpBase.BaseRxJava
import com.zxcx.zhizhe.mvpBase.INullGetPostPresenter
import com.zxcx.zhizhe.retrofit.AppClient
import com.zxcx.zhizhe.retrofit.BaseBean
import com.zxcx.zhizhe.retrofit.BaseSubscriber
import com.zxcx.zhizhe.retrofit.NullPostSubscriber
import com.zxcx.zhizhe.ui.my.userInfo.ChangeHeadImageActivity
import com.zxcx.zhizhe.ui.my.userInfo.UserInfoBean
import com.zxcx.zhizhe.ui.my.writer_status_writer
import com.zxcx.zhizhe.utils.*
import kotlinx.android.synthetic.main.activity_apply_for_creation_2.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

/**
 * 申请创作
 */
class ApplyForCreation2Activity : BaseActivity() ,INullGetPostPresenter<UserInfoBean>{

    private val oldNickName = SharedPreferencesUtil.getString(SVTSConstants.nickName, "")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_apply_for_creation_2)
        ButterKnife.bind(this)

        val headImg = SharedPreferencesUtil.getString(SVTSConstants.imgUrl, "")
        ImageLoader.load(mActivity, headImg, R.drawable.default_header, iv_afc2_head)
        et_afc2_nick_name.setText(oldNickName)
    }

    public override fun onDestroy() {
        SMSSDK.unregisterAllEventHandler()
        EventBus.getDefault().unregister(this)
        super.onDestroy()
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onMessageEvent(event: UserInfoChangeSuccessEvent) {
        val headImg = SharedPreferencesUtil.getString(SVTSConstants.imgUrl, "")
        ImageLoader.load(mActivity, headImg, R.drawable.default_header, iv_afc2_head)
    }

    private fun changeNickName(name: String) {
        mDisposable = AppClient.getAPIService().changeUserInfo(null, name, null, null, null)
                .compose(BaseRxJava.handleResult())
                .compose(BaseRxJava.io_main_loading(this))
                .subscribeWith(object : BaseSubscriber<UserInfoBean>(this) {
                    override fun onNext(bean: UserInfoBean) {
                        getDataSuccess(bean)
                    }
                })
        addSubscription(mDisposable)
    }

    private fun applyCreation() {
        mDisposable = AppClient.getAPIService().applyCreation()
                .compose(BaseRxJava.handlePostResult())
                .compose(BaseRxJava.io_main())
                .subscribeWith(object : NullPostSubscriber<BaseBean<*>>(this) {

                    override fun onNext(baseBean: BaseBean<*>) {
                        postSuccess()
                    }
                })
        addSubscription(mDisposable)
    }

    override fun getDataSuccess(bean: UserInfoBean?) {
        ZhiZheUtils.saveUserInfo(bean)
        applyCreation()
    }

    override fun getDataFail(msg: String?) {
        toastError(msg)
    }

    override fun postSuccess() {
        SharedPreferencesUtil.saveData(SVTSConstants.writerStatus, writer_status_writer)
        startActivity(CreationActivity::class.java,{})
        finish()
    }

    override fun postFail(msg: String?) {
        toastError(msg)
    }

    override fun setListener() {
        iv_afc2_close.setOnClickListener { onBackPressed() }

        iv_afc2_head.setOnClickListener {
            mActivity.startActivity(ChangeHeadImageActivity::class.java,{})
        }

        et_afc2_nick_name.afterTextChanged {
            tv_afc2_start.isEnabled = et_afc2_nick_name.length() > 0
        }

        tv_afc2_start.setOnClickListener {
            if (et_afc2_nick_name.text.toString() != oldNickName) {
                changeNickName(et_afc2_nick_name.text.toString())
            }else{
                //未改动昵称时，直接申请
                applyCreation()
            }
        }
    }
}
