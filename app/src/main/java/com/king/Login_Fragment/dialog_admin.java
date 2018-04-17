package com.king.Login_Fragment;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.king.qqdaigua.MainActivity;
import com.king.qqdaigua.R;


/**
 * Created by KingLee on 2018/4/6.
 */

public class dialog_admin extends DialogFragment {
    private View view;
    private EditText et_pwd;
    private Button bt_cancel, bt_login;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.dialog_admin, null);
        getDialog().setTitle("请输入管理员密码");
        init();
        Log.e("Dialog提示：", "登录Dialog已打开");


        return view;
    }

    private void init() {
        bt_cancel = (Button) view.findViewById(R.id.bt_cancel);
        bt_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getDialog().cancel();
            }
        });
        et_pwd = (EditText) view.findViewById(R.id.et_pwd);
        bt_login = (Button) view.findViewById(R.id.bt_login);
        bt_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String pwd = et_pwd.getText().toString();
                if (pwd.equals(MainActivity.admin_pwd) || pwd == MainActivity.admin_pwd) {
                    Log.e("密码", "正确");
                    getDialog().cancel();
                } else {
                    Toast.makeText(getActivity(), "密码错误，非管理员严禁进入！", Toast.LENGTH_SHORT).show();
                    Log.e("密码", "错误");
                    getDialog().cancel();
                }
            }
        });
    }

}
