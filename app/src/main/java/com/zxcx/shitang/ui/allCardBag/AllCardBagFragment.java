package com.zxcx.shitang.ui.allCardBag;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.zxcx.shitang.R;
import com.zxcx.shitang.mvpBase.MvpFragment;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class AllCardBagFragment extends MvpFragment<AllCardBagPresenter> implements AllCardBagContract.View {

    @BindView(R.id.rv_all_card_bag)
    RecyclerView mRvAllCardBag;
    Unbinder unbinder;

    private List<AllCardBagBean> mList = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_all_card_bag, container, false);
        unbinder = ButterKnife.bind(this, root);
        return root;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        for (int i = 0; i < 10; i++){
            AllCardBagBean bagBean = new AllCardBagBean();
            bagBean.setType("分类"+i);
            List<AllCardBagBean.ContentBean> list = new ArrayList<>();
            for (int j = 0; j < 4; j++){
                AllCardBagBean.ContentBean bean = new AllCardBagBean.ContentBean();
                bean.setTitle("标题"+j);
                list.add(bean);
            }
            bagBean.setContent(list);
            mList.add(bagBean);
        }/*
        AllCardBagAdapter adapter = new AllCardBagAdapter(mList);
        mRvAllCardBag.setLayoutManager(new GridLayoutManager(this.getContext(),3));
        mRvAllCardBag.setAdapter(adapter);*/
    }

    @Override
    protected AllCardBagPresenter createPresenter() {
        return new AllCardBagPresenter(this);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void getDataSuccess(AllCardBagBean bean) {

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}