package com.zxcx.zhizhe.ui.my.selectAttention.now

import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v7.widget.GridLayoutManager
import android.view.View
import com.zxcx.zhizhe.R
import com.zxcx.zhizhe.mvpBase.MvpActivity
import com.zxcx.zhizhe.ui.my.selectAttention.ClassifyBean
import com.zxcx.zhizhe.ui.my.selectAttention.man.NowSelectManActivity
import com.zxcx.zhizhe.utils.LogCat
import com.zxcx.zhizhe.utils.startActivity
import kotlinx.android.synthetic.main.activity_now_select.*
import kotlinx.android.synthetic.main.toolbar.*

/**
 * @author : MarkFrank01
 * @Created on 2019/3/28
 * @Description :
 */
class NowSelectActivity : MvpActivity<NowSelectPresenter>(), NowSelectContract.View{



    private lateinit var mAdapter1: NowSelectAdapter
    private lateinit var mAdapter2: NowSelectOtherAdapter

    //数据处理后热门分类
    private var mNewHotClassify: MutableList<ClassifyBean> = ArrayList()
    //数据处理后其它分类
    private var mNewOtherClassify: MutableList<ClassifyBean> = ArrayList()

    //收集数据
    private var mCheckedList = ArrayList<ClassifyBean>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_now_select)
        initRecycleView()
//        initData()
        initView()
        mPresenter.getInterestRecommendForClassify()
    }

    override fun createPresenter(): NowSelectPresenter {
        return NowSelectPresenter(this)
    }

    override fun postSuccess() {
    }

    override fun postFail(msg: String?) {
    }

    override fun followClassifySuccess() {
        toastShow("关注成功")
        mActivity.startActivity(NowSelectManActivity::class.java){}
    }

    override fun getDataSuccess(list: MutableList<ClassifyBean>) {
        list.forEach {
            if (it.isHit == 1) {
                mNewHotClassify.add(it)

            } else if (it.isHit == 0) {
                mNewOtherClassify.add(it)
            }

            if (it.isFollow){
                if (!mCheckedList.contains(it)){
                    mCheckedList.add(it)
                }
            }
        }


        mAdapter1.setNewData(mNewHotClassify)
        mAdapter2.setNewData(mNewOtherClassify)
    }

    override fun setListener() {
        tv_toolbar_right.setOnClickListener {
            val idList = mutableListOf<Int>()
            mCheckedList.forEach {
                idList.add(it.id)
            }
           LogCat.e("size is "+idList.size)
            mPresenter.followClassify(idList)
        }
    }

    private fun initRecycleView() {
        mAdapter1 = NowSelectAdapter(ArrayList())
        mAdapter2 = NowSelectOtherAdapter(ArrayList())

        val manager1 = GridLayoutManager(mActivity, 4)
        val manager2 = GridLayoutManager(mActivity, 4)

        rv_select_classify.adapter = mAdapter1
        rv_select_classify_2.adapter = mAdapter2

        rv_select_classify.layoutManager = manager1
        rv_select_classify_2.layoutManager = manager2

        mAdapter1.setOnItemChildClickListener { adapter, view, position ->
            val bean = adapter.data[position] as ClassifyBean
            bean.isFollow = !bean.isFollow
            mAdapter1.notifyItemChanged(position)

            if (bean.isFollow){
                if (!mCheckedList.contains(bean)){
                    mCheckedList.add(bean)
                }
            }else{
                if (mCheckedList.contains(bean)){
                    mCheckedList.remove(bean)
                }
            }
            tv_toolbar_right.isEnabled = mCheckedList.isNotEmpty()
        }

        mAdapter2.setOnItemChildClickListener { adapter, view, position ->
            val bean = adapter.data[position] as ClassifyBean
            bean.isFollow = !bean.isFollow
            mAdapter2.notifyItemChanged(position)

            if (bean.isFollow){
                if (!mCheckedList.contains(bean)){
                    mCheckedList.add(bean)
                }
            }else{
                if (mCheckedList.contains(bean)){
                    mCheckedList.remove(bean)
                }
            }
            tv_toolbar_right.isEnabled = mCheckedList.isNotEmpty()
        }
    }

    private fun initView() {
        initToolBar()
        tv_toolbar_right.visibility = View.VISIBLE
        tv_toolbar_right.text = "下一步"
        tv_toolbar_right.isEnabled = false
        tv_toolbar_right.setTextColor(ContextCompat.getColorStateList(mActivity, R.color.color_text_enable_blue))
    }
}

