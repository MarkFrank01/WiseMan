package com.zxcx.zhizhe.ui.circle.circlehome

import android.os.Bundle
import android.support.v4.view.ViewPager
import android.support.v7.widget.GridLayoutManager
import android.util.ArrayMap
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.chad.library.adapter.base.BaseQuickAdapter
import com.youth.banner.BannerConfig
import com.zxcx.zhizhe.R
import com.zxcx.zhizhe.event.LogoutEvent
import com.zxcx.zhizhe.mvpBase.MvpFragment
import com.zxcx.zhizhe.ui.circle.adapter.CircleAdapter
import com.zxcx.zhizhe.ui.circle.allmycircle.AllMyCircleActivity
import com.zxcx.zhizhe.ui.circle.bean.CircleClassifyBean
import com.zxcx.zhizhe.ui.circle.circledetaile.CircleDetaileActivity
import com.zxcx.zhizhe.ui.circle.classify.CircleClassifyActivity
import com.zxcx.zhizhe.ui.circle.createcircle.CreateCircleActivity
import com.zxcx.zhizhe.ui.my.message.MessageActivity
import com.zxcx.zhizhe.ui.search.search.SearchActivity
import com.zxcx.zhizhe.ui.welcome.ADBean
import com.zxcx.zhizhe.ui.welcome.WebViewActivity
import com.zxcx.zhizhe.utils.Constants
import com.zxcx.zhizhe.utils.GlideBannerImageLoader
import com.zxcx.zhizhe.utils.LogCat
import com.zxcx.zhizhe.utils.startActivity
import com.zxcx.zhizhe.widget.CustomLoadMoreView
import com.zxcx.zhizhe.widget.EmptyView
import com.zxcx.zhizhe.widget.gridview.Model
import io.reactivex.functions.Function
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
        BaseQuickAdapter.RequestLoadMoreListener, BaseQuickAdapter.OnItemClickListener, BaseQuickAdapter.OnItemChildClickListener {


    private lateinit var mAdapter: CircleAdapter
    private lateinit var mCircleBean: CircleBean

    //广告块的数据
    private var mAdList: MutableList<ADBean> = mutableListOf()
    private var imageList: MutableList<String> = mutableListOf()

    //分类部分的数据
    private var mClassifyData: MutableList<Model> = mutableListOf()
    private var mClassifyPage = 0
    private var mClassifyPageSize = Constants.PAGE_SIZE

    private var mClassifySAVEData: MutableList<CircleClassifyBean> = mutableListOf()

    //圈子列表的数据
    private var mCircleListPage = 0
    private var mCircleListPageSize = Constants.PAGE_SIZE

    //处理分组数据
    private val map: ArrayMap<String, ArrayList<CircleBean>> = ArrayMap()

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
        initView()
        initRecyclerView()
        initADView()
        mPresenter.getAD()
        mPresenter.getClassify(mClassifyPage, mClassifyPageSize)
        getCircleById()
    }

    override fun onDestroyView() {
        EventBus.getDefault().unregister(this)
        super.onDestroyView()
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onMessageEvent(event: LogoutEvent) {
        toastShow("刷新成功")
    }

    override fun getDataSuccess(list: MutableList<CircleBean>) {

//        mDisposable = Flowable.just(list)
//                .observeOn(Schedulers.computation())
//                .map(PackData(map))
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribeWith(object : DisposableSubscriber<List<CircleChooseBean>>() {
//                    override fun onComplete() {
//                    }
//
//                    override fun onNext(t: List<CircleChooseBean>) {
//                        LogCat.e("CircleChooseBean size ${t.size}")
//                        mAdapter.data.clear()
//                        for (dcBean in t) {
//
//                            dcBean.list[0].showTitle = dcBean.date
//                            mAdapter.data.addAll(dcBean.list)
//                            if (dcBean.list.size % 2 != 0) {
//                                mAdapter.data.add(dcBean)
//                            }
//                        }
//                        mAdapter.notifyDataSetChanged()
//
//                        mCircleListPage++
//                        if (list.size < mCircleListPageSize) {
//                            mAdapter.loadMoreEnd(false)
//                        } else {
//                            mAdapter.loadMoreComplete()
//                            mAdapter.setEnableLoadMore(false)
//                            mAdapter.setEnableLoadMore(true)
//                        }
//                    }
//
//                    override fun onError(t: Throwable?) {
//                    }
//
//                })
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
        list.forEach {
            mClassifyData.add(Model(it.title, it.titleImage))
            mClassifySAVEData.add(it)
        }

        if (mClassifyPage < 3) {
            mClassifyPage++
            mPresenter.getClassify(mClassifyPage, mClassifyPageSize)
        } else {
            gv_circle_classify.pageSize = 10
            gv_circle_classify.setGridItemClickListener { pos, position, str ->

                mActivity.startActivity(CircleClassifyActivity::class.java) {
                    it.putExtra("circleClassifyActivityID", mClassifySAVEData[position].id)
                    it.putExtra("circleClassifyActivityTitle", mClassifySAVEData[position].title)
                }
            }
            gv_circle_classify.init(mClassifyData)
        }
    }

    override fun getMyJoinCircleListSuccess(list: MutableList<CircleBean>) {
        iv_circle_to_my.visibility = View.VISIBLE

//        if (list[0].titleImage.isNotEmpty() && list[0].titleImage != "") {
//            circle_image.visibility = View.VISIBLE
//            ImageLoader.load(mActivity, list[0].titleImage, R.drawable.default_card, circle_image)
//
//        }
//        if (list.size > 1) {
//            if (list[1].titleImage.isNotEmpty() && list[1].titleImage != "") {
//                circle_image2.visibility = View.VISIBLE
//                ImageLoader.load(mActivity, list[1].titleImage, R.drawable.default_card, circle_image2)
//            }
//        }
//
//        if (list.size > 2) {
//            if (list[2].titleImage.isNotEmpty() && list[2].titleImage != "") {
//                circle_image3.visibility = View.VISIBLE
//                ImageLoader.load(mActivity, list[2].titleImage, R.drawable.default_card, circle_image3)
//            }
//        }
    }

    override fun onLoadMoreRequested() {
        getCircleById()
    }

    override fun onItemClick(adapter: BaseQuickAdapter<*, *>, view: View, position: Int) {

    }

    override fun onItemChildClick(adapter: BaseQuickAdapter<*, *>, view: View, position: Int) {
        when (view.id) {
            R.id.to_content_circle -> {
                val bean = adapter.data[position] as CircleBean
                toastShow("to circle")
                mActivity.startActivity(CircleDetaileActivity::class.java) {
                    it.putExtra("circleID", bean.id)
                    //   val bean = adapter.data[position] as CardBean
                }
            }
        }
    }


    override fun circleOnClick(bean: CircleBean) {
    }

    override fun setListener() {
        iv_circle_to_my.setOnClickListener {
            //            mActivity.startActivity(MyCircleActivity::class.java){}
            mActivity.startActivity(AllMyCircleActivity::class.java) {}
        }

        iv_3.setOnClickListener {
            mActivity.startActivity(CreateCircleActivity::class.java) {}

        }
        iv_2.setOnClickListener {
            if (checkLogin()) {
                mActivity.startActivity(MessageActivity::class.java) {}
            }
        }
        iv_1.setOnClickListener {
            mActivity.startActivity(SearchActivity::class.java, {})
        }
    }

    private fun initRecyclerView() {

        //第三块
//        val layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
//        val layoutManager = GridLayoutManager(context, 2)
        mAdapter = CircleAdapter(ArrayList(), this)
        mAdapter.setLoadMoreView(CustomLoadMoreView())
        mAdapter.setOnLoadMoreListener(this, rv_circle_home_2)
        mAdapter.onItemClickListener = this
        mAdapter.onItemChildClickListener = this

        val view = EmptyView.getEmptyView(mActivity,"暂无内容",R.drawable.no_data)
        mAdapter.emptyView = view
//        mAdapter.setSpanSizeLookup { _, position ->
//
//        }
        rv_circle_home_2.layoutManager = object :GridLayoutManager(context,2){
            override fun canScrollVertically() = false
        }
        rv_circle_home_2.isNestedScrollingEnabled = false
        rv_circle_home_2.setHasFixedSize(true)
        rv_circle_home_2.isFocusable = false

//        rv_circle_home_2.layoutManager = layoutManager
        rv_circle_home_2.adapter = mAdapter
    }

    private fun initADView() {
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
                if (mAdList.size > 0) {

                    if (iv_ad_label_card != null) {

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

    private fun initView() {
        if (checkLogin()) {
            circle_hint_login.visibility = View.GONE
            circle_hint_login_dec.visibility = View.GONE
        }
        mPresenter.getMyJoinCircleList(0, 3)
    }

    //获取圈子
    private fun getCircleById() {
        mPresenter.getRecommendCircleListByPage(mCircleListPage, mCircleListPageSize)
    }
}

class PackData(private val map: ArrayMap<String, ArrayList<CircleBean>>) : Function<List<CircleBean>, List<CircleChooseBean>> {

    override fun apply(list: List<CircleBean>): List<CircleChooseBean> {
        for (bean in list) {
            val chooseTitle = bean.classifytitle
            bean.newTitle = bean.classifytitle
            if (map.containsKey(chooseTitle)) {
                map[chooseTitle]?.add(bean)
            } else {
                val newList: ArrayList<CircleBean> = ArrayList()
                newList.add(bean)
                map[chooseTitle] = newList
            }
        }
        val dcBeanList = ArrayList<CircleChooseBean>()
        for (mutableEntry in map) {
            mutableEntry.value.sortWith(Comparator { o1, o2 ->
                o2?.classifytitle?.compareTo(o1.classifytitle)!!
            })
            val ChooseBean = CircleChooseBean(mutableEntry.key, mutableEntry.value)
            dcBeanList.add(ChooseBean)
        }
        dcBeanList.sortWith(Comparator { o1, o2 ->
            o2?.list?.get(0)?.classifytitle?.compareTo(o1?.list?.get(0)?.classifytitle!!)!!
        })
        return dcBeanList
    }

}