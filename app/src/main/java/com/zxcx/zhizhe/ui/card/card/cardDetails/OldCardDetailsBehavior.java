package com.zxcx.zhizhe.ui.card.card.cardDetails;

import android.content.Context;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.view.ViewCompat;
import android.support.v4.widget.NestedScrollView;
import android.util.AttributeSet;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.Scroller;

import com.zxcx.zhizhe.R;

/**
 * Created by anm on 2017/7/26.
 */

public class OldCardDetailsBehavior extends CoordinatorLayout.Behavior<RelativeLayout> {

    private Context mContext;

    public OldCardDetailsBehavior(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
    }

    @Override
    public boolean layoutDependsOn(CoordinatorLayout parent, RelativeLayout child, View dependency) {
        if (dependency instanceof NestedScrollView){
            return true;
        }else {
            return false;
        }
    }

    @Override
    public boolean onStartNestedScroll(CoordinatorLayout coordinatorLayout, RelativeLayout child, View directTargetChild, View target, int nestedScrollAxes) {
        if ((nestedScrollAxes & ViewCompat.SCROLL_AXIS_VERTICAL) != 0){
            return true;
        }else {
            return false;
        }
    }

    @Override
    public boolean onNestedFling(CoordinatorLayout coordinatorLayout, RelativeLayout child, View target, float velocityX, float velocityY, boolean consumed) {
        Scroller scroller = new Scroller(mContext);
        scroller.fling(0,0,0,(int) velocityY,0,0,-50000,50000);
        int finalY = scroller.getFinalY();
        int topHeight = target.findViewById(R.id.tv_card_details_name).getHeight()
                +target.findViewById(R.id.tv_card_details_name).getTop();
        int scrollY = target.getScrollY();
        if (scrollY + finalY>topHeight){
            child.findViewById(R.id.toolbar).findViewById(R.id.tv_card_details_title).setVisibility(View.VISIBLE);
        }else {
            child.findViewById(R.id.toolbar).findViewById(R.id.tv_card_details_title).setVisibility(View.GONE);
        }
        return super.onNestedFling(coordinatorLayout,child,target,velocityX,velocityY,consumed);
    }

    @Override
    public void onNestedScroll(CoordinatorLayout coordinatorLayout, RelativeLayout child, View target, int dxConsumed, int dyConsumed, int dxUnconsumed, int dyUnconsumed) {
        //CardDetailsName到手机顶部的距离
        int topHeight = target.findViewById(R.id.tv_card_details_name).getHeight()
                +target.findViewById(R.id.tv_card_details_name).getTop();
        if (target.getScrollY()+dyConsumed>topHeight){
            child.findViewById(R.id.toolbar).findViewById(R.id.tv_card_details_title).setVisibility(View.VISIBLE);
        }else {
            child.findViewById(R.id.toolbar).findViewById(R.id.tv_card_details_title).setVisibility(View.GONE);
        }
    }
}
