package com.king.qqdaigua;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.king.Caculer_Fragment.caculer_Fragment;
import com.king.Login_Fragment.BlankFragment1;
import com.king.YH_Fragment.update_dialog;
import com.king.util.HttpRequest;
import com.king.util.OperatingSharedPreferences;

import org.json.JSONException;
import org.json.JSONObject;


public class MainActivity extends AppCompatActivity {
    //版本号控制更新提示
    public static String app_ver = "3.3";

    public static String admin_pwd = "123456";
    public static String app_qq = "1776885812";
    public static String app_name = "帝王代挂";
    public static String app_subName = " For Android";
    public static String app_url = "https://www.dkingdg.com/";
    public static String app_buy = "https://www.dkingdg.com/buy/";
    public static String web_jiekou = "http://api.52dg.gg/";
    public static String web_jiekou1 = "http://kkkking.daigua.org/";
    public static String buy_url = "https://www.dkingdg.com/buy/";

    private Toolbar toolbar;
    private FloatingActionButton fab;
    private int qi_sign = 0;
    private String update_sign = "0";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initv();
        new Handler().postDelayed(new LoadMainTabTask(), 0);
        updateCheck();
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
                openURL(app_url);
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
            getSupportFragmentManager().beginTransaction().replace(R.id.content_main, new
                    caculer_Fragment()).commit();
//            runOnUiThread(new Runnable() {
//                @Override
//                public void run() {
//                    Toast.makeText(getApplicationContext(), "等级计算器即将到来", Toast.LENGTH_LONG)
//                            .show();
//                }
//            });
            return true;
        } else if (id == R.id.action_settings1) {
            updateCheck();
            if (update_sign.equals("0")) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getApplicationContext(), "您安装的是最新版本", Toast.LENGTH_LONG)
                                .show();
                    }
                });
            }
        } else if (id == R.id.action_settings2) {
            new about_dialog().show(getSupportFragmentManager(), "");
        }

        return super.onOptionsItemSelected(item);
    }

    public void updateCheck() {
        String post_url = MainActivity.app_url + "ajax/dg" +
                ".php?ajax=true&star=update_ver";
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("type", "update");
            HttpRequest http = new HttpRequest(post_url, jsonObject.toString(), handler);
            http.start();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void openURL(String s) {
        Intent intent = new Intent();
        intent.setAction("android.intent.action.VIEW");
        Uri content_url = Uri.parse(s);
        intent.setData(content_url);
        startActivity(intent);
    }


    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 10) {
                try {
                    JSONObject json = new JSONObject((String) msg.obj);
                    String code = json.getString("code");
                    if (Double.parseDouble(code) > Double.parseDouble(MainActivity.app_ver)) {
                        update_sign = "1";
                        new update_dialog().show(getSupportFragmentManager(), "");
                    } else {
                        update_sign = "0";
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    };
}
