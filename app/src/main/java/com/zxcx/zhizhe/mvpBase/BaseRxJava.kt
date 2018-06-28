package com.zxcx.zhizhe.mvpBase

import com.zxcx.zhizhe.retrofit.BaseArrayBean
import com.zxcx.zhizhe.retrofit.BaseBean
import com.zxcx.zhizhe.utils.Constants
import io.reactivex.FlowableTransformer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

/**
 * Created by anm on 2017/9/11.
 */

object BaseRxJava {

	fun <T> io_main(): FlowableTransformer<T, T> {
		return FlowableTransformer { upstream ->
			upstream.subscribeOn(Schedulers.io())
					.observeOn(AndroidSchedulers.mainThread())
		}
	}

	fun <T> io_main_loading(presenter: IBasePresenter?): FlowableTransformer<T, T> {
		return FlowableTransformer { upstream ->
			upstream
					.subscribeOn(Schedulers.io())
					.doOnSubscribe { presenter?.showLoading() }
					.doOnTerminate { presenter?.hideLoading() }
					.observeOn(AndroidSchedulers.mainThread())
		}
	}

	fun <T> handleResult(): FlowableTransformer<BaseBean<T>, T> {
		return FlowableTransformer { upstream ->
			upstream.map { result ->
				if (Constants.RESULT_OK == result.code) {
					result.data
				} else {
					throw RuntimeException(result.code.toString() + result.message)
				}
			}
		}
	}

	fun handlePostResult(): FlowableTransformer<BaseBean<*>, BaseBean<*>> {
		return FlowableTransformer { upstream ->
			upstream.map { result ->
				if (Constants.RESULT_OK == result.code) {
					result
				} else {
					throw RuntimeException(result.code.toString() + result.message)
				}
			}
		}
	}

	fun <T> handleArrayResult(): FlowableTransformer<BaseArrayBean<T>, MutableList<T>> {
		return FlowableTransformer { upstream ->
			upstream.map { result ->
				if (Constants.RESULT_OK == result.code) {
					result.data
				} else {
					throw RuntimeException(result.code.toString() + result.message)
				}
			}
		}
	}
}
