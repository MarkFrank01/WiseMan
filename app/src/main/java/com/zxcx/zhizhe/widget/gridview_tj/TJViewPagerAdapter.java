package com.zxcx.zhizhe.widget.gridview_tj;

import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

/**
 * @author : MarkFrank01
 * @Created on 2019/3/8
 * @Description :
 */
public class TJViewPagerAdapter<T extends View> extends PagerAdapter{

    private List<T> mViewList;

    public TJViewPagerAdapter(List<T> viewList) {
        mViewList = viewList;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView(mViewList.get(position));
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        container.addView(mViewList.get(position));
        return (mViewList.get(position));
    }

    @Override
    public int getCount() {
        if (mViewList == null)
            return 0;
        return mViewList.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }
}
