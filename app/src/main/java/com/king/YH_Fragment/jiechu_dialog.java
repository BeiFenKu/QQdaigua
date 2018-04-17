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

public class jiechu_dialog extends DialogFragment {

    private View view;
    private Button bt_jiechu;
    private Button bt_cancel;
    private ProgressDialog jiechuDialog;
    private SharedPreferences preferences;
    private String sid;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.dialog_jiechu, null);
        getDialog().setTitle("解除拉黑注意：");
        bindViews();

        return view;
    }

    private void bindViews() {
        preferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        sid = preferences.getString("sid", "");
        bt_jiechu = (Button) view.findViewById(R.id.bt_jiechu);
        bt_jiechu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                jiechuDialog = new ProgressDialog(getContext());
                jiechuDialog.setTitle("解除拉黑中");
                jiechuDialog.setMessage("解除拉黑中，请稍等...");
                jiechuDialog.setCancelable(false);
                jiechuDialog.show();

                String post_url = MainActivity.web_jiekou + "api/submit" +
                        ".php?act=rblack&id=" + sid;
                JSONObject jsonObject = new JSONObject();
                try {
                    jsonObject.put("type", "rblack");
                    HttpRequest http = new HttpRequest(post_url, jsonObject.toString(), handler1);
                    http.start();
                } catch (JSONException e) {
                    e.printStackTrace();
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
            if (msg.what == 7) {
                try {
                    jiechuDialog.cancel();
                    JSONObject json = new JSONObject((String) msg.obj);
                    String code = json.getString("code");
                    if (code.equals("1")) {
                        Toast.makeText(getContext(), "解除成功，当天21点后解除的，请于明天提交补挂参加补挂场", Toast
                                .LENGTH_LONG)
                                .show();
                        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id
                                .content_main, new YH_Fragment()).commit();
                        getDialog().cancel();
                    } else {
                        Toast.makeText(getContext(), "解除失败，请尝试重新登陆", Toast
                                .LENGTH_LONG)
                                .show();
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
