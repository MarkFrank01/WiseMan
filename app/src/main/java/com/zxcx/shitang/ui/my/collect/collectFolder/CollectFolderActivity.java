package com.zxcx.shitang.ui.my.collect.collectFolder;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.zxcx.shitang.R;
import com.zxcx.shitang.mvpBase.MvpActivity;
import com.zxcx.shitang.ui.my.collect.collectCard.CollectCardActivity;
import com.zxcx.shitang.ui.my.collect.collectFolder.adapter.CollectFolderAdapter;
import com.zxcx.shitang.ui.my.collect.collectFolder.itemDecoration.CollectFolderItemDecoration;
import com.zxcx.shitang.widget.CustomLoadMoreView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.zxcx.shitang.App.getContext;

public class CollectFolderActivity extends MvpActivity<CollectFolderPresenter> implements CollectFolderContract.View,
        BaseQuickAdapter.RequestLoadMoreListener, CollectFolderAdapter.CollectFolderCheckListener, BaseQuickAdapter.OnItemClickListener{

    @BindView(R.id.rv_collect_folder)
    RecyclerView mRvCollectFolder;
    @BindView(R.id.tv_toolbar_right)
    TextView mTvToolbarRight;
    private CollectFolderAdapter mCollectFolderAdapter;
    private List<CollectFolderBean> mList = new ArrayList<>();
    private List<CollectFolderBean> mCheckedList = new ArrayList<>();
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
        mCollectFolderAdapter = new CollectFolderAdapter(mList, this);
        mCollectFolderAdapter.setLoadMoreView(new CustomLoadMoreView());
        mCollectFolderAdapter.setOnLoadMoreListener(this, mRvCollectFolder);
        mCollectFolderAdapter.setOnItemClickListener(this);
        mRvCollectFolder.setLayoutManager(new GridLayoutManager(getContext(), 2));
        mRvCollectFolder.setAdapter(mCollectFolderAdapter);
        mRvCollectFolder.addItemDecoration(new CollectFolderItemDecoration());
        mCollectFolderAdapter.notifyDataSetChanged();
    }

    @Override
    protected CollectFolderPresenter createPresenter() {
        return new CollectFolderPresenter(this);
    }

    @Override
    public void getDataSuccess(CollectFolderBean bean) {

    }

    @Override
    public void onLoadMoreRequested() {
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
    }

    private void getData() {
        for (int i = 0; i < 10; i++) {
            mList.add(new CollectFolderBean());
        }
    }

    @Override
    public void onCheckedChanged(CollectFolderBean bean, int position, boolean isChecked) {
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
                mCollectFolderAdapter.setDelete(true);
                mCollectFolderAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
                    @Override
                    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {

                    }
                });
                mCollectFolderAdapter.notifyDataSetChanged();
                break;
            case "取消":
                mTvToolbarRight.setText("编辑");
                mCollectFolderAdapter.setDelete(false);
                mCollectFolderAdapter.setOnItemClickListener(this);
                mCollectFolderAdapter.notifyDataSetChanged();
                break;
            case "删除":
                mList.removeAll(mCheckedList);
                mCollectFolderAdapter.notifyDataSetChanged();
                mCheckedList.clear();
                break;
        }
    }

    @Override
    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
        Intent intent = new Intent(this, CollectCardActivity.class);
        startActivity(intent);
    }
}
