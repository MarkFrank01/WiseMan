package com.zxcx.zhizhe.ui.my


import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatDelegate
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.uuch.adlibrary.AdConstant
import com.uuch.adlibrary.AdManager
import com.uuch.adlibrary.bean.AdInfo
import com.youth.banner.transformer.DepthPageTransformer
import com.zxcx.zhizhe.R
import com.zxcx.zhizhe.event.ChangeNightModeEvent
import com.zxcx.zhizhe.event.LoginEvent
import com.zxcx.zhizhe.event.LogoutEvent
import com.zxcx.zhizhe.event.UserInfoChangeSuccessEvent
import com.zxcx.zhizhe.mvpBase.BaseRxJava
import com.zxcx.zhizhe.mvpBase.MvpFragment
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
import com.zxcx.zhizhe.ui.newrank.NewRankActivity
import com.zxcx.zhizhe.ui.welcome.ADBean
import com.zxcx.zhizhe.ui.welcome.WebViewActivity
import com.zxcx.zhizhe.utils.*
import kotlinx.android.synthetic.main.fragment_my.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

/**
 * 我的页面
 * A simple [Fragment] subclass.
 */
//class MyFragment : BaseFragment(), IGetPresenter<MyTabBean> {
class MyFragment : MvpFragment<MyFragmentPresenter>(), MyFragmentContract.View {


    private var writerStatus: Int = 0
    private var hasSystemMessage: Boolean = false
    private var hasDynamicMessage: Boolean = false
    private var totalIntelligenceValue: Int = 0
    private var readNum: Int = 0
    private var creationNum: Int = 0
    private var fansNum: Int = 0
    private var level: Int = 0
    private var actionType: Int = 0

    private var advList: ArrayList<AdInfo> = ArrayList()
    private var mAdList: MutableList<ADBean> = mutableListOf()

    private var lastADTime:Long = 0
    private var lastADID:Int = 0

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

        lastADTime = SharedPreferencesUtil.getLong(SVTSConstants.homeMyLastOpenedTime,0)
        lastADID = SharedPreferencesUtil.getInt(SVTSConstants.homeMyLastOpenedID,0)


