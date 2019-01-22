package com.zxcx.zhizhe.ui.circle.circlehome

import android.os.Bundle
import android.support.v4.view.ViewPager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.chad.library.adapter.base.BaseQuickAdapter
import com.youth.banner.BannerConfig
import com.zxcx.zhizhe.R
import com.zxcx.zhizhe.event.LogoutEvent
import com.zxcx.zhizhe.mvpBase.MvpFragment
import com.zxcx.zhizhe.ui.circle.adapter.CircleAdapter
import com.zxcx.zhizhe.ui.circle.bean.CircleClassifyBean
import com.zxcx.zhizhe.ui.welcome.ADBean
import com.zxcx.zhizhe.ui.welcome.WebViewActivity
import com.zxcx.zhizhe.utils.Constants
import com.zxcx.zhizhe.utils.GlideBannerImageLoader
import com.zxcx.zhizhe.utils.LogCat
import com.zxcx.zhizhe.utils.startActivity
import com.zxcx.zhizhe.widget.gridview.Model
import kotlinx.android.synthetic.main.fragment_circle.*
import kotlinx.android.synthetic.main.fragment_hot.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

/**
 * @author : MarkFrank01
 * @Created on 2019/1/21
 * @Description :
 */
class CircleFragment : MvpFragment<CirclePresenter>(), CircleContract.View, CircleHomeOnClickListener,
        BaseQuickAdapter.RequestLoadMoreListener, BaseQuickAdapter.OnItemClickListener {

    private lateinit var mAdapter: CircleAdapter
    private lateinit var mCircleBean: CircleBean

    //广告块的数据
    private var mAdList: MutableList<ADBean> = mutableListOf()
    private var imageList: MutableList<String> = mutableListOf()

    //分类部分的数据
    private var mClassifyData:MutableList<Model> = mutableListOf()
    private var mClassifyPage = 0
    private var mClassifyPageSize = Constants.PAGE_SIZE

    //分类部分的adapter
//    private lateinit var mClassifyAdapter: CircleListAdapter

    override fun createPresenter(): CirclePresenter {
        return CirclePresenter(this)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_circle, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        EventBus.getDefault().register(this)
        mCircleBean = CircleBean()
        initRecyclerView()
        initADView()
        mPresenter.getAD()
        mPresenter.getClassify(mClassifyPage,mClassifyPageSize)
    }

    override fun onDestroyView() {
        EventBus.getDefault().unregister(this)
        super.onDestroyView()
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onMessageEvent(event: LogoutEvent) {
        toastShow("刷新成功")
    }

    override fun getDataSuccess(bean: MutableList<CircleBean>?) {
        LogCat.e("圈子获取圈子信息成功")

    }

    override fun getADSuccess(list: MutableList<ADBean>) {
        LogCat.e("圈子获取广告成功 ${list.size}")

        if (list.size > 0) {
            mAdList = list
            imageList.clear()
            mAdList.forEach {
                imageList.add(it.titleImage)
            }
            fl_banner_circle.visibility = View.VISIBLE
        } else {
            fl_banner_circle.visibility = View.GONE
        }
        banner_circle.setImages(imageList)
        banner_circle.start()
    }

    override fun getClassifySuccess(list: MutableList<CircleClassifyBean>) {
        LogCat.e("圈子获取分类成功 ${list.size}")
//        mCircleBean.classifyList = list
//        mClassifyAdapter.setNewData(list as List<MultiItemEntity>?)
//        mClassifyAdapter.addData(list)
        list.forEach {
             mClassifyData.add(Model(it.title,it.titleImage))
        }

        gv_circle_classify.pageSize = 10
        gv_circle_classify.setGridItemClickListener { pos, position, str ->
            toastShow("pos $pos")
        }
        gv_circle_classify.init(mClassifyData)
    }

    override fun onLoadMoreRequested() {
    }

    override fun onItemClick(adapter: BaseQuickAdapter<*, *>?, view: View?, position: Int) {
    }

    override fun circleOnClick(bean: CircleBean) {
    }


    private fun initRecyclerView() {

        //第二块
//        mGridViewPager
//                .setPageSize(10)
//                .setGridItemClickListener { pos, position, str ->
//                    toastShow("点击")
//                }


//        mClassifyAdapter = CircleListAdapter(ArrayList())
//        mClassifyAdapter.onItemClickListener = this
//
//        rv_circle_home_1.layoutManager = LinearLayoutManager(context,LinearLayoutManager.HORIZONTAL,false)
//        rv_circle_home_1.adapter = mClassifyAdapter

        //第三块
//        val layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
//        mAdapter = CircleAdapter(ArrayList(), this)
//        mAdapter.setLoadMoreView(CustomLoadMoreView())
//        mAdapter.setOnLoadMoreListener(this, rv_circle_home_2)
//        mAdapter.onItemClickListener = this
////        val view = EmptyView.getEmptyView(mActivity,"暂无内容",R.drawable.no_data)
////        mAdapter.emptyView = view
//
//        rv_circle_home_2.layoutManager = layoutManager
//        rv_circle_home_2.adapter = mAdapter
    }

    private fun initADView(){
        banner_circle.setImageLoader(GlideBannerImageLoader())
        banner_circle.setIndicatorGravity(BannerConfig.RIGHT)
        banner_circle.setOnBannerListener {
            val adUrl = mAdList[it].behavior
            val adTitle = mAdList[it].description
//            val adImage = mAdList[it].titleImage
            val adImage = mAdList[it].shareImage
            val shareDescription = mAdList[it].shareDescription
            mActivity.startActivity(WebViewActivity::class.java) {
                it.putExtra("isAD", true)
                it.putExtra("title", adTitle)
                it.putExtra("url", adUrl)
                it.putExtra("imageUrl", adImage)
                it.putExtra("shareDescription", shareDescription)
            }
        }

        banner_circle.setOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrollStateChanged(state: Int) {

            }

            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {

            }

            override fun onPageSelected(position: Int) {
                val newPosition = position % 3
                val ad = mAdList[newPosition]
                if (mAdList.size>0) {

                    if (iv_ad_label_card!=null) {

                        when (ad.styleType) {

                            0 -> {
                                iv_ad_label_card.setImageResource(R.drawable.iv_ad_label_0)
                            }
                            1 -> {
                                iv_ad_label_card.setImageResource(R.drawable.iv_ad_label_1)
                            }
                            2 -> {
                                iv_ad_label_card.setImageResource(R.drawable.iv_ad_label_2)
                            }
                        }
                    }
                }
            }

        })
    }
}