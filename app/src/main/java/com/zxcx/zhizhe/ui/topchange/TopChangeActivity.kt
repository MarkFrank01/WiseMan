package com.zxcx.zhizhe.ui.topchange

import android.os.Bundle
import android.support.v7.widget.GridLayoutManager
import android.view.View
import com.zxcx.zhizhe.R
import com.zxcx.zhizhe.mvpBase.MvpActivity
import com.zxcx.zhizhe.ui.my.selectAttention.ClassifyBean
import com.zxcx.zhizhe.utils.LogCat
import kotlinx.android.synthetic.main.activity_top_change.*

/**
 * @author : MarkFrank01
 * @Created on 2019/3/29
 * @Description :
 */
class TopChangeActivity : MvpActivity<TopChangePresenter>(), TopChangeContract.View {

    private lateinit var mAdapter1: TopChange1Adapter
    private lateinit var mAdapter2: TopChange2Adapter


    //数据处理后热门分类
    private var mNewHotClassify: MutableList<ClassifyBean> = ArrayList()
    //数据处理后其它分类
    private var mNewOtherClassify: MutableList<ClassifyBean> = ArrayList()

    //收集数据
    private var mCheckedList = ArrayList<ClassifyBean>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_top_change)
        initRecycleView()
        initView()
        mPresenter.getAllNavClassify()
    }

    override fun createPresenter(): TopChangePresenter {
        return TopChangePresenter(this)
    }

    override fun setClassifyMenuSuccess() {
        toastShow("设置成功")
        finish()
    }

    override fun getAllNavClassifySuccess(list: MutableList<ClassifyBean>) {
        list.forEach {
            if (it.isInNav) {
                mNewHotClassify.add(it)
            } else {
                mNewOtherClassify.add(it)
            }
        }

        mAdapter1.setNewData(mNewHotClassify)
        mAdapter2.setNewData(mNewOtherClassify)

    }

    override fun postSuccess() {
    }

    override fun postFail(msg: String?) {
    }

    override fun setListener() {
        tv_select_label_change_1.setOnClickListener {

            tv_select_label_change_1.visibility = View.GONE
            tv_select_label_change_2.visibility = View.VISIBLE
        }

        tv_select_label_change_2.setOnClickListener {
            //            toastShow("选择完成")
            mCheckedList = mNewHotClassify as ArrayList<ClassifyBean>

            val idList = mutableListOf<Int>()
            mCheckedList.forEach {
                idList.add(it.id)
            }
            LogCat.e("size is " + idList.size)
            mPresenter.setClassifyMenu(idList)
        }
    }

    override fun getDataSuccess(bean: MutableList<ClassifyBean>?) {
    }

    private fun initRecycleView() {
        mAdapter1 = TopChange1Adapter(ArrayList())
        mAdapter2 = TopChange2Adapter(ArrayList())

        val manage1 = GridLayoutManager(mActivity, 4)
        val manage2 = GridLayoutManager(mActivity, 4)

        rv_select_classify.adapter = mAdapter1
        rv_select_classify_2.adapter = mAdapter2

        rv_select_classify.layoutManager = manage1
        rv_select_classify_2.layoutManager = manage2

        mAdapter1.setOnItemChildClickListener { adapter, view, position ->
            val bean = adapter.data[position] as ClassifyBean
//            bean.isInNav = !bean.isInNav
//            mAdapter1.notifyItemChanged(position)

            mAdapter2.addData(bean)
            mAdapter1.remove(position)

            mAdapter1.notifyDataSetChanged()
            mAdapter2.notifyDataSetChanged()
        }

        mAdapter2.setOnItemChildClickListener { adapter, view, position ->
            val bean = adapter.data[position] as ClassifyBean

            mAdapter1.addData(bean)
            mAdapter2.remove(position)

            mAdapter1.notifyDataSetChanged()
            mAdapter2.notifyDataSetChanged()
        }

    }

    private fun initView() {
        initToolBar("所有分类")

    }
}