        onRefreshAD(lastADTime,lastADID.toLong())
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
                mActivity.startActivity(UserInfoActivity::class.java) {}
            }
        }
        iv_my_head.setOnClickListener {
            if (checkLogin()) {
                mActivity.startActivity(UserInfoActivity::class.java) {}
            }
        }
        tv_my_lv.expandViewTouchDelegate(ScreenUtils.dip2px(5f))
        tv_my_lv.setOnClickListener {
            if (checkLogin()) {
                mActivity.startActivity(IntelligenceValueActivity::class.java) {}
            }
        }
        ll_my_top_read.setOnClickListener {
            if (checkLogin()) {
                mActivity.startActivity(ReadCardsActivity::class.java) {}
            }
        }
        ll_my_note.setOnClickListener {
            if (checkLogin()) {
                mActivity.startActivity(NoteActivity::class.java) {}
            }
        }
        ll_my_top_creation.setOnClickListener {
            if (checkLogin()) {
                when (SharedPreferencesUtil.getInt(SVTSConstants.writerStatus, 0)) {
                    writer_status_writer -> {
                        //创作界面
                        mActivity.startActivity(CreationActivity::class.java) {}
                    }
                    else -> {
                        val dialog = CreationAgreementDialog()
                        dialog.mListener = {
                            mActivity.startActivity(CreationActivity::class.java) {}
                        }
                        dialog.show(mActivity.supportFragmentManager, "")
                    }
                }
            }
        }
        ll_my_message.setOnClickListener {
            if (checkLogin()) {
                mActivity.startActivity(MessageActivity::class.java) {}
            }
        }
        ll_my_follow.setOnClickListener {
            if (checkLogin()) {
                mActivity.startActivity(FollowUserActivity::class.java) {}
            }
        }
        ll_my_fans.setOnClickListener {
            if (checkLogin()) {
                mActivity.startActivity(FansActivity::class.java, {})
            }
        }
        ll_my_collect.setOnClickListener {
            if (checkLogin()) {
                mActivity.startActivity(CollectCardActivity::class.java, {})
            }
        }
        ll_my_like.setOnClickListener {
            if (checkLogin()) {
                mActivity.startActivity(LikeCardsActivity::class.java, {})
            }
        }
        ll_my_setting.setOnClickListener {
            mActivity.startActivity(CommonSettingActivity::class.java, {})
        }
        //添加监听前初始化
        cb_my_night_model.isChecked = Constants.IS_NIGHT
        cb_my_night_model.setOnClickListener { _ ->
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

        ll_my_rank.setOnClickListener {
            mActivity.startActivity(NewRankActivity::class.java){}
        }
    }

    private fun setViewLogout() {
        tv_my_nick_name.text = "注册/登录"
        tv_my_nick_name.setTextColor(mActivity.getColorForKotlin(R.color.button_blue))
        tv_my_lv.visibility = View.GONE
        tv_my_signature.text = ""
        iv_my_head.setImageResource(R.drawable.iv_my_head_placeholder)
        tv_my_top_read_num.text = "0"
        tv_my_top_creation_num.text = "0"
        tv_my_top_fans_num.text = "0"
    }

    private fun setViewLogin() {
        tv_my_lv.visibility = View.VISIBLE
        tv_my_nick_name.text = SharedPreferencesUtil.getString(SVTSConstants.nickName, "")
        tv_my_nick_name.setTextColor(mActivity.getColorForKotlin(R.color.button_blue))
        tv_my_signature.text = SharedPreferencesUtil.getString(SVTSConstants.signature, "")
        val headImg = SharedPreferencesUtil.getString(SVTSConstants.imgUrl, "")
        ImageLoader.load(mActivity, headImg, R.drawable.default_header, iv_my_head)
        writerStatus = SharedPreferencesUtil.getInt(SVTSConstants.writerStatus, 0)
        hasDynamicMessage = SharedPreferencesUtil.getBoolean(SVTSConstants.hasDynamicMessage, false)
        hasSystemMessage = SharedPreferencesUtil.getBoolean(SVTSConstants.hasSystemMessage, false)
        totalIntelligenceValue = SharedPreferencesUtil.getInt(SVTSConstants.totalIntelligenceValue, 0)
        readNum = SharedPreferencesUtil.getInt(SVTSConstants.readNum, 0)
        creationNum = SharedPreferencesUtil.getInt(SVTSConstants.creationNum, 0)
        fansNum = SharedPreferencesUtil.getInt(SVTSConstants.fansNum, 0)
        level = SharedPreferencesUtil.getInt(SVTSConstants.level, 0)
        actionType = SharedPreferencesUtil.getInt(SVTSConstants.actionType,0)
        refreshRedPoint()
    }

    private fun getRedPointStatus() {
        if (SharedPreferencesUtil.getInt(SVTSConstants.userId, 0) == 0) {
            return
        }
        mDisposable = AppClient.getAPIService().myTabInfo
                .compose(BaseRxJava.handleResult())
                .compose(BaseRxJava.io_main())
                .subscribeWith(object : BaseSubscriber<MyTabBean>(mPresenter) {
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
        fansNum = bean.fansCount
        level = bean.level
        actionType = bean.actionType
        SharedPreferencesUtil.saveData(SVTSConstants.writerStatus, writerStatus)
        SharedPreferencesUtil.saveData(SVTSConstants.hasDynamicMessage, hasDynamicMessage)
        SharedPreferencesUtil.saveData(SVTSConstants.hasSystemMessage, hasSystemMessage)
        SharedPreferencesUtil.saveData(SVTSConstants.totalIntelligenceValue, totalIntelligenceValue)
        SharedPreferencesUtil.saveData(SVTSConstants.readNum, readNum)
        SharedPreferencesUtil.saveData(SVTSConstants.creationNum, creationNum)
        SharedPreferencesUtil.saveData(SVTSConstants.noteNum, fansNum)
        SharedPreferencesUtil.saveData(SVTSConstants.level, level)
        SharedPreferencesUtil.saveData(SVTSConstants.actionType,actionType)
        refreshRedPoint()
    }


    override fun createPresenter(): MyFragmentPresenter {
        return MyFragmentPresenter(this)
    }

    override fun getADSuccess(list: MutableList<ADBean>) {

        var title = ""
        var url = ""
        var id1 = 0

        if (list.size > 0) {
            mAdList = list
            mAdList.forEach {
                addImageData(it.titleImage)
                title = it.description
                url = it.behavior
                id1 = it.id
            }

            SharedPreferencesUtil.saveData(SVTSConstants.homeMyLastOpenedTime,System.currentTimeMillis())
            SharedPreferencesUtil.saveData(SVTSConstants.homeMyLastOpenedID,id1)
            
            showImageDialog(title, url)
        }
    }


    private fun addImageData(url: String) {
        val adInfo = AdInfo()
        adInfo.activityImg = url
        advList.add(adInfo)
    }

    private fun showImageDialog(title: String, url: String) {
        val adManager = AdManager(activity, advList)
        adManager.setOverScreen(true)
                .setPageTransformer(DepthPageTransformer())
                .setOnImageClickListener { _, _ ->
                    val intent = Intent(context, WebViewActivity::class.java)
                    intent.putExtra("title", title)
                    intent.putExtra("url", url)
                    startActivity(intent)
                }
        adManager.showAdDialog(AdConstant.ANIM_DOWN_TO_UP)
    }

    private fun refreshRedPoint() {
        if (SharedPreferencesUtil.getInt(SVTSConstants.userId, 0) == 0) {
            return
        }
        tv_my_lv.text = getString(R.string.tv_level, level)
        tv_my_top_read_num.text = readNum.toString()
        tv_my_top_creation_num.text = creationNum.toString()
        tv_my_top_fans_num.text = fansNum.toString()

        if (actionType != 0 && actionType == 1) {
            iv_item_card_officials.visibility = View.VISIBLE
        }
    }

    private fun onRefreshAD(lastADTime:Long,lastADID:Long) {
        mPresenter.getAD(lastADTime,lastADID)
    }
}
