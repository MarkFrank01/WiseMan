package com.zxcx.shitang.ui.collect.cardBagCollect;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.zxcx.shitang.R;
import com.zxcx.shitang.mvpBase.MvpFragment;

public class CardBagCollectFragment extends MvpFragment<CardBagCollectPresenter> implements CardBagCollectContract.View {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_card_bag_collect, container, false);
        return root;
    }

    @Override
    protected CardBagCollectPresenter createPresenter() {
        return new CardBagCollectPresenter(this);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void getDataSuccess(CardBagCollectBean bean) {

    }
}