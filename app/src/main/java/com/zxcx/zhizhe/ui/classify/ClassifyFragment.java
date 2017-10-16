package com.zxcx.zhizhe.ui.classify;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.entity.MultiItemEntity;
import com.kingja.loadsir.callback.Callback;
import com.kingja.loadsir.core.LoadService;
import com.kingja.loadsir.core.LoadSir;
import com.zxcx.zhizhe.R;
import com.zxcx.zhizhe.event.ClassifyClickRefreshEvent;
import com.zxcx.zhizhe.loadCallback.ClassifyLoadingCallback;
import com.zxcx.zhizhe.loadCallback.HomeLoadingCallback;
import com.zxcx.zhizhe.loadCallback.NetworkErrorCallback;
import com.zxcx.zhizhe.mvpBase.MvpFragment;
import com.zxcx.zhizhe.ui.card.cardBag.CardBagActivity;
import com.zxcx.zhizhe.ui.search.search.SearchActivity;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class ClassifyFragment extends MvpFragment<ClassifyPresenter> implements ClassifyContract.View,
        SwipeRefreshLayout.OnRefreshListener{

    @BindView(R.id.rv_classify)
    RecyclerView mRvClassify;
    Unbinder unbinder;
    @BindView(R.id.tv_home_search)
    TextView mTvHomeSearch;
    @BindView(R.id.srl_classify)
    SwipeRefreshLayout mSrlClassify;

    private ClassifyAdapter mClassifyAdapter;
    private GridLayoutManager manager;
    private LoadService loadService;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_classify, container, false);
        unbinder = ButterKnife.bind(this, root);

        LoadSir loadSir = new LoadSir.Builder()
                .addCallback(new HomeLoadingCallback())
                .addCallback(new NetworkErrorCallback())
                .setDefaultCallback(HomeLoadingCallback.class)
                .build();
        loadService = loadSir.register(root, new Callback.OnReloadListener() {
            @Override
            public void onReload(View v) {
                loadService.showCallback(HomeLoadingCallback.class);
                onRefresh();
            }
        });
        return loadService.getLoadLayout();
    }

    @Override
    protected ClassifyPresenter createPresenter() {
        return new ClassifyPresenter(this);
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        if (hidden){
            EventBus.getDefault().unregister(this);
        }else {
            EventBus.getDefault().register(this);
        }
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initRecyclerView();
        getClassify();

        mSrlClassify.setOnRefreshListener(this);
        mSrlClassify.setColorSchemeColors(ContextCompat.getColor(getContext(), R.color.button_blue));
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(ClassifyClickRefreshEvent event) {
        mRvClassify.smoothScrollToPosition(0);
        mSrlClassify.setRefreshing(true);
        onRefresh();
    }

    @Override
    public void onRefresh() {
        mClassifyAdapter.getData().clear();
        getClassify();
    }

    @Override
    public void getDataSuccess(List<ClassifyBean> list) {
        loadService.showSuccess();
        if (mSrlClassify.isRefreshing()) {
            mSrlClassify.setRefreshing(false);
        }
        mClassifyAdapter.notifyDataSetChanged();
        for (ClassifyBean bean : list) {
            mClassifyAdapter.addData(bean);
            mClassifyAdapter.addData(bean.getDataList());
        }
        if (mClassifyAdapter.getData().size() == 0){
            //占空图
        }
    }

    @Override
    public void toastFail(String msg) {
        super.toastFail(msg);
        loadService.showCallback(ClassifyLoadingCallback.class);
    }

    @OnClick(R.id.tv_home_search)
    public void onViewClicked() {
        Intent intent = new Intent(getContext(), SearchActivity.class);
        startActivity(intent);
    }

    private void getClassify() {
        mPresenter.getClassify();
    }

    private void initRecyclerView() {
        mClassifyAdapter = new ClassifyAdapter(new ArrayList<MultiItemEntity>());
        mClassifyAdapter.setOnItemChildClickListener(new OnClassifyItemClickListener());
        manager = new GridLayoutManager(getContext(), 4);
        manager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                if(mClassifyAdapter.getItemViewType(position) == ClassifyCardBagBean.TYPE_CARD_BAG){
                    return 1;
                }else {
                    return manager.getSpanCount();
                }
            }
        });
        mRvClassify.setAdapter(mClassifyAdapter);
        mRvClassify.setLayoutManager(manager);
    }

    class OnClassifyItemClickListener implements BaseQuickAdapter.OnItemChildClickListener{

        @Override
        public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
            switch (view.getId()){
                case R.id.rl_item_classify:
                    ClassifyCardBagBean bean = (ClassifyCardBagBean) adapter.getData().get(position);
                    Intent intent = new Intent(getActivity(), CardBagActivity.class);
                    intent.putExtra("id",bean.getId());
                    intent.putExtra("name",bean.getName());
                    startActivity(intent);
                    break;
            }
        }
    }
}