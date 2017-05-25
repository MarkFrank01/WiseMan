package com.zxcx.shitang.ui;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.RadioButton;

import com.zxcx.shitang.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.home_fragment_content)
    FrameLayout mHomeFragmentContent;
    @BindView(R.id.home_tab_home)
    RadioButton mHomeTabHome;
    @BindView(R.id.home_tab_all)
    RadioButton mHomeTabAll;
    @BindView(R.id.home_tab_note)
    RadioButton mHomeTabNote;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
    }

    @OnClick({R.id.home_tab_home, R.id.home_tab_all, R.id.home_tab_note})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.home_tab_home:
                break;
            case R.id.home_tab_all:
                break;
            case R.id.home_tab_note:
                break;
        }
    }
}
