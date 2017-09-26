package com.zxcx.shitang.ui.card.card.collect;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.zxcx.shitang.R;
import com.zxcx.shitang.event.AddCollectFolderDialogEvent;
import com.zxcx.shitang.event.CollectSuccessEvent;
import com.zxcx.shitang.mvpBase.BaseActivity;
import com.zxcx.shitang.mvpBase.BaseRxJava;
import com.zxcx.shitang.mvpBase.IGetPostPresenter;
import com.zxcx.shitang.mvpBase.PostBean;
import com.zxcx.shitang.retrofit.AppClient;
import com.zxcx.shitang.retrofit.BaseArrayBean;
import com.zxcx.shitang.retrofit.BaseBean;
import com.zxcx.shitang.retrofit.BaseSubscriber;
import com.zxcx.shitang.retrofit.PostSubscriber;
import com.zxcx.shitang.ui.loginAndRegister.login.LoginActivity;
import com.zxcx.shitang.ui.my.collect.collectFolder.CollectFolderBean;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by anm on 2017/7/4.
 */

public class SelectCollectFolderActivity extends BaseActivity implements IGetPostPresenter<List<CollectFolderBean>, PostBean> {


    @BindView(R.id.iv_toolbar_back)
    ImageView mIvToolbarBack;
    @BindView(R.id.toolbar_title)
    TextView mToolbarTitle;
    @BindView(R.id.iv_toolbar_right)
    ImageView mIvToolbarRight;
    @BindView(R.id.tv_toolbar_right)
    TextView mTvToolbarRight;
    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.rv_dialog_select_collect_folder)
    RecyclerView mRvDialogSelectCollectFolder;
    private SelectCollectFolderAdapter mAdapter;
    private int cardId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_collect_folder);
        ButterKnife.bind(this);
        initToolBar("修改收藏夹");
        mIvToolbarRight.setVisibility(View.VISIBLE);
        mIvToolbarRight.setImageResource(R.drawable.iv_dialog_collect_folder_add);
        initRecyclerView();
        getCollectFolder(0, 10000);
        EventBus.getDefault().register(this);
        cardId = getIntent().getIntExtra("cardId", 0);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    private void initRecyclerView() {
        LinearLayoutManager hotCardBagLayoutManager = new LinearLayoutManager(mActivity, LinearLayoutManager.VERTICAL, false);

        mAdapter = new SelectCollectFolderAdapter(new ArrayList<CollectFolderBean>());
        mAdapter.setOnItemClickListener(new CollectFolderItemClickListener());
        mRvDialogSelectCollectFolder.setLayoutManager(hotCardBagLayoutManager);
        mRvDialogSelectCollectFolder.setAdapter(mAdapter);

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(AddCollectFolderDialogEvent event) {
        mAdapter.getData().clear();
        mAdapter.notifyDataSetChanged();
        getCollectFolder(0, 10000);
    }

    @OnClick(R.id.iv_toolbar_right)
    public void onMIvDialogCollectFolderAddClicked() {
        startActivity(new Intent(mActivity, AddCollectFolderActivity.class));
    }

    public void getCollectFolder(int page, int pageSize) {
        mDisposable = AppClient.getAPIService().getCollectFolder(page, pageSize)
                .compose(BaseRxJava.<BaseArrayBean<CollectFolderBean>>io_main())
                .compose(BaseRxJava.<CollectFolderBean>handleArrayResult())
                .subscribeWith(new BaseSubscriber<List<CollectFolderBean>>(this) {
                    @Override
                    public void onNext(List<CollectFolderBean> list) {
                        SelectCollectFolderActivity.this.getDataSuccess(list);
                    }
                });
        addSubscription(mDisposable);
    }

    public void addCollectCard(int folderId, int cardId) {
        mDisposable = AppClient.getAPIService().addCollectCard(folderId, cardId)
                .compose(BaseRxJava.<BaseBean>io_main())
                .compose(BaseRxJava.handlePostResult())
                .subscribeWith(new PostSubscriber<BaseBean>(this) {
                    @Override
                    public void onNext(BaseBean bean) {
                        SelectCollectFolderActivity.this.postSuccess(new PostBean());
                    }
                });
        addSubscription(mDisposable);
    }

    @Override
    public void getDataSuccess(List<CollectFolderBean> list) {
        mAdapter.addData(list);
        if (list.size() == 0) {
            View view = View.inflate(mActivity, R.layout.view_no_data, null);
            TextView textView = (TextView) view.findViewById(R.id.tv_no_data);
            textView.setText(R.string.no_collect_folder);
            mAdapter.setEmptyView(view);
        }
    }

    @Override
    public void getDataFail(String msg) {
        toastShow(msg);
    }

    @Override
    public void postSuccess(PostBean bean) {
        EventBus.getDefault().post(new CollectSuccessEvent());
        finish();
    }

    @Override
    public void postFail(String msg) {
        toastShow(msg);
    }

    @Override
    public void showLoading() {

    }

    @Override
    public void hideLoading() {

    }

    @Override
    public void startLogin() {
        startActivity(new Intent(mActivity, LoginActivity.class));
    }

    private class CollectFolderItemClickListener implements BaseQuickAdapter.OnItemClickListener {

        @Override
        public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
            CollectFolderBean bean = (CollectFolderBean) adapter.getData().get(position);
            addCollectCard(bean.getId(), cardId);
        }
    }
}
