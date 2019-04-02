package com.zxcx.zhizhe.ui.topchange

import android.os.Bundle
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.helper.ItemTouchHelper
import android.view.View
import com.chad.library.adapter.base.callback.ItemDragAndSwipeCallback
import com.chad.library.adapter.base.listener.OnItemDragListener
import com.zxcx.zhizhe.R
import com.zxcx.zhizhe.mvpBase.MvpActivity
import com.zxcx.zhizhe.ui.my.selectAttention.ClassifyBean
import com.zxcx.zhizhe.utils.LogCat
import com.zxcx.zhizhe.utils.SharedPreferencesUtil
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

    //是否是编辑的状态
    private var mCanChoose = false

    //??
    private lateinit var mItemTouchHelper: ItemTouchHelper
    private lateinit var mItemDragAndSwipeCallback: ItemDragAndSwipeCallback

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
//        finish()
        SharedPreferencesUtil.saveData("saveOnce",true)
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

            mCanChoose = true
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

            mCanChoose = false

            tv_select_label_change_1.visibility = View.VISIBLE
            tv_select_label_change_2.visibility = View.GONE
        }
    }

    override fun getDataSuccess(bean: MutableList<ClassifyBean>?) {
    }

    private fun initRecycleView() {
        mAdapter1 = TopChange1Adapter(ArrayList())
        val manage1 = GridLayoutManager(mActivity, 4)
        rv_select_classify.adapter = mAdapter1
        rv_select_classify.layoutManager = manage1


        mItemDragAndSwipeCallback = ItemDragAndSwipeCallback(mAdapter1)
        mItemTouchHelper = ItemTouchHelper(mItemDragAndSwipeCallback)
        mItemTouchHelper.attachToRecyclerView(rv_select_classify)


        mAdapter1.enableDragItem(mItemTouchHelper)

        val listener = object  :OnItemDragListener{
            override fun onItemDragMoving(source: RecyclerView.ViewHolder?, from: Int, target: RecyclerView.ViewHolder?, to: Int) {
            }

            override fun onItemDragStart(viewHolder: RecyclerView.ViewHolder?, pos: Int) {
            }

            override fun onItemDragEnd(viewHolder: RecyclerView.ViewHolder?, pos: Int) {

            }
        }

        mAdapter1.setOnItemDragListener(listener)
        ////////////////////////////////////////

        mAdapter2 = TopChange2Adapter(ArrayList())
        val manage2 = GridLayoutManager(mActivity, 4)
        rv_select_classify_2.adapter = mAdapter2
        rv_select_classify_2.layoutManager = manage2

        mAdapter1.setOnItemChildClickListener { adapter, view, position ->
            val bean = adapter.data[position] as ClassifyBean
//            bean.isInNav = !bean.isInNav
//            mAdapter1.notifyItemChanged(position)
            if (mCanChoose) {

                mAdapter2.addData(bean)
                mAdapter1.remove(position)

                mAdapter1.notifyDataSetChanged()
                mAdapter2.notifyDataSetChanged()
            }
        }

        mAdapter2.setOnItemChildClickListener { adapter, view, position ->
            val bean = adapter.data[position] as ClassifyBean

            if (mCanChoose) {
                mAdapter1.addData(bean)
                mAdapter2.remove(position)

                mAdapter1.notifyDataSetChanged()
                mAdapter2.notifyDataSetChanged()
            }
        }


    }

    private fun initView() {
        initToolBar("所有分类")

    }
}