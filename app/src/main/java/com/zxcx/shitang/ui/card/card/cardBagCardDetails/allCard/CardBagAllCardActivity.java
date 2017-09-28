package com.zxcx.shitang.ui.card.card.cardBagCardDetails.allCard;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.zxcx.shitang.R;
import com.zxcx.shitang.mvpBase.BaseActivity;
import com.zxcx.shitang.ui.card.card.cardBagCardDetails.CardBagCardDetailsActivity;
import com.zxcx.shitang.ui.card.card.cardBagCardDetails.CardBagCardDetailsBean;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CardBagAllCardActivity extends BaseActivity implements BaseQuickAdapter.OnItemClickListener{

    @BindView(R.id.iv_toolbar_back)
    ImageView mIvToolbarBack;
    @BindView(R.id.toolbar_title)
    TextView mToolbarTitle;
    @BindView(R.id.iv_toolbar_right)
    ImageView mIvToolbarRight;
    @BindView(R.id.tv_toolbar_right)
    TextView mTvToolbarRight;
    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.rv_all_card)
    RecyclerView mRvAllCard;
    private CardBagAllCardAdapter mAdapter;
    private int currentCardId;
    private ArrayList<CardBagCardDetailsBean> mList;
    private String name;
    private int id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_card_bag_all_card);
        ButterKnife.bind(this);

        initData();
        initToolBar(name);

        initRecyclerView();
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.anim_left_in,R.anim.anim_right_out);
    }

    private void initRecyclerView() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(mActivity, LinearLayoutManager.VERTICAL, false);
        mAdapter = new CardBagAllCardAdapter(mList,currentCardId);
        mAdapter.setOnItemClickListener(this);
        mRvAllCard.setLayoutManager(layoutManager);
        mRvAllCard.setAdapter(mAdapter);
    }

    private void initData() {
        mList = getIntent().getParcelableArrayListExtra("allCard");
        currentCardId = getIntent().getIntExtra("cardId", 0);
        id = getIntent().getIntExtra("id", 0);
        name = getIntent().getStringExtra("name");
    }

    @Override
    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
        Intent intent = new Intent(this, CardBagCardDetailsActivity.class);
        intent.putExtra("name",name);
        intent.putExtra("id",id);
        intent.putExtra("cardId",mList.get(position).getId());
        intent.putParcelableArrayListExtra("allCard", mList);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        overridePendingTransition(R.anim.anim_left_in,R.anim.anim_right_out);
    }
}
