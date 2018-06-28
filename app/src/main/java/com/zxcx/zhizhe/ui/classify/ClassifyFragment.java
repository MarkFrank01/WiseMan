package com.zxcx.zhizhe.ui.classify;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.kingja.loadsir.callback.Callback;
import com.kingja.loadsir.core.LoadSir;
import com.zxcx.zhizhe.R;
import com.zxcx.zhizhe.event.ClassifyClickRefreshEvent;
import com.zxcx.zhizhe.loadCallback.ClassifyLoadingCallback;
import com.zxcx.zhizhe.loadCallback.LoginTimeoutCallback;
import com.zxcx.zhizhe.loadCallback.NetworkErrorCallback;
import com.zxcx.zhizhe.mvpBase.MvpFragment;
import com.zxcx.zhizhe.ui.card.cardBag.CardBagActivity;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class ClassifyFragment extends MvpFragment<ClassifyPresenter> implements
	ClassifyContract.View,
	SwipeRefreshLayout.OnRefreshListener, BaseQuickAdapter.OnItemChildClickListener {

	@BindView(R.id.rv_classify)
	RecyclerView mRvClassify;
	Unbinder unbinder;

	private ClassifyAdapter mAdapter;
	private GridLayoutManager manager;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
		Bundle savedInstanceState) {
		View root = inflater.inflate(R.layout.fragment_classify, container, false);
		unbinder = ButterKnife.bind(this, root);

		LoadSir loadSir = new LoadSir.Builder()
			.addCallback(new ClassifyLoadingCallback())
			.addCallback(new LoginTimeoutCallback())
			.addCallback(new NetworkErrorCallback())
			.setDefaultCallback(ClassifyLoadingCallback.class)
			.build();
		loadService = loadSir.register(root, new Callback.OnReloadListener() {
			@Override
			public void onReload(View v) {
				loadService.showCallback(ClassifyLoadingCallback.class);
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
		if (hidden) {
			EventBus.getDefault().unregister(this);
		} else {
			EventBus.getDefault().register(this);
		}
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		initRecyclerView();
		getClassify();
	}

	@Override
	public void onDestroyView() {
		super.onDestroyView();
		unbinder.unbind();
	}

	@Override
	public void clearLeaks() {
		loadService = null;
		mAdapter = null;
	}

	@Subscribe(threadMode = ThreadMode.MAIN)
	public void onMessageEvent(ClassifyClickRefreshEvent event) {
		mRvClassify.scrollToPosition(0);
		onRefresh();
	}

	@Override
	public void onRefresh() {
		getClassify();
	}

	@Override
	public void getDataSuccess(List<ClassifyBean> list) {
		loadService.showSuccess();
		for (ClassifyBean bean : list) {
			mAdapter.getData().add(bean);
			mAdapter.getData().addAll(bean.getDataList());
		}
		mAdapter.notifyDataSetChanged();
		if (mAdapter.getData().size() == 0) {
			//占空图
		}
	}

	@Override
	public void toastFail(String msg) {
		super.toastFail(msg);
		loadService.showCallback(ClassifyLoadingCallback.class);
	}

	private void getClassify() {
		mPresenter.getClassify();
	}

	private void initRecyclerView() {
		mAdapter = new ClassifyAdapter(new ArrayList<>());
		mAdapter.setOnItemChildClickListener(this);
		manager = new GridLayoutManager(getContext(), 3);
		manager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
			@Override
			public int getSpanSize(int position) {
				if (mAdapter.getItemViewType(position) == ClassifyCardBean.TYPE_CARD_BAG) {
					return 1;
				} else {
					return manager.getSpanCount();
				}
			}
		});
		mRvClassify.setAdapter(mAdapter);
		mRvClassify.setLayoutManager(manager);
		mRvClassify.addItemDecoration(new ClassifyItemDecoration());
	}

	@Override
	public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
		switch (view.getId()) {
			case R.id.rl_item_classify:
				ClassifyCardBean bean = (ClassifyCardBean) adapter.getData().get(position);
				Intent intent = new Intent(mActivity, CardBagActivity.class);
				intent.putExtra("id", bean.getId());
				intent.putExtra("name", bean.getName());
				startActivity(intent);
				break;
		}
	}
}