package com.zxcx.zhizhe.ui.welcome

import android.content.Intent
import android.os.Bundle
import android.support.v4.view.PagerAdapter
import android.support.v4.view.ViewPager
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import butterknife.ButterKnife
import com.gyf.barlibrary.ImmersionBar
import com.zxcx.zhizhe.R
import com.zxcx.zhizhe.mvpBase.BaseActivity
import com.zxcx.zhizhe.ui.MainActivity
import com.zxcx.zhizhe.utils.Utils
import kotlinx.android.synthetic.main.activity_guide_page.*
import java.util.*

/**
 * 启动引导页
 */

class GuidePageActivity : BaseActivity() {

    /**
     * 3张引导页面的图片
     */
    private val mImgIds = intArrayOf(R.drawable.guide2, R.drawable.guide3, R.drawable.guide4, R.drawable.guide1)
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
        mImmersionBar = ImmersionBar.with(this)
        mImmersionBar.keyboardEnable(true)
        mImmersionBar.init()
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
    private fun jumpToIndexActivity(needLogin: Boolean) {

        val intent = Intent(this, MainActivity::class.java)
        intent.putExtra("needLogin", needLogin)
        startActivity(intent)

        Utils.setIsFirstLaunchApp(false)

        finish()
    }


    private fun initViewPager() {


        view_pager.adapter = object : PagerAdapter() {

            override fun instantiateItem(container: ViewGroup, position: Int): Any {
                container.addView(mImageViews[position])
                return mImageViews[position]
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
                    jumpToIndexActivity(false)
                }

                when (position) {
                    0 -> {
                        iv1.setImageResource(R.drawable.bg_backbutton)
                        iv2.setImageResource(R.drawable.bg_backbutton4)
                        iv3.setImageResource(R.drawable.bg_backbutton4)
                        iv4.setImageResource(R.drawable.bg_backbutton4)
                    }

                    1 -> {
                        iv1.setImageResource(R.drawable.bg_backbutton4)
                        iv2.setImageResource(R.drawable.bg_backbutton)
                        iv3.setImageResource(R.drawable.bg_backbutton4)
                        iv4.setImageResource(R.drawable.bg_backbutton4)
                    }

                    2 -> {
                        iv1.setImageResource(R.drawable.bg_backbutton4)
                        iv2.setImageResource(R.drawable.bg_backbutton4)
                        iv3.setImageResource(R.drawable.bg_backbutton)
                        iv4.setImageResource(R.drawable.bg_backbutton4)
                    }

                    3 -> {
                        iv1.setImageResource(R.drawable.bg_backbutton4)
                        iv2.setImageResource(R.drawable.bg_backbutton4)
                        iv3.setImageResource(R.drawable.bg_backbutton4)
                        iv4.setImageResource(R.drawable.bg_backbutton)
                    }
                }
            }

            override fun onPageSelected(position: Int) {

            }

            override fun onPageScrollStateChanged(state: Int) {
                isScrolling = state == 1
            }
        })
    }

    override fun setListener() {
        super.setListener()
        tv_skip.setOnClickListener {
            jumpToIndexActivity(true)
        }
    }
}
