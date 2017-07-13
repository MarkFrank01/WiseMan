package com.zxcx.shitang.ui.my.collect.collectCard;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
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
    @BindView(R.id.et_toolbar_title_edit)
    EditText mEtToolbarTitleEdit;
    @BindView(R.id.iv_toolbar_edit)
    ImageView mIvToolbarEdit;

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
        mEtToolbarTitleEdit.setHint("笔记本");

        getData();
        initRecyclerView();

        mTvToolbarRight.setVisibility(View.VISIBLE);
        mTvToolbarRight.setText("编辑");
        mEtToolbarTitleEdit.setOnEditorActionListener(new ToolbarEditListener());
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

    @OnClick(R.id.iv_toolbar_edit)
    public void onEditClicked() {
        mEtToolbarTitleEdit.setVisibility(View.VISIBLE);
        Utils.showInputMethod(mEtToolbarTitleEdit);
        mToolbarTitle.setVisibility(View.GONE);
        mIvToolbarEdit.setVisibility(View.GONE);
    }

    private class ToolbarEditListener implements TextView.OnEditorActionListener {
        @Override
        public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
            //此处会响应2次 分别为ACTION_DOWN和ACTION_UP
            if (actionId == EditorInfo.IME_ACTION_DONE
                    || (event != null && KeyEvent.KEYCODE_ENTER == event.getKeyCode() && KeyEvent.ACTION_DOWN == event.getAction())) {

                mEtToolbarTitleEdit.setVisibility(View.GONE);
                mToolbarTitle.setVisibility(View.VISIBLE);
                mIvToolbarEdit.setVisibility(View.VISIBLE);
                mToolbarTitle.setText(mEtToolbarTitleEdit.getText());
                mEtToolbarTitleEdit.setHint(mEtToolbarTitleEdit.getText());
                Utils.closeInputMethod(mEtToolbarTitleEdit);
                return true;
            }
            return false;
        }
    }
}
