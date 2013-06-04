package com.eoe.control;

import java.util.ArrayList;

import com.eoe.control.fragment.MyFragmentPagerAdapter;
import com.eoe.control.fragment.TestFragment;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.TextView;

public class MyFragment extends FragmentActivity{
    private TextView mTabFirst, mTabDynamic, mTabCircle, mTabFriend;
    private ViewPager mPager;
    private ArrayList<Fragment> fragmentsList;
    private ImageView ivBottomLine;
    
    private int currIndex = 0;
    private int bottomLineWidth;
    private int offset = 0;
    private int position_one;
    private int position_two;
    private int position_three;
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
//        setContentView(R.layout.myfragment);  //加载单独的片段
        
        setContentView(R.layout.fragment);
        InitWidth();
        InitTextView();
        InitViewPager();
    }
    
    private void InitWidth() {
        ivBottomLine = (ImageView) findViewById(R.id.iv_bottom_line);
        bottomLineWidth = ivBottomLine.getLayoutParams().width;
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        int screenW = dm.widthPixels;
        offset = (int) ((screenW / 4.0 - bottomLineWidth) / 2);

        position_one = (int) (screenW / 4.0);
        position_two = position_one * 2;
        position_three = position_one * 3;
    }
    
    private void InitTextView() {
    	mTabFirst = (TextView) findViewById(R.id.t_tab_first);
    	mTabDynamic = (TextView) findViewById(R.id.t_tab_dynamic);
    	mTabCircle = (TextView) findViewById(R.id.t_tab_circle);
    	mTabFriend = (TextView) findViewById(R.id.t_tab_friend);

        mTabFirst.setOnClickListener(new MyOnClickListener(0));
        mTabDynamic.setOnClickListener(new MyOnClickListener(1));
        mTabCircle.setOnClickListener(new MyOnClickListener(2));
        mTabFriend.setOnClickListener(new MyOnClickListener(3));
    }
    
    private void InitViewPager() {
        mPager = (ViewPager) findViewById(R.id.vPager);
        fragmentsList = new ArrayList<Fragment>();

        Fragment firstFragment = TestFragment.newInstance("Hello First.this is the first.");
        Fragment dynamicFragment = TestFragment.newInstance("Hello Dynamic.this is dynamic. ");
        Fragment circleFragment=TestFragment.newInstance("Hello Circle.this is circle.");
        Fragment friendFragment=TestFragment.newInstance("Hello Friend.this is firend");

        fragmentsList.add(firstFragment);
        fragmentsList.add(dynamicFragment);
        fragmentsList.add(circleFragment);
        fragmentsList.add(friendFragment);
        
        mPager.setAdapter(new MyFragmentPagerAdapter(getSupportFragmentManager(), fragmentsList));
        mPager.setCurrentItem(0);
        mPager.setOnPageChangeListener(new MyOnPageChangeListener());
    }
    
    public class MyOnClickListener implements View.OnClickListener {
        private int index = 0;

        public MyOnClickListener(int i) {
            index = i;
        }

        @Override
        public void onClick(View v) {
            mPager.setCurrentItem(index);
        }
    };
    
    public class MyOnPageChangeListener implements OnPageChangeListener {

        @Override
        public void onPageSelected(int arg0) {
            Animation animation = null;
            switch (arg0) {
            case 0:
                if (currIndex == 1) {
                    animation = new TranslateAnimation(position_one, 0, 0, 0);
                    mTabDynamic.setTextColor(0xffdddddd);
                } else if (currIndex == 2) {
                    animation = new TranslateAnimation(position_two, 0, 0, 0);
                    mTabCircle.setTextColor(0xffdddddd);
                } else if (currIndex == 3) {
                    animation = new TranslateAnimation(position_three, 0, 0, 0);
                    mTabFriend.setTextColor(0xffdddddd);
                }
                mTabFirst.setTextColor(0xffffffff);
                break;
            case 1:
                if (currIndex == 0) {
                    animation = new TranslateAnimation(offset, position_one, 0, 0);
                    mTabFirst.setTextColor(0xffdddddd);
                } else if (currIndex == 2) {
                    animation = new TranslateAnimation(position_two, position_one, 0, 0);
                    mTabCircle.setTextColor(0xffdddddd);
                } else if (currIndex == 3) {
                    animation = new TranslateAnimation(position_three, position_one, 0, 0);
                    mTabFriend.setTextColor(0xffdddddd);
                }
                mTabDynamic.setTextColor(0xffffffff);
                break;
            case 2:
                if (currIndex == 0) {
                    animation = new TranslateAnimation(offset, position_two, 0, 0);
                    mTabFirst.setTextColor(0xffdddddd);
                } else if (currIndex == 1) {
                    animation = new TranslateAnimation(position_one, position_two, 0, 0);
                    mTabDynamic.setTextColor(0xffdddddd);
                } else if (currIndex == 3) {
                    animation = new TranslateAnimation(position_three, position_two, 0, 0);
                    mTabFriend.setTextColor(0xffdddddd);
                }
                mTabCircle.setTextColor(0xffffffff);
                break;
            case 3:
                if (currIndex == 0) {
                    animation = new TranslateAnimation(offset, position_three, 0, 0);
                    mTabFirst.setTextColor(0xffdddddd);
                } else if (currIndex == 1) {
                    animation = new TranslateAnimation(position_one, position_three, 0, 0);
                    mTabDynamic.setTextColor(0xffdddddd);
                } else if (currIndex == 2) {
                    animation = new TranslateAnimation(position_two, position_three, 0, 0);
                    mTabCircle.setTextColor(0xffdddddd);
                }
                mTabFriend.setTextColor(0xffffffff);
                break;
            }
            currIndex = arg0;
            animation.setFillAfter(true);
            animation.setDuration(300);
            ivBottomLine.startAnimation(animation);
        }

        @Override
        public void onPageScrolled(int arg0, float arg1, int arg2) {
        }

        @Override
        public void onPageScrollStateChanged(int arg0) {
        }
    }
}
