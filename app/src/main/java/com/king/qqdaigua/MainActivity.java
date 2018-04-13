package com.king.qqdaigua;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.king.Fragment1.BlankFragment1;
import com.king.Fragment2.YH_Fragment;
import com.king.util.OperatingSharedPreferences;


public class MainActivity extends AppCompatActivity {
    public static String app_name = "帝王代挂";
    public static String app_subName = " For Android";
    public static String admin_pwd = "123456";
    public static String app_url = "https://www.dkingdg.com/";
    public static String app_buy = "https://www.dkingdg.com/buy/";
    public static String web_jiekou = "http://api.52dg.gg/";

    private Toolbar toolbar;
    private FloatingActionButton fab;
    private int qi_sign = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initv();
        new Handler().postDelayed(new LoadMainTabTask(), 0);

        getSupportFragmentManager().beginTransaction().replace(R.id.content_main, new BlankFragment1())
                .commit();
    }

    /**
     * 基本功能：加载介绍界面或主页面
     * 编写：Jason
     */
    private class LoadMainTabTask implements Runnable {
        @Override
        public void run() {
            qi_sign++;
            boolean opentimes = OperatingSharedPreferences
                    .getSharedPreferences(getApplicationContext());
            // 若为新安装的应用，进入介绍界面,并保存启动次数到SharedPreferences。若不是新安装，则直接进入首页
            if (opentimes) {
                OperatingSharedPreferences
                        .setSharedPreferences(getApplicationContext());
                Intent intent = new Intent();
                intent.setClass(getApplicationContext(),
                        PowerSplashActivity.class);
                startActivity(intent);//打开引导页
            } else {
                Intent intent = new Intent(getApplicationContext(),
                        QiDongActivity.class);
                startActivity(intent);//直接打开首页
            }
        }
    }

    private void initv() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setBackgroundColor(Color.rgb(24, 180, 237));
        toolbar.setTitle("");
        setSupportActionBar(toolbar);

        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setBackgroundTintList(ColorStateList.valueOf(Color.WHITE));
        fab.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Snackbar.make(v, "你已经长按", Snackbar.LENGTH_LONG)
                        .show();
                return true;
            }
        });
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "长按我可进入网页版哦~", Snackbar.LENGTH_SHORT)
                        .show();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
