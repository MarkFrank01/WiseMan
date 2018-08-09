package com.zxcx.zhizhe.mvpBase

import com.zxcx.zhizhe.retrofit.BaseArrayBean
import com.zxcx.zhizhe.retrofit.BaseBean
import com.zxcx.zhizhe.utils.Constants
import io.reactivex.FlowableTransformer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

/**
 * Created by anm on 2017/9/11.
 * RxJava网络请求通用处理封装
 */

object BaseRxJava {

	/**
	 * 切换线程
	 */
	fun <T> io_main(): FlowableTransformer<T, T> {
		return FlowableTransformer { upstream ->
			upstream.subscribeOn(Schedulers.io())
					.observeOn(AndroidSchedulers.mainThread())
		}
	}

	/**
	 * 切换线程且显示加载弹窗
	 */
	fun <T> io_main_loading(presenter: IBasePresenter?): FlowableTransformer<T, T> {
		return FlowableTransformer { upstream ->
			upstream
					.subscribeOn(Schedulers.io())
					.doOnSubscribe { presenter?.showLoading() }
					.doOnTerminate { presenter?.hideLoading() }
					.observeOn(AndroidSchedulers.mainThread())
		}
	}

	/**
	 * 对返回code异常时抛出错误，正常时取出里面数据返回
	 */
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

	/**
	 * 对返回code异常时抛出错误，正常时返回自身
	 */
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

	/**
	 * 对返回code异常时抛出错误，正常时取出里面数据返回
	 */
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
