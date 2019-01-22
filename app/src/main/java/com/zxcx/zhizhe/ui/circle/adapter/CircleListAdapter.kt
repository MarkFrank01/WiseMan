package com.zxcx.zhizhe.ui.circle.adapter

import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.zxcx.zhizhe.R
import com.zxcx.zhizhe.ui.my.selectAttention.ClassifyBean
import com.zxcx.zhizhe.utils.LogCat

/**
 * @author : MarkFrank01
 * @Created on 2019/1/21
 * @Description :
 */
//class CircleListAdapter(data:CircleBean):
//        BaseMultiItemQuickAdapter<CircleBean, BaseViewHolder>(data),BaseQuickAdapter.OnItemClickListener{
//class CircleListAdapter(data:List<MultiItemEntity>):BaseMultiItemQuickAdapter<MultiItemEntity,BaseViewHolder>(data), BaseQuickAdapter.OnItemClickListener {
@Deprecated("弃用")
class CircleListAdapter(data: List<ClassifyBean>) : BaseQuickAdapter<ClassifyBean, BaseViewHolder>(R.layout.item_circle_list_iten, data) {

    override fun convert(helper: BaseViewHolder, item: ClassifyBean) {
        var classifyList = data
        val rv = helper.getView<RecyclerView>(R.id.rv_item_circle_list)

        if (helper.adapterPosition < 2) {
            rv.visibility = View.VISIBLE
            if (helper.adapterPosition == 0) {
                classifyList = classifyList.subList(0, 10)
            } else if (helper.adapterPosition == 1) {
                if (classifyList.size > 20) {
                    classifyList = classifyList.subList(11, 20)
                } else {
                    classifyList = classifyList.subList(11, classifyList.size)
                }
            }

            val adapter = CircleListItemAdapter(classifyList)
            rv.adapter = adapter
            rv.layoutManager = GridLayoutManager(mContext, 5)
            LogCat.e("TIME" + helper.adapterPosition)
        } else {
            rv.visibility = View.GONE
        }
    }

//    init {
//        addItemType(ClassifyBean.TYPE_CLASSIFY, R.layout.item_circle_list_iten)
//    }

//    override fun convert(helper: BaseViewHolder, item: MultiItemEntity) {
//        when(helper.itemViewType){
//            ClassifyBean.TYPE_CLASSIFY ->{
//
//                    val rv = helper.getView<RecyclerView>(R.id.rv_item_circle_list)
//
//                    val adapter = CircleListItemAdapter(data)
//                    adapter.onItemClickListener = this
//                    rv.adapter = adapter
//                    rv.layoutManager = GridLayoutManager(mContext, 5)
//            }
//        }
//    }
//
//    override fun onItemClick(adapter: BaseQuickAdapter<*, *>, view: View, position: Int) {
//        LogCat.e("进入第一层")
//    }

}