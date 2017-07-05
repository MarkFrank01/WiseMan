package com.zxcx.shitang.ui.card.card.collectDialog;

import android.app.DialogFragment;
import android.app.FragmentManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.zxcx.shitang.R;
import com.zxcx.shitang.ui.card.cardBag.CardBagActivity;
import com.zxcx.shitang.ui.home.hot.HotBean;

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
        mContext = getActivity();
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

        initRecyclerView();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick(R.id.iv_dialog_collect_folder_add)
    public void onViewClicked() {
        FragmentManager fragmentManager = getActivity().getFragmentManager();

    }

    private void initRecyclerView() {

        LinearLayoutManager hotCardBagLayoutManager = new LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false);
        
        mAdapter = new SelectCollectFolderAdapter(mList);
        mAdapter.setOnItemClickListener(new CollectFolderItemClickListener(mContext));
        mRvDialogSelectCollectFolder.setLayoutManager(hotCardBagLayoutManager);
        mRvDialogSelectCollectFolder.setAdapter(mAdapter);
        
    }

    private void getData() {
        for (int i = 0; i < 10; i++) {
            mList.add(new HotBean());
        }
    }

    static class CollectFolderItemClickListener implements BaseQuickAdapter.OnItemClickListener{

        private Context mContext;

        public CollectFolderItemClickListener(Context context) {
            mContext  = context;
        }

        @Override
        public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
            Intent intent = new Intent(mContext, CardBagActivity.class);
            mContext.startActivity(intent);
        }
    }
}
