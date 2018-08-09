package com.zxcx.zhizhe.retrofit

import com.zxcx.zhizhe.App
import com.zxcx.zhizhe.R
import com.zxcx.zhizhe.mvpBase.IGetPresenter
import com.zxcx.zhizhe.utils.Constants
import com.zxcx.zhizhe.utils.LogCat

import io.reactivex.subscribers.DisposableSubscriber

/**
 * Created by anm on 2017/7/24.
 * Get功能错误预处理
 */

abstract class BaseSubscriber<T>(private val mPresenter: IGetPresenter<*>?) : DisposableSubscriber<T>() {

	abstract override fun onNext(t: T)

	override fun onError(t: Throwable) {
		if (t.message != null) {
			val code = t.message?.substring(0, 3)
			try {
				val code1 = Integer.parseInt(code)
				val message = t.message?.substring(3)
				t.printStackTrace()
				LogCat.d(t.message)
				if (Constants.TOKEN_OUTTIME == code1) {
					mPresenter?.startLogin()
				} else {
					mPresenter?.getDataFail(message)
				}
			} catch (e: NumberFormatException) {
				LogCat.e(t.message, t)
				mPresenter?.getDataFail(App.getContext().getString(R.string.network_error))
			}

		} else {
			mPresenter?.getDataFail(App.getContext().getString(R.string.network_error))
		}
	}

	override fun onComplete() {

	}
}
