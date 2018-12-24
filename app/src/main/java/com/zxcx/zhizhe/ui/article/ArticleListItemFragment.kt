package com.zxcx.zhizhe.ui.article

import android.content.Intent
import android.os.Bundle
import android.support.v4.app.ActivityOptionsCompat
import android.support.v4.util.Pair
import android.support.v4.view.ViewPager
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.scwang.smartrefresh.layout.api.RefreshLayout
import com.scwang.smartrefresh.layout.listener.OnRefreshListener
import com.youth.banner.BannerConfig
import com.zxcx.zhizhe.R
import com.zxcx.zhizhe.mvpBase.BaseRxJava
import com.zxcx.zhizhe.mvpBase.MvpFragment
import com.zxcx.zhizhe.retrofit.AppClient
import com.zxcx.zhizhe.retrofit.BaseSubscriber
import com.zxcx.zhizhe.ui.article.articleDetails.ArticleDetailsActivity
import com.zxcx.zhizhe.ui.article.subject.SubjectArticleActivity
import com.zxcx.zhizhe.ui.card.hot.CardBean
import com.zxcx.zhizhe.ui.welcome.ADBean
import com.zxcx.zhizhe.ui.welcome.WebViewActivity
import com.zxcx.zhizhe.utils.*
import com.zxcx.zhizhe.widget.CustomLoadMoreView
import kotlinx.android.synthetic.main.fragment_long_list_item.*

/**
 * 首页-长文-其他Tab的Fragment
 */

