package com.king.YH_Fragment;


import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.view.animation.ScaleAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.king.qqdaigua.MainActivity;
import com.king.qqdaigua.R;
import com.king.util.HttpRequest;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * A simple {@link Fragment} subclass.
 */
public class YH_Fragment extends Fragment {

    protected boolean useThemestatusBarColor = true;//是否使用特殊的标题栏背景颜色，android5
    // .0以上可以设置状态栏背景色，如果不使用则使用透明色值
    protected boolean useStatusBarColor = false;//是否使用状态栏文字和图标为暗色，如果状态栏采用了白色系，则需要使状态栏和图标为暗色，android6
    // .0以上可以设置

    //判断网络任务是否完成 上限次数
    public static int sign_dialog_cancel = 2;
    private int sign_dialog = 0;

    //第一个Fragment 登录成功传过来的 帐号密码 SID值
    private String user, pwd, sid;
    //0电脑管家-1电脑QQ-2手机QQ-3勋章墙-4QQ音乐-5QQ手游-6空间访客
    private String[] status = new String[7];
    //    记录最近一次点击 开关项目ID
    private int switch_sign;


    private ScaleAnimation sa1 = new ScaleAnimation(1, 0, 1, 1,
            Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 1.0f);
    private ScaleAnimation sa2 = new ScaleAnimation(0, 1, 1, 1,
            Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 1.0f);
    private RotateAnimation animation, animation1;
    private ProgressDialog pgDialog, switchDialog;
    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;
    private View view;
    private TextView tv_userqq;
    private TextView tv_board;
    private TextView tv_lhzt;
    private TextView tv_bt_jiechu;
    private Button bt_xufei;
    private TextView tv_viplevel;
    private ImageView img_xz;
    private ImageView img_mqq;
    private ImageView img_pcqq;
    private ImageView img_music;
    private ImageView img_guanjia;
    private ImageView img_game;
    private ImageView[] imageViews = new ImageView[7];
    TextView[] textViews = new TextView[7];
    private TextView tv_guanjia;
    private TextView tv_xz;
    private TextView tv_mqq;
    private TextView tv_pcqq;
    private TextView tv_music;
    private TextView tv_game;
    private Button bt_gengxin;
    private Button bt_lougua;
    private RelativeLayout ll_bgbg;
    private LinearLayout ll_moren;
    private Button bt_lou_cancel, bt_lou_submit, bt_kf;
    private RadioGroup group_1;
    private RadioButton group_rb;
    private ImageView img_kjfk;
    private TextView tv_kjfk;
    private ImageView img_weishi;
    private ImageView img_load;


