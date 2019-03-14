package com.zxcx.zhizhe.ui.circle.circlequestiondetail

import android.os.Bundle
import android.support.design.widget.BottomSheetBehavior
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.entity.MultiItemEntity
import com.zxcx.zhizhe.R
import com.zxcx.zhizhe.mvpBase.MvpActivity
import com.zxcx.zhizhe.ui.circle.circledetaile.CircleDetailBean
import com.zxcx.zhizhe.utils.ImageLoader
import com.zxcx.zhizhe.utils.LogCat
import com.zxcx.zhizhe.utils.SVTSConstants
import com.zxcx.zhizhe.utils.SharedPreferencesUtil
import com.zxcx.zhizhe.widget.CommentLoadMoreView
import kotlinx.android.synthetic.main.activity_question_detail.*
import kotlinx.android.synthetic.main.toolbar.*

/**
 * @author : MarkFrank01
 * @Created on 2019/3/13
 * @Description :
 */
class CircleQuestionDetailActivity : MvpActivity<CircleQuestionDetailPresenter>(), CircleQuestionDetailContract.View,
        BaseQuickAdapter.OnItemClickListener,BaseQuickAdapter.OnItemChildClickListener, BaseQuickAdapter.RequestLoadMoreListener {



    private lateinit var mAdapter:CircleQuestionDetailCommentAdapter
    private var mPage = 0
    private var parentCommentId: Int? = null
    private lateinit var behavior: BottomSheetBehavior<View>

    private var mUserId = SharedPreferencesUtil.getInt(SVTSConstants.userId, 0)


    private var huatiID = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_question_detail)
        initData()
        initView()
        initRecyclerView()


        mPresenter.getQuestionInfo(huatiID)
        mPresenter.getAnswerList(huatiID,mPage)
    }


    override fun createPresenter(): CircleQuestionDetailPresenter {
        return CircleQuestionDetailPresenter(this)
    }

    override fun getCommentBeanSuccess(list: MutableList<CircleCommentBean>) {
        LogCat.e("显示把"+list.size)
        if (mPage == 0){
            mAdapter.setNewData(list as List<MultiItemEntity>)
//            val emptyView = EmptyView.getEmptyView2(mActivity,"暂无评论",R.drawable.no_comment)
//            mAdapter.emptyView = emptyView
        }else{
            mAdapter.addData(list)
        }

        mPage++
        if (list.isEmpty()){
            mAdapter.loadMoreEnd(false)
        }else{
            mAdapter.loadMoreComplete()
            mAdapter.setEnableLoadMore(false)
            mAdapter.setEnableLoadMore(true)
        }
    }

    override fun getBasicQuestionSuccess(bean: CircleDetailBean) {
        if (bean.qaImageEntityList.isNotEmpty()){
            ImageLoader.load(this,bean.qaImageEntityList[0],R.drawable.default_card,question_pic)
        }else{
            question_pic.visibility = View.GONE
        }

        ImageLoader.load(this,bean.usersVO.avater,R.drawable.default_card,question_push)

        question_title.text = bean.title
        question_desc.text = bean.description
        question_time.text = bean.createTime
        circle_detail_username.text = bean.usersVO.name
        tv_item_card_read1.text = ""+bean.pv
        tv_item_card_comment1.text = ""+bean.commentCount
        tv_item_card_dianzan.text = ""+bean.likeCount
    }

    override fun getDataSuccess(bean: CircleDetailBean?) {
    }

    override fun likeSuccess() {
    }

    override fun unlikeSuccess() {
    }

    override fun onItemChildClick(adapter: BaseQuickAdapter<*, *>?, view: View?, position: Int) {
    }

    override fun onItemClick(adapter: BaseQuickAdapter<*, *>, view: View, position: Int) {
        var bean = adapter.getItem(position) as MultiItemEntity
        if (bean.itemType == CircleCommentBean.TYPE_LEVEL_0){
            bean = bean as CircleCommentBean
            toastShow(getString(R.string.et_comment_hint, bean.authorVO?.name))
        }
    }

    override fun onLoadMoreRequested() {
        mPresenter.getAnswerList(huatiID,mPage)
    }

    private fun initView(){
        initToolBar()
        iv_toolbar_right.visibility = View.VISIBLE
        iv_toolbar_right.setImageResource(R.drawable.c_more_2)
    }

    private fun initData(){
        huatiID = intent.getIntExtra("huatiId",0)
    }

    private fun initRecyclerView(){
        mAdapter = CircleQuestionDetailCommentAdapter(arrayListOf())
        mAdapter.setLoadMoreView(CommentLoadMoreView())
        mAdapter.setOnLoadMoreListener(this,rv_ht_comment)
        mAdapter.onItemClickListener = this
        mAdapter.onItemChildClickListener = this

        rv_ht_comment.layoutManager = object :LinearLayoutManager(this){
            override fun canScrollVertically() = false
        }

        rv_ht_comment.isNestedScrollingEnabled = false
        rv_ht_comment.setHasFixedSize(true)
        rv_ht_comment.isFocusable = false

        rv_ht_comment.adapter = mAdapter

    }


}