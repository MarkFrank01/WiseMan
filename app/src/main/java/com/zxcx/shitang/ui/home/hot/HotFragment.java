package com.zxcx.shitang.ui.home.hot;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.zxcx.shitang.R;
import com.zxcx.shitang.mvpBase.MvpFragment;

public class HotFragment extends MvpFragment<HotPresenter> implements HotContract.View {

    private static HotFragment fragment = null;

    public static HotFragment newInstance() {
        if (fragment == null) {
            fragment = new HotFragment();
        }
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_hot, container, false);
        return root;
    }

    @Override
    protected HotPresenter createPresenter() {
        return new HotPresenter(this);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void getDataSuccess(HotBean bean) {

    }
}