package com.zxcx.zhizhe.ui.loginAndRegister.passwordLogin

import com.zxcx.zhizhe.mvpBase.GetView
import com.zxcx.zhizhe.mvpBase.IGetPresenter
import com.zxcx.zhizhe.ui.loginAndRegister.login.LoginBean

interface PasswordLoginContract {

    interface View : GetView<LoginBean>

    interface Presenter : IGetPresenter<LoginBean>
}

