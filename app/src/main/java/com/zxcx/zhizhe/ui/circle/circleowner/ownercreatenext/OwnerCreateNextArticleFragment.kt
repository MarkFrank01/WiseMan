package com.zxcx.zhizhe.ui.circle.circleowner.ownercreatenext

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import com.chad.library.adapter.base.BaseQuickAdapter
import com.scwang.smartrefresh.layout.api.RefreshLayout
import com.zxcx.zhizhe.R
import com.zxcx.zhizhe.event.GetBackNumAndDataEvent2
import com.zxcx.zhizhe.event.GetNextArcEvent
import com.zxcx.zhizhe.mvpBase.BaseFragment
import com.zxcx.zhizhe.mvpBase.RefreshMvpFragment
import com.zxcx.zhizhe.ui.card.hot.CardBean
import com.zxcx.zhizhe.ui.circle.adapter.ManageCreateCircleNextAdapter
import com.zxcx.zhizhe.utils.LogCat
import kotlinx.android.synthetic.main.fragment_owner_next_card.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

/**
 * @author : MarkFrank01
 * @Created on 2019/3/11
 * @Description :
 */
//class OwnerCreateNextArticleFragment :RefreshMvpFragment<OwnerCreateNextPresenter>(),OwnerCreateNextContract.View,
class OwnerCreateNextArticleFragment :BaseFragment(),
        BaseQuickAdapter.RequestLoadMoreListener, BaseQuickAdapter.OnItemClickListener, BaseQuickAdapter.OnItemChildClickListener{

    //存放卡片的数量值
    private var mNumberCard:Int =0

    //传递值
    private var mBackList: MutableList<Int> = ArrayList()

    private lateinit var mAdapter:ManageCreateCircleNextAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_owner_next_card,container,false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
//        EventBus.getDefault().register(this)
//        initData()
        initRecycleView()
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onMessageEvent(event: GetNextArcEvent){
        LogCat.e("GetNextCard"+event.contentList.size)
        mAdapter.setNewData(event.contentList)
    }

    override fun onResume() {
        super.onResume()
        EventBus.getDefault().register(this)

    }

    override fun onDestroyView() {
        EventBus.getDefault().unregister(this)
        super.onDestroyView()
    }

    override fun onLoadMoreRequested() {
    }

    override fun onItemClick(adapter: BaseQuickAdapter<*, *>?, view: View?, position: Int) {
    }

    override fun onItemChildClick(adapter: BaseQuickAdapter<*, *>, view: View, position: Int) {
        val cb = view.findViewById<CheckBox>(R.id.cb_choose_push_manage)
        val bean = adapter.data[position] as CardBean

        if (cb.isChecked){
            mNumberCard++
            mBackList.add(bean.id)
        }else{
            mNumberCard--
            mBackList.remove(bean.id)
        }

        EventBus.getDefault().post(GetBackNumAndDataEvent2(1,mBackList))

    }

    private fun initRecycleView(){
        mAdapter = ManageCreateCircleNextAdapter(ArrayList())
        mAdapter.onItemChildClickListener = this

        rv_create_next.layoutManager = LinearLayoutManager(mActivity,LinearLayoutManager.VERTICAL,false)
        rv_create_next.adapter = mAdapter
    }
}