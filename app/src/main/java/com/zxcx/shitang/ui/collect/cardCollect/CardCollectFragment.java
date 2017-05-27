package com.zxcx.shitang.ui.collect.cardCollect;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.zxcx.shitang.R;
import com.zxcx.shitang.mvpBase.MvpFragment;

public class CardCollectFragment extends MvpFragment<CardCollectPresenter> implements CardCollectContract.View {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_card_collect, container, false);
        return root;
    }

    @Override
    protected CardCollectPresenter createPresenter() {
        return new CardCollectPresenter(this);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void getDataSuccess(CardCollectBean bean) {

    }
}