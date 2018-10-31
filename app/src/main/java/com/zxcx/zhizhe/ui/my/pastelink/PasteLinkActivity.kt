package com.zxcx.zhizhe.ui.my.pastelink

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.support.v7.widget.AppCompatEditText
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.widget.LinearLayout
import com.chad.library.adapter.base.BaseQuickAdapter
import com.zxcx.zhizhe.R
import com.zxcx.zhizhe.event.PushPastEvent
import com.zxcx.zhizhe.mvpBase.MvpActivity
import com.zxcx.zhizhe.ui.my.creation.CreationActivity
import com.zxcx.zhizhe.ui.my.creation.newCreation.CanNotSaveDialog
import com.zxcx.zhizhe.ui.my.readCards.MyCardItemDecoration
import com.zxcx.zhizhe.ui.welcome.WebViewActivity
import com.zxcx.zhizhe.utils.Utils
import com.zxcx.zhizhe.utils.startActivity
import com.zxcx.zhizhe.widget.UploadingDialog
import kotlinx.android.synthetic.main.activity_paste_link.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import java.util.*

/**
 * 黏贴作品链接页面
 */

class PasteLinkActivity : MvpActivity<PasteLinkPresenter>(), PasteLinkContract.View, PasteLinkClickListener {


    private lateinit var mAdapter: PasteLinkAdapter
    private lateinit var mUploadingDialog: UploadingDialog

    private var mSize: Int = 0
    private var mList2 = ArrayList<String>()
    private var mNotAdd: Boolean = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_paste_link)
        initView()
        EventBus.getDefault().register(this)
        mUploadingDialog = UploadingDialog()

        mAdapter.onItemChildClickListener = BaseQuickAdapter.OnItemChildClickListener { adapter, view, position ->

            val bean = adapter.data[position] as PastLinkBean

            when (view) {
                is LinearLayout -> {
                    val dialog = DeleteLinkDialog()
                    dialog.mCancelListener = {

                    }
                    dialog.mConfirmListener = {
                        if (mSize == 1) {
                            toastShow("第一个不能删除哦")
                        }
                        if (position == mSize - 1) {
                            mNotAdd = false
                        }
                        if (mSize > 1) {
                            adapter.remove(position)
                            adapter.notifyItemRemoved(position)
                            mList2.remove(mList2[position])
                            mSize--

                            ll_add_more_link.isEnabled = true
                        }
                    }

                    dialog.show(supportFragmentManager, "")
                }

                is AppCompatEditText -> {
                    startActivity(WebViewActivity::class.java) {
                        it.putExtra("url", bean.link)
                        it.putExtra("title", "我的作品")
                        it.putExtra("imageUrl", bean.link)
                        it.putExtra("isAD", true)
                    }
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
        runOnUiThread {
            mUploadingDialog.setSuccess(true)
        }
        Handler().postDelayed({
            //            EventBus.getDefault().post(PushPastEvent())
            startActivity(CreationActivity::class.java) {
                it.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                it.putExtra("goto", 1)
            }
            Utils.closeInputMethod(mActivity)
            finish()
        }, 1000)

    }

    override fun postFail(msg: String?) {
        mUploadingDialog.setSuccess(false)
    }

    override fun getDataSuccess(list: List<PastLinkBean>?) {
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onMessageEvent(event: PushPastEvent) {
        mPresenter.pushLinkList(mList2)
    }


    private fun initView() {
        mAdapter = PasteLinkAdapter(ArrayList())
        mAdapter.mListener = this
        //还需加上点击添加新的item
        rv_past_link.layoutManager = LinearLayoutManager(mActivity, LinearLayoutManager.VERTICAL, false)
        rv_past_link.adapter = mAdapter

        //item之间的间距，待调整
        rv_past_link.addItemDecoration(MyCardItemDecoration())

        var test = PastLinkBean(0, "", false)
        mAdapter.addData(test)
        mSize++
        mList2.add("")

    }

    override fun setListener() {
        tv_toolbar_back.setOnClickListener {
            val dialog = CanNotSaveDialog()
            dialog.mCancelListener = {

            }

            dialog.mConfirmListener = {
                onBackPressed()
            }

            dialog.show(supportFragmentManager, "")
        }

        tv_toolbar_right.setOnClickListener {

            Handler().postDelayed({
                EventBus.getDefault().post(PushPastEvent())
                val bundle = Bundle()
                bundle.putString("uploadingText", "正在提交")
                bundle.putString("successText", "审核中")
                bundle.putString("failText", "提交失败")
                mUploadingDialog.arguments = bundle
                mUploadingDialog.show(supportFragmentManager, "")
            }, 1000)
        }


        ll_add_more_link.setOnClickListener {
            if (!mNotAdd) {
                if (mSize < 10) {
                    ll_add_more_link.isEnabled = true
                    mAdapter.addData(PastLinkBean(mSize + 1, "", true))
                    mAdapter.notifyItemInserted(mSize + 1)
                    mSize++

                    mList2.add("")
                } else {
                    ll_add_more_link.isEnabled = false
                }
            }
            mNotAdd = true
        }

    }


    override fun onClickSave(position: Int) {
        val bean = mAdapter.data[position] as PastLinkBean

        mList2[position] = bean.link!!

        tv_toolbar_right.isEnabled = mList2[position] != ""
        mNotAdd = mList2[position] != ""

    }

    override fun onItemIsNull(isNull: Boolean) {
        mNotAdd = isNull

    }

}