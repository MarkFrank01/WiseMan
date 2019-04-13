package com.zxcx.zhizhe.ui.circle.circlemessage

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import com.chad.library.adapter.base.BaseQuickAdapter
import com.zxcx.zhizhe.R
import com.zxcx.zhizhe.mvpBase.MvpActivity
import com.zxcx.zhizhe.ui.circle.circlemessage.question.CircleMesQaActivity
import com.zxcx.zhizhe.ui.circle.circlemessage.zan.CircleMesZanActivity
import com.zxcx.zhizhe.ui.circle.circlequestion.circleanwser.CircleAnswerChildActivity
import com.zxcx.zhizhe.utils.Constants
import com.zxcx.zhizhe.utils.LogCat
import com.zxcx.zhizhe.utils.startActivity
import com.zxcx.zhizhe.widget.CustomLoadMoreView
import com.zxcx.zhizhe.widget.EmptyView
import kotlinx.android.synthetic.main.activity_circle_message.*

/**
 * @author : MarkFrank01
 * @Created on 2019/3/19
 * @Description : 动态的首页
 */
class CircleMessageActivity : MvpActivity<CircleMessagePresenter>(), CircleMessageContract.View,
        BaseQuickAdapter.OnItemChildClickListener, BaseQuickAdapter.RequestLoadMoreListener {

    private var page = 0

    private lateinit var mAdapter:CircleMessageAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_circle_message)

        initView()
        initRecycleView()

        mPresenter.getCircleTabInfo()
        mPresenter.getCommentMessageList(page, Constants.PAGE_SIZE)
    }

    override fun setListener() {
        ll_to_qa.setOnClickListener {
            mActivity.startActivity(CircleMesQaActivity::class.java){}
        }

        ll_to_dianzan.setOnClickListener {
            mActivity.startActivity(CircleMesZanActivity::class.java){}
        }
    }

    override fun createPresenter(): CircleMessagePresenter {
        return CircleMessagePresenter(this)
    }

    override fun getRedPointStatusSuccess(bean: MyCircleTabBean) {
        if (bean.hasCircleQuestionDynamicMessage) {
            iv_tab_red_point_1.visibility = View.VISIBLE
        } else {
            iv_tab_red_point_1.visibility = View.GONE
        }

        if (bean.hasCircleLikeDynamicMessage) {
            iv_tab_red_point_2.visibility = View.VISIBLE
        } else {
            iv_tab_red_point_2.visibility = View.GONE
        }
    }

    override fun getCommentMessageListSuccess(list: MutableList<MyCircleTabBean>) {
        LogCat.e("MessageList" + list.size)

        val  emptyView = EmptyView.getEmptyView(mActivity,"暂时没有动态",R.drawable.no_data)
        mAdapter.emptyView = emptyView

        if (page == 0){
            mAdapter.setNewData(list)
        }else {
            mAdapter.addData(list)
        }

        mAdapter.notifyDataSetChanged()

        page++

        if (list.isEmpty()){
            mAdapter.loadMoreEnd(false)
        }else{
            mAdapter.loadMoreComplete()
            mAdapter.setEnableLoadMore(false)
            mAdapter.setEnableLoadMore(true)
        }
    }

    override fun getDataSuccess(bean: MyCircleTabBean?) {
    }

    override fun onItemChildClick(adapter: BaseQuickAdapter<*, *>, view: View, position: Int) {
        val bean = adapter.data[position] as MyCircleTabBean
        when(bean.messageType){
            301 ->{
//                toastShow("HH")
//                mActivity.startActivity(CircleAnswerChildActivity::class.java){
//                    it.putExtra("qaId",huatiID)
//                    it.putExtra("CircleId",circleId)
//                    it.putExtra("name",bean.authorVO?.name)
//                    it.putExtra("qaCommentId",bean.id)
//                }

                mActivity.startActivity(CircleAnswerChildActivity::class.java){
                    it.putExtra("qaId",bean.relatedQaId)
                    it.putExtra("CircleId",bean.relatedCircleId)
                    it.putExtra("name",bean.relatedUserName)
                    it.putExtra("qaCommentId",bean.relatedCommentId)
                }
            }
        }
    }

    override fun onLoadMoreRequested() {
        mPresenter.getCommentMessageList(page, Constants.PAGE_SIZE)
    }


    private fun initView() {
        initToolBar("圈子动态")

    }

    private fun initRecycleView(){
        mAdapter = CircleMessageAdapter(ArrayList())
        mAdapter.setLoadMoreView(CustomLoadMoreView())
        mAdapter.setOnLoadMoreListener(this,rv_circle_message)
        mAdapter.onItemChildClickListener = this

        rv_circle_message.layoutManager = object :LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false){
            override fun canScrollVertically() = false
        }

        rv_circle_message.adapter = mAdapter
    }


//    override fun onResume() {
//        super.onResume()
//        if (SharedPreferencesUtil.getInt(SVTSConstants.userId,0)!= 0){
//            getRedPointStatus()
//        }
//    }
//

}