package com.zxcx.zhizhe.ui.card.cardBag;

import android.content.Context;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by anm on 2017/12/1.
 */

public class CardBagBehavior extends AppBarLayout.ScrollingViewBehavior {

    public CardBagBehavior() {
    }

    public CardBagBehavior(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean onStartNestedScroll(CoordinatorLayout coordinatorLayout, View child, View directTargetChild, View target, int nestedScrollAxes) {
        if (child instanceof RecyclerView && child.getVisibility() == View.VISIBLE){
            return super.onStartNestedScroll(coordinatorLayout, child, directTargetChild, target, nestedScrollAxes);
        }else {
            return false;
        }
    }

    @Override
    public boolean layoutDependsOn(CoordinatorLayout parent, View child, View dependency) {
        if (child instanceof RecyclerView && child.getVisibility() == View.VISIBLE){
            return true;
        }else {
            return false;
        }
    }
}
