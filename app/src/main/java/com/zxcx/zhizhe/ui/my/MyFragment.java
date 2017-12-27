package com.zxcx.zhizhe.ui.my;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.makeramen.roundedimageview.RoundedImageView;
import com.zxcx.zhizhe.R;
import com.zxcx.zhizhe.event.LoginEvent;
import com.zxcx.zhizhe.event.LogoutEvent;
import com.zxcx.zhizhe.event.UserInfoChangeSuccessEvent;
import com.zxcx.zhizhe.mvpBase.BaseFragment;
import com.zxcx.zhizhe.mvpBase.BaseRxJava;
import com.zxcx.zhizhe.mvpBase.IGetPresenter;
import com.zxcx.zhizhe.retrofit.AppClient;
import com.zxcx.zhizhe.retrofit.BaseSubscriber;
import com.zxcx.zhizhe.ui.my.collect.CollectCardActivity;
import com.zxcx.zhizhe.ui.my.creation.ApplyReviewActivity;
import com.zxcx.zhizhe.ui.my.creation.CreationActivity;
import com.zxcx.zhizhe.ui.my.creation.CreationAgreementDialog;
import com.zxcx.zhizhe.ui.my.followUser.FollowUserActivity;
import com.zxcx.zhizhe.ui.my.intelligenceValue.IntelligenceValueActivity;
import com.zxcx.zhizhe.ui.my.likeCards.LikeCardsActivity;
import com.zxcx.zhizhe.ui.my.message.MessageActivity;
import com.zxcx.zhizhe.ui.my.note.NoteActivity;
import com.zxcx.zhizhe.ui.my.readCards.ReadCardsActivity;
import com.zxcx.zhizhe.ui.my.setting.CommonSettingActivity;
import com.zxcx.zhizhe.ui.my.userInfo.UserInfoActivity;
import com.zxcx.zhizhe.utils.ImageLoader;
import com.zxcx.zhizhe.utils.SVTSConstants;
import com.zxcx.zhizhe.utils.SharedPreferencesUtil;
import com.zxcx.zhizhe.utils.ZhiZheUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

import static com.zxcx.zhizhe.ui.my.RedPointBeanKt.writer_status_review;
import static com.zxcx.zhizhe.ui.my.RedPointBeanKt.writer_status_writer;

/**
 * A simple {@link Fragment} subclass.
 */
public class MyFragment extends BaseFragment implements IGetPresenter<RedPointBean>{

    Unbinder unbinder;
    @BindView(R.id.iv_message_red_point)
    ImageView mIvMessageRedPoint;
    @BindView(R.id.iv_my_head)
    RoundedImageView mIvMyHead;
    @BindView(R.id.tv_my_nick_name)
    TextView mTvMyNickName;
    @BindView(R.id.tv_my_info)
    TextView mTvMyInfo;
    @BindView(R.id.fl_my_message)
    FrameLayout mFlMyMessage;
    @BindView(R.id.iv_creation_red_point)
    ImageView mIvCreationRedPoint;

