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

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.zxcx.shitang.R;
import com.zxcx.shitang.event.ChangeCollectFolderNameEvent;
import com.zxcx.shitang.mvpBase.MvpActivity;
import com.zxcx.shitang.mvpBase.PostBean;
import com.zxcx.shitang.ui.card.card.newCardDetails.CardDetailsActivity;
import com.zxcx.shitang.ui.my.collect.collectCard.adapter.CollectCardAdapter;
import com.zxcx.shitang.ui.my.collect.collectFolder.itemDecoration.CollectFolderItemDecoration;
import com.zxcx.shitang.utils.Constants;
import com.zxcx.shitang.utils.Utils;
import com.zxcx.shitang.widget.CustomLoadMoreView;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.zxcx.shitang.App.getContext;

public class CollectCardActivity extends MvpActivity<CollectCardPresenter> implements CollectCardContract.View,
        BaseQuickAdapter.RequestLoadMoreListener, CollectCardAdapter.CollectCardCheckListener, BaseQuickAdapter.OnItemClickListener {

    private static final int ACTION_DELETE = 100;
    private static final int ACTION_CHANGE = 101;

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

    private CollectCardAdapter mAdapter;
    private List<CollectCardBean> mCheckedList = new ArrayList<>();
    private int page = 0;
    private int folderId;
    private int mAction;
    private String newName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_collect_card);
        ButterKnife.bind(this);

        initData();
        initRecyclerView();
        getCollectCard();

        mTvToolbarRight.setVisibility(View.VISIBLE);
        mTvToolbarRight.setText("编辑");
        mEtEditCollectFolder.addTextChangedListener(new AddCollectFolderTextWatcher());
    }

    private void initData() {
        String name = getIntent().getStringExtra("name");
        folderId = getIntent().getIntExtra("id",0);
        initToolBar(name);
        mEtEditCollectFolder.setHint(name);
    }

    @Override
    public void onBackPressed() {
        if (mLlCollectFolderEdit.getVisibility() == View.VISIBLE){
            onMIvCommonCloseClicked();
        }else if (mLlCollectCardEdit.getVisibility() == View.VISIBLE){
            mCheckedList.clear();
            mLlCollectCardEdit.setVisibility(View.GONE);
            mTvToolbarRight.setText("编辑");
            mAdapter.setDelete(false);
            mAdapter.setOnItemClickListener(this);
            mAdapter.notifyDataSetChanged();
        }else {
            super.onBackPressed();
        }
    }

    @Override
    protected CollectCardPresenter createPresenter() {
        return new CollectCardPresenter(this);
    }

    @Override
    public void onLoadMoreRequested() {
        getCollectCard();
    }

    @Override
    public void getDataSuccess(List<CollectCardBean> list) {
        if (page == 1){
            mAdapter.notifyDataSetChanged();
        }
        page++;
        mAdapter.addData(list);
        if (list.size() < Constants.PAGE_SIZE){
            mAdapter.loadMoreEnd(false);
        }else {
            mAdapter.loadMoreComplete();
        }
        if (mAdapter.getData().size() == 0){
            //占空图
            View view = View.inflate(mActivity, R.layout.view_no_data, null);
            TextView textView = (TextView) view.findViewById(R.id.tv_no_data);
            textView.setText(R.string.no_collect_card);
            mAdapter.setEmptyView(view);
        }
    }

    @Override
    public void toastFail(String msg) {
        super.toastFail(msg);
        mAdapter.loadMoreFail();
    }

    @Override
    public void postSuccess(PostBean bean) {
        if (mAction == ACTION_DELETE){
            mAdapter.getData().removeAll(mCheckedList);
            mAdapter.notifyDataSetChanged();
            mCheckedList.clear();
        }else if (mAction == ACTION_CHANGE){
            initToolBar(newName);
            mEtEditCollectFolder.setHint(newName);
            EventBus.getDefault().post(new ChangeCollectFolderNameEvent(folderId, newName));
        }
    }

    @Override
    public void postFail(String msg) {
        toastShow(msg);
    }

    private void getCollectCard() {
        mPresenter.getCollectCard(folderId, page, Constants.PAGE_SIZE);
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
                mAdapter.setDelete(true);
                mAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
                    @Override
                    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {

                    }
                });
                mAdapter.notifyDataSetChanged();
                break;
            case "取消":
                mLlCollectCardEdit.setVisibility(View.GONE);
                mTvToolbarRight.setText("编辑");
                mAdapter.setDelete(false);
                mAdapter.setOnItemClickListener(this);
                mAdapter.notifyDataSetChanged();
                break;
            case "删除":
                mAction = ACTION_DELETE;
                List<Integer> idList = new ArrayList<>();
                for (CollectCardBean bean : mCheckedList) {
                    idList.add(bean.getId());
                }
                mPresenter.deleteCollectCard(folderId,idList);
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
        mEtEditCollectFolder.setText("");
        mLlCollectCardEdit.setVisibility(View.VISIBLE);
        Utils.closeInputMethod(mEtEditCollectFolder);
    }

    @OnClick(R.id.iv_dialog_collect_folder_confirm)
    public void onMIvDialogCollectFolderConfirmClicked() {
        newName = mEtEditCollectFolder.getText().toString();
        Animation animation = AnimationUtils.loadAnimation(this, R.anim.anim_add_collect_folder_close);
        mLlCollectFolderEdit.startAnimation(animation);
        mLlCollectFolderEdit.setVisibility(View.GONE);
        mEtEditCollectFolder.setText("");
        mLlCollectCardEdit.setVisibility(View.VISIBLE);
        Utils.closeInputMethod(mEtEditCollectFolder);

        mAction = ACTION_CHANGE;
        mPresenter.changeCollectFolderName(folderId,newName);
    }

    @OnClick(R.id.rl_collect_folder_edit)
    public void onMRlCollectFolderEditClicked() {
        onMIvCommonCloseClicked();
    }

    @OnClick(R.id.ll_collect_folder_edit)
    public void onMLlCollectFolderEditClicked() {
        //保持为空，覆盖掉底下的点击事件监听
    }

    private void initRecyclerView() {
        mAdapter = new CollectCardAdapter(new ArrayList<CollectCardBean>(), this);
        mAdapter.setLoadMoreView(new CustomLoadMoreView());
        mAdapter.setOnLoadMoreListener(this, mRvCollectCard);
        mAdapter.setOnItemClickListener(this);
        mRvCollectCard.setLayoutManager(new GridLayoutManager(getContext(), 2));
        mRvCollectCard.setAdapter(mAdapter);
        mRvCollectCard.addItemDecoration(new CollectFolderItemDecoration());
        mAdapter.notifyDataSetChanged();
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
