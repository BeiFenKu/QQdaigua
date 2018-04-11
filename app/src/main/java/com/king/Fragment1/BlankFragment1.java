package com.king.Fragment1;


import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.king.ewrite.CircleImageView;
import com.king.qqdaigua.R;
import com.king.util.InternetCheck;
import com.king.util.SetImageViewUtil;

/**
 * A simple {@link Fragment} subclass.
 */
public class BlankFragment1 extends Fragment {

    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;
    private View view;
    private CircleImageView image_touxiang;
    private RotateAnimation animation;
    private Button bt_zh, bt_login;

    //    登录/注册 控制开关
    private Button bt_reg, bt_loginback;
    //    登录界面组件组
    private TextView et_account, et_pwd, tv_fail;
    private CheckBox cb_remaber;
    private LinearLayout ll1, ll3;
    //    注册界面组件组
    private TextView et_reg_account, et_reg_pwd;
    private RelativeLayout rl_regkm;
    private LinearLayout ll2, ll4;

    public BlankFragment1() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.blank_fragment1, container, false);
        checkInternet();
        init();
        checkReamber();

        return view;
    }

    private void checkInternet() {
        final ProgressDialog pgDialog = new ProgressDialog(getContext());
        pgDialog.setTitle("检查网络中");
        pgDialog.setMessage("检查网络中，请稍等...");
        pgDialog.show();
        pgDialog.setCancelable(false);
        new Thread(){
            @Override
            public void run() {
                if (InternetCheck.Check() == true){
                    pgDialog.cancel();
                } else {
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            pgDialog.setTitle("网络错误");
                            pgDialog.setMessage("网络错误，请检查网络连接");
                            Toast.makeText(getContext(), "网络错误，请检查网络是否正常", Toast.LENGTH_LONG).show();
                        }
                    });
                }
            }
        }.start();
    }

    private void checkReamber() {
        preferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        boolean isRemenber = preferences.getBoolean("cb_remaber", false);
        String user_qq = preferences.getString("user_qq", "123455");
        if (isRemenber) {
            String account = preferences.getString("account", "");
            String pwd = preferences.getString("pwd", "");
            et_account.setText(account);
            et_pwd.setText(pwd);
            cb_remaber.setChecked(true);
        }
    }

    private void init() {
        ll4 = (LinearLayout) view.findViewById(R.id.ll4);
        ll2 = (LinearLayout) view.findViewById(R.id.ll2);
        rl_regkm = (RelativeLayout) view.findViewById(R.id.rl_regkm);
        et_reg_pwd = (EditText) view.findViewById(R.id.et_reg_pwd);
        et_reg_account = (EditText) view.findViewById(R.id.et_reg_account);
        ll3 = (LinearLayout) view.findViewById(R.id.ll3);
        ll1 = (LinearLayout) view.findViewById(R.id.ll1);
        cb_remaber = (CheckBox) view.findViewById(R.id.cb_remaber);
        et_pwd = (EditText) view.findViewById(R.id.et_pwd);
        et_account = (EditText) view.findViewById(R.id.et_account);
        bt_loginback = (Button) view.findViewById(R.id.bt_loginback);
        bt_loginback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                main_switch(true);
            }
        });
        bt_login = (Button) view.findViewById(R.id.bt_login);
        bt_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String account = et_account.getText().toString();
                String pwd = et_pwd.getText().toString();
                if (account.length() == 0 || pwd.length() == 0) {
                    Toast.makeText(getContext(), "帐号或密码不可为空", Toast.LENGTH_SHORT).show();
                } else {
                    if (account != null && pwd != null) {
                        editor = preferences.edit();
                        editor.putString("user_qq", account);
                        editor.putString("account", account);
                        if (cb_remaber.isChecked()) {
                            editor.putString("pwd", pwd);
                            editor.putBoolean("cb_remaber", true);
                        } else {
                            editor.clear();
                        }
                    }
                    editor.apply();
                }
            }
        });
        bt_reg = (Button) view.findViewById(R.id.bt_reg);
        bt_reg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                main_switch(false);
            }
        });
        bt_zh = (Button) view.findViewById(R.id.bt_zh);

        tv_fail = (TextView) view.findViewById(R.id.textView);
        tv_fail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new dialog1_admin().show(getFragmentManager(), "");
            }
        });
        image_touxiang = (CircleImageView) view.findViewById(R.id.image_touxiang);
        SetImageViewUtil.setImageToImageView(image_touxiang,"http://q2.qlogo.cn/headimg_dl?dst_uin=1278991552&spec=100");
        animation = new RotateAnimation(0, 360, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        animation.setDuration(1000);
        animation.setFillAfter(true);
        image_touxiang.setAnimation(animation);
        image_touxiang.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                new dialog_admin().show(getFragmentManager(), "");
                return true;
            }
        });
        image_touxiang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                image_touxiang.startAnimation(animation);
            }
        });
    }


    private void main_switch(boolean check) {
        if (!check) {
            et_account.setVisibility(View.INVISIBLE);
            et_pwd.setVisibility(View.INVISIBLE);
            tv_fail.setVisibility(View.INVISIBLE);
            cb_remaber.setVisibility(View.INVISIBLE);
            ll1.setVisibility(View.INVISIBLE);
            ll3.setVisibility(View.INVISIBLE);

            et_reg_account.setVisibility(View.VISIBLE);
            et_reg_pwd.setVisibility(View.VISIBLE);
            rl_regkm.setVisibility(View.VISIBLE);
            ll2.setVisibility(View.VISIBLE);
            ll4.setVisibility(View.VISIBLE);
        } else {
            et_account.setVisibility(View.VISIBLE);
            et_pwd.setVisibility(View.VISIBLE);
            tv_fail.setVisibility(View.VISIBLE);
            cb_remaber.setVisibility(View.VISIBLE);
            ll1.setVisibility(View.VISIBLE);
            ll3.setVisibility(View.VISIBLE);

            et_reg_account.setVisibility(View.INVISIBLE);
            et_reg_pwd.setVisibility(View.INVISIBLE);
            rl_regkm.setVisibility(View.INVISIBLE);
            ll2.setVisibility(View.INVISIBLE);
            ll4.setVisibility(View.INVISIBLE);
        }
    }

}
