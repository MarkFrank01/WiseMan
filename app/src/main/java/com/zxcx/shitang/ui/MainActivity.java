package com.zxcx.shitang.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.RadioButton;

import com.zxcx.shitang.R;
import com.zxcx.shitang.event.ChangeNightModeEvent;
import com.zxcx.shitang.event.ClassifyClickRefreshEvent;
import com.zxcx.shitang.event.HomeClickRefreshEvent;
import com.zxcx.shitang.mvpBase.BaseActivity;
import com.zxcx.shitang.ui.card.card.cardDetails.CardDetailsActivity;
import com.zxcx.shitang.ui.card.cardBag.CardBagActivity;
import com.zxcx.shitang.ui.classify.ClassifyFragment;
import com.zxcx.shitang.ui.home.HomeFragment;
import com.zxcx.shitang.ui.my.MyFragment;
import com.zxcx.shitang.ui.welcome.WebViewActivity;
import com.zxcx.shitang.utils.Constants;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends BaseActivity {

    @BindView(R.id.home_fragment_content)
    FrameLayout mHomeFragmentContent;
    @BindView(R.id.home_tab_home)
    RadioButton mHomeTabHome;
    @BindView(R.id.home_tab_classify)
    RadioButton mHomeTabAll;
    @BindView(R.id.home_tab_my)
    RadioButton mHomeTabMy;

    private Fragment mCurrentFragment = new Fragment();
    private HomeFragment mHomeFragment = new HomeFragment();
    private ClassifyFragment mClassifyFragment = new ClassifyFragment();
    private MyFragment mMyFragment = new MyFragment();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        EventBus.getDefault().register(this);

        mHomeTabHome.performClick();

        Intent intent = getIntent();
        //判断是否点击了广告或通知
        gotoADActivity(intent);
        gotoNotificationActivity(intent);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @OnClick({R.id.home_tab_home, R.id.home_tab_classify, R.id.home_tab_my})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.home_tab_home:
                switchFragment(mHomeFragment);
                break;
            case R.id.home_tab_classify:
                switchFragment(mClassifyFragment);
                break;
            case R.id.home_tab_my:
                switchFragment(mMyFragment);
                break;
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(ChangeNightModeEvent event) {
        this.recreate();
    }

    private void switchFragment(Fragment newFragment) {

        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction transaction = fm.beginTransaction();

        if(mCurrentFragment == newFragment){
            if (newFragment == mHomeFragment){
                EventBus.getDefault().post(new HomeClickRefreshEvent());
            }else if (newFragment == mClassifyFragment){
                EventBus.getDefault().post(new ClassifyClickRefreshEvent());
            }
        }else {
            if (newFragment.isAdded()) {
                //.setCustomAnimations(R.anim.fragment_anim_left_in,R.anim.fragment_anim_right_out)
                transaction.hide(mCurrentFragment).show(newFragment).commitAllowingStateLoss();
            } else {
                transaction.hide(mCurrentFragment).add(R.id.home_fragment_content, newFragment).commitAllowingStateLoss();
            }
            mCurrentFragment = newFragment;
        }
    }

    private void gotoADActivity(Intent intent) {
        if (intent.getBooleanExtra("hasAd",false)){
            Intent intent1 = new Intent(this, WebViewActivity.class);
            intent1.putExtra("title",intent.getStringExtra("title"));
            intent1.putExtra("url",intent.getStringExtra("url"));
            startActivity(intent1);
        }
    }

    private void gotoNotificationActivity(Intent intent) {
        Bundle bundle = intent.getBundleExtra("push");
        if (bundle != null){
            String type = bundle.getString("type");
            String id = bundle.getString("id");
            String url = bundle.getString("url");
            Intent detailIntent = new Intent();

            switch (type){
                case Constants.PUSH_TYPE_CARD_BAG:
                    detailIntent.setClass(this, CardBagActivity.class);
                    break;
                case Constants.PUSH_TYPE_CARD:
                    detailIntent.setClass(this, CardDetailsActivity.class);
                    break;
                case Constants.PUSH_TYPE_AD:
                    detailIntent.setClass(this, WebViewActivity.class);
                    break;
            }
            detailIntent.putExtra("id", id);
            detailIntent.putExtra("url", url);
        }
    }
}
