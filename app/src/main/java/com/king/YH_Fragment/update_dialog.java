package com.king.YH_Fragment;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.king.qqdaigua.MainActivity;
import com.king.qqdaigua.R;
import com.king.util.HttpRequest;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by KingLee on 2018/4/17.
 */

public class update_dialog extends DialogFragment {

    private View view;
    private TextView tv_update;
    private Button bt_submit;
    private Button bt_cancel;
    private TextView tv_update_title;


    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.dialog_update, null);
        BindViews();
        getDialog().setTitle("有最新更新");
        settText();
        return view;
    }

    private void settText() {
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

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 10) {
                try {
                    JSONObject json = new JSONObject((String) msg.obj);
                    String code = json.getString("code");
                    final String url = json.getString("url");
                    String error = json.getString("error");
                    tv_update_title.setText("新版本：V"+code);
                    tv_update.setText("更新内容："+error);
                    bt_submit.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            openURL(url);
                        }
                    });
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    };


    private void BindViews() {
        tv_update_title = (TextView) view.findViewById(R.id.tv_update_title);
        tv_update = (TextView) view.findViewById(R.id.tv_update);
        bt_submit = (Button) view.findViewById(R.id.bt_submit);
        bt_cancel = (Button) view.findViewById(R.id.bt_cancel);
        bt_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getDialog().cancel();
            }
        });
    }

    private void openURL(String s) {
        Intent intent = new Intent();
        intent.setAction("android.intent.action.VIEW");
        Uri content_url = Uri.parse(s);
        intent.setData(content_url);
        startActivity(intent);
    }
}
