package com.zxcx.zhizhe.ui.card.card.cardBagCardDetails;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.zxcx.zhizhe.R;
import com.zxcx.zhizhe.event.CollectSuccessEvent;
import com.zxcx.zhizhe.mvpBase.MvpActivity;
import com.zxcx.zhizhe.retrofit.APIService;
import com.zxcx.zhizhe.ui.card.card.cardBagCardDetails.allCard.CardBagAllCardActivity;
import com.zxcx.zhizhe.ui.card.card.collect.SelectCollectFolderActivity;
import com.zxcx.zhizhe.ui.card.card.newCardDetails.CardDetailsBean;
import com.zxcx.zhizhe.ui.card.card.share.ShareCardDialog;
import com.zxcx.zhizhe.ui.loginAndRegister.login.LoginActivity;
import com.zxcx.zhizhe.utils.SVTSConstants;
import com.zxcx.zhizhe.utils.SharedPreferencesUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class CardBagCardDetailsActivity extends MvpActivity<CardBagCardDetailsPresenter> implements CardBagCardDetailsContract.View,
        ViewPager.OnPageChangeListener {

    @BindView(R.id.vp_card_bag_card_details)
    ViewPager mVpCardBagCardDetails;
    @BindView(R.id.iv_card_details_back)
    ImageView mIvCardDetailsBack;
    @BindView(R.id.tv_card_details_title)
    TextView mTvCardDetailsTitle;
    @BindView(R.id.cb_card_details_collect)
    CheckBox mCbCardDetailsCollect;
    @BindView(R.id.cb_card_details_like)
    CheckBox mCbCardDetailsLike;
    @BindView(R.id.iv_card_details_share)
    ImageView mTvCardDetailsShare;
    @BindView(R.id.tv_card_details_num)
    TextView mTvCardDetailsNum;
    @BindView(R.id.cb_card_details_un_like)
    CheckBox mCbCardDetailsUnLike;

    private CardBagCardDetailsAdapter mAdapter;
    private List<Integer> mIdList = new ArrayList<>();
    private ArrayList<CardBagCardDetailsBean> mAllCardList = new ArrayList<>();
    private String name;
    private String imageUrl;
    private int id;
    private int cardId;
    private int collectNum;
    private String cardName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_card_bag_card_details);
        ButterKnife.bind(this);
        EventBus.getDefault().register(this);

        id = getIntent().getIntExtra("id", 0);
        cardId = getIntent().getIntExtra("cardId", 0);
        name = getIntent().getStringExtra("name");
        ArrayList<CardBagCardDetailsBean> list = getIntent().getParcelableArrayListExtra("allCard");

        mTvCardDetailsTitle.setText(name);

        if (list != null && list.size() != 0) {
            getAllCardIdSuccess(list);
        } else {
            mPresenter.getAllCardId(id);
        }

        mPresenter.getCardDetails(cardId);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Override
    protected CardBagCardDetailsPresenter createPresenter() {
        return new CardBagCardDetailsPresenter(this);
    }

    @Override
    public void getAllCardIdSuccess(List<CardBagCardDetailsBean> list) {
        mAllCardList.clear();
        mAllCardList.addAll(list);
        for (CardBagCardDetailsBean bean : list) {
            mIdList.add(bean.getId());
        }
        mAdapter = new CardBagCardDetailsAdapter(getSupportFragmentManager(), mIdList);
        mVpCardBagCardDetails.setAdapter(mAdapter);

        int position = mIdList.indexOf(cardId);
        mVpCardBagCardDetails.setCurrentItem(position);
        mVpCardBagCardDetails.addOnPageChangeListener(this);

        String num = position + 1 + "/" + mIdList.size();
        mTvCardDetailsNum.setText(num);
    }

    @Override
    public void getDataSuccess(CardDetailsBean bean) {
        collectNum = bean.getCollectNum();
        int likeNum = bean.getLikeNum();
        int unLikeNum = bean.getUnLikeNum();
        imageUrl = bean.getImageUrl();
        cardName = bean.getName();
        mCbCardDetailsCollect.setText(collectNum + "");
        mCbCardDetailsLike.setText(likeNum + "");
        mCbCardDetailsUnLike.setText(unLikeNum + "");
        mCbCardDetailsCollect.setChecked(bean.getIsCollect());
        mCbCardDetailsLike.setChecked(bean.getIsLike());
        mCbCardDetailsUnLike.setChecked(bean.isUnLike());
    }

    @Override
    public void postSuccess() {

    }

    @Override
    public void UnCollectSuccess() {
        mCbCardDetailsCollect.setChecked(false);
        collectNum--;
        mCbCardDetailsCollect.setText(collectNum + "");
    }

    @Override
    public void postFail(String msg) {
        toastShow(msg);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(CollectSuccessEvent event) {
        toastShow("收藏成功");
        mCbCardDetailsCollect.setChecked(true);
        collectNum++;
        mCbCardDetailsCollect.setText(collectNum + "");
    }

    @OnClick(R.id.iv_card_details_share)
    public void onShareClicked() {
        ShareCardDialog shareCardDialog = new ShareCardDialog();
        Bundle bundle = new Bundle();
        bundle.putString("title", cardName);
        bundle.putString("text", name);
        bundle.putString("url", APIService.API_SERVER_URL + "/view/articleLight/" + cardId);
        bundle.putString("imageUrl", imageUrl);
        shareCardDialog.setArguments(bundle);
        shareCardDialog.show(getFragmentManager(), "");
    }

    @OnClick(R.id.cb_card_details_collect)
    public void onCbCollectClicked() {
        //checkBox点击之后选中状态就已经更改了
        mCbCardDetailsCollect.setChecked(!mCbCardDetailsCollect.isChecked());
        if (!mCbCardDetailsCollect.isChecked()) {
            if (SharedPreferencesUtil.getInt(SVTSConstants.userId, 0) != 0) {
                Intent intent = new Intent(mActivity, SelectCollectFolderActivity.class);
                intent.putExtra("cardId", cardId);
                startActivity(intent);
            } else {
                toastShow("请先登录");
                startActivity(new Intent(mActivity, LoginActivity.class));
            }
        } else {
            mPresenter.removeCollectCard(cardId);
        }
    }

    @OnClick(R.id.cb_card_details_like)
    public void onCbLikeClicked() {
        //checkBox点击之后选中状态就已经更改了
        mCbCardDetailsLike.setChecked(!mCbCardDetailsLike.isChecked());
        if (!mCbCardDetailsLike.isChecked()) {
            if (SharedPreferencesUtil.getInt(SVTSConstants.userId, 0) != 0) {
                mPresenter.likeCard(cardId);
            } else {
                toastShow("请先登录");
                startActivity(new Intent(mActivity, LoginActivity.class));
            }
        } else {
            mPresenter.removeLikeCard(cardId);
        }
    }

    @OnClick(R.id.cb_card_details_un_like)
    public void onCbUnLikeClicked() {
        //checkBox点击之后选中状态就已经更改了
        mCbCardDetailsUnLike.setChecked(!mCbCardDetailsUnLike.isChecked());
        if (!mCbCardDetailsUnLike.isChecked()) {
            if (SharedPreferencesUtil.getInt(SVTSConstants.userId, 0) != 0) {
                mPresenter.unLikeCard(cardId);
            } else {
                toastShow("请先登录");
                startActivity(new Intent(mActivity, LoginActivity.class));
            }
        } else {
            mPresenter.removeUnLikeCard(cardId);
        }
    }

    @OnClick(R.id.tv_card_details_num)
    public void onMTvCardDetailsNumClicked() {
        Intent intent = new Intent(this, CardBagAllCardActivity.class);
        intent.putExtra("name", name);
        intent.putExtra("id", id);
        intent.putExtra("cardId", cardId);
        intent.putParcelableArrayListExtra("allCard", mAllCardList);
        startActivity(intent);
        overridePendingTransition(R.anim.anim_right_in, R.anim.anim_left_out);
    }

    @OnClick(R.id.iv_card_details_back)
    public void onBackClicked() {
        onBackPressed();
    }


    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        cardId = mIdList.get(position);
        mPresenter.getCardDetails(cardId);
        String num = position + 1 + "/" + mIdList.size();
        mTvCardDetailsNum.setText(num);
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }
}
