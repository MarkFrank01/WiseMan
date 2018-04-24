package com.zxcx.zhizhe.ui.welcome

import android.content.Intent
import android.os.Bundle
import android.support.v4.view.PagerAdapter
import android.support.v4.view.ViewPager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import butterknife.ButterKnife
import com.zxcx.zhizhe.R
import com.zxcx.zhizhe.mvpBase.BaseActivity
import com.zxcx.zhizhe.ui.MainActivity
import com.zxcx.zhizhe.utils.Utils
import kotlinx.android.synthetic.main.activity_guide_page.*
import java.util.*

class GuidePageActivity : BaseActivity() {

    /**
     * 5张引导页面的图片
     */
    private val mImgIds = intArrayOf(R.drawable.guide_1, R.drawable.guide_2, R.drawable.guide_3)
    /**
     * 图片资源容器
     */
    private val mImageViews = ArrayList<ImageView>()

    private var isScrolling: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_guide_page)
        ButterKnife.bind(this)

        initImgData()

        initViewPager()
    }

    override fun initStatusBar() {
        //覆盖父类修改状态栏方法
    }

    private fun initImgData() {
        for (imgId in mImgIds) {
            val imageView = ImageView(applicationContext)
            imageView.scaleType = ImageView.ScaleType.CENTER_CROP
            imageView.setImageResource(imgId)

            mImageViews.add(imageView)
        }
    }


    /**
     * 跳转到App主界面
     */
    private fun jumpToIndexActivity() {

        val intent = Intent(this, MainActivity::class.java)
        intent.putExtra("isFirst", true)
        startActivity(intent)

        Utils.setIsFirstLaunchApp(false)

        finish()
    }


    private fun initViewPager() {


        view_pager.adapter = object : PagerAdapter() {

            override fun instantiateItem(container: ViewGroup, position: Int): Any {
                // 给最后一页引导页添加点击事件
                return if (position == view_pager.adapter!!.count - 1) {
                    val view = LayoutInflater.from(this@GuidePageActivity).inflate(R.layout.guide_5_layout, container, false)
                    val guideButton = view.findViewById<View>(R.id.guide_button) as ImageView
                    guideButton.setOnClickListener { jumpToIndexActivity() }
                    container.addView(view)
                    view
                } else {
                    container.addView(mImageViews[position])
                    mImageViews[position]
                }

            }

            override fun destroyItem(container: ViewGroup, position: Int,
                                     `object`: Any) {
                container.removeView(mImageViews[position])
            }

            override fun isViewFromObject(view: View, `object`: Any): Boolean {
                return view === `object`
            }

            override fun getCount(): Int {
                return mImgIds.size
            }
        }

        view_pager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
                if (position == mImgIds.size - 1 && positionOffset == 0f && positionOffsetPixels == 0 && isScrolling) {
                    isScrolling = false
                    jumpToIndexActivity()
                }
            }

            override fun onPageSelected(position: Int) {

            }

            override fun onPageScrollStateChanged(state: Int) {
                isScrolling = state == 1
            }
        })
    }
}
