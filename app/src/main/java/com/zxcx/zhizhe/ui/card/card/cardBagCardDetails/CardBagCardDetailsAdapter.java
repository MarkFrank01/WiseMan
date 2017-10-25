package com.zxcx.zhizhe.ui.card.card.cardBagCardDetails;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import java.util.List;

/**
 * Created by anm on 2017/7/14.
 */

public class CardBagCardDetailsAdapter extends FragmentStatePagerAdapter {

    private int mCount;
    private List<Integer> mList;

    public CardBagCardDetailsAdapter(FragmentManager fm,  List<Integer> list) {
        super(fm);
        mCount = list.size();
        mList = list;
    }

    @Override
    public Fragment getItem(int position) {
        return CardBagCardDetailsFragment.newInstance(mList.get(position));
    }

    @Override
    public int getCount() {
        return mCount;
    }
}
