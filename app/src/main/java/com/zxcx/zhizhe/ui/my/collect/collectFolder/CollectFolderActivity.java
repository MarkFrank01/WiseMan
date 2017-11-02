package com.zxcx.zhizhe.ui.my.collect.collectFolder;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.kingja.loadsir.core.LoadSir;
import com.zxcx.zhizhe.R;
import com.zxcx.zhizhe.event.AddCollectFolderEvent;
import com.zxcx.zhizhe.event.ChangeCollectFolderNameEvent;
import com.zxcx.zhizhe.event.DeleteCollectCardEvent;
import com.zxcx.zhizhe.event.DeleteConfirmEvent;
import com.zxcx.zhizhe.loadCallback.LoadingCallback;
import com.zxcx.zhizhe.loadCallback.NetworkErrorCallback;
import com.zxcx.zhizhe.mvpBase.MvpActivity;
import com.zxcx.zhizhe.ui.card.card.collect.AddCollectFolderActivity;
import com.zxcx.zhizhe.ui.card.cardBag.itemDecoration.CardBagCardItemDecoration;
import com.zxcx.zhizhe.ui.my.collect.collectCard.CollectCardActivity;
import com.zxcx.zhizhe.ui.my.collect.collectFolder.adapter.CollectFolderAdapter;
import com.zxcx.zhizhe.utils.Constants;
import com.zxcx.zhizhe.widget.CustomLoadMoreView;
import com.zxcx.zhizhe.widget.DeleteConfirmDialog;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class CollectFolderActivity extends MvpActivity<CollectFolderPresenter> implements CollectFolderContract.View,
        BaseQuickAdapter.RequestLoadMoreListener, CollectFolderAdapter.CollectFolderCheckListener, BaseQuickAdapter.OnItemClickListener {

    @BindView(R.id.tv_toolbar_right)
    TextView mTvToolbarRight;
    @BindView(R.id.rv_collect_folder)
    RecyclerView mRvCollectFolder;


    private CollectFolderAdapter mAdapter;
    private List<CollectFolderBean> mCheckedList = new ArrayList<>();
    private int page = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_collect_folder);
        ButterKnife.bind(this);
        EventBus.getDefault().register(this);
        initToolBar(R.string.tltle_collect_folder);

        initRecyclerView();
        getCollectFolder();

        mTvToolbarRight.setVisibility(View.VISIBLE);
        mTvToolbarRight.setText("编辑");

        initLoadSir();
    }

    public void initLoadSir() {
        loadService = LoadSir.getDefault().register(this, this);
    }

    @Override
    public void onReload(View v) {
        super.onReload(v);
        loadService.showCallback(LoadingCallback.class);
        getCollectFolder();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onBackPressed() {
        if (!"编辑".equals(mTvToolbarRight.getText().toString())) {
            mCheckedList.clear();
            mTvToolbarRight.setText("编辑");
            mAdapter.setDelete(false);
            mAdapter.setOnItemClickListener(this);
            mAdapter.notifyDataSetChanged();
        } else {
            super.onBackPressed();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Override
    protected CollectFolderPresenter createPresenter() {
        return new CollectFolderPresenter(this);
    }

    private void getCollectFolder() {
        mPresenter.getCollectFolder(page, Constants.PAGE_SIZE);
    }

    @Override
    public void onLoadMoreRequested() {
        getCollectFolder();
    }

    @Override
    public void getDataSuccess(List<CollectFolderBean> list) {
        if (page == 0) {
            mAdapter.setNewData(list);
            loadService.showSuccess();
        } else {
            mAdapter.addData(list);
        }
        page++;
        if (list.size() < Constants.PAGE_SIZE) {
            CollectFolderBean bean = new CollectFolderBean();
            bean.setItemType(0);
            mAdapter.addData(bean);
            mAdapter.loadMoreEnd(false);
        } else {
            mAdapter.loadMoreComplete();
        }
        if (mAdapter.getData().size() == 0) {
            //占空图
            CollectFolderBean bean = new CollectFolderBean();
            bean.setItemType(0);
            mAdapter.addData(bean);
        }
    }

    @Override
    public void toastFail(String msg) {
        super.toastFail(msg);
        mAdapter.loadMoreFail();
        if (page == 0) {
            loadService.showCallback(NetworkErrorCallback.class);
        }
    }

    @Override
    public void postSuccess() {
        mAdapter.getData().removeAll(mCheckedList);
        mAdapter.notifyDataSetChanged();
        mCheckedList.clear();

    }

    @Override
    public void postFail(String msg) {
        toastShow(msg);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(DeleteConfirmEvent event) {
        List<Integer> idList = new ArrayList<>();
        for (CollectFolderBean bean : mCheckedList) {
            idList.add(bean.getId());
        }
        mPresenter.deleteCollectFolder(idList);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(ChangeCollectFolderNameEvent event) {
        List<CollectFolderBean> list = mAdapter.getData();
        for (CollectFolderBean bean : list) {
            if (bean.getId() == event.getId()) {
                bean.setName(event.getName());
                mAdapter.notifyItemChanged(list.indexOf(bean));
            }
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(DeleteCollectCardEvent event) {
        CollectFolderBean bean = new CollectFolderBean();
        bean.setId(event.getCollectFolderId());
        int position = mAdapter.getData().indexOf(bean);
        bean = mAdapter.getItem(position);
        bean.setNum(bean.getNum() - event.getCount());
        mAdapter.notifyItemChanged(position);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(AddCollectFolderEvent event) {
        page = 0;
        getCollectFolder();
    }

    @OnClick(R.id.tv_toolbar_right)
    public void onViewClicked() {
        switch (mTvToolbarRight.getText().toString()) {
            case "编辑":
                intoEditMode();
                break;
            case "取消":
                exitEditMode();
                break;
            case "删除":
                if (mCheckedList.size() == 0) {
                    toastShow("未选中收藏夹！");
                } else {
                    DeleteConfirmDialog dialog = new DeleteConfirmDialog();
                    dialog.show(getFragmentManager(), "");
                }
                break;
        }
    }

    @Override
    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
        CollectFolderBean bean = (CollectFolderBean) adapter.getData().get(position);
        switch (bean.getItemType()){
            case 0:
                startActivity(new Intent(mActivity, AddCollectFolderActivity.class));
                break;
            case 1:
                Intent intent = new Intent(this, CollectCardActivity.class);
                intent.putExtra("id", bean.getId());
                intent.putExtra("name", bean.getName());
                startActivity(intent);
                break;
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
        if (mCheckedList.size() > 0) {
            mTvToolbarRight.setText("删除");
        } else {
            mTvToolbarRight.setText("取消");
        }
    }

    private void initRecyclerView() {
        mAdapter = new CollectFolderAdapter(new ArrayList<CollectFolderBean>(), this);
        mAdapter.setLoadMoreView(new CustomLoadMoreView());
        mAdapter.setOnLoadMoreListener(this, mRvCollectFolder);
        mAdapter.setOnItemClickListener(this);
        mRvCollectFolder.setLayoutManager(new StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL));
        mRvCollectFolder.setAdapter(mAdapter);
        mRvCollectFolder.addItemDecoration(new CardBagCardItemDecoration());
        mAdapter.notifyDataSetChanged();
    }

    private void exitEditMode() {
        mTvToolbarRight.setText("编辑");
        mCheckedList.clear();
        mAdapter.setDelete(false);
        mAdapter.setOnItemClickListener(this);
        CollectFolderBean bean = new CollectFolderBean();
        bean.setItemType(0);
        mAdapter.getData().add(bean);
        mAdapter.notifyDataSetChanged();
    }

    private void intoEditMode() {
        mTvToolbarRight.setText("取消");
        mAdapter.setDelete(true);
        mAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                //将整个item的点击事件置空，避免点击其他区域进入收藏夹内
            }
        });
        CollectFolderBean bean = mAdapter.getData().get(mAdapter.getData().size() - 1);
        if (bean.getItemType() == 0) {
            mAdapter.getData().remove(bean);
        }
        mAdapter.notifyDataSetChanged();
    }
}
