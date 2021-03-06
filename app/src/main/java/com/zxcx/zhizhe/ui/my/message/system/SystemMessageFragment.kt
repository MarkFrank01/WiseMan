package com.zxcx.zhizhe.ui.my.message.system

import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.chad.library.adapter.base.BaseQuickAdapter
import com.zxcx.zhizhe.R
import com.zxcx.zhizhe.mvpBase.MvpFragment
import com.zxcx.zhizhe.ui.article.articleDetails.ArticleDetailsActivity
import com.zxcx.zhizhe.ui.card.hot.CardBean
import com.zxcx.zhizhe.ui.circle.circledetaile.CircleDetaileActivity
import com.zxcx.zhizhe.ui.my.creation.ApplyForCreation1Activity
import com.zxcx.zhizhe.ui.my.creation.creationDetails.RejectCardDetailsActivity
import com.zxcx.zhizhe.ui.my.creation.creationDetails.RejectDetailsActivity
import com.zxcx.zhizhe.ui.my.creation.newCreation.CreationEditorActivity
import com.zxcx.zhizhe.ui.my.intelligenceValue.IntelligenceValueActivity
import com.zxcx.zhizhe.ui.my.writer_status_reject
import com.zxcx.zhizhe.ui.my.writer_status_user
import com.zxcx.zhizhe.ui.welcome.WebViewActivity
import com.zxcx.zhizhe.utils.*
import com.zxcx.zhizhe.widget.CustomLoadMoreView
import com.zxcx.zhizhe.widget.EmptyView
import kotlinx.android.synthetic.main.fragment_system_message.*

/**
 * 系统消息
 */

