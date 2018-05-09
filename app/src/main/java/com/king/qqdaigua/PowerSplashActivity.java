package com.king.qqdaigua;

import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;

import java.util.ArrayList;

/*
 * 基本功能：展示开机引导页
 * 创建：Jason
 */
public class PowerSplashActivity extends AppCompatActivity {
    ViewPager viewPager;
    ArrayList<View> list;
    ViewGroup main, group;
    ImageView imageView;
    ImageView[] imageViews;
    Button enter;
    private static int c_id = 0;

    private static int start_id = 0;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        LayoutInflater inflater = getLayoutInflater();
        View layout1 = inflater.inflate(R.layout.qdt_1, null);
        View layout2 = inflater.inflate(R.layout.qdt_2, null);
        View layout3 = inflater.inflate(R.layout.qdt_3, null);
        enter = (Button) layout3.findViewById(R.id.enter);
        enter.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        list = new ArrayList<View>();
        list.add(layout1);
        list.add(layout2);
        list.add(layout3);
        imageViews = new ImageView[list.size()];
        ViewGroup main = (ViewGroup) inflater.inflate(R.layout.activity_power_splash, null);
        ViewGroup group = (ViewGroup) main.findViewById(R.id.viewGroup);
        viewPager = (ViewPager) main.findViewById(R.id.viewPager);
        setContentView(main);
        viewPager.setAdapter(new ImageViewAdapter());
        viewPager.setOnPageChangeListener(new MyListener());
        viewPager.setCurrentItem(300);

    }

    class ImageViewAdapter extends PagerAdapter {

        @Override
        public int getCount() {
            return Integer.MAX_VALUE;
        }

        @Override
        public boolean isViewFromObject(View arg0, Object arg1) {
            return arg0 == arg1;
        }

        @Override
        public int getItemPosition(Object object) {
            // TODO Auto-generated method stub
            return super.getItemPosition(object);
        }

        @Override
        public void destroyItem(View arg0, int arg1, Object arg2) {
            // TODO Auto-generated method stub
            //((ViewPager) arg0).removeView(list.get(arg1));
        }

        @Override
        public Object instantiateItem(View arg0, int arg1) {
            // TODO Auto-generated method stub
            try {
                ((ViewPager) arg0).addView(list.get(arg1 % list.size()), 0);
            } catch (Exception e) {
                // TODO: handle exception
            }
            return list.get(arg1 % list.size());
        }

        @Override
        public void restoreState(Parcelable arg0, ClassLoader arg1) {
            // TODO Auto-generated method stub

        }

        @Override
        public Parcelable saveState() {
            // TODO Auto-generated method stub
            return null;
        }

        @Override
        public void startUpdate(View arg0) {
            // TODO Auto-generated method stub

        }

        @Override
        public void finishUpdate(View arg0) {
            // TODO Auto-generated method stub

        }
    }

    class MyListener implements OnPageChangeListener {

        //当滑动状态改变时调用
        @Override
        public void onPageScrollStateChanged(int arg0) {
            // TODO Auto-generated method stub
            //arg0=arg0%list.size();
        }

        //当当前页面被滑动时调用
        @Override
        public void onPageScrolled(int arg0, float arg1, int arg2) {
            // TODO Auto-generated method stub

        }

        //当新的页面被选中时调用
        @Override
        public void onPageSelected(int arg0) {
            Log.e("当新的页面被选中时调用", arg0 + "");
            if (start_id == 0){
                start_id = arg0;
            }
            if (start_id + 3 == arg0){
                finish();
            }
            if (arg0 > 2) {
                arg0 = arg0 % list.size();
            }
            c_id = arg0;
//            for (int i = 0; i < imageViews.length; i++) {
//                imageViews[arg0]
//                        .setBackgroundResource(R.drawable.guide_dot_white);
//                if (arg0 != i) {
//                    imageViews[i]
//                            .setBackgroundResource(R.drawable.guide_dot_black);
//                }
//            }
        }

    }
}