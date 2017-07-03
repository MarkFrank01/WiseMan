package com.zxcx.shitang.ui.search.search;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.flexbox.FlexLine;
import com.google.android.flexbox.FlexboxLayout;
import com.zxcx.shitang.R;
import com.zxcx.shitang.mvpBase.MvpActivity;
import com.zxcx.shitang.ui.search.result.SearchResultActivity;
import com.zxcx.shitang.utils.ScreenUtils;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SearchActivity extends MvpActivity<SearchPresenter> implements SearchContract.View ,View.OnClickListener{


    @BindView(R.id.et_search)
    EditText mEtSearch;
    @BindView(R.id.fl_search_hot)
    FlexboxLayout mFlSearchHot;

    String[] mStrings = {"签", "标签", "长标签", "这是一个长标签", "这也是一个长标签", "签", "标签", "长标签", "这是一个长标签", "这也是一个长标签"};
    @BindView(R.id.iv_search_hot_refresh)
    ImageView mIvSearchHotRefresh;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_search);
        ButterKnife.bind(this);

        for (int i = 0; i < 10; i++) {
            TextView textView = (TextView) View.inflate(mActivity, R.layout.item_search_hot, null);
            textView.setText(mStrings[i]);
            mFlSearchHot.addView(textView);
        }

        mEtSearch.setOnEditorActionListener(new SearchListener());
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus) {

            List<FlexLine> flexLines = mFlSearchHot.getFlexLines();
            int num = 0;
            if (flexLines.size() > 2) {
                num += flexLines.get(0).getItemCount();
                num += flexLines.get(1).getItemCount();
                mFlSearchHot.removeAllViews();
            }
            for (int i = 0; i < num; i++) {
                TextView textView = (TextView) View.inflate(mActivity, R.layout.item_search_hot, null);
                textView.setText(mStrings[i]);
                textView.setOnClickListener(this);
                mFlSearchHot.addView(textView);

                ViewGroup.MarginLayoutParams mlp = (ViewGroup.MarginLayoutParams) textView.getLayoutParams();

                mlp.setMargins(0, ScreenUtils.dip2px(mActivity, 15), ScreenUtils.dip2px(mActivity, 10), 0);
            }
        }
    }

    @Override
    protected SearchPresenter createPresenter() {
        return new SearchPresenter(this);
    }

    @Override
    public void getDataSuccess(SearchBean bean) {

    }

    @OnClick(R.id.iv_left_close)
    public void onMIvLeftCloseClicked() {
        finish();
    }

    @OnClick(R.id.rl_search)
    public void onMRlSearchClicked() {
    }

    @OnClick(R.id.ll_search_hot_refresh)
    public void onRefreshClicked() {
        Animation animation = AnimationUtils.loadAnimation(this,R.anim.anim_search_refresh);
        mIvSearchHotRefresh.startAnimation(animation);
    }

    @Override
    public void onClick(View v) {
        mEtSearch.setText(((TextView)v).getText());
    }

    class SearchListener implements TextView.OnEditorActionListener {
        @Override
        public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
            //此处会响应2次 分别为ACTION_DOWN和ACTION_UP
            if (actionId == EditorInfo.IME_ACTION_SEARCH
                    || actionId == EditorInfo.IME_ACTION_DONE
                    || (event != null && KeyEvent.KEYCODE_ENTER == event.getKeyCode() && KeyEvent.ACTION_DOWN == event.getAction())) {

                Intent intent = new Intent(SearchActivity.this,SearchResultActivity.class);
                startActivity(intent);

                return true;
            }
            return false;
        }
    }
}
