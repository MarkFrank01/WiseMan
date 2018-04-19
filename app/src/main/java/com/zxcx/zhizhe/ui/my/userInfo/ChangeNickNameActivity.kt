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
import kotlinx.android.synthetic.main.activity_change_nick_name.*

/**
 * Created by anm on 2017/7/13.
 */

class ChangeNickNameActivity : BaseActivity(), IPostPresenter<UserInfoBean> {

    private var name: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_change_nick_name)
        ButterKnife.bind(this)

        name = SharedPreferencesUtil.getString(SVTSConstants.nickName, "")
        et_dialog_change_nick_name.setText(name)
    }

    override fun onBackPressed() {
        super.onBackPressed()
        Utils.closeInputMethod(et_dialog_change_nick_name)
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

        tv_change_nick_name_save.setOnClickListener {
            if (et_dialog_change_nick_name.length() == 1){
                return@setOnClickListener
            }
            if (et_dialog_change_nick_name.text.toString() != name) {
                changeNickName(et_dialog_change_nick_name.text.toString())
            } else {
                toastShow(R.string.user_info_change)
                onBackPressed()
            }
        }

        et_dialog_change_nick_name.afterTextChanged {
            if (et_dialog_change_nick_name.length() > 0){
                tv_change_nick_name_hint.visibility = View.VISIBLE
            }
        }
    }

    private fun changeNickName(name: String) {
        mDisposable = AppClient.getAPIService().changeUserInfo(null, name, null, null, null)
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
