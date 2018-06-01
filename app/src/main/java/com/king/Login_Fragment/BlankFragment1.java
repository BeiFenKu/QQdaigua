package com.king.Login_Fragment;


import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.king.YH_Fragment.YH_Fragment;
import com.king.ewrite.CircleImageView;
import com.king.modul.UserAccount;
import com.king.qqdaigua.MainActivity;
import com.king.qqdaigua.R;
import com.king.util.HttpRequest;
import com.king.util.InternetCheck;
import com.king.util.SetImageViewUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import static com.king.qqdaigua.R.id.rl1;

/**
 * A simple {@link Fragment} subclass.
 */
public class BlankFragment1 extends Fragment implements Handler.Callback {

    protected boolean useThemestatusBarColor = true;//是否使用特殊的标题栏背景颜色，android5
    // .0以上可以设置状态栏背景色，如果不使用则使用透明色值
    protected boolean useStatusBarColor = false;//是否使用状态栏文字和图标为暗色，如果状态栏采用了白色系，则需要使状态栏和图标为暗色，android6
    // .0以上可以设置

    //控制密码明文显示否  0密文 1明文
    private String eye_sign = "0";

    //控制多帐号  0闭合 1下拉
    private String multi_sign = "0";

    //记录下拉列表数量
    private String multi_count = "0";

    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;
    private View view;
    private CircleImageView image_touxiang;
    private RotateAnimation animation;
    private Button bt_zh, bt_login, bt_regkm, bt_buy;
    private ProgressDialog dialog_login;
    private TextView et_km;
    private ImageView title_img;

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
    private ImageView eye, multi;
    private ProgressDialog pgDialog;
    private List<UserAccount> UserData = new LinkedList<>();

    //PopupWindow对象
    private PopupWindow selectPopupWindow = null;
    //自定义Adapter
    private OptionsAdapter optionsAdapter = null;
    //下拉框选项数据源
    private ArrayList<String> Select_datas = new ArrayList<String>();
    ;
    //下拉框依附组件
    private RelativeLayout select_parent;
    //下拉框依附组件宽度，也将作为下拉框的宽度
    private int pwidth;
    //文本框  et_account
    //下拉箭头图片组件
    private ImageView select_image;
    //恢复数据源按钮
    private Button select_button;
    //展示所有下拉选项的ListView
    private ListView select_listView = null;
    //用来处理选中或者删除下拉项消息
    private Handler select_handler;
    //是否初始化完成标志
    private boolean select_flag = false;

