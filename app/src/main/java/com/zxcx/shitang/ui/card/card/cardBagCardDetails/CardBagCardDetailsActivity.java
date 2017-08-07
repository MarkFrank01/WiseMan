package com.zxcx.shitang.ui.card.card.cardBagCardDetails;

import android.os.Bundle;
import android.support.v4.view.ViewPager;

import com.zxcx.shitang.R;
import com.zxcx.shitang.mvpBase.BaseActivity;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CardBagCardDetailsActivity extends BaseActivity {

    @BindView(R.id.vp_card_bag_card_details)
    ViewPager mVpCardBagCardDetails;

    private CardBagCardDetailsAdapter mAdapter;
    private List<String> mList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_card_bag_card_details);
        ButterKnife.bind(this);

        for (int i = 0; i < 20; i++) {
            mList.add("666");
        }

        mAdapter = new CardBagCardDetailsAdapter(getSupportFragmentManager(),20,mList);
        mVpCardBagCardDetails.setAdapter(mAdapter);
    }
}
