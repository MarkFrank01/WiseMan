package com.zxcx.zhizhe.ui.my.message.dynamic

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.zxcx.zhizhe.R
import com.zxcx.zhizhe.mvpBase.BaseFragment
import com.zxcx.zhizhe.mvpBase.BaseRxJava
import com.zxcx.zhizhe.mvpBase.IGetPresenter
import com.zxcx.zhizhe.retrofit.AppClient
import com.zxcx.zhizhe.retrofit.BaseSubscriber
import kotlinx.android.synthetic.main.fragment_dynamic_message.*

class DynamicMessageFragment : BaseFragment(), IGetPresenter<DynamicMessageBean> {

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater!!.inflate(R.layout.fragment_dynamic_message, container, false)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        getDynamicMessage()
        ll_dynamic_message_follow.setOnClickListener {
            if (tv_dynamic_message_follow.text != getString(R.string.tv_dynamic_message_no_data)){
                //进入关注消息列表
            }
        }
        ll_dynamic_message_like.setOnClickListener {
            if (tv_dynamic_message_like.text != getString(R.string.tv_dynamic_message_no_data)){
                //进入关注消息列表
            }
        }
        ll_dynamic_message_collect.setOnClickListener {
            if (tv_dynamic_message_collect.text != getString(R.string.tv_dynamic_message_no_data)){
                //进入关注消息列表
            }
        }
    }

    private fun getDynamicMessage() {
        mDisposable = AppClient.getAPIService().dynamicMessage
                .compose(BaseRxJava.io_main())
                .compose(BaseRxJava.handleResult())
                .subscribeWith(object : BaseSubscriber<DynamicMessageBean>(this) {
                    override fun onNext(bean: DynamicMessageBean) {
                        getDataSuccess(bean)
                    }
                })
        addSubscription(mDisposable)
    }

    override fun getDataSuccess(bean: DynamicMessageBean) {
        bean.followerUserStr?.let { tv_dynamic_message_follow.text = getString(R.string.tv_dynamic_message_follow,it) }
        bean.likeUserStr?.let { tv_dynamic_message_follow.text = getString(R.string.tv_dynamic_message_like,it) }
        bean.collectedUserStr?.let { tv_dynamic_message_follow.text = getString(R.string.tv_dynamic_message_collect,it) }
    }

    override fun getDataFail(msg: String?) {
        toastShow(msg)
    }
}