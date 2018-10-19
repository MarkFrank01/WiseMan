package com.zxcx.zhizhe.ui.my.pastelink

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.widget.EditText
import android.widget.LinearLayout
import com.chad.library.adapter.base.BaseQuickAdapter
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

class PasteLinkActivity : MvpActivity<PasteLinkPresenter>(), PasteLinkContract.View ,PasteLinkListener{

    private lateinit var mAdapter: PasteLinkAdapter
    var mSize: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_paste_link)
//        EventBus.getDefault().register(this)

        initView()

        mAdapter.onItemChildClickListener = BaseQuickAdapter.OnItemChildClickListener { adapter, view, position ->

            when (view) {
                is LinearLayout -> {
                    if (mSize > 1) {
                        adapter.remove(position)
                        adapter.notifyItemRemoved(position)
                        mSize--
                    }
                }

                is EditText -> {
                    toastShow("save")
                }
            }

        }

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
    fun onMessageEvent(event: PushPastEvent) {
        mPresenter.pushLinkList()
    }


    private fun initView() {
        mAdapter = PasteLinkAdapter(ArrayList())
        //还需加上点击添加新的item
        rv_past_link.layoutManager = LinearLayoutManager(mActivity, LinearLayoutManager.VERTICAL, false)
        rv_past_link.adapter = mAdapter

        //item之间的间距，待调整
        rv_past_link.addItemDecoration(MyCardItemDecoration())

        var test = PastLinkBean(0, "", false)
        mAdapter.addData(test)
        mSize++


    }

    override fun setListener() {
        tv_toolbar_back.setOnClickListener {
            onBackPressed()
        }

        ll_add_more_link.setOnClickListener {
            if (mSize < 10) {
                mAdapter.addData(PastLinkBean(0, "", true))
//                mAdapter.notifyDataSetChanged()
                mAdapter.notifyItemInserted(mSize + 1)
                mSize++

            } else {
                toastShow("最多添加十个")
            }
        }
    }

    override fun onSaveLink(position: Int) {

    }
}