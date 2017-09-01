package com.zxcx.shitang.ui.my.collect.collectFolder;

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
import com.zxcx.shitang.event.DeleteConfirmEvent;
import com.zxcx.shitang.mvpBase.MvpActivity;
import com.zxcx.shitang.ui.my.collect.collectCard.CollectCardActivity;
import com.zxcx.shitang.ui.my.collect.collectFolder.adapter.CollectFolderAdapter;
import com.zxcx.shitang.ui.my.collect.collectFolder.itemDecoration.CollectFolderItemDecoration;
import com.zxcx.shitang.utils.Constants;
import com.zxcx.shitang.utils.SVTSConstants;
import com.zxcx.shitang.utils.SharedPreferencesUtil;
import com.zxcx.shitang.utils.Utils;
import com.zxcx.shitang.widget.CustomLoadMoreView;
import com.zxcx.shitang.widget.DeleteConfirmDialog;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.zxcx.shitang.App.getContext;

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
    private CollectFolderAdapter mCollectFolderAdapter;
    private List<CollectFolderBean> mList = new ArrayList<>();
    private List<CollectFolderBean> mCheckedList = new ArrayList<>();
    private int page = 1;
    private int mAction;
    private int mUserId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_collect_folder);
        ButterKnife.bind(this);
        EventBus.getDefault().register(this);
        initToolBar(R.string.tltle_collect_folder);

        mUserId = SharedPreferencesUtil.getInt(SVTSConstants.userId,0);
        getCollectFolder();
        initRecyclerView();

        mTvToolbarRight.setVisibility(View.VISIBLE);
        mTvToolbarRight.setText("编辑");
        mEtDialogAddCollectFolder.addTextChangedListener(new AddCollectFolderTextWatcher());
    }

    @Override
    public void onResume() {
        super.onResume();
        mUserId = SharedPreferencesUtil.getInt(SVTSConstants.userId,0);
    }

    @Override
    public void onBackPressed() {
        if (mLlCollectFolderAdd.getVisibility() == View.VISIBLE){
            onMIvCommonCloseClicked();
        }else if (mLlCollectFolder.getVisibility() == View.VISIBLE){
            mCheckedList.clear();
            mLlCollectFolder.setVisibility(View.GONE);
            mTvToolbarRight.setText("编辑");
            mCollectFolderAdapter.setDelete(false);
            mCollectFolderAdapter.setOnItemClickListener(this);
            mCollectFolderAdapter.notifyDataSetChanged();
        }else {
            super.onBackPressed();
        }
    }

    @Override
    protected CollectFolderPresenter createPresenter() {
        return new CollectFolderPresenter(this);
    }

    private void getCollectFolder() {
        mPresenter.getCollectFolder(mUserId,page,Constants.PAGE_SIZE);
    }

    @Override
    public void onLoadMoreRequested() {
        getCollectFolder();
    }

    @Override
    public void getDataSuccess(List<CollectFolderBean> list) {
        if (page == 1){
            mCollectFolderAdapter.notifyDataSetChanged();
        }
        page++;
        mCollectFolderAdapter.addData(list);
        if (list.size() < Constants.PAGE_SIZE){
            mCollectFolderAdapter.loadMoreEnd(false);
        }else {
            mCollectFolderAdapter.loadMoreComplete();
        }
        if (mCollectFolderAdapter.getData().size() == 0){
            //占空图
        }
    }

    @Override
    public void toastFail(String msg) {
        super.toastFail(msg);
        mCollectFolderAdapter.loadMoreFail();
    }

    @Override
    public void postSuccess() {
        if (mAction == ACTION_DELETE){
            mList.removeAll(mCheckedList);
            mCollectFolderAdapter.notifyDataSetChanged();
            mCheckedList.clear();
        }else if (mAction == ACTION_ADD){
            //// TODO: 2017/8/31  添加收藏夹成功后处理
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
        mPresenter.deleteCollectFolder(mUserId,idList);
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
        mPresenter.addCollectFolder(mUserId,name);
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
                mCollectFolderAdapter.setDelete(true);
                mCollectFolderAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
                    @Override
                    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                        //将整个item的点击事件置空，避免点击其他区域进入收藏夹内
                    }
                });
                mCollectFolderAdapter.notifyDataSetChanged();
                break;
            case "取消":
                mLlCollectFolder.setVisibility(View.GONE);
                mTvToolbarRight.setText("编辑");
                mCheckedList.clear();
                mCollectFolderAdapter.setDelete(false);
                mCollectFolderAdapter.setOnItemClickListener(this);
                mCollectFolderAdapter.notifyDataSetChanged();
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
        mCollectFolderAdapter = new CollectFolderAdapter(mList, this);
        mCollectFolderAdapter.setLoadMoreView(new CustomLoadMoreView());
        mCollectFolderAdapter.setOnLoadMoreListener(this, mRvCollectFolder);
        mCollectFolderAdapter.setOnItemClickListener(this);
        mRvCollectFolder.setLayoutManager(new GridLayoutManager(getContext(), 2));
        mRvCollectFolder.setAdapter(mCollectFolderAdapter);
        mRvCollectFolder.addItemDecoration(new CollectFolderItemDecoration());
        mCollectFolderAdapter.notifyDataSetChanged();
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
