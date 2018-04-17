package com.king.qqdaigua;

import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.king.ewrite.CustomTextView;

/**
 * Created by KingLee on 2018/4/11.
 */

/***
 * 启动图Activity
 *
 */
public class QiDongActivity extends AppCompatActivity {

    private CustomTextView bt_sec;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.adt_main);

        iniv();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                finish();
            }
        }, 2000);
    }

    private void iniv() {
        bt_sec = (CustomTextView) findViewById(R.id.enter);
        bt_sec.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
