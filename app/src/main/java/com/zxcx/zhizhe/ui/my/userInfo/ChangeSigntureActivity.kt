package com.zxcx.zhizhe.ui.my.userInfo

import android.os.Bundle
import android.view.View
import butterknife.ButterKnife
import com.zxcx.zhizhe.R
import com.zxcx.zhizhe.mvpBase.BaseActivity
import com.zxcx.zhizhe.mvpBase.BaseRxJava
import com.zxcx.zhizhe.mvpBase.IPostPresenter
import com.zxcx.zhizhe.retrofit.AppClient
import com.zxcx.zhizhe.retrofit.PostSubscriber
import com.zxcx.zhizhe.utils.*
import kotlinx.android.synthetic.main.activity_change_signture.*

/**
 * Created by anm on 2017/7/13.
 */

class ChangeSigntureActivity : BaseActivity(), IPostPresenter<UserInfoBean> {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_change_signture)
        ButterKnife.bind(this)

        val signture = SharedPreferencesUtil.getString(SVTSConstants.signture, "")
        et_dialog_change_signture.setText(signture)
    }

    override fun onBackPressed() {
        super.onBackPressed()
        Utils.closeInputMethod(et_dialog_change_signture)
    }

    override fun postSuccess(bean: UserInfoBean) {
        ZhiZheUtils.saveUserInfo(bean)
        toastShow(R.string.user_info_change)
        onBackPressed()
    }

    override fun postFail(msg: String) {
        toastFail(msg)
    }

    override fun setListener() {
        iv_common_close.setOnClickListener {
            onBackPressed()
        }

        tv_change_signture_save.setOnClickListener {
            changeSignture(et_dialog_change_signture.text.toString())
        }

        et_dialog_change_signture.afterTextChanged {
            if (et_dialog_change_signture.length() in 1..5 || et_dialog_change_signture.length() == 18){
                tv_change_signture_hint.visibility = View.VISIBLE
            }else{
                tv_change_signture_hint.visibility = View.GONE
            }
            tv_change_signture_save.isEnabled = it.length in 6..18
        }
    }

    private fun changeSignture(signture: String) {
        mDisposable = AppClient.getAPIService().changeUserInfo(null, null, null, null, signture)
                .compose(BaseRxJava.handleResult())
                .compose(BaseRxJava.io_main_loading(this))
                .subscribeWith(object : PostSubscriber<UserInfoBean>(this) {
                    override fun onNext(bean: UserInfoBean) {
                        postSuccess(bean)
                    }
                })
        addSubscription(mDisposable)
    }
}
