package com.zxcx.zhizhe.ui.welcome;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.zxcx.zhizhe.R;
import com.zxcx.zhizhe.mvpBase.BaseActivity;
import com.zxcx.zhizhe.ui.MainActivity;
import com.zxcx.zhizhe.utils.Utils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class GuidePageActivity extends BaseActivity {

    /** 引导页面的控件 */
    @BindView(R.id.viewPager)
    ViewPager mViewPager;
    /**
     * 5张引导页面的图片
     */
    private int[] mImgIds = new int[]{R.drawable.guide_1, R.drawable.guide_2,
            R.drawable.guide_3};
    /**
     * 图片资源容器
     */
    private List<ImageView> mImageViews = new ArrayList<>();

    private boolean isScrolling;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guide_page);
        ButterKnife.bind(this);

        initImgData();

        initViewPager();
    }

    @Override
    public void initStatusBar() {
        //覆盖父类修改状态栏方法
    }

    private void initImgData() {
        for (int imgId : mImgIds) {
            ImageView imageView = new ImageView(getApplicationContext());
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            imageView.setImageResource(imgId);

            mImageViews.add(imageView);
        }
    }


    /**
     * 跳转到App主界面
     */
    private void jumpToIndexActivity() {

        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra("isFirst", true);
        startActivity(intent);

        Utils.setIsFirstLaunchApp(false);

        finish();
    }


    private void initViewPager() {


        mViewPager.setAdapter(new PagerAdapter() {

            @Override
            public Object instantiateItem(ViewGroup container, int position) {
                // 给最后一页引导页添加点击事件
                if (position == mViewPager.getAdapter().getCount() - 1) {
                    View view = LayoutInflater.from(GuidePageActivity.this).inflate(R.layout.guide_5_layout,container,false);
                    ImageView guideButton = (ImageView) view.findViewById(R.id.guide_button);
                    guideButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            jumpToIndexActivity();
                        }
                    });
                    container.addView(view);
                    return view;
                }else {
                    container.addView(mImageViews.get(position));

                    return mImageViews.get(position);
                }

            }

            @Override
            public void destroyItem(ViewGroup container, int position,
                                    Object object) {
                container.removeView(mImageViews.get(position));
            }

            @Override
            public boolean isViewFromObject(View view, Object object) {
                return view == object;
            }

            @Override
            public int getCount() {
                return mImgIds.length;
            }
        });

        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                if(position == mImgIds.length -1 && positionOffset==0 && positionOffsetPixels == 0 && isScrolling){
                    isScrolling = false;
                    jumpToIndexActivity();
                }
            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {
                if (state == 1){
                    isScrolling = true;
                }else {
                    isScrolling = false;
                }
            }
        });
    }
}
