package com.zxcx.zhizhe.ui.newrank

import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import com.chad.library.adapter.base.BaseQuickAdapter
import com.zxcx.zhizhe.R
import com.zxcx.zhizhe.mvpBase.MvpActivity
import com.zxcx.zhizhe.ui.newrank.morerank.MoreRankActivity
import com.zxcx.zhizhe.ui.rank.UserRankBean
import com.zxcx.zhizhe.ui.welcome.WebViewActivity
import com.zxcx.zhizhe.utils.ImageLoader
import com.zxcx.zhizhe.utils.SVTSConstants
import com.zxcx.zhizhe.utils.SharedPreferencesUtil
import com.zxcx.zhizhe.utils.startActivity
import kotlinx.android.synthetic.main.activity_newrank.*
import kotlinx.android.synthetic.main.toolbar.*

/**
 * @author : MarkFrank01
 * @Created on 2019/3/22
 * @Description :
 */
class NewRankActivity : MvpActivity<NewRankPresenter>(), NewRankContract.View,
        BaseQuickAdapter.OnItemChildClickListener,BaseQuickAdapter.OnItemClickListener {

    private var mUserId:Int = 0
    private lateinit var mAdapter: NewRankAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_newrank)

        initToolBar("我的榜单")
        iv_toolbar_right.visibility = View.VISIBLE
        iv_toolbar_right.setImageResource(R.drawable.my_que)

        initRecyclerView()
        onRefresh()
    }

    override fun createPresenter(): NewRankPresenter {
        return NewRankPresenter(this)
    }

    override fun getMyRankSuccess(bean: UserRankBean) {
//        iv_up_or_down
        tv_up_or_down.text = bean.rankChange.toString()
//        ImageLoader.load(mActivity,bean.imageUrl,R.drawable.default_header,iv_up_or_down)
        ImageLoader.load(mActivity,bean.imageUrl,R.drawable.default_header,iv_my_head)
        iv_zhili.text = bean.intelligence.toString()
        chaoguo.text = bean.percentageOfUsersExceeded.toString()+"%"
        shangban.text = bean.onRankCount.toString()
        tv_my_lv.text = bean.rankIndex.toString()

    }

    override fun getTopTenRankSuccess(bean: List<UserRankBean>) {

        tv_rank_first.text = bean[0].name
        tv_rank_second.text = bean[1].name
        tv_rank_third.text = bean[2].name

        tv_rank_first_2.text = bean[0].intelligence.toString()
        tv_rank_second_2.text = bean[1].intelligence.toString()
        tv_rank_third_1.text = bean[2].intelligence.toString()

        ImageLoader.load(mActivity,bean[0].imageUrl,R.drawable.default_header,iv_rank_first)
        ImageLoader.load(mActivity,bean[1].imageUrl,R.drawable.default_header,iv_rank_second)
        ImageLoader.load(mActivity,bean[2].imageUrl,R.drawable.default_header,iv_rank_third)



        mAdapter.setNewData(bean)
        mAdapter.remove(0)
        mAdapter.remove(0)
        mAdapter.remove(0)


    }

    override fun getDataSuccess(bean: List<UserRankBean>?) {
    }

    override fun onItemChildClick(adapter: BaseQuickAdapter<*, *>?, view: View?, position: Int) {
    }

    override fun onItemClick(adapter: BaseQuickAdapter<*, *>?, view: View?, position: Int) {
    }

    override fun setListener() {
        load_more_load_end_view.setOnClickListener {
            mActivity.startActivity(MoreRankActivity::class.java){}
        }

        last_week_rank.setOnClickListener {
            mActivity.startActivity(MoreRankActivity::class.java){}

        }

        iv_toolbar_right.setOnClickListener {
            val intent = Intent(this,WebViewActivity::class.java)
            intent.putExtra("title","智者榜单说明")
            intent.putExtra("url","http://192.168.1.153:8043/pages/list-explain.html")
            startActivity(intent)
        }
    }

    private fun initRecyclerView() {
        mAdapter = NewRankAdapter(arrayListOf())
        mAdapter.onItemChildClickListener = this
        rv_ht_rank.layoutManager = object : LinearLayoutManager(this) {
            override fun canScrollVertically() = false
        }

        rv_ht_rank.adapter = mAdapter
    }

    private fun onRefresh(){
        mUserId = SharedPreferencesUtil.getInt(SVTSConstants.userId, 0)
        if (mUserId != 0){
            mPresenter.getMyRank()
        }
        mPresenter.getTopTenRank()
    }
}