    private int writerStatus;
    private boolean hasSystemMessage;
    private boolean hasDynamicMessage;
    private int totalIntelligenceValue;

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
        if (SharedPreferencesUtil.getInt(SVTSConstants.userId, 0) == 0) {
            setViewLogout();
        } else {
            setViewLogin();
            getRedPointStatus();
        }
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!hidden) {
            hasDynamicMessage = SharedPreferencesUtil.getBoolean(SVTSConstants.hasDynamicMessage,false);
            hasSystemMessage = SharedPreferencesUtil.getBoolean(SVTSConstants.hasSystemMessage,false);
            refreshRedPoint();
            getRedPointStatus();
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
    public void onMessageEvent(UserInfoChangeSuccessEvent event) {
        setViewLogin();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(LogoutEvent event) {
        setViewLogout();
    }

    @OnClick(R.id.rl_my_head)
    public void onMRlMyHeadClicked() {
        checkLogin();
    }

    @OnClick(R.id.fl_my_message)
    public void onMFlMyMessageClicked() {
        //消息界面
        Intent intent = new Intent(getContext(), MessageActivity.class);
        startActivity(intent);
    }

    @OnClick(R.id.iv_my_setting)
    public void onMIvMySettingClicked() {
        //设置界面
        Intent intent = new Intent(getContext(), CommonSettingActivity.class);
        startActivity(intent);
    }

    @OnClick({R.id.iv_my_head, R.id.tv_my_nick_name})
    public void onMIvMyHeadClicked() {
        //账户资料界面
        if (checkLogin()) {
            Intent intent = new Intent(getContext(), UserInfoActivity.class);
            startActivity(intent);
        }
    }

    @OnClick(R.id.tv_my_info)
    public void onMTvMyInfoClicked() {
        if (checkLogin()) {
            //智力值界面
            Intent intent = new Intent(getContext(), IntelligenceValueActivity.class);
            startActivity(intent);
        }
    }

    @OnClick(R.id.ll_my_creation)
    public void onMLlMyCreationClicked() {
        if (checkLogin()){
            switch (writerStatus){
                case writer_status_writer:
                    //创作界面
                    Intent intent = new Intent(getContext(), CreationActivity.class);
                    startActivity(intent);
                    break;
                case writer_status_review:
                    //资格审核中
                    Intent intent1 = new Intent(getContext(), ApplyReviewActivity.class);
                    startActivity(intent1);
                    break;
                default:
                    CreationAgreementDialog dialog = new CreationAgreementDialog();
                    dialog.show(mActivity.getFragmentManager(),"");
                    break;

            }
        }

    }

    @OnClick(R.id.ll_my_read)
    public void onMLlMyReadClicked() {
        if (checkLogin()) {
            //阅读页面
            Intent intent = new Intent(getContext(), ReadCardsActivity.class);
            startActivity(intent);
        }
    }

    @OnClick(R.id.ll_my_note)
    public void onMLlMyNoteClicked() {
        if (checkLogin()) {
            //笔记页面
            Intent intent = new Intent(getContext(), NoteActivity.class);
            startActivity(intent);
        }
    }

    @OnClick(R.id.ll_my_follow)
    public void onMLlMyFollowClicked() {
        if (checkLogin()) {
            //关注页面
            Intent intent = new Intent(getContext(), FollowUserActivity.class);
            startActivity(intent);
        }
    }

    @OnClick(R.id.ll_my_collect)
    public void onMLlMyCollectClicked() {
        if (checkLogin()) {
            //收藏页面
            Intent intent = new Intent(getContext(), CollectCardActivity.class);
            startActivity(intent);
        }
    }

    @OnClick(R.id.ll_my_like)
    public void onMLlMyLikeClicked() {
        if (checkLogin()) {
            //点赞页面
            Intent intent = new Intent(getContext(), LikeCardsActivity.class);
            startActivity(intent);
        }
    }

    private void setViewLogout() {
        mFlMyMessage.setClickable(false);
        mIvMessageRedPoint.setVisibility(View.GONE);
        mTvMyNickName.setText("注册/登录");
        mTvMyNickName.setTextColor(ContextCompat.getColor(mActivity, R.color.button_blue));
        mTvMyInfo.setText("登录看谁在关注你");
        mTvMyInfo.setTextColor(ContextCompat.getColor(mActivity, R.color.text_color_3));
        mIvMyHead.setImageResource(R.drawable.iv_my_head_placeholder);
    }

    private void setViewLogin() {
        mFlMyMessage.setClickable(true);
        mTvMyNickName.setText(SharedPreferencesUtil.getString(SVTSConstants.nickName, ""));
        mTvMyNickName.setTextColor(ContextCompat.getColor(mActivity, R.color.text_color_1));
        String headImg = SharedPreferencesUtil.getString(SVTSConstants.imgUrl, "");
        ImageLoader.load(mActivity, headImg, R.drawable.default_header, mIvMyHead);
        writerStatus = SharedPreferencesUtil.getInt(SVTSConstants.writerStatus,0);
        hasDynamicMessage = SharedPreferencesUtil.getBoolean(SVTSConstants.hasDynamicMessage,false);
        hasSystemMessage = SharedPreferencesUtil.getBoolean(SVTSConstants.hasSystemMessage,false);
        totalIntelligenceValue = SharedPreferencesUtil.getInt(SVTSConstants.totalIntelligenceValue,0);
        refreshRedPoint();
    }

    private void getRedPointStatus() {
        if (SharedPreferencesUtil.getInt(SVTSConstants.userId,0) == 0){
            return;
        }
        mDisposable = AppClient.getAPIService().getRedPointStatus()
                .compose(BaseRxJava.handleResult())
                .compose(BaseRxJava.io_main())
                .subscribeWith(new BaseSubscriber<RedPointBean>(this) {
                    @Override
                    public void onNext(RedPointBean bean) {
                        getDataSuccess(bean);
                    }
                });
        addSubscription(mDisposable);
    }

    @Override
    public void getDataSuccess(RedPointBean bean) {
        writerStatus = bean.getWriterStatus();
        hasDynamicMessage = bean.getHasDynamicMessage();
        hasSystemMessage = bean.getHasSystemMessage();
        totalIntelligenceValue = bean.getTotalIntelligenceValue();
        SharedPreferencesUtil.saveData(SVTSConstants.writerStatus,writerStatus);
        SharedPreferencesUtil.saveData(SVTSConstants.hasDynamicMessage,hasDynamicMessage);
        SharedPreferencesUtil.saveData(SVTSConstants.hasSystemMessage,hasSystemMessage);
        SharedPreferencesUtil.saveData(SVTSConstants.totalIntelligenceValue,totalIntelligenceValue);
        refreshRedPoint();
    }

    private void refreshRedPoint() {
        if (hasSystemMessage || hasDynamicMessage){
            mIvMessageRedPoint.setVisibility(View.VISIBLE);
        }else {
            mIvMessageRedPoint.setVisibility(View.GONE);
        }
        if (writerStatus == writer_status_writer){
            mIvCreationRedPoint.setVisibility(View.GONE);
        }else {
            mIvCreationRedPoint.setVisibility(View.VISIBLE);
        }
        mTvMyInfo.setText(getString(R.string.tv_other_user_info, ZhiZheUtils.getFormatNumber(totalIntelligenceValue)));
        mTvMyInfo.setTextColor(ContextCompat.getColor(mActivity, R.color.button_blue));
    }

    @Override
    public void getDataFail(String msg) {
        toastShow(msg);
    }
}
