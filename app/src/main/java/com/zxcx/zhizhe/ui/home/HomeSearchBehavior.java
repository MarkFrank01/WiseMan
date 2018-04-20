package com.zxcx.zhizhe.ui.home;

import android.content.Context;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;

import com.zxcx.zhizhe.utils.ScreenUtils;

/**
 * Created by anm on 2018/3/23.
 */

public class HomeSearchBehavior extends CoordinatorLayout.Behavior<ImageView> {
    private int maxScrollDistance;
    private float maxChildWidth;
    private int appbarStartPoint;

    public HomeSearchBehavior(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean layoutDependsOn(CoordinatorLayout parent, ImageView child, View dependency) {
        return dependency instanceof AppBarLayout;
    }

    private int startX;
    private int startY;

    @Override
    public boolean onDependentViewChanged(CoordinatorLayout parent, ImageView child, View dependency) {
        //这里的dependency就是布局中的AppBarLayout，child即显示的搜索按钮
        if (maxScrollDistance == 0) {
            //也就是第一次进来时，计算出AppBarLayout的最大垂直变化距离
            maxScrollDistance = dependency.getBottom() - ScreenUtils.dip2px(40);
        }
        //计算出appbar的开始的y坐标
        if (appbarStartPoint == 0)
            appbarStartPoint = dependency.getBottom();
        //计算出搜索按钮的宽度
        if (maxChildWidth == 0)
            maxChildWidth = Math.min(child.getWidth(), child.getHeight());
        //计算出搜索按钮的起始x坐标
        if (startX == 0)
            startX = (int) (dependency.getWidth() - maxChildWidth - ScreenUtils.dip2px(24));
        //计算出搜索按钮的起始y坐标
        if (startY == 0)
            startY = (int) (dependency.getBottom() / 2 - maxChildWidth / 2 - ScreenUtils.dip2px(7));

        //计算出appbar已经变化距离的百分比，起始位置y减去当前位置y，然后除以最大距离
        float expandedPercentageFactor = (appbarStartPoint - dependency.getBottom()) * 1.0f /
                (maxScrollDistance * 1.0f);
        //根据上面计算出的百分比，计算出搜索按钮应该移动的y距离,通过百分比乘以最大距离
        float moveY = expandedPercentageFactor * (startY);
        //根据上面计算出的百分比，计算出搜索按钮应该移动的x距离(横向移动的距离是从距右边34dp变化为22dp，差值12dp)
        float moveX = expandedPercentageFactor * ScreenUtils.dip2px(12);
        //更新搜索按钮的位置
        child.setX(startX + moveX);
        child.setY(startY - moveY);
        return true;
    }
}