    public YH_Fragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_yh, container, false);
        bindviews();
        setTitle();

        setBoard();
        setBlack();
        return view;
    }


    private void setBlack() {
        sid = preferences.getString("sid", "");
        Log.e("SID为", "");
        String post_url = MainActivity.app_url + "ajax/dg" +
                ".php?ajax=true&star=post&do=yewu&info=dginfo1&sid=" + sid;
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("type", "black");
            HttpRequest http = new HttpRequest(post_url, jsonObject.toString(), handler);
            http.start();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void setBoard() {
        String post_url = MainActivity.app_url + "ajax/dg.php?ajax=true&star=get";
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("type", "board");
            HttpRequest http = new HttpRequest(post_url, jsonObject.toString(), handler);
            http.start();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 3) {
                try {
                    JSONObject json = new JSONObject((String) msg.obj);
                    String code = json.getString("code");
                    String message = json.getString("error");
                    if (code.equals("0")) {
                        tv_board.setText(message);
                    } else {
                        Toast.makeText(getContext(), "公告获取失败", Toast.LENGTH_SHORT).show();
                    }
                    pgdialog_cancel();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else if (msg.what == 4) {
                try {
                    JSONObject json = new JSONObject((String) msg.obj);
                    String code = json.getString("code");
                    String black = json.getString("black");
                    String data = json.getString("data");
                    String dgtime = json.getString("dgtime");
                    for (int i = 0, j = 0; i < 13; i += 2) {
                        status[j++] = data.charAt(i) + "";
                    }
                    CheckSwithImg(imageViews, status, textViews);
                    int dgtime_int = Integer.parseInt(dgtime);
                    if (code.equals("0")) {
                        if (!black.equals("0")) {
                            tv_bt_jiechu.setVisibility(View.VISIBLE);
                            if (black.equals("1")) {
                                tv_lhzt.setText("密码错误");
                            } else if (black.equals("2")) {
                                tv_lhzt.setText("QQ冻结");
                            } else if (black.equals("3")) {
                                tv_lhzt.setText("请关闭设备锁");
                            }
                        } else {
                            tv_lhzt.setText("正常加速中");
                        }
                    } else {
                        Toast.makeText(getContext(), "登录状态失效，请退出重新登录", Toast.LENGTH_SHORT).show();
                    }
                    bt_xufei.setText("续费[剩余" + dgtime + "天]");
                    if (dgtime_int < 30 && dgtime_int > 0) {
                        tv_viplevel.setText("VIP 1");
                    } else if (dgtime_int >= 30 && dgtime_int < 90) {
                        tv_viplevel.setText("VIP 2");
                    } else if (dgtime_int >= 90 && dgtime_int < 180) {
                        tv_viplevel.setText("VIP 3");
                    } else if (dgtime_int >= 180 && dgtime_int < 365) {
                        tv_viplevel.setText("VIP 4");
                    } else if (dgtime_int >= 365) {
                        tv_viplevel.setText("VIP 5");
                    } else if (dgtime_int <= 0) {
                        tv_viplevel.setTextColor(Color.BLACK);
                        tv_viplevel.setText("VIP 0");
                    }

                    pgdialog_cancel();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else if (msg.what == 5) {
                try {
                    JSONObject json = new JSONObject((String) msg.obj);
                    String code = json.getString("code");
                    switchDialog.cancel();
                    if (code.equals("1")) {
                        String word = "";
                        String word1 = textViews[switch_sign].getText().toString();
                        for (int j = 0; j < word1.length() - 3; j++) {
                            word += word1.charAt(j);
                        }
                        if (status[switch_sign].equals("0")) {
                            word += "关闭]";
                            textViews[switch_sign].setText(word);
                            ImgHui(imageViews[switch_sign]);
                            imageViews[switch_sign].startAnimation(animation1);
                        } else if (status[switch_sign].equals("1")) {
                            if (switch_sign == 2) {
                                word += "开启]";
                                textViews[switch_sign].setText(word);
                                imageViews[switch_sign].setImageDrawable(getResources().getDrawable(R
                                        .mipmap.htt_sjqq));
                                ImgLiang(imageViews[switch_sign]);
                                imageViews[switch_sign].startAnimation(animation);
                            } else {
                                word += "开启]";
                                textViews[switch_sign].setText(word);
                                ImgLiang(imageViews[switch_sign]);
                                imageViews[switch_sign].startAnimation(animation);
                            }
                        } else if (status[switch_sign].equals
                                ("2")) {
                            word += "开启]";
                            textViews[switch_sign].setText(word);
                            imageViews[switch_sign].setImageDrawable(getResources().getDrawable(R
                                    .mipmap.htt_sjqq_android));
                            ImgLiang(imageViews[switch_sign]);
                            imageViews[switch_sign].startAnimation(animation);
                        }
                        Toast.makeText(getContext(), "修改成功，立即生效", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getContext(), "修改失败，检查是否过期，或者尝试重新登录", Toast.LENGTH_SHORT)
                                .show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else if (msg.what == 9) {
                try {
                    JSONObject json = new JSONObject((String) msg.obj);
                    String code = json.getString("code");
                    if (code.equals("1")) {
                        if (getActivity() != null) {
                            getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(getContext(), "补挂提交成功，补挂场时间16-24点", Toast
                                            .LENGTH_LONG)
                                            .show();
                                }
                            });
                        }
                    } else {
                        if (getActivity() != null) {
                            getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(getContext(), "补挂失败，补挂提交时间10-16点", Toast
                                            .LENGTH_LONG)
                                            .show();
                                }
                            });
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                switchDialog.cancel();
            } else if (msg.what == 11) {
                try {
                    JSONObject json = new JSONObject((String) msg.obj);
                    String code = json.getString("code");
                    if (code.equals("0")) {
                        if(status[2].equals("1")){
                            mirror_flip(status[2]);
                            status[2] = "2";
                        } else {
                            mirror_flip(status[2]);
                            status[2] = "1";
                        }
                        if (getActivity() != null) {
                            getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(getContext(), "切换成功", Toast
                                            .LENGTH_LONG)
                                            .show();
                                }
                            });
                        }
                    } else {
                        if (getActivity() != null) {
                            getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(getContext(), "切换失败，或已经是该状态", Toast
                                            .LENGTH_LONG)
                                            .show();
                                }
                            });
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                switchDialog.cancel();
            } else {
                Toast.makeText(getContext(), "网络请求错误", Toast.LENGTH_SHORT).show();
            }
        }
    };

    private void bindviews() {
        if (getActivity() != null) {
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    try {
                        pgDialog = new ProgressDialog(getContext());
                        pgDialog.setTitle("请稍等");
                        pgDialog.setMessage("资源搜寻中，请稍等...");
                        pgDialog.show();
                        pgDialog.setCancelable(false);
                    } catch (Exception e) {

                    }
                }
            });
        }


        animation = new RotateAnimation(0, 360, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        animation.setDuration(1000);
        animation.setFillAfter(true);
        animation1 = new RotateAnimation(360, 0, Animation.RELATIVE_TO_SELF, 0.5f, Animation
                .RELATIVE_TO_SELF, 0.5f);
        animation1.setDuration(1000);
        animation1.setFillAfter(true);

        bt_kf = (Button) view.findViewById(R.id.bt_kf);
        bt_kf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openURL("http://wpa.qq.com/msgrd?v=3&uin=" + MainActivity.app_qq + "&site=qq&menu=yes");
            }
        });
        ll_bgbg = (RelativeLayout) view.findViewById(R.id.ll_bgbg);
        ll_moren = (LinearLayout) view.findViewById(R.id.ll_moren);
        bt_lougua = (Button) view.findViewById(R.id.bt_lougua);
        bt_lougua.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ll_moren.getVisibility() == View.VISIBLE) {
                    ll_moren.setVisibility(View.INVISIBLE);
                    ll_bgbg.setVisibility(View.VISIBLE);
                } else {
                    ll_moren.setVisibility(View.VISIBLE);
                    ll_bgbg.setVisibility(View.INVISIBLE);
                }
            }
        });
        bt_lou_cancel = (Button) view.findViewById(R.id.bt_lou_cancel);
        bt_lou_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ll_moren.getVisibility() == View.VISIBLE) {
                    ll_moren.setVisibility(View.INVISIBLE);
                    ll_bgbg.setVisibility(View.VISIBLE);
                } else {
                    ll_moren.setVisibility(View.VISIBLE);
                    ll_bgbg.setVisibility(View.INVISIBLE);
                }
            }
        });
        tv_game = (TextView) view.findViewById(R.id.tv_game);
        tv_music = (TextView) view.findViewById(R.id.tv_music);
        tv_pcqq = (TextView) view.findViewById(R.id.tv_pcqq);
        tv_mqq = (TextView) view.findViewById(R.id.tv_mqq);
        tv_xz = (TextView) view.findViewById(R.id.tv_xz);
        tv_kjfk = (TextView) view.findViewById(R.id.tv_kjfk);
        tv_guanjia = (TextView) view.findViewById(R.id.tv_guanjia);
        textViews[0] = tv_guanjia;
        textViews[1] = tv_pcqq;
        textViews[2] = tv_mqq;
        textViews[3] = tv_xz;
        textViews[4] = tv_music;
        textViews[5] = tv_game;
        textViews[6] = tv_kjfk;
        group_1 = (RadioGroup) view.findViewById(R.id.group_1);
        group_1.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                group_rb = (RadioButton) view.findViewById(group_1
                        .getCheckedRadioButtonId());

            }
        });
        bt_lou_submit = (Button) view.findViewById(R.id.bt_lou_submit);
        bt_lou_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("检测Group", "" + group_rb.getText());
                String func = "";
                switch (group_rb.getText().toString()) {
                    case "电脑管家":
                        func = "guanjia";
                        break;
                    case "电脑QQ":
                        func = "pcqq";
                        break;
                    case "手机QQ":
                        func = "mqq";
                        break;
                    case "勋章墙":
                        func = "xunzhang";
                        break;
                    case "QQ音乐":
                        func = "qqmusic";
                        break;
                    case "QQ手游":
                        func = "xunzhang";
                        break;
                    case "空间访客（易冻结）":
                        func = "qqfk";
                        break;
                    default:
                        break;
                }
                String post_url = MainActivity.web_jiekou + "api/submit" +
                        ".php?act=fill&id=" + sid + "&uin=" + user + "&func=" + func;
                JSONObject jsonObject = new JSONObject();
                try {
                    jsonObject.put("type", "bg");
                    HttpRequest http = new HttpRequest(post_url, jsonObject.toString(), handler);
                    http.start();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

        switchDialog = new ProgressDialog(getContext());
        switchDialog.setTitle("修改中");
        switchDialog.setMessage("修改中，请稍等...");
        switchDialog.setCancelable(false);
        img_game = (ImageView) view.findViewById(R.id.img_game);
        img_game.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switchDialog.show();
                switch_sign = 5;
                Switchswitch(5);
            }
        });
        img_guanjia = (ImageView) view.findViewById(R.id.img_guanjia);
        img_guanjia.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switchDialog.show();
                switch_sign = 0;
                Switchswitch(0);
            }
        });
        img_music = (ImageView) view.findViewById(R.id.img_music);
        img_music.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switchDialog.show();
                switch_sign = 4;
                Switchswitch(4);
            }
        });
        img_pcqq = (ImageView) view.findViewById(R.id.img_pcqq);
        img_pcqq.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switchDialog.show();
                switch_sign = 1;
                Switchswitch(1);
            }
        });
        img_mqq = (ImageView) view.findViewById(R.id.img_mqq);
        img_mqq.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                setMqqStatus();
                return true;
            }
        });
        img_mqq.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switchDialog.show();
                switch_sign = 2;
                Switchswitch(2);
            }
        });
        img_xz = (ImageView) view.findViewById(R.id.img_xz);
        img_xz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switchDialog.show();
                switch_sign = 3;
                Switchswitch(3);
            }
        });
        img_kjfk = (ImageView) view.findViewById(R.id.img_kjfk);
        img_kjfk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switchDialog.show();
                switch_sign = 6;
                Switchswitch(6);
            }
        });
        img_weishi = (ImageView) view.findViewById(R.id.img_weishi);
        ImgHui(img_weishi);
        img_load = (ImageView) view.findViewById(R.id.img_load);
        imageViews[0] = img_guanjia;
        imageViews[1] = img_pcqq;
        imageViews[2] = img_mqq;
        imageViews[3] = img_xz;
        imageViews[4] = img_music;
        imageViews[5] = img_game;
        imageViews[6] = img_kjfk;
        tv_viplevel = (TextView) view.findViewById(R.id.tv_viplevel);
        bt_gengxin = (Button) view.findViewById(R.id.bt_gengxin);
        bt_gengxin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new gengxin_dialog().show(getFragmentManager(), null);
            }
        });
        bt_xufei = (Button) view.findViewById(R.id.bt_xufei);
        bt_xufei.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new xufei_dialog().show(getFragmentManager(), "");
            }
        });
        tv_bt_jiechu = (TextView) view.findViewById(R.id.tv_bt_jiechu);
        tv_bt_jiechu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new jiechu_dialog().show(getFragmentManager(), "");
            }
        });
        tv_lhzt = (TextView) view.findViewById(R.id.lhzt);
        preferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        String sid = preferences.getString("sid", "");
        Log.e("SID1为", sid);
        user = preferences.getString("account", "");
        pwd = preferences.getString("pwd", "");

        tv_userqq = (TextView) view.findViewById(R.id.tv_dqqq);
        tv_userqq.setText("当前QQ：" + user);
        tv_board = (TextView) view.findViewById(R.id.tv_board);
        tv_board.setMovementMethod(ScrollingMovementMethod.getInstance());

    }

    private void setMqqStatus() {
        if (status[2].equals("0")) {
            Toast.makeText(getContext(), "切换状态前需要打开该功能哦", Toast.LENGTH_SHORT).show();
            return;
        }
        switchDialog.show();
        String post_url = MainActivity
                .web_jiekou1 + "ajax/dg?ajax=true&star=post&do=yewu&info=login";
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("type", "gengx_phonetype");
            jsonObject.put("qq", user);
            jsonObject.put("pwd", pwd);
            jsonObject.put("status", status[2]);
            HttpRequest http = new HttpRequest(post_url, jsonObject.toString(), handler);
            http.start();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void pgdialog_cancel() {
        sign_dialog++;
        if (sign_dialog == sign_dialog_cancel) {
            pgDialog.cancel();
        }
    }

    private void ImgHui(ImageView imageView) {
        ColorMatrix matrix = new ColorMatrix();
        matrix.setSaturation(0);//饱和度 0灰色 100过度彩色，50正常
        ColorMatrixColorFilter filter = new ColorMatrixColorFilter(matrix);
        imageView.setColorFilter(filter);
    }

    private void ImgLiang(ImageView imageView) {
        ColorMatrix matrix = new ColorMatrix();
        matrix.reset();
        ColorMatrixColorFilter filter = new ColorMatrixColorFilter(matrix);
        imageView.setColorFilter(filter);
    }

    private String switchName(int sign) {
        String switch_name = "";
        switch (sign) {
            case 0:
                switch_name = "guanjia";
                break;
            case 1:
                switch_name = "pcqq";
                break;
            case 2:
                switch_name = "mqq";
                break;
            case 3:
                switch_name = "xunzhang";
                break;
            case 4:
                switch_name = "qqmusic";
                break;
            case 5:
                switch_name = "qqgame";
                break;
            case 6:
                switch_name = "qqfk";
                break;
        }
        return switch_name;
    }

    private void Switchswitch(int sign) {
        String switch_name = switchName(sign);
        if (status[sign].equals("0")) {
            status[sign] = "1";
        } else if (status[sign].equals("1") || status[sign].equals("2")) {
            status[sign] = "0";
        }
        String post_url = MainActivity.web_jiekou + "api/submit" +
                ".php?act=switch&id=" + sid + "&uin=" + user + "&func=" + switch_name + "&star=" + status[sign];
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("type", "switch");
            HttpRequest http = new HttpRequest(post_url, jsonObject.toString(), handler);
            http.start();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void CheckSwithImg(ImageView[] imageViews, String[] status, TextView[] textView) {
        img_weishi.startAnimation(animation);
        img_load.startAnimation(animation);
        for (int i = 0; i < imageViews.length; i++) {
            imageViews[i].startAnimation(animation);
            switch (status[i]) {
                case "0":
                    ImgHui(imageViews[i]);
                    String word = "";
                    String word1 = textView[i].getText().toString();
                    for (int j = 0; j < word1.length() - 3; j++) {
                        word += word1.charAt(j);
                    }
                    word += "关闭]";
                    textView[i].setText(word);
                    break;
                case "1":
//                    ImgLiang(imageViews[i]);
                    word = "";
                    word1 = textView[i].getText().toString();
                    for (int j = 0; j < word1.length() - 3; j++) {
                        word += word1.charAt(j);
                    }
                    word += "开启]";
                    textView[i].setText(word);
                    break;
                case "2":
                    imageViews[i].setImageDrawable(getResources().getDrawable(R.mipmap
                            .htt_sjqq_android));

                    word = "";
                    word1 = textView[i].getText().toString();
                    for (int j = 0; j < word1.length() - 3; j++) {
                        word += word1.charAt(j);
                    }
                    word += "开启]";
                    textView[i].setText(word);
                    break;
            }
        }
    }

    private void openURL(String s) {
        Intent intent = new Intent();
        intent.setAction("android.intent.action.VIEW");
        Uri content_url = Uri.parse(s);
        intent.setData(content_url);
        startActivity(intent);
    }

    private void mirror_flip(final String status){
        sa1.setDuration(500);
        sa2.setDuration(500);
        sa1.setAnimationListener(new Animation.AnimationListener() {
            /**
             * 动画开始时调用该方法
             *
             * @Override
             */
            @Override
            public void onAnimationStart(Animation animation) {
            }

            /**
             * 动画重复时调用该方法
             *
             * @Override
             */
            @Override
            public void onAnimationRepeat(Animation animation) {
            }

            /**
             * 动画结束时调用该方法
             *
             * @Override
             */
            @Override
            public void onAnimationEnd(Animation animation) {
                if (status.equals("1")){
                    imageViews[2].setImageDrawable(getResources().getDrawable(R.mipmap
                            .htt_sjqq_android));
                } else {
                    imageViews[2].setImageDrawable(getResources().getDrawable(R.mipmap
                            .htt_sjqq));
                }
                imageViews[2].startAnimation(sa2);
            }
        });
        imageViews[2].startAnimation(sa1);
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
}
