package com.zxcx.shitang.ui.classify;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.entity.MultiItemEntity;
import com.zxcx.shitang.R;
import com.zxcx.shitang.mvpBase.MvpFragment;
import com.zxcx.shitang.ui.search.search.SearchActivity;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class ClassifyFragment extends MvpFragment<ClassifyPresenter> implements ClassifyContract.View ,
        BaseQuickAdapter.RequestLoadMoreListener, SwipeRefreshLayout.OnRefreshListener{

    @BindView(R.id.rv_classify)
    RecyclerView mRvClassify;
    Unbinder unbinder;
    @BindView(R.id.tv_home_search)
    TextView mTvHomeSearch;
    @BindView(R.id.srl_classify)
    SwipeRefreshLayout mSrlClassify;

    private List<MultiItemEntity> mList = new ArrayList<>();
    private ClassifyAdapter mClassifyAdapter;
    private int TOTAL_COUNTER = 20;
    private boolean isErr = false;
    private static ClassifyFragment fragment = null;
    private GridLayoutManager manager;

    public static ClassifyFragment newInstance() {
        if (fragment == null) {
            fragment = new ClassifyFragment();
        }
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_classify, container, false);
        unbinder = ButterKnife.bind(this, root);
        return root;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getData();
        initRecyclerView();

        mSrlClassify.setOnRefreshListener(this);
        mSrlClassify.setColorSchemeColors(ContextCompat.getColor(getContext(), R.color.colorPrimary));
    }

    private void getData() {
        for (int i = 0; i < 10; i++) {
            ClassifyBean bagBean = new ClassifyBean();
            bagBean.setType(ClassifyBean.TYPE_CLASSIFY);
            if (i != 0) bagBean.setImgUrl("66");
            mList.add(bagBean);
            for (int j = 0; j < 6; j++) {
                ClassifyBean children = new ClassifyBean();
                children.setType(ClassifyBean.TYPE_CARD_BAG);
                mList.add(children);
            }
        }
    }

    private void initRecyclerView() {
        mClassifyAdapter = new ClassifyAdapter(mList);
        manager = new GridLayoutManager(getContext(), 3);
        manager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                if(mClassifyAdapter.getItemViewType(position) == ClassifyBean.TYPE_CARD_BAG){
                    return 1;
                }else {
                    return manager.getSpanCount();
                }
            }
        });
        mRvClassify.setAdapter(mClassifyAdapter);
        mRvClassify.setLayoutManager(manager);
        mClassifyAdapter.expandAll();
    }

    @Override
    protected ClassifyPresenter createPresenter() {
        return new ClassifyPresenter(this);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void getDataSuccess(ClassifyBean bean) {

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public void onRefresh() {
        isErr = false;
        mClassifyAdapter.setEnableLoadMore(false);
        mClassifyAdapter.setEnableLoadMore(true);
        mList.clear();
        getData();
        mClassifyAdapter.notifyDataSetChanged();
        if (mSrlClassify.isRefreshing()) {
            mSrlClassify.setRefreshing(false);
        }
    }

    @Override
    public void onLoadMoreRequested() {
        mSrlClassify.setEnabled(false);
        if (mClassifyAdapter.getData().size() > TOTAL_COUNTER) {
            mClassifyAdapter.loadMoreEnd(false);
        } else {
            if (isErr) {
                getData();
//                mClassifyAdapter.addData();
                mClassifyAdapter.notifyDataSetChanged();
                mClassifyAdapter.loadMoreComplete();
            } else {
                isErr = true;
                Toast.makeText(getContext(), "网络错误", Toast.LENGTH_LONG).show();
                mClassifyAdapter.loadMoreFail();
            }
        }
        mSrlClassify.setEnabled(true);
    }

    @OnClick(R.id.tv_home_search)
    public void onViewClicked() {
        Intent intent = new Intent(getContext(), SearchActivity.class);
        startActivity(intent);
    }
}