package com.zxcx.zhizhe.ui.card.card.cardBagCardDetails;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.RelativeLayout;
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
    @BindView(R.id.tv_card_details_share)
    ImageView mTvCardDetailsShare;
    @BindView(R.id.rl_card_details)
    RelativeLayout mRlCardDetails;
    @BindView(R.id.tv_card_details_num)
    TextView mTvCardDetailsNum;

    private CardBagCardDetailsAdapter mAdapter;
    private List<Integer> mIdList = new ArrayList<>();
    private ArrayList<CardBagCardDetailsBean> mAllCardList = new ArrayList<>();
    private String name;
    private String imageUrl;
    private int id;
    private int cardId;
    private int likeNum;
    private int collectNum;
    private Action mAction;

    private enum Action {
        unCollect,
        like,
        unLike
    }

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
        likeNum = bean.getLikeNum();
        imageUrl = bean.getImageUrl();
        mCbCardDetailsCollect.setText(collectNum + "");
        mCbCardDetailsLike.setText(likeNum + "");
        mCbCardDetailsCollect.setChecked(bean.getIsCollect());
        mCbCardDetailsLike.setChecked(bean.getIsLike());
    }

    @Override
    public void postSuccess() {
        if (mAction == Action.unCollect) {
            collectNum--;
            mCbCardDetailsCollect.setText(collectNum + "");
        } else if (mAction == Action.like) {
            likeNum++;
            mCbCardDetailsLike.setText(likeNum + "");
        } else if (mAction == Action.unLike) {
            likeNum--;
            mCbCardDetailsLike.setText(likeNum + "");
        }
    }

    @Override
    public void postFail(String msg) {
        toastShow(msg);
        if (mAction == Action.unCollect) {
            mCbCardDetailsCollect.setChecked(true);
        } else if (mAction == Action.like) {
            mCbCardDetailsLike.setChecked(false);
        } else if (mAction == Action.unLike) {
            mCbCardDetailsLike.setChecked(true);
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(CollectSuccessEvent event) {
        toastShow("收藏成功");
        mCbCardDetailsCollect.setChecked(true);
        collectNum++;
        mCbCardDetailsCollect.setText(collectNum + "");
    }

    @OnClick(R.id.tv_card_details_share)
    public void onShareClicked() {
        ShareCardDialog shareCardDialog = new ShareCardDialog();
        Bundle bundle = new Bundle();
        bundle.putString("title",name);
        bundle.putString("url", APIService.API_SERVER_URL + "/view/articleLight/" + cardId);
        bundle.putString("imageUrl",imageUrl);
        shareCardDialog.setArguments(bundle);
        shareCardDialog.show(getFragmentManager(), "");
    }

    @OnClick(R.id.cb_card_details_collect)
    public void onCollectClicked() {
        //checkBox点击之后选中状态就已经更改了
        if (mCbCardDetailsCollect.isChecked()) {
            mCbCardDetailsCollect.setChecked(false);
            if (SharedPreferencesUtil.getInt(SVTSConstants.userId, 0) != 0) {
                Intent intent = new Intent(mActivity, SelectCollectFolderActivity.class);
                intent.putExtra("cardId", cardId);
                startActivity(intent);
            } else {
                toastShow("请先登录");
                startActivity(new Intent(mActivity, LoginActivity.class));
            }
        } else {
            mAction = Action.unCollect;
            mCbCardDetailsCollect.setChecked(false);
            mPresenter.removeCollectCard(cardId);
        }
    }

    @OnClick(R.id.cb_card_details_like)
    public void onMCbCardDetailsLikeClicked() {
        //checkBox点击之后选中状态就已经更改了
        if (mCbCardDetailsLike.isChecked()) {
            if (SharedPreferencesUtil.getInt(SVTSConstants.userId, 0) != 0) {
                mAction = Action.like;
                mPresenter.likeCard(cardId);
            } else {
                toastShow("请先登录");
                startActivity(new Intent(mActivity, LoginActivity.class));
            }
        } else {
            mAction = Action.unLike;
            mCbCardDetailsLike.setChecked(false);
            mPresenter.unLikeCard(cardId);
        }
    }

    @OnClick(R.id.tv_card_details_num)
    public void onMTvCardDetailsNumClicked() {
        Intent intent = new Intent(this, CardBagAllCardActivity.class);
        intent.putExtra("name",name);
        intent.putExtra("id",id);
        intent.putExtra("cardId",cardId);
        intent.putParcelableArrayListExtra("allCard", mAllCardList);
        startActivity(intent);
        overridePendingTransition(R.anim.anim_right_in,R.anim.anim_left_out);
    }

    @OnClick(R.id.iv_card_details_back)
    public void onBackClicked() {
        finish();
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
    /*@Override
    public void onDefaultShareClick() {
        Bitmap bitmap = ScreenUtils.getBitmapByView(mVpCardBagCardDetails);
        String fileName = FileUtil.getFileName();
        String imagePath = FileUtil.PATH_BASE + fileName;
        FileUtil.saveBitmapToSDCard(bitmap, FileUtil.PATH_BASE, fileName);

        ShareCardDialog shareCardDialog = new ShareCardDialog();
        Bundle bundle = new Bundle();
        bundle.putString("imagePath", imagePath);
        shareCardDialog.setArguments(bundle);
        shareCardDialog.show(getFragmentManager(), "");
    }

    @Override
    public void onDiyShareClick() {
        Intent intent = new Intent(this, DiyShareActivity.class);
        startActivity(intent);
    }*/
}
