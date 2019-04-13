package com.zxcx.zhizhe.widget

import android.animation.ArgbEvaluator
import android.content.Context
import android.graphics.Typeface
import android.support.design.widget.TabLayout
import android.support.v4.view.ViewPager
import android.util.AttributeSet
import android.util.TypedValue
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.zxcx.zhizhe.R
import com.zxcx.zhizhe.utils.ScreenUtils
import com.zxcx.zhizhe.utils.getColorForKotlin
import java.lang.ref.WeakReference

/**
 * 卡片，长文列表TabLayout
 */

class NewTabLayout : TabLayout {

    //默认字体大小
    private val DEFAULT_NORMAL_TEXT_SIZE_SP = ScreenUtils.dip2px(15f)
    private var mNormalTextSize = DEFAULT_NORMAL_TEXT_SIZE_SP
    //选中字体大小
    private val DEFAULT_SELECT_TEXT_SIZE_SP = ScreenUtils.dip2px(15f)
    private var mSelectTextSize = DEFAULT_SELECT_TEXT_SIZE_SP
    //字体颜色
    private val DEFAULT_NORMAL_TEXT_COLOR = context.getColorForKotlin(R.color.text_color_3)
    private var mNormalTextColor = DEFAULT_NORMAL_TEXT_COLOR
    private val DEFAULT_SELECT_TEXT_COLOR = context.getColorForKotlin(R.color.text_color_1)
    private var mSelectTextColor = DEFAULT_SELECT_TEXT_COLOR
    //利用估值器实现渐变
    private val argbEvaluator = ArgbEvaluator()

    var selectedPosition = 0
    var nextPosition = 0

    constructor(context: Context) : this(context, null) {}

    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0) {}

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {

        val a = context.obtainStyledAttributes(attrs, R.styleable.CardListTabLayout)
        mNormalTextSize = a.getDimensionPixelSize(R.styleable.CardListTabLayout_minTextSize, DEFAULT_NORMAL_TEXT_SIZE_SP)
        mSelectTextSize = a.getDimensionPixelSize(R.styleable.CardListTabLayout_maxTextSize, DEFAULT_SELECT_TEXT_SIZE_SP)
        mNormalTextColor = a.getColor(R.styleable.CardListTabLayout_minTextColor, DEFAULT_NORMAL_TEXT_COLOR)
        mSelectTextColor = a.getDimensionPixelSize(R.styleable.CardListTabLayout_maxTextColor, DEFAULT_SELECT_TEXT_COLOR)
        a.recycle()
    }

    override fun addTab(tab: Tab) {
        super.addTab(tab)
        if (tabCount == 1) {
            val textView = tab.customView?.findViewById<TextView>(R.id.tv_tab_card_list)
            textView?.setTextSize(TypedValue.COMPLEX_UNIT_PX, mSelectTextSize.toFloat())
//			val customViewParent = tab.customView?.parent as ViewGroup
//			val lp = customViewParent.layoutParams as ViewGroup.LayoutParams
//			lp.width = tab.customView?.measuredWidth ?: ViewGroup.LayoutParams.WRAP_CONTENT
//			customViewParent.layoutParams = lp

            val img = tab.customView?.findViewById<ImageView>(R.id.show_select)
            img?.visibility = View.VISIBLE
        }
    }


    override fun setupWithViewPager(viewPager: ViewPager?) {
        super.setupWithViewPager(viewPager)
        viewPager?.addOnPageChangeListener(CardListTabLayoutOnPageChangeListener(this))
    }

    class CardListTabLayoutOnPageChangeListener(tabLayout: NewTabLayout) : ViewPager.OnPageChangeListener {
        private val mTabLayoutRef: WeakReference<NewTabLayout> = WeakReference(tabLayout)
        private var mNormalTextSize = mTabLayoutRef.get()?.mNormalTextSize
                ?: ScreenUtils.dip2px(15f)
        private var mSelectTextSize = mTabLayoutRef.get()?.mSelectTextSize
                ?: ScreenUtils.dip2px(22f)
        private var mNormalTextColor = mTabLayoutRef.get()?.mNormalTextColor
        private var mSelectTextColor = mTabLayoutRef.get()?.mSelectTextColor

        override fun onPageScrolled(position: Int, positionOffset: Float,
                                    positionOffsetPixels: Int) {
        }

        override fun onPageScrollStateChanged(state: Int) {
        }

        override fun onPageSelected(position: Int) {
            val tabLayout = mTabLayoutRef.get()
            if (tabLayout != null) {
                for (index in 0 until tabLayout.tabCount) {
                    val notSelectedChild = tabLayout.getTabAt(index)?.customView?.findViewById<TextView>(R.id.tv_tab_card_list)
                    notSelectedChild?.setTextSize(TypedValue.COMPLEX_UNIT_PX, mNormalTextSize.toFloat())
                    notSelectedChild?.typeface = Typeface.DEFAULT
                    mNormalTextColor?.let { notSelectedChild?.setTextColor(it) }

                    var imageView = tabLayout.getTabAt(index)?.customView?.findViewById<ImageView>(R.id.show_select)
                    imageView?.visibility = View.INVISIBLE
                }
                val selectedChild = tabLayout.getTabAt(position)?.customView?.findViewById<TextView>(R.id.tv_tab_card_list)
                selectedChild?.setTextSize(TypedValue.COMPLEX_UNIT_PX, mSelectTextSize.toFloat())
                selectedChild?.typeface = Typeface.DEFAULT_BOLD
                mSelectTextColor?.let { selectedChild?.setTextColor(it) }

                var imageViewSelect = tabLayout.getTabAt(position)?.customView?.findViewById<ImageView>(R.id.show_select)
                imageViewSelect?.visibility = View.VISIBLE

            }
        }
    }


}
