package com.king.Fragment2;


import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
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
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.king.qqdaigua.MainActivity;
import com.king.qqdaigua.R;
import com.king.util.HttpRequest;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * A simple {@link Fragment} subclass.
 */
public class YH_Fragment extends Fragment {
    //判断网络任务是否完成 上限次数
    public static int sign_dialog_cancel = 2;
    private int sign_dialog = 0;

    //第一个Fragment 登录成功传过来的 帐号密码值
    private String user, pwd;
    //0电脑管家-1电脑QQ-2手机QQ-3勋章墙-4QQ音乐-5QQ手游
    private String[] status = new String[6];

    private RotateAnimation animation, animation1;
    private ProgressDialog pgDialog;
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
    private ImageView[] imageViews = new ImageView[6];
    TextView[] textViews = new TextView[6];
    private TextView tv_guanjia;
    private TextView tv_xz;
    private TextView tv_mqq;
    private TextView tv_pcqq;
    private TextView tv_music;
    private TextView tv_game;


    public YH_Fragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        animation = new RotateAnimation(0, 360, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        animation.setDuration(1000);
        animation.setFillAfter(true);
        animation1 = new RotateAnimation(360, 0, Animation.RELATIVE_TO_SELF, 0.5f, Animation
                .RELATIVE_TO_SELF, 0.5f);
        animation1.setDuration(1000);
        animation1.setFillAfter(true);

        view = inflater.inflate(R.layout.fragment_yh, container, false);
        bindviews();

        setBoard();
        setBlack();
        return view;
    }

    private void setBlack() {
        String sid = preferences.getString("sid", "");
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
                    for (int i = 0, j = 0; i < 11; i += 2) {
                        status[j++] = data.charAt(i) + "";
                    }
                    CheckSwithImg(imageViews, status, textViews);
                    String dgtime = json.getString("dgtime");
                    int dgtime_int = Integer.parseInt(dgtime);
                    if (code.equals("0")) {
                        if (!black.equals("0")) {
                            tv_bt_jiechu.setVisibility(View.VISIBLE);
                            if (black == "1") {
                                tv_lhzt.setText("密码错误");
                            } else if (black == "2") {
                                tv_lhzt.setText("QQ冻结");
                            } else if (black == "3") {
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
            } else {
                Toast.makeText(getContext(), "网络请求错误", Toast.LENGTH_SHORT).show();
            }
        }
    };

    private void bindviews() {
        pgDialog = new ProgressDialog(getContext());
        pgDialog.setTitle("请稍等");
        pgDialog.setMessage("资源搜寻中，请稍等...");
        pgDialog.show();
        pgDialog.setCancelable(false);

        tv_game = (TextView) view.findViewById(R.id.tv_game);
        tv_music = (TextView) view.findViewById(R.id.tv_music);
        tv_pcqq = (TextView) view.findViewById(R.id.tv_pcqq);
        tv_mqq = (TextView) view.findViewById(R.id.tv_mqq);
        tv_xz = (TextView) view.findViewById(R.id.tv_xz);
        tv_guanjia = (TextView) view.findViewById(R.id.tv_guanjia);
        textViews[0] = tv_guanjia;
        textViews[1] = tv_pcqq;
        textViews[2] = tv_mqq;
        textViews[3] = tv_xz;
        textViews[4] = tv_music;
        textViews[5] = tv_game;
        img_game = (ImageView) view.findViewById(R.id.img_game);
        img_game.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Switchswitch(5);
            }
        });
        img_guanjia = (ImageView) view.findViewById(R.id.img_guanjia);
        img_guanjia.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Switchswitch(0);
            }
        });
        img_music = (ImageView) view.findViewById(R.id.img_music);
        img_music.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Switchswitch(4);
            }
        });
        img_pcqq = (ImageView) view.findViewById(R.id.img_pcqq);
        img_pcqq.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Switchswitch(1);
            }
        });
        img_mqq = (ImageView) view.findViewById(R.id.img_mqq);
        img_mqq.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Switchswitch(2);
            }
        });
        img_xz = (ImageView) view.findViewById(R.id.img_xz);
        img_xz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Switchswitch(3);
            }
        });
        TextView tv_xz = (TextView) view.findViewById(R.id.tv_xz);
        imageViews[0] = img_guanjia;
        imageViews[1] = img_pcqq;
        imageViews[2] = img_mqq;
        imageViews[3] = img_xz;
        imageViews[4] = img_music;
        imageViews[5] = img_game;
        tv_viplevel = (TextView) view.findViewById(R.id.tv_viplevel);
        bt_xufei = (Button) view.findViewById(R.id.bt_xufei);
        tv_bt_jiechu = (TextView) view.findViewById(R.id.tv_bt_jiechu);
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

    private void Switchswitch(int sign) {
        if (status[sign].equals("0")) {
            status[sign] = "1";
            String word = "";
            String word1 = textViews[sign].getText().toString();
            for (int j = 0; j < word1.length() - 3; j++) {
                word += word1.charAt(j);
            }
            word += "开启]";
            textViews[sign].setText(word);
            ImgLiang(imageViews[sign]);
            imageViews[sign].startAnimation(animation);
        } else if (status[sign].equals("1")) {
            status[sign] = "0";
            String word = "";
            String word1 = textViews[sign].getText().toString();
            for (int j = 0; j < word1.length() - 3; j++) {
                word += word1.charAt(j);
            }
            word += "关闭]";
            textViews[sign].setText(word);
            ImgHui(imageViews[sign]);
            imageViews[sign].startAnimation(animation1);
        }
    }

    private void CheckSwithImg(ImageView[] imageViews, String[] status, TextView[] textView) {
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
            }
        }
    }
}
