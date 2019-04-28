package com.zxcx.zhizhe.ui.circle.circlequestiondetail

import android.os.Bundle
import android.support.design.widget.BottomSheetBehavior
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import android.widget.CheckBox
import android.widget.TextView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.entity.MultiItemEntity
import com.youth.banner.BannerConfig
import com.zxcx.zhizhe.R
import com.zxcx.zhizhe.mvpBase.MvpActivity
import com.zxcx.zhizhe.ui.circle.circledetaile.CircleDetailBean
import com.zxcx.zhizhe.ui.circle.circlequestion.circleanwser.CircleAnswerActivity
import com.zxcx.zhizhe.ui.circle.circlequestion.circleanwser.CircleAnswerChildActivity
import com.zxcx.zhizhe.utils.*
import com.zxcx.zhizhe.widget.CommentLoadMoreView
import kotlinx.android.synthetic.main.activity_question_detail.*

/**
 * @author : MarkFrank01
 * @Created on 2019/3/13
 * @Description : 提问的详情的列表
 */
class CircleQuestionDetailActivity : MvpActivity<CircleQuestionDetailPresenter>(), CircleQuestionDetailContract.View,
        BaseQuickAdapter.OnItemClickListener,BaseQuickAdapter.OnItemChildClickListener, BaseQuickAdapter.RequestLoadMoreListener {


    //图片的数据
    private var imageList: MutableList<String> = mutableListOf()

    private lateinit var mAdapter:CircleQuestionDetailCommentAdapter
    private var mPage = 0
    private var parentCommentId: Int? = null
    private lateinit var behavior: BottomSheetBehavior<View>

    private var mUserId = SharedPreferencesUtil.getInt(SVTSConstants.userId, 0)

    //就是qaId
    private var huatiID = 0

    //CircleId
    private var circleId = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_question_detail)
        initData()
        initView()
        initADView()
        initRecyclerView()


        mPresenter.getQuestionInfo(huatiID)
