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
import com.zxcx.zhizhe.event.LoginEvent
import com.zxcx.zhizhe.event.LogoutEvent
import com.zxcx.zhizhe.mvpBase.MvpFragment
import com.zxcx.zhizhe.ui.circle.adapter.CircleNewAdapter
import com.zxcx.zhizhe.ui.circle.allmycircle.AllMyCircleActivity
import com.zxcx.zhizhe.ui.circle.bean.CircleClassifyBean
import com.zxcx.zhizhe.ui.circle.circledetaile.CircleDetaileActivity
import com.zxcx.zhizhe.ui.circle.circlemessage.CircleMessageActivity
import com.zxcx.zhizhe.ui.circle.classify.CircleClassifyActivity
import com.zxcx.zhizhe.ui.circle.classify.CircleTuiActivity
import com.zxcx.zhizhe.ui.loginAndRegister.login.LoginActivity
import com.zxcx.zhizhe.ui.search.search.SearchActivity
import com.zxcx.zhizhe.ui.welcome.ADBean
import com.zxcx.zhizhe.ui.welcome.WebViewActivity
import com.zxcx.zhizhe.utils.*
import com.zxcx.zhizhe.widget.gridview.Model
import io.reactivex.functions.Function
import kotlinx.android.synthetic.main.fragment_circle.*
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


    //    private lateinit var mAdapter: CircleAdapter
    private lateinit var mCircleBean: CircleBean

    //新的CircleAdapter
    private lateinit var mCircleNewAdapter: CircleNewAdapter

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
    private var mCircleListPageSize = 6

    //处理分组数据
    private val map: ArrayMap<String, ArrayList<CircleBean>> = ArrayMap()

    //记录前三个圈子的ID
    private var id_1:Int = 0
    private var id_2:Int = 0
    private var id_3:Int = 0

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
//        initRecyclerView()

        initRv()
        initADView()
        mPresenter.getAD()
        mPresenter.getClassify(mClassifyPage, mClassifyPageSize)
        mPresenter.getIndexCircleList()
        getCircleById()
    }

    override fun onDestroyView() {
        EventBus.getDefault().unregister(this)
        super.onDestroyView()
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onMessageEvent(event: LogoutEvent) {
//        toastShow("刷新成功")
        circle_hint_login.visibility = View.VISIBLE
        circle_hint_login_dec.visibility = View.VISIBLE
        ImageLoader.load(mActivity, R.drawable.iv_my_head_placeholder, R.drawable.default_card, circle_image)
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onMessageEvent(event: LoginEvent) {
        circle_hint_login.visibility = View.GONE
        circle_hint_login_dec.visibility = View.GONE
        ImageLoader.load(mActivity, R.drawable.c_circle_default, R.drawable.default_card, circle_image)
    }

    override fun getDataSuccess(list: MutableList<CircleBean>) {

        if (list.size<1){
            mCircleListPage = 0
            getCircleById()
        }else{
            mCircleNewAdapter.data.clear()
            mCircleNewAdapter.data.addAll(list)
            mCircleNewAdapter.notifyDataSetChanged()
            mCircleListPage++
        }

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
//                            if (dcBean.list.size>1) {
//                                dcBean.list[1].showTitle = "更多"
//                            }
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
            banner_circle.setImages(imageList)
            banner_circle.start()
        } else {
            fl_banner_circle.visibility = View.GONE
        }
//        banner_circle.setImages(imageList)
//        banner_circle.start()
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

    //获取首页显示的我的圈子
    override fun getMyJoinCircleListSuccess(list: MutableList<CircleBean>) {
//        iv_circle_to_my.visibility = View.VISIBLE

        if (list.size > 0) {
            if (list[0].titleImage.isNotEmpty() && list[0].titleImage != "") {

                circle_image1.visibility = View.VISIBLE
                ImageLoader.load(mActivity, list[0].titleImage, R.drawable.default_card, circle_image1)

                id_1 = list[0].id
            }
        }
        if (list.size > 1) {
            if (list[1].titleImage.isNotEmpty() && list[1].titleImage != "") {
                circle_image2.visibility = View.VISIBLE
                ImageLoader.load(mActivity, list[1].titleImage, R.drawable.default_card, circle_image2)

                id_2 = list[1].id
            }
        }

        if (list.size > 2) {
            if (list[2].titleImage.isNotEmpty() && list[2].titleImage != "") {
                circle_image3.visibility = View.VISIBLE
                ImageLoader.load(mActivity, list[2].titleImage, R.drawable.default_card, circle_image3)

                id_3 = list[2].id
            }
        }
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
        circle_image.setOnClickListener {
            if (SharedPreferencesUtil.getInt(SVTSConstants.userId, 0) != 0) {
                mActivity.startActivity(AllMyCircleActivity::class.java) {}
            } else {
                mActivity.startActivity(LoginActivity::class.java) {}
            }
        }



        iv_3.setOnClickListener {
            //方便测试时这里是创建圈子
//            mActivity.startActivity(CreateCircleActivity::class.java) {}

            //正式时为搜索功能
//            mActivity.startActivity(CircleSearchPreActivity::class.java){}
            mActivity.startActivity(SearchActivity::class.java){}
        }
        //圈子的消息
        iv_2.setOnClickListener {
            if (checkLogin()) {
//                mActivity.startActivity(MessageActivity::class.java) {}
                mActivity.startActivity(CircleMessageActivity::class.java){}
            }
        }
        //圈子外面的搜索
        iv_1.setOnClickListener {
            mActivity.startActivity(SearchActivity::class.java, {})
        }

        circle_hint_login.setOnClickListener {
            mActivity.startActivity(LoginActivity::class.java) {}
        }

        circle_hint_login_dec.setOnClickListener {
            mActivity.startActivity(LoginActivity::class.java) {}
        }

        more_change.setOnClickListener {
            getCircleById()
        }

        to_more_tuijian.setOnClickListener {
            mActivity.startActivity(CircleTuiActivity::class.java){}

//            mActivity.startActivity(CircleClassifyActivity::class.java){
//                it.putExtra("circleClassifyActivityID", 0)
//                it.putExtra("circleClassifyActivityTitle", "推荐")
//            }
        }

        //直接进入自己的圈子
        circle_image1.setOnClickListener {
            mActivity.startActivity(CircleDetaileActivity::class.java){
                it.putExtra("circleID",id_1)
            }
        }

        circle_image2.setOnClickListener {
            mActivity.startActivity(CircleDetaileActivity::class.java){
                it.putExtra("circleID",id_2)
            }
        }

        circle_image3.setOnClickListener {
            mActivity.startActivity(CircleDetaileActivity::class.java){
                it.putExtra("circleID",id_3)
            }
        }
    }

    private fun initRecyclerView() {

        //第三块

//        mAdapter = CircleAdapter(ArrayList(), this)
//        mAdapter.setLoadMoreView(CustomLoadMoreView())
//        mAdapter.setOnLoadMoreListener(this, rv_circle_home_2)
//        mAdapter.onItemClickListener = this
//        mAdapter.onItemChildClickListener = this
//
//        val view = EmptyView.getEmptyView2(mActivity,"",R.drawable.c_test)
//        mAdapter.emptyView = view
////        mAdapter.setSpanSizeLookup { _, position ->
////
////        }
//        rv_circle_home_2.layoutManager = object :GridLayoutManager(context,2){
//            override fun canScrollVertically() = false
//        }
//        rv_circle_home_2.isNestedScrollingEnabled = false
//        rv_circle_home_2.setHasFixedSize(true)
//        rv_circle_home_2.isFocusable = false

//        rv_circle_home_2.adapter = mAdapter
    }

    private fun initRv() {
        mCircleNewAdapter = CircleNewAdapter(ArrayList())
        mCircleNewAdapter.onItemClickListener = this
        mCircleNewAdapter.onItemChildClickListener = this

        rv_circle_home_2.layoutManager = object : GridLayoutManager(context, 2) {
            override fun canScrollVertically() = false
        }
        rv_circle_home_2.isNestedScrollingEnabled = false
        rv_circle_home_2.setHasFixedSize(true)
        rv_circle_home_2.isFocusable = false

        rv_circle_home_2.adapter = mCircleNewAdapter
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

                    if (iv_ad_label_circle != null) {

                        when (ad.styleType) {

                            0 -> {
                                iv_ad_label_circle.setImageResource(R.drawable.iv_ad_label_0)
                            }
                            1 -> {
                                iv_ad_label_circle.setImageResource(R.drawable.iv_ad_label_1)
                            }
                            2 -> {
                                iv_ad_label_circle.setImageResource(R.drawable.iv_ad_label_2)
                            }
                        }
                    }
                }
            }

        })
    }

    private fun initView() {
        if (checkLogin1()) {
            circle_hint_login.visibility = View.GONE
            circle_hint_login_dec.visibility = View.GONE
            ImageLoader.load(mActivity, R.drawable.c_circle_default, R.drawable.default_card, circle_image)
        } else {
            circle_hint_login.visibility = View.VISIBLE
            circle_hint_login_dec.visibility = View.VISIBLE
            ImageLoader.load(mActivity, R.drawable.iv_my_head_placeholder, R.drawable.default_card, circle_image)

        }
//        mPresenter.getMyJoinCircleList(0, 3)
    }

    //获取圈子
    private fun getCircleById() {
        mPresenter.getRecommendCircleListByPage(mCircleListPage, mCircleListPageSize)
    }

    fun checkLogin1(): Boolean {
        return SharedPreferencesUtil.getInt(SVTSConstants.userId, 0) != 0
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