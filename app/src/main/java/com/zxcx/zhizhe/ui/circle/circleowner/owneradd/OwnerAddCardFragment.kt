package com.zxcx.zhizhe.ui.circle.circleowner.owneradd

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.util.SparseBooleanArray
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import com.chad.library.adapter.base.BaseQuickAdapter
import com.scwang.smartrefresh.layout.api.RefreshLayout
import com.zxcx.zhizhe.R
import com.zxcx.zhizhe.event.GetBackNumAndDataEvent
import com.zxcx.zhizhe.mvpBase.RefreshMvpFragment
import com.zxcx.zhizhe.ui.card.hot.CardBean
import com.zxcx.zhizhe.ui.circle.adapter.ManageCreateCircleAdapter
import com.zxcx.zhizhe.utils.Constants
import com.zxcx.zhizhe.utils.SharedPreferencesUtil
import com.zxcx.zhizhe.widget.CustomLoadMoreView
import com.zxcx.zhizhe.widget.EmptyView
import kotlinx.android.synthetic.main.fragment_owner_card.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

/**
 * @author : MarkFrank01
 * @Created on 2019/3/12
 * @Description :
 */
class OwnerAddCardFragment :RefreshMvpFragment<OwnerAddPresenter>(),OwnerAddContract.View,
        BaseQuickAdapter.RequestLoadMoreListener, BaseQuickAdapter.OnItemClickListener, BaseQuickAdapter.OnItemChildClickListener{

    private var mPage = 0
    private var mcircleId = 0

    //存放卡片的数量值
    private var mNumberCard:Int =0

    //传递值
    private var mBackList: MutableList<Int> = ArrayList()

    //把选中的卡片的bean回传到下个Ac中
    private var listBackCard: MutableList<CardBean> = ArrayList()

    private lateinit var mAdapter : ManageCreateCircleAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_owner_card,container,false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        EventBus.getDefault().register(this)
        mRefreshLayout = refresh_layout2
        initData()
        initRecycleView()
        onRefresh()
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onMessageEvent(event: GetBackNumAndDataEvent){

    }

    override fun onDestroyView() {
        EventBus.getDefault().unregister(this)
        super.onDestroyView()
    }

    override fun onRefresh(refreshLayout: RefreshLayout?) {
        mPage = 0
        onRefresh()
    }

    override fun createPresenter(): OwnerAddPresenter {
        return OwnerAddPresenter(this)
    }

    override fun getDataSuccess(list: MutableList<CardBean>) {
        val emptyView = EmptyView.getEmptyView(mActivity,"暂无相关作品, 赶紧创作吧",R.drawable.need_add)
        mAdapter.emptyView = emptyView


        mRefreshLayout.finishRefresh()
        if (mPage == 0){
            mAdapter.setNewData(list)
        }else{
            mAdapter.addData(list)
        }

        mPage++
        if (list.size< Constants.PAGE_SIZE){
            mAdapter.loadMoreEnd(false)
        }else{
            mAdapter.loadMoreComplete()
            mAdapter.setEnableLoadMore(false)
            mAdapter.setEnableLoadMore(true)
        }
    }

    override fun onLoadMoreRequested() {
        onRefresh()
    }

    override fun onItemClick(adapter: BaseQuickAdapter<*, *>?, view: View?, position: Int) {
    }

    override fun onItemChildClick(adapter: BaseQuickAdapter<*, *>, view: View, position: Int) {
        val cb = view.findViewById<CheckBox>(R.id.cb_choose_push_manage)
        val bean = adapter.data[position] as CardBean

        if (cb.isChecked){
            mNumberCard++
            mBackList.add(bean.id)
            listBackCard.add(bean)
        }else{
            mNumberCard--
            mBackList.remove(bean.id)
            listBackCard.remove(bean)
        }

        val mCheckStates = SparseBooleanArray()
        cb.setOnCheckedChangeListener { buttonView, isChecked ->
            var pos:Int = buttonView.tag as Int
            if (isChecked){
                mCheckStates.put(pos,true)
            }else{
                mCheckStates.delete(pos)
            }
        }
        cb.isChecked = mCheckStates.get(position,false)


        EventBus.getDefault().post(GetBackNumAndDataEvent(0,mBackList,listBackCard))
    }

    private fun initRecycleView(){
        mAdapter = ManageCreateCircleAdapter(ArrayList())
        mAdapter.onItemClickListener = this
        mAdapter.onItemChildClickListener = this

        rv_create_card.layoutManager = LinearLayoutManager(mActivity, LinearLayoutManager.VERTICAL,false)
        rv_create_card.adapter = mAdapter

        mAdapter.setLoadMoreView(CustomLoadMoreView())
        mAdapter.setOnLoadMoreListener(this,rv_create_card)
    }

    fun onRefresh(){
        getCanChooseCard()
    }

    //获取可选择的卡片
    private fun getCanChooseCard(){
        mPresenter.getLockableArticleForAdd(1,mcircleId,mPage,10)
    }

    private fun initData(){
        mcircleId = SharedPreferencesUtil.getInt("mcircleId",-1)
    }

}