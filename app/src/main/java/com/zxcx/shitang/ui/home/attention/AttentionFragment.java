package com.zxcx.shitang.ui.home.attention;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.zxcx.shitang.R;
import com.zxcx.shitang.mvpBase.MvpFragment;
import com.zxcx.shitang.ui.home.HomeFragment;

public class AttentionFragment extends MvpFragment<AttentionPresenter> implements AttentionContract.View {

    private static AttentionFragment fragment = null;

    public static AttentionFragment newInstance() {
        if (fragment == null) {
            fragment = new AttentionFragment();
        }
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_attention, container, false);
        return root;
    }

    @Override
    protected AttentionPresenter createPresenter() {
        return new AttentionPresenter(this);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void getDataSuccess(AttentionBean bean) {

    }
}