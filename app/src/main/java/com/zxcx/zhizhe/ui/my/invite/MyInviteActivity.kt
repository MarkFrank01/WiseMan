package com.zxcx.zhizhe.ui.my.invite

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import com.chad.library.adapter.base.BaseQuickAdapter
import com.lxj.xpopup.XPopup
import com.lxj.xpopup.interfaces.OnSelectListener
import com.zxcx.zhizhe.R
import com.zxcx.zhizhe.mvpBase.MvpActivity
import com.zxcx.zhizhe.ui.my.invite.input.InputInviteActivity
import com.zxcx.zhizhe.utils.LogCat
import com.zxcx.zhizhe.utils.startActivity
import com.zxcx.zhizhe.widget.bottomsharepopup.CircleBottomSharePopup
import kotlinx.android.synthetic.main.activity_my_invite.*
import kotlinx.android.synthetic.main.toolbar.*

/**
 * @author : MarkFrank01
 * @Created on 2019/3/23
 * @Description :
 */
class MyInviteActivity : MvpActivity<MyInvitePresenter>(), MyInviteContract.View,
        BaseQuickAdapter.OnItemChildClickListener {


    private lateinit var mAdapter: MyInviteAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_invite)

        initToolBar()
        tv_toolbar_right.visibility = View.VISIBLE
        tv_toolbar_right.text = "填写邀请码"

        initRecyclerView()

        mPresenter.getInvitationHistory()
        mPresenter.getInvitationInfo()
    }



    override fun setListener() {
        copy_my_invite_text.setOnClickListener {
            var text = copy_my_invite_text.text.toString().trim()
            putTextInto(this, text)
            toastShow("复制成功")
        }

        sb_wenan.setOnClickListener {
            var text = copy_my_invite_text.text.toString().trim()
            putTextInto(this, text)
            toastShow("复制成功")
        }

        share_to_invite.setOnClickListener {
            showshare()
        }

        tv_toolbar_right.setOnClickListener {
            mActivity.startActivity(InputInviteActivity::class.java) {}
        }
    }

    override fun createPresenter(): MyInvitePresenter {
        return MyInvitePresenter(this)
    }

    override fun getInvitationHistorySuccess(list: MutableList<InviteBean>) {
        LogCat.e("list size is " + list.size)
        mAdapter.setNewData(list)

        if (list.size < 1) {
            rv_my_invite.visibility = View.GONE
            show_empty_1.visibility = View.VISIBLE
            show_empty_2.visibility = View.VISIBLE
        }
    }

    override fun getInvitationInfoSuccess(bean: InviteBean) {
        copy_my_invite_text.text = bean.invitationCode
        man_num.text = "" + bean.alreadyInviteesTotal + "/" + bean.inviteesTotal
    }

    override fun receiveInvitationCodeReward(bean: InviteBean) {
        toastShow("领取成功")
    }


    override fun getDataSuccess(bean: MutableList<InviteBean>?) {
    }

    override fun onItemChildClick(adapter: BaseQuickAdapter<*, *>, view: View, position: Int) {
        val bean = adapter.data[position] as InviteBean
        when(view.id){
            R.id.invite_get->{
                mPresenter.receiveInvitationCodeReward(bean.userId)
                bean.hasReceiveReward = true
                mAdapter.notifyItemChanged(position)
            }
        }
    }

    //粘贴板
    private fun putTextInto(context: Context, text: String) {
        var clipManager: ClipboardManager = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        //创建clipData对象
        var clipData: ClipData = ClipData.newPlainText("", text)
        clipManager.primaryClip = clipData
    }

    //弹出分享四兄弟
    private fun showshare() {
        XPopup.Builder(mActivity)
                .asCustom(CircleBottomSharePopup(this,
                        OnSelectListener { position, text ->
                            when (position) {
                                1 -> {
                                    toastShow("微信")
                                }

                                2 -> {
                                    toastShow("朋友圈")
                                }

                                3 -> {
                                    toastShow("球球号")
                                }
                                4 -> {
                                    toastShow("微博")
                                }
                            }
                        })
                ).show()
    }

    private fun initRecyclerView() {
        mAdapter = MyInviteAdapter(ArrayList())
        mAdapter.onItemChildClickListener = this
        rv_my_invite.layoutManager = LinearLayoutManager(mActivity, LinearLayoutManager.HORIZONTAL, false)
        rv_my_invite.adapter = mAdapter
    }
}