package com.zxcx.zhizhe.ui.my


import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatDelegate
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.zxcx.zhizhe.R
import com.zxcx.zhizhe.event.ChangeNightModeEvent
import com.zxcx.zhizhe.event.LoginEvent
import com.zxcx.zhizhe.event.LogoutEvent
import com.zxcx.zhizhe.event.UserInfoChangeSuccessEvent
import com.zxcx.zhizhe.mvpBase.BaseFragment
import com.zxcx.zhizhe.mvpBase.BaseRxJava
import com.zxcx.zhizhe.mvpBase.IGetPresenter
import com.zxcx.zhizhe.retrofit.AppClient
import com.zxcx.zhizhe.retrofit.BaseSubscriber
import com.zxcx.zhizhe.ui.my.collect.CollectCardActivity
import com.zxcx.zhizhe.ui.my.creation.CreationActivity
import com.zxcx.zhizhe.ui.my.creation.CreationAgreementDialog
import com.zxcx.zhizhe.ui.my.followUser.FansActivity
import com.zxcx.zhizhe.ui.my.followUser.FollowUserActivity
import com.zxcx.zhizhe.ui.my.intelligenceValue.IntelligenceValueActivity
import com.zxcx.zhizhe.ui.my.likeCards.LikeCardsActivity
import com.zxcx.zhizhe.ui.my.message.MessageActivity
import com.zxcx.zhizhe.ui.my.note.NoteActivity
import com.zxcx.zhizhe.ui.my.readCards.ReadCardsActivity
import com.zxcx.zhizhe.ui.my.setting.CommonSettingActivity
import com.zxcx.zhizhe.ui.my.userInfo.UserInfoActivity
import com.zxcx.zhizhe.utils.*
import kotlinx.android.synthetic.main.fragment_my.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

/**
 * A simple [Fragment] subclass.
 */
class MyFragment : BaseFragment(), IGetPresenter<MyTabBean> {

