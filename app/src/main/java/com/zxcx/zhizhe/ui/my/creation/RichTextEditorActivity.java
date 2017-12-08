package com.zxcx.zhizhe.ui.my.creation;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.gyf.barlibrary.ImmersionBar;
import com.zxcx.zhizhe.R;
import com.zxcx.zhizhe.mvpBase.MvpActivity;
import com.zxcx.zhizhe.utils.AndroidBug5497Workaround;
import com.zxcx.zhizhe.utils.Constants;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class RichTextEditorActivity extends MvpActivity<RichTextEditorPresenter> implements RichTextEditorContract.View {

    @BindView(R.id.iv_toolbar_back)
    ImageView mIvToolbarBack;
    @BindView(R.id.rb_rte_editor)
    RadioButton mRbRteEditor;
    @BindView(R.id.rb_rte_preview)
    RadioButton mRbRtePreview;
    @BindView(R.id.tv_toolbar_right)
    TextView mTvToolbarRight;
    @BindView(R.id.fl_content)
    FrameLayout mFlContent;
    @BindView(R.id.rl_toolbar)
    RelativeLayout mRlToolbar;

    private Fragment mCurrentFragment = new Fragment();
    private RichTextEditorFragment mEditorFragment = new RichTextEditorFragment();
    private PreviewFragment mPreviewFragment = new PreviewFragment();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rich_text_editor);
        ButterKnife.bind(this);
        AndroidBug5497Workaround.assistActivity(this);
        mRbRteEditor.performClick();


        if (!Constants.IS_NIGHT) {
            ImmersionBar.with(this)
                    .statusBarColor(R.color.white_final)
                    .statusBarDarkFont(true, 0.2f)
                    .flymeOSStatusBarFontColor(R.color.black)
                    .statusBarView(R.id.status_bar)
                    .init();
        } else {

        }
    }

    @Override
    public void initStatusBar() {

    }

    @Override
    protected RichTextEditorPresenter createPresenter() {
        return new RichTextEditorPresenter(this);
    }

    @Override
    public void getDataSuccess(RichTextEditorBean bean) {

    }

    @OnClick(R.id.iv_toolbar_back)
    public void onMIvToolbarBackClicked() {
        onBackPressed();
    }

    @OnClick(R.id.rb_rte_editor)
    public void onMRbRteEditorClicked() {
        switchFragment(mEditorFragment);

    }

    @OnClick(R.id.rb_rte_preview)
    public void onMRbRtePreviewClicked() {
        switchFragment(mPreviewFragment);
        mPreviewFragment.setTitle(mEditorFragment.getTitle());
        mPreviewFragment.setContent(mEditorFragment.getContent());

    }

    @OnClick(R.id.tv_toolbar_right)
    public void onMTvToolbarRightClicked() {
    }

    private void switchFragment(Fragment newFragment) {
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction transaction = fm.beginTransaction();

        if (newFragment.isAdded()) {
            //.setCustomAnimations(R.anim.fragment_anim_left_in,R.anim.fragment_anim_right_out)
            transaction.hide(mCurrentFragment).show(newFragment).commitAllowingStateLoss();
        } else {
            transaction.hide(mCurrentFragment).add(R.id.fl_content, newFragment).commitAllowingStateLoss();
        }
        mCurrentFragment = newFragment;
    }
}
