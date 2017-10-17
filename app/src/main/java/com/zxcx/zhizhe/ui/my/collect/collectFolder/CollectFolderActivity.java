package com.zxcx.zhizhe.ui.my.collect.collectFolder;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.kingja.loadsir.core.LoadSir;
import com.zxcx.zhizhe.R;
import com.zxcx.zhizhe.event.ChangeCollectFolderNameEvent;
import com.zxcx.zhizhe.event.DeleteConfirmEvent;
import com.zxcx.zhizhe.loadCallback.LoadingCallback;
import com.zxcx.zhizhe.loadCallback.NetworkErrorCallback;
import com.zxcx.zhizhe.mvpBase.MvpActivity;
import com.zxcx.zhizhe.ui.my.collect.collectCard.CollectCardActivity;
import com.zxcx.zhizhe.ui.my.collect.collectFolder.adapter.CollectFolderAdapter;
import com.zxcx.zhizhe.ui.my.collect.collectFolder.itemDecoration.CollectFolderItemDecoration;
import com.zxcx.zhizhe.utils.Constants;
import com.zxcx.zhizhe.utils.Utils;
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

import static com.zxcx.zhizhe.App.getContext;

public class CollectFolderActivity extends MvpActivity<CollectFolderPresenter> implements CollectFolderContract.View,
        BaseQuickAdapter.RequestLoadMoreListener, CollectFolderAdapter.CollectFolderCheckListener, BaseQuickAdapter.OnItemClickListener {

    private static final int ACTION_DELETE = 100;
    private static final int ACTION_ADD = 101;

    @BindView(R.id.rv_collect_folder)
    RecyclerView mRvCollectFolder;
    @BindView(R.id.tv_toolbar_right)
    TextView mTvToolbarRight;
    @BindView(R.id.ll_collect_folder)
    LinearLayout mLlCollectFolder;
    @BindView(R.id.et_dialog_add_collect_folder)
    EditText mEtDialogAddCollectFolder;
    @BindView(R.id.ll_collect_folder_add)
    LinearLayout mLlCollectFolderAdd;
    @BindView(R.id.iv_dialog_collect_folder_confirm)
    ImageView mIvDialogCollectFolderConfirm;
    private CollectFolderAdapter mAdapter;
    private List<CollectFolderBean> mCheckedList = new ArrayList<>();
    private int page = 0;
    private int mAction;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_collect_folder);
        ButterKnife.bind(this);
        EventBus.getDefault().register(this);
        initToolBar(R.string.tltle_collect_folder);

        initRecyclerView();
        getCollectFolder();

        mIvDialogCollectFolderConfirm.setEnabled(false);
        mTvToolbarRight.setVisibility(View.VISIBLE);
        mTvToolbarRight.setText("编辑");
        mEtDialogAddCollectFolder.addTextChangedListener(new AddCollectFolderTextWatcher());

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
        if (mLlCollectFolderAdd.getVisibility() == View.VISIBLE){
            onMIvCommonCloseClicked();
        }else if (mLlCollectFolder.getVisibility() == View.VISIBLE){
            mCheckedList.clear();
            mLlCollectFolder.setVisibility(View.GONE);
            mTvToolbarRight.setText("编辑");
            mAdapter.setDelete(false);
            mAdapter.setOnItemClickListener(this);
            mAdapter.notifyDataSetChanged();
        }else {
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
        mPresenter.getCollectFolder(page,Constants.PAGE_SIZE);
    }

    @Override
    public void onLoadMoreRequested() {
        getCollectFolder();
    }

    @Override
    public void getDataSuccess(List<CollectFolderBean> list) {
        if (page == 0){
            mAdapter.setNewData(list);
            loadService.showSuccess();
        }else {
            mAdapter.addData(list);
        }
        page++;
        if (list.size() < Constants.PAGE_SIZE){
            mAdapter.loadMoreEnd(false);
        }else {
            mAdapter.loadMoreComplete();
        }
        if (mAdapter.getData().size() == 0){
            //占空图
            View view = LayoutInflater.from(mActivity).inflate(R.layout.view_no_data, null);
            TextView textView = (TextView) view.findViewById(R.id.tv_no_data);
            textView.setText(R.string.no_collect_folder);
            mAdapter.setEmptyView(view);
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
        if (mAction == ACTION_DELETE){
            mAdapter.getData().removeAll(mCheckedList);
            mAdapter.notifyDataSetChanged();
            mCheckedList.clear();
        }else if (mAction == ACTION_ADD){
            page = 0;
            getCollectFolder();
        }
    }

    @Override
    public void postFail(String msg) {
        toastShow(msg);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(DeleteConfirmEvent event) {
        mAction = ACTION_DELETE;
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
            if (bean.getId() == event.getId()){
                bean.setName(event.getName());
                mAdapter.notifyItemChanged(list.indexOf(bean));
            }
        }
    }

    @OnClick(R.id.ll_collect_folder)
    public void onMLlCollectFolderClicked() {
        Animation animation = AnimationUtils.loadAnimation(this, R.anim.anim_add_collect_folder_open);
        mLlCollectFolderAdd.startAnimation(animation);
        mLlCollectFolderAdd.setVisibility(View.VISIBLE);
        mLlCollectFolder.setVisibility(View.GONE);
        Utils.showInputMethod(mEtDialogAddCollectFolder);
    }

    @OnClick(R.id.iv_common_close)
    public void onMIvCommonCloseClicked() {
        Animation animation = AnimationUtils.loadAnimation(this, R.anim.anim_add_collect_folder_close);
        mLlCollectFolderAdd.startAnimation(animation);
        mLlCollectFolderAdd.setVisibility(View.GONE);
        mEtDialogAddCollectFolder.setText("");
        mLlCollectFolder.setVisibility(View.VISIBLE);
        Utils.closeInputMethod(mEtDialogAddCollectFolder);
    }

    @OnClick(R.id.iv_dialog_collect_folder_confirm)
    public void onMIvDialogCollectFolderConfirmClicked() {
        String name = mEtDialogAddCollectFolder.getText().toString();
        Animation animation = AnimationUtils.loadAnimation(this, R.anim.anim_add_collect_folder_close);
        mLlCollectFolderAdd.startAnimation(animation);
        mLlCollectFolderAdd.setVisibility(View.GONE);
        mEtDialogAddCollectFolder.setText("");
        mLlCollectFolder.setVisibility(View.VISIBLE);
        Utils.closeInputMethod(mEtDialogAddCollectFolder);

        mAction = ACTION_ADD;
        mPresenter.addCollectFolder(name);
    }

    @OnClick(R.id.ll_collect_folder_add)
    public void onMLlCollectFolderAddClicked() {
        onMIvCommonCloseClicked();
    }

    @OnClick(R.id.rl_collect_folder_add)
    public void onMRlCollectFolderAddClicked() {
        //保持为空，覆盖掉底下的点击事件监听
    }

    @OnClick(R.id.tv_toolbar_right)
    public void onViewClicked() {
        switch (mTvToolbarRight.getText().toString()) {
            case "编辑":
                mLlCollectFolder.setVisibility(View.VISIBLE);
                mTvToolbarRight.setText("取消");
                mAdapter.setDelete(true);
                mAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
                    @Override
                    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                        //将整个item的点击事件置空，避免点击其他区域进入收藏夹内
                    }
                });
                mAdapter.notifyDataSetChanged();
                break;
            case "取消":
                mLlCollectFolder.setVisibility(View.GONE);
                mTvToolbarRight.setText("编辑");
                mCheckedList.clear();
                mAdapter.setDelete(false);
                mAdapter.setOnItemClickListener(this);
                mAdapter.notifyDataSetChanged();
                break;
            case "删除":
                if (mCheckedList.size() == 0){
                    toastShow("未选中收藏夹！");
                }else {
                    DeleteConfirmDialog dialog = new DeleteConfirmDialog();
                    dialog.show(getFragmentManager(),"");
                }
                break;
        }
    }

    @Override
    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
        CollectFolderBean bean = (CollectFolderBean) adapter.getData().get(position);
        Intent intent = new Intent(this, CollectCardActivity.class);
        intent.putExtra("id",bean.getId());
        intent.putExtra("name",bean.getName());
        startActivity(intent);
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
        mRvCollectFolder.setLayoutManager(new GridLayoutManager(getContext(), 2));
        mRvCollectFolder.setAdapter(mAdapter);
        mRvCollectFolder.addItemDecoration(new CollectFolderItemDecoration());
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
                mIvDialogCollectFolderConfirm.setEnabled(true);
            } else {
                mIvDialogCollectFolderConfirm.setEnabled(false);
            }
        }
    }
}
