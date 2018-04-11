package com.king.Fragment1;


import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.king.qqdaigua.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class BlankFragment1 extends Fragment {
    private View view;
    private ImageView image_touxiang;
    private RotateAnimation animation;
    private TextView tv_fail;
    private Button bt_zh, bt_login , bt_reg;

    public BlankFragment1() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.blank_fragment1, container, false);
        init();

        return view;
    }

    private void init() {
        bt_login = (Button) view.findViewById(R.id.bt_login);
        bt_reg = (Button) view.findViewById(R.id.bt_reg);
        bt_zh = (Button) view.findViewById(R.id.bt_zh);

        tv_fail = (TextView) view.findViewById(R.id.textView);
        tv_fail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new dialog1_admin().show(getFragmentManager(), "");
            }
        });
        image_touxiang = (ImageView) view.findViewById(R.id.image_touxiang);

        animation = new RotateAnimation(0,360, Animation.RELATIVE_TO_SELF,0.5f, Animation.RELATIVE_TO_SELF,0.5f);
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

}