    private var writerStatus: Int = 0
    private var hasSystemMessage: Boolean = false
    private var hasDynamicMessage: Boolean = false
    private var totalIntelligenceValue: Int = 0
    private var readNum: Int = 0
    private var creationNum: Int = 0
    private var noteNum: Int = 0
    private var level: String = ""

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_my, container, false)
        EventBus.getDefault().register(this)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (SharedPreferencesUtil.getInt(SVTSConstants.userId, 0) == 0) {
            setViewLogout()
        } else {
            setViewLogin()
        }
    }

    override fun onHiddenChanged(hidden: Boolean) {
        super.onHiddenChanged(hidden)
        if (!hidden) {
            hasDynamicMessage = SharedPreferencesUtil.getBoolean(SVTSConstants.hasDynamicMessage, false)
            hasSystemMessage = SharedPreferencesUtil.getBoolean(SVTSConstants.hasSystemMessage, false)
            refreshRedPoint()
            getRedPointStatus()
        }
    }

    override fun onResume() {
        hasDynamicMessage = SharedPreferencesUtil.getBoolean(SVTSConstants.hasDynamicMessage, false)
        hasSystemMessage = SharedPreferencesUtil.getBoolean(SVTSConstants.hasSystemMessage, false)
        refreshRedPoint()
        getRedPointStatus()
        super.onResume()
    }

    override fun onDestroy() {
        super.onDestroy()
        EventBus.getDefault().unregister(this)
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onMessageEvent(event: LoginEvent) {
        setViewLogin()
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onMessageEvent(event: UserInfoChangeSuccessEvent) {
        setViewLogin()
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onMessageEvent(event: LogoutEvent) {
        setViewLogout()
    }

    override fun setListener() {
        tv_my_nick_name.setOnClickListener {
            if (checkLogin()) {
                mActivity.startActivity(UserInfoActivity::class.java,{})
            }
        }
        iv_my_head.setOnClickListener {
            if (checkLogin()) {
                mActivity.startActivity(UserInfoActivity::class.java,{})
            }
        }
        ll_my_intelligence_value.setOnClickListener {
            if (checkLogin()) {
                mActivity.startActivity(IntelligenceValueActivity::class.java,{})
            }
        }
        ll_my_top_read.setOnClickListener {
            if (checkLogin()) {
                mActivity.startActivity(ReadCardsActivity::class.java,{})
            }
        }
        ll_my_top_note.setOnClickListener {
            if (checkLogin()) {
                mActivity.startActivity(NoteActivity::class.java,{})
            }
        }
        ll_my_top_creation.setOnClickListener {
            if (checkLogin()) {
                when (writerStatus) {
                    writer_status_writer -> {
                        //创作界面
                        mActivity.startActivity(CreationActivity::class.java,{})
                    }
                    else -> {
                        val dialog = CreationAgreementDialog()
                        dialog.show(mActivity.fragmentManager, "")
                    }
                }
            }
        }
        ll_my_message.setOnClickListener {
            if (checkLogin()) {
                mActivity.startActivity(MessageActivity::class.java,{})
            }
        }
        ll_my_follow.setOnClickListener {
            if (checkLogin()) {
                mActivity.startActivity(FollowUserActivity::class.java,{})
            }
        }
        ll_my_fans.setOnClickListener {
            if (checkLogin()) {
                mActivity.startActivity(FansActivity::class.java,{})
            }
        }
        ll_my_collect.setOnClickListener {
            if (checkLogin()) {
                mActivity.startActivity(CollectCardActivity::class.java,{})
            }
        }
        ll_my_like.setOnClickListener {
            if (checkLogin()) {
                mActivity.startActivity(LikeCardsActivity::class.java,{})
            }
        }
        ll_my_setting.setOnClickListener {
            mActivity.startActivity(CommonSettingActivity::class.java,{})
        }
        //添加监听前初始化
        cb_my_night_model.isChecked = Constants.IS_NIGHT
        cb_my_night_model.setOnClickListener { _->
            val isChecked = cb_my_night_model.isChecked
            SharedPreferencesUtil.saveData(SVTSConstants.isNight, isChecked)
            Constants.IS_NIGHT = isChecked
            if (isChecked) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            }
            EventBus.getDefault().post(ChangeNightModeEvent())
        }
    }

    private fun setViewLogout() {
        iv_message_red_point.visibility = View.GONE
        tv_my_nick_name.text = "注册/登录"
        tv_my_nick_name.setTextColor(ContextCompat.getColor(mActivity, R.color.button_blue))
        tv_my_info.text = "登录看谁在关注你"
        tv_my_info.setTextColor(ContextCompat.getColor(mActivity, R.color.text_color_3))
        iv_my_head.setImageResource(R.drawable.iv_my_head_placeholder)
        ll_my_fans.visibility = View.GONE
        tv_my_lv.visibility = View.GONE
        tv_my_top_read_num.text = "0"
        tv_my_top_note_num.text = "0"
        tv_my_top_creation_num.text = "0"
    }

    private fun setViewLogin() {
        tv_my_lv.visibility = View.VISIBLE
        tv_my_nick_name.text = SharedPreferencesUtil.getString(SVTSConstants.nickName, "")
        tv_my_nick_name.setTextColor(ContextCompat.getColor(mActivity, R.color.text_color_1))
        val headImg = SharedPreferencesUtil.getString(SVTSConstants.imgUrl, "")
        ImageLoader.load(mActivity, headImg, R.drawable.default_header, iv_my_head)
        writerStatus = SharedPreferencesUtil.getInt(SVTSConstants.writerStatus, 0)
        hasDynamicMessage = SharedPreferencesUtil.getBoolean(SVTSConstants.hasDynamicMessage, false)
        hasSystemMessage = SharedPreferencesUtil.getBoolean(SVTSConstants.hasSystemMessage, false)
        totalIntelligenceValue = SharedPreferencesUtil.getInt(SVTSConstants.totalIntelligenceValue, 0)
        readNum = SharedPreferencesUtil.getInt(SVTSConstants.readNum, 0)
        creationNum = SharedPreferencesUtil.getInt(SVTSConstants.creationNum, 0)
        noteNum = SharedPreferencesUtil.getInt(SVTSConstants.noteNum, 0)
        level = SharedPreferencesUtil.getString(SVTSConstants.level,"")
        refreshRedPoint()
    }

    private fun getRedPointStatus() {
        if (SharedPreferencesUtil.getInt(SVTSConstants.userId, 0) == 0) {
            return
        }
        mDisposable = AppClient.getAPIService().myTabInfo
                .compose(BaseRxJava.handleResult())
                .compose(BaseRxJava.io_main())
                .subscribeWith(object : BaseSubscriber<MyTabBean>(this) {
                    override fun onNext(bean: MyTabBean) {
                        getDataSuccess(bean)
                    }
                })
        addSubscription(mDisposable)
    }

    override fun getDataSuccess(bean: MyTabBean) {
        writerStatus = bean.writerStatus
        hasDynamicMessage = bean.hasDynamicMessage
        hasSystemMessage = bean.hasSystemMessage
        totalIntelligenceValue = bean.totalIntelligenceValue
        readNum = bean.cardViewCount
        creationNum = bean.cardCreationCount
        noteNum = bean.noteCount
        level = bean.intelligenceValueLevel
        SharedPreferencesUtil.saveData(SVTSConstants.writerStatus, writerStatus)
        SharedPreferencesUtil.saveData(SVTSConstants.hasDynamicMessage, hasDynamicMessage)
        SharedPreferencesUtil.saveData(SVTSConstants.hasSystemMessage, hasSystemMessage)
        SharedPreferencesUtil.saveData(SVTSConstants.totalIntelligenceValue, totalIntelligenceValue)
        SharedPreferencesUtil.saveData(SVTSConstants.readNum, readNum)
        SharedPreferencesUtil.saveData(SVTSConstants.creationNum, creationNum)
        SharedPreferencesUtil.saveData(SVTSConstants.noteNum, noteNum)
        SharedPreferencesUtil.saveData(SVTSConstants.level, level)
        refreshRedPoint()
    }

    private fun refreshRedPoint() {
        if (SharedPreferencesUtil.getInt(SVTSConstants.userId, 0) == 0) {
            return
        }
        iv_message_red_point.visibility = if (hasSystemMessage || hasDynamicMessage) View.VISIBLE else View.GONE
        ll_my_fans.visibility = if (writerStatus == writer_status_writer) View.VISIBLE else View.GONE
        tv_my_info.text = getString(R.string.tv_other_user_info, ZhiZheUtils.getFormatNumber(totalIntelligenceValue))
        tv_my_info.setTextColor(ContextCompat.getColor(mActivity, R.color.button_blue))
        tv_my_lv.text = level
        tv_my_top_read_num.text = readNum.toString()
        tv_my_top_creation_num.text = creationNum.toString()
        tv_my_top_note_num.text = noteNum.toString()
    }

    override fun getDataFail(msg: String) {
        toastShow(msg)
    }
}