    public BlankFragment1() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.blank_fragment1, container, false);
        checkInternet();
        init();
        setTitle();
        checkReamber();
        setMultiAccount();
        return view;
    }

    private void setMultiAccount() {
        view.getViewTreeObserver().addOnWindowFocusChangeListener(new ViewTreeObserver
                .OnWindowFocusChangeListener() {
            @Override
            public void onWindowFocusChanged(boolean hasFocus) {
                if (getActivity() != null) {
                    getActivity().onWindowFocusChanged(hasFocus);
                    if (getActivity() != null) {
                        if (multi_sign.equals("0")) {
                            getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    multi.setImageDrawable(getResources().getDrawable(R.mipmap.down_account));
                                }
                            });
                            multi_sign = "1";
                        } else {
                            getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    multi.setImageDrawable(getResources().getDrawable(R.mipmap.up_account));
                                }
                            });
                            multi_sign = "0";
                        }
                    }
                    while (!select_flag) {
                        initWedget();
                        select_flag = true;
                    }
                }
            }
        });
    }

    private void checkInternet() {
        if (getActivity() != null) {
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    pgDialog = new ProgressDialog(getContext());
                    pgDialog.setTitle("检查网络中");
                    pgDialog.setMessage("检查网络中，请稍等...");
                    pgDialog.show();
                    pgDialog.setCancelable(false);
                }
            });
        }
        new Thread() {
            @Override
            public void run() {
                if (InternetCheck.Check()) {
                    pgDialog.cancel();
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            image_touxiang.startAnimation(animation);
                        }
                    });
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
        multi_count = preferences.getString("mu_count", "0");
        if (!multi_count.equals("0")) {
            for (int i = 1; i <= Integer.valueOf(multi_count); i++) {
                String qq = preferences.getString("mu_qq_" + i, "");
                String pwd = preferences.getString("mu_pwd_" + i, "");
                Boolean rember = preferences.getBoolean("mu_rember_" + i, false);
                if (qq.equals("")) {
                    continue;
                }
                UserData.add(new UserAccount(qq, pwd, rember));
            }
        }

        boolean isRemenber = preferences.getBoolean("cb_remaber", false);
        String user_qq = preferences.getString("account", "123455");
        SetImageViewUtil.setImageToImageView(image_touxiang, "http://q2.qlogo" +
                ".cn/headimg_dl?dst_uin=" + user_qq + "&spec=100");
        String account = preferences.getString("account", "");
        et_account.setText(account);
        if (isRemenber) {
            String pwd = preferences.getString("pwd", "");
            et_pwd.setText(pwd);
            cb_remaber.setChecked(true);
        }
        System.out.println("Shread记忆"+user_qq);
        if (isRemenber){
            System.out.println("记住了");
        } else {
            System.out.println("没有记住");
        }
    }

    private void init() {
        ll4 = (LinearLayout) view.findViewById(R.id.ll4);
        ll2 = (LinearLayout) view.findViewById(R.id.ll2);
        rl_regkm = (RelativeLayout) view.findViewById(R.id.rl_regkm);
        et_reg_pwd = (EditText) view.findViewById(R.id.et_reg_pwd);
        et_reg_account = (EditText) view.findViewById(R.id.et_reg_account);
        et_reg_account.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                } else {
                    if (!et_reg_account.getText().toString().equals("")) {
                        setNewHeadIMG(et_reg_account.getText().toString());
                    }
                }
            }
        });
        ll3 = (LinearLayout) view.findViewById(R.id.ll3);
        ll1 = (LinearLayout) view.findViewById(R.id.ll1);
        et_pwd = (EditText) view.findViewById(R.id.et_pwd);
        et_account = (EditText) view.findViewById(R.id.et_account);
        et_account.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                } else {
                    if (!et_account.getText().toString().equals("")) {
                        setNewHeadIMG(et_account.getText().toString());
                    }
                }
            }
        });
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
                        editor.putString("pwd", pwd);
                        saveUserData(UserData, account, pwd, cb_remaber.isChecked());
                        editor.apply();
                        ajax_login(account, pwd);
                    }
                }
            }
        });
        cb_remaber = (CheckBox) view.findViewById(R.id.cb_remaber);
        cb_remaber.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView,
                                         boolean isChecked) {
                // TODO Auto-generated method stub
                editor = preferences.edit();
                if (isChecked) {
                    editor.putBoolean("cb_remaber", true);
                } else {
                    editor.putBoolean("cb_remaber", false);
                }
                editor.apply();
            }
        });
        et_km = (EditText) view.findViewById(R.id.et_km);
        bt_regkm = (Button) view.findViewById(R.id.bt_regkm);
        bt_regkm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String account = et_reg_account.getText().toString();
                String kami = et_km.getText().toString();
                String pwd = et_reg_pwd.getText().toString();
                if (account.length() == 0 || kami.length() == 0 || pwd.length() == 0) {
                    Toast.makeText(getContext(), "注册失败，不可留空", Toast.LENGTH_SHORT).show();
                } else {
                    ajax_reg(account, kami, pwd);
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
        bt_zh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openURL(MainActivity.app_url + "find.html");
            }
        });
        bt_buy = (Button) view.findViewById(R.id.bt_buy);
        bt_buy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openURL(MainActivity.app_buy);
            }
        });
        title_img = (ImageView) view.findViewById(R.id.title_img);

        tv_fail = (TextView) view.findViewById(R.id.textView);
        tv_fail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new dialog1_admin().show(getFragmentManager(), "");
            }
        });

        preferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        String user_qq = preferences.getString("account", "123455");
        animation = new RotateAnimation(0, 360, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        animation.setDuration(1000);
        animation.setFillAfter(true);
        image_touxiang = (CircleImageView) view.findViewById(R.id.image_touxiang);
        setNewHeadIMG(user_qq);
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
        multi = (ImageView) view.findViewById(R.id.multi);
        eye = (ImageView) view.findViewById(R.id.eye);
        eye.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (eye_sign.equals("0")) {
                    if (getActivity() != null) {
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                eye.setImageDrawable(getResources().getDrawable(R.mipmap.eye_open));
                            }
                        });
                    }
                    et_pwd.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    eye_sign = "1";
                } else {
                    if (getActivity() != null) {
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                eye.setImageDrawable(getResources().getDrawable(R.mipmap.eye_close));
                            }
                        });
                    }
                    et_pwd.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    eye_sign = "0";
                }
            }
        });
    }

    /***
     * 开通
     *
     * @param account 帐号
     * @param kami    卡密
     * @param pwd     密码
     */
    private void ajax_reg(String account, String kami, String pwd) {
        dialog_login = new ProgressDialog(getContext());
        dialog_login.setTitle("开通中");
        dialog_login.setMessage("开通中，请稍等...");
        dialog_login.setCancelable(false);
        dialog_login.show();
        JSONObject json = new JSONObject();
        String post_url = MainActivity.app_url + "ajax/dg" +
                ".php?ajax=true&star=post&do=yewu&info=paydg";
        try {
            json.put("type", "pay");
            json.put("qq", account);
            json.put("kami", kami);
            json.put("pwd", pwd);
            HttpRequest http = new HttpRequest(post_url, json.toString(), handler);
            http.start();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /***
     * 登录
     *
     * @param qq  帐号
     * @param pwd 密码
     */
    private void ajax_login(String qq, String pwd) {
        if (getActivity() != null) {
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    dialog_login = new ProgressDialog(getContext());
                    dialog_login.setTitle("登录中");
                    dialog_login.setMessage("登录中，请稍等...");
                    dialog_login.setCancelable(false);
                    if (!dialog_login.isShowing()) {
                        dialog_login.show();
                    }
                }
            });
        }
        JSONObject jsonobject = new JSONObject();
        String post_url = null;
        try {
            post_url = MainActivity.web_jiekou + "api/submit.php?act=query&qq=" + qq + "&pwd=" +
                    URLEncoder.encode(pwd,"UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        try {
            jsonobject.put("type", "login");
            HttpRequest http = new HttpRequest(post_url, jsonobject.toString(), handler);
            http.start();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 1) {
                //登录
                try {
                    JSONObject json = new JSONObject((String) msg.obj);
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                    Date adddate = sdf.parse(json.getString("adddate"));
                    Date enddate = sdf.parse(json.getString("enddate"));
                    editor = preferences.edit();
                    editor.putString("serverday", getGapCount(adddate,enddate)+"");
                    editor.apply();
                    String code = json.getString("code");
                    String sid = json.getString("id");
                    System.out.println("登录成功SID为"+sid);
                    if (code.equals("1")) {
                        //登录成功
                        editor = preferences.edit();
                        editor.putString("sid", sid);
                        editor.apply();
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(getContext(), "登录成功", Toast.LENGTH_SHORT).show();
                            }
                        });
                        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id
                                .content_main, new YH_Fragment()).commit();
                        dialog_login.cancel();
                    } else {
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(getContext(), "登录失败：密码错误或者未开通代挂", Toast.LENGTH_SHORT).show();
                            }
                        });
                        dialog_login.cancel();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            } else if (msg.what == 2) {
                //注册
                try {
                    JSONObject json = new JSONObject((String) msg.obj);
                    String code = json.getString("code");
                    String error = json.getString("error");
                    if (code.equals("0")) {
                        dialog_login.cancel();
                        Toast.makeText(getContext(), "代挂开通成功，请返回登录", Toast.LENGTH_LONG).show();
                    } else if (code.equals("1")) {
                        if (!error.equals("此激活码不存在") && !error.equals("卡密格式错误")) {
                            dialog_login.cancel();
                            Toast.makeText(getContext(), error, Toast.LENGTH_LONG)
                                    .show();
                        } else {
                            dialog_login.cancel();
                            Toast.makeText(getContext(), "开通失败，卡密错误，卡密购买请点击下方按钮", Toast.LENGTH_LONG)
                                    .show();
                        }
                    } else {
                        dialog_login.cancel();
                        Toast.makeText(getContext(), "未知错误，建议返回登录尝试", Toast.LENGTH_LONG)
                                .show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else {
                dialog_login.cancel();
                Toast.makeText(getContext(), "请求失败，请稍等登录", Toast.LENGTH_LONG).show();
            }
        }
    };

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


    private void openURL(String s) {
        Intent intent = new Intent();
        intent.setAction("android.intent.action.VIEW");
        Uri content_url = Uri.parse(s);
        intent.setData(content_url);
        startActivity(intent);
    }

    private void saveUserData(List<UserAccount> UserData, String qq, String pwd, boolean rember) {
        if (UserData.size() == 0) {

        } else {
            int length = UserData.size();
            for (int i = 0; i < length; i++) {
                if (UserData.get(i).getQq().equals(qq)) {
                    return;
                }
            }
        }

        multi_count = String.valueOf(Integer.valueOf(multi_count) + 1);
        editor = preferences.edit();
        editor.putString("mu_qq_" + multi_count, qq);
        if (rember){
            editor.putString("mu_pwd_" + multi_count, pwd);
            editor.putBoolean("mu_rember_" + multi_count, rember);
            editor.putString("mu_count", multi_count);
            Log.e("已储存", multi_count + "个帐号");
            editor.apply();
        } else {
            editor.putString("mu_pwd_" + multi_count, "");
            editor.putBoolean("mu_rember_" + multi_count, rember);
            editor.putString("mu_count", multi_count);
            Log.e("已储存", multi_count + "个帐号");
            editor.apply();
        }
    }


    @Override
    public boolean handleMessage(Message message) {
        Bundle data = message.getData();
        switch (message.what) {
            case 3:
                //选中下拉项，下拉框消失
                int selIndex = data.getInt("selIndex");
                if (Select_datas.get(selIndex).equals("添加新代挂QQ...")) {
                    et_account.setText("");
                    et_pwd.setText("");
                } else {
                    et_account.setText(Select_datas.get(selIndex));
                    et_pwd.setText(UserData.get(selIndex).getPwd());
                    if (UserData.get(selIndex).getRember()) {
                        if (getActivity() != null) {
                            getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    cb_remaber.setChecked(true);
                                }
                            });
                        }
                    } else {
                        if (getActivity() != null) {
                            getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    cb_remaber.setChecked(false);
                                }
                            });
                        }
                    }
                }
                setNewHeadIMG(Select_datas.get(selIndex));
                dismiss();
                break;
            case 4:
                //移除下拉项数据
                int delIndex = data.getInt("delIndex");
                if (delIndex == data.size()){
                    break;
                }
                Select_datas.remove(delIndex);

                editor = preferences.edit();
                int delIndex_1 = delIndex + 1;
                editor.putString("mu_qq_" + delIndex_1, "");
                Log.e("已删除", "第" + delIndex_1 + "个帐号");
                editor.apply();
                //刷新下拉列表
                optionsAdapter.notifyDataSetChanged();
                break;
        }
        return false;
    }

    /**
     * PopupWindow消失
     */
    public void dismiss() {
        selectPopupWindow.dismiss();
    }

    private void initWedget() {
        //初始化Handler,用来处理消息
        select_handler = new Handler(BlankFragment1.this);

        //初始化界面组件
        select_parent = (RelativeLayout) view.findViewById(rl1);


        //获取下拉框依附的组件宽度
        int width = select_parent.getWidth();
        pwidth = width;

        //设置点击下拉箭头图片事件，点击弹出PopupWindow浮动下拉框
        multi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (select_flag) {
                    //显示PopupWindow窗口
                    popupWindwShowing();
                }
            }
        });
        initPopuWindow();

    }

    /**
     * 初始化填充Adapter所用List数据
     */
    private void initDatas() {

        Select_datas.clear();
        if (UserData.size() != 0) {
            for (int i = 0; i < UserData.size(); i++) {
                Select_datas.add(UserData.get(i).getQq());
            }
        }
        Select_datas.add("添加新代挂QQ...");
    }

    private void initPopuWindow() {
        initDatas();

        View loginwindow = (View) getActivity().getLayoutInflater().inflate(R.layout.select_options,
                null);
        select_listView = (ListView) loginwindow.findViewById(R.id.list);

        //设置自定义Adapter
        optionsAdapter = new OptionsAdapter(getActivity(), select_handler, Select_datas);
        select_listView.setAdapter(optionsAdapter);

        selectPopupWindow = new PopupWindow(loginwindow, pwidth, LinearLayout.LayoutParams
                .WRAP_CONTENT, true);

        selectPopupWindow.setOutsideTouchable(true);

        //这一句是为了实现弹出PopupWindow后，当点击屏幕其他部分及Back键时PopupWindow会消失，
        //没有这一句则效果不能出来，但并不会影响背景
        //本人能力极其有限，不明白其原因，还望高手、知情者指点一下
        selectPopupWindow.setBackgroundDrawable(new BitmapDrawable());
    }


    /**
     * 显示PopupWindow窗口
     *
     * @param
     */
    public void popupWindwShowing() {
        //将selectPopupWindow作为parent的下拉框显示，并指定selectPopupWindow在Y方向上向上偏移3pix，
        //这是为了防止下拉框与文本框之间产生缝隙，影响界面美化
        //（是否会产生缝隙，及产生缝隙的大小，可能会根据机型、Android系统版本不同而异吧，不太清楚）
        selectPopupWindow.showAsDropDown(select_parent, 0, -3);
    }

    private void setNewHeadIMG(final String new_qq) {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                SetImageViewUtil.setImageToImageView(image_touxiang, "http://q2.qlogo" +
                        ".cn/headimg_dl?dst_uin=" + new_qq + "&spec=100");
                image_touxiang.startAnimation(animation);
            }
        });
    }
    public void setTitle(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {//5.0及以上
            View decorView = getActivity().getWindow().getDecorView();
            int option = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE;
            decorView.setSystemUiVisibility(option);
            //根据上面设置是否对状态栏单独设置颜色
            if (useThemestatusBarColor) {
                getActivity().getWindow().setStatusBarColor(getResources().getColor(R.color.blue_title));
            } else {
                getActivity().getWindow().setStatusBarColor(Color.TRANSPARENT);
            }
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {//4.4到5.0
            WindowManager.LayoutParams localLayoutParams = getActivity().getWindow().getAttributes();
            localLayoutParams.flags = (WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS | localLayoutParams.flags);
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && !useStatusBarColor) {//android6.0以后可以对状态栏文字颜色和图标进行修改
            getActivity().getWindow().getDecorView().setSystemUiVisibility( View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN|View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        }
    }
    public static int getGapCount(Date startDate, Date endDate) {
        return (int) ((endDate.getTime() - startDate.getTime()) / (1000 * 60 * 60 * 24));
    }
}