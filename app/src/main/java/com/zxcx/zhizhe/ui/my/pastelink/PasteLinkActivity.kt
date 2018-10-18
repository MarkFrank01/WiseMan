package com.zxcx.zhizhe.ui.my.pastelink

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import com.zxcx.zhizhe.R
import com.zxcx.zhizhe.event.PushPastEvent
import com.zxcx.zhizhe.mvpBase.MvpActivity
import com.zxcx.zhizhe.ui.my.readCards.MyCardItemDecoration
import kotlinx.android.synthetic.main.activity_paste_link.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

/**
 * 黏贴作品链接页面
 */

class PasteLinkActivity:MvpActivity<PasteLinkPresenter>(),PasteLinkContract.View{

    private lateinit var mAdapter: PasteLinkAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_paste_link)
//        EventBus.getDefault().register(this)

        initView()
    }

    override fun onDestroy() {
        EventBus.getDefault().unregister(this)
        super.onDestroy()
    }

    override fun createPresenter(): PasteLinkPresenter {
        return PasteLinkPresenter(this)
    }

    override fun postSuccess() {
    }

    override fun postFail(msg: String?) {
        toastError(msg)
    }

    override fun getDataSuccess(list: List<PastLinkBean>?) {
        mAdapter.setNewData(list)
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onMessageEvent(event: PushPastEvent){
        mPresenter.pushLinkList()
    }


    private fun initView(){
        mAdapter = PasteLinkAdapter(ArrayList())
        //还需加上点击添加新的item
        rv_past_link.layoutManager = LinearLayoutManager(mActivity,LinearLayoutManager.VERTICAL,false)
        rv_past_link.adapter = mAdapter

        //item之间的间距，待调整
        rv_past_link.addItemDecoration(MyCardItemDecoration())

        var test=PastLinkBean(1,"0")
        var test1=PastLinkBean(1,"0")
        var test2=PastLinkBean(1,"0")
        var test3=PastLinkBean(1,"0")
        mAdapter.addData(test)
        mAdapter.addData(test1)
        mAdapter.addData(test2)
        mAdapter.addData(test3)
    }

    override fun setListener() {
        tv_toolbar_back.setOnClickListener {
            onBackPressed()
        }

    }
}