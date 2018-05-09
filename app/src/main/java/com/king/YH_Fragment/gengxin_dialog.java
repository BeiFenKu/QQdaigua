package com.king.YH_Fragment;

import android.app.ProgressDialog;
import android.content.SharedPreferences;
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
import android.widget.Toast;

import com.king.qqdaigua.MainActivity;
import com.king.qqdaigua.R;
import com.king.util.HttpRequest;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by KingLee on 2018/4/16.
 */

public class gengxin_dialog extends DialogFragment {

    private View view;
    private Button bt_cancel;
    private Button bt_submit;
    private ProgressDialog gengxDialog;
    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;
    private String sid, new_pwd;
    private EditText et_npwd;
    private String user;
    private String pwd;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.dialog_gengxin, null);
        getDialog().setTitle("更新密码：");
        bindViews();
        return view;
    }

    private void bindViews() {
        preferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        sid = preferences.getString("sid", "");
        user = preferences.getString("account", "");
        pwd = preferences.getString("pwd", "");

        et_npwd = (EditText) view.findViewById(R.id.et_npwd);
        bt_cancel = (Button) view.findViewById(R.id.bt_cancel);
        bt_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getDialog().cancel();
            }
        });
        bt_submit = (Button) view.findViewById(R.id.bt_submit);
        bt_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gengxDialog = new ProgressDialog(getContext());
                gengxDialog.setTitle("更新密码中");
                gengxDialog.setMessage("更新密码中，请稍等...");
                gengxDialog.setCancelable(false);
                gengxDialog.show();

                new_pwd = et_npwd.getText().toString();
                if (new_pwd.length() == 0) {
                    Toast.makeText(getContext(), "新密码不可为空", Toast
                            .LENGTH_LONG)
                            .show();
                    getDialog().cancel();
                    gengxDialog.cancel();
                } else {
                    String post_url = MainActivity
                            .web_jiekou1 + "ajax/dg?ajax=true&star=post&do=yewu&info=login";
                    JSONObject jsonObject = new JSONObject();
                    try {
                        jsonObject.put("type", "gengx");
                        jsonObject.put("qq", user);
                        jsonObject.put("pwd", pwd);
                        jsonObject.put("npwd", new_pwd);
                        HttpRequest http = new HttpRequest(post_url, jsonObject.toString(), handler1);
                        http.start();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    Handler handler1 = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 8) {
                try {
                    gengxDialog.cancel();
                    JSONObject json = new JSONObject((String) msg.obj);
                    String code = json.getString("code");
                    if (code.equals("0")) {

                        editor = preferences.edit();
                        editor.putString("pwd", new_pwd);
                        editor.apply();
                        handler1.post(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(getContext(), "更新成功，当天21点后更新的，请于明天提交补挂参加补挂场", Toast
                                        .LENGTH_LONG)
                                        .show();
                            }
                        });
                        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id
                                .content_main, new YH_Fragment()).commit();
                        getDialog().cancel();
                    } else {
                        handler1.post(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(getContext(), "更新失败，原因如下：\n1.21点-10点禁止改密\n", Toast
                                        .LENGTH_LONG)
                                        .show();
                            }
                        });
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
}
