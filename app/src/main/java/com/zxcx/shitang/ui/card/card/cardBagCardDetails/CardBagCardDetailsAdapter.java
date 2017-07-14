package com.zxcx.shitang.ui.card.card.cardBagCardDetails;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import java.util.List;

/**
 * Created by anm on 2017/7/14.
 */

public class CardBagCardDetailsAdapter extends FragmentStatePagerAdapter {

    private int mCount;
    private List<String> mList;

    public CardBagCardDetailsAdapter(FragmentManager fm, int count, List<String> list) {
        super(fm);
        mCount = count;
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
