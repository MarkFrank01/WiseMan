package com.zxcx.shitang.ui.welcome;

import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.zxcx.shitang.R;
import com.zxcx.shitang.utils.Utils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class GuidePageActivity extends AppCompatActivity {

    /** 引导页面的控件 */
    @BindView(R.id.viewPager)
    ViewPager mViewPager;
    /**
     * 5张引导页面的图片
     */
    private int[] mImgIds = new int[]{R.mipmap.guide_1, R.mipmap.guide_2,
            R.mipmap.guide_3, R.mipmap.guide_4, R.mipmap.guide_5};
    /**
     * 图片资源容器
     */
    private List<ImageView> mImageViews = new ArrayList<>();


    private ImageView[] indicator_imgs = new ImageView[5];//存放引到图片数组
    private boolean isScrolling;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guide_page);
        ButterKnife.bind(this);

        initImgData();

        if (isFirstLaunchApp()) {
            initIndicator();
            initViewPager();
        } else {
            jumpToIndexActivity();
        }
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
     * 判断是否是第一次启动App
     *
     * @return 是-true 否-false
     */
    private boolean isFirstLaunchApp() {
        return Utils.getIsFirstLaunchApp(this);
    }


    /**
     * 跳转到App主界面
     */
    private void jumpToIndexActivity() {

        Utils.setIsFirstLaunchApp(GuidePageActivity.this,false);

        /*Intent intent = new Intent(this, WelcomeActivity.class);
        startActivity(intent);*/

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
                // 改变所有导航的背景图片为：未选中
                for (int i = 0; i < indicator_imgs.length; i++) {

                    indicator_imgs[i].setBackgroundResource(R.mipmap.white);

                }

                // 改变当前背景图片为：选中
                indicator_imgs[position].setBackgroundResource(R.mipmap.black);


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

    /**
     * 初始化引导图标
     * 动态创建多个小圆点，然后组装到线性布局里
     */
    private void initIndicator(){

        ImageView imgView;
        View v = findViewById(R.id.indicator);// 线性水平布局，负责动态调整导航图标

        for (int i = 0; i < indicator_imgs.length; i++) {
            imgView = new ImageView(this);
            LinearLayout.LayoutParams params_linear = new LinearLayout.LayoutParams(20,20);
            params_linear.setMargins(7, 10, 7, 10);
            imgView.setLayoutParams(params_linear);
            indicator_imgs[i] = imgView;

            if (i == 0) { // 初始化第一个为选中状态

                indicator_imgs[i].setBackgroundResource(R.mipmap.black);
            } else {
                indicator_imgs[i].setBackgroundResource(R.mipmap.white);
            }
            ((ViewGroup)v).addView(indicator_imgs[i]);
        }

    }
}
