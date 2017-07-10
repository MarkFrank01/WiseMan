package com.zxcx.shitang.ui.my;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.makeramen.roundedimageview.RoundedImageView;
import com.zxcx.shitang.R;
import com.zxcx.shitang.event.LoginEvent;
import com.zxcx.shitang.event.LogoutEvent;
import com.zxcx.shitang.mvpBase.BaseFragment;
import com.zxcx.shitang.ui.loginAndRegister.login.LoginActivity;
import com.zxcx.shitang.ui.my.aboutUS.AboutUSActivity;
import com.zxcx.shitang.ui.my.collect.collectFolder.CollectFolderActivity;
import com.zxcx.shitang.ui.my.userInfo.UserInfoActivity;
import com.zxcx.shitang.utils.SVTSConstants;
import com.zxcx.shitang.utils.SharedPreferencesUtil;
import com.zxcx.shitang.utils.StringUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MyFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MyFragment extends BaseFragment {

    private static MyFragment fragment = null;
    Unbinder unbinder;
    @BindView(R.id.tv_my_login)
    TextView mTvMyLogin;
    @BindView(R.id.tv_my_nick_name)
    TextView mTvMyNickName;
    @BindView(R.id.tv_my_collect)
    TextView mTvMyCollect;
    @BindView(R.id.iv_toolbar_back)
    ImageView mIvToolbarBack;
    @BindView(R.id.iv_my_head)
    RoundedImageView mIvMyHead;
    @BindView(R.id.rl_my_head)
    RelativeLayout mRlMyHead;
    @BindView(R.id.ll_my_collect)
    LinearLayout mLlMyCollect;
    @BindView(R.id.tv_my_common_setting)
    TextView mTvMyCommonSetting;
    @BindView(R.id.ll_my_common_setting)
    LinearLayout mLlMyCommonSetting;
    @BindView(R.id.tv_my_feedback)
    TextView mTvMyFeedback;
    @BindView(R.id.ll_my_feedback)
    LinearLayout mLlMyFeedback;
    @BindView(R.id.tv_my_about_us)
    TextView mTvMyAboutUs;
    @BindView(R.id.ll_my_about_us)
    LinearLayout mLlMyAboutUs;
    @BindView(R.id.tv_my_info)
    TextView mTvMyInfo;

    public static MyFragment newInstance() {
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
        EventBus.getDefault().register(this);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initToolBar(view, R.string.title_my);
        mIvToolbarBack.setVisibility(View.GONE);

        if (StringUtils.isEmpty(SharedPreferencesUtil.getString(SVTSConstants.userId,""))){
            setViewLogout();
        }else {
            setViewLogin();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(LoginEvent event) {
        setViewLogin();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(LogoutEvent event) {
        setViewLogout();
    }

    @OnClick(R.id.rl_my_head)
    public void onMIvMyHeadClicked() {
        if (StringUtils.isEmpty(SharedPreferencesUtil.getString(SVTSConstants.userId,""))) {
            Intent intent = new Intent(getContext(), LoginActivity.class);
            startActivity(intent);
        }else {
            Intent intent = new Intent(getContext(), UserInfoActivity.class);
            startActivity(intent);
        }
    }

    @OnClick(R.id.ll_my_collect)
    public void onMLlMyCollectClicked() {
        if (StringUtils.isEmpty(SharedPreferencesUtil.getString(SVTSConstants.userId,""))) {
            Intent intent = new Intent(getContext(), LoginActivity.class);
            startActivity(intent);
        }else {
            Intent intent = new Intent(getContext(), CollectFolderActivity.class);
            startActivity(intent);
        }
    }

    @OnClick(R.id.ll_my_common_setting)
    public void onMLlMyCommonSettingClicked() {
        Intent intent = new Intent(getContext(), CommonSettingActivity.class);
        startActivity(intent);
    }

    @OnClick(R.id.ll_my_feedback)
    public void onMLlMyFeedbackClicked() {

    }

    @OnClick(R.id.ll_my_about_us)
    public void onMLlMyAboutUsClicked() {
        Intent intent = new Intent(getContext(), AboutUSActivity.class);
        startActivity(intent);
    }

    private void setViewLogout() {
        mTvMyLogin.setVisibility(View.VISIBLE);
        mTvMyNickName.setVisibility(View.GONE);
        mTvMyInfo.setVisibility(View.GONE);
        mIvMyHead.setImageResource(R.drawable.iv_my_head_placeholder);
    }

    private void setViewLogin() {
        mTvMyLogin.setVisibility(View.GONE);
        mTvMyNickName.setVisibility(View.VISIBLE);
        mTvMyInfo.setVisibility(View.VISIBLE);
        mTvMyNickName.setText(SharedPreferencesUtil.getString(SVTSConstants.nickName,""));
        mIvMyHead.setImageResource(R.drawable.iv_my_head_icon);
    }
}
