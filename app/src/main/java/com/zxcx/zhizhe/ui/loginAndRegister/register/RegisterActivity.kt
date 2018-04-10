package com.zxcx.zhizhe.ui.loginAndRegister.register

import android.os.Bundle
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.view.View
import cn.jpush.android.api.JPushInterface
import com.meituan.android.walle.WalleChannelReader
import com.zxcx.zhizhe.R
import com.zxcx.zhizhe.event.LoginEvent
import com.zxcx.zhizhe.event.StopRegisteredEvent
import com.zxcx.zhizhe.mvpBase.MvpActivity
import com.zxcx.zhizhe.ui.loginAndRegister.login.LoginBean
import com.zxcx.zhizhe.utils.*
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import kotlinx.android.synthetic.main.activity_register.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import java.util.concurrent.TimeUnit
import java.util.regex.Pattern

class RegisterActivity : MvpActivity<RegisterPresenter>(), RegisterContract.View {

    private var passwordRules = "^.{8,20}$"
    private var passwordPattern = Pattern.compile(passwordRules)
    private var jpushRID: String? = null
    private var phone: String? = null
    private var verifyKey: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
        EventBus.getDefault().register(this)

        phone = intent.getStringExtra("phone")
        verifyKey = intent.getStringExtra("verifyKey")
        jpushRID = JPushInterface.getRegistrationID(mActivity)
    }

    override fun initStatusBar() {
        //全屏输入法问题
        AndroidBug5497Workaround.assistActivity(this)
    }

    override fun onWindowFocusChanged(hasFocus: Boolean) {
        super.onWindowFocusChanged(hasFocus)
        if (hasFocus) {
            //延迟弹出软键盘
            Observable.timer(100, TimeUnit.MILLISECONDS, AndroidSchedulers.mainThread())
                    .subscribe { Utils.showInputMethod(et_register_password) }
        }
    }

    override fun onBackPressed() {
        val stopRegisteredDialog = StopRegisteredDialog()
        stopRegisteredDialog.show(mActivity.fragmentManager, "")
    }

    public override fun onDestroy() {
        EventBus.getDefault().unregister(this)
        super.onDestroy()
    }

    override fun createPresenter(): RegisterPresenter {
        return RegisterPresenter(this)
    }

    override fun getDataSuccess(bean: LoginBean) {
        ZhiZheUtils.saveLoginData(bean)
        EventBus.getDefault().post(LoginEvent())
        toastShow("欢迎来到智者")
        finish()
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onMessageEvent(event: StopRegisteredEvent) {
        finish()
    }

    private fun checkPassword(): Boolean {
        return passwordPattern.matcher(et_register_password.text.toString()).matches()
    }

    override fun setListener() {
        iv_register_close.setOnClickListener {
            onBackPressed()
        }

        et_register_password.afterTextChanged {
            cb_register_password_show.visibility = if (et_register_password.length() == 0) View.GONE else View.VISIBLE
            tv_register_login.isEnabled = checkPassword()
        }

        tv_register_login.setOnClickListener {
            val appType = Constants.APP_TYPE
            val appChannel = WalleChannelReader.getChannel(mActivity)
            val appVersion = Utils.getAppVersionName(mActivity)
            val password = MD5Utils.md5(et_register_password.text.toString())
            mPresenter.phoneRegister(phone, verifyKey, jpushRID, password, appType, appChannel, appVersion)
        }

        cb_register_password_show.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked){
                et_register_password.transformationMethod = HideReturnsTransformationMethod.getInstance()
            }else{
                et_register_password.transformationMethod = PasswordTransformationMethod.getInstance()
            }
        }
    }
}
