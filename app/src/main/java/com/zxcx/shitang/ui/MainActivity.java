package com.zxcx.shitang.ui;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.RadioButton;

import com.zxcx.shitang.R;
import com.zxcx.shitang.ui.classify.ClassifyFragment;
import com.zxcx.shitang.ui.home.HomeFragment;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.home_fragment_content)
    FrameLayout mHomeFragmentContent;
    @BindView(R.id.home_tab_home)
    RadioButton mHomeTabHome;
    @BindView(R.id.home_tab_classify)
    RadioButton mHomeTabAll;
    @BindView(R.id.home_tab_note)
    RadioButton mHomeTabNote;

    private Fragment mCurrentFragment = new Fragment();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        mHomeTabHome.performClick();
    }

    @OnClick({R.id.home_tab_home, R.id.home_tab_classify, R.id.home_tab_note})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.home_tab_home:
                switchFragment(HomeFragment.newInstance());
                break;
            case R.id.home_tab_classify:
                switchFragment(ClassifyFragment.newInstance());
                break;
            case R.id.home_tab_note:
                break;
        }
    }

    private void switchFragment(Fragment newFragment) {

        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction transaction = fm.beginTransaction();

        if (newFragment.isAdded()) {
            //.setCustomAnimations(R.anim.fragment_anim_left_in,R.anim.fragment_anim_right_out)
            transaction.hide(mCurrentFragment).show(newFragment).commitAllowingStateLoss();
        } else {
            transaction.hide(mCurrentFragment).add(R.id.home_fragment_content, newFragment).commitAllowingStateLoss();
        }
        mCurrentFragment = newFragment;
    }
}
