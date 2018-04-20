package com.zxcx.zhizhe.ui.loginAndRegister.initUserInfo

import android.os.Bundle
import com.zxcx.zhizhe.R
import com.zxcx.zhizhe.event.ChangeSexEvent
import com.zxcx.zhizhe.mvpBase.BaseActivity
import com.zxcx.zhizhe.mvpBase.BaseRxJava
import com.zxcx.zhizhe.mvpBase.IPostPresenter
import com.zxcx.zhizhe.retrofit.AppClient
import com.zxcx.zhizhe.retrofit.PostSubscriber
import com.zxcx.zhizhe.ui.my.selectAttention.SelectAttentionActivity
import com.zxcx.zhizhe.ui.my.userInfo.UserInfoBean
import com.zxcx.zhizhe.utils.*
import com.zxcx.zhizhe.widget.CustomDatePicker
import kotlinx.android.synthetic.main.activity_init_user_info.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import java.text.SimpleDateFormat
import java.util.*

class InitUserInfoActivity : BaseActivity(), IPostPresenter<UserInfoBean> {

    var mBirth = SharedPreferencesUtil.getString(SVTSConstants.birthday,"")
    var mSex = SharedPreferencesUtil.getInt(SVTSConstants.sex,3)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_init_user_info)
        EventBus.getDefault().register(this)

        if (!StringUtils.isEmpty(mBirth)){
            tv_iui_birthday.text = mBirth
        }

        tv_iui_sex.text = if (mSex == 1) "男" else if(mSex == 0) "女" else ""
    }

    override fun onDestroy() {
        EventBus.getDefault().unregister(this)
        super.onDestroy()
    }

    override fun postSuccess(bean: UserInfoBean?) {
        mActivity.startActivity(SelectAttentionActivity::class.java,{
            it.putExtra("isInit",true)
        })
    }

    override fun postFail(msg: String?) {
        toastError(msg)
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onMessageEvent(event: ChangeSexEvent) {
        mSex = event.sex
        refreshNext()
    }

    override fun setListener() {
        iv_iui_close.setOnClickListener {
            onBackPressed()
        }

        tv_iui_sex.setOnClickListener {
            SelectSexDialog().show(fragmentManager,this.localClassName)
        }

        tv_iui_birthday.setOnClickListener {
            if (StringUtils.isEmpty(mBirth)) {
                mBirth = DateTimeUtils.getDate()
            }
            mBirth = "$mBirth 00:00"
            val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.CHINA)
            val now = sdf.format(Date())
            val customDatePicker1 = CustomDatePicker(this, { // 回调接口，获得选中的时间
                val date = it.split(" ")[0]
                tv_iui_birthday.text = date
                mBirth = date
                refreshNext()
            }, "1900-01-01 00:00", now) // 初始化日期格式请用：yyyy-MM-dd HH:mm，否则不能正常运行
            customDatePicker1.showSpecificTime(false) // 不显示时和分
            customDatePicker1.setIsLoop(false) // 不允许循环滚动
            customDatePicker1.show(mBirth)
        }

        tv_iui_next.setOnClickListener {
            initUserInfo(mSex,mBirth)
        }
    }

    private fun refreshNext() {
        tv_iui_next.isEnabled = !StringUtils.isEmpty(tv_iui_sex.text.toString()) && !StringUtils.isEmpty(tv_iui_sex.text.toString())
    }

    private fun initUserInfo(sex: Int, birth: String) {
        mDisposable = AppClient.getAPIService().changeUserInfo(null, null, sex, birth, null)
                .compose(BaseRxJava.handleResult())
                .compose(BaseRxJava.io_main_loading(this))
                .subscribeWith(object : PostSubscriber<UserInfoBean>(this) {
                    override fun onNext(bean: UserInfoBean) {
                        postSuccess(bean)
                    }
                })
    }
}
