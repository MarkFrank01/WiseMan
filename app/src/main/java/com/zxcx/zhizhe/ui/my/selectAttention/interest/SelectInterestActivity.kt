package com.zxcx.zhizhe.ui.my.selectAttention.interest

import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.StaggeredGridLayoutManager
import android.view.View
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.entity.MultiItemEntity
import com.zxcx.zhizhe.R
import com.zxcx.zhizhe.event.HomeClickRefreshEvent
import com.zxcx.zhizhe.event.SelectAttentionEvent
import com.zxcx.zhizhe.mvpBase.MvpActivity
import com.zxcx.zhizhe.ui.my.selectAttention.ClassifyCardBean
import com.zxcx.zhizhe.utils.LogCat
import kotlinx.android.synthetic.main.activity_interest.*
import kotlinx.android.synthetic.main.toolbar.*
import org.greenrobot.eventbus.EventBus

/**
 * @author : MarkFrank01
 * @Created on 2019/1/16
 * @Description :
 */
class SelectInterestActivity : MvpActivity<SelectInterestPresenter>(), SelectInterestContract.View,
        BaseQuickAdapter.OnItemChildClickListener {

    //推荐的标签
    private var mCollectionList: MutableList<ClassifyCardBean> = ArrayList()

    //推荐的用户
    private var mUserList: MutableList<AttentionManBean> = ArrayList()

    private lateinit var mHotLabelAdapter: SelectHotLabelAdapter
    private lateinit var mHotManAdapter: SelectHotManAdapter

    override fun createPresenter(): SelectInterestPresenter {
        return SelectInterestPresenter(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_interest)
        initRecyclerView()
        initView()
        mPresenter.getInterestRecommend()
    }


    override fun postSuccess() {
        EventBus.getDefault().post(SelectAttentionEvent())
        toastShow(R.string.user_info_change)
        EventBus.getDefault().post(HomeClickRefreshEvent())
        finish()
    }

    override fun postFail(msg: String?) {
    }

    override fun getDataSuccess(bean: InterestRecommendBean) {
        mCollectionList = bean.collectionList
        mUserList = bean.usersList

        for (bean in mCollectionList){
            mHotLabelAdapter.data.add(bean)
        }
        mHotLabelAdapter.notifyDataSetChanged()
        tv_toolbar_right.isEnabled = mCollectionList.isEmpty()
        LogCat.e("Coll size " + mCollectionList.size + "  mU size " + mUserList.size)

//        mHotLabelAdapter.setNewData(mCollectionList as List<MultiItemEntity>?)
        mHotManAdapter.setNewData(mUserList as List<MultiItemEntity>?)

    }

    override fun setListener() {
        tv_toolbar_right.setOnClickListener {
            val idList = mutableListOf<Int>()
            mCollectionList.forEach {
                idList.add(it.id)
            }
            mPresenter.changeAttentionList(idList)
        }
    }

    override fun onItemChildClick(adapter: BaseQuickAdapter<*, *>, view: View, position: Int) {
        val bean = adapter.data[position] as ClassifyCardBean

        when(view.id){
            R.id.fl_item_select_card_bag,R.id.cb_item_select_hot_label ->{
                bean.isChecked = !bean.isChecked
                mHotManAdapter.notifyItemChanged(position)

                if (bean.isChecked){
                    if (!mCollectionList.contains(bean)){
                        mCollectionList.add(bean)
                    }
                }else{
                    if (mCollectionList.contains(bean)){
                        mCollectionList.remove(bean)
                    }
                }
                tv_toolbar_right.isEnabled = mCollectionList.isNotEmpty()
            }
        }

    }

    private fun initView() {
        initToolBar()
        tv_toolbar_right.visibility = View.VISIBLE
        tv_toolbar_right.text = "完成"
        tv_toolbar_right.isEnabled = false
        tv_toolbar_right.setTextColor(ContextCompat.getColorStateList(mActivity, R.color.color_text_enable_blue))
    }

    private fun initRecyclerView() {
        mHotLabelAdapter = SelectHotLabelAdapter(ArrayList())
        mHotManAdapter = SelectHotManAdapter(ArrayList())

        mHotLabelAdapter.onItemChildClickListener = this
        mHotManAdapter.onItemChildClickListener = this

//        val hotLabelManager = StaggeredGridLayoutManager(3,StaggeredGridLayoutManager.VERTICAL)
        val hotManManager = GridLayoutManager(mActivity, 4)

        val hotLabelManager = StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL)
//        val hotManManager = StaggeredGridLayoutManager(4, StaggeredGridLayoutManager.VERTICAL)


        rv_hot_label.adapter = mHotLabelAdapter
        rv_hot_label.layoutManager = hotLabelManager


        rv_hot_man.adapter = mHotManAdapter
        rv_hot_man.layoutManager = hotManManager
    }
}