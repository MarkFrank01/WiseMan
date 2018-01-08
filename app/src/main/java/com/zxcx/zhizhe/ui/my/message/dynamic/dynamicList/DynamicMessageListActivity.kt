package com.zxcx.zhizhe.ui.my.message.dynamic.dynamicList

import android.content.Intent
import android.os.Bundle
import android.support.v4.util.ArrayMap
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import com.chad.library.adapter.base.BaseQuickAdapter
import com.zxcx.zhizhe.R
import com.zxcx.zhizhe.event.ClearMessageEvent
import com.zxcx.zhizhe.mvpBase.MvpActivity
import com.zxcx.zhizhe.ui.card.card.cardDetails.CardDetailsActivity
import com.zxcx.zhizhe.ui.my.message.system.message_collect
import com.zxcx.zhizhe.ui.my.message.system.message_follow
import com.zxcx.zhizhe.ui.my.message.system.message_like
import com.zxcx.zhizhe.utils.Constants
import com.zxcx.zhizhe.widget.CustomLoadMoreView
import com.zxcx.zhizhe.widget.EmptyView
import io.reactivex.Flowable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.functions.Function
import io.reactivex.schedulers.Schedulers
import io.reactivex.subscribers.DisposableSubscriber
import kotlinx.android.synthetic.main.activity_follow_message.*
import kotlinx.android.synthetic.main.toolbar.*
import org.greenrobot.eventbus.EventBus
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class DynamicMessageListActivity : MvpActivity<DynamicMessageListPresenter>(), DynamicMessageListContract.View,
        BaseQuickAdapter.RequestLoadMoreListener,BaseQuickAdapter.OnItemClickListener {

    private var mMessageType = 0
    private var mPage = 0
    private val mPageSize = Constants.PAGE_SIZE
    private lateinit var mAdapter: DynamicMessageAdapter
    private val map: ArrayMap<String, ArrayList<DynamicMessageListBean>> = ArrayMap()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_follow_message)
        mMessageType = intent.getIntExtra("messageType",0)

        initView()

        initRecyclerView()
        mPresenter.getDynamicMessageList(mMessageType,mPage,mPageSize)
    }

    override fun createPresenter(): DynamicMessageListPresenter {
        return DynamicMessageListPresenter(this)
    }

    override fun getDataSuccess(list: List<DynamicMessageListBean>) {
        mDisposable = Flowable.just(list)
                .observeOn(Schedulers.computation())
                .map(PackData(map))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object: DisposableSubscriber<List<DynamicBean>>() {
                    override fun onError(t: Throwable?) {
                    }

                    override fun onComplete() {
                    }

                    override fun onNext(list: List<DynamicBean>) {
                        mAdapter.data.clear()
                        for (dynamicBean in list){
                            mAdapter.data.add(dynamicBean)
                            mAdapter.data.addAll(dynamicBean.list)
                        }
                        mAdapter.notifyDataSetChanged()

                        iv_toolbar_right.visibility = if (mAdapter.data.isEmpty()) View.GONE else View.VISIBLE
                        mPage ++
                        if (list.size < mPageSize) {
                            mAdapter.loadMoreEnd(false)
                        } else {
                            mAdapter.loadMoreComplete()
                            mAdapter.setEnableLoadMore(false)
                            mAdapter.setEnableLoadMore(true)
                        }
                    }

                })
    }

    override fun postSuccess() {
        mAdapter.data.clear()
        mAdapter.notifyDataSetChanged()
        iv_toolbar_right.visibility = View.GONE
        EventBus.getDefault().post(ClearMessageEvent(mMessageType))
    }

    override fun postFail(msg: String?) {
        toastShow(msg)
    }

    override fun onLoadMoreRequested() {
        mPresenter.getDynamicMessageList(mMessageType,mPage,mPageSize)
    }

    override fun onItemClick(adapter: BaseQuickAdapter<*, *>?, view: View?, position: Int) {
        if (mAdapter.data[position].itemType == dynamic_content) {
            val bean = mAdapter.data[position] as DynamicMessageListBean
            if (bean.messageType != message_follow){
                val intent = Intent(mActivity,CardDetailsActivity::class.java)
                intent.putExtra("id",bean.relatedCardId)
                startActivity(intent)
            }
        }
    }

    private fun initView() {
        when (mMessageType) {
            message_follow -> {
                initToolBar("关注")
            }
            message_like -> {
                initToolBar("点赞")
            }
            message_collect -> {
                initToolBar("收藏")
            }
        }

        iv_toolbar_right.visibility = View.VISIBLE
        iv_toolbar_right.setImageResource(R.drawable.common_delete)
        iv_toolbar_right.setOnClickListener {
            mPresenter.deleteDynamicMessageList(mMessageType)
        }
    }

    private fun initRecyclerView() {
        mAdapter = DynamicMessageAdapter(ArrayList())
        mAdapter.onItemClickListener = this
        mAdapter.setLoadMoreView(CustomLoadMoreView())
        mAdapter.setOnLoadMoreListener(this,rv_follow_message)
        rv_follow_message.layoutManager = LinearLayoutManager(mActivity, LinearLayoutManager.VERTICAL,false)
        rv_follow_message.adapter = mAdapter
        val emptyView = EmptyView.getEmptyView(mActivity,"暂无消息","可前往系统设置开启",null,null)
        mAdapter.emptyView = emptyView
    }

    class PackData(private val map: ArrayMap<String, ArrayList<DynamicMessageListBean>>): Function<List<DynamicMessageListBean>, List<DynamicBean>> {
        private val dateFormat = SimpleDateFormat("yyyy/MM/dd", Locale.CHINA)
        private val timeFormat = SimpleDateFormat("HH:mm", Locale.CHINA)
        override fun apply(list: List<DynamicMessageListBean>): List<DynamicBean> {
            for (bean in list) {
                val date = dateFormat.format(bean.date)
                val time = timeFormat.format(bean.date)
                bean.newTime = time
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
                Collections.sort(mutableEntry.value) { o1, o2 ->
                    o2?.date?.compareTo(o1?.date)!!
                }
                val dynamicBean = DynamicBean(mutableEntry.key,mutableEntry.value)
                dynamicBeanList.add(dynamicBean)
            }
            Collections.sort(dynamicBeanList) { o1, o2 ->
                o2?.list?.get(0)?.date?.compareTo(o1?.list?.get(0)?.date)!!
            }
            return dynamicBeanList
        }
    }
}
