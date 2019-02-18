package com.zxcx.zhizhe.widget

import android.animation.ArgbEvaluator
import android.content.Context
import android.graphics.Typeface
import android.support.design.widget.TabLayout
import android.support.v4.view.ViewPager
import android.util.AttributeSet
import android.util.TypedValue
import android.widget.TextView
import com.zxcx.zhizhe.R
import com.zxcx.zhizhe.utils.SVTSConstants
import com.zxcx.zhizhe.utils.ScreenUtils
import com.zxcx.zhizhe.utils.SharedPreferencesUtil
import com.zxcx.zhizhe.utils.getColorForKotlin
import java.lang.ref.WeakReference

/**
 * 卡片，长文列表TabLayout
 */

class CardListTabLayout : TabLayout {

    //默认字体大小
    private val DEFAULT_NORMAL_TEXT_SIZE_SP = ScreenUtils.dip2px(15f)
    private var mNormalTextSize = DEFAULT_NORMAL_TEXT_SIZE_SP
    //选中字体大小
    private val DEFAULT_SELECT_TEXT_SIZE_SP = ScreenUtils.dip2px(22f)
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
        //addOnTabSelectedListener(TabSelectListener(this))
        //整体前后加边距
//		val child = getChildAt(0)
//		val lp = child.layoutParams as FrameLayout.LayoutParams
//		lp.setMargins(ScreenUtils.dip2px(10f), 0, ScreenUtils.dip2px(10f), 0)
//		child.layoutParams = lp
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
        }
    }

    override fun setupWithViewPager(viewPager: ViewPager?) {
        super.setupWithViewPager(viewPager)
        viewPager?.addOnPageChangeListener(CardListTabLayoutOnPageChangeListener(this))
    }

    class CardListTabLayoutOnPageChangeListener(tabLayout: CardListTabLayout) : ViewPager.OnPageChangeListener {
        private val mTabLayoutRef: WeakReference<CardListTabLayout> = WeakReference(tabLayout)
        private var mNormalTextSize = mTabLayoutRef.get()?.mNormalTextSize
                ?: ScreenUtils.dip2px(15f)
        private var mSelectTextSize = mTabLayoutRef.get()?.mSelectTextSize
                ?: ScreenUtils.dip2px(22f)
        private var mNormalTextColor = mTabLayoutRef.get()?.mNormalTextColor
        private var mSelectTextColor = mTabLayoutRef.get()?.mSelectTextColor

        override fun onPageScrolled(position: Int, positionOffset: Float,
                                    positionOffsetPixels: Int) {
            val tabLayout = mTabLayoutRef.get()
            val selectedChild = tabLayout?.getTabAt(position)?.customView?.findViewById<TextView>(R.id.tv_tab_card_list)
            val nextChild = if (position + 1 < tabLayout?.tabCount ?: 0)
                tabLayout?.getTabAt(position + 1)?.customView?.findViewById<TextView>(R.id.tv_tab_card_list)
            else
                null

            if (position == 0) {
                if (selectedChild != null) {
                    tabLayout.selectedPosition = position
                    selectedChild.setTextSize(TypedValue.COMPLEX_UNIT_PX, mSelectTextSize - (mSelectTextSize - mNormalTextSize) * positionOffset)
                    //初始颜色值
                    val bgColor = when {
                        positionOffset == 0f -> //显示选中颜色
                            mSelectTextColor
                        positionOffset > 1 -> //显示普通颜色
                            mNormalTextColor
                        else -> //滚动过程中渐变的颜色
                            tabLayout.argbEvaluator.evaluate(positionOffset, mSelectTextColor, mNormalTextColor) as Int?
                    }
                    bgColor?.let { selectedChild.setTextColor(bgColor) }

                    if (positionOffset > 0.5) {
                        selectedChild.typeface = Typeface.DEFAULT
                    } else {
                        selectedChild.typeface = Typeface.DEFAULT_BOLD
                    }
                }
            }
//
//			if (nextChild != null) {
//				tabLayout?.nextPosition = position + 1
//				nextChild.setTextSize(TypedValue.COMPLEX_UNIT_PX, mNormalTextSize + (mSelectTextSize - mNormalTextSize) * positionOffset)
//				//初始颜色值
//				val bgColor = when {
//					positionOffset == 0f -> //显示初始透明颜色
//						mNormalTextColor
//					positionOffset > 1 -> //滚动到一个定值后,颜色最深,而且不再加深
//						mSelectTextColor
//					else -> //滚动过程中渐变的颜色
//						tabLayout?.argbEvaluator?.evaluate(positionOffset, mNormalTextColor, mSelectTextColor) as Int
//				}
//				bgColor?.let { nextChild.setTextColor(bgColor) }
//				if (Math.abs(positionOffset) > 0.5) {
//					nextChild.typeface = Typeface.DEFAULT_BOLD
//				} else {
//					nextChild.typeface = Typeface.DEFAULT
//				}
//			}
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
                }
                val selectedChild = tabLayout.getTabAt(position)?.customView?.findViewById<TextView>(R.id.tv_tab_card_list)
                selectedChild?.setTextSize(TypedValue.COMPLEX_UNIT_PX, mSelectTextSize.toFloat())
                selectedChild?.typeface = Typeface.DEFAULT_BOLD
                mSelectTextColor?.let { selectedChild?.setTextColor(it) }

                if (position == 0) {
                    SharedPreferencesUtil.saveData(SVTSConstants.adTypePositionLong, 0)
                } else {
                    SharedPreferencesUtil.saveData(SVTSConstants.adTypePositionLong, position)
                }
            }
        }
    }

    class TabSelectListener(tabLayout: CardListTabLayout) : TabLayout.OnTabSelectedListener {
        private val mTabLayoutRef: WeakReference<CardListTabLayout> = WeakReference(tabLayout)
        private var mNormalTextSize = mTabLayoutRef.get()?.mNormalTextSize
                ?: ScreenUtils.dip2px(15f)
        private var mSelectTextSize = mTabLayoutRef.get()?.mSelectTextSize
                ?: ScreenUtils.dip2px(22f)
        private var mNormalTextColor = mTabLayoutRef.get()?.mNormalTextColor
        private var mSelectTextColor = mTabLayoutRef.get()?.mSelectTextColor

        override fun onTabReselected(tab: Tab?) {

        }

        override fun onTabUnselected(tab: Tab?) {
        }

        override fun onTabSelected(tab: Tab?) {
            val selectedPosition = tab?.position ?: 0
            val selectedChild = tab?.customView?.findViewById<TextView>(R.id.tv_tab_card_list)
            selectedChild?.setTextSize(TypedValue.COMPLEX_UNIT_PX, mSelectTextSize.toFloat())
            selectedChild?.typeface = Typeface.DEFAULT_BOLD
            mSelectTextColor?.let { selectedChild?.setTextColor(it) }

            val tabLayout = mTabLayoutRef.get() ?: return
            if (tabLayout.selectedPosition != selectedPosition) {
                val notSelectedChild = tabLayout.getTabAt(tabLayout.selectedPosition)?.customView?.findViewById<TextView>(R.id.tv_tab_card_list)
                notSelectedChild?.post {
                    notSelectedChild.setTextSize(TypedValue.COMPLEX_UNIT_PX, mNormalTextSize.toFloat())
                    notSelectedChild.typeface = Typeface.DEFAULT
                    mNormalTextColor?.let { notSelectedChild.setTextColor(it) }
                }
            }
            if (tabLayout.nextPosition != selectedPosition) {
                val notSelectedChild = tabLayout.getTabAt(tabLayout.nextPosition)?.customView?.findViewById<TextView>(R.id.tv_tab_card_list)
                notSelectedChild?.post {
                    notSelectedChild.setTextSize(TypedValue.COMPLEX_UNIT_PX, mNormalTextSize.toFloat())
                    notSelectedChild.typeface = Typeface.DEFAULT
                    mNormalTextColor?.let { notSelectedChild.setTextColor(it) }
                }
            }
        }

    }

}