//        mPresenter.getAnswerList(huatiID,mPage)
    }


    override fun onResume() {
        super.onResume()
        mPage = 0
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

        mAdapter.notifyDataSetChanged()

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
//        if (bean.qaImageEntityList.isNotEmpty()){
//            ImageLoader.load(this,bean.qaImageEntityList[0],R.drawable.default_card,question_pic)
//        }else{
//            question_pic.visibility = View.GONE
//        }

        if (bean.qaImageEntityList.isNotEmpty()){
            imageList.clear()
            for (url in bean.qaImageEntityList.listIterator()){
                imageList.add(url)
                LogCat.e(url)
            }
            question_pic.visibility = View.VISIBLE
            question_pic.setImages(imageList)
            question_pic.start()

        }else{
            question_pic.visibility = View.GONE

        }

        ImageLoader.load(this,bean.usersVO.avater,R.drawable.default_card,question_push)

        question_title.text = bean.title
        if (bean.description.isNotEmpty()&&bean.description!="") {
            question_desc.text = bean.description
        }else{
            question_desc.visibility = View.GONE
        }
        question_time.text = bean.distanceTime
        circle_detail_username.text = bean.usersVO.name
        tv_item_card_read1.text = ""+bean.pv
        tv_item_card_comment1.text = ""+bean.commentCount
        tv_item_card_dianzan.text = ""+bean.likeCount
    }

    override fun getDataSuccess(bean: MutableList<CircleCommentBean>) {
    }

    override fun likeSuccess() {
    }

    override fun unlikeSuccess() {
    }

    override fun postSuccess(bean: CircleCommentBean) {
    }

    override fun postFail(msg: String?) {
    }

    override fun onItemChildClick(adapter: BaseQuickAdapter<*, *>, view: View, position: Int) {

        var bean = adapter.getItem(position) as MultiItemEntity
        val commentId: Int
        if (bean.itemType == CircleCommentBean.TYPE_LEVEL_0) {
            bean = bean as CircleCommentBean
            commentId = bean.id
        } else {
            bean = bean as CircleChildCommentBean
            commentId = bean.id
        }

        when(view.id){
            R.id.cb_item_comment_like ->{
                val checkBox = view as CheckBox
                if (checkBox.isChecked){

//                    if (bean.itemType == CircleCommentBean.TYPE_LEVEL_0){
//                        bean = bean as CircleCommentBean
//                        if (mUserId == bean.authorVO?.id){
//                            toastShow("不能点赞自己哦")
//                            view.isChecked = !view.isChecked
//                            return
//                        }
//                    }else{
//                        bean = bean as CircleChildCommentBean
//                        if (mUserId == bean.authorVO?.id){
//                            toastShow("不能点赞自己哦")
//                            view.isChecked = !view.isChecked
//                            return
//                        }
//                    }

                    val tvLikeNm = adapter.getViewByPosition(position, R.id.tv_item_comment_like_num) as TextView
                    tvLikeNm.text = (tvLikeNm.text.toString().toInt() + 1).toString()
                    mPresenter.likeQAOrQAComment_comment(huatiID,commentId)

                }else{
                    val tvLikeNm = adapter.getViewByPosition(position, R.id.tv_item_comment_like_num) as TextView
                    tvLikeNm.text = (tvLikeNm.text.toString().toInt() - 1).toString()
                    mPresenter.unlikeQAOrQAComment_comment(huatiID,commentId)
                }
            }

            R.id.tv_item_comment_expand->{
                toastShow("展开")
            }
        }
    }

    override fun onItemClick(adapter: BaseQuickAdapter<*, *>, view: View, position: Int) {
        var bean = adapter.getItem(position) as MultiItemEntity
        if (bean.itemType == CircleCommentBean.TYPE_LEVEL_0){
            bean = bean as CircleCommentBean
//            toastShow(getString(R.string.et_comment_hint, bean.authorVO?.name))
            mActivity.startActivity(CircleAnswerChildActivity::class.java){
                it.putExtra("qaId",huatiID)
                it.putExtra("CircleId",circleId)
                it.putExtra("name",bean.authorVO?.name)
                it.putExtra("qaCommentId",bean.id)
            }
        }
    }

    override fun setListener() {

        //以下四个去同一个的地方
        comment_bottom.setOnClickListener {
            mActivity.startActivity(CircleAnswerActivity::class.java){
                it.putExtra("qaId",huatiID)
                it.putExtra("CircleId",circleId)
            }
        }

        ll_comment_input.setOnClickListener {
            mActivity.startActivity(CircleAnswerActivity::class.java){
                it.putExtra("qaId",huatiID)
                it.putExtra("CircleId",circleId)
            }
        }

        tv_comment_send.setOnClickListener {
            mActivity.startActivity(CircleAnswerActivity::class.java){
                it.putExtra("qaId",huatiID)
                it.putExtra("CircleId",circleId)
            }
        }

        et_comment.setOnClickListener {
            mActivity.startActivity(CircleAnswerActivity::class.java){
                it.putExtra("qaId",huatiID)
                it.putExtra("CircleId",circleId)
            }
        }
    }

    override fun onLoadMoreRequested() {
        mPresenter.getAnswerList(huatiID,mPage)
    }

    private fun initView(){
        initToolBar()
//        iv_toolbar_right.visibility = View.VISIBLE
//        iv_toolbar_right.setImageResource(R.drawable.c_more_2)
    }

    private fun initData(){
        huatiID = intent.getIntExtra("huatiId",0)
        circleId = intent.getIntExtra("CircleId",0)
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

        //打开有风险 需谨慎
//        rv_ht_comment.isNestedScrollingEnabled = false
//        rv_ht_comment.setHasFixedSize(true)
//        rv_ht_comment.isFocusable = false

        rv_ht_comment.adapter = mAdapter

    }


    private fun initADView(){
        question_pic.setImageLoader(GlideBannerImageLoader())
        question_pic.setIndicatorGravity(BannerConfig.CENTER)

    }

}