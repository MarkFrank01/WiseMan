package com.zxcx.shitang.ui.card.card.collectDialog;

import android.app.DialogFragment;
import android.content.Context;
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
import com.zxcx.shitang.event.CollectSuccessEvent;
import com.zxcx.shitang.ui.home.hot.HotBean;
import com.zxcx.shitang.utils.ScreenUtils;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * Created by anm on 2017/7/4.
 */

public class SelectCollectFolderDialog extends DialogFragment {

    @BindView(R.id.rv_dialog_select_collect_folder)
    RecyclerView mRvDialogSelectCollectFolder;
    Unbinder unbinder;

    private SelectCollectFolderAdapter mAdapter;
    private List<HotBean> mList = new ArrayList<>();
    private Context mContext;

    public SelectCollectFolderDialog() {
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        View view = inflater.inflate(R.layout.dialog_select_collect_folder, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getData();
    }

    @Override
    public void onStart() {
        super.onStart();

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
    }

    private void initRecyclerView() {

        LinearLayoutManager hotCardBagLayoutManager = new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false);

        mAdapter = new SelectCollectFolderAdapter(mList);
        mAdapter.setOnItemClickListener(new CollectFolderItemClickListener());
        mRvDialogSelectCollectFolder.setLayoutManager(hotCardBagLayoutManager);
        mRvDialogSelectCollectFolder.setAdapter(mAdapter);

    }

    private void getData() {
        for (int i = 0; i < 10; i++) {
            mList.add(new HotBean());
        }
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

    private class CollectFolderItemClickListener implements BaseQuickAdapter.OnItemClickListener {

        @Override
        public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
            EventBus.getDefault().post(new CollectSuccessEvent());
            SelectCollectFolderDialog.this.dismiss();
        }
    }
}
