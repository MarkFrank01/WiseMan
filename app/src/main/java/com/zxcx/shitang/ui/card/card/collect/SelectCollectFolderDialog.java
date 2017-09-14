package com.zxcx.shitang.ui.card.card.collect;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.zxcx.shitang.R;
import com.zxcx.shitang.event.ChangeBirthdayDialogEvent;
import com.zxcx.shitang.event.CollectSuccessEvent;
import com.zxcx.shitang.mvpBase.BaseDialog;
import com.zxcx.shitang.mvpBase.IGetPostPresenter;
import com.zxcx.shitang.mvpBase.PostBean;
import com.zxcx.shitang.retrofit.AppClient;
import com.zxcx.shitang.retrofit.BaseArrayBean;
import com.zxcx.shitang.retrofit.BaseBean;
import com.zxcx.shitang.retrofit.BaseSubscriber;
import com.zxcx.shitang.retrofit.PostSubscriber;
import com.zxcx.shitang.ui.loginAndRegister.login.LoginActivity;
import com.zxcx.shitang.ui.my.collect.collectFolder.CollectFolderBean;
import com.zxcx.shitang.utils.SVTSConstants;
import com.zxcx.shitang.utils.ScreenUtils;
import com.zxcx.shitang.utils.SharedPreferencesUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * Created by anm on 2017/7/4.
 */

public class SelectCollectFolderDialog extends BaseDialog implements IGetPostPresenter<List<CollectFolderBean>,PostBean> {

    @BindView(R.id.rv_dialog_select_collect_folder)
    RecyclerView mRvDialogSelectCollectFolder;
    Unbinder unbinder;

    private SelectCollectFolderAdapter mAdapter;
    private Context mContext;
    private int cardId;
    private int userId = SharedPreferencesUtil.getInt(SVTSConstants.userId,0);

    public SelectCollectFolderDialog() {
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        View view = inflater.inflate(R.layout.dialog_select_collect_folder, container, false);
        unbinder = ButterKnife.bind(this, view);
        EventBus.getDefault().register(this);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getCollectFolder(userId,1,10000);
    }

    @Override
    public void onStart() {
        super.onStart();

        cardId = getArguments().getInt("cardId");
        mContext = getActivity();
        initRecyclerView();

        Window window = getDialog().getWindow();
        window.setBackgroundDrawableResource(R.color.translate);
        window.getDecorView().setPadding(ScreenUtils.dip2px(12), 0, ScreenUtils.dip2px(12), ScreenUtils.dip2px(10));
        WindowManager.LayoutParams lp = getDialog().getWindow().getAttributes();
        lp.gravity = Gravity.BOTTOM;
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = ScreenUtils.dip2px(327);
        window.setAttributes(lp);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
        EventBus.getDefault().unregister(this);
    }

    private void initRecyclerView() {
        LinearLayoutManager hotCardBagLayoutManager = new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false);

        mAdapter = new SelectCollectFolderAdapter(new ArrayList<CollectFolderBean>());
        mAdapter.setOnItemClickListener(new CollectFolderItemClickListener());
        mRvDialogSelectCollectFolder.setLayoutManager(hotCardBagLayoutManager);
        mRvDialogSelectCollectFolder.setAdapter(mAdapter);

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(ChangeBirthdayDialogEvent event) {
        mAdapter.getData().clear();
        mAdapter.notifyDataSetChanged();
        getCollectFolder(userId,1,10000);
    }

    @OnClick(R.id.iv_dialog_collect_folder_close)
    public void onMIvDialogCollectFolderCloseClicked() {
        dismiss();
    }

    @OnClick(R.id.iv_dialog_collect_folder_add)
    public void onMIvDialogCollectFolderAddClicked() {
        AddCollectFolderDialog addCollectFolderDialog = new AddCollectFolderDialog();
        addCollectFolderDialog.show(getFragmentManager(),"");
    }

    public void getCollectFolder(int userId, int page, int pageSize){
        subscription = AppClient.getAPIService().getCollectFolder(userId, page,pageSize)
                .compose(this.<BaseArrayBean<CollectFolderBean>>io_main())
                .compose(this.<CollectFolderBean>handleArrayResult())
                .subscribeWith(new BaseSubscriber<List<CollectFolderBean>>(this) {
                    @Override
                    public void onNext(List<CollectFolderBean> list) {
                        SelectCollectFolderDialog.this.getDataSuccess(list);
                    }
                });
        addSubscription(subscription);
    }

    public void addCollectCard(int userId, int folderId, int cardId){
        subscription = AppClient.getAPIService().addCollectCard(userId, folderId, cardId)
                .compose(this.<BaseBean<PostBean>>io_main())
                .compose(this.<PostBean>handleResult())
                .subscribeWith(new PostSubscriber<PostBean>(this) {
                    @Override
                    public void onNext(PostBean bean) {
                        SelectCollectFolderDialog.this.postSuccess(bean);
                    }
                });
        addSubscription(subscription);
    }

    @Override
    public void getDataSuccess(List<CollectFolderBean> bean) {
        mAdapter.addData(bean);
    }

    @Override
    public void getDataFail(String msg) {
        toastShow(msg);
    }

    @Override
    public void postSuccess(PostBean bean) {
        EventBus.getDefault().post(new CollectSuccessEvent());
        SelectCollectFolderDialog.this.dismiss();
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
        startActivity(new Intent(getActivity(), LoginActivity.class));
    }

    private class CollectFolderItemClickListener implements BaseQuickAdapter.OnItemClickListener {

        @Override
        public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
            CollectFolderBean bean = (CollectFolderBean) adapter.getData().get(position);
            addCollectCard(userId,bean.getId(),cardId);
        }
    }
}
