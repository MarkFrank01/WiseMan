package com.zxcx.zhizhe.ui.loginAndRegister.passwordLogin

import android.os.Bundle
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.view.View
import cn.jpush.android.api.JPushInterface
import com.meituan.android.walle.WalleChannelReader
import com.zxcx.zhizhe.R
import com.zxcx.zhizhe.event.PasswordLoginEvent
import com.zxcx.zhizhe.mvpBase.MvpActivity
import com.zxcx.zhizhe.ui.loginAndRegister.forget.ForgetPasswordActivity
import com.zxcx.zhizhe.ui.loginAndRegister.login.LoginBean
import com.zxcx.zhizhe.utils.*
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import kotlinx.android.synthetic.main.activity_password_login.*
import org.greenrobot.eventbus.EventBus
import java.util.concurrent.TimeUnit
import java.util.regex.Pattern

class PasswordLoginActivity : MvpActivity<PasswordLoginPresenter>(), PasswordLoginContract.View {

    private var phoneRules = "^1\\d{10}$"
    private var phonePattern = Pattern.compile(phoneRules)
    private var passwordRules = "^.{8,20}$"
    private var passwordPattern = Pattern.compile(passwordRules)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_password_login)
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
                    .subscribe { Utils.showInputMethod(et_login_phone) }
        }
    }

    override fun createPresenter(): PasswordLoginPresenter {
        return PasswordLoginPresenter(this)
    }

    override fun getDataSuccess(bean: LoginBean) {
        ZhiZheUtils.saveLoginData(bean)
        EventBus.getDefault().post(PasswordLoginEvent())
        onBackPressed()
    }

    private fun checkPhone(): Boolean {
        return phonePattern.matcher(et_login_phone.text.toString()).matches()
    }

    private fun checkPassword(): Boolean {
        return passwordPattern.matcher(et_login_password.text.toString()).matches()
    }

    override fun setListener() {
        iv_login_close.setOnClickListener { onBackPressed() }

        iv_login_phone_clear.setOnClickListener {
            et_login_phone.setText("")
            Utils.showInputMethod(et_login_phone)
        }

        et_login_phone.afterTextChanged {
            val isPhone = checkPhone()
            iv_login_phone_clear.visibility = if (et_login_phone.length() == 0) View.GONE else View.VISIBLE
            tv_login_login.isEnabled = isPhone && checkPassword()
            et_login_password.isEnabled = isPhone
        }

        et_login_password.afterTextChanged {
            cb_login_password_show.visibility = if (et_login_password.length() == 0) View.GONE else View.VISIBLE
            tv_login_login.isEnabled = checkPhone() && checkPassword()
        }

        cb_login_password_show.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked){
                et_login_password.transformationMethod = HideReturnsTransformationMethod.getInstance()
            }else{
                et_login_password.transformationMethod = PasswordTransformationMethod.getInstance()
            }
            Utils.showInputMethod(et_login_password)
        }

        tv_login_login.setOnClickListener {
            val jpushRID = JPushInterface.getRegistrationID(mActivity)
            val appType = Constants.APP_TYPE
            val appChannel = WalleChannelReader.getChannel(mActivity)
            val appVersion = Utils.getAppVersionName(mActivity)
            val password = MD5Utils.md5(et_login_password.text.toString())
            mPresenter.phoneLogin(et_login_phone.text.toString(), password, jpushRID, appType, appChannel?:"官方", appVersion)
        }

        tv_login_forget.setOnClickListener {
            mActivity.startActivity(ForgetPasswordActivity::class.java,{})
        }
    }
}
