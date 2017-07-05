package com.zxcx.shitang.ui.my.collect.collectCard;

import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.zxcx.shitang.R;
import com.zxcx.shitang.mvpBase.MvpActivity;
import com.zxcx.shitang.ui.my.collect.collectCard.adapter.CollectCardAdapter;
import com.zxcx.shitang.ui.my.collect.collectFolder.itemDecoration.CollectFolderItemDecoration;
import com.zxcx.shitang.widget.CustomLoadMoreView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.zxcx.shitang.App.getContext;

public class CollectCardActivity extends MvpActivity<CollectCardPresenter> implements CollectCardContract.View,
        BaseQuickAdapter.RequestLoadMoreListener,  CollectCardAdapter.CollectCardCheckListener {

    @BindView(R.id.tv_toolbar_right)
    TextView mTvToolbarRight;
    @BindView(R.id.rv_collect_card)
    RecyclerView mRvCollectCard;

    private CollectCardAdapter mCollectCardAdapter;
    private List<CollectCardBean> mList = new ArrayList<>();
    private List<CollectCardBean> mCheckedList = new ArrayList<>();
    private boolean isErr = false;
    private int TOTAL_COUNTER = 20;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_collect_folder);
        ButterKnife.bind(this);
        initToolBar(R.string.tltle_collect_folder);

        getData();
        initRecyclerView();

        mTvToolbarRight.setVisibility(View.VISIBLE);
        mTvToolbarRight.setText("编辑");
    }

    private void initRecyclerView() {
        mCollectCardAdapter = new CollectCardAdapter(mList, this);
        mCollectCardAdapter.setLoadMoreView(new CustomLoadMoreView());
        mCollectCardAdapter.setOnLoadMoreListener(this, mRvCollectCard);
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
        if (mCheckedList.size()>0){
            mTvToolbarRight.setText("删除");
        }else {
            mTvToolbarRight.setText("取消");
        }
    }

    @OnClick(R.id.tv_toolbar_right)
    public void onViewClicked() {
        switch (mTvToolbarRight.getText().toString()){
            case "编辑":
                mTvToolbarRight.setText("取消");
                mCollectCardAdapter.setDelete(true);
                mCollectCardAdapter.notifyDataSetChanged();
                break;
            case "取消":
                mTvToolbarRight.setText("编辑");
                mCollectCardAdapter.setDelete(false);
                mCollectCardAdapter.notifyDataSetChanged();
                break;
            case "删除":
                mList.removeAll(mCheckedList);
                mCollectCardAdapter.notifyDataSetChanged();
                mCheckedList.clear();
                break;
        }
    }
}
