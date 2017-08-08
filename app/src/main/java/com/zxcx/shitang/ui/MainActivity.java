package com.zxcx.shitang.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.RadioButton;

import com.zxcx.shitang.R;
import com.zxcx.shitang.ui.card.card.cardDetails.CardDetailsActivity;
import com.zxcx.shitang.ui.card.cardBag.CardBagActivity;
import com.zxcx.shitang.ui.classify.ClassifyFragment;
import com.zxcx.shitang.ui.home.HomeFragment;
import com.zxcx.shitang.ui.my.MyFragment;
import com.zxcx.shitang.ui.welcome.WebViewActivity;
import com.zxcx.shitang.utils.Constants;

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

        Intent intent = getIntent();
        //判断是否点击了广告或通知
        gotoADActivity(intent);
        gotoNotificationActivity(intent);
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
                switchFragment(MyFragment.newInstance());
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
