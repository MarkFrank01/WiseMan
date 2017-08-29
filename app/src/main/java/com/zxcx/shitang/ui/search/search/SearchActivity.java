package com.zxcx.shitang.ui.search.search;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
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
import com.zxcx.shitang.utils.StringUtils;
import com.zxcx.shitang.utils.Utils;

import java.util.ArrayList;
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
    List<String> mList = new ArrayList<>();
    @BindView(R.id.iv_search_hot_refresh)
    ImageView mIvSearchHotRefresh;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        ButterKnife.bind(this);

        mEtSearch.setOnEditorActionListener(new SearchListener());
        mPresenter.getSearchHot();
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus) {
            //延迟弹出软键盘
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    Utils.showInputMethod(mEtSearch);
                }
            },100);
        }
    }

    @Override
    protected SearchPresenter createPresenter() {
        return new SearchPresenter(this);
    }

    @Override
    public void getDataSuccess(List<String> bean) {
        mList.addAll(bean);
        refreshLabel();
        mIvSearchHotRefresh.clearAnimation();
    }

    @OnClick(R.id.iv_left_close)
    public void onMIvLeftCloseClicked() {
        finish();
    }

    @OnClick(R.id.ll_search_hot_refresh)
    public void onRefreshClicked() {
        Animation animation = AnimationUtils.loadAnimation(this,R.anim.anim_search_refresh);
        mIvSearchHotRefresh.startAnimation(animation);
        mPresenter.getSearchHot();
    }

    @Override
    public void onClick(View v) {
        mEtSearch.setText(((TextView)v).getText());
    }

    private void refreshLabel() {
        for (int i = 0; i < 10; i++) {
            TextView textView = (TextView) View.inflate(mActivity, R.layout.item_search_hot, null);
            textView.setText(mList.get(i));
            mFlSearchHot.addView(textView);
        }
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

            mlp.setMargins(0, ScreenUtils.dip2px(15), ScreenUtils.dip2px(10), 0);
        }
    }

    class SearchListener implements TextView.OnEditorActionListener {
        @Override
        public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
            //此处会响应2次 分别为ACTION_DOWN和ACTION_UP
            if (actionId == EditorInfo.IME_ACTION_SEARCH
                    || actionId == EditorInfo.IME_ACTION_DONE
                    || (event != null && KeyEvent.KEYCODE_ENTER == event.getKeyCode() && KeyEvent.ACTION_DOWN == event.getAction())) {

                if (StringUtils.isEmpty(v.getText().toString())){
                    toastShow("搜索内容不能为空！");
                    return true;
                }

                Intent intent = new Intent(SearchActivity.this,SearchResultActivity.class);
                intent.putExtra("keyword",v.getText().toString());
                startActivity(intent);
                Utils.closeInputMethod(mEtSearch);
                finish();

                return true;
            }
            return false;
        }
    }
}
