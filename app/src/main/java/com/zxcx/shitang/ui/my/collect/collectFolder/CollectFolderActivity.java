package com.zxcx.shitang.ui.my.collect.collectFolder;

import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.zxcx.shitang.R;
import com.zxcx.shitang.mvpBase.MvpActivity;
import com.zxcx.shitang.ui.my.collect.collectFolder.adapter.CollectFolderAdapter;
import com.zxcx.shitang.ui.my.collect.collectFolder.itemDecoration.CollectFolderItemDecoration;
import com.zxcx.shitang.widget.CustomLoadMoreView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.zxcx.shitang.App.getContext;

public class CollectFolderActivity extends MvpActivity<CollectFolderPresenter> implements CollectFolderContract.View ,
        BaseQuickAdapter.RequestLoadMoreListener, SwipeRefreshLayout.OnRefreshListener{

    @BindView(R.id.rv_collect_folder)
    RecyclerView mRvCollectFolder;
    @BindView(R.id.srl_collect_folder)
    SwipeRefreshLayout mSrlCollectFolder;
    private CollectFolderAdapter mCollectFolderAdapter;
    private List<CollectFolderBean> mList = new ArrayList<>();
    private boolean isErr = false;
    private int TOTAL_COUNTER = 20;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_collect_folder);
        ButterKnife.bind(this);
        initToolBar(R.string.tltle_collect_bag);

        getData();
        initRecyclerView();

        mSrlCollectFolder.setOnRefreshListener(this);
        mSrlCollectFolder.setColorSchemeColors(ContextCompat.getColor(getContext(), R.color.colorPrimary));
    }

    private void initRecyclerView() {
        mCollectFolderAdapter = new CollectFolderAdapter(mList);
        mCollectFolderAdapter.setLoadMoreView(new CustomLoadMoreView());
        mCollectFolderAdapter.setOnLoadMoreListener(this, mRvCollectFolder);
        mRvCollectFolder.setLayoutManager(new GridLayoutManager(getContext(), 2));
        mRvCollectFolder.setAdapter(mCollectFolderAdapter);
        mRvCollectFolder.addItemDecoration(new CollectFolderItemDecoration());
    }

    @Override
    protected CollectFolderPresenter createPresenter() {
        return new CollectFolderPresenter(this);
    }

    @Override
    public void getDataSuccess(CollectFolderBean bean) {

    }

    @Override
    public void onRefresh() {
        isErr = false;
        mList.clear();
        getData();
        mCollectFolderAdapter.notifyDataSetChanged();
        mCollectFolderAdapter.setEnableLoadMore(false);
        mCollectFolderAdapter.setEnableLoadMore(true);
        if (mSrlCollectFolder.isRefreshing()) {
            mSrlCollectFolder.setRefreshing(false);
        }
    }

    @Override
    public void onLoadMoreRequested() {
        mSrlCollectFolder.setEnabled(false);
        if (mCollectFolderAdapter.getData().size() > TOTAL_COUNTER) {
            mCollectFolderAdapter.loadMoreEnd(false);
        } else {
            if (isErr) {
                getData();
//                mHotCardAdapter.addData();
                mCollectFolderAdapter.notifyDataSetChanged();
                mCollectFolderAdapter.loadMoreComplete();
            } else {
                isErr = true;
                Toast.makeText(getContext(), "网络错误", Toast.LENGTH_LONG).show();
                mCollectFolderAdapter.loadMoreFail();
            }
        }
        mSrlCollectFolder.setEnabled(true);
    }

    private void getData() {
        for (int i = 0; i < 10; i++) {
            mList.add(new CollectFolderBean());
        }
    }
}
