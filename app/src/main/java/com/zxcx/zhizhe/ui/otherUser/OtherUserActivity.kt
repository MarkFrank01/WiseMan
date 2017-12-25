package com.zxcx.zhizhe.ui.otherUser

import android.content.Intent
import android.os.Bundle
import com.zxcx.zhizhe.R
import com.zxcx.zhizhe.mvpBase.BaseActivity
import com.zxcx.zhizhe.mvpBase.BaseRxJava
import com.zxcx.zhizhe.mvpBase.IGetPresenter
import com.zxcx.zhizhe.retrofit.AppClient
import com.zxcx.zhizhe.retrofit.BaseSubscriber
import com.zxcx.zhizhe.utils.ImageLoader
import com.zxcx.zhizhe.utils.StringUtils
import kotlinx.android.synthetic.main.activity_other_user.*

class OtherUserActivity : BaseActivity() , IGetPresenter<OtherUserInfoBean>{

    private var hasCard = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_other_user)
        val name = intent.getStringExtra("name")
        val id = intent.getIntExtra("id",0)
        initToolBar(name)
        ll_other_user_creation.setOnClickListener {
            if (hasCard){
                //跳转他的创作页
                val intent = Intent(this,OtherUserCreationActivity::class.java)
                intent.putExtra("otherUserId",id)
                startActivity(intent)
            }else{
                toastShow("该用户暂无创作")
            }
        }
        getOtherUserInfo(id)
    }

    private fun getOtherUserInfo(id: Int){
        mDisposable = AppClient.getAPIService().getAuthorInfo(id)
                .compose(BaseRxJava.handleResult())
                .compose(BaseRxJava.io_main())
                .subscribeWith(object : BaseSubscriber<OtherUserInfoBean>(this){
                    override fun onNext(t: OtherUserInfoBean?) {
                        getDataSuccess(t)
                    }
                })
    }

    override fun getDataSuccess(bean: OtherUserInfoBean?) {
        hasCard = bean?.hasCard ?:false
        if(!StringUtils.isEmpty(bean?.signture)) {
            tv_other_user_signture.text = bean?.signture
        }
        tv_other_user_info.text = getString(R.string.tv_other_user_info,bean?.readNum)
        ImageLoader.load(mActivity,bean?.imageUrl, R.drawable.default_header,iv_other_user_head)
    }

    override fun getDataFail(msg: String?) {
        toastShow(msg)
    }
}