class SystemMessageFragment : MvpFragment<SystemMessagePresenter>(), SystemMessageContract.View,
        BaseQuickAdapter.RequestLoadMoreListener, BaseQuickAdapter.OnItemChildClickListener {

    private var mPage = 0
    private val mPageSize = Constants.PAGE_SIZE
    private lateinit var mAdapter: SystemMessageAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_system_message, container, false)
    }

    override fun createPresenter(): SystemMessagePresenter {
        return SystemMessagePresenter(this)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        SharedPreferencesUtil.saveData(SVTSConstants.hasDynamicMessage, true)
        initRecyclerView()
    }

    override fun onResume() {
        super.onResume()
        mPresenter.getSystemMessage(mPage, mPageSize)
    }

    override fun getDataSuccess(list: List<SystemMessageBean>) {
        if (mPage == 0) {
            mAdapter.setNewData(list)
        } else {
            mAdapter.addData(list)
        }
        mPage++
        if (list.size < Constants.PAGE_SIZE) {
            mAdapter.isUseEmpty(true)
            mAdapter.loadMoreEnd(false)
        } else {
            mAdapter.loadMoreComplete()
            mAdapter.setEnableLoadMore(false)
            mAdapter.setEnableLoadMore(true)
        }
    }

    override fun getCardSuccess(bean: CardBean) {
        if (bean.cardType == 1) {
            mActivity.startActivity(RejectCardDetailsActivity::class.java) {
                it.putExtra("cardBean", bean)
            }
        } else {
            mActivity.startActivity(RejectDetailsActivity::class.java) {
                it.putExtra("cardBean", bean)
            }
        }
    }

    override fun getCardNoFound() {
        toastShow("卡片已被重新编辑")
    }

    override fun onLoadMoreRequested() {
        mPresenter.getSystemMessage(mPage, mPageSize)
    }

    override fun onItemChildClick(adapter: BaseQuickAdapter<*, *>?, view: View?, position: Int) {

        val bean = mAdapter.data[position]

        when (view?.id) {
            R.id.tv_item_system_message_action -> {
                val intent = Intent()
                when (bean.messageType) {
                    message_card_pass -> {
                        intent.setClass(mActivity, ArticleDetailsActivity::class.java)
                        val cardBean = CardBean()
                        cardBean.id = bean.relatedCardId ?: 0
                        intent.putExtra("cardBean", cardBean)
                        startActivity(intent)
                    }
                    message_card_reject -> {
                        bean.relatedCardId?.let { mPresenter.getRejectDetails(it) }
                        return
                    }
                    message_apply_pass -> {
                        intent.setClass(mActivity, CreationEditorActivity::class.java)
                        startActivity(intent)
                    }
                    message_apply_reject -> {
                        if (SharedPreferencesUtil.getInt(SVTSConstants.writerStatus, 0) == writer_status_user
                                || SharedPreferencesUtil.getInt(SVTSConstants.writerStatus, 0) == writer_status_reject) {
                            //没有创作资格
                            intent.setClass(mActivity, ApplyForCreation1Activity::class.java)
                            startActivity(intent)
                        } else {
                            return
                        }
                    }
                    message_rank -> {
//                        intent.setClass(mActivity, AllRankActivity::class.java)
                        //修改为去看智力值
                        intent.setClass(mActivity, IntelligenceValueActivity::class.java)
                        startActivity(intent)
                    }
                    message_recommend -> {
                        intent.setClass(mActivity, ArticleDetailsActivity::class.java)
                        val cardBean = CardBean()
                        cardBean.id = bean.relatedCardId ?: 0
                        intent.putExtra("cardBean", cardBean)
                        startActivity(intent)
                    }
                    message_link_pass -> {
                        LogCat.e("url${bean.remaskContent}")
//                intent.setClass(mActivity,WebViewActivity::class.java)
//                intent.putExtra("title","我的作品")
//                intent.putExtra("url", bean.remaskContent)
                        intent.setClass(mActivity, ArticleDetailsActivity::class.java)
                        val cardBean = CardBean()
                        cardBean.id = bean.relatedCardId ?: 0
                        intent.putExtra("cardBean", cardBean)
                        startActivity(intent)
                    }
                    message_link_unpass -> {
                        LogCat.e("url${bean.remaskContent}")
                        intent.setClass(mActivity, WebViewActivity::class.java)
//                        intent.putExtra("title", "一键发布作品链接")
                        intent.putExtra("title", "作品链接详情")
                        intent.putExtra("url", bean.remaskContent)
                        startActivity(intent)
                    }

                    MESSAGE_TYPE_SYSTEM_CIRCLE_PASS->{
                        toastShow("圈子通过")
                        intent.setClass(mActivity,CircleDetaileActivity::class.java)
                        intent.putExtra("circleID",bean.relatedCircleId)
                        startActivity(intent)
                    }

                    MESSAGE_TYPE_SYSTEM_CIRCLE_UNPASS->{
                        toastShow("圈子不通过")
                    }

                    MESSAGE_TYPE_SYSTEM_CIRCLE_IS_CLOSED->{
                        toastShow("圈子被关闭")
                    }

                    MESSAGE_TYPE_SYSTEM_CIRCLE_NOT_SUBMITTED->{
                        toastShow("圈子未提交")
                    }

                    MESSAGE_TYPE_SYSTEM_CIRCLE_EDITING_PASS->{
                        toastShow("圈子编辑通过")
                    }

                    MESSAGE_TYPE_SYSTEM_CIRCLE_EDITING_UNPASS->{
                        toastShow("圈子编辑不通过")
                    }

                    MESSAGE_TYPE_SYSTEM_CIRCLE_HAS_WITHDRAW->{
                        toastShow("圈子已提现")
                    }
                }

//                startActivity(intent)
            }

            R.id.tv_item_system_message_content -> {
                val intent1 = Intent()
                intent1.setClass(mActivity, WebViewActivity::class.java)
                intent1.putExtra("title", "智者创作协议")
                intent1.putExtra("url", getString(R.string.base_url) + getString(R.string.creation_agreement_url))
                startActivity(intent1)
            }
        }


    }

    private fun initRecyclerView() {
        mAdapter = SystemMessageAdapter(ArrayList())
        mAdapter.onItemChildClickListener = this
        mAdapter.setLoadMoreView(CustomLoadMoreView())
        mAdapter.setOnLoadMoreListener(this, rv_system_message)
        rv_system_message.layoutManager = LinearLayoutManager(mActivity, LinearLayoutManager.VERTICAL, false)
        rv_system_message.adapter = mAdapter
        rv_system_message.addItemDecoration(SystemMessageItemDecoration())
        val emptyView = EmptyView.getEmptyView(mActivity, "暂无内容", R.drawable.no_data)
        mAdapter.emptyView = emptyView
        mAdapter.isUseEmpty(false)
    }
}