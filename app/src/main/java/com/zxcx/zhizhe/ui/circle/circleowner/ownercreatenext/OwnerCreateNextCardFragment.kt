package com.zxcx.zhizhe.ui.circle.circleowner.ownercreatenext

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.chad.library.adapter.base.BaseQuickAdapter
import com.scwang.smartrefresh.layout.api.RefreshLayout
import com.zxcx.zhizhe.R
import com.zxcx.zhizhe.event.GetNextCardEvent
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
class OwnerCreateNextCardFragment :RefreshMvpFragment<OwnerCreateNextPresenter>(),OwnerCreateNextContract.View,
        BaseQuickAdapter.RequestLoadMoreListener, BaseQuickAdapter.OnItemClickListener, BaseQuickAdapter.OnItemChildClickListener{

    private lateinit var mAdapter:ManageCreateCircleNextAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_owner_next_card,container,false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        EventBus.getDefault().register(this)
        mRefreshLayout = refresh_layout_next
//        initData()
        initRecycleView()

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onMessageEvent(event: GetNextCardEvent){
        LogCat.e("GetNextCard"+event.cardBeanList.size)
        mAdapter.setNewData(event.cardBeanList)
    }


    override fun onDestroyView() {
        EventBus.getDefault().unregister(this)
        super.onDestroyView()
    }

    override fun onRefresh(refreshLayout: RefreshLayout?) {
    }

    override fun createPresenter(): OwnerCreateNextPresenter {
        return OwnerCreateNextPresenter(this)
    }

    override fun getDataSuccess(bean: MutableList<CardBean>?) {
    }

    override fun onLoadMoreRequested() {
    }

    override fun onItemClick(adapter: BaseQuickAdapter<*, *>?, view: View?, position: Int) {
    }

    override fun onItemChildClick(adapter: BaseQuickAdapter<*, *>?, view: View?, position: Int) {
    }

    private fun initRecycleView(){
        mAdapter = ManageCreateCircleNextAdapter(ArrayList())
        mAdapter.onItemChildClickListener = this

        rv_create_next.layoutManager = LinearLayoutManager(mActivity,LinearLayoutManager.VERTICAL,false)
        rv_create_next.adapter = mAdapter
    }
}