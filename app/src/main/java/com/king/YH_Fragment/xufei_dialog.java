package com.king.YH_Fragment;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.king.qqdaigua.MainActivity;
import com.king.qqdaigua.R;
import com.king.util.HttpRequest;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by KingLee on 2018/4/15.
 */

public class xufei_dialog extends DialogFragment {
    private View view;
    private EditText et_kami;
    private Button bt_submit;
    private Button bt_cancel;
    private SharedPreferences preferences;
    private ProgressDialog xufeiDialog;

    private String user, pwd, sid;
    private TextView tv_buy;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.dialog_xufei, null);
        getDialog().setTitle("代挂续费");

        bindViews();
        return view;
    }

    private void bindViews() {
        preferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        sid = preferences.getString("sid", "");
        user = preferences.getString("account", "");
        pwd = preferences.getString("pwd", "");
        tv_buy = (TextView) view.findViewById(R.id.tv_buy);
        tv_buy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openURL(MainActivity.buy_url);
            }
        });
        et_kami = (EditText) view.findViewById(R.id.et_kami);
        bt_submit = (Button) view.findViewById(R.id.bt_submit);
        bt_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                xufeiDialog = new ProgressDialog(getContext());
                xufeiDialog.setTitle("请稍等");
                xufeiDialog.setMessage("充值提交中，请稍等...");
                xufeiDialog.show();
                xufeiDialog.setCancelable(false);

                String kami = et_kami.getText().toString();
                if (kami.length() == 0) {
                    Toast.makeText(getContext(), "卡密不可为空", Toast.LENGTH_SHORT).show();
                    getDialog().cancel();
                    xufeiDialog.cancel();
                } else {
                    String post_url = MainActivity.web_jiekou + "api/submit" +
                            ".php?act=active&id=" + sid + "&uin=" + user + "&km=" + kami;
                    JSONObject jsonObject = new JSONObject();
                    try {
                        jsonObject.put("type", "xufei");
                        HttpRequest http = new HttpRequest(post_url, jsonObject.toString(), handler1);
                        http.start();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        bt_cancel = (Button) view.findViewById(R.id.bt_cancel);
        bt_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getDialog().cancel();
            }
        });
    }

    Handler handler1 = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 6) {
                try {
                    xufeiDialog.cancel();
                    JSONObject json = new JSONObject((String) msg.obj);
                    String code = json.getString("code");
                    if (code.equals("1")) {
                        Toast.makeText(getContext(), "续费代挂成功", Toast.LENGTH_SHORT).show();
                        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id
                                .content_main, new YH_Fragment()).commit();
                        getDialog().cancel();
                    } else {
                        Toast.makeText(getContext(), "续费失败，卡密错误", Toast.LENGTH_SHORT).show();
                        getDialog().cancel();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else {
                Toast.makeText(getContext(), "网络请求错误", Toast.LENGTH_SHORT).show();
            }
        }
    };

    private void openURL(String s) {
        Intent intent = new Intent();
        intent.setAction("android.intent.action.VIEW");
        Uri content_url = Uri.parse(s);
        intent.setData(content_url);
        startActivity(intent);
    }
}
