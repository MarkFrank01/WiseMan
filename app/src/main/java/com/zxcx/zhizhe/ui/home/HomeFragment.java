package com.zxcx.zhizhe.ui.home;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.zxcx.zhizhe.R;
import com.zxcx.zhizhe.event.GotoHomeRankEvent;
import com.zxcx.zhizhe.mvpBase.BaseFragment;
import com.zxcx.zhizhe.mvpBase.BaseRxJava;
import com.zxcx.zhizhe.mvpBase.IGetPresenter;
import com.zxcx.zhizhe.retrofit.AppClient;
import com.zxcx.zhizhe.retrofit.BaseSubscriber;
import com.zxcx.zhizhe.ui.home.attention.AttentionFragment;
import com.zxcx.zhizhe.ui.home.hot.HotFragment;
import com.zxcx.zhizhe.ui.home.rank.RankFragment;
import com.zxcx.zhizhe.ui.search.search.SearchActivity;
import com.zxcx.zhizhe.ui.search.search.SearchBean;
import com.zxcx.zhizhe.utils.ScreenUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class HomeFragment extends BaseFragment implements IGetPresenter<SearchBean> {

    @BindView(R.id.tl_home)
    TabLayout mTlHome;
    Unbinder unbinder;
    @BindView(R.id.app_bar_layout)
    AppBarLayout mAppBarLayout;
    @BindView(R.id.fl_home)
    FrameLayout mFlHome;
    @BindView(R.id.tv_home_search)
    TextView mTvHomeSearch;
    private HotFragment mHotFragment = new HotFragment();
    private AttentionFragment mAttentionFragment = new AttentionFragment();
    private RankFragment mRankFragment = new RankFragment();
    private Fragment mCurrentFragment = new Fragment();

    private String[] titles = new String[]{"推荐", "关注", "榜单"};

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
        EventBus.getDefault().register(this);
        for (int i = 0; i < titles.length; i++) {
            TabLayout.Tab tab = mTlHome.newTab();
            tab.setCustomView(R.layout.tab_home);
            TextView textView = tab.getCustomView().findViewById(R.id.tv_tab_home);
            textView.setText(titles[i]);
            mTlHome.addTab(tab);
//            tab.setText(titles[i]);
            getSearchHot();
        }
        mTlHome.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                switch (tab.getPosition()) {
                    case 0:
                        switchFragment(mHotFragment);
                        break;
                    case 1:
                        switchFragment(mAttentionFragment);
                        break;
                    case 2:
                        switchFragment(mRankFragment);
                        break;
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        ViewGroup.LayoutParams para = mTlHome.getLayoutParams();
        int screenWidth = ScreenUtils.getScreenWidth(); //屏幕宽度
        para.width = screenWidth * 2 / 3;
        mTlHome.setLayoutParams(para);

        initAppBarLayout();
        mTlHome.getTabAt(0).select();
        switchFragment(mHotFragment);
    }

    private void switchFragment(Fragment newFragment) {

        FragmentManager fm = getChildFragmentManager();
        FragmentTransaction transaction = fm.beginTransaction();

        if (newFragment.isAdded()) {
            //.setCustomAnimations(R.anim.fragment_anim_left_in,R.anim.fragment_anim_right_out)
            transaction.hide(mCurrentFragment).show(newFragment).commitAllowingStateLoss();
        } else {
            transaction.hide(mCurrentFragment).add(R.id.fl_home, newFragment).commitAllowingStateLoss();
        }
        mCurrentFragment = newFragment;
    }

    private void initAppBarLayout() {
        mAppBarLayout.addOnOffsetChangedListener((appBarLayout, i) -> {
            mHotFragment.setAppBarLayoutVerticalOffset(i);
            mAttentionFragment.setAppBarLayoutVerticalOffset(i);
            mRankFragment.setMAppBarLayoutVerticalOffset(i);
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        EventBus.getDefault().unregister(this);
        unbinder.unbind();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(GotoHomeRankEvent event) {
        mTlHome.getTabAt(2).select();
    }

    @OnClick(R.id.tv_home_search)
    public void onViewClicked() {
        Intent intent = new Intent(getContext(), SearchActivity.class);
        startActivity(intent);
    }

    public void getSearchHot() {
        mDisposable = AppClient.getAPIService().getSearchHot()
                .compose(BaseRxJava.INSTANCE.handleArrayResult())
                .compose(BaseRxJava.INSTANCE.io_main())
                .subscribeWith(new BaseSubscriber<List<SearchBean>>(this) {
                    @Override
                    public void onNext(List<SearchBean> list) {
                        if (list != null && list.size() > 0) {
                            getDataSuccess(list.get(0));
                        }
                    }
                });
        addSubscription(mDisposable);
    }

    @Override
    public void getDataSuccess(SearchBean bean) {
        mTvHomeSearch.setText(bean.getConent());
    }

    @Override
    public void getDataFail(String msg) {

    }
}
