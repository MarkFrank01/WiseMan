package com.zxcx.shitang.ui;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;

import com.chad.library.adapter.base.entity.MultiItemEntity;
import com.zxcx.shitang.R;
import com.zxcx.shitang.ui.allCardBag.AllCardBagAdapter;
import com.zxcx.shitang.ui.allCardBag.AllCardBagBean;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class Main2Activity extends AppCompatActivity {

    @BindView(R.id.rv_all_card_bag)
    RecyclerView mRvAllCardBag;
    private ArrayList<MultiItemEntity> mList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_all_card_bag);
        ButterKnife.bind(this);
        for (int i = 0; i < 10; i++) {
            AllCardBagBean bagBean = new AllCardBagBean();
            bagBean.setType("分类" + i);
            for (int j = 0; j < 4; j++) {
                AllCardBagBean.ContentBean bean = new AllCardBagBean.ContentBean();
                bean.setTitle("标题" + i + j);
                bagBean.addSubItem(bean);
            }
            mList.add(bagBean);
        }
        AllCardBagAdapter adapter = new AllCardBagAdapter(mList);
        mRvAllCardBag.setLayoutManager(new StaggeredGridLayoutManager(3,StaggeredGridLayoutManager.VERTICAL));
        mRvAllCardBag.setAdapter(adapter);
    }

}
