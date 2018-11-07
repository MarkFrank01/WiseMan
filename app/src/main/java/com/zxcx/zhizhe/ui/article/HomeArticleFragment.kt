package com.zxcx.zhizhe.ui.article

import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentStatePagerAdapter
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.uuch.adlibrary.AdConstant
import com.uuch.adlibrary.AdManager
import com.uuch.adlibrary.bean.AdInfo
import com.youth.banner.transformer.DepthPageTransformer
import com.zxcx.zhizhe.R
import com.zxcx.zhizhe.mvpBase.BaseRxJava
import com.zxcx.zhizhe.mvpBase.MvpFragment
import com.zxcx.zhizhe.retrofit.AppClient
import com.zxcx.zhizhe.retrofit.BaseSubscriber
import com.zxcx.zhizhe.ui.article.attention.AttentionArticleFragment
import com.zxcx.zhizhe.ui.card.cardList.CardCategoryBean
import com.zxcx.zhizhe.ui.search.search.HotSearchBean
import com.zxcx.zhizhe.ui.search.search.SearchActivity
import com.zxcx.zhizhe.ui.welcome.ADBean
import com.zxcx.zhizhe.ui.welcome.WebViewActivity
import com.zxcx.zhizhe.utils.SVTSConstants
import com.zxcx.zhizhe.utils.SharedPreferencesUtil
import com.zxcx.zhizhe.utils.startActivity
import kotlinx.android.synthetic.main.fragment_home_article.*

/**
 * 首页-长文Fragment
 */

//class HomeArticleFragment : BaseFragment(), IGetPresenter<MutableList<CardCategoryBean>> {
class HomeArticleFragment : MvpFragment<HomeArticlePresenter>(), HomeArticleContract.View {


    private var advList: ArrayList<AdInfo> = ArrayList()
    private var mAdList: MutableList<ADBean> = mutableListOf()


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home_article, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        tl_card_list.setupWithViewPager(vp_article_list)
        tl_card_list.getTabAt(1)?.select()
        getArticleCategory()

        onRefreshAD()

//        showFirstDialog()
//        val adManager = AdManager(activity, advList)
//        adManager.setOverScreen(true)
//                .setPageTransformer(DepthPageTransformer())
//                .setOnImageClickListener { view, advInfo ->
//                    toastShow("get AD")
//                }
//
//        adManager.showAdDialog(AdConstant.ANIM_DOWN_TO_UP)
    }

    override fun onResume() {
        super.onResume()
        getSearchDefaultKeyword()
    }

    override fun setListener() {
        super.setListener()
        tv_home_article_search.setOnClickListener {
            mActivity.startActivity(SearchActivity::class.java) {}
        }
    }

    override fun getDataSuccess(list: MutableList<CardCategoryBean>) {
        vp_article_list.adapter = fragmentManager?.let { CardListViewPagerAdapter(list, it) }
        tl_card_list.removeAllTabs()
        list.forEach {
            val tab = tl_card_list.newTab()
            tab.setCustomView(R.layout.tab_card_list)
            val textView = tab.customView?.findViewById<TextView>(R.id.tv_tab_card_list)
            textView?.text = it.name
            tl_card_list.addTab(tab)
        }
    }

    override fun createPresenter(): HomeArticlePresenter {
        return HomeArticlePresenter(this)
    }

    override fun getADSuccess(list: MutableList<ADBean>) {
        var title = ""
        var url = ""

        if (list.size > 0) {
            mAdList = list
            mAdList.forEach {
                addImageData(it.titleImage)
                title = it.description
                url = it.behavior
            }
            showImageDialog(title, url)
        }
    }


    fun getSearchDefaultKeywordSuccess(bean: HotSearchBean) {
        tv_home_article_search.text = bean.conent
    }

//    override fun getDataFail(msg: String?) {
//        toastFail(msg)
//    }

    private fun getArticleCategory() {
        mDisposable = AppClient.getAPIService().articleCategory
                .compose(BaseRxJava.handleArrayResult())
                .compose(BaseRxJava.io_main())
                .subscribeWith(object : BaseSubscriber<MutableList<CardCategoryBean>>(mPresenter) {
                    override fun onNext(t: MutableList<CardCategoryBean>) {
                        getDataSuccess(t)
                    }
                })
        addSubscription(mDisposable)
    }

    private fun getSearchDefaultKeyword() {
        mDisposable = AppClient.getAPIService().searchDefaultKeyword
                .compose(BaseRxJava.handleResult())
                .compose(BaseRxJava.io_main())
                .subscribeWith(object : BaseSubscriber<HotSearchBean>(mPresenter) {
                    override fun onNext(t: HotSearchBean) {
                        getSearchDefaultKeywordSuccess(t)
                    }
                })
        addSubscription(mDisposable)
    }

    class CardListViewPagerAdapter(val list: MutableList<CardCategoryBean>, fm: FragmentManager) : FragmentStatePagerAdapter(fm) {

        private val articleFragment = AttentionArticleFragment()

        override fun getItem(position: Int): Fragment {
            return if (list[position].id == -1) {
                articleFragment
            } else {
                SharedPreferencesUtil.saveData(SVTSConstants.adTypePositionLong, position)
                ArticleListItemFragment.newInstance(list[position].id)
            }
        }

        override fun getCount(): Int {
            return list.size
        }
    }

    private fun addImageData(url: String) {
        val adInfo = AdInfo()
        adInfo.activityImg = url
        advList.add(adInfo)
    }

//    private fun showFirstDialog() {
//        val adInfo = AdInfo()
//        adInfo.activityImg = "https://raw.githubusercontent.com/yipianfengye/android-adDialog/master/images/testImage1.png"
//        advList.add(adInfo)
//    }

    private fun showImageDialog(title: String, url: String) {
        val adManager = AdManager(activity, advList)
        adManager.setOverScreen(true)
                .setPageTransformer(DepthPageTransformer())
                .setOnImageClickListener { _, _ ->
                    val intent = Intent(context, WebViewActivity::class.java)
                    intent.putExtra("title", title)
                    intent.putExtra("url",url)
                    startActivity(intent)
                }
        adManager.showAdDialog(AdConstant.ANIM_DOWN_TO_UP)
    }

    private fun onRefreshAD() {
        mPresenter.getAD()
    }
}
