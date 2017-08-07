package com.zxcx.shitang.ui.my.collect.collectCard;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.zxcx.shitang.R;
import com.zxcx.shitang.mvpBase.MvpActivity;
import com.zxcx.shitang.ui.card.card.cardDetails.CardDetailsActivity;
import com.zxcx.shitang.ui.my.collect.collectCard.adapter.CollectCardAdapter;
import com.zxcx.shitang.ui.my.collect.collectFolder.itemDecoration.CollectFolderItemDecoration;
import com.zxcx.shitang.utils.Utils;
import com.zxcx.shitang.widget.CustomLoadMoreView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.zxcx.shitang.App.getContext;

public class CollectCardActivity extends MvpActivity<CollectCardPresenter> implements CollectCardContract.View,
        BaseQuickAdapter.RequestLoadMoreListener, CollectCardAdapter.CollectCardCheckListener, BaseQuickAdapter.OnItemClickListener {

    @BindView(R.id.tv_toolbar_right)
    TextView mTvToolbarRight;
    @BindView(R.id.rv_collect_card)
    RecyclerView mRvCollectCard;
    @BindView(R.id.toolbar_title)
    TextView mToolbarTitle;
    @BindView(R.id.ll_collect_card_edit)
    LinearLayout mLlCollectCardEdit;
    @BindView(R.id.iv_dialog_collect_folder_confirm)
    ImageView mIvDialogCollectFolderConfirm;
    @BindView(R.id.et_edit_collect_folder)
    EditText mEtEditCollectFolder;
    @BindView(R.id.ll_collect_folder_edit)
    LinearLayout mLlCollectFolderEdit;

    private CollectCardAdapter mCollectCardAdapter;
    private List<CollectCardBean> mList = new ArrayList<>();
    private List<CollectCardBean> mCheckedList = new ArrayList<>();
    private boolean isErr = false;
    private int TOTAL_COUNTER = 20;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_collect_card);
        ButterKnife.bind(this);
        initToolBar("笔记本");
        mEtEditCollectFolder.setHint("笔记本");

        getData();
        initRecyclerView();

        mTvToolbarRight.setVisibility(View.VISIBLE);
        mTvToolbarRight.setText("编辑");
        mEtEditCollectFolder.addTextChangedListener(new AddCollectFolderTextWatcher());
    }

    @Override
    public void onBackPressed() {
        if (mLlCollectFolderEdit.getVisibility() == View.VISIBLE){
            onMIvCommonCloseClicked();
        }else {
            super.onBackPressed();
        }
    }

    private void initRecyclerView() {
        mCollectCardAdapter = new CollectCardAdapter(mList, this);
        mCollectCardAdapter.setLoadMoreView(new CustomLoadMoreView());
        mCollectCardAdapter.setOnLoadMoreListener(this, mRvCollectCard);
        mCollectCardAdapter.setOnItemClickListener(this);
        mRvCollectCard.setLayoutManager(new GridLayoutManager(getContext(), 2));
        mRvCollectCard.setAdapter(mCollectCardAdapter);
        mRvCollectCard.addItemDecoration(new CollectFolderItemDecoration());
        mCollectCardAdapter.notifyDataSetChanged();
    }

    @Override
    protected CollectCardPresenter createPresenter() {
        return new CollectCardPresenter(this);
    }

    @Override
    public void getDataSuccess(CollectCardBean bean) {

    }

    @Override
    public void onLoadMoreRequested() {
        if (mCollectCardAdapter.getData().size() > TOTAL_COUNTER) {
            mCollectCardAdapter.loadMoreEnd(false);
        } else {
            if (isErr) {
                getData();
//                mHotCardAdapter.addData();
                mCollectCardAdapter.notifyDataSetChanged();
                mCollectCardAdapter.loadMoreComplete();
            } else {
                isErr = true;
                Toast.makeText(getContext(), "网络错误", Toast.LENGTH_LONG).show();
                mCollectCardAdapter.loadMoreFail();
            }
        }
    }

    private void getData() {
        for (int i = 0; i < 10; i++) {
            mList.add(new CollectCardBean());
        }
    }

    @Override
    public void onCheckedChanged(CollectCardBean bean, int position, boolean isChecked) {
        if (isChecked) {
            if (!mCheckedList.contains(bean)) {
                mCheckedList.add(bean);
            }
        } else {
            if (mCheckedList.contains(bean)) {
                mCheckedList.remove(bean);
            }
        }
        if (mCheckedList.size() > 0) {
            mTvToolbarRight.setText("删除");
        } else {
            mTvToolbarRight.setText("取消");
        }
    }

    @OnClick(R.id.tv_toolbar_right)
    public void onViewClicked() {
        switch (mTvToolbarRight.getText().toString()) {
            case "编辑":
                mLlCollectCardEdit.setVisibility(View.VISIBLE);
                mTvToolbarRight.setText("取消");
                mCollectCardAdapter.setDelete(true);
                mCollectCardAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
                    @Override
                    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {

                    }
                });
                mCollectCardAdapter.notifyDataSetChanged();
                break;
            case "取消":
                mLlCollectCardEdit.setVisibility(View.GONE);
                mTvToolbarRight.setText("编辑");
                mCollectCardAdapter.setDelete(false);
                mCollectCardAdapter.setOnItemClickListener(this);
                mCollectCardAdapter.notifyDataSetChanged();
                break;
            case "删除":
                mList.removeAll(mCheckedList);
                mCollectCardAdapter.notifyDataSetChanged();
                mCheckedList.clear();
                break;
        }
    }

    @Override
    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
        Intent intent = new Intent(this, CardDetailsActivity.class);
        startActivity(intent);
    }

    @OnClick(R.id.ll_collect_card_edit)
    public void onMLlCollectCardEditClicked() {
        Animation animation = AnimationUtils.loadAnimation(this, R.anim.anim_add_collect_folder_open);
        mLlCollectFolderEdit.startAnimation(animation);
        mLlCollectFolderEdit.setVisibility(View.VISIBLE);
        mLlCollectCardEdit.setVisibility(View.GONE);
        Utils.showInputMethod(mEtEditCollectFolder);
    }

    @OnClick(R.id.iv_common_close)
    public void onMIvCommonCloseClicked() {
        Animation animation = AnimationUtils.loadAnimation(this, R.anim.anim_add_collect_folder_close);
        mLlCollectFolderEdit.startAnimation(animation);
        mLlCollectFolderEdit.setVisibility(View.GONE);
        mLlCollectCardEdit.setVisibility(View.VISIBLE);
        Utils.closeInputMethod(mEtEditCollectFolder);
    }

    @OnClick(R.id.iv_dialog_collect_folder_confirm)
    public void onMIvDialogCollectFolderConfirmClicked() {
    }

    @OnClick(R.id.rl_collect_folder_edit)
    public void onMRlCollectFolderEditClicked() {
        onMIvCommonCloseClicked();
    }

    @OnClick(R.id.ll_collect_folder_edit)
    public void onMLlCollectFolderEditClicked() {
        //保持为空，覆盖掉底下的点击事件监听
    }

    class AddCollectFolderTextWatcher implements TextWatcher {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            if (s.length() > 0) {
                mIvDialogCollectFolderConfirm.setImageResource(R.drawable.iv_dialog_collect_folder_confirm_blue);
            } else {
                mIvDialogCollectFolderConfirm.setImageResource(R.drawable.iv_dialog_collect_folder_confirm_white);
            }
        }
    }
}
