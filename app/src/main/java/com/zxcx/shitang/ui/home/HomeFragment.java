package com.zxcx.shitang.ui.home;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.zxcx.shitang.R;
import com.zxcx.shitang.event.HomeClickRefreshEvent;
import com.zxcx.shitang.mvpBase.BaseFragment;
import com.zxcx.shitang.ui.home.attention.AttentionFragment;
import com.zxcx.shitang.ui.home.hot.HotFragment;
import com.zxcx.shitang.ui.search.search.SearchActivity;

import org.greenrobot.eventbus.EventBus;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class HomeFragment extends BaseFragment {

    @BindView(R.id.tl_home)
    TabLayout mTlHome;
    @BindView(R.id.vp_home)
    ViewPager mVpHome;
    Unbinder unbinder;
    private HotFragment mHotFragment = new HotFragment();
    private AttentionFragment mAttentionFragment = new AttentionFragment();

    private String[] titles = new String[]{"热门推荐", "我关注的"};

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mVpHome.setAdapter(new FragmentPagerAdapter(getChildFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                if (position == 0) {
                    return mHotFragment;
                } else {
                    return mAttentionFragment;
                }
            }

            @Override
            public int getCount() {
                return 2;
            }
        });
        mTlHome.setupWithViewPager(mVpHome);
        for (int i = 0; i < mTlHome.getTabCount(); i++) {
            TabLayout.Tab tab = mTlHome.getTabAt(i);
            tab.setCustomView(R.layout.tab_home);
            ((TextView) tab.getCustomView().findViewById(R.id.tv_tab_home)).setText(titles[i]);
//            tab.setText(titles[i]);
        }
        mTlHome.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                EventBus.getDefault().post(new HomeClickRefreshEvent());
            }
        });
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick(R.id.tv_home_search)
    public void onViewClicked() {
        Intent intent = new Intent(getContext(), SearchActivity.class);
        startActivity(intent);
    }
}
