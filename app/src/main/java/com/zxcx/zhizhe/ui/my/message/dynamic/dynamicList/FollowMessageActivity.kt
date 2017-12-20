package com.zxcx.zhizhe.ui.my.message.dynamic.dynamicList

import android.content.Intent
import android.os.Bundle
import android.os.PersistableBundle
import android.support.v4.util.ArrayMap
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import com.chad.library.adapter.base.BaseQuickAdapter
import com.zxcx.zhizhe.R
import com.zxcx.zhizhe.mvpBase.MvpActivity
import com.zxcx.zhizhe.ui.card.card.cardDetails.CardDetailsActivity
import com.zxcx.zhizhe.ui.my.creation.ApplyForCreationActivity
import com.zxcx.zhizhe.ui.my.creation.newCreation.NewCreationTitleActivity
import com.zxcx.zhizhe.ui.my.creation.rejectDetails.RejectDetailsActivity
import com.zxcx.zhizhe.ui.my.message.system.*
import com.zxcx.zhizhe.ui.rank.RankActivity
import com.zxcx.zhizhe.utils.Constants
import com.zxcx.zhizhe.widget.CustomLoadMoreView
import com.zxcx.zhizhe.widget.EmptyView
import io.reactivex.Flowable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.functions.Function
import io.reactivex.schedulers.Schedulers
import io.reactivex.subscribers.DisposableSubscriber
import kotlinx.android.synthetic.main.fragment_system_message.*
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class FollowMessageActivity : MvpActivity<DynamicMessageListPresenter>(), DynamicMessageListContract.View,
        BaseQuickAdapter.RequestLoadMoreListener,BaseQuickAdapter.OnItemChildClickListener {

    private var mMessageType = 0
    private var mPage = 0
    private val mPageSize = Constants.PAGE_SIZE
    private lateinit var mAdapter: SystemMessageAdapter
    private val map: ArrayMap<String, ArrayList<DynamicMessageListBean>> = ArrayMap()

    override fun onCreate(savedInstanceState: Bundle?, persistentState: PersistableBundle?) {
        super.onCreate(savedInstanceState, persistentState)
        setContentView(R.layout.activity_follow_message)
        initRecyclerView()
        mPresenter.getDynamicMessageList(mMessageType,mPage,mPageSize)
    }

    override fun createPresenter(): DynamicMessageListPresenter {
        return DynamicMessageListPresenter(this)
    }

    override fun getDataSuccess(list: List<DynamicMessageListBean>) {
        mDisposable = Flowable.just(list)
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.computation())
                .map(PackData(map))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object: DisposableSubscriber<List<DynamicBean>>() {
                    override fun onError(t: Throwable?) {
                    }

                    override fun onComplete() {
                    }

                    override fun onNext(t: List<DynamicBean>?) {
//                        mAdapter.setNewData(t)
                    }

                })
        /*if (mPage == 0) {
            mAdapter.setNewData(list)
        } else {
            mAdapter.addData(list)
        }
        mPage++
        if (list.size < Constants.PAGE_SIZE) {
            mAdapter.loadMoreEnd(false)
        } else {
            mAdapter.loadMoreComplete()
            mAdapter.setEnableLoadMore(false)
            mAdapter.setEnableLoadMore(true)
        }*/
    }

    override fun onLoadMoreRequested() {
        mPresenter.getDynamicMessageList(mMessageType,mPage,mPageSize)
    }

    override fun onItemChildClick(adapter: BaseQuickAdapter<*, *>?, view: View?, position: Int) {
        val bean = mAdapter.data[position]
        val intent = Intent()
        when(bean.messageType){
            message_card_pass -> {
                intent.setClass(mActivity, CardDetailsActivity::class.java)
                intent.putExtra("id",bean.relatedCardId)
            }
            message_card_reject -> {
                intent.setClass(mActivity, RejectDetailsActivity::class.java)
                intent.putExtra("id",bean.relatedCardId)
            }
            message_apply_pass -> {
                intent.setClass(mActivity, NewCreationTitleActivity::class.java)
            }
            message_apply_reject -> {
                intent.setClass(mActivity, ApplyForCreationActivity::class.java)
            }
            message_rank -> {
                intent.setClass(mActivity, RankActivity::class.java)
            }
            message_recommend -> {
                intent.setClass(mActivity, CardDetailsActivity::class.java)
                intent.putExtra("id",bean.relatedCardId)
            }
        }
        startActivity(intent)
    }

    private fun initRecyclerView() {
        mAdapter = SystemMessageAdapter(ArrayList())
        mAdapter.onItemChildClickListener = this
        mAdapter.setLoadMoreView(CustomLoadMoreView())
        mAdapter.setOnLoadMoreListener(this,rv_system_message)
        rv_system_message.layoutManager = LinearLayoutManager(mActivity, LinearLayoutManager.VERTICAL,false)
        rv_system_message.adapter = mAdapter
        val emptyView = EmptyView.getEmptyView(mActivity,"暂时没有更多消息","前往我的页面-系统设置开启推送",null,null)
        mAdapter.emptyView = emptyView
    }

    class PackData(private val map: ArrayMap<String, ArrayList<DynamicMessageListBean>>): Function<List<DynamicMessageListBean>, List<DynamicBean>> {
        private val dateFormat = SimpleDateFormat("yyyy/MM/dd", Locale.CHINA)
        private val timeFormat = SimpleDateFormat("hh:mm", Locale.CHINA)
        override fun apply(list: List<DynamicMessageListBean>): List<DynamicBean> {
            for (bean in list) {
                val date = dateFormat.format(bean.time)
                val time = timeFormat.format(bean.time)
                bean.time = time
                if (map.containsKey(date)){
                    map[date]?.add(bean)
                }else{
                    val newList: ArrayList<DynamicMessageListBean> = ArrayList()
                    newList.add(bean)
                    map.put(date, newList)
                }
            }
            val dynamicBeanList = ArrayList<DynamicBean>()
            for (mutableEntry in map) {
                val dynamicBean = DynamicBean(mutableEntry.key,mutableEntry.value)
                dynamicBeanList.add(dynamicBean)
            }
            return dynamicBeanList
        }
    }
}
