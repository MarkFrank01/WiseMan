package com.zxcx.shitang.ui.my;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.zxcx.shitang.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MyFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MyFragment extends Fragment {

    private static MyFragment fragment = null;
    Unbinder unbinder;
    @BindView(R.id.tv_my_login)
    TextView mTvMyLogin;
    @BindView(R.id.tv_my_nick_name)
    TextView mTvMyNickName;
    @BindView(R.id.tv_my_collect)
    TextView mTvMyCollect;

    public static MyFragment newInstance(String param1, String param2) {
        if (fragment == null) {
            fragment = new MyFragment();
        }
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_my, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick(R.id.iv_my_head)
    public void onMIvMyHeadClicked() {
    }

    @OnClick(R.id.ll_my_collect)
    public void onMLlMyCollectClicked() {
    }

    @OnClick(R.id.ll_my_common_setting)
    public void onMLlMyCommonSettingClicked() {
    }

    @OnClick(R.id.ll_my_feedback)
    public void onMLlMyFeedbackClicked() {
    }

    @OnClick(R.id.ll_my_about_us)
    public void onMLlMyAboutUsClicked() {
    }

    @OnClick(R.id.ll_my_share)
    public void onMLlMyShareClicked() {
    }
}