//class ArticleListItemFragment : BaseFragment(), IGetPresenter<MutableList<ArticleAndSubjectBean>>,
//		BaseQuickAdapter.RequestLoadMoreListener, BaseQuickAdapter.OnItemClickListener,
//		OnRefreshListener, SubjectOnClickListener {
class ArticleListItemFragment : MvpFragment<ArticleListItemPresenter>(), ArticleListItemContract.View,
        BaseQuickAdapter.RequestLoadMoreListener, BaseQuickAdapter.OnItemClickListener,
        OnRefreshListener, SubjectOnClickListener {


    private lateinit var mAdapter: ArticleAndSubjectAdapter
    private var mPage = 0

    private var mAdList: MutableList<ADBean> = mutableListOf()
    private var imageList: MutableList<String> = mutableListOf()

    private var ad_type_position = 0


    companion object {
        const val ARG_ID = "categoryId"
        @JvmStatic
        fun newInstance(id: Int) =
                ArticleListItemFragment().apply {
                    arguments = Bundle().apply {
                        putInt(ARG_ID, id)
                    }
                }
    }

    private var categoryId: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            categoryId = it.getInt(ARG_ID)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_long_list_item, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initRecyclerView()
        initView()
        refresh_layout.setOnRefreshListener(this)
        getArticleListForCategory(categoryId, mPage)

        ad_type_position = SharedPreferencesUtil.getInt(SVTSConstants.adTypePositionLong, 0)
        onRefreshAD(ad_type_position)
    }

    override fun onStart() {
        super.onStart()
        //开始轮播
        banner_card.startAutoPlay()
    }

    override fun onStop() {
        super.onStop()
        //结束轮播
        banner_card.stopAutoPlay()
    }

    private fun initRecyclerView() {
        val layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        mAdapter = ArticleAndSubjectAdapter(arrayListOf(), this)
        mAdapter.setLoadMoreView(CustomLoadMoreView())
        mAdapter.setOnLoadMoreListener(this, rv_card_list_item)
        mAdapter.onItemClickListener = this
        rv_card_list_item.setBackgroundResource(R.color.strip)
        rv_card_list_item.layoutManager = layoutManager
        rv_card_list_item.adapter = mAdapter
        rv_card_list_item.addItemDecoration(ArticleItemDecoration())
    }

    private fun initView() {
        banner_card.setImageLoader(GlideBannerImageLoader())
        banner_card.setIndicatorGravity(BannerConfig.RIGHT)
        banner_card.setOnBannerListener {
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
                it.putExtra("shareDescription",shareDescription)
            }
        }

        banner_card.setOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrollStateChanged(state: Int) {

            }

            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {

            }

            override fun onPageSelected(position: Int) {
                val newPosition = position % 3
                val ad = mAdList[newPosition]
                if (mAdList.size>0) {
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

        })
    }

    override fun onItemClick(adapter: BaseQuickAdapter<*, *>, view: View, position: Int) {
        val bean = adapter.data[position] as ArticleAndSubjectBean
        if (bean.itemType == ArticleAndSubjectBean.TYPE_ARTICLE) {
            val articleImg = view.findViewById<ImageView>(R.id.iv_item_card_icon)
            val articleTitle = view.findViewById<TextView>(R.id.tv_item_card_title)
            val articleCategory = view.findViewById<TextView>(R.id.tv_item_card_category)
            val articleLabel = view.findViewById<TextView>(R.id.tv_item_card_label)
            val bundle = ActivityOptionsCompat.makeSceneTransitionAnimation(mActivity,
                    Pair.create(articleImg, articleImg.transitionName),
                    Pair.create(articleTitle, articleTitle.transitionName),
                    Pair.create(articleCategory, articleCategory.transitionName),
                    Pair.create(articleLabel, articleLabel.transitionName)).toBundle()
            val intent = Intent(mActivity, ArticleDetailsActivity::class.java)
            intent.putExtra("cardBean", bean.cardBean)
            mActivity.startActivity(intent, bundle)
        }
    }

    override fun onRefresh(refreshLayout: RefreshLayout?) {
        mPage = 0
        getArticleListForCategory(categoryId, mPage)
    }

    override fun onLoadMoreRequested() {
        getArticleListForCategory(categoryId, mPage)
    }

    override fun getDataSuccess(list: MutableList<ArticleAndSubjectBean>) {
        if (mPage == 0) {
            refresh_layout.finishRefresh()
            mAdapter.setNewData(list)
        } else {
            mAdapter.addData(list)
        }
        mPage++
        if (list.isEmpty()) {
            mAdapter.loadMoreEnd(false)
        } else {
            mAdapter.loadMoreComplete()
            mAdapter.setEnableLoadMore(false)
            mAdapter.setEnableLoadMore(true)
        }
    }

    override fun getADSuccess(list: MutableList<ADBean>) {
        if (list.size > 0) {
            mAdList = list
            imageList.clear()
            mAdList.forEach {
                imageList.add(it.titleImage)
            }
            fl_banner_long.visibility = View.VISIBLE
            fl_line.visibility = View.VISIBLE

            banner_card.setImages(imageList)
            banner_card.start()
        } else {
            fl_banner_long.visibility = View.GONE
            fl_line.visibility = View.GONE
        }

//        if (ad_type_position != 0) {
//            onRefreshAD(ad_type_position)
//        } else {
//            onRefreshAD(0)
//        }
    }

    override fun createPresenter(): ArticleListItemPresenter {
        return ArticleListItemPresenter(this)
    }

    private fun getArticleListForCategory(cardCategoryId: Int, page: Int) {
        mDisposable = AppClient.getAPIService().getArticleListForCategory(cardCategoryId, page, Constants.PAGE_SIZE)
                .compose(BaseRxJava.handleArrayResult())
                .compose(BaseRxJava.io_main())
                .subscribeWith(object : BaseSubscriber<MutableList<ArticleAndSubjectBean>>(mPresenter) {
                    override fun onNext(t: MutableList<ArticleAndSubjectBean>) {
                        getDataSuccess(t)
                    }
                })
        addSubscription(mDisposable)
    }

    override fun articleOnClick(bean: CardBean) {
        mActivity.startActivity(ArticleDetailsActivity::class.java) {
            it.putExtra("cardBean", bean)
        }
    }

    override fun subjectOnClick(bean: SubjectBean) {
        val intent = Intent(mActivity, SubjectArticleActivity::class.java)
        intent.putExtra("id", bean.id)
        intent.putExtra("name", bean.name)
        mActivity.startActivity(intent)
    }

    private fun onRefreshAD(id: Int) {
        mPresenter.getAD(id)
    }

    override fun closeAD() {
        fl_banner_long.visibility = View.GONE
        fl_line.visibility = View.GONE
    }
}
