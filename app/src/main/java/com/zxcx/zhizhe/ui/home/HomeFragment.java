package com.zxcx.zhizhe.ui.home;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.TextPaint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.zxcx.zhizhe.R;
import com.zxcx.zhizhe.event.HomeClickRefreshEvent;
import com.zxcx.zhizhe.event.HomeTopClickRefreshEvent;
import com.zxcx.zhizhe.mvpBase.BaseFragment;
import com.zxcx.zhizhe.ui.home.attention.AttentionFragment;
import com.zxcx.zhizhe.ui.home.hot.HotFragment;
import com.zxcx.zhizhe.ui.search.search.SearchActivity;
import com.zxcx.zhizhe.utils.ScreenUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

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
    @BindView(R.id.app_bar_layout)
    AppBarLayout mAppBarLayout;
    private HotFragment mHotFragment = new HotFragment();
    private AttentionFragment mAttentionFragment = new AttentionFragment();

    private String[] titles = new String[]{"推荐", "关注"};

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        unbinder = ButterKnife.bind(this, view);
        EventBus.getDefault().register(this);
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
            TextView textView = (TextView) tab.getCustomView().findViewById(R.id.tv_tab_home);
            textView.setText(titles[i]);
            TextPaint paint = textView.getPaint();
            paint.setFakeBoldText(true);
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
                EventBus.getDefault().post(new HomeTopClickRefreshEvent());
            }
        });

        ViewGroup.LayoutParams para = mTlHome.getLayoutParams();
        int screenWidth = ScreenUtils.getScreenWidth(); //屏幕宽度
        para.width = screenWidth * 2 / 3;
        mTlHome.setLayoutParams(para);

        initAppBarLayout();
    }

    private void initAppBarLayout() {
        mAppBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int i) {
                mHotFragment.setAppBarLayoutVerticalOffset(i);
                mAttentionFragment.setAppBarLayoutVerticalOffset(i);
            }
        });
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(HomeClickRefreshEvent event) {
        mVpHome.setCurrentItem(0);
        EventBus.getDefault().post(new HomeTopClickRefreshEvent());
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void onDestroyView() {
        EventBus.getDefault().unregister(this);
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick(R.id.tv_home_search)
    public void onViewClicked() {
        Intent intent = new Intent(getContext(), SearchActivity.class);
        startActivity(intent);
    }
